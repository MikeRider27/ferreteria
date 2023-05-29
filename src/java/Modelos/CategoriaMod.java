package Modelos;

import Dto.CategoriaDTO;
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

public class CategoriaMod extends HttpServlet {

    private String query, categoriadescripcion;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoCategoria;

    public CategoriaMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoCategoria = bandera < 4 ? Integer.parseInt(request.getParameter("id_categoria").trim()) : 0;
        categoriadescripcion = bandera < 4 ? request.getParameter("categoria").trim() : "";

        System.out.println("ID " + codigoCategoria);
        System.out.println("Categoria " + categoriadescripcion);

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
                String filas = recuperarCategorias();
                if (filas != null) {
                    out.print(filas);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String CategoriaID = obtenerCategoriaID(Integer.parseInt(request.getParameter("id_categoria").trim()));
                //System.out.println("Categoria: " + CategoriaID);

                if (CategoriaID != null) {
                    out.println(CategoriaID);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.categoria(descripcion)  VALUES (?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, categoriadescripcion);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.categoria WHERE id_categoria=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoCategoria);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.categoria SET descripcion=? WHERE id_categoria=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, categoriadescripcion);
            ps.setInt(2, codigoCategoria);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarCategorias() {
        String rows = "";
        try {
            query = "SELECT id_categoria, descripcion FROM public.categoria order by id_categoria;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_categoria") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerCategoriaID(Integer id_categoria) {
        CategoriaDTO dto;
        ArrayList<CategoriaDTO> lista;
        try {
            query = "SELECT id_categoria, descripcion FROM public.categoria WHERE id_categoria=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_categoria);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new CategoriaDTO();
                dto.setId_categoria(rs.getInt("id_categoria"));
                dto.setCategoria(rs.getString("descripcion"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CategoriaMod.class.getName()).log(Level.SEVERE, null, ex);
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