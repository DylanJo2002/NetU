/**
 * El propósito de este paquete que hereda de Paquete es dar respuesta a 
 * una petición cuando se solicita un perfil. Este contiene la información
 * del Empleado como sus Publicaciones.
 */
package Paquetes;

public class Perfil extends Paquete {
    
    private int codigo;
    private String nombre;
    private String correo;
    private String sexo;
    private String nombreDependencia;
    private String nombreSubdependencia;
    private String descripcion;
    //FALTA EL MÓDULO DE PUBLICACIONES. ¡ESPERA!

    //DANIEL
    private byte[] foto;
   

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
    //DANIEL
    
    public Perfil(int codigo) {
        this.codigo = codigo;
    }
   
    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getSexo() {
        return sexo;
    }

    public String getNombreDependencia() {
        return nombreDependencia;
    }

    public String getNombreSubdependencia() {
        return nombreSubdependencia;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setNombreDependencia(String nombreDependencia) {
        this.nombreDependencia = nombreDependencia;
    }

    public void setNombreSubdependencia(String nombreSubdependencia) {
        this.nombreSubdependencia = nombreSubdependencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    
}
