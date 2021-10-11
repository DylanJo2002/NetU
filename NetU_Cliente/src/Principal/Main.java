/**
 * El propósito de la clase es ser la principal e iniciadora
 */
package Principal;

import Controlador.Controlador;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println("ERROR AL ASIGNAR LOOK AND FEEL");
        }
        Controlador controladorNetUCliente = new Controlador();
    }
    
}
