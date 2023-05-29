package Modelos;

import Genericos.ConexionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class permisosUsuariosMod extends HttpServlet {

    private String query, usuario, clave;
    private ResultSet rs;
    private PreparedStatement ps;

    public permisosUsuariosMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bloqueAdministracion;
        String bloqueReferenciales;
        String bloqueMovimiento;
        String bloqueInformes;
        String bloqueAyuda;

        bloqueAdministracion = "            <div class=\"w3-dropdown-hover\">\n"
                + "                <button class=\"w3-button\">Administracion</button>\n"
                + "                <div class=\"w3-dropdown-content w3-bar-block w3-card-2\">\n"
                + "                    <a href=\"Cliente.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Clientes</a>\n"
                + "                    <a href=\"Empleado.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Empleados</a>\n"
                + "                    <a href=\"sucursal.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Sucursales</a>\n"
                //+ "                    <a href=\"Deposito.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Depositos</a>\n"
                + "                    <a href=\"Usuario.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Usuarios</a>\n"
                + "                </div>\n"
                + "            </div>";

        bloqueReferenciales = "            <div class=\"w3-dropdown-hover\">\n"
                + "                <button class=\"w3-button\">Referenciales</button>\n"
                + "                <div class=\"w3-dropdown-content w3-bar-block w3-card-2\">\n"
                + "                    <a href=\"caja.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Caja</a>\n"
                + "                    <a href=\"cargo.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Cargos</a>\n"
                + "                    <a href=\"categoria.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Categorias</a>\n"
                + "                    <a href=\"ciudad.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Ciudades</a>\n"
                + "                    <a href=\"condicion_venta.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Condicion de Venta</a>\n"
                + "                    <a href=\"marca.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Marcas</a>\n"
                + "                    <a href=\"nacionalidad.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Nacionalidades</a>\n"
                + "                    <a href=\"MantenerPerfil.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Perfiles</a>\n"
                + "                </div>\n"
                + "            </div>";

        bloqueMovimiento = "            <div class=\"w3-dropdown-hover\">\n"
                + "                <button class=\"w3-button\">Movimientos</button>\n"
                + "                <div class=\"w3-dropdown-content w3-bar-block w3-card-2\">\n"
                + "                    <a href=\"AperturaCierre.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Apertura-Cierre</a>\n"
                + "                    <a href=\"Existencia.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Inventarios</a>\n"
                + "                    <a href=\"GenerarPresupuesto.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Presupuesto</a>\n"
                + "                    <a href=\"Producto.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Productos</a>\n"
                + "                    <a href=\"Venta.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Ventas</a>\n"
                + "                </div>\n"
                + "            </div>";

        bloqueInformes = "            <div class=\"w3-dropdown-hover\">\n"
                + "                <button class=\"w3-button\">Informes</button>\n"
                + "                <div class=\"w3-dropdown-content w3-bar-block w3-card-2\">\n"
                + "                    <a href=\"Reportes.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Reportes Administrativos</a>\n"
                + "                    <a href=\"Reportes1.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Reportes Referenciales</a>\n"
                + "                    <a href=\"Reportes2.html\" target=\"principal\" class=\"w3-bar-item w3-button\">Reportes Movimientos</a>\n"
                + "                </div>\n"
                + "            </div>";

        bloqueAyuda = "            <div class=\"w3-dropdown-hover\">\n"
                + "    <target=\"principal\" class=\"w3-bar-item w3-button\">"
                + "         <button class=\"w3-button\" onclick=\"Ayuda1();\">Ayuda</button></a>\n"
                + "</div>";

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //recuperar datos del js <getParameter>
        usuario = request.getParameter("par_usuario").trim();
        clave = request.getParameter("par_clave").trim();
        //        System.out.println("usuario" + usuario);
//        System.out.println("clave" + clave);

        Integer permiso = ObtenerPerfil();
        System.out.println("Permiso del usuario " + permiso);
        if (permiso != null) {
            switch (permiso) {
                case 1:
                    //System.out.println("Case 1");
                    //System.out.println(bloqueAdministracion + bloqueReferenciales + bloqueMovimiento + bloqueInformes + bloqueAyuda);
                    out.println(bloqueAdministracion + bloqueReferenciales + bloqueMovimiento + bloqueInformes + bloqueAyuda);
                    out.close();
                    break;
                case 2:
                    out.println(bloqueInformes + bloqueAyuda);
                    break;
                case 3:
                    out.println(bloqueAdministracion + bloqueReferenciales + bloqueMovimiento + bloqueInformes + bloqueAyuda);
                    break;

            }
        }
    }

    private Integer ObtenerPerfil() {
        try {
            query = "SELECT id_usuario, perfil_id_perfil, empleado_id_empleado FROM usuario "
                    + "WHERE estado=TRUE AND usuario=? and clave=?";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, usuario);
            ps.setString(2, clave);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("perfil_id_perfil");
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(permisosUsuariosMod.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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
