package Modelos;

import Dto.DepositoDTO;
import Genericos.ConexionDB;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DepositoMod extends HttpServlet {

    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, id_sucursal, id_deposito;
    private String descripcion, query;

    public DepositoMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera").trim());
        id_deposito = bandera < 4 ? Integer.parseInt(request.getParameter("id_deposito").trim()) : 0;
        id_sucursal = bandera < 4 ? Integer.parseInt(request.getParameter("id_sucursal").trim()) : 0;
        descripcion = bandera < 4 ? request.getParameter("deposito").trim().toUpperCase() : "";

        switch (bandera) {
            case 1:
                Alta();
                break;
            case 2:
                Modificacion();
                break;
            case 3:
                Baja();
                break;
            case 4:
                String datos = TablaDeposito();
                out.println((datos != null ? datos : null));
                out.close();
                break;
            case 5:
                String suc = obtenerSucursal();
                if (suc != null) {
                    out.println(suc);
                    out.close();
                }
                break;
            case 6:
                response.setContentType("application/json, charset=UTF-8");
                String deposito = obtenerDepositoID(Integer.parseInt(request.getParameter("id_deposito").trim()));
                if (deposito != null) {
                    out.println(deposito);
                    out.close();
                }
                break;
//            case 7:
//                String dep = DepositoHTML();
//                if (dep != null) {
//                    out.print(dep);
//                    out.close();
//                }
//                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.deposito(sucursal_id_sucursal, descripcion) VALUES (?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_sucursal);
            ps.setString(2, descripcion);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DepositoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.deposito WHERE id_deposito=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_deposito);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DepositoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.deposito SET sucursal_id_sucursal=?, descripcion=? WHERE id_deposito=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_sucursal);
            ps.setString(2, descripcion);
            ps.setInt(3, id_deposito);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DepositoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String TablaDeposito() {
        String rows = "";
        try {
            query = "SELECT \"ID\", \"DEPOSITO\", \"SUCURSAL\" FROM public.vista_deposito ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td  align=\"center\">" + rs.getInt("ID") + "</td>";
                rows += "<td>" + rs.getString("DEPOSITO") + "</td>";
                rows += "<td>" + rs.getString("SUCURSAL") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(DepositoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerSucursal() {
        try {
            query = "SELECT id_sucursal, descripcion FROM public.sucursal ORDER BY id_sucursal;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_sucursal") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(DepositoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerDepositoID(Integer id_deposito) {
        DepositoDTO dto;
        ArrayList<DepositoDTO> lista;
        try {
            query = "SELECT dep.id_deposito, dep.descripcion, dep.sucursal_id_sucursal, suc.descripcion as sucursal FROM deposito dep "
                    + "INNER JOIN sucursal suc ON dep.sucursal_id_sucursal = suc.id_sucursal WHERE id_deposito=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_deposito);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new DepositoDTO();
                dto.setId_deposito(rs.getInt("id_deposito"));
                dto.setDeposito(rs.getString("descripcion"));
                dto.setId_sucursal(rs.getInt("sucursal_id_sucursal"));
                dto.setSucursal(rs.getString("sucursal"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepositoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    private String DepositoHTML() {
//        String rows = "";
//        String seleccion;
//        try {
//            query = "SELECT \"ID\", \"DEPOSITO\", \"SUCURSAL\" FROM public.vista_deposito ORDER BY \"ID\";";
//            ps = ConexionDB.getDBcon().prepareStatement(query);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                seleccion = "<input id=" + rs.getInt("ID") + " class=\"w3-check w3-small opcion_seleccion_deposito\" type=\"checkbox\">";
//                rows += "<tr>";
//                rows += "<td  align=\"center\"> " + seleccion + "  </td> ";
//                rows += "<td>" + rs.getString("DEPOSITO") + "</td>";
//                rows += "<td>" + rs.getString("SUCURSAL") + "</td>";
//                rows += "</tr>";
//            }
//            return rows;
//        } catch (SQLException ex) {
//            Logger.getLogger(DepositoMod.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("Estamos en metodo Recuperacion - Select");
//        return null;
//    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}