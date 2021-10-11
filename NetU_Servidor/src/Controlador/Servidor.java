/**
 * Esta clase es el Servidor y hará todo lo relacionado él
 */

package Controlador;

import Modelo.GestorBDEmpleado;
import Paquetes.CambiarDescripcion;
import Paquetes.Chat;
import Paquetes.EliminarPublicacion;
import Paquetes.EnvioMensaje;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import Paquetes.Paquete;
import Paquetes.PetIniciarSesion;
import Paquetes.Publicacion;
import Paquetes.Publicaciones;
import Paquetes.ResIniciarSesion;
import java.util.ArrayList;
import java.util.List;

public class Servidor extends Thread {
    
    private List<HiloEmpleado> empleadosConectados;
    private GestorBDEmpleado gestorEmpleado;
    private ServerSocket sockServer;
    private final int puerto = 12345;
    /**
     * Constructor donde se le pasa el gestor de los empleados
     * @param gestorEmpleado El gestor del empelado que se le asignará a cada
     * uno de los Empleados
     */
    public Servidor(GestorBDEmpleado gestorEmpleado) {
        
        this.gestorEmpleado = gestorEmpleado;
        empleadosConectados = new ArrayList<>();
        try {
            sockServer = new ServerSocket(puerto);

        } catch (IOException ex) {
            System.out.println("Erro al iniciar el socket server: " + ex.getMessage());
        }

    }

    public void enviarChat(HiloEmpleado empleado, int codReceptor){
        Chat chat = new Chat();
        chat.setCodigoDestinatario(codReceptor);
        chat.setTipo(Paquete.chat);
        chat = gestorEmpleado.contruirChat(chat,empleado.codigo);
        empleado.enviarPaquete(chat);
    }
    
    public HiloEmpleado buscarEmpleado(int codEmpleado){
        for(HiloEmpleado empleado : empleadosConectados){
            if(empleado.codigo == codEmpleado){
                return empleado;
            }
        }
        return null;
    }
    
    @Override
    public void run() {
        while (true) {

            try {
               Socket sockEmpleado =  sockServer.accept();
               
               HiloEmpleado empleado = new HiloEmpleado(sockEmpleado, gestorEmpleado);
               empleadosConectados.add(empleado);
               empleado.start();
            } catch (IOException ex) {
                System.out.println("Erro al aceptar un empleado: " + ex.getMessage());
            }

        }
    }

    class HiloEmpleado extends Thread {

        public int codigo;
        private GestorBDEmpleado gestor;
        private Socket socket;
        private ObjectInputStream entrada;
        private ObjectOutputStream salida;
        /**
         * Constructor
         * @param sockEmpleado El socket del Empleado
         * @param gestor El gestor con el que se harán las consultas SQL
         */
        public HiloEmpleado(Socket sockEmpleado, GestorBDEmpleado gestor) {

            this.gestor = gestor;
            socket = sockEmpleado;

            abrirFlujos();
        }
        /**
         * Se inicializan los flujos
         */
        public void abrirFlujos() {

            try {
                salida = new ObjectOutputStream(socket.getOutputStream());
                entrada = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                System.out.println("Erro al abrir flujos: " + ex.getMessage());
            }

        }
        /**
         * Envía un paquete al Empleado (NetU Cliente)
         * @param paquete 
         */
        public void enviarPaquete(Paquete paquete) {

            try {
                salida.writeObject(paquete);
            } catch (IOException ex) {
                System.out.println("Erro al enviar el paquete: " + ex.getMessage());
            }

        }
        /**
         * El propósito del método es asignarle el código al Empleado únicamente
         * cuando la sesión haya sido correcta
         * @param exito El valor de éxito del Paquete ResIniciarSesion generado
         * @param codigo El código del Empleado
         */
        public void setCodigoEmpleado(int exito, int codigo){
            
            if(exito != Paquete.error){
                this.codigo = codigo;
            }
            
        }

        @Override
        public void run() {

            try {

                while (true) {
                    Paquete paquete = (Paquete) entrada.readObject();

                    if (paquete.getTipo() == Paquete.petIniciarSesion) {
                        
                        PetIniciarSesion peticionIniciarSesion;
                        peticionIniciarSesion = (PetIniciarSesion) paquete;
                        ResIniciarSesion respuestaIniciarSesion;
                        
                        respuestaIniciarSesion = gestor.iniciarSesion(peticionIniciarSesion);                       
                        respuestaIniciarSesion.setTipo(Paquete.resIniciarSesion);
                        
                        setCodigoEmpleado(respuestaIniciarSesion.getExito(),
                                peticionIniciarSesion.getCodigo());
                        
                        enviarPaquete(respuestaIniciarSesion);
                        
                    }else{
                    
                    if(paquete.getTipo() == Paquete.cambiarDescripcion){
                        CambiarDescripcion cambiarDescripcion;
                        cambiarDescripcion = (CambiarDescripcion) paquete;
                        
                        gestorEmpleado.cambiarDescripcion(codigo,
                                cambiarDescripcion.getNuevaDescripcion());
                    }else{
                    
                    if(paquete.getTipo() == Paquete.publicar){
                        
                        Publicacion publicacion = (Publicacion) paquete;
                        publicacion.setId_Empleado(codigo);
                        
                        Publicaciones publicaciones;
                        
                        publicaciones = gestorEmpleado.crearPublicacion(publicacion);
                        publicaciones.setTipo(Paquete.publicaciones);
                        
                        enviarPaquete(publicaciones);                       
                        
                    }else{
                    
                    if(paquete.getTipo() == Paquete.eliminarPublicacion){
                        EliminarPublicacion eliminarPublicacion;
                        eliminarPublicacion = (EliminarPublicacion) paquete;                  
                        eliminarPublicacion.setCodigoEmpleado(codigo);
                        
                        gestorEmpleado.eliminarPublicaciones(eliminarPublicacion);
                        
                        Publicaciones publicaciones;
                        publicaciones = gestorEmpleado.listarPublicaciones(codigo);
                        publicaciones.setTipo(Paquete.publicaciones);
                        enviarPaquete(publicaciones);
                        
                    }else{
                    
                    if(paquete.getTipo() == Paquete.chat){
                        Chat peticionChat = (Chat) paquete;
                        peticionChat = gestorEmpleado
                                .contruirChat(peticionChat, codigo);
                        enviarPaquete(peticionChat);
                    }else{
                    
                    if(paquete.getTipo() == Paquete.envioMensaje){
                        EnvioMensaje mensaje = (EnvioMensaje) paquete;
                        gestorEmpleado.crearMensaje(mensaje, codigo);
                        Chat chatActual = new Chat();
                        chatActual.setCodigoDestinatario(mensaje.getCodigoDestinatario());
                        chatActual.setTipo(Paquete.chat);
                        enviarPaquete(gestorEmpleado.contruirChat(chatActual, codigo));
                        HiloEmpleado empDestinatario = 
                            buscarEmpleado(mensaje.getCodigoDestinatario());
                        if(empDestinatario != null && 
                                gestorEmpleado.chatAbierto(mensaje.
                                        getCodigoDestinatario(), codigo)){
                                enviarChat(empDestinatario, codigo);
                        }
                        
                    }else{
                    if(paquete.getTipo() == Paquete.cerrarChat){
                        gestorEmpleado.cerrarChat(codigo);
                    }
                    }
                    }
                    }                     
                    }    
                    }                       
                    }
                }

            } catch (IOException | ClassNotFoundException ex) {
                gestorEmpleado.cambiarEstadoEmpleado(codigo, false);
                empleadosConectados.remove(this);
                System.out.println("Erro al leer un paquete: " + ex.getMessage()+" "+empleadosConectados.size());
            }

        }

    }

}
