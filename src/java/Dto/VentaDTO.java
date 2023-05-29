package Dto;

import java.util.List;

public class VentaDTO {
    private Integer id_venta;
    private Integer id_documento;
    private String documento;
    private Integer id_cv;
    private String condicion;
    private Integer id_producto;
    private String fecha;
    private String obs;
    private String descripcion;
    private String plazo;
    private Integer id_usuario;
    private String usuario;
    private String total_venta;
    private Integer id_ac;
    private List<DetalleVentaDTO> detalle;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public Integer getId_venta() {
        return id_venta;
    }

    public void setId_venta(Integer id_venta) {
        this.id_venta = id_venta;
    }

    public Integer getId_documento() {
        return id_documento;
    }

    public void setId_documento(Integer id_documento) {
        this.id_documento = id_documento;
    }

    public Integer getId_cv() {
        return id_cv;
    }

    public void setId_cv(Integer id_cv) {
        this.id_cv = id_cv;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }
    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
  
    public Integer getId_ac() {
        return id_ac;
    }

    public void setId_ac(Integer id_ac) {
        this.id_ac = id_ac;
    }

    public String getTotal_venta() {
        return total_venta;
    }

    public void setTotal_venta(String total_venta) {
        this.total_venta = total_venta;
    }

    public List<DetalleVentaDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleVentaDTO> detalle) {
        this.detalle = detalle;
    }
    

}
