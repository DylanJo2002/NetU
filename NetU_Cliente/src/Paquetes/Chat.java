/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paquetes;

import java.util.ArrayList;

/**
 *
 * @author dilan
 */
public class Chat extends Paquete{
    private String propietario;
    private String destinatario;
    private ArrayList<Mensaje> mensajes;

    public Chat(String propietario, String destinatario, ArrayList<Mensaje> mensajes) {
        this.propietario = propietario;
        this.destinatario = destinatario;
        this.mensajes = mensajes;
    }

    public String getPropietario() {
        return propietario;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }  
}
