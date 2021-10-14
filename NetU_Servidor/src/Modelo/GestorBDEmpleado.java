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
import Vista.ElementoBandeja;
import Vista.Empleado;
import Vista.itemCombo;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
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
                respuesta.setDependencias(consultarDependencias());
                respuesta.setSubdependencias(consultarSubdependencias(respuesta.getDependencias()));
                respuesta.setBandeja(consultarBandeja(new Bandeja(), peticion.getCodigo()));
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
                + "nombre_Subdependencia,descripcion,foto " //DANIEL
                + "FROM empleado,dependencia,subdependencia,img_perfiles "//DANIEL
                + "WHERE empleado.dependencia = dependencia.id_Dependencia "
                + "AND empleado.subdependencia = subdependencia.id_Subdependencia "
                + "AND empleado.codigo_Empleado = img_perfiles.codigo_Empleado "//DANIEL
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
                perfil.setFoto(consultaRealizada.getBytes("foto"));//DANIEL               
                
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
                + conjunto + " AND codigo_Empleado_2 "+conjunto
                + " ORDER BY fecha, hora ASC";
        String crearChatEmisor = "INSERT INTO chat values('"+codigoEmpleado+"','"
                +chat.getCodigoDestinatario()+"',1)";
        String crearChatReceptor = "INSERT INTO chat values('"+
                chat.getCodigoDestinatario()+"','" +codigoEmpleado+"',0)";
        String actualizarEstado = "UPDATE chat set abierto = 1 WHERE "
                + "codigo_Empleado_1 = "+codigoEmpleado+" AND codigo_Empleado_2"
                + " = "+chat.getCodigoDestinatario();
        String existeChat = "SELECT * FROM chat WHERE codigo_Empleado_1 = "
                +codigoEmpleado+" AND codigo_Empleado_2 = "+chat.getCodigoDestinatario();
        
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

        System.out.println();
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
    
    
    /**
     * El propósito del método es consultar todas las dependencias desde la
     * BBDD.
     *
     * @return Un ResultSet que incluye las dependencias por ID y Nombre
     */
    public ArrayList<itemCombo> consultarDependencias() {

        Connection con = ConexionBD.coneccion;
        String consulta = "SELECT id_Dependencia as id, nombre_Dependencia as nombre FROM dependencia";
        ResultSet resultado = null;
        ArrayList<itemCombo> dependencias;
        dependencias = new ArrayList<itemCombo>();

        try {
            PreparedStatement statement = con.prepareStatement(consulta);

            resultado = statement.executeQuery();
            
            while(resultado.next()){
                itemCombo item = new itemCombo(resultado.getInt("id"), resultado.getString("nombre"));
                dependencias.add(item);
            }

        } catch (SQLException ex) {
            System.out.println("ERROR AL CONSULTAR DEPENDENCIA " + ex.getMessage());
        }

        
        return dependencias;
     }	
	  
    //DANIELL
    
    public ConsultaPerfiles consultaPerfiles(ConsultaPerfiles peticion) {

        String mensaje = "";

        ConsultaPerfiles respuesta = new ConsultaPerfiles();
        //Consulta para verificar si existe el empleado
        String cExiteEmpleado = "SELECT * FROM empleado WHERE codigo_Empleado = "
                + peticion.getCodigo();
        
        

        try {
            ResultSet result;

            result = ejecutarQuery(cExiteEmpleado);

            if (!result.next()) {

                mensaje = "No existe un empleado registrado con el código "
                        + peticion.getCodigo() + "\n";

            }
            

            if (mensaje.isEmpty()) {
                respuesta.setPerfil(construirPerfil(new Perfil(Integer.parseInt
                                                      (peticion.getCodigo()))));
                respuesta.setPublicaciones(listarPublicaciones(
                                        Integer.parseInt(peticion.getCodigo())));                
                
            } else {

                respuesta.setExito(Paquete.error);                

            }

            respuesta.setMensaje(mensaje);

        } catch (SQLException ex) {
            System.out.println("Error al consultar el empleado " + ex.getMessage()
                                                    + "Método Consultar Perfiles");
        }

        return respuesta;

    }
    
    public void cambiarFotoBD(CambiarFoto cf){
        
        try {
            Connection con = ConexionBD.coneccion;
            String update = "UPDATE img_perfiles SET foto = ? "
                    + "WHERE codigo_Empleado = ?";            
            PreparedStatement psFoto = con.prepareStatement(update);            
            psFoto.setBytes(1, cf.getFoto());
            psFoto.setInt(2, Integer.parseInt(cf.getCodigo()));           
            psFoto.executeUpdate();            
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }  
    }
    
    
    
    //DANIEL

    /**
     * El propósito del método es consultar las subdependencias pertenecientes a
     * una dependencia
     *
     * @param id_Dependencia El ID de la dependencia a la que pertenece la
     * subdependencia
     * @return Un ResultSet con el ID y Nombre de las subdependenicas que le
     * pertenecen a la dependencia indicada.
     */
    public ArrayList<ArrayList<itemCombo>> consultarSubdependencias(ArrayList<itemCombo> dependencias) {

        Connection con = ConexionBD.coneccion;
        String consulta = "SELECT id_Subdependencia as id, nombre_Subdependencia as nombre FROM subdependencia ";
        consulta += "WHERE id_Dependencia = ?";
        ResultSet resultado = null;
        ArrayList<ArrayList<itemCombo>> subdependencias;
        subdependencias = new ArrayList<ArrayList<itemCombo>>();
        ArrayList<itemCombo> fila;
        
        try {
            
            for(itemCombo dependencia: dependencias){
                
                PreparedStatement statement = con.prepareStatement(consulta);
                statement.setInt(1, dependencia.getId());
                resultado = statement.executeQuery();
                fila = new ArrayList<itemCombo>();
                
                while(resultado.next()){
                itemCombo item = new itemCombo(resultado.getInt("id"), resultado.getString("nombre"));
                fila.add(item);
                }
                
                subdependencias.add(fila);
            }
            

        } catch (SQLException ex) {
            System.out.println("ERROR AL CONSULTAR SUBDEPENDENCIA " + ex.getMessage());
        }
        
        return subdependencias;
    }

  

    /**
     * El propósito del método es listar los empleados según nombre, código de
     * dependencia y subdependencia (si es 0, entonces se listan en el todo
     * según la categoría)
     *
     * @param dep id de la dependencia
     * @param sub id de la subdependencia
     * @param nombre nombre del empleado a buscar
     * @return ArrayList, lista de objetos Empleado
     */
    public List<Empleado> buscarEmpleados(int dep, int sub, String nombre,
            int empleadoExcepcion) {

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Empleado> listado = new ArrayList<>();

        //CONSULTAS
        String sql = "";

        try {

            con = ConexionBD.coneccion;

            if (nombre.isEmpty()) {

                nombre = "'%'";

            } else {
                nombre = "'%" + nombre + "%'";
            }

            if (dep == 0) {

                if (sub == 0) {

                    sql = "SELECT e.nombre,e.correo,codigo_Empleado, e.sexo , "
                            + "d.nombre_Dependencia , s.nombre_Subdependencia, \n"
                            + "e.dependencia, e.subdependencia\n"
                            + "from empleado e, dependencia d, subdependencia s\n"
                            + "where e.dependencia = d.id_Dependencia\n"
                            + "and e.subdependencia = s.id_Subdependencia\n"
                            + "and e.nombre like " + nombre + "\n"
                            + "and e.codigo_Empleado != " + empleadoExcepcion + "\n"
                            + "order by e.codigo_Empleado ;";

                }

            } else if (sub == 0) {

                sql = "SELECT e.nombre,e.correo,codigo_Empleado, e.sexo , "
                        + "d.nombre_Dependencia , s.nombre_Subdependencia, \n"
                        + "e.dependencia, e.subdependencia\n"
                        + "from empleado e, dependencia d, subdependencia s\n"
                        + "where e.dependencia = d.id_Dependencia\n"
                        + "and e.dependencia = " + dep + "\n"
                        + "and e.subdependencia = s.id_Subdependencia\n"
                        + "and e.nombre like " + nombre + "\n"
                        + "and e.codigo_Empleado != " + empleadoExcepcion + "\n"
                        + "order by e.codigo_Empleado ;";

            } else {

                sql = "SELECT e.nombre,e.correo,codigo_Empleado, e.sexo , "
                        + "d.nombre_Dependencia , s.nombre_Subdependencia, \n"
                        + "e.dependencia, e.subdependencia\n"
                        + "from empleado e, dependencia d, subdependencia s\n"
                        + "where e.dependencia = d.id_Dependencia\n"
                        + "and e.dependencia = " + dep + "\n"
                        + "and e.subdependencia = s.id_Subdependencia\n"
                        + "and e.subdependencia = " + sub + "\n"
                        + "and e.nombre like " + nombre + "\n"
                        + "and e.codigo_Empleado != " + empleadoExcepcion + "\n"
                        + "order by e.codigo_Empleado ;";

            }

            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setCodigo(Integer.parseInt(rs.getString("codigo_Empleado")));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setCorreo(rs.getString("correo"));
                empleado.setSexo(rs.getString("sexo"));
                empleado.setDependencia(rs.getInt("dependencia"));
                empleado.setSubDependencia(rs.getInt("subdependencia"));
                empleado.setNombreDependencia(rs.getString("nombre_Dependencia"));
                empleado.setNombreSubdependencia(rs.getString("nombre_Subdependencia"));
                listado.add(empleado);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Código : "
                    + ex.getErrorCode() + "\nError :" + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Código : "
                        + ex.getErrorCode() + "\nError :" + ex.getMessage());
            }
        }
        return listado;
    }
    
    public Bandeja consultarBandeja(Bandeja bandeja, int codigoEmpleado){
        String consulta = "SELECT emp2.codigo_Empleado,emp2.nombre " +
        "FROM chat chat join empleado emp1 ON chat.codigo_Empleado_1 = " +
        "emp1.codigo_Empleado join empleado emp2 on chat.codigo_Empleado_2 = " +
        "emp2.codigo_Empleado join mensaje m on m.codigo_Empleado_2 = " +
        "chat.codigo_Empleado_2 and m.codigo_Empleado_1 = "+codigoEmpleado+" "+
        "or chat.codigo_Empleado_2 and m.codigo_Empleado_2 = "+codigoEmpleado+" "+        
        "WHERE chat.codigo_Empleado_1 = "+codigoEmpleado+" "+
        "group by codigo_Empleado,nombre order by fecha desc, hora desc";
        
        ResultSet set = ejecutarQuery(consulta);
        try {
            while(set.next()){
                ElementoBandeja elemento = new ElementoBandeja(
                        set.getString("nombre"),set.getInt("codigo_Empleado"));
                bandeja.agregarElemento(elemento);
            }
        } catch (SQLException ex) {
            System.out.println("Error al leer la consulta de la bandeja "
                    +ex.getMessage());
        }
        
        return bandeja;
    }
}
