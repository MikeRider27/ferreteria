package Dto;

import java.util.List;

public class PresupuestoDTO {
    private Integer id_presupuesto;
    private Integer id_usuario;
    private String usuario;
    private String fecha;
    private String observacion;
    private List<Detalle_PresupuestoDTO>detalle;

    public Integer getId_presupuesto() {
        return id_presupuesto;
    }

    public void setId_presupuesto(Integer id_presupuesto) {
        this.id_presupuesto = id_presupuesto;
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
 
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<Detalle_PresupuestoDTO>getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle_PresupuestoDTO>detalle) {
        this.detalle = detalle;
    }
   
}
