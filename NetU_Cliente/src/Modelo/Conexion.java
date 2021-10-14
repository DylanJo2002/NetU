/**
 * El propósito de la clase es gestionar todo lo relacionado a la conexión
 * por medio de Socket con NetU Servidor
 */
package Modelo;


import Controlador.Controlador;
import Paquetes.Bandeja;
import Paquetes.Chat;
import Paquetes.ConsultaNotificacion;
import Paquetes.ConsultaPerfiles;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Paquetes.Publicaciones;
import Paquetes.Paquete;
import Paquetes.ResIniciarSesion;
import Paquetes.RespuestaBusqueda;


public class Conexion extends Thread {

    Controlador controlador;
    public static final String ipServidor = "127.0.0.1";
    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    private int conexionCerrada;

    /**
     * Constructor. Inicializa el socket y abre los flujos usando abrirFlujos()
     *
     * @param controlador Objeto de la clase Controlador. Para servir
     * intermediario a la hora de recibir paquetes.
     */
    public Conexion(Controlador controlador) {

        this.controlador = controlador;

        try {
            socket = new Socket(ipServidor, 12345);
        } catch (IOException ex) {
            System.out.println("Erro al inicializar el socket: " + ex.getMessage());
        }

        abrirFlujos();

    }

    /**
     * El propósito del método es inicializar el ObjectOutputStream y
     * ObjectInputStream del Socket. Por medio de estos se hace la comunicación
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
     * El propósito del método es cerrar tanto los flujos como el socket para
     * que el término de la conexión sea limpio
     */
    public void cerrarFliujos() {

        conexionCerrada = -1;

        try {
            entrada.close();
            salida.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar flujos: " + ex.getMessage());
        }

    }

    /**
     * El propósito del método es enviar Paquetes hacia NetU Servidor
     *
     * @param paquete El Paquete que se desea enviar a NetU Servidor
     */
    public void enviarPaquete(Paquete paquete) {

        try {
            salida.writeObject(paquete);
            System.out.println("Envié el paquete: ");

        } catch (IOException ex) {
            System.out.println("Erro al enviar el paquete: " + ex.getMessage());
        }
        
    }

    /**
     * El propósito del método es escuchar cualquier Paquete que llegue desde
     * NetU Servidor y así generar acciones mediante el Controlador
     */
    @Override
    public void run() {
        try {

            while (conexionCerrada == 0) {
                
                Paquete paquete = (Paquete) entrada.readObject();

                switch (paquete.getTipo()) {

                    case Paquete.resIniciarSesion: {
                        controlador.gestResIniciarSesion((ResIniciarSesion) paquete);
                    } break;
                    
                    case Paquete.publicaciones: {
                        controlador.cargarPublicaciones((Publicaciones) paquete);
                    } break;
                    
                    case Paquete.respuestaBusqueda: {
                        
                        RespuestaBusqueda resBusqueda;
                        resBusqueda = (RespuestaBusqueda) paquete;
                        controlador.cargarEmpleados(resBusqueda.getEmpleados());
                    } break;    
                    case Paquete.chat: {
                        controlador.construirChat((Chat) paquete);
                        System.out.println("RECIBÍ CHAT");
                        
                        
                    } break;

                  //DANIEL
                    case Paquete.consultaPerfil: {                        
                        controlador.iniciarPerfiles((ConsultaPerfiles)paquete);                        
                    } break;
                    case Paquete.consultaNotificacion: {                        
                        controlador.cargarNotificaciones((ConsultaNotificacion)paquete);   
                        System.out.println("");
                    } break;
                    
                  //DANIEL
                    case Paquete.bandeja: {
                        controlador.cargarElementosBandeja((Bandeja) paquete);
                        System.out.println("RECIBÍ BANDEJA");
                    } break;
               }
            }
        } catch (IOException ex) {
            System.out.println("Error al escuchar al servidor: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al escuchar al servidor: " + ex.getMessage());
        }

    }

}
