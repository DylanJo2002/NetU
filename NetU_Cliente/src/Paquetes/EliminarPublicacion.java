/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paquetes;

import java.util.List;



public class EliminarPublicacion extends Paquete {
    
    private int codigoEmpleado;
    private List<Integer> idPublicaciones;

    public EliminarPublicacion(List<Integer> idPublicaciones) {
        this.idPublicaciones = idPublicaciones;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public List<Integer> getIdPublicaciones() {
        return idPublicaciones;
    }
    
}
