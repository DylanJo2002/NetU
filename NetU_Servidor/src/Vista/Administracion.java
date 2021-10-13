/**
 * El propósito de este Form es servir de vista para todo lo que tenga que ver
 * con el Empleado (Administrador) que inicie sesión en el servidor.
 */
package Vista;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class Administracion extends javax.swing.JFrame {

    private ArrayList<itemCombo> dependencias; //Almacena el nombre de las dependencias
    private TMEmpleado tableModel;
    public static final int valorCBXDependenciaListado = 1;
    public static final int valorCBXDependenciaCUP = 2;
    public static final int valorCBXSubdependenciaCUP = 3;
    public static final int valorCBXSubdependenciaListado = 4;

    /**
     * Constructor vacío Crea el TMEmpleado, el modelo de tabla para la lista de
     * empleados. Inicializa los componentes (Método generado automáticamente
     * por Form)
     *
     */
    public Administracion() {
        tableModel = new TMEmpleado();
        initComponents();
        setTitle("Administracion - NetU Servidor");
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        dependencias = new ArrayList();
        activarControles(false);
        setWidthCombo();
    }

    /**
     * Mediante la librería BoundsPopupMenuListener permite desplegar los
     * valores de los combobox de acuerdo al tamño de sus carácteres
     */
    public void setWidthCombo() {

        BoundsPopupMenuListener listener;
        listener = new BoundsPopupMenuListener(true, false);
        cbxSubdependenciaCU.addPopupMenuListener(listener);
        cbxSubdependenciaCU.setPrototypeDisplayValue(new itemCombo(0, "ItemWWW"));
        cbxSubdependenciaListado.addPopupMenuListener(listener);
        cbxSubdependenciaListado.setPrototypeDisplayValue(new itemCombo(0, "ItemWWW"));
    }
    

    /**
     * Muestra un mensaje por medio de JOptionPane
     *
     * @param mensaje El mensaje que se quiere mostrar
     * @param titulo El título del JOptionPane
     * @param icono El ícono (error y demás).
     */
    public void mostrarMensaje(String mensaje, String titulo, int icono) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, icono);
    }

    /**
     * Este método permite cargar y mostrar los empleados en la tabla
     *
     * @param empleados Una List<Empleado> quienes queremos mostrar en la tabla
     */
    public void cargarEmpleados(List<Empleado> empleados) {
        tableModel.setEmpleados(empleados);
        tablaEmpleados.updateUI();
    }
    
    //DANIEL
    @Override
    public Image getIconImage(){
        Image retValue = Toolkit.getDefaultToolkit().getImage(
                ClassLoader.getSystemResource("Imagen/netU_icon.png"));
        return retValue;
    }
    //DANIEL
    /**
     * @return El nombre que se escribió en la seccion CU(crear o modificar)
     */
    public String getNombre() {
        return txtNombreCU.getText().trim();
    }

    /**
     * @return Devuelve el correo que se escribió en la seccion CU(crear o
     * modificar)
     */
    public String getCorreo() {
        return txtCorreoCU.getText().trim();
    }

    /**
     * @return Devuelve el sexo que se escribió en la sección CU(crear o
     * modificar)
     */
    public String getSexo() {
        return cbxSexo.getSelectedItem().toString().trim();
    }

    /**
     * @return Devuelve el código que se escribió en la sección CU(crear o
     * modificar), si hay un error o un código inválido devuelve -1
     */
    public int getCodigo() {
        int codigo = -1;

        try {

            codigo = Integer.parseInt(txtCodigo.getText());

        } catch (NumberFormatException e) {
            codigo = -1;
        }

        if (codigo == 0) {
            codigo = -1;
        }

        return codigo;
    }

    /**
     * @return Devuelve el nombre que se digitó en el textField de búsqueda para
     * listar empleado(s)
     */
    public String getNombreBusqueda() {
        return txtNombreBusqueda.getText();
    }

    /**
     * @return Devuelve el combobox de las dependencias en la tabla de los
     * empleados
     */
    public JComboBox<itemCombo> getCbxDependenciaListado() {
        return cbxDependenciaListado;
    }

    /**
     * @return Devuelve el combo de dependencias del CU (Create, Update)
     */
    public JComboBox<itemCombo> getCbxDependenciaCU() {
        return cbxDependenciasCU;
    }

    /**
     * @return Devuelve el combo de subdependencia del CU (Create, Update)
     */
    public JComboBox<itemCombo> getCbxSubdependenciaCU() {
        return cbxSubdependenciaCU;
    }

    /**
     * @return Devuelve el combo de subdependencia en la tabla de los empleados
     */
    public JComboBox<itemCombo> getCbxSubdependenciasListado() {
        return cbxSubdependenciaListado;
    }

    /**
     * El propósito del método es llenar los combos de ambas dependencias cuando
     * ya se tienen los nombres en el arreglo 'dependencias'. Además de eso, le
     * agrega un ítem primero a cada uno, para cada caso.
     */
    public void llenarCbxDependencias() {

        cbxDependenciaListado.removeAllItems();
        cbxDependenciasCU.removeAllItems();

        cbxDependenciaListado.addItem(new itemCombo(0, "TODAS"));
        cbxDependenciasCU.addItem(new itemCombo(0, "SELECCIONAR"));

        for (itemCombo item : dependencias) {
            cbxDependenciaListado.addItem(item);
            cbxDependenciasCU.addItem(item);
        }
    }

    /**
     * El propósito del método es llenar cualquiera de los dos combos de
     * subdependencia de acuerdo a una consulta que se pasa por parámetro
     *
     * @param combo El número del combo que se llenará (definidos como static al
     * principio de la clase-Form Administracion)
     * @param consulta El resultado de la consulta a las subdependencias en la
     * base de datos (de acuerdo a una dependencia)
     */
    public void llenarCbxSubdpendencias(int combo, ResultSet consulta) {

        JComboBox<itemCombo> combobox = null;

        switch (combo) {

            case valorCBXSubdependenciaListado: {
                combobox = cbxSubdependenciaListado;
                combobox.removeAllItems();
                combobox.addItem(new itemCombo(0, "TODAS"));
            }
            break;
            case valorCBXSubdependenciaCUP: {
                combobox = cbxSubdependenciaCU;
                combobox.removeAllItems();
                combobox.addItem(new itemCombo(0, "SELECCIONAR"));
            }
            break;

        }

        try {

            if (consulta != null) {
                while (consulta.next()) {

                    combobox.addItem(new itemCombo(Integer.parseInt(consulta.getString("id")),
                            consulta.getString("nombre")));

                }

            }
        } catch (SQLException ex) {
            System.out.println("Error SQL al llenar los combos de subdependencias");;
        }

    }

    //Se agregan escuchas a los botones
    /**
     * Asigna una escucha al botón de modificar empleado
     *
     * @param listenPrograma Quien escucha
     */
    public void addListenerBtnModEmpleado(ActionListener listenPrograma) {
        btnModEmpleados.addActionListener(listenPrograma);
    }

    /**
     * Se agrega una escucha al botón de eliminar empleado
     *
     * @param listenPrograma Quien escucha
     */
    public void addListenerBtnElmEmpleado(ActionListener listenPrograma) {
        btnElmEmpleados.addActionListener(listenPrograma);
    }

    /**
     * Agrega una escucha al botón de registrar empleado
     *
     * @param listenPrograma Quien escucha
     */
    public void addListenerBtnRegistrarEmpleado(ActionListener listenPrograma) {
        btnRegistrarEmpleados.addActionListener(listenPrograma);
    }

    /**
     * Agrega una escucha al botón de registrar administrador
     *
     * @param listenPrograma Quien escucha
     */
    public void addListenerBtnRegistrarAdmin(ActionListener listenPrograma) {
        btnRegistrarAdmin.addActionListener(listenPrograma);
    }

    /**
     * Agrega una escucha al botón de buscar, en el listado de los empleados
     *
     * @param listenPrograma QuIEN ESCUCHA
     */
    public void addListenerBtnBusListado(ActionListener listenPrograma) {
        btnBusListado.addActionListener(listenPrograma);
    }
    
    public void addListenerBtnCerarSesion(ActionListener listenPrograma) {
        btnCerrarSesion.addActionListener(listenPrograma);
    }    

    /**
     * Añade una escucha a los dos combos de las dependencias (solo se añade a
     * estos porque de acuerdo a la dependencia que elijan se mostrará su
     * subdependencia)
     *
     * @param al Quien escucha
     */
    public void addListenerCbxDependencias(ItemListener al) {
        cbxDependenciasCU.addItemListener(al);
        cbxDependenciaListado.addItemListener(al);
    }
    

    /**
     * Añade escuchas a todos los textFields de el Form Administración para
     * evitar el ingreso de datos erróneos
     *
     * @param kl Quien escucha
     */
    public void addKeyListener(KeyListener kl) {
        txtNombreCU.addKeyListener(kl);
        txtNombreBusqueda.addKeyListener(kl);
        txtCorreoCU.addKeyListener(kl);
        txtCodigo.addKeyListener(kl);
    }

    /**
     * Metodo para activar y desactivar componentes del CU (Create, Update)
     *
     * @param estado, si es TRUE se activan los textFields y comboboxs del CU y
     * se desactiva la tabla. Con FALSE se desctivan los textFields y comboboxs
     * del CU y se activa la tabla
     */
    public void activarControles(boolean estado) {
        txtNombreCU.setEnabled(estado);
        txtCorreoCU.setEnabled(estado);
        txtCodigo.setEnabled(estado);
        cbxDependenciasCU.setEnabled(estado);
        cbxSexo.setEnabled(estado);
        cbxSubdependenciaCU.setEnabled(estado);
        tablaEmpleados.setEnabled(!estado);
    }

    /**
     * El propósito del método es limpiar la parte del CU (Create, Update).
     */
    public void limpiar() {
        txtNombreCU.setText("");
        txtCorreoCU.setText("");
        txtCodigo.setText("");
        txtNombreBusqueda.setText("");
        cbxSexo.setSelectedItem("Seleccionar");
        seleccionarItem(valorCBXDependenciaCUP, 0);
        seleccionarItem(valorCBXSubdependenciaCUP, 0);

    }

    /**
     * El propósito del método es activar o desactivar los controles del CU
     * (Create, Update) según el estado de los botones. Se limpian los textFiels
     * se cambian los textos de los botones del CU y sus actionCommand
     * dependiendo del caso. El método se llamará siempre que se de click en los
     * botones del CU que tengan que ver con registrar.
     *
     * @param referencia Si es 1 y alguno de los dos botones de registrar
     * empleado o registrar administrador están activados, entonces indica el
     * actionCommand es el de registrar un empleado, sino, es el de registrar un
     * administrador
     */
    public void accionRegistrar(int referencia) {

        limpiar();

        if (btnRegistrarEmpleados.isEnabled() || btnRegistrarAdmin.isEnabled()) {

            activarControles(true);
            btnRegistrarEmpleados.setEnabled(false);
            btnRegistrarAdmin.setEnabled(false);
            btnElmEmpleados.setText("Cancelar");
            btnElmEmpleados.setActionCommand("Cancelar");
            btnModEmpleados.setText("Registrar");
            txtNombreCU.requestFocusInWindow();

            if (referencia == 1) {
                btnModEmpleados.setActionCommand("Registrar emple");
            } else {
                btnModEmpleados.setActionCommand("Registrar admin");
            }

        } else {
            activarControles(false);
            btnRegistrarEmpleados.setEnabled(true);
            btnRegistrarAdmin.setEnabled(true);
            btnModEmpleados.setText("Modificar");
            btnModEmpleados.setActionCommand("Modificar");
            btnElmEmpleados.setText("Eliminar");
            btnElmEmpleados.setActionCommand("Eliminar");
            txtNombreCU.requestFocusInWindow();
            seleccionarItem(valorCBXDependenciaCUP, 0);
        }
    }

    /**
     * El propósito del método es activar y modificar el Form cuando se
     * seleccione la operación de modificar un empleado.
     *
     * @return El método retorna 0 si no existe un empleado seleccionado o 1 si
     * un empleado está seleccionado y
     */
    public int accionModificar() {

        int modificar = 0;

        if (btnModEmpleados.getText().equals("Modificar")) {

            if (tablaEmpleados.getSelectedRow() == -1) {

                if (tablaEmpleados.getRowCount() == 0) {
                    mostrarMensaje("No existen registros en la tabla", "Error al eliminar"
                            + "un empelado", JOptionPane.ERROR_MESSAGE);
                } else {
                    mostrarMensaje("Debe seleccionar un empleado de la tabla", "Error al eliminar"
                            + "un empelado", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                activarControles(true);
                txtCodigo.setEnabled(false);
                btnRegistrarEmpleados.setEnabled(false);
                btnRegistrarAdmin.setEnabled(false);
                tablaEmpleados.setEnabled(false);

                btnModEmpleados.setText("Actualizar");
                btnModEmpleados.setActionCommand("Actualizar");
                btnElmEmpleados.setText("Cancelar");
                btnElmEmpleados.setActionCommand("Cancelar");
                txtNombreCU.requestFocusInWindow();
                modificar = 1;
            }
        } else {
            limpiar();
            btnRegistrarEmpleados.setEnabled(true);
            btnRegistrarAdmin.setEnabled(true);
            tablaEmpleados.setEnabled(true);

            btnModEmpleados.setText("Modificar");
            btnModEmpleados.setActionCommand("Modificar");
            btnElmEmpleados.setText("Eliminar");
            btnElmEmpleados.setActionCommand("Eliminar");
            txtNombreCU.requestFocusInWindow();

        }

        return modificar;
    }

    /**
     * El propósito del método es verificar si existe un empleado seleccionado
     * en la tabla antes de eliminarlo.
     *
     * @return Retorna 0 si no existe un empleado seleccionado en la tabla o 1
     * en caso contrario. Lanza un mensaje cuando no existe un empleado
     * seleccionado
     */
    public int accionEliminar() {

        int eliminar = 0;

        if (tablaEmpleados.getSelectedRow() == -1) {

            if (tablaEmpleados.getRowCount() == 0) {
                mostrarMensaje("No existen registros en la tabla", "Error al eliminar"
                        + "un empelado", JOptionPane.ERROR_MESSAGE);
            } else {
                mostrarMensaje("Debe seleccionar un empleado de la tabla", "Error al eliminar"
                        + "un empelado", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            eliminar = 1;
        }

        return eliminar;
    }

    /**
     * El propósito del método es cargar todos los datos del empleado en la
     * parte del CU (Create, Update) cuando se desea modificarlo.
     */
    public void cargarEmpleadoAModificar() {

        Empleado empleado = getEmpleadoFromTable();

        txtNombreCU.setText(empleado.getNombre());
        txtCorreoCU.setText(empleado.getCorreo());

        seleccionarItem(valorCBXDependenciaCUP, empleado.getIdDependencia());
        seleccionarItem(valorCBXSubdependenciaCUP, empleado.getIdSubdependencia());

        cbxSexo.setSelectedItem(empleado.getSexo());

        txtCodigo.setText("" + empleado.getCodigo());

    }
    /**
     * 
     * @param valorCBX  El índice del combobox donde existe el ítem que se desea
     * seleccionar
     * @param codigoitem El código del itemCombo que se desea seleccionar
     */
    public void seleccionarItem(int valorCBX, int codigoitem) {

        JComboBox<itemCombo> combobox = null;

        switch (valorCBX) {

            case valorCBXDependenciaListado: {
                combobox = cbxDependenciaListado;
            }
            break;

            case valorCBXDependenciaCUP: {
                combobox = cbxDependenciasCU;
            }
            break;

            case valorCBXSubdependenciaCUP: {
                combobox = cbxSubdependenciaCU;
            }
            break;

            case valorCBXSubdependenciaListado: {
                combobox = cbxSubdependenciaListado;
            }
            break;
        }

        if (combobox != null) {
            for (int i = 0; i < combobox.getItemCount(); i++) {

                itemCombo item = combobox.getItemAt(i);

                if (item.getId() == codigoitem) {
                    combobox.setSelectedItem(item);
                    break;
                }
            }

        }

    }
    /**
     * El propósito del método es asignar las dependencias que retorna una
     * consulta SQL de las mismas.
     * @param consultaDependencias La consulta SQL del nombre y código de las
     * dependencias
     */
    public void setDependencias(ResultSet consultaDependencias) {
        try {
            dependencias.clear();

            while (consultaDependencias.next()) {

                dependencias.add(new itemCombo(Integer.parseInt(consultaDependencias.getString("id")), consultaDependencias.getString("nombre")));

            }
        } catch (SQLException ex) {
            mostrarMensaje("Ocurrió el siguiente error tipo SQL"+ex.getMessage(),
                    "Error al llenar las dependencias", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * El propósito del método es retornar un Empleado seleccionado de la tabla
     * @return Retorna un Empleado, quien es el seleccionado en la tabla
     */
    public Empleado getEmpleadoFromTable() {

        Empleado empleado = tableModel.getEmpleadoAt(tablaEmpleados.getSelectedRow());

        return empleado;
    }
    /**
     * El método devuelve el JTextField del Form
     * @return El JTextField del Form
     */
    public JTextField getTxtNombreCU() {
        return txtNombreCU;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNombre = new javax.swing.JLabel();
        pnlInfEmpleado = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreCU = new javax.swing.JTextField();
        txtCorreoCU = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        cbxDependenciasCU = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbxSubdependenciaCU = new javax.swing.JComboBox<>();
        cbxSexo = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        btnRegistrarEmpleados = new javax.swing.JButton();
        btnModEmpleados = new javax.swing.JButton();
        btnElmEmpleados = new javax.swing.JButton();
        btnRegistrarAdmin = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cbxDependenciaListado = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cbxSubdependenciaListado = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtNombreBusqueda = new javax.swing.JTextField();
        btnBusListado = new javax.swing.JButton();
        srcListadoEmpleadosS = new javax.swing.JScrollPane();
        tablaEmpleados = new javax.swing.JTable(){

            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }

                return tip;

            }
        };
        btnCerrarSesion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(getIconImage());

        lblNombre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNombre.setText("REGISTRO EMPLEADOS");

        pnlInfEmpleado.setBackground(new java.awt.Color(255, 255, 255));
        pnlInfEmpleado.setBorder(javax.swing.BorderFactory.createTitledBorder("Informacion de Empleados"));

        jLabel1.setText("Nombre Completo");

        jLabel2.setText("Correo Electronico");

        jLabel3.setText("Código");

        jLabel4.setText("Dependencia");

        cbxDependenciasCU.setModel(new javax.swing.DefaultComboBoxModel<>(new itemCombo[] {null }));

        jLabel5.setText("Sexo");

        jLabel6.setText("Subdependencia");

        cbxSubdependenciaCU.setModel(new javax.swing.DefaultComboBoxModel<>(new itemCombo[] {null}));

        cbxSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar", "Femenino", "Masculino" }));

        javax.swing.GroupLayout pnlInfEmpleadoLayout = new javax.swing.GroupLayout(pnlInfEmpleado);
        pnlInfEmpleado.setLayout(pnlInfEmpleadoLayout);
        pnlInfEmpleadoLayout.setHorizontalGroup(
            pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfEmpleadoLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfEmpleadoLayout.createSequentialGroup()
                        .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxDependenciasCU, 0, 332, Short.MAX_VALUE)
                            .addComponent(cbxSubdependenciaCU, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInfEmpleadoLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInfEmpleadoLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(8, 8, 8)))
                        .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCodigo)
                            .addComponent(cbxSexo, 0, 122, Short.MAX_VALUE))
                        .addGap(26, 26, 26))
                    .addGroup(pnlInfEmpleadoLayout.createSequentialGroup()
                        .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(30, 30, 30)
                        .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombreCU, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                            .addComponent(txtCorreoCU))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlInfEmpleadoLayout.setVerticalGroup(
            pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfEmpleadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombreCU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCorreoCU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfEmpleadoLayout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxSubdependenciaCU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(37, 37, 37))
                    .addGroup(pnlInfEmpleadoLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(pnlInfEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxSexo)
                            .addComponent(cbxDependenciasCU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(79, 79, 79))))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnRegistrarEmpleados.setText("Registrar Empleado");

        btnModEmpleados.setText("Modificar");

        btnElmEmpleados.setText("Eliminar");

        btnRegistrarAdmin.setText("Registrar Administrador");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRegistrarEmpleados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnModEmpleados)
                        .addGap(24, 24, 24)
                        .addComponent(btnElmEmpleados))
                    .addComponent(btnRegistrarAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModEmpleados)
                    .addComponent(btnElmEmpleados))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(btnRegistrarEmpleados)
                .addGap(28, 28, 28)
                .addComponent(btnRegistrarAdmin)
                .addGap(34, 34, 34))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado de Empleados"));

        jLabel7.setText("Dependencia");

        cbxDependenciaListado.setModel(new javax.swing.DefaultComboBoxModel<>(new itemCombo[] {new itemCombo(0, "TODAS") }));

        jLabel8.setText("Subdependencia");

        cbxSubdependenciaListado.setModel(new javax.swing.DefaultComboBoxModel<>(new itemCombo[] {new itemCombo(0, "TODAS") }));

        jLabel9.setText("Nombre de Empleado");

        btnBusListado.setText("Buscar");

        tablaEmpleados.setModel(tableModel);
        tablaEmpleados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        srcListadoEmpleadosS.setViewportView(tablaEmpleados);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(srcListadoEmpleadosS)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxDependenciaListado, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxSubdependenciaListado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNombreBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBusListado)))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbxDependenciaListado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(cbxSubdependenciaListado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtNombreBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBusListado))
                .addGap(31, 31, 31)
                .addComponent(srcListadoEmpleadosS, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnCerrarSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen/LogoCerrarSesion.png"))); // NOI18N
        btnCerrarSesion.setToolTipText("Cerrar sesión\n");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlInfEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(295, 295, 295)
                                .addComponent(lblNombre)
                                .addGap(97, 97, 97)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addGap(18, 18, 18)
                        .addComponent(pnlInfEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBusListado;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnElmEmpleados;
    private javax.swing.JButton btnModEmpleados;
    private javax.swing.JButton btnRegistrarAdmin;
    private javax.swing.JButton btnRegistrarEmpleados;
    private javax.swing.JComboBox<itemCombo> cbxDependenciaListado;
    private javax.swing.JComboBox<itemCombo> cbxDependenciasCU;
    private javax.swing.JComboBox<String> cbxSexo;
    private javax.swing.JComboBox<itemCombo> cbxSubdependenciaCU;
    private javax.swing.JComboBox<itemCombo> cbxSubdependenciaListado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JPanel pnlInfEmpleado;
    private javax.swing.JScrollPane srcListadoEmpleadosS;
    private javax.swing.JTable tablaEmpleados;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCorreoCU;
    private javax.swing.JTextField txtNombreBusqueda;
    private javax.swing.JTextField txtNombreCU;
    // End of variables declaration//GEN-END:variables
}
