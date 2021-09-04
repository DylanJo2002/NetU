/**
  * Clase PetIniciarSesion. La clase hereda de Paquete. Su función es la de hacer
  * una peticion de inicio de sesion desde el cliente, con la información importante
 */
package Paquetes;

public class PetIniciarSesion extends Paquete {
    
    private int codigo;
    private String password;
    
    /**
     * Constructor inicializando su código y contraseña
     * @param codigo El código que el usuario desde NetU Cliente hace la petición
     * de inicio de sesión
     * @param password La constraseña que el usuario desde NetU Cliente hace la petición
     * de inicio de sesión
     */
    public PetIniciarSesion(int codigo, String password) {
        this.codigo = codigo;
        this.password = password;
    }
    /**
     * El propósito del método es devolver el código.
     * @return Devuelve el código
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * El propósito del método es devolver la contraseña
     * @return Devuelve la contraseña
     */
    public String getPassword() {
        return password;
    }
    
}
