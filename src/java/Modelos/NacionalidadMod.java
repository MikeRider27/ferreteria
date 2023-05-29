package Modelos;

import Dto.NacionalidadDTO;
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

public class NacionalidadMod extends HttpServlet {

    private String query, descripcion;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoN;

    public NacionalidadMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoN = bandera < 4 ? Integer.parseInt(request.getParameter("id_nacionalidad").trim()) : 0;
        descripcion = bandera < 4 ? request.getParameter("nacionalidad").trim().toUpperCase() : "";

        System.out.println("ID " + codigoN);
        System.out.println("Descripicion " + descripcion);

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
                String filas = recuperarNacionalidades();
                if (filas != null) {
                    out.print(filas);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String NacID = obtenerNacID(Integer.parseInt(request.getParameter("id_nacionalidad").trim()));
                //System.out.println("Nacionalidad: " + NacID);

                if (NacID != null) {
                    out.println(NacID);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.nacionalidad(descripcion) VALUES (?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(NacionalidadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.nacionalidad WHERE id_nacionalidad=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoN);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(NacionalidadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.nacionalidad SET descripcion=? WHERE id_nacionalidad=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setInt(2, codigoN);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(NacionalidadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarNacionalidades() {
        String rows = "";
        try {
            query = "SELECT id_nacionalidad, descripcion FROM public.nacionalidad order by id_nacionalidad;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_nacionalidad") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(NacionalidadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
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

    private String obtenerNacID(Integer id_nacionalidad) {
        NacionalidadDTO dto;
        ArrayList<NacionalidadDTO> lista;
        try {
            query = "SELECT id_nacionalidad, descripcion FROM public.nacionalidad WHERE id_nacionalidad=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_nacionalidad);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new NacionalidadDTO();
                dto.setId_nacionalidad(rs.getInt("id_nacionalidad"));
                dto.setNacionalidad(rs.getString("descripcion"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NacionalidadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
