/**
 * El propósito de la clase es servir de intermediario entre la conexión
 * y los paquetes de la vista
 */
package Controlador;

import Modelo.Conexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Paquetes.Paquete;
import Paquetes.Perfil;
import Paquetes.PetIniciarSesion;
import Paquetes.ResIniciarSesion;
import Paquetes.CambiarDescripcion;
import Paquetes.Chat;
import Paquetes.EliminarPublicacion;
import Paquetes.PeticionBusqueda;
import Paquetes.EnvioMensaje;
import Paquetes.Publicacion;
import Paquetes.Publicaciones;
import Vista.Empleado;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import vista.ChatGUI;
import vista.LoginGUI;
import vista.PrincipalGUI;

public class Controlador implements ActionListener, KeyListener, WindowListener {

    private Conexion conexion;
    private LoginGUI login;
    private PrincipalGUI principalGUI;
    private ChatGUI chatGui;
    private int chatKeyPressed;
    private String nombreChat;

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
    
    public void abrirLogin(){
        Thread hiloLogin = new Thread(login);
        hiloLogin.start();
    }

    /**
     * El propósito del método es iniciar el Form principal de NetU Cliente
     * cuando se haya hecho login.
     */
    public void iniciarPrincipalGUI(ResIniciarSesion resIniciarSesion) { 
        login.cerrar();
        principalGUI = new PrincipalGUI();
        asignarEscuchasVentanaPrincipal();
        
        Perfil perfil = resIniciarSesion.getPerfil();

        principalGUI.cargarInformacion(perfil.getNombre(), perfil.getCorreo(),
                perfil.getNombreDependencia(), perfil.getNombreSubdependencia(),
                perfil.getDescripcion(), perfil.getSexo());

        principalGUI.cargarPublicaciones(resIniciarSesion.getPublicaciones());
        
        principalGUI.setDependencias(resIniciarSesion.getDependencias());
        principalGUI.setSubdependencias(resIniciarSesion.getSubdependencias());
        
        principalGUI.llenarCbxDependencias();
        
    }

    public void asignarEscuchasVentanaPrincipal() {
        principalGUI.asignarEscuchaAreaDescripcion(this);
        principalGUI.asignarEscuchaBtnModificarDescripcion(this);
        principalGUI.asignarEscuchaBtnEliminarPublicacion(this);
        principalGUI.asignarEscuchaBtnBuscar(this);
        principalGUI.asignarEscuchaBtnEnviarMensaje(this);
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
            
            if(e.getSource().equals(principalGUI.getBtnEnviarMensaje())){
                String codigoInput = JOptionPane.showInputDialog("Escriba el código del empleado");       
                if(chatGui != null){
                    JOptionPane.showMessageDialog(null,"Cierra el chat con "
                            .concat(chatGui.getTitle()).concat(" para abrir otro")); 
                    return;
                }
                try {
                    nombreChat = "NOMBRE DEL EMPLEADO";
                    int codigoEmpleado = Integer.parseInt(codigoInput);
                    Chat peticionChat = new Chat();
                    peticionChat.setCodigoDestinatario(codigoEmpleado);
                    peticionChat.setTipo(Paquete.chat);
                    conexion.enviarPaquete(peticionChat);
                }catch(NumberFormatException exception){
                    JOptionPane.showMessageDialog(null,"Debe ingresar un código"
                            .concat(" válido"));
                }
                
            }
            
            if(e.getSource().equals(principalGUI.getBtnBuscar())){
                
                int idDependencia = principalGUI.getIdDependecia();
                int idSubdependencia = principalGUI.getIdSubdependecia();
                String nombre = principalGUI.getTxtNombreBusqueda();
                
                PeticionBusqueda petBusqueda;
                petBusqueda = new PeticionBusqueda(idDependencia, idSubdependencia, nombre);
                petBusqueda.setTipo(Paquete.peticionBusqueda);
                
                conexion.enviarPaquete(petBusqueda);
            }

        }
        
        if(chatGui != null){
            if(e.getSource().equals(chatGui.getBtnEnviar())){
                enviarMensaje();
            }
        }

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
            /*ArrayList<itemCombo> dep = resIniciarSesion.getDependencias();
            ArrayList<ArrayList<itemCombo>> subDep = resIniciarSesion.getSubdependencias();

            for(itemCombo item: dep){
                System.out.print(item.getNombre()+ "  ");   
            }
            
            
            for(ArrayList<itemCombo> fila: subDep){
                System.out.print(subDep.indexOf(fila) + "   ");
                
                for(itemCombo item: fila){
                    System.out.print(item.getNombre());
                }
            }*/
            
        } else {
            login.desplegarMensaje(1, "Error al iniciar sesión",
                    resIniciarSesion.getMensaje());
        }

    }

    public void cargarPublicaciones(Publicaciones publicaciones) {

        principalGUI.cargarPublicaciones(publicaciones);

    }
    

    public void cargarEmpleados(List<Empleado> empleados){
        principalGUI.cargarEmpleados(empleados);
    }
    public void enviarMensaje(){
        String mensaje =  chatGui.obtenerMensaje();
        if(mensaje.isEmpty()){
            principalGUI.desplegarMensajeDialogo(1,"Mensaje vacío",
                    "Asegúrese de escribir algo antes de enviar un mensaje");
            return;
        }
        EnvioMensaje paqueteMensaje = new EnvioMensaje(mensaje,
        chatGui.getCodigoDestinatario());
        paqueteMensaje.setTipo(Paquete.envioMensaje);
        conexion.enviarPaquete(paqueteMensaje);
        chatGui.limpiarTxtMensaje();
        System.out.println("Mensaje enviado");
    }

    public void construirChat(Chat chat){
        if(chatGui == null){
            chatGui = new ChatGUI(nombreChat, 
                    chat.getMensajes(),chat.getCodigoDestinatario());
            chatGui.setKeyListener(this);
            chatGui.setWindowsListener(this);
            chatGui.setActionListener(this);
            return;
        }
        
        chatGui.cargarMensages(chat.getMensajes());
    }
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
                return;
            }
        }
        
       if(chatGui != null){
            if(chatKeyPressed == KeyEvent.VK_SHIFT && 
                    e.getKeyCode()==KeyEvent.VK_ENTER){
                System.out.println("Enter");
                chatGui.saltoLineaMensaje();
            } else {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    enviarMensaje();
                }
            }           
            chatKeyPressed = e.getKeyCode();
            System.out.println("Tecla "+e.getKeyCode());           
       }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Paquete cerrarChat = new Paquete();
        cerrarChat.setTipo(Paquete.cerrarChat);
        conexion.enviarPaquete(cerrarChat);
        chatGui.dispose();
        chatGui = null;
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

}
