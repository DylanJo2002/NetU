/**
 * El propósito de la clase es gestionar la información de las publicaciones
 * para las transacciones entre NetU Cliente y Servidor, como Paquete o siendo
 * parte de Él.
 */
package Paquetes;

public class Publicacion extends Paquete {
    
    private int id_Empleado;
    private int id_Publicacion;
    private String fecha;
    private String hora;
    private String contenido;

    public int getId_Publicacion() {
        return id_Publicacion;
    }

    public void setId_Publicacion(int id_Publicacion) {
        this.id_Publicacion = id_Publicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getContenido() {
        return contenido;
    }

    public int getId_Empleado() {
        return id_Empleado;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setId_Empleado(int id_Empleado) {
        this.id_Empleado = id_Empleado;
    }
    
    
    
}
