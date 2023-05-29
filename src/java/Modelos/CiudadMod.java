package Modelos;

import Dto.CiudadDTO;
import Genericos.ConexionDB;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
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

public class CiudadMod extends HttpServlet {

    private String query, ciudaddesc;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoCiudad;

    public CiudadMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoCiudad = bandera < 4 ? Integer.parseInt(request.getParameter("id_ciudad").trim()) : 0;
        ciudaddesc = bandera < 4 ? request.getParameter("ciudad").trim().toUpperCase() : "";

        System.out.println("ID " + codigoCiudad);
        System.out.println("Ciudad " + ciudaddesc);

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
                String filas = recuperarCiudades();
                if (filas != null) {
                    out.print(filas);
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String CiudadesID = obtenerCiudadesID(Integer.parseInt(request.getParameter("id_ciudad").trim()));
                //System.out.println("Ciudades: " + CiudadesID);

                if (CiudadesID != null) {
                    out.println(CiudadesID);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.ciudad(descripcion)  VALUES (?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, ciudaddesc);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CiudadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.ciudad WHERE id_ciudad=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoCiudad);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CiudadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.ciudad SET descripcion=? WHERE id_ciudad=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, ciudaddesc);
            ps.setInt(2, codigoCiudad);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CiudadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarCiudades() {
        String rows = "";
        try {
            query = "SELECT id_ciudad, descripcion FROM public.ciudad order by id_ciudad;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_ciudad") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(CiudadMod.class.getName()).log(Level.SEVERE, null, ex);
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

    private String obtenerCiudadesID(Integer id_ciudad) {
        CiudadDTO dto;
        ArrayList<CiudadDTO> lista;
        try {
            query = "SELECT id_ciudad, descripcion FROM public.ciudad WHERE id_ciudad=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_ciudad);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new CiudadDTO();
                dto.setId_ciudad(rs.getInt("id_ciudad"));
                dto.setCiudad(rs.getString("descripcion"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CiudadMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
