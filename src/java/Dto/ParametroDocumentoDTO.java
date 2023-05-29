package Dto;

import java.sql.Date;

public class ParametroDocumentoDTO {
    private Integer id_pd;
    private Integer id_documento;
    private Integer nro_formulario;
    private String nro_factura;
    private Integer nro_utilizado;
    private Integer nro_timbrado;
    private Date fecha;

    public Integer getId_pd() {
        return id_pd;
    }

    public void setId_pd(Integer id_pd) {
        this.id_pd = id_pd;
    }

    public Integer getId_documento() {
        return id_documento;
    }

    public void setId_documento(Integer id_documento) {
        this.id_documento = id_documento;
    }

    public Integer getNro_formulario() {
        return nro_formulario;
    }

    public void setNro_formulario(Integer nro_formulario) {
        this.nro_formulario = nro_formulario;
    }

    public String getNro_factura() {
        return nro_factura;
    }

    public void setNro_factura(String nro_factura) {
        this.nro_factura = nro_factura;
    }

    public Integer getNro_utilizado() {
        return nro_utilizado;
    }

    public void setNro_utilizado(Integer nro_utilizado) {
        this.nro_utilizado = nro_utilizado;
    }

    public Integer getNro_timbrado() {
        return nro_timbrado;
    }

    public void setNro_timbrado(Integer nro_timbrado) {
        this.nro_timbrado = nro_timbrado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
