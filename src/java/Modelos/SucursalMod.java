package Modelos;

import Dto.SucursalDTO;
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

public class SucursalMod extends HttpServlet {

    private String query, descripcion, telefono, direccion;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoSuc;

    public SucursalMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoSuc = bandera < 4 ? Integer.parseInt(request.getParameter("id_sucursal").trim()) : 0;
        descripcion = bandera < 4 ? request.getParameter("sucursal").trim() : "";
        telefono = bandera < 4 ? request.getParameter("telefono").trim() : "";
        direccion = bandera < 4 ? request.getParameter("direccion").trim() : "";

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
                String filas = recuperarSucursales();
                if (filas != null) {
                    out.print(filas);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String SucursalID = obtenerSucursalID(Integer.parseInt(request.getParameter("id_sucursal").trim()));
                if (SucursalID != null) {
                    out.println(SucursalID);
                    out.close();
                }
                break;
            case 6:
                String suc = SucursalHTML();
                if (suc != null) {
                    out.print(suc);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.sucursal(descripcion, telefono, direccion) VALUES (?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setString(2, telefono);
            ps.setString(3, direccion);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SucursalMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.sucursal WHERE id_sucursal=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoSuc);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SucursalMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.sucursal SET descripcion=?, telefono=?, direccion=? WHERE id_sucursal=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setString(2, telefono);
            ps.setString(3, direccion);
            ps.setInt(4, codigoSuc);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SucursalMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarSucursales() {
        String rows = "";
        try {
            query = "SELECT id_sucursal, descripcion, telefono, direccion FROM sucursal ORDER BY id_sucursal;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_sucursal") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "<td>" + rs.getString("telefono") + "</td>";
                rows += "<td>" + rs.getString("direccion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(SucursalMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String SucursalHTML() {
        String rows = "";
        String seleccion;
        try {
            query = "SELECT id_sucursal, descripcion, telefono, direccion FROM sucursal ORDER BY id_sucursal;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                seleccion = "<input id=" + rs.getInt("id_sucursal") + " class=\"w3-check w3-small opcion_seleccion_sucursal\" type=\"checkbox\">";
                rows += "<tr>";
                rows += "<td  align=\"center\"> " + seleccion + "  </td> ";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "<td>" + rs.getString("telefono") + "</td>";
                rows += "<td>" + rs.getString("direccion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(SucursalMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerSucursalID(Integer id_sucursal) {
        SucursalDTO dto;
        ArrayList<SucursalDTO> lista;
        try {
            query = "SELECT id_sucursal, descripcion, telefono, direccion FROM public.sucursal WHERE id_sucursal=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_sucursal);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new SucursalDTO();
                dto.setId_sucursal(rs.getInt("id_sucursal"));
                dto.setSucursal(rs.getString("descripcion"));
                dto.setTelefono(rs.getString("telefono"));
                dto.setDireccion(rs.getString("direccion"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }

        } catch (SQLException ex) {
            Logger.getLogger(SucursalMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

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
