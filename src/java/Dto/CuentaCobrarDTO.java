package Dto;

public class CuentaCobrarDTO {
    private Integer id_cc;
    private Integer id_venta;
    private String importe_total;
    private String importe_cobrado;
    private Integer nro_cuota;
    private String fecha;

    public Integer getId_cc() {
        return id_cc;
    }

    public void setId_cc(Integer id_cc) {
        this.id_cc = id_cc;
    }

    public Integer getId_venta() {
        return id_venta;
    }

    public void setId_venta(Integer id_venta) {
        this.id_venta = id_venta;
    }

    public String getImporte_total() {
        return importe_total;
    }

    public void setImporte_total(String importe_total) {
        this.importe_total = importe_total;
    }

    public String getImporte_cobrado() {
        return importe_cobrado;
    }

    public void setImporte_cobrado(String importe_cobrado) {
        this.importe_cobrado = importe_cobrado;
    }

    public Integer getNro_cuota() {
        return nro_cuota;
    }

    public void setNro_cuota(Integer nro_cuota) {
        this.nro_cuota = nro_cuota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    
}
