package Dto;

import java.util.List;

public class DepositoDTO {

    private Integer id_deposito;
    private Integer id_sucursal;
    private String deposito;
    private String sucursal;
    private List<ExistenciaDTO>detalle;

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public Integer getId_deposito() {
        return id_deposito;
    }

    public void setId_deposito(Integer id_deposito) {
        this.id_deposito = id_deposito;
    }

    public Integer getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(Integer id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public List<ExistenciaDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<ExistenciaDTO> detalle) {
        this.detalle = detalle;
    }


}
