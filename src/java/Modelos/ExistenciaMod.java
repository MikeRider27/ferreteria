package Modelos;

import Dto.DepositoDTO;
import Dto.ExistenciaDTO;
import Genericos.ConexionDB;
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

public class ExistenciaMod extends HttpServlet {

    private String query;
    private ResultSet rs;
    private PreparedStatement ps;
    private int filasAfectadas;
    private boolean esAnular = false;

    public ExistenciaMod() {
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
            Type type = new TypeToken<DepositoDTO>() {
            }.getType();
            DepositoDTO obDEP = jsonAObjeto.fromJson(json, type);
            agregarExistencia(obDEP);
        } else {
            String id_deposito;
            if (json.contains("bandera=3")) {
                id_deposito = json.replace("bandera=3&id_deposito=", "");
                anularExistencia(Integer.parseInt(id_deposito));
                return;
            } else {
                id_deposito = json.replace("bandera=2&id_deposito=", "");
                String deposito = recuperarExistencia(Integer.parseInt(id_deposito));
                if (deposito != null) {
                    response.setContentType("application/json, charset=UTF-8");
                    out.println(deposito);
                    out.close();
                }
            }
        }
    }

    private boolean agregarExistencia(DepositoDTO objeto) {
        int id_deposito = 0;
        try {
            ConexionDB.Transaccion(ConexionDB.TR.INICIAR);
            query = "INSERT INTO deposito(sucursal_id_sucursal, descripcion) VALUES (?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, objeto.getId_sucursal());
            ps.setString(2, objeto.getDeposito());
            filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Inserto en Cab ");
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    id_deposito = rs.getInt(1);
                    System.out.println("recupera idGenerado " + id_deposito);

                    for (ExistenciaDTO item : objeto.getDetalle()) {
                        query = "INSERT INTO existencia(deposito_id_deposito, producto_id_producto, cantidad) VALUES (?, ?, ?);";
                        ps = ConexionDB.getDBcon().prepareStatement(query);
                        ps.setInt(1, id_deposito);
                        ps.setInt(2, item.getId_producto());
                        ps.setInt(3, item.getCantidad());
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
            String urlJasper = "nota_existencia.jasper";
            HashMap p = new HashMap();
            p.put("id_deposito", id_deposito);
            Reportes.verReporte(getServletContext().getRealPath("/WEB-INF/Reportes/" + urlJasper), p);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String recuperarExistencia(Integer id_deposito) {
        DepositoDTO item = null;
        ExistenciaDTO itemDeta;
        ArrayList<ExistenciaDTO> lista;
        ArrayList<DepositoDTO> deposito;
        try {
            query = "SELECT dep.descripcion as deposito, dep.sucursal_id_sucursal, suc.descripcion as sucursal, ex.producto_id_producto, "
                    + "prod.descripcion as producto, ex.cantidad FROM deposito dep "
                    + "INNER JOIN sucursal suc ON dep.sucursal_id_sucursal=suc.id_sucursal "
                    + "INNER JOIN existencia ex ON dep.id_deposito=ex.deposito_id_deposito "
                    + "INNER JOIN producto prod ON ex.producto_id_producto=prod.id_producto WHERE dep.id_deposito=? and dep.estado=TRUE;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_deposito);
            rs = ps.executeQuery();
            deposito = new ArrayList<>();
            lista = new ArrayList<>();
            item = new DepositoDTO();
            while (rs.next()) {
                item.setDeposito(rs.getString("deposito"));
                item.setId_sucursal(rs.getInt("sucursal_id_sucursal"));
                item.setSucursal(rs.getString("sucursal"));
                itemDeta = new ExistenciaDTO();
                itemDeta.setId_producto(rs.getInt("producto_id_producto"));
                itemDeta.setProducto(rs.getString("producto"));
                itemDeta.setCantidad(rs.getInt("cantidad"));
                lista.add(itemDeta);
                item.setDetalle(lista);
            }
            deposito.add(item);
            return new Gson().toJson(deposito);
        } catch (SQLException ex) {
            Logger.getLogger(ExistenciaMod.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean anularExistencia(Integer id_deposito) {
        try {
            query = "UPDATE deposito SET estado=FALSE WHERE id_deposito=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_deposito);
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
            Logger.getLogger(ExistenciaMod.class.getName()).log(Level.SEVERE, null, ex);
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
