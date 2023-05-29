package Dto;

public class EmpleadoDTO {
    private Integer id_empleado;
    private Integer id_caja;
    private Integer id_cargo;
    private Integer id_nacionalidad;
    private Integer id_ciudad;
    private String nom_empleado;
    private String ape_empleado;
    private String cedula;
    private String direccion;
    private String telefono;

    public Integer getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(Integer id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNom_empleado() {
        return nom_empleado;
    }

    public void setNom_empleado(String nom_empleado) {
        this.nom_empleado = nom_empleado;
    }

    public String getApe_empleado() {
        return ape_empleado;
    }

    public void setApe_empleado(String ape_empleado) {
        this.ape_empleado = ape_empleado;
    }

    public Integer getId_caja() {
        return id_caja;
    }

    public void setId_caja(Integer id_caja) {
        this.id_caja = id_caja;
    }

    public Integer getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(Integer id_cargo) {
        this.id_cargo = id_cargo;
    }

    public Integer getId_nacionalidad() {
        return id_nacionalidad;
    }

    public void setId_nacionalidad(Integer id_nacionalidad) {
        this.id_nacionalidad = id_nacionalidad;
    }

    public Integer getId_ciudad() {
        return id_ciudad;
    }

    public void setId_ciudad(Integer id_ciudad) {
        this.id_ciudad = id_ciudad;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
}
