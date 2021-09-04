/**
  *El propósito de la clase Empleado es facilitar el manejo de la información y
  *las acciones que se realizan al empleado en NetU Servidor.
 */
package Vista;

public class Empleado {

    private int codigo;
    private int idDependencia;
    private int idSubdependencia;
    private String nombre;
    private String correo;
    private String sexo;
    private String nombreDependencia;
    private String nombreSubdependencia;

    /**
     * Constructor vacío
     */
    public Empleado() {

    }

    /**
     *
     * @return Devuelve el codigo del empleado
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     *
     * @return Devuelve el nombre del empleado
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @return Devuelve el correo del empleado
     */
    public String getCorreo() {
        return correo;
    }

    /**
     *
     * @return Devuelve el ID de la dependencia a la que pertenece el empleado
     */
    public int getIdDependencia() {
        return idDependencia;
    }

    /**
     *
     * @return Devuelve el ID de la subdependencia a la que pertenece el
     * empleado
     */
    public int getIdSubdependencia() {
        return idSubdependencia;
    }

    /**
     *
     * @return Devuelve el sexo del empleado (Masculino ó Femenino)
     */
    public String getSexo() {
        return sexo;
    }

    /**
     *
     * @return Devuelve el nombre de la dependencia a la que pertenece el
     * empleado
     */
    public String getNombreDependencia() {
        return nombreDependencia;
    }

    /**
     *
     * @return Devuelve el nombre de la subdependencia a la que pertenece el
     * empleado
     */
    public String getNombreSubdependencia() {
        return nombreSubdependencia;
    }

    /**
     * Cambia el nombre de la dependencia a la que pertenece el empleado
     *
     * @param nombreDependencia
     */
    public void setNombreDependencia(String nombreDependencia) {
        this.nombreDependencia = nombreDependencia;
    }

    /**
     * Cambia el nombre del empleado
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Cambia el nombre de la subdependencia a la que pertenece el empleado
     *
     * @param nombreSubdependencia
     */
    public void setNombreSubdependencia(String nombreSubdependencia) {
        this.nombreSubdependencia = nombreSubdependencia;
    }

    /**
     * Cambia el correo del empleado
     *
     * @param correo
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Cambia el sexo del empleado
     *
     * @param sexo
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * Cambia el ID de la dependencia a la que pertenece el empleado
     *
     * @param idDependencia
     */
    public void setDependencia(int idDependencia) {
        this.idDependencia = idDependencia;
    }

    /**
     * Cambia el ID de la subdependencia a la que pertenece el empleado
     *
     * @param idSubdependencia
     */
    public void setSubDependencia(int idSubdependencia) {
        this.idSubdependencia = idSubdependencia;
    }

    /**
     * Cambia el código del empleado
     *
     * @param codigo
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

}
