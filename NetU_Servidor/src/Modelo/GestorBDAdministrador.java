/**
 * El propósito de la clase es servir de gestor hacia todas las operaciones
 * relacionadas con la BBDD que hace el Administrador.
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import Vista.Empleado;

public class GestorBDAdministrador {

    /**
     * El propósito del método es verificar si la sesión con las credenciales
     * ingresadas son correctas
     *
     * @param codigo El código del Administrador
     * @param password La contraseña del Administrador
     * @return Retorna un String vacio si las credenciales son correctas, y un
     * String no vacío con un mensaje de error si no lo son.
     */
    public String iniciarSesion(String codigo, String password) {

        String mensaje = "";
        ResultSet result;
        PreparedStatement stment;

        //Consulta para verificar si existe el Administrador
        String cExiteAdmin = "SELECT * FROM administrador WHERE id_Empleado = "
                + codigo;
        //Consulta para verificar si la contraseña del Administrador es correcta
        String cPassCorrect = "SELECT * FROM login, administrador \n"
                + "WHERE login.codigo_Empleado = administrador.id_Empleado \n"
                + "AND login.codigo_Empleado = " + codigo + "\n"
                + "AND contraseña = " + "'" + password + "'";
        try {
            stment = ConexionBD.coneccion.prepareStatement(cExiteAdmin);
            result = stment.executeQuery();

            if (!result.next()) {
                mensaje = "No existe un Administrador registrado con el código";
            } else {
                stment = ConexionBD.coneccion.prepareStatement(cPassCorrect);
                result = stment.executeQuery();

                if (!result.next()) {

                    mensaje += "La contraseña ingresada es incorrecta";

                }

            }

        } catch (SQLException ex) {
            System.out.println("Eror SQL al verificar sesión del administrador");
        }

        return mensaje;
    }

    ;

    /**
     * El propósito del método es agregar un empleado a la BBDD
     *
     * @param empleado Objeto de la clase Empleado a insertar en la BBDD
     * @param password La contraseña que se le asigna al empleado
     * @return rs resultado de la operación INSERT (Si es 1, la operación fue
     * exitosa)
     */
   
    public int agregarEmpleado(Empleado empleado, String password) {

        Connection con = null;
        PreparedStatement pstm;
        pstm = null;
        int rs = 0;

        try {
            con = ConexionBD.coneccion;
            String sql = "INSERT INTO empleado values (?,?,?,?,?,?,?)";
            String sql1 = "INSERT INTO login values (?,?,?)";
            pstm = con.prepareStatement(sql);

            pstm.setInt(1, empleado.getCodigo());
            pstm.setString(2, empleado.getNombre());
            pstm.setString(3, empleado.getCorreo());
            pstm.setString(4, empleado.getSexo());
            pstm.setInt(5, empleado.getIdDependencia());
            pstm.setInt(6, empleado.getIdSubdependencia());
            pstm.setString(7, "");
            rs = pstm.executeUpdate();

            pstm = con.prepareStatement(sql1);

            pstm.setInt(1, empleado.getCodigo());
            pstm.setString(2, password);
            pstm.setInt(3, 0);
            pstm.executeUpdate();

            JOptionPane.showMessageDialog(null, "EMPLEADO REGISTRADO CON EXITO\n"
                    + "CODIGO: " + empleado.getCodigo() + "\nCONTRASEÑA: " + password);

            EnviaLogin(empleado.getCorreo(), empleado.getNombre(),
                    empleado.getCodigo() + "", password);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Código : "
                    + ex.getErrorCode() + "\nError :" + ex.getMessage());
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Código : "
                        + ex.getErrorCode() + "\nError :" + ex.getMessage());
            }
        }

        return rs;
    }

    /**
     * El objetivo del método es modificar un empelado en la BBDD
     *
     * @param empleado Objeto de la clase Empleado a modificar en la BBDD
     * @return rs resultado de la operación UPDATE (Si es 1, la operación fue
     * exitosa)
     */
    public int modificarEmpleado(Empleado empleado) {

        Connection con = null;
        PreparedStatement pstm;
        pstm = null;
        int rs = 0;

        try {
            con = ConexionBD.coneccion;
            String sql = "UPDATE empleado "
                    + "SET nombre=?, correo=?, sexo=?, dependencia=?, subdependencia=? "
                    + "WHERE codigo_Empleado=?";

            pstm = con.prepareStatement(sql);
            pstm.setString(1, empleado.getNombre());
            pstm.setString(2, empleado.getCorreo());
            pstm.setString(3, empleado.getSexo());
            pstm.setInt(4, empleado.getIdDependencia());
            pstm.setInt(5, empleado.getIdSubdependencia());
            pstm.setInt(6, empleado.getCodigo());
            rs = pstm.executeUpdate();

            JOptionPane.showMessageDialog(null, "SE ACTUALIZO LOS DATOS DEL EMPLEADO CON EXITO");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Código : "
                    + ex.getErrorCode() + "\nError :" + ex.getMessage());
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Código : "
                        + ex.getErrorCode() + "\nError :" + ex.getMessage());
            }
        }
        return rs;
    }

    /**
     * El objetivo del método es eliminar un empleado de la BBDD. Si el Empleado
     * es un Administrador se vuelve a confirmar
     *
     * @param codigo Código del empleado a eliminar
     * @return rs resultado de la operación DELETE (Si es 1, la operación fue
     * exitosa)
     */
    public int eliminarEmpleado(int codigo) {

        Connection con = null;
        PreparedStatement pstm;
        pstm = null;
        int res = 0;
        boolean eliminar = true; //Por Default true, si es un administrador ponemos
        //a consideración la eliminación

        try {
            con = ConexionBD.coneccion;

            if (esAdministrador(codigo)) {

                if (cantidadAdministradores() >= 1) {
                    int confirmacion;

                    confirmacion = JOptionPane.showConfirmDialog(null,
                            "¿Está de acuerdo con eliminar "
                            + "el Administrador con código " + codigo + "?",
                            "ELIMINAR ADMINISTRADOR", JOptionPane.YES_NO_OPTION);

                    eliminar = confirmacion == JOptionPane.YES_OPTION;
                }else{
                    eliminar = false;
                    JOptionPane.showMessageDialog(null, "DEBE HABER AL MENOS "
                            + "1 ADMINISTRADOR PARA ESTA ACCIÓN", "ERROR: "
                                    + "MINIMO DE ADMINISTRADORES",JOptionPane.ERROR_MESSAGE);
                }

            }

            if (eliminar) {
                String sql = "DELETE FROM empleado WHERE codigo_Empleado=?";
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, codigo);
                res = pstm.executeUpdate();

                JOptionPane.showMessageDialog(null, "SE ELIMINO EL EMPLEADO CON EXITO");
            } else {
                res = 1;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Código : "
                    + ex.getErrorCode() + "\nError :" + ex.getMessage());
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Código : "
                        + ex.getErrorCode() + "\nError :" + ex.getMessage());
            }
        }

        return res;
    }

    /**
     * @param empleado Objeto de la clase Empleado a insertar en la BBDD
     * @param password La contraseña que se le asigana al Administrador
     * @return rs resultado de la operación INSERT (Si es 1, la operación fue
     * exitosa)
     */
    public int agregarAdministrador(Empleado empleado, String password) {

        Connection con = null;
        PreparedStatement pstm;
        PreparedStatement pstm1;
        pstm = null;
        pstm1 = null;
        int rs = 0;

        try {
            con = ConexionBD.coneccion;
            String sql = "INSERT INTO empleado VALUES (?,?,?,?,?,?,?)";
            String sqlAdmin = "INSERT INTO administrador VALUES (?)";
            String sql1 = "INSERT INTO login values (?,?,0)";

            pstm1 = con.prepareStatement(sqlAdmin);
            pstm1.setInt(1, empleado.getCodigo());

            pstm = con.prepareStatement(sql);
            pstm.setInt(1, empleado.getCodigo());
            pstm.setString(2, empleado.getNombre());
            pstm.setString(3, empleado.getCorreo());
            pstm.setString(4, empleado.getSexo());
            pstm.setInt(5, empleado.getIdDependencia());
            pstm.setInt(6, empleado.getIdSubdependencia());
            pstm.setString(7, "");

            rs = pstm.executeUpdate();
            rs = pstm1.executeUpdate();

            pstm = con.prepareStatement(sql1);

            pstm.setInt(1, empleado.getCodigo());
            pstm.setString(2, password);

            pstm.executeUpdate();

            JOptionPane.showMessageDialog(null, "EMPLEADO ADMINISTRADOR REGISTRADO CON EXITO\n"
                    + "CODIGO: " + empleado.getCodigo() + "\nCONTRASEÑA: " + password);

            EnviaLogin(empleado.getCorreo(), empleado.getNombre() + "-ADMINISTRADOR",
                    empleado.getCodigo() + "", password);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Código : "
                    + ex.getErrorCode() + "\nError :" + ex.getMessage());
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Código : "
                        + ex.getErrorCode() + "\nError :" + ex.getMessage());
            }
        }

        return rs;
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
    public List<Empleado> buscarEmpleados(int dep, int sub, String nombre) {

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

    /**
     * El propósito del método es consultar todas las dependencias desde la
     * BBDD.
     *
     * @return Un ResultSet que incluye las dependencias por ID y Nombre
     */
    public ResultSet consultarDependencias() {

        Connection con = ConexionBD.coneccion;
        String consulta = "SELECT id_Dependencia as id, nombre_Dependencia as nombre FROM dependencia";
        ResultSet resultado = null;

        try {
            PreparedStatement statement = con.prepareStatement(consulta);

            resultado = statement.executeQuery();

        } catch (SQLException ex) {
            System.out.println("ERROR AL CONSULTAR DEPENDENCIA " + ex.getMessage());
        }

        return resultado;

    }

    /**
     * El propósito del método es consultar las subdependencias pertenecientes a
     * una dependencia
     *
     * @param id_Dependencia El ID de la dependencia a la que pertenece la
     * subdependencia
     * @return Un ResultSet con el ID y Nombre de las subdependenicas que le
     * pertenecen a la dependencia indicada.
     */
    public ResultSet consultarSubdependencias(int id_Dependencia) {

        Connection con = ConexionBD.coneccion;
        String consulta = "SELECT id_Subdependencia as id, nombre_Subdependencia as nombre FROM subdependencia ";
        consulta += "WHERE id_Dependencia = " + id_Dependencia;
        ResultSet resultado = null;

        try {
            PreparedStatement statement = con.prepareStatement(consulta);

            resultado = statement.executeQuery();

        } catch (SQLException ex) {
            System.out.println("ERROR AL CONSULTAR SUBDEPENDENCIA " + ex.getMessage());
        }
        return resultado;
    }

    public int cantidadAdministradores() {

        int cantidadAdmins = 0;

        Connection conexion = ConexionBD.coneccion;
        PreparedStatement statement;
        ResultSet resultadoConsulta;

        String consultaSQL = "SELECT COUNT(*) as cantidad FROM administrador";

        try {
            statement = conexion.prepareStatement(consultaSQL);

            resultadoConsulta = statement.executeQuery();

            if (resultadoConsulta.next()) {
                cantidadAdmins = resultadoConsulta.getInt("cantidad");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "EROR AL VERIFICAR SI ES ADMINISTRADOR",
                    "EROR SQL", JOptionPane.ERROR_MESSAGE);
        }

        return cantidadAdmins;

    }

    /**
     * El propósito del método es conocer si un Empleado es administrador
     * mediante su código
     *
     * @param codigo El código del Empleado
     * @return true si es administrador, false si no
     */
    public boolean esAdministrador(int codigo) {
        boolean esAdmini = false;

        Connection conexion = ConexionBD.coneccion;
        PreparedStatement statement;
        ResultSet resultadoConsulta;

        String consultaSQL = "SELECT * FROM administrador WHERE "
                + "id_Empleado = " + codigo;

        try {
            statement = conexion.prepareStatement(consultaSQL);

            resultadoConsulta = statement.executeQuery();

            if (resultadoConsulta.next()) {
                esAdmini = true;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "EROR AL VERIFICAR SI ES ADMINISTRADOR",
                    "EROR SQL", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Es administrador: "+esAdmini);
        return esAdmini;

    }

    /**
     * El propósito del método es enviar los datos del login una vez se cree un
     * Empleado.
     *
     * @param mailDestinatario El correo a quien se le enviará los datos del
     * login
     * @param nombreEmpleado El nombre del empleado
     * @param codigoEmpleado El código del empleado
     * @param contraseniaEmpleado La contraseña temporal del empleado
     */
    public void EnviaLogin(String mailDestinatario, String nombreEmpleado,
            String codigoEmpleado, String contraseniaEmpleado) {

        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");

        Session sesion = Session.getDefaultInstance(propiedad);
        String correoEnvia = "sistemanetu2021@gmail.com";
        String contrasena = "NetU2021";
        String receptor = mailDestinatario;//Destinatario 
        String asunto = "Datos Inicio Sesión";// Asunto correo
        String mensaje = "Cordial saludo Sr(a) " + nombreEmpleado
                + "\n\nSu código es: " + codigoEmpleado
                + " \n\nSu contraseña inicial es: " + contraseniaEmpleado
                + " \n\nTiene la opcion de actualizarla luego.\n\n"
                + "Gracias por su amable atención,\n\n"
                + "Administración.";// Mensaje  

        MimeMessage mail = new MimeMessage(sesion);

        try {
            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
            mail.setSubject(asunto);
            mail.setText(mensaje);

            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(correoEnvia, contrasena);
            transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transporte.close();
            JOptionPane.showMessageDialog(null, "Correo Enviado");// Confirmación de envío de correo

        } catch (AddressException ex) {
            System.out.println("ERROR " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("ERROR " + ex.getMessage());
        }
    }

}
