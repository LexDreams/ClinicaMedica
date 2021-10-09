/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dto;

/**
 *
 * @author administrador
 */
public class PacienteDTO {
    private int idPasiente;
    private String nombre;
    private Float presupuesto;
    private String telefono;
    private String direccion;
    private String sexo;
    private String fecha_nac;
    
    public int getIdPasiente() {
        return idPasiente;
    }

    public void setIdPasiente(int idPasiente) {
        this.idPasiente = idPasiente;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Float getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(float presupuesto) {
        this.presupuesto = presupuesto;
    }
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String getFechaN() {
        return fecha_nac;
    }

    public void setFechaN(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }   
}
