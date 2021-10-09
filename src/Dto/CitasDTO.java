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
public class CitasDTO {
    private int idCita ;
    private String fecha_cita;
    private String hora;
    private String atendido;
    private int idPasiente;
    private int idConsulta;

    public String getAtendido() {
        return atendido;
    }

    public void setAtendido(String atendido) {
        this.atendido = atendido;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }
    public String getFechaCita() {
        return fecha_cita;
    }

    public void setFechaCita(String fecha_cita) {
        this.fecha_cita = fecha_cita;
    }
    public int getIdPasiente() {
        return idPasiente;
    }

    public void setIdPasiente(int idPasiente) {
        this.idPasiente = idPasiente;
    }
}
