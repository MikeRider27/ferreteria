package Modelos;

import Dto.CondicionVentaDTO;
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

public class CondicionVentaMod extends HttpServlet {

    private String query, descripcion, plazo;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoCV;

    public CondicionVentaMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoCV = bandera < 4 ? Integer.parseInt(request.getParameter("id_cv").trim()) : 0;
        descripcion = bandera < 4 ? request.getParameter("descripcion").trim().toUpperCase() : "";
        plazo = bandera < 4 ? request.getParameter("plazo").trim().toUpperCase() : "";

        System.out.println("ID " + codigoCV);
        System.out.println("Condicion de Venta " + descripcion);
        System.out.println("Plazo " + plazo);

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
                String filas = recuperarCV();
                if (filas != null) {
                    out.print(filas);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String cvID = obtenercvID(Integer.parseInt(request.getParameter("id_cv").trim()));
                if (cvID != null) {
                    out.println(cvID);
                    out.close();
                }
                break;
            case 6:
                String cv = cargarCV();
                if (cv != null) {
                    out.print(cv);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.condicion_venta(descripcion, plazo) VALUES (?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setString(2, plazo);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CondicionVentaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.condicion_venta WHERE id_cv=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoCV);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CondicionVentaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.condicion_venta SET descripcion=?, plazo=? WHERE id_cv=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setString(2, plazo);
            ps.setInt(3, codigoCV);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CondicionVentaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarCV() {
        String rows = "";
        try {
            query = "SELECT id_cv, descripcion, plazo FROM public.condicion_venta order by id_cv;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_cv") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "<td>" + rs.getString("plazo") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(CondicionVentaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenercvID(Integer id_cv) {
        CondicionVentaDTO dto;
        ArrayList<CondicionVentaDTO> lista;
        try {
            query = "SELECT id_cv, descripcion, plazo FROM public.condicion_venta WHERE id_cv=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_cv);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new CondicionVentaDTO();
                dto.setId_cv(rs.getInt("id_cv"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setPlazo(rs.getString("plazo"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CondicionVentaMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String cargarCV() {
        try {
            query = "SELECT id_cv, descripcion, plazo FROM public.condicion_venta ORDER BY id_cv;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_cv") + ">" + rs.getString("descripcion") + " => " + rs.getString("plazo") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(CondicionVentaMod.class.getName()).log(Level.SEVERE, null, ex);
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
