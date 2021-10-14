/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paquetes;

import java.util.List;

/**
 *
 * @author danie
 */
public class Notificaciones extends Paquete{
    private List<ConsultaNotificacion> notifcaiones;

    public List<ConsultaNotificacion> getNotifcaiones() {
        return notifcaiones;
    }

    public void setNotifcaiones(List<ConsultaNotificacion> notifcaiones) {
        this.notifcaiones = notifcaiones;
    }
   
   
}
