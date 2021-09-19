/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paquetes;

/**
 *
 * @author dilan
 */
public class EnvioMensaje extends Paquete{
    private String mensaje;

    public EnvioMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
