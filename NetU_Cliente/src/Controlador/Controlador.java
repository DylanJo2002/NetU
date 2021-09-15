/**
 * El propósito de la clase es servir de intermediario entre la conexión
 * y los paquetes de la vista
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Paquetes.Paquete;
import Paquetes.Perfil;
import Paquetes.PetIniciarSesion;
import Paquetes.ResIniciarSesion;
import Paquetes.CambiarDescripcion;
import Paquetes.CambiarFoto;
import Paquetes.ConsultaPerfiles;
import Paquetes.EliminarPublicacion;
import Paquetes.Publicacion;
import Paquetes.Publicaciones;
import Vista.PerfilUsuarios;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import vista.LoginGUI;
import vista.PrincipalGUI;

public class Controlador implements ActionListener, KeyListener {

    private Conexion conexion;
    private LoginGUI login;
    private PrincipalGUI principalGUI;

    /**
     * Constructor. Se inicializa la conexión y se empieza a escuchar (loop) a
     * NetU Servidor
     */
    public Controlador() {

        conexion = new Conexion(this);
        conexion.start();
        login = new LoginGUI(this,this);
        abrirLogin();

    }
    
  //DANIEL
     private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
  //DANIEL
    
    public void abrirLogin(){
        Thread hiloLogin = new Thread(login);
        hiloLogin.start();
    }

    /**
     * El propósito del método es iniciar el Form principal de NetU Cliente
     * cuando se haya hecho login.
     */
    public void iniciarPrincipalGUI(ResIniciarSesion resIniciarSesion) { 
        JOptionPane.showMessageDialog(null, "Hola");
        login.cerrar();
        principalGUI = new PrincipalGUI();
        asignarEscuchasVentanaPrincipal();
        
        Perfil perfil = resIniciarSesion.getPerfil();
        setCode(perfil.getCodigo());// DANIEL
        principalGUI.cargarInformacion(perfil.getNombre(), perfil.getCorreo(),
                perfil.getNombreDependencia(), perfil.getNombreSubdependencia(),
                perfil.getDescripcion(), perfil.getSexo(),perfil.getFoto());//DANIEL

        principalGUI.cargarPublicaciones(resIniciarSesion.getPublicaciones());
        
    }

    public void asignarEscuchasVentanaPrincipal() {
        principalGUI.asignarEscuchaAreaDescripcion(this);
        principalGUI.asignarEscuchaBtnModificarDescripcion(this);
        principalGUI.asignarEscuchaBtnEliminarPublicacion(this);
        principalGUI.escuchaBtnVerPerfil(this);//DANIEL
        principalGUI.escuchaBtnCambiarFoto(this);//DANIEL
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Escucha del login
        if (login != null) {

            if (e.getSource().equals(login.getBtnLogin())) {

                int codigo = login.getCodigo();
                String password = login.getPassword();

                if (codigo != -1 && codigo != 0 && !password.isEmpty()) {

                    PetIniciarSesion packet = new PetIniciarSesion(codigo, password);
                    packet.setTipo(Paquete.petIniciarSesion);
                    conexion.enviarPaquete(packet);

                } else {

                    String mensaje = "OCURRIERON ESTOS ERRORES AL INTENTAR "
                            + "INICIAR SESIÓN\n\n";

                    if (codigo == -1) {
                        mensaje += "El código no es válido \n";

                    } else {
                        if (codigo == 0) {
                            mensaje += "Debe ingresar un código \n";
                        }
                    }

                    if (password.isEmpty()) {
                        mensaje += "Debe ingresar una contraseña";
                    }

                    login.desplegarMensaje(1, "Error al iniciar sesión", mensaje);

                }

            }

        }

        if (principalGUI != null) {

            if (e.getSource().equals(principalGUI.getBtnModificarDescripcion())) {

                JButton botonModificar = principalGUI.getBtnModificarDescripcion();
                String textoModificar = botonModificar.getText();
                if (textoModificar.equals("Editar descripción")) {
                    principalGUI.modificarDescripcion(1);
                } else {
                    if (textoModificar.equals("Guardar")) {
                        String nuevaDescripcion = principalGUI.getTextAreaDescripcion();
                        if (!nuevaDescripcion.isEmpty()) {
                            principalGUI.setDescripcionEmpleado(nuevaDescripcion);

                            CambiarDescripcion paqueteDescripcion;
                            paqueteDescripcion = new CambiarDescripcion(nuevaDescripcion);
                            paqueteDescripcion.setTipo(Paquete.cambiarDescripcion);

                            conexion.enviarPaquete((Paquete) paqueteDescripcion);

                            principalGUI.modificarDescripcion(2);
                            principalGUI.desplegarMensajeDialogo(1, "DESCRIPCIÓN EDITADA",
                                    "La descripción se ha editado satisfactoriamente");
                        } else {
                            principalGUI.desplegarMensajeDialogo(1, "ERROR AL MODIFICAR LA"
                                    + "DESCRIPCION", "La descripción no puede ser "
                                    + "un texto vacío");

                            principalGUI.modificarDescripcion(2);
                        }
                    }
                }
            }

            if (principalGUI.getEscribirPublicacion() != null) {

                if (e.getSource().equals(principalGUI.getEscribirPublicacion().getBtnPublicar())) {

                    String contenidoPublicacion;
                    contenidoPublicacion = principalGUI.getEscribirPublicacion().getTextPublicacion();

                    if (!contenidoPublicacion.isEmpty()) {
                        principalGUI.getEscribirPublicacion().dispose();
                        Publicacion nuevaPublicacion;

                        nuevaPublicacion = new Publicacion();
                        nuevaPublicacion.setContenido(contenidoPublicacion);
                        nuevaPublicacion.setTipo(Paquete.publicar);

                        conexion.enviarPaquete(nuevaPublicacion);

                    } else {
                        principalGUI.desplegarMensajeDialogo(1, "ERROR AL PUBLICAR", ""
                                + "La publicación no puede estar vacía");
                    }

                }

            }

            if (e.getSource().equals(principalGUI.getBtnEliminarPublicacion())) {

                List<Integer> idPublicaciones;
                idPublicaciones = principalGUI.obtenerPublicacionesSeleccionadas();
                String usted = "";
                if (principalGUI.getSexo().equals("Masculino")) {
                    usted = "seguro";
                } else {
                    usted = "segura";
                }
                if (idPublicaciones != null) {
                    if (idPublicaciones.size() > 0) {
                        int respuesta = principalGUI.desplegarMensajeConfirmacion(
                                "CONFIRMACIÓN", "¿Está " + usted + " de eliminar "
                                + idPublicaciones.size() + " publicacion(es)?");

                        if (respuesta == JOptionPane.YES_OPTION) {
                            EliminarPublicacion eliminarPublicaciones;
                            eliminarPublicaciones = new EliminarPublicacion(idPublicaciones);
                            eliminarPublicaciones.setTipo(Paquete.eliminarPublicacion);
                            conexion.enviarPaquete(eliminarPublicaciones);
                        }
                        
                        principalGUI.desSeleccionarPublicaciones();
                    } else {
                        principalGUI.desplegarMensajeDialogo(1, "ERROR AL ELIMINAR PUBLICACION(ES)",
                                "Debe seleccionar al menos una publicación");
                    }
                } else {
                    principalGUI.desplegarMensajeDialogo(1, "ERROR AL ELIMINAR PUBLICACION(ES)",
                             "Usted no tiene ninguna publicación.\n ¡Comienza publicando una!");
                }

            }

        }
        
     //DANIEL
        if (principalGUI != null) {
            if (e.getSource().equals(principalGUI.getBtnVerPerfil())) {

                consultarPerfil();
            }
        }

        if (principalGUI != null) {

            if (e.getSource().equals(principalGUI.getBtnCambiarFoto())) {
                agregarFotoLocal();
            }
        }
     
     //DANIEL
     
     

    }

    /**
     * El propósito del método (Gestionar respuesta a iniciar sesion) es manejar
     * la información del paquete de acuerdo a lo que se envía desde el
     * servidor.
     *
     * @param resIniciarSesion La respuesta del servidor
     */
    public void gestResIniciarSesion(ResIniciarSesion resIniciarSesion) {

        if (resIniciarSesion.getExito() != 10) {

            iniciarPrincipalGUI(resIniciarSesion);

        } else {
            login.desplegarMensaje(1, "Error al iniciar sesión",
                    resIniciarSesion.getMensaje());
        }

    }

    public void cargarPublicaciones(Publicaciones publicaciones) {

        principalGUI.cargarPublicaciones(publicaciones);

    }
    
    
  //DANIEL
    public void consultarPerfil() {
        String codigo = JOptionPane.showInputDialog(
                "Ingresa Código de Empleado a Buscar", null);
        ConsultaPerfiles cp = new ConsultaPerfiles();
        cp.setCodigo(codigo);
        cp.setTipo(7);
        conexion.enviarPaquete(cp);
    }

    public void iniciarPerfiles(ConsultaPerfiles cp) {
        PerfilUsuarios perfilUsuarios = new PerfilUsuarios();
        asignarEscuchasVentanaPrincipal();
        Perfil perfil = cp.getPerfil();
        perfilUsuarios.cargarInformacion(perfil.getNombre(), perfil.getCorreo(),
                perfil.getNombreDependencia(), perfil.getNombreSubdependencia(),
                perfil.getDescripcion(), perfil.getSexo(), perfil.getFoto());
        perfilUsuarios.cargarPublicaciones(cp.getPublicaciones());        
        perfilUsuarios.setVisible(true);
        perfilUsuarios.setLocationRelativeTo(null);

    }
    
    public void agregarFotoLocal() {
        JFileChooser j = new JFileChooser("C:\\Users\\danie\\Desktop"
                + "\\Imagenes Perfiles");
        FileNameExtensionFilter fil = new FileNameExtensionFilter("JPEG,JPG, "
                + "PNG & GIF", "jpg", "png", "gif", "jpeg");
        j.setFileFilter(fil);

        int s = j.showOpenDialog(principalGUI);
        if (s == JFileChooser.APPROVE_OPTION) {
            InputStream input = null;
            try {
                File ruta = new File(j.getSelectedFile().getAbsolutePath());
                byte[] icono = new byte[(int) ruta.length()];
                input = new FileInputStream(ruta);
                input.read(icono);
                byte[] bi = icono;
                BufferedImage image = null;
                InputStream in = new ByteArrayInputStream(bi);
                image = ImageIO.read(in);
                ImageIcon imgi = new ImageIcon(image.getScaledInstance(150,150,0));
                principalGUI.setImagenPerfil(imgi);

                //Actualizar Foto en Servidor                                                 
                CambiarFoto cf = new CambiarFoto();
                cf.setCodigo(""+getCode());
                cf.setTipo(12);
                cf.setFoto(bi);
                conexion.enviarPaquete(cf);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Ingresa un archivo correcto");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ingresa un archivo correcto");
            }

        }
    }
    
  //DANIEL
    

    /**
     * El método permite desplegar un mensaje por medio de JOptionPane
     *
     * @param mensaje El mensaje que se desea mostrar
     */
//    public void desplegarMensajeDialogo(int tipo,String titulo,String mensaje) {
//
//        switch(tipo){
//            case 1: {
//                JOptionPane.showMessageDialog(null,mensaje, titulo, 
//                        JOptionPane.PLAIN_MESSAGE, icon);
//            }
//        }
//    }
    @Override
    public void keyTyped(KeyEvent e) {

        String numeros = "1234567890";
        String espacio = " ";

        if (login != null) {
            if (e.getSource().equals(login.getTxtCodigo())) {

                if (!numeros.contains("" + e.getKeyChar())) {
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                }

            } else {
                if (e.getSource().equals(login.getTxtPass())) {

                    if (espacio.contains("" + e.getKeyChar())) {
                        e.consume();
                        Toolkit.getDefaultToolkit().beep();
                    }

                }

            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (principalGUI != null) {
            if (e.getSource().equals(principalGUI.getTxtAreaDescripcion())) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    principalGUI.modificarDescripcion(2);
                }

            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
