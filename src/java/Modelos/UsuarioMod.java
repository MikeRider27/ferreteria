package Modelos;

import Dto.UsuarioDTO;
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

public class UsuarioMod extends HttpServlet {

    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, id_usuario, id_perfil, id_caja, id_empleado;
    private String usuario, clave, query;

    public UsuarioMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // System.out.println("Llegamos al servlet");
        bandera = Integer.parseInt(request.getParameter("bandera").trim());

        id_usuario = bandera < 4 ? Integer.parseInt(request.getParameter("id_usuario").trim()) : 0;
        usuario = bandera < 4 ? request.getParameter("usuario").trim() : "";
        clave = bandera < 4 ? request.getParameter("clave").trim() : "";
        id_perfil = bandera < 4 ? Integer.parseInt(request.getParameter("id_perfil").trim()) : 0;
        id_caja = bandera < 4 ? Integer.parseInt(request.getParameter("id_caja").trim()) : 0;
        id_empleado = bandera < 4 ? Integer.parseInt(request.getParameter("id_empleado").trim()) : 0;

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
                String datos = CargarUsuario();
                out.println((datos != null ? datos : null));
                out.close();
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String usuarioUnico = obtenerUsuarioSegunID(Integer.parseInt(request.getParameter("id_usuario").trim()));
                if (usuarioUnico != null) {
                    out.println(usuarioUnico);
                    out.close();
                }
                break;
            case 6:
                String perfil = obtenerPerfil();
                if (perfil != null) {
                    out.println(perfil);
                    out.close();
                }
                break;
            case 7:
                String caja = obtenerCaja();
                if (caja != null) {
                    out.println(caja);
                    out.close();
                }
                break;
            case 8:
                String empleado = obtenerEmpleado();
                if (empleado != null) {
                    out.println(empleado);
                    out.close();
                }
                break;
            case 9:
                String us = UsuarioHTML();
                if (us != null) {
                    out.print(us);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.usuario(usuario, clave, perfil_id_perfil, caja_id_caja, empleado_id_empleado) "
                    + "VALUES (?, ?, ?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, usuario);
            ps.setString(2, clave);
            ps.setInt(3, id_perfil);
            ps.setInt(4, id_caja);
            ps.setInt(5, id_empleado);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.usuario WHERE id_usuario=?";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_usuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.usuario SET usuario=?, clave=?, empleado_id_empleado=?, perfil_id_perfil=?, caja_id_caja=? "
                    + "WHERE id_usuario=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, usuario);
            ps.setString(2, clave);
            ps.setInt(3, id_empleado);
            ps.setInt(4, id_perfil);
            ps.setInt(5, id_caja);
            ps.setInt(6, id_usuario);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String CargarUsuario() {
        String rows = "";
        try {
            query = "SELECT \"ID\", \"USUARIO\", \"PERFIL\", \"EMPLEADO/A\", \"CAJA\" FROM public.vista_usuario ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td  align=\"center\">" + rs.getInt("ID") + "</td>";
                rows += "<td>" + rs.getString("USUARIO") + "</td>";
                rows += "<td>" + rs.getString("PERFIL") + "</td>";
                rows += "<td>" + rs.getString("EMPLEADO/A") + "</td>";
                rows += "<td>" + rs.getString("CAJA") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerPerfil() {
        try {
            query = "SELECT id_perfil, descripcion FROM public.perfil ORDER BY id_perfil;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_perfil") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerEmpleado() {
        try {
            query = "SELECT id_empleado, empleado FROM public.empleado ORDER BY id_empleado;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_empleado") + ">" + rs.getString("empleado") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerUsuarioSegunID(Integer id_usuario) {
        UsuarioDTO dto;
        ArrayList<UsuarioDTO> lista;
        try {
            query = "SELECT u.id_usuario, u.usuario, u.clave, u.empleado_id_empleado, u.perfil_id_perfil, u.caja_id_caja, "
                    + "concat((em.nombres::text || ' '::text) || em.apellidos::text) AS \"EMPLEADO/A\" FROM public.usuario u "
                    + "INNER JOIN empleado em ON u.empleado_id_empleado = em.id_empleado WHERE id_usuario=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_usuario);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new UsuarioDTO();
                dto.setId_usuario(rs.getInt("id_usuario"));
                dto.setUsuario(rs.getString("usuario"));
                dto.setClave(rs.getString("clave"));
                dto.setId_empleado(rs.getInt("empleado_id_empleado"));
                dto.setEmpleado(rs.getString("EMPLEADO/A"));
                dto.setId_perfil(rs.getInt("perfil_id_perfil"));
                dto.setId_caja(rs.getInt("caja_id_caja"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String UsuarioHTML() {
        String rows = "";
        String seleccion;
        try {
            query = "SELECT \"ID\", \"USUARIO\", \"PERFIL\", \"EMPLEADO/A\", \"CAJA\" FROM public.vista_usuario ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                seleccion = "<input id=" + rs.getInt("ID") + " class=\"w3-check w3-small opcion_seleccion_usuario\" type=\"checkbox\">";
                rows += "<tr>";
                rows += "<td  align=\"center\"> " + seleccion + "  </td> ";
                rows += "<td>" + rs.getString("USUARIO") + "</td>";
                rows += "<td>" + rs.getString("PERFIL") + "</td>";
                rows += "<td>" + rs.getString("EMPLEADO/A") + "</td>";
                rows += "<td>" + rs.getString("CAJA") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioMod.class.getName()).log(Level.SEVERE, null, ex);
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
