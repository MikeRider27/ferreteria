package Modelos;

import Dto.MarcaDTO;
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

public class MarcaMod extends HttpServlet {

    private String query, descripcion;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoM;

    public MarcaMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoM = bandera < 4 ? Integer.parseInt(request.getParameter("id_marca").trim()) : 0;
        descripcion = bandera < 4 ? request.getParameter("marca").trim().toUpperCase() : "";

        System.out.println("ID " + codigoM);
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
                String filas = recuperarMarcas();
                if (filas != null) {
                    out.print(filas);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String MarcaID = obtenerMarcaID(Integer.parseInt(request.getParameter("id_marca").trim()));
                //System.out.println("Marca: " + MarcaID);

                if (MarcaID != null) {
                    out.println(MarcaID);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.marca(descripcion) VALUES (?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.marca WHERE id_marca=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoM);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.marca SET descripcion=? WHERE id_marca=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setInt(2, codigoM);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarMarcas() {
        String rows = "";
        try {
            query = "SELECT id_marca, descripcion FROM public.marca order by id_marca;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_marca") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaMod.class.getName()).log(Level.SEVERE, null, ex);
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

    private String obtenerMarcaID(Integer id_marca) {
        MarcaDTO dto;
        ArrayList<MarcaDTO> lista;
        try {
            query = "SELECT id_marca, descripcion FROM public.marca WHERE id_marca=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_marca);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new MarcaDTO();
                dto.setId_marca(rs.getInt("id_marca"));
                dto.setMarca(rs.getString("descripcion"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}