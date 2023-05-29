package Modelos;

import Dto.TipoDocumentoDTO;
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

public class TipoDocumentoMod extends HttpServlet {

    private String query, descripcion;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoDoc;

    public TipoDocumentoMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoDoc = bandera < 4 ? Integer.parseInt(request.getParameter("id_documento").trim()) : 0;
        descripcion = bandera < 4 ? request.getParameter("documento").trim() : "";

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
                String filas = recuperarTipoDoc();
                if (filas != null) {
                    out.print(filas);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String DocID = obtenerDocID(Integer.parseInt(request.getParameter("id_documento").trim()));
                //System.out.println("Tipo De Documento: " + DocID);

                if (DocID != null) {
                    out.println(DocID);
                    out.close();
                }
                break;
            case 6:
                String doc = cargarDoc();
                if (doc != null) {
                    out.print(doc);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.tipo_documento(descripcion) VALUES (?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoDocumentoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.tipo_documento WHERE id_documento=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoDoc);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoDocumentoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.tipo_documento SET descripcion=? WHERE id_documento=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, descripcion);
            ps.setInt(2, codigoDoc);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoDocumentoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarTipoDoc() {
        String rows = "";
        try {
            query = "SELECT id_documento, descripcion FROM public.tipo_documento order by id_documento";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_documento") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(TipoDocumentoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerDocID(Integer id_documento) {
        TipoDocumentoDTO dto;
        ArrayList<TipoDocumentoDTO> lista;
        try {
            query = "SELECT id_documento, descripcion FROM public.tipo_documento WHERE id_documento=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_documento);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new TipoDocumentoDTO();
                dto.setId_documento(rs.getInt("id_documento"));
                dto.setDocumento(rs.getString("descripcion"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoDocumentoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String cargarDoc() {
        try {
            query = "SELECT id_documento, descripcion FROM public.tipo_documento ORDER BY id_documento;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_documento") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(TipoDocumentoMod.class.getName()).log(Level.SEVERE, null, ex);
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
