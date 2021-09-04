/**
 * Clase cambiarDescripcion. Extiende de Paquete. Sirve para cambiar la descipcion
 * de un Empleado. Se envía desde NetU Cliente a NetU Servidor.
 */
package Paquetes;

public class CambiarDescripcion extends Paquete {
    
    private String nuevaDescripcion;

    public CambiarDescripcion(String nuevaDescripcion) {
        this.nuevaDescripcion = nuevaDescripcion;
    }

    public String getNuevaDescripcion() {
        return nuevaDescripcion;
    }
    
    
    
}
