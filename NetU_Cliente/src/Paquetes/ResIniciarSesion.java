/**
 * El propósito de la clase es servir de respuesta a una petición de iniciar
 * sesión desde NetU Cliente. Hereda de Paquete
 */
package Paquetes;

import Vista.itemCombo;
import java.util.ArrayList;

public class ResIniciarSesion extends Paquete {
    
    private int exito;
    private String mensaje;
    private Perfil perfil;
    private Publicaciones publicaciones;
    private ArrayList<itemCombo> dependencias;
    private ArrayList<ArrayList<itemCombo>> subdependencias;
    private Bandeja bandeja;
    /**
     * El propósito del método es devolver el valor del éxito.
     * @return Un entero que define si el inicio de sesión fue exitoso si es 0
     */
    public int getExito() {
        return exito;
    }
    /**
     * El propósito del método es asignar un valor de éxito.
     * @param exito El valor del éxito.
     */
    public void setExito(int exito) {
        this.exito = exito;
    }
    /**
     * El propósito del método es devolver un mensaje que se haya producido 
     * durante la petición de inicio de sesión.
     * @return Un mensaje que puede ser de error
     */
    public String getMensaje() {
        return mensaje;
    }
    /**
     * El propósito del método es asignar un mensaje al paquete de respuesta.
     * @param mensaje El mensaje que se quiere asignar.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    /**
     * El propósito del método es obtener el Perfil del Empleado
     * @return El Perfil del Empleado quien inició sesion
     */
    public Perfil getPerfil() {
        return perfil;
    }
    /**
     * El propósito del método es asignarle un Perfil al Empleado
     * @param perfil El Perfil que se quiere asignar
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Publicaciones getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(Publicaciones publicaciones) {
        this.publicaciones = publicaciones;
    }

    public ArrayList<itemCombo> getDependencias() {
        return dependencias;
    }

    public void setDependencias(ArrayList<itemCombo> dependencias) {
        this.dependencias = dependencias;
    }

    public ArrayList<ArrayList<itemCombo>> getSubdependencias() {
        return subdependencias;
    }

    public void setSubdependencias(ArrayList<ArrayList<itemCombo>> subdependencias) {
        this.subdependencias = subdependencias;
    }

    public Bandeja getBandeja() {
        return bandeja;
    }

    public void setBandeja(Bandeja bandeja) {
        this.bandeja = bandeja;
    }   

}
