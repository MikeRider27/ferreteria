package Modelos;

import Dto.ClienteDTO;
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

public class ClienteMod extends HttpServlet {

    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, id_cliente, id_nacionalidad, id_ciudad;
    private String nom_cliente, ape_cliente, cedula, ruc, direccion, telefono, query;

    public ClienteMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera").trim());
        id_cliente = bandera < 4 ? Integer.parseInt(request.getParameter("id_cliente").trim()) : 0;
        id_nacionalidad = bandera < 4 ? Integer.parseInt(request.getParameter("id_nacionalidad").trim()) : 0;
        id_ciudad = bandera < 4 ? Integer.parseInt(request.getParameter("id_ciudad").trim()) : 0;
        nom_cliente = bandera < 4 ? request.getParameter("nom_cliente").trim().toUpperCase() : "";
        ape_cliente = bandera < 4 ? request.getParameter("ape_cliente").trim().toUpperCase() : "";
        cedula = bandera < 4 ? request.getParameter("cedula").trim() : "";
        ruc = bandera < 4 ? request.getParameter("ruc").trim() : "";
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
                String rows = recuperarCliente();
                if (rows != null) {
                    out.print(rows);
                    out.close();
                }
                break;
            case 5:
                String nacionalidad = obtenerNacionalidad();
                System.out.println("Nac " + nacionalidad);
                if (nacionalidad != null) {
                    out.println(nacionalidad);
                    out.close();
                }
                break;
            case 6:
                String ciudad = obtenerCiudad();
                System.out.println("Ciudad " + ciudad);

                if (ciudad != null) {
                    out.println(ciudad);
                    out.close();
                }
                break;
            case 7:
                response.setContentType("application/json, charset=UTF-8");
                String clienteID = obtenerClienteID(Integer.parseInt(request.getParameter("id_cliente").trim()));
                //System.out.println("Cliente: " + clienteID);

                if (clienteID != null) {
                    out.println(clienteID);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.cliente(nombres, apellidos, cedula, ruc, direccion, telefono, nacionalidad_id_nacionalidad, ciudad_id_ciudad) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, nom_cliente);
            ps.setString(2, ape_cliente);
            ps.setString(3, cedula);
            ps.setString(4, ruc);
            ps.setString(5, direccion);
            ps.setString(6, telefono);
            ps.setInt(7, id_nacionalidad);
            ps.setInt(8, id_ciudad);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.cliente WHERE id_cliente=?";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_cliente);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.cliente SET nombres=?, apellidos=?, cedula=?, ruc=?, direccion=?, telefono=?, nacionalidad_id_nacionalidad=?, "
                    + "ciudad_id_ciudad=? WHERE id_cliente=?";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, nom_cliente);
            ps.setString(2, ape_cliente);
            ps.setString(3, cedula);
            ps.setString(4, ruc);
            ps.setString(5, direccion);
            ps.setString(6, telefono);
            ps.setInt(7, id_nacionalidad);
            ps.setInt(8, id_ciudad);
            ps.setInt(9, id_cliente);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String recuperarCliente() {
        String rows = "";
        try {
            query = "SELECT \"ID\", \"NACIONALIDAD\", \"CIUDAD\", \"CLIENTE\", \"CEDULA\", \"RUC\", \"DIRECCION\", \"TELEFONO\" "
                    + "FROM public.vista_cliente ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("ID") + "</td>";
                rows += "<td>" + rs.getString("NACIONALIDAD") + "</td>";
                rows += "<td>" + rs.getString("CIUDAD") + "</td>";
                rows += "<td>" + rs.getString("CLIENTE") + "</td>";
                rows += "<td>" + rs.getString("CEDULA") + "</td>";
                rows += "<td>" + rs.getString("RUC") + "</td>";
                rows += "<td>" + rs.getString("DIRECCION") + "</td>";
                rows += "<td>" + rs.getString("TELEFONO") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerClienteID(Integer id_cliente) {
        ClienteDTO dto;
        ArrayList<ClienteDTO> lista;
        try {
            query = "SELECT id_cliente, nombres, apellidos, cedula, ruc, direccion, telefono, nacionalidad_id_nacionalidad, ciudad_id_ciudad "
                    + "FROM public.cliente WHERE id_cliente=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_cliente);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new ClienteDTO();
                dto.setId_cliente(rs.getInt("id_cliente"));
                dto.setNom_cliente(rs.getString("nombres"));
                dto.setApe_cliente(rs.getString("apellidos"));
                dto.setCedula(rs.getString("cedula"));
                dto.setRuc(rs.getString("ruc"));
                dto.setDireccion(rs.getString("direccion"));
                dto.setTelefono(rs.getString("telefono"));
                dto.setId_nacionalidad(rs.getInt("nacionalidad_id_nacionalidad"));
                dto.setId_ciudad(rs.getInt("ciudad_id_ciudad"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ClienteMod.class.getName()).log(Level.SEVERE, null, ex);
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
