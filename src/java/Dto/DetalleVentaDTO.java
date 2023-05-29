package Dto;

public class DetalleVentaDTO {

    private Integer id_venta;
    private Integer id_producto;
    private String producto;
    private Integer cantidad;
    private Integer precio_venta;
    private Integer total_venta;

    public Integer getId_venta() {
        return id_venta;
    }

    public void setId_venta(Integer id_venta) {
        this.id_venta = id_venta;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(Integer precio_venta) {
        this.precio_venta = precio_venta;
    }

    public Integer getTotal_venta() {
        return total_venta;
    }

    public void setTotal_venta(Integer total_venta) {
        this.total_venta = total_venta;
    }

    
}
