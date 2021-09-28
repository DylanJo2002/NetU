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
public class Chat extends Paquete {
    private int codigoDestinatario;
    private ArrayList<Mensaje> mensajes;
    
    public Chat() {
    }
    
    public int getCodigoDestinatario() {
        return codigoDestinatario;
    }
    
    public ArrayList<Mensaje> getMensajes() {
        return mensajes;
    }  
  
    public void setCodigoDestinatario(int destinatario) {
        this.codigoDestinatario = destinatario;
    }

    public void setMensajes(ArrayList<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
