package Paquetes;

import Vista.Empleado;
import java.util.List;

/**
 *
 * @author PuntoPC
 */
public class RespuestaBusqueda extends Paquete{
    
    private List<Empleado> empleados;
    

    public RespuestaBusqueda(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }
    
    
}
