package Modelos;

import Dto.ProductoDTO;
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

public class ProductoMod extends HttpServlet {

    private ResultSet rs;
    private PreparedStatement ps;
    private Integer bandera, id_producto, precio_costo, precio_venta, id_marca, id_categoria, iva;
    private String producto, query;

    public ProductoMod() {
        ConexionDB.getInstancia();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        bandera = Integer.parseInt(request.getParameter("bandera").trim());
        id_producto = bandera < 4 ? Integer.parseInt(request.getParameter("id_producto").trim()) : 0;
        producto = bandera < 4 ? request.getParameter("producto").trim().toUpperCase() : "";
        precio_costo = bandera < 4 ? Integer.parseInt(request.getParameter("precio_costo").trim()) : 0;
        precio_venta = bandera < 4 ? Integer.parseInt(request.getParameter("precio_venta").trim()) : 0;
        id_marca = bandera < 4 ? Integer.parseInt(request.getParameter("id_marca").trim()) : 0;
        id_categoria = bandera < 4 ? Integer.parseInt(request.getParameter("id_categoria").trim()) : 0;
        iva = bandera < 4 ? Integer.parseInt(request.getParameter("iva").trim()) : 0;

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
                String rows = recuperarProducto();
                if (rows != null) {
                    out.print(rows);
                    out.close();
                }
                break;
            case 5:
                response.setContentType("application/json, charset=UTF-8");
                String prod = obtenerProductoID(Integer.parseInt(request.getParameter("id_producto").trim()));
                if (prod != null) {
                    out.println(prod);
                    out.close();
                }
                break;
            case 6:
                String marca = obtenerMarca();
                if (marca != null) {
                    out.println(marca);
                    out.close();
                }
                break;
            case 7:
                String categoria = obtenerCategoria();
                if (categoria != null) {
                    out.println(categoria);
                    out.close();
                }
            case 8:
                String pro = recuperarProductoHTML();
                if (pro != null) {
                    out.print(pro);
                    out.close();
                }
                break;
            case 9:
                String IVA = obtenerIVA();
                if (IVA != null) {
                    out.print(IVA);
                    out.close();
                }
                break;
        }
    }

    private boolean Alta() {
        try {
            query = "INSERT INTO public.producto(descripcion, precio_costo, precio_venta, marca_id_marca, categoria_id_categoria, tipo_impuesto_id_ti)\n"
                    + "    VALUES (?, ?, ?, ?, ?, ?);";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, producto);
            ps.setInt(2, precio_costo);
            ps.setInt(3, precio_venta);
            ps.setInt(4, id_marca);
            ps.setInt(5, id_categoria);
            ps.setInt(6, iva);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Baja() {
        try {
            query = "DELETE FROM public.producto WHERE id_producto=?";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_producto);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean Modificacion() {
        try {
            query = "UPDATE public.producto SET descripcion=?, precio_costo=?, precio_venta=?, marca_id_marca=?, categoria_id_categoria=?, tipo_impuesto_id_ti=? WHERE id_producto=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setString(1, producto);
            ps.setInt(2, precio_costo);
            ps.setInt(3, precio_venta);
            ps.setInt(4, id_marca);
            ps.setInt(5, id_categoria);
            ps.setInt(6, iva);
            ps.setInt(7, id_producto);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private String recuperarProducto() {
        String rows = "";
        try {
            query = "SELECT \"ID\", \"PRODUCTO\", \"PRECIO COSTO\", \"PRECIO VENTA\", \"MARCA\", \"CATEGORIA\", \"IMPUESTO\" "
                    + "FROM public.vista_producto ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows += "<tr>";
                rows += "<td>" + rs.getInt("ID") + "</td>";
                rows += "<td>" + rs.getString("PRODUCTO") + "</td>";
                rows += "<td>" + rs.getInt("PRECIO COSTO") + "</td>";
                rows += "<td>" + rs.getInt("PRECIO VENTA") + "</td>";
                rows += "<td>" + rs.getString("MARCA") + "</td>";
                rows += "<td>" + rs.getString("CATEGORIA") + "</td>";
                rows += "<td>" + rs.getString("IMPUESTO") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String recuperarProductoHTML() {
        String rows = "";
        String seleccion;
        try {
            query = "SELECT \"ID\", \"PRODUCTO\", \"MARCA\", \"CATEGORIA\", \"PRECIO VENTA\", \"IMPUESTO\" FROM public.vista_producto ORDER BY \"ID\";";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                seleccion = "<input id=" + rs.getInt("ID") + " class=\"w3-check w3-small opcion_seleccion_producto\" type=\"checkbox\">";
                rows += "<tr>";
                rows += "<td  align=\"center\"> " + seleccion + "  </td> ";
                rows += "<td>" + rs.getString("PRODUCTO") + "</td>";
                rows += "<td>" + rs.getString("MARCA") + "</td>";
                rows += "<td>" + rs.getString("CATEGORIA") + "</td>";
                rows += "<td>" + rs.getInt("PRECIO VENTA") + "</td>";
                rows += "<td>" + rs.getString("IMPUESTO") + "</td>";
                rows += "</tr>";
            }
            return rows;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Estamos en metodo Recuperacion - Select");
        return null;
    }

    private String obtenerMarca() {
        try {
            query = "SELECT id_marca, descripcion FROM public.marca ORDER BY id_marca;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_marca") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerCategoria() {
        try {
            query = "SELECT id_categoria, descripcion FROM public.categoria ORDER BY id_categoria;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_categoria") + ">" + rs.getString("descripcion") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerIVA() {
        try {
            query = "SELECT id_ti, descripcion, porcentaje FROM public.tipo_impuesto ORDER BY id_ti;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            rs = ps.executeQuery();
            String option = "";
            while (rs.next()) {
                option += "<option value= " + rs.getInt("id_ti") + ">" + rs.getString("descripcion") + " " + rs.getString("porcentaje") + "</option>";
            }
            return option;
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String obtenerProductoID(Integer id_producto) {
        ProductoDTO dto;
        ArrayList<ProductoDTO> lista;
        try {
            query = "SELECT id_producto, descripcion, precio_costo, precio_venta, marca_id_marca, categoria_id_categoria, tipo_impuesto_id_ti "
                    + "FROM public.producto WHERE id_producto=?;";
            ps = ConexionDB.getDBcon().prepareStatement(query);
            ps.setInt(1, id_producto);
            rs = ps.executeQuery();
            lista = new ArrayList<>();
            if (rs.next()) {
                dto = new ProductoDTO();
                dto.setId_producto(rs.getInt("id_producto"));
                dto.setProducto(rs.getString("descripcion"));
                dto.setPrecio_costo(rs.getInt("precio_costo"));
                dto.setPrecio_venta(rs.getInt("precio_venta"));
                dto.setId_marca(rs.getInt("marca_id_marca"));
                dto.setId_categoria(rs.getInt("categoria_id_categoria"));
                dto.setIva(rs.getInt("tipo_impuesto_id_ti"));
                lista.add(dto);
                return new Gson().toJson(lista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoMod.class.getName()).log(Level.SEVERE, null, ex);
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
