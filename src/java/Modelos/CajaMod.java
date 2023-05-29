package Modelos;

import Dto.CajaDTO;
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

public class CajaMod extends HttpServlet {

    private String query, cajadescripcion;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoCaja;

    public CajaMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoCaja = bandera < 4 ? Integer.parseInt(request.getParameter("id_caja").trim()) : 0;
        cajadescripcion = bandera < 4 ? request.getParameter("caja").trim() : "";

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
                String rows = recuperarCaja();
                if (rows != null) {
                    out.print(rows);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String RegistroUnico = recuperarCajaId(Integer.parseInt(request.getParameter("id_caja").trim()));//;
                //System.out.println("Json = " + RegistroUnico);

                if (RegistroUnico != null) {
                    out.println(RegistroUnico);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.caja(descripcion)  VALUES (?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, cajadescripcion);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CajaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.caja WHERE id_caja=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoCaja);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CajaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.caja SET descripcion=? WHERE id_caja=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, cajadescripcion);
            ps.setInt(2, codigoCaja);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CajaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarCaja() {
        String rows = "";
        try {
            query = "SELECT id_caja, descripcion FROM public.caja order by id_caja;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_caja") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(CajaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String recuperarCajaId(Integer id_caja) {
        CajaDTO item;
        ArrayList<CajaDTO> registroCaja;

        try {
            query = "SELECT id_caja, descripcion FROM public.caja WHERE id_caja=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_caja);
            rs = ps.executeQuery();
            registroCaja = new ArrayList<>();
            if (rs.next()) {
                item = new CajaDTO();
                item.setId_caja(rs.getInt("id_caja"));
                item.setCaja(rs.getString("descripcion"));
                registroCaja.add(item);
                return new Gson().toJson(registroCaja);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CajaMod.class.getName()).log(Level.SEVERE, null, ex);
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
