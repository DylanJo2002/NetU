/**
 * El propósito de la clase es generalizar aspectos de los paquetes que serán
 * enviados y recibidos entre NetU Cliente y NetU Servidor
 */
package Paquetes;

import java.io.Serializable;

public class Paquete implements Serializable {

    public static final int petIniciarSesion = 1;
    public static final int resIniciarSesion = 2;
    public static final int cambiarDescripcion = 3;
    public static final int publicar = 4;
    public static final int publicaciones = 5;
    public static final int eliminarPublicacion = 6;
    public static final int error = 10;

    private int tipo;
    /**
     * El propósito del método es devolver el tipo de paquete.
     * @return Devuelve el tipo de paquete que es (según las constantes)
     */
    public int getTipo() {
        return tipo;
    }
    /**
     * El propósito del método es asignar un tipo de paquete
     * @param tipo El tipo de paquete que se quiere asignar(según las constantes)
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

}
