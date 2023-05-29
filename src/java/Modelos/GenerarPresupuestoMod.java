package Modelos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Dto.PresupuestoDTO;
import Dto.Detalle_PresupuestoDTO;
import Dto.ProductoDTO;
import Genericos.ConexionDB;
import static Genericos.ConexionDB.TR.INICIAR;
import Genericos.Reportes;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GenerarPresupuestoMod extends HttpServlet {

    private String query;
    private ResultSet rs;
    private PreparedStatement ps;
    private int filasAfectadas;
    private boolean esAnular = false;

    public GenerarPresupuestoMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

        String json = "";
        if (br.ready()) {
            json = br.readLine();
        }
        System.out.println("Json Leido " + json);
        if (json.contains("\"bandera\":1")) {
            System.out.println("Case 1");
            Gson jsonAObjeto = new Gson();
            Type type = new TypeToken<PresupuestoDTO>() {
            }.getType();
            PresupuestoDTO obPresp = jsonAObjeto.fromJson(json, type);
            agregarPresupuesto(obPresp);
        } else {
            String id_presupuesto;
            if (json.contains("bandera=3")) {
                id_presupuesto = json.replace("bandera=3&id_presupuesto=", "");
                anularPresupuesto(Integer.parseInt(id_presupuesto));
                return;
            } else {
                id_presupuesto = json.replace("bandera=2&id_presupuesto=", "");
                String presupuesto = recuperarPresupuesto(Integer.parseInt(id_presupuesto));
                if (presupuesto != null) {
                    response.setContentType("application/json, charset=UTF-8");
                    out.println(presupuesto);
                    out.close();
                }
            }
        }
    }

    private boolean agregarPresupuesto(PresupuestoDTO objeto) {
        int id_presupuesto = 0;
        try {
            ConexionDB.Transaccion(ConexionDB.TR.INICIAR);
            query = "INSERT INTO public.presupuesto(usuario_id_usuario, fecha, observacion) VALUES (?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, objeto.getId_usuario());
            ps.setString(2, objeto.getFecha());
            ps.setString(3, objeto.getObservacion());
            filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Inserto en Cab ");
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    id_presupuesto = rs.getInt(1);
                    System.out.println("recupera idGenerado " + id_presupuesto);

                    for (Detalle_PresupuestoDTO item : objeto.getDetalle()) {
                        query = "INSERT INTO public.detalle_presupuesto(presupuesto_id_presupuesto, producto_id_producto, cantidad, precio_venta) "
                                + "VALUES (?, ?, ?, ?);";
                        ps = ConexionDB.getDBcon().prepareStatement(query);
                        ps.setInt(1, id_presupuesto);
                        ps.setInt(2, item.getId_producto());
                        ps.setInt(3, item.getCantidad());
                        ps.setInt(4, item.getPrecio_venta());
                        //ps.setInt(5, item.getTotal_venta());
                        filasAfectadas = ps.executeUpdate();
                        if (filasAfectadas <= 0) {
                            ConexionDB.Transaccion(ConexionDB.TR.CANCELAR);
                            return false;
                        }
                    }
                }
            } else {
                ConexionDB.Transaccion(ConexionDB.TR.CANCELAR);
                return false;
            }
            ConexionDB.Transaccion(ConexionDB.TR.CONFIRMAR);
            String urlJasper = "nota_presupuesto.jasper";
            HashMap p = new HashMap();
            p.put("id_presupuesto", id_presupuesto);
            Reportes.verReporte(getServletContext().getRealPath("/WEB-INF/Reportes/" + urlJasper), p);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(GenerarPresupuestoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String recuperarPresupuesto(Integer id_presupuesto) {
        PresupuestoDTO item = null;
        Detalle_PresupuestoDTO itemDeta;
        ArrayList<Detalle_PresupuestoDTO> lista;
        ArrayList<PresupuestoDTO> presupuesto;
        try {
            query = "SELECT p.fecha, p.observacion, p.usuario_id_usuario, u.usuario, dp.producto_id_producto, prd.descripcion, dp.cantidad, "
                    + "dp.precio_venta FROM presupuesto p "
                    + "INNER JOIN usuario u  ON p.usuario_id_usuario=u.id_usuario "
                    + "INNER JOIN detalle_presupuesto dp ON p.id_presupuesto=dp.presupuesto_id_presupuesto "
                    + "INNER JOIN producto prd ON dp.producto_id_producto=prd.id_producto WHERE p.id_presupuesto=? and p.estado=TRUE;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_presupuesto);
            rs = ps.executeQuery();
            presupuesto = new ArrayList<>();
            lista = new ArrayList<>();
            item = new PresupuestoDTO();
            while (rs.next()) {
                item.setId_usuario(rs.getInt("usuario_id_usuario"));
                item.setUsuario(rs.getString("usuario"));
                item.setFecha(rs.getString("fecha"));
                item.setObservacion(rs.getString("observacion"));
                itemDeta = new Detalle_PresupuestoDTO();
                itemDeta.setId_producto(rs.getInt("producto_id_producto"));
                itemDeta.setDescripcion(rs.getString("descripcion"));
                itemDeta.setCantidad(rs.getInt("cantidad"));
                itemDeta.setPrecio_venta(rs.getInt("precio_venta"));
                //itemDeta.setTotal_venta(rs.getInt("total_venta"));
                lista.add(itemDeta);
                item.setDetalle(lista);
            }
            presupuesto.add(item);
            return new Gson().toJson(presupuesto);
        } catch (SQLException ex) {
            Logger.getLogger(GenerarPresupuestoMod.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean anularPresupuesto(Integer id_presupuesto) {
        try {
            query = "UPDATE presupuesto SET estado=FALSE WHERE id_presupuesto=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_presupuesto);
            ConexionDB.Transaccion(ConexionDB.TR.INICIAR);
            filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                ConexionDB.Transaccion(ConexionDB.TR.CONFIRMAR);
                esAnular = false;
                return true;
            } else {
                ConexionDB.Transaccion(ConexionDB.TR.CANCELAR);
                esAnular = false;
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerarPresupuestoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        //reset    
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
