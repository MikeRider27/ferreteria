package Modelos;

import Dto.CargoDTO;
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

public class cargoMod extends HttpServlet {

    private String query, cargodescripcion;
    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, codigoCargo;

    public cargoMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera"));
        codigoCargo = bandera < 4 ? Integer.parseInt(request.getParameter("id_cargo").trim()) : 0;
        cargodescripcion = bandera < 4 ? request.getParameter("cargo").trim() : "";

        System.out.println("ID " + codigoCargo);
        System.out.println("Cargo " + cargodescripcion);

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
                String filas = recuperarCargo();
                if (filas != null) {
                    out.print(filas);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String RegistroUnico = recuperarCargoId(Integer.parseInt(request.getParameter("id_cargo").trim()));//;
                //System.out.println("Json = " + RegistroUnico);

                if (RegistroUnico != null) {
                    out.println(RegistroUnico);
                    out.close();
                }
                break;
        }

    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.cargo(descripcion)  VALUES (?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, cargodescripcion);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(cargoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Alta - Insert INTO");
        return false;
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.cargo WHERE id_cargo=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, codigoCargo);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(cargoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Baja - Delete");
        return false;
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.cargo SET descripcion=? WHERE id_cargo=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, cargodescripcion);
            ps.setInt(2, codigoCargo);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(cargoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Modificacion - Update");
        return false;
    }

    private String recuperarCargo() {
        String rows = "";
        try {
            query = "SELECT id_cargo, descripcion FROM public.cargo order by id_cargo;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("id_cargo") + "</td>";
                rows += "<td>" + rs.getString("descripcion") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(cargoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String recuperarCargoId(Integer id_cargo) {
        CargoDTO item;
        ArrayList<CargoDTO> registroCargo;
        
        try {
            query = "SELECT id_cargo, descripcion FROM public.cargo WHERE id_cargo=?";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_cargo);
            rs = ps.executeQuery();
            registroCargo = new ArrayList<>();
            if (rs.next()) {
                item = new CargoDTO();
                item.setId_cargo(rs.getInt("id_cargo"));
                item.setCargo(rs.getString("descripcion"));
                registroCargo.add(item);
                return new Gson().toJson(registroCargo);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CargoDTO.class.getName()).log(Level.SEVERE, null, ex);
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