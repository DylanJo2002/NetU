/**
 * El propósito de esta clase es servir de modelo a la tabla que se muestra
 * en la ventana principal del servidor: nos sirve para manejar de mejor forma
 * los datos de los empleados y a estos como una lista
 *
 * La clase implementa la interfaz TableModel e implementa sus métodos
 */
package Vista;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TMEmpleado implements TableModel {

    private final int columnas = 5;
    protected EventListenerList listenerList;
    private List<Empleado> empleados;

    public TMEmpleado() {
        listenerList = new EventListenerList();
        empleados = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return empleados.size();
    }

    @Override
    public int getColumnCount() {
        return columnas;
    }

    @Override
    public String getColumnName(int columnIndex) {

        String columnName = "";

        switch (columnIndex) {

            case 0: {
                columnName = "Nombre";
            }
            break;

            case 1: {
                columnName = "Correo Electrónico";
            }
            break;

            case 2: {
                columnName = "Sexo";
            }
            break;

            case 3: {
                columnName = "Dependencia";
            }
            break;

            case 4: {
                columnName = "Subdependencia";
            }
            break;

        }

        return columnName;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Empleado empleado = empleados.get(rowIndex);
        String valor = "";

        switch (columnIndex) {

            case 0: {
                valor = empleado.getNombre();
            }
            break;

            case 1: {
                valor = empleado.getCorreo();
            }
            break;

            case 2: {
                valor = empleado.getSexo();
            }
            break;

            case 3: {
                valor = empleado.getNombreDependencia();
            }
            break;

            case 4: {
                valor = empleado.getNombreSubdependencia();
            }
            break;

        }

        return valor;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listenerList.add(TableModelListener.class, l);

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }
    /**
     * Cambia la lista de empleados que la tabla muestra, recibiendo una lista
     * nueva
     * @param empleados 
     */
    public void setEmpleados(List<Empleado> empleados) {
        this.empleados.clear();
        this.empleados = empleados;

    }
    /**
     * Obtiene el Empleado de una fila seleccionada en la tabla que existe
     * en la misma posición en el arreglo o lista.
    */
    public Empleado getEmpleadoAt(int fila) {

        return empleados.get(fila);
    }

}
