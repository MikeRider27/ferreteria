package Modelos;

import Dto.EmpleadoDTO;
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

public class EmpleadoMod extends HttpServlet {

    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, id_empleado, id_cargo, id_caja, id_nacionalidad, id_ciudad;
    private String nom_empleado, ape_empleado, cedula, direccion, telefono, query;

    public EmpleadoMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera").trim());
        id_empleado = bandera < 4 ? Integer.parseInt(request.getParameter("id_empleado").trim()) : 0;
        id_nacionalidad = bandera < 4 ? Integer.parseInt(request.getParameter("id_nacionalidad").trim()) : 0;
        id_ciudad = bandera < 4 ? Integer.parseInt(request.getParameter("id_ciudad").trim()) : 0;
        id_cargo = bandera < 4 ? Integer.parseInt(request.getParameter("id_cargo").trim()) : 0;
        id_caja = bandera < 4 ? Integer.parseInt(request.getParameter("id_caja").trim()) : 0;
        nom_empleado = bandera < 4 ? request.getParameter("nom_empleado").trim().toUpperCase() : "";
        ape_empleado = bandera < 4 ? request.getParameter("ape_empleado").trim().toUpperCase() : "";
        cedula = bandera < 4 ? request.getParameter("cedula").trim() : "";
        direccion = bandera < 4 ? request.getParameter("direccion").trim().toUpperCase() : "";
        telefono = bandera < 4 ? request.getParameter("telefono").trim() : "";

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
                response.setContentType("application/json, charset=UTF-8");
                String empleadoID = obtenerEmpleadoID(Integer.parseInt(request.getParameter("id_empleado").trim()));
                if (empleadoID != null) {
                    out.println(empleadoID);
                    out.close();
                }
                break;
            case 5:
                String rows = recuperarEmpleadoHTML();
                if (rows != null) {
                    out.print(rows);
                    out.close();
                }
                break;
            case 6:
                String caja = obtenerCaja();
                if (caja != null) {
                    out.println(caja);
                    out.close();
                }
                break;
            case 7:
                String cargo = obtenerCargo();
                if (cargo != null) {
                    out.println(cargo);
                    out.close();
                }
                break;
            case 8:
                String nacionalidad = obtenerNacionalidad();
                if (nacionalidad != null) {
                    out.println(nacionalidad);
                    out.close();
                }
                break;
            case 9:
                String ciudad = obtenerCiudad();
                if (ciudad != null) {
                    out.println(ciudad);
                    out.close();
                }
                break;
            case 10:
                String emp = recuperarEmpleado();
                if (emp != null) {
                    out.print(emp);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.empleado(caja_id_caja, cargo_id_cargo, nacionalidad_id_nacionalidad, ciudad_id_ciudad, nombres, apellidos, "
                    + "cedula, direccion, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_caja);
            ps.setInt(2, id_cargo);
            ps.setInt(3, id_nacionalidad);
            ps.setInt(4, id_ciudad);
            ps.setString(5, nom_empleado);
            ps.setString(6, ape_empleado);
            ps.setString(7, cedula);
            ps.setString(8, direccion);
            ps.setString(9, telefono);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.empleado WHERE id_empleado=?";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_empleado);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.empleado SET nombres=?, apellidos=?, cedula=?, direccion=?, telefono=?, caja_id_caja=?, "
                    + "cargo_id_cargo=?, nacionalidad_id_nacionalidad=?, ciudad_id_ciudad=? WHERE id_empleado=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, nom_empleado);
            ps.setString(2, ape_empleado);
            ps.setString(3, cedula);
            ps.setString(4, direccion);
            ps.setString(5, telefono);
            ps.setInt(6, id_caja);
            ps.setInt(7, id_cargo);
            ps.setInt(8, id_nacionalidad);
            ps.setInt(9, id_ciudad);
            ps.setInt(10, id_empleado);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String recuperarEmpleadoHTML() {
        String rows = "";
        String seleccion;
        try {
            query = "SELECT \"ID\", \"CAJA\", \"CARGO\", \"NACIONALIDAD\", \"CIUDAD\", \"EMPLEADO/A\", \n"
                    + "       \"CEDULA\", \"DIRECCION\", \"TELEFONO\"\n"
                    + "  FROM public.vista_empleado ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                seleccion = "<input id=" + rs.getInt("ID") + " class=\"w3-check w3-small opcion_seleccion_empleado\" type=\"checkbox\">";
                rows += "<tr>";
                rows += "<td  align=\"center\"> " + seleccion + "  </td> ";
                rows += "<td>" + rs.getString("CAJA") + "</td>";
                rows += "<td>" + rs.getString("CARGO") + "</td>";
                rows += "<td>" + rs.getString("NACIONALIDAD") + "</td>";
                rows += "<td>" + rs.getString("CIUDAD") + "</td>";
                rows += "<td>" + rs.getString("EMPLEADO/A") + "</td>";
                rows += "<td>" + rs.getString("CEDULA") + "</td>";
                rows += "<td>" + rs.getString("DIRECCION") + "</td>";
                rows += "<td>" + rs.getString("TELEFONO") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String recuperarEmpleado() {
        String rows = "";
        try {
            query = "SELECT \"ID\", \"CAJA\", \"CARGO\", \"NACIONALIDAD\", \"CIUDAD\", \"EMPLEADO/A\", \n"
                    + "       \"CEDULA\", \"DIRECCION\", \"TELEFONO\"\n"
                    + "  FROM public.vista_empleado ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("ID") + "  </td> ";
                rows += "<td>" + rs.getString("CAJA") + "</td>";
                rows += "<td>" + rs.getString("CARGO") + "</td>";
                rows += "<td>" + rs.getString("NACIONALIDAD") + "</td>";
                rows += "<td>" + rs.getString("CIUDAD") + "</td>";
                rows += "<td>" + rs.getString("EMPLEADO/A") + "</td>";
                rows += "<td>" + rs.getString("CEDULA") + "</td>";
                rows += "<td>" + rs.getString("DIRECCION") + "</td>";
                rows += "<td>" + rs.getString("TELEFONO") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerEmpleadoID(Integer id_empleado) {
        EmpleadoDTO dto;
        ArrayList<EmpleadoDTO> lista;
        try {
            query = "SELECT id_empleado, nombres, apellidos, cedula, direccion, telefono, caja_id_caja, cargo_id_cargo, "
                    + "nacionalidad_id_nacionalidad, ciudad_id_ciudad FROM public.empleado WHERE id_empleado=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_empleado);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new EmpleadoDTO();
                dto.setId_empleado(rs.getInt("id_empleado"));
                dto.setNom_empleado(rs.getString("nombres"));
                dto.setApe_empleado(rs.getString("apellidos"));
                dto.setCedula(rs.getString("cedula"));;
                dto.setDireccion(rs.getString("direccion"));
                dto.setTelefono(rs.getString("telefono"));
                dto.setId_caja(rs.getInt("caja_id_caja"));
                dto.setId_cargo(rs.getInt("cargo_id_cargo"));
                dto.setId_nacionalidad(rs.getInt("nacionalidad_id_nacionalidad"));
                dto.setId_ciudad(rs.getInt("ciudad_id_ciudad"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerCargo() {
        try {
            query = "SELECT id_cargo, descripcion FROM public.cargo ORDER BY id_cargo;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_cargo") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerNacionalidad() {
        try {
            query = "SELECT id_nacionalidad, descripcion FROM public.nacionalidad ORDER BY id_nacionalidad;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_nacionalidad") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerCiudad() {
        try {
            query = "SELECT id_ciudad, descripcion FROM public.ciudad ORDER BY id_ciudad;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_ciudad") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoMod.class.getName()).log(Level.SEVERE, null, ex);
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
