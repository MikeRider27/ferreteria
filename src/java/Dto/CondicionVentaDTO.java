package Dto;

public class CondicionVentaDTO {
    private Integer id_cv;
    private String descripcion;
    private String plazo;

    public Integer getId_cv() {
        return id_cv;
    }

    public void setId_cv(Integer id_cv) {
        this.id_cv = id_cv;
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
}