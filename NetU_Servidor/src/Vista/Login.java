/**
 * El propósito de este Form es servir de vista para el login en NetU Servidor
 */
package Vista;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends javax.swing.JFrame {

    /**
     * Constructor vacío: inicia los componentes, cambia el título, la
     * redimensión a falso y su posición en el centro de la pantalla
     */
    public Login() {

        initComponents();
        setTitle("Inicio Servidor");
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * Este método es llamado desde el constructor para inicializar los
     * elementos No se puede cambiar, es intrínseco al Form.
     *
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnIniciarSesion = new javax.swing.JButton();
        labelCodigo = new javax.swing.JLabel();
        labelPassword = new javax.swing.JLabel();
        txtCodigoEmpleado = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtPasswordEmpleado = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnIniciarSesion.setText("Iniciar Sesión");

        labelCodigo.setText("Código");

        labelPassword.setText("Contraseña");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen/LogoServer.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPassword)
                            .addComponent(labelCodigo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 24, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCodigoEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                    .addComponent(txtPasswordEmpleado)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCodigoEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCodigo))
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPassword)
                    .addComponent(txtPasswordEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     *
     * @return El código que el Empleado (Administrador) escribió en el
     * txtCodigoEmpleado
     */
    public int getCodigo() {

        int codigo;

        try {

            codigo = Integer.parseInt(txtCodigoEmpleado.getText().trim());

        } catch (NumberFormatException ne) {

            codigo = -1;

        }

        return codigo;
    }
  

    /**
     * El método primero extrae el arreglo de carácteres que el Empleado
     * (Administrador) digitó y luego los recorre, colocando cada uno en la
     * variable password para luego retornar esta
     *
     * @return La contraseña que digitó el Empleado (Administrador)
     */
    public String getPassword() {

        String password = "";
        char[] charsPassword = txtPasswordEmpleado.getPassword();

        for (int i = 0; i < charsPassword.length; i++) {

            char caracter = charsPassword[i];
            password += caracter;

        }

        return password;
    }

    /**
     * Añade la escucha del evento de tipo Action al botón btnIniciarSesion
     *
     * @param al
     */
    public void addActionListener(ActionListener al) {
        btnIniciarSesion.addActionListener(al);

    }

    /**
     * Añade la escucha del evento de tipo KeyListener a los dos txtFields Nos
     * sirve para evitar que el usuario ingrese espacios o carácteres
     * incorrectos
     *
     * @param kl
     */
    public void addKeyListener(KeyListener kl) {
        txtPasswordEmpleado.addKeyListener(kl);
        txtCodigoEmpleado.addKeyListener(kl);
    }

    /**
     *
     * @return El botón btnIniciarSesion
     */
    public JButton getBtnLogin() {
        return btnIniciarSesion;
    }
    /**
     * @return El textField del codigo del Empleado para restringir el ingreso
     * de información
     */
    public JTextField getTxtCodigoEmpleado() {
        return txtCodigoEmpleado;
    }
    /**
     * @return El textField de la constraseña del Empleado para restringir 
     * el ingreso de información
     */
    public JPasswordField getTxtPasswordEmpleado() {
        return txtPasswordEmpleado;
    }
    /**
     * El método permite limpiar los campos de texto en el Form
     */
    public void limpiarCampos(){
        txtCodigoEmpleado.setText("");
        txtPasswordEmpleado.setText("");
    }    
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelCodigo;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JTextField txtCodigoEmpleado;
    private javax.swing.JPasswordField txtPasswordEmpleado;
    // End of variables declaration//GEN-END:variables
}
