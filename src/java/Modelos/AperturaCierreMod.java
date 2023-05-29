package Modelos;

import Dto.AperturaCierreDTO;
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

public class AperturaCierreMod extends HttpServlet {

    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, apertura, id_ca, id_us, monto1, monto2;
    private String fecha1, fecha2, hora1, hora2, query;

    public AperturaCierreMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera").trim());
        apertura = bandera < 4 ? Integer.parseInt(request.getParameter("apertura").trim()) : 0;
        id_ca = bandera < 4 ? Integer.parseInt(request.getParameter("ca").trim()) : 0;
        id_us = bandera < 4 ? Integer.parseInt(request.getParameter("us").trim()) : 0;
        fecha1 = bandera < 4 ? request.getParameter("fecha1").trim() : "";
        fecha2 = bandera < 4 ? request.getParameter("fecha2").trim() : "";
        hora1 = bandera < 4 ? request.getParameter("hora1").trim() : "";
        hora2 = bandera < 4 ? request.getParameter("hora2").trim() : "";
        monto1 = bandera < 4 ? Integer.parseInt(request.getParameter("monto1").trim()) : 0;
        monto2 = bandera < 4 ? Integer.parseInt(request.getParameter("monto2").trim()) : 0;

        switch (bandera) {
            case 1:
                Alta();
                break;

            case 4:
                String rows = tablaAC();
                if (rows != null) {
                    out.print(rows);
                    out.close();
                }
                break;
            case 5:
                String caja = obtenerCaja();
                //System.out.println("Caja " + caja);
                if (caja != null) {
                    out.println(caja);
                    out.close();
                }
                break;
            case 6:
                String usuario = obtenerUsuario();
                //System.out.println("Usuario " + usuario);
                if (usuario != null) {
                    out.println(usuario);
                    out.close();
                }
                break;
            case 7:
                response.setContentType("application/json, charset=UTF-8");
                String ACID = obtenerACID(Integer.parseInt(request.getParameter("apertura").trim()));
                if (ACID != null) {
                    out.println(ACID);
                    out.close();
                }
                break;
            case 8:
                String ac = acHTML();
                if (ac != null) {
                    out.print(ac);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.apertura_cierre(fecha_inicio, fecha_fin, hora_inicio, hora_fin, monto_inicial, monto_final, caja_id_caja, "
                    + "usuario_id_usuario)  VALUES (?, ?, ?, ?, ?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, fecha1);
            ps.setString(2, fecha2);
            ps.setString(2, hora1);
            ps.setString(3, hora2);
            ps.setInt(4, monto1);
            ps.setInt(5, monto2);
            ps.setInt(6, id_ca);
            ps.setInt(7, id_us);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AperturaCierreMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String tablaAC() {
        String rows = "";
        try {
            query = "SELECT \"ID\", \"CAJA\", \"USUARIO\", \"FECHA INICIO\", \"HORA INICIO\", \"FECHA FIN\", \"HORA FIN\", \"MONTO INICIAL\", "
                    + "\"MONTO FINAL\" FROM public.vista_apertura_cierre ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("ID") + "</td>";
                rows += "<td>" + rs.getString("CAJA") + "</td>";
                rows += "<td>" + rs.getString("USUARIO") + "</td>";
                rows += "<td>" + rs.getString("FECHA INICIO") + "</td>";
                rows += "<td>" + rs.getString("HORA INICIO") + "</td>";
                rows += "<td>" + rs.getString("FECHA FIN") + "</td>";
                rows += "<td>" + rs.getString("HORA FIN") + "</td>";
                rows += "<td>" + rs.getInt("MONTO INICIAL") + "</td>";
                rows += "<td>" + rs.getInt("MONTO FINAL") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(AperturaCierreMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerACID(Integer id_apertura) {
        AperturaCierreDTO dto;
        ArrayList<AperturaCierreDTO> lista;
        try {
            query = "SELECT id_ac, fecha_inicio, fecha_fin, hora_inicio, hora_fin, monto_inicial, monto_final, caja_id_caja, usuario_id_usuario "
                    + "FROM public.apertura_cierre WHERE id_ac=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_apertura);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new AperturaCierreDTO();
                dto.setApertura(rs.getInt("id_ac"));
                dto.setFecha_inicio(rs.getString("fecha_inicio"));
                dto.setFecha_fin(rs.getString("fecha_fin"));
                dto.setHora1(rs.getString("hora_inicio"));
                dto.setHora2(rs.getString("hora_fin"));
                dto.setMonto1(rs.getInt("monto_inicial"));
                dto.setMonto2(rs.getInt("monto_final"));
                dto.setCa(rs.getInt("caja_id_caja"));
                dto.setUs(rs.getInt("usuario_id_usuario"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AperturaCierreMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerCaja() {
        try {
            query = "SELECT id_caja, descripcion FROM public.caja ORDER BY id_caja;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_caja") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(AperturaCierreMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerUsuario() {
        try {
            query = "SELECT id_usuario, usuario FROM public.usuario ORDER BY id_usuario;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_usuario") + ">" + rs.getString("usuario") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(AperturaCierreMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String acHTML() {
        String rows = "";
        String seleccion;
        try {
            query = "SELECT \"ID\", \"CAJA\", \"USUARIO\", \"FECHA INICIO\", \"HORA INICIO\", \"FECHA FIN\", \"HORA FIN\", \"MONTO INICIAL\", "
                    + "\"MONTO FINAL\" FROM public.vista_apertura_cierre ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                seleccion = "<input id=" + rs.getInt("ID") + " class=\"w3-check w3-small opcion_seleccion_ac\" type=\"checkbox\">";
                rows += "<tr>";
                rows += "<td  align=\"center\"> " + seleccion + "  </td> ";
                rows += "<td>" + rs.getString("CAJA") + "</td>";
                rows += "<td>" + rs.getString("USUARIO") + "</td>";
                rows += "<td>" + rs.getString("FECHA INICIO") + "</td>";
                rows += "<td>" + rs.getString("HORA INICIO") + "</td>";
                rows += "<td>" + rs.getString("FECHA FIN") + "</td>";
                rows += "<td>" + rs.getString("HORA FIN") + "</td>";
                rows += "<td>" + rs.getInt("MONTO INICIAL") + "</td>";
                rows += "<td>" + rs.getInt("MONTO FINAL") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(AperturaCierreMod.class.getName()).log(Level.SEVERE, null, ex);
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

}
