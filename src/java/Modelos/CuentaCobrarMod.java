package Modelos;

import Dto.CuentaCobrarDTO;
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

public class CuentaCobrarMod extends HttpServlet {

    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, id_cc, id_venta, nro_cuota;
    private String importe_total, importe_cobrado, fecha, query;

    public CuentaCobrarMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        System.out.println("Llegamos al servlet");

        bandera = Integer.parseInt(request.getParameter("bandera").trim());
        id_cc = bandera < 4 ? Integer.parseInt(request.getParameter("id_cc").trim()) : 0;
        id_venta = bandera < 4 ? Integer.parseInt(request.getParameter("id_venta").trim()) : 0;
        importe_total = bandera < 4 ? request.getParameter("importe_total").trim() : "";
        importe_cobrado = bandera < 4 ? request.getParameter("importe_cobrado").trim() : "";
        nro_cuota = bandera < 4 ? Integer.parseInt(request.getParameter("nro_cuota").trim()) : 0;
        fecha = bandera < 4 ? request.getParameter("fecha").trim() : "";

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
                String rows = recuperarCC();
                if (rows != null) {
                    out.print(rows);
                    out.close();
                }
                break;
            case 5:
                String venta = obtenerVenta();
                System.out.println("Venta " + venta);
                if (venta != null) {
                    out.println(venta);
                    out.close();
                }
                break;
            case 6:
                response.setContentType("application/json, charset=UTF-8");
                String CCid = obtenerCCid(Integer.parseInt(request.getParameter("id_cc").trim()));
                System.out.println("Cuenta a Cobrar: " + CCid);

                if (CCid != null) {
                    out.println(CCid);
                    out.close();
                }
                break;
        }

    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.cuenta_cobrar(importe_total, importe_cobrado, nro_cuota, fecha, venta_id_venta) "
                    + "VALUES (?, ?, ?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, importe_total);
            ps.setString(2, importe_cobrado);
            ps.setInt(3, nro_cuota);
            ps.setString(4, fecha);
            ps.setInt(5, id_venta);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.cuenta_cobrar WHERE id_cc=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_cc);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.cuenta_cobrar SET importe_total=?, importe_cobrado=?, nro_cuota=?, fecha=?, "
                    + "venta_id_venta=? WHERE id_cc=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, importe_total);
            ps.setString(2, importe_cobrado);
            ps.setInt(3, nro_cuota);
            ps.setString(4, fecha);
            ps.setInt(5, id_venta);
            ps.setInt(6, id_cc);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    private String recuperarCC() {
        String rows = "";
        try {
            query = "SELECT id_cc, importe_total, importe_cobrado, nro_cuota, fecha, venta FROM public.vs_cuenta_cobrar "
                    + "ORDER BY id_ac=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_cc") + "</td>";
                rows += "<td>" + rs.getString("importe_total") + "</td>";
                rows += "<td>" + rs.getString("importe_cobrado") + "</td>";
                rows += "<td>" + rs.getInt("nro_cuota") + "</td>";
                rows += "<td>" + rs.getString("fecha") + "</td>";
                rows += "<td>" + rs.getString("venta") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }
    
    private String obtenerCCid(Integer id_cc) {
        CuentaCobrarDTO dto;
        ArrayList<CuentaCobrarDTO> lista;
        try {
            query = "SELECT id_cc, importe_total, importe_cobrado, nro_cuota, fecha, venta_id_venta "
                    + "FROM public.cuenta_cobrar WHERE id_cc=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_cc);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new CuentaCobrarDTO();
                dto.setImporte_total(rs.getString("importe_total"));
                dto.setImporte_cobrado(rs.getString("importe_cobrado"));
                dto.setNro_cuota(rs.getInt("nro_cuota"));
                dto.setFecha(rs.getString("fecha"));
                dto.setId_venta(rs.getInt("venta_id_venta"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerVenta() {
        try {
            query = "SELECT id_venta, tipo_venta FROM public.venta;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_venta") + ">" + rs.getString("tipo_venta") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
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
