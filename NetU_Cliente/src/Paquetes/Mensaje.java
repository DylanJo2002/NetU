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
public class Mensaje extends Paquete{
    private String contenido;
    private String fecha;
    private String hora;
    private boolean propio;

    public Mensaje(String contenido, String fecha, String hora) {
        this.contenido = contenido;
        this.fecha = fecha;
        this.hora = hora;
        this.propio = propio;
    }
    
    public void setPropio(boolean propio){
        this.propio = propio;
    }

    public String getContenido() {
        return contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
    
    public boolean getPropio() {
        return propio;
    }
}
