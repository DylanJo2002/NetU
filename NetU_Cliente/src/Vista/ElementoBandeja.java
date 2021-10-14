/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.io.Serializable;

/**
 *
 * @author dilan
 */
public class ElementoBandeja implements Serializable{
    private String nombreEmpleado;
    private int codigoEmpleado;

    public ElementoBandeja(String nombreEmpleado, int codigoEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }   
}
