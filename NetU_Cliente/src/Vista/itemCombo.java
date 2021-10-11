/**
 * El propósito de esta clase es manejar de mejor forma los ítems de los combos
 * relacionados con las dependencias y subdependencias
 */
package Vista;

import java.io.Serializable;

public class itemCombo implements Serializable{
    
    int id;
    String nombre;
    /**
     * Constructor que permite inicializar el objeto con su id y nombre
     * @param id La identificación o el código del item
     * @param nombre El nombre del ítem (Lo que se mostrará en el combobox)
     */
    public itemCombo(int id, String nombre){
        
        this.id = id;
        this.nombre = nombre;
        
    }
    /**
     * El método permite devolver el ID del itemCombo
     * @return Retorna su ID
     */
    public int getId() {
        return id;
    }
    /**
     * El método permite devolver el nombre del itemCombo
     * @return Retorna su nombre
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * El método indica que se debe mostrar el nombre cuando el itemCombo
     * se encuentre en el combobox.
     * @return El nombre del itemCombo, para mostrarlo en su combobox.
     */
    @Override
    public String toString() {
        return nombre;
    }
    
}
