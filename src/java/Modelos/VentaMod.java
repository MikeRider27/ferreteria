package Modelos;

import Dto.DetalleVentaDTO;
import Dto.VentaDTO;
import Genericos.ConexionDB;
import static Genericos.ConexionDB.TR.CANCELAR;
import static Genericos.ConexionDB.TR.INICIAR;
import Genericos.Reportes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class VentaMod extends HttpServlet {

    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, id_documento, id_cv, id_usuario, id_ac;
    private String total_venta, query;
    private int filasAfectadas;
    private boolean esAnular = false;

    public VentaMod() {
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
            Type type = new TypeToken<VentaDTO>() {
            }.getType();
            VentaDTO objetoVenta = jsonAObjeto.fromJson(json, type);
            agregarVenta(objetoVenta);
        } else {
            String id_venta;
            if (json.contains("bandera=3")) {
                id_venta = json.replace("bandera=3&id_venta=", "");
                anularVenta(Integer.parseInt(id_venta));
                return;
            } else {
                id_venta = json.replace("bandera=2&id_venta=", "");
                String venta = recuperarVenta(Integer.parseInt(id_venta));
                if (venta != null) {
                    response.setContentType("application/json, charset=UTF-8");
                    out.println(venta);
                    out.close();
                }
            }
        }
    }

    private boolean agregarVenta(VentaDTO objeto) {
        int id_venta = 0;
        try {
            ConexionDB.Transaccion(ConexionDB.TR.INICIAR);
            query = "INSERT INTO public.venta(apertura_cierre_id_ac, fecha, tipo_documento_id_documento, condicion_venta_id_cv, usuario_id_usuario, obs) "
                    + "VALUES (?, ?, ?, ?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, objeto.getId_ac());
            ps.setString(2, objeto.getFecha().trim());
            ps.setInt(3, objeto.getId_documento());
            ps.setInt(4, objeto.getId_cv());
            ps.setInt(5, objeto.getId_usuario());
            ps.setString(6, objeto.getObs());
            filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Inserto en Cab ");
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    id_venta = rs.getInt(1);
                    System.out.println("recupera idGenerado " + id_venta);

                    for (DetalleVentaDTO item : objeto.getDetalle()) {
                        query = "INSERT INTO public.detalle_venta(venta_id_venta, producto_id_producto, cantidad, precio_venta) VALUES (?, ?, ?, ?);";
                        ps = ConexionDB.getDBcon().prepareStatement(query);
                        ps.setInt(1, id_venta);
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
            String urlJasper = "nota_venta.jasper";
            HashMap p = new HashMap();
            p.put("id_venta", id_venta);
            Reportes.verReporte(getServletContext().getRealPath("/WEB-INF/Reportes/" + urlJasper), p);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VentaMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String recuperarVenta(Integer id_venta) {
        VentaDTO item = null;
        DetalleVentaDTO itemDeta;
        ArrayList<DetalleVentaDTO> lista;
        ArrayList<VentaDTO> venta;
        try {
            query = "SELECT v.tipo_documento_id_documento, doc.descripcion as documento, v.condicion_venta_id_cv, cv.descripcion as condicion, cv.plazo,"
                    + " v.fecha, v.obs, v.usuario_id_usuario, u.usuario, v.apertura_cierre_id_ac, dv.producto_id_producto, prd.descripcion as producto, "
                    + "dv.cantidad, dv.precio_venta, dv.cantidad * dv.precio_venta AS total_venta  FROM venta v  \n"
                    + "INNER JOIN tipo_documento doc ON v.tipo_documento_id_documento=doc.id_documento \n"
                    + "INNER JOIN condicion_venta cv ON v.condicion_venta_id_cv=cv.id_cv \n"
                    + "INNER JOIN usuario u ON v.usuario_id_usuario=u.id_usuario \n"
                    + "INNER JOIN apertura_cierre ac ON v.apertura_cierre_id_ac=ac.id_ac \n"
                    + "INNER JOIN detalle_venta dv ON v.id_venta=dv.venta_id_venta \n"
                    + "INNER JOIN producto prd ON dv.producto_id_producto=prd.id_producto WHERE v.id_venta=? and v.estado=TRUE;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_venta);
            rs = ps.executeQuery();
            venta = new ArrayList<>();
            lista = new ArrayList<>();
            item = new VentaDTO();
            while (rs.next()) {
                item.setFecha(rs.getString("fecha"));
                item.setId_documento(rs.getInt("tipo_documento_id_documento"));
                item.setDocumento(rs.getString("documento"));
                item.setId_cv(rs.getInt("condicion_venta_id_cv"));
                item.setCondicion(rs.getString("condicion"));
                item.setId_usuario(rs.getInt("usuario_id_usuario"));
                item.setUsuario(rs.getString("usuario"));
                item.setId_ac(rs.getInt("apertura_cierre_id_ac"));
                item.setObs(rs.getString("obs"));
                itemDeta = new DetalleVentaDTO();
                itemDeta.setId_producto(rs.getInt("producto_id_producto"));
                itemDeta.setProducto(rs.getString("producto"));
                itemDeta.setCantidad(rs.getInt("cantidad"));
                itemDeta.setPrecio_venta(rs.getInt("precio_venta"));
                //itemDeta.setTotal_venta(rs.getInt("total_venta"));
                lista.add(itemDeta);
                item.setDetalle(lista);
            }
            venta.add(item);
            return new Gson().toJson(venta);
        } catch (SQLException ex) {
            Logger.getLogger(VentaMod.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean anularVenta(Integer id_venta) {
        try {
            query = "UPDATE venta SET estado=FALSE WHERE id_venta=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_venta);
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
            Logger.getLogger(VentaMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        //reset    
    }

    private String obtenerDoc() {
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
            Logger.getLogger(VentaMod.class.getName()).log(Level.SEVERE, null, ex);
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
                option += "<option value= " + rs.getInt("id_documento") + ">" + rs.getString("descripcion") + ">" + rs.getString("plazo") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(VentaMod.class.getName()).log(Level.SEVERE, null, ex);
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
