/**
 * El propósito de la clase es servir de gestor a las operaciones que pueda
 * solicitar un Empleado desde NetU Cliente hacia la base de datos
 */
package Modelo;

import Controlador.Tiempo;
import Modelo.ConexionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Paquetes.Paquete;
import Paquetes.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestorBDEmpleado {

    /**
     * El propósito del método es generar un paquete de tipo ResIniciarSesion
     * con la respuesta a la petición de iniciar sesión
     *
     * @param peticion El paquete de tipo PetIniciarSesion que envia el Empleado
     * desde NetU Cliente con la información del login
     * @return Un paquete ResIniciarSesion que es respuesta a la petición de
     * sesión pasada como parámetro.
     */
    public ResIniciarSesion iniciarSesion(PetIniciarSesion peticion) {

        String mensaje = "";

        ResIniciarSesion respuesta = new ResIniciarSesion();
        //Consulta para verificar si existe el empleado
        String cExiteEmpleado = "SELECT * FROM empleado WHERE codigo_Empleado = "
                + peticion.getCodigo();
        //Consulta para verificar si la contraseña del empleado es correcta
        String cPassCorrect = "SELECT * FROM login, empleado \n"
                + "WHERE login.codigo_Empleado = empleado.codigo_Empleado \n"
                + "AND login.codigo_Empleado = " + peticion.getCodigo() + "\n"
                + "AND contraseña = " + "'" + peticion.getPassword() + "'";

        try {
            ResultSet result;

            result = ejecutarQuery(cExiteEmpleado);

            if (!result.next()) {

                mensaje = "No existe un empleado registrado con el código "
                        + peticion.getCodigo() + "\n";

            }

            result = ejecutarQuery(cPassCorrect);

            if (!result.next() && mensaje.isEmpty()) {

                mensaje += "La contraseña ingresada es incorrecta";

            }

            if (mensaje.isEmpty()) {
                respuesta.setPerfil(construirPerfil(new Perfil(peticion.getCodigo())));
                respuesta.setPublicaciones(listarPublicaciones(peticion.getCodigo()));
                cambiarEstadoEmpleado(peticion.getCodigo(), true);
            } else {

                respuesta.setExito(Paquete.error);

            }

            respuesta.setMensaje(mensaje);

        } catch (SQLException ex) {
            System.out.println("Error al iniciar sesion BD " + ex.getMessage());
        }

        return respuesta;

    }

    /**
     * El propósito del método es construír la información del Perfil a través
     * de la BBDD
     *
     * @param perfil El Perfil inicial (con su código)
     * @return Un Perfil con su información completa
     */
    public Perfil construirPerfil(Perfil perfil) {

        String consultaSQL = "SELECT nombre,sexo,correo,nombre_Dependencia,"
                + "nombre_Subdependencia,descripcion "
                + "FROM empleado,dependencia,subdependencia "
                + "WHERE empleado.dependencia = dependencia.id_Dependencia "
                + "AND empleado.subdependencia = subdependencia.id_Subdependencia "
                + "AND empleado.codigo_Empleado = " + perfil.getCodigo();

        ResultSet consultaRealizada = ejecutarQuery(consultaSQL);

        try {
            if (consultaRealizada.next()) {
                perfil.setNombre(consultaRealizada.getString("nombre"));
                perfil.setSexo(consultaRealizada.getString("sexo"));
                perfil.setCorreo(consultaRealizada.getString("correo"));
                perfil.setNombreDependencia(consultaRealizada.getString("nombre_Dependencia"));
                perfil.setNombreSubdependencia(consultaRealizada.getString("nombre_Subdependencia"));
                perfil.setDescripcion(consultaRealizada.getString("descripcion"));
            }
        } catch (SQLException ex) {
            System.out.println("ERROR AL OBTENER CONSULTA PARA CONSTRUIR"
                    + "PERFIL " + ex.getMessage());
        }

        return perfil;
    }

    /**
     * El propósito del método es ejecutar una consulta SQL.
     *
     * @param consulta La consulta SQL que se quiere realizar
     * @return Un ResultSet con el resultado de la consulta SQL
     */
    public ResultSet ejecutarQuery(String consulta) {
        System.out.println(consulta);
        ResultSet result = null;
        PreparedStatement stment;

        try {
            stment = ConexionBD.coneccion.prepareStatement(consulta);
            result = stment.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar el query " + ex.getMessage());
        }

        return result;

    }

    public void cambiarDescripcion(int codigoEmpleado, String nuevaDescripcion) {

        String update = "UPDATE empleado SET descripcion = '" + nuevaDescripcion + "' "
                + "WHERE codigo_Empleado = " + codigoEmpleado;

        ejecutarUpdate(update);

    }

    /**
     * El propósito del método es cambiar el estado de logeo de un Empleado
     *
     * @param estado TRUE para que esté conectado, FALSE para estar desconectado
     */
    public void cambiarEstadoEmpleado(int codigoEmpelado, boolean estado) {
        String updateEstado = "";
        if (estado) {
            updateEstado = "UPDATE login SET estado = 1 \n"
                    + "WHERE login.codigo_Empleado = " + codigoEmpelado;
        } else {
            updateEstado = "UPDATE login SET estado = 0 \n"
                    + "WHERE login.codigo_Empleado = " + codigoEmpelado;
        }

        ejecutarUpdate(updateEstado);
    }

    /**
     * El propósito del método es insertar una nueva publicación del Empleado y
     * retornar sus publicaciones actualizadas
     *
     * @param publicacion El paquete Publicacion que se recibe desde NetU
     * Cliente
     * @return Publicaciones, un Paquete que lista las publicaciones
     */
    public Publicaciones crearPublicacion(Publicacion publicacion) {

        String insertPublicacion = "INSERT INTO publicacion "
                + "(codigo_Empleado,fecha,hora,contenido) VALUES "
                + "(" + publicacion.getId_Empleado() + ",'"
                + Tiempo.obtenerFechaActual() + "','" + Tiempo.obtenerHoraActual() + "','"
                + publicacion.getContenido() + "')";
        System.out.println(insertPublicacion);
        ejecutarUpdate(insertPublicacion);

        return listarPublicaciones(publicacion.getId_Empleado());

    }
    
    public void eliminarPublicaciones(EliminarPublicacion eliminarPublicacion) {

        String idPublicaciones = "(";
        List<Integer> listaPublicaciones = eliminarPublicacion.getIdPublicaciones();
        
        for (int idPublicacion : listaPublicaciones) {

            if (listaPublicaciones.size()-1 == listaPublicaciones.indexOf(idPublicacion)) {
                idPublicaciones += idPublicacion+")";
            } else {
                idPublicaciones += idPublicacion + ",";
            }

        }

        String updateEliminar = "DELETE FROM publicacion WHERE codigo_Empleado = "
                + eliminarPublicacion.getCodigoEmpleado() + " AND id_Publicacion "
                + "IN " + idPublicaciones;
        
        ejecutarUpdate(updateEliminar);

    }
    
    public Publicaciones listarPublicaciones(int codigoEmpleado) {

        String consultaPublicaciones = "SELECT id_Publicacion as id,fecha,hora,"
                + "contenido FROM publicacion WHERE codigo_Empleado = "
                + codigoEmpleado + " ORDER BY fecha desc, hora desc";

        ResultSet consulta = ejecutarQuery(consultaPublicaciones);
        Publicaciones publicaciones = new Publicaciones();
        Publicacion publicacion;
        List<Publicacion> listaPublicaciones = new ArrayList();
        try {
            while (consulta.next()) {

                publicacion = new Publicacion();

                publicacion.setId_Publicacion(consulta.getInt("id"));
                publicacion.setFecha(Tiempo.convertirFecha(consulta.getString("fecha")));
                publicacion.setHora(Tiempo.convertirHora(consulta.getString("hora")));
                publicacion.setContenido(consulta.getString("contenido"));

                listaPublicaciones.add(publicacion);

            }

            publicaciones.asignarPublicaciones(listaPublicaciones);

        } catch (SQLException ex) {
            System.out.println("Error al listar publicaciones: " + ex.getMessage());
        }

        return publicaciones;
    }

    public Chat contruirChat(Chat chat, int codigoEmpleado) {
        String conjunto = "IN ("+codigoEmpleado+","+chat.getCodigoDestinatario()+")";
        String consultarChat = "SELECT * FROM mensaje WHERE codigo_Empleado_1 "
                + conjunto + " OR codigo_Empleado_2 "+conjunto
                + " ORDER BY fecha, hora ASC";
        String crearChatEmisor = "INSERT INTO chat values('"+codigoEmpleado+"','"
                +chat.getCodigoDestinatario()+"',1)";
        String crearChatReceptor = "INSERT INTO chat values('"+
                chat.getCodigoDestinatario()+"','" +codigoEmpleado+"',0)";
        String actualizarEstado = "UPDATE chat set abierto = 1 WHERE "
                + "codigo_Empleado_1 = "+codigoEmpleado+" AND codigo_Empleado_2"
                + " = "+chat.getCodigoDestinatario();
        String existeChat = "SELECT * FROM chat WHERE codigo_Empleado_1 "
                +conjunto+" OR codigo_Empleado_2 "+conjunto;
        
        ResultSet resultChat = ejecutarQuery(consultarChat);
        try {
            ArrayList<Mensaje> mensajes= new ArrayList<Mensaje>();
            while(resultChat.next()){
                int codigoEmisor = resultChat.getInt("codigo_Empleado_1");
                
                Mensaje mensaje = new Mensaje(resultChat.getString("contenido"),
                    resultChat.getString("fecha"),resultChat.getString("hora"));
                
                if(codigoEmisor == codigoEmpleado){
                    mensaje.setPropio(true);
                }
                mensajes.add(mensaje);
            }
            
            chat.setMensajes(mensajes);
            
            ResultSet consultaExisteChat = ejecutarQuery(existeChat);
            if(consultaExisteChat.next()){
                ejecutarUpdate(actualizarEstado);
            }else{
                ejecutarUpdate(crearChatEmisor);
                ejecutarUpdate(crearChatReceptor);
            }            
        } catch (SQLException ex) {
            System.out.println("Error al recorrer la consulta en CHAT");
            return null;
        }

        
        return chat;
    };
    
    public void crearMensaje(EnvioMensaje mensaje, int codigoEmpleado){
        String hora = Tiempo.obtenerHoraActual();
        String fecha = Tiempo.obtenerFechaActual();
        
        String insercion = "INSERT INTO mensaje VALUES("+codigoEmpleado
                +","+mensaje.getCodigoDestinatario()+",'"+fecha+"','"+hora+"','"
                +mensaje.getMensaje()+"');";
        ejecutarUpdate(insercion);
    }
    
    public boolean chatAbierto(int codigoEmisor, int codigoReceptor){
        String consulta = "SELECT * FROM chat where codigo_Empleado_1 = "
                +codigoEmisor+" AND codigo_Empleado_2 = "+codigoReceptor
                +" AND abierto = 1";
        ResultSet resultado = ejecutarQuery(consulta);
        try {
            if(resultado.next()){
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error al verificar el chat abierto: "+ex.getMessage());
        }
        return false;
    }
    
    public void cerrarChat(int codigoEmisor){
        String actualizarEstado = "UPDATE chat set abierto = 0 WHERE "
                + "codigo_Empleado_1 = "+codigoEmisor+" AND abierto = 1";
        ejecutarUpdate(actualizarEstado);
    }
    
    /**
     * El propósito del método es ejecutar una actualización SQL.
     *
     * @param update El Update SQL que se quiere realizar
     */
    
    public void ejecutarUpdate(String update) {
        System.out.println(update);
        PreparedStatement stment;
        int bandera = -1;

        try {
            stment = ConexionBD.coneccion.prepareStatement(update);
            bandera = stment.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al ejecutar el query " + ex.getMessage());
        }

        if (bandera == 1) {
            System.out.println("Se actualizó el estado de un empleado");
        }

    }
}
