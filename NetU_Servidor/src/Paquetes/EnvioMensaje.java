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
    private int codigoDestinatario;

    public EnvioMensaje(String mensaje,int codigoDestinatario) {
        this.mensaje = mensaje;
        this.codigoDestinatario = codigoDestinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getCodigoDestinatario() {
        return codigoDestinatario;
    }
    
    
}
