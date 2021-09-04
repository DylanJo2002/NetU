/**
 * El propósito de la clase es servir de controlador entre la Vista y el Modelo
 */
package Controlador;

import Modelo.GestorBDAdministrador;
import Modelo.ConexionBD;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import Modelo.GestorBDEmpleado;
import Vista.Empleado;
import Vista.Login;
import Vista.Administracion;
import Vista.itemCombo;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controlador implements ActionListener, ItemListener, KeyListener {

    private GestorBDEmpleado gestorEmpleado;
    private Servidor servidor;
    private GestorBDAdministrador modelo;
    private Administracion vistaPrincipal;
    private Login login;

    /**
     * Constructor vacío
     */
    public Controlador() {

        //CREO LA CONEXION CON LA BD
        ConexionBD.Conectar();

        modelo = new GestorBDAdministrador();
        gestorEmpleado = new GestorBDEmpleado();
        servidor = new Servidor(gestorEmpleado);

        servidor.start();
        login = new Login();
        login.addActionListener(this);
        login.addKeyListener(this);

        abrirLogin(true);
    }

    /**
     * Permite mostar u oculta el login
     *
     * @param ocultar true para mostrar, false para ocultar el login
     */
    public void abrirLogin(boolean ocultar) {
        login.setVisible(ocultar);
    }

    /**
     * El método permite llenar los combos de las dependencias, siendo
     * intermediario
     */
    public void llenarCombosDependencias() {

        vistaPrincipal.setDependencias(modelo.consultarDependencias());

        vistaPrincipal.llenarCbxDependencias();

    }

    /**
     * Llena la tabla con todos los empleados existentes
     */
    public void llenaTabla() {
        List<Empleado> listadoEmpleados;
        listadoEmpleados = this.modelo.buscarEmpleados(0, 0, "");
        this.vistaPrincipal.cargarEmpleados(listadoEmpleados);

    }

    /**
     * El método permite registrar un Empleado u Administrador (intermediario)
     *
     * @param referencia Si es 1, se registra un empleado, sino, se registra un
     * administrador
     */
    public void registro(int referencia) {

        itemCombo dep = (itemCombo) (vistaPrincipal.getCbxDependenciaCU().getSelectedItem());
        itemCombo sub = (itemCombo) (vistaPrincipal.getCbxSubdependenciaCU().getSelectedItem());

        if (vistaPrincipal.getCodigo() == -1) {
            vistaPrincipal.mostrarMensaje("INGRESE UN CÓDIGO VÁLIDO PARA EL EMPLEADO", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } else if (vistaPrincipal.getNombre().equals("")) {
            vistaPrincipal.mostrarMensaje("INGRESE EL NOMBRE DEL EMPLEADO", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } else if (!esCorreo(vistaPrincipal.getCorreo())) {
            vistaPrincipal.mostrarMensaje("INGRESE UN CORREO VÁLIDO", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } else if (vistaPrincipal.getSexo().equals("Seleccionar")) {
            vistaPrincipal.mostrarMensaje("INGRESE EL SEXO DEL EMPLEADO", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } else if (dep.getId() == 0) {
            vistaPrincipal.mostrarMensaje("INGRESE LA DEPENDENCIA DEL EMPLEADO", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } else if (sub.getId() == 0) {
            vistaPrincipal.mostrarMensaje("INGRESE LA SUBDEPENDENCIA DEL EMPLEADO", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } else {

            Empleado empleado = new Empleado();

            empleado.setCodigo(vistaPrincipal.getCodigo());
            empleado.setNombre(vistaPrincipal.getNombre());
            empleado.setCorreo(vistaPrincipal.getCorreo());
            empleado.setSexo(vistaPrincipal.getSexo());
            empleado.setDependencia(dep.getId());
            empleado.setSubDependencia(sub.getId());

            int resultado = 0;

            if (referencia == 1) {
                resultado = modelo.agregarEmpleado(empleado, generarContraseña());
            } else {
                resultado = modelo.agregarAdministrador(empleado, generarContraseña());
            }

            if (resultado == 1) {

                llenaTabla();

                vistaPrincipal.activarControles(false);
                vistaPrincipal.accionRegistrar(1);
            } else {
                vistaPrincipal.mostrarMensaje("Error al grabar",
                        "Confirmación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * El método permite actualizar la información de un Empleado seleccionado
     * en la tabla
     */
    public void actualizar() {
        String mensaje = "";
        String sexo = vistaPrincipal.getSexo();
        String nombre = vistaPrincipal.getNombre();
        String correo = vistaPrincipal.getCorreo();

        itemCombo dep = (itemCombo) (vistaPrincipal.getCbxDependenciaCU().getSelectedItem());
        itemCombo sub = (itemCombo) (vistaPrincipal.getCbxSubdependenciaCU().getSelectedItem());

        if (nombre.isEmpty()) {
            mensaje += "Debe escribir un nombre\n";
        }

        if (!esCorreo(correo)) {
            mensaje += "Debe escribir un correo válido\n";
        }

        if (dep.getId() == 0) {
            mensaje += "Debe seleccionar una dependencia\n";
        }

        if (sub.getId() == 0) {
            mensaje += "Debe seleccionar una subdependencia\n";
        }

        if (sexo.equalsIgnoreCase("Seleccionar")) {
            mensaje += "Debe seleccionar un sexo\n";
        }

        if (!mensaje.isEmpty()) {
            mensaje = "ACTUALIZACION FALLIDA\n\n" + mensaje;
            vistaPrincipal.mostrarMensaje(mensaje, "Confirmación ", JOptionPane.ERROR_MESSAGE);
        } else {

            Empleado empleado = new Empleado();

            empleado.setCodigo(vistaPrincipal.getCodigo());
            empleado.setNombre(nombre);
            empleado.setCorreo(correo);
            empleado.setSexo(sexo);
            empleado.setDependencia(dep.getId());
            empleado.setSubDependencia(sub.getId());

            if (modelo.modificarEmpleado(empleado) == 1) {

                vistaPrincipal.activarControles(false);
                vistaPrincipal.accionModificar();

                vistaPrincipal.seleccionarItem(vistaPrincipal.valorCBXDependenciaListado, 0);
                llenaTabla();
            } else {
                vistaPrincipal.mostrarMensaje(
                        "ACTUALIZACION FALLIDA", "Confirmación ", JOptionPane.ERROR_MESSAGE);
            }

        }

    }

    /**
     * El método permite eliminar un Empleado seleccionado en la tabla
     */
    private void eliminar() {

        Empleado empleado = vistaPrincipal.getEmpleadoFromTable();

        int respuesta = JOptionPane.showConfirmDialog(null,
                "¿DESEA ELIMINAR EL EMPLEADO DE CODIGO : " + empleado.getCodigo() + " ?",
                "Confirmación de Acción", JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {

            if (modelo.eliminarEmpleado(empleado.getCodigo()) == 1) {
                llenaTabla();
            } else {
                JOptionPane.showMessageDialog(null,
                        "ERROR AL ELIMINAR EL EMPLEADO", "Confirmación de acción", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    /**
     * El método permite crear y abrir la ventana principal (Administración)
     */
    public void abrirVentanaPrincipal() {

        vistaPrincipal = new Administracion();

        vistaPrincipal.addListenerBtnRegistrarEmpleado(this);
        vistaPrincipal.addListenerBtnRegistrarAdmin(this);
        vistaPrincipal.addListenerBtnModEmpleado(this);
        vistaPrincipal.addListenerBtnElmEmpleado(this);
        vistaPrincipal.addListenerBtnBusListado(this);
        vistaPrincipal.addListenerBtnCerarSesion(this);
        vistaPrincipal.addKeyListener(this);

        llenaTabla();

        //LLENO LOS COMBOS DE LAS DEPENDENCIAS
        vistaPrincipal.addListenerCbxDependencias(this);
        llenarCombosDependencias();
    }

    /**
     * El méotodo permite consultar las dependencias y asignarlas a la vista
     * principal
     */
    public void llenarDependencias() {

        vistaPrincipal.setDependencias(modelo.consultarDependencias());

    }

    /**
     * El método permite obtener la información del o los empleados que se
     * desean buscar y cargarlos a la tabla
     */
    private void busqueda() {

        itemCombo dep = (itemCombo) (vistaPrincipal.getCbxDependenciaListado().getSelectedItem());
        itemCombo sub = (itemCombo) (vistaPrincipal.getCbxSubdependenciasListado().getSelectedItem());
        String nombre = vistaPrincipal.getNombreBusqueda().trim();

        vistaPrincipal.cargarEmpleados(modelo.buscarEmpleados(dep.getId(), sub.getId(), nombre));
    }

    /**
     * El propósito del método es generar una contraseña aleatorea
     *
     * @return Una contraseña temporal
     */
    private String generarContraseña() {

        String password = "";
        String abecedario = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTU"
                + "VWXYZ1234567890";

        for (int i = 0; i < 10; i++) {

            password += "" + abecedario.charAt((int) Math.floor(Math.random() * 61));

        }

        return password;
    }

    /**
     * El propósito del método es verificar un correo
     *
     * @param correo El correo que se quiere verificar
     * @return True si es un correo, False si no es así
     */
    public boolean esCorreo(String correo) {
        Pattern pat = null;
        Matcher mat = null;
        boolean match = false;
        pat = Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
        mat = pat.matcher(correo);

        return mat.find();
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {

        if (ie.getStateChange() == ItemEvent.SELECTED) {

            if (ie.getSource().equals(vistaPrincipal.getCbxDependenciaListado())) {

                itemCombo item = (itemCombo) vistaPrincipal.getCbxDependenciaListado().getSelectedItem();
                if (item.getId() != 0) {
                    vistaPrincipal.llenarCbxSubdpendencias(vistaPrincipal.valorCBXSubdependenciaListado, modelo.consultarSubdependencias(item.getId()));
                } else {
                    vistaPrincipal.llenarCbxSubdpendencias(vistaPrincipal.valorCBXSubdependenciaListado, null);
                }
            } else if (ie.getSource().equals(vistaPrincipal.getCbxDependenciaCU())) {

                itemCombo item = (itemCombo) vistaPrincipal.getCbxDependenciaCU().getSelectedItem();

                if (item.getId() != 0) {
                    vistaPrincipal.llenarCbxSubdpendencias(vistaPrincipal.valorCBXSubdependenciaCUP, modelo.consultarSubdependencias(item.getId()));
                } else {
                    vistaPrincipal.llenarCbxSubdpendencias(vistaPrincipal.valorCBXSubdependenciaCUP, null);
                }

            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getActionCommand().equalsIgnoreCase("REGISTRAR EMPLEADO")) {
            vistaPrincipal.accionRegistrar(1);
        } else if (ae.getActionCommand().equalsIgnoreCase("REGISTRAR ADMINISTRADOR")) {
            vistaPrincipal.accionRegistrar(2);
        } else if (ae.getActionCommand().equalsIgnoreCase("MODIFICAR")) {
            if (vistaPrincipal.accionModificar() != 0) {
                vistaPrincipal.cargarEmpleadoAModificar();
            }
        } else if (ae.getActionCommand().equalsIgnoreCase("GUARDAR")) {
            registro(1);
        } else if (ae.getActionCommand().equalsIgnoreCase("Registrar admin")) {
            registro(2);
        } else if (ae.getActionCommand().equalsIgnoreCase("Registrar emple")) {
            registro(1);
        } else if (ae.getActionCommand().equalsIgnoreCase("ACTUALIZAR")) {
            actualizar();
        } else if (ae.getActionCommand().equalsIgnoreCase("ELIMINAR")) {
            if (vistaPrincipal.accionEliminar() != 0) {
                eliminar();
            }

        } else if (ae.getActionCommand().equalsIgnoreCase("CANCELAR")) {
            vistaPrincipal.accionRegistrar(1);
        } else if (ae.getActionCommand().equalsIgnoreCase("BUSCAR")) {
            busqueda();
        }

        if (login != null) {
            if (ae.getSource().equals(login.getBtnLogin())) {

                int codigo = login.getCodigo();
                String password = login.getPassword();
                login.limpiarCampos();
                if (codigo != -1 && !password.isEmpty()) {

                    String mensajeSesion = modelo.iniciarSesion("" + codigo, password);

                    if (mensajeSesion.isEmpty()) {
                        abrirLogin(false);
                        abrirVentanaPrincipal();
                    } else {
                        JOptionPane.showMessageDialog(null, mensajeSesion,
                                "ERROR AL INICIAR SESION", JOptionPane.ERROR_MESSAGE);
                    }

                } else {

                    String mensaje = "ERROR: OCURRIERON ESTOS ERRORES AL INTENTAR "
                            + "INICIAR SESIÓN\n\n";

                    if (codigo == -1) {
                        mensaje += "El código no es válido \n";

                    }

                    if (password.isEmpty()) {
                        mensaje += "Debe ingresar una contraseña";
                    }

                    JOptionPane.showMessageDialog(null, mensaje);

                }

            }
        }

        if (vistaPrincipal != null) {
            if (ae.getSource().equals(vistaPrincipal.getBtnCerrarSesion())) {
                int respuestaCerrarSesion = 0;
                respuestaCerrarSesion = JOptionPane.showConfirmDialog(vistaPrincipal, "¿Está seguro de cerrar la sesión?",
                         "Cerrar sesión", 0);

                if (respuestaCerrarSesion == JOptionPane.YES_OPTION) {
                    vistaPrincipal.dispose();
                    abrirLogin(true);
                }

            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

        String charNoPermitidos = "|`]'\\!'\"#$%,:;<=>[";
        String espacio = " ";

        if (!e.getSource().equals(login.getTxtPasswordEmpleado())
                && !e.getSource().equals(login.getTxtCodigoEmpleado())) {

            if (e.getSource().equals(vistaPrincipal.getTxtNombreCU())) {

                if (charNoPermitidos.contains("" + e.getKeyChar())) {
                    Toolkit.getDefaultToolkit().beep();
                    e.consume();
                }

            } else {
                if (espacio.contains("" + e.getKeyChar())
                        || espacio.contains("" + e.getKeyChar())) {
                    Toolkit.getDefaultToolkit().beep();
                    e.consume();
                }
            }
        } else {

            if (espacio.contains("" + e.getKeyChar())
                    || espacio.contains("" + e.getKeyChar())) {
                Toolkit.getDefaultToolkit().beep();
                e.consume();
            }

        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
