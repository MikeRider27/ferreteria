package Modelos;

import Dto.DetalleVentaDTO;
import Dto.VentaDTO;
import Genericos.ConexionDB;
import static Genericos.ConexionDB.TR.*;
import Genericos.Genericos;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Generar_PedidoMod extends HttpServlet {

    private String query;
    private PreparedStatement ps;
    private ResultSet rs;
    private Integer filaAfectadas;

    public Generar_PedidoMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if (br.ready()) {
            json = br.readLine();
        }

        System.out.println("Json Leido " + json);
        //json = "[" + json + "]";

        Gson jsonAObjeto = new Gson();
        Type type = new TypeToken<VentaDTO>() {}.getType();

        VentaDTO fromJson = jsonAObjeto.fromJson(json, type);

        System.out.println("Objeto Resultante " + fromJson);
        generar_Pedido(fromJson);

    }

    public boolean generar_Pedido(VentaDTO objeto) {
        Integer idVenta;
        //Integer idPedido=Genericos.getUltimoID("tabla", "id");
        try {
            ConexionDB.Transaccion(INICIAR);
            query = "INSERT INTO public.venta(tipo_documento_id_documento, condicion_venta_id_cv, usuario_id_usuario, "
                    + "apertura_cierre_id_ac, fecha, obs) VALUES (?, ?, ?, ?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, objeto.getId_documento());
            ps.setInt(2, objeto.getId_cv());
            ps.setInt(3, objeto.getId_usuario());
            ps.setInt(4, objeto.getId_ac());
            ps.setString(5, objeto.getFecha().trim());
            ps.setString(6, objeto.getObs().trim());
            filaAfectadas = ps.executeUpdate();
            if (filaAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idVenta = rs.getInt(1);
                    for (DetalleVentaDTO item : objeto.getDetalle()) {
                        query = "INSERT INTO public.detalle_venta(venta_id_venta, producto_id_producto, descripcion, cantidad)"
                                + " VALUES (?, ?, ?, ?);";
                        ps = ConexionDB.getDBcon().prepareStatement(query);
                        ps.setInt(1, idVenta);
                        ps.setInt(2, item.getId_producto());
                        ps.setString(3, item.getProducto());
                        ps.setInt(4, item.getCantidad());
                        filaAfectadas = ps.executeUpdate();
                        if (filaAfectadas <= 0) {
                            ConexionDB.Transaccion(CANCELAR);
                            return false;
                        }
                    }
                    if (filaAfectadas > 0) {
                        ConexionDB.Transaccion(CONFIRMAR);
                        return true;
                    }
                }
            } else {
                ConexionDB.Transaccion(CANCELAR);
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

            return false;
        }
        return false;
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
