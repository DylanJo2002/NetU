/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paquetes;

/**
 *
 * @author danie
 */
public class ConsultaNotificacion extends Paquete{
    private int codEmpRecibe;
    private int estadoMensaje;
    private String nomEmpEnvia;
    private Notificaciones notificaciones;

    public Notificaciones getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(Notificaciones notificaciones) {
        this.notificaciones = notificaciones;
    }
    
    public ConsultaNotificacion(){
        
    }

    public int getCodEmpRecibe() {
        return codEmpRecibe;
    }

    public void setCodEmpRecibe(int codEmpRecibe) {
        this.codEmpRecibe = codEmpRecibe;
    }

    public int getEstadoMensaje() {
        return estadoMensaje;
    }

    public void setEstadoMensaje(int estadoMensaje) {
        this.estadoMensaje = estadoMensaje;
    }    

    public String getNomEmpEnvia() {
        return nomEmpEnvia;
    }

    public void setNomEmpEnvia(String nomEmpEnvia) {
        this.nomEmpEnvia = nomEmpEnvia;
    }
    
}
