package Modelos;

import Dto.PerfilDTO;
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

public class MantenerPerfilesMod extends HttpServlet {

    private String query, nombrePerfil;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoPerfil;

    public MantenerPerfilesMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoPerfil = bandera < 4 ? Integer.parseInt(request.getParameter("id_perfil").trim()) : 0;
        nombrePerfil = bandera < 4 ? request.getParameter("perfil").trim().toUpperCase() : "";

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
                String filas = recuperarPerfiles();
                if (filas != null) {
                    out.print(filas);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String PerfilID = obtenerPerfilID(Integer.parseInt(request.getParameter("id_perfil").trim()));
                //System.out.println("Perfil: " + PerfilID);

                if (PerfilID != null) {
                    out.println(PerfilID);
                    out.close();
                }
                break;
        }

    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.perfil(descripcion) VALUES (?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, nombrePerfil);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MantenerPerfilesMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.perfil WHERE id_perfil=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoPerfil);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MantenerPerfilesMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.perfil SET descripcion=? WHERE id_perfil=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, nombrePerfil);
            ps.setInt(2, codigoPerfil);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MantenerPerfilesMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private String recuperarPerfiles() {
        String rows = "";
        try {
            query = "SELECT id_perfil, descripcion FROM public.perfil order by id_perfil;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_perfil") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(MantenerPerfilesMod.class.getName()).log(Level.SEVERE, null, ex);
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

    private String obtenerPerfilID(Integer id_perfil) {
        PerfilDTO dto;
        ArrayList<PerfilDTO> lista;
        try {
            query = "SELECT id_perfil, descripcion FROM public.perfil WHERE id_perfil=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_perfil);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new PerfilDTO();
                dto.setId_perfil(rs.getInt("id_perfil"));
                dto.setPerfil(rs.getString("descripcion"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MantenerPerfilesMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
