package Paquetes;

/**
 *
 * @author PuntoPC
 */
public class PeticionBusqueda extends Paquete{
    
    private int idDependencia;
    private int idSubdependencia;
    private String nombreEmpleado;

    //Constructor
    public PeticionBusqueda(int idDependencia, int idSubdependencia, String nombreEmpleado) {
        this.idDependencia = idDependencia;
        this.idSubdependencia = idSubdependencia;
        this.nombreEmpleado = nombreEmpleado;
    }

    public int getIdDependencia() {
        return idDependencia;
    }

    public int getIdSubdependencia() {
        return idSubdependencia;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }
    
    
    
}
