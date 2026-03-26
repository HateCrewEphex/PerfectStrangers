/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

/**
 *
 * @author Ephex
 */
public class PSEmpleados extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSEmpleados.class.getName());

    /**
     * Creates new form PSEmpleados
     */
    public PSEmpleados() {
        initComponents();
        
        // --- ESTILOS GENERALES DE LA VENTANA ---
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); // Iniciar en pantalla completa
        setLocationRelativeTo(null); // Centrar en pantalla al abrir
        
        java.awt.Font fontLabels = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
        java.awt.Font fontInputs = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24)); // Título principal más grande
        
        jLNombre.setFont(fontLabels);
        jLAPaterno.setFont(fontLabels);
        jLAMaterno.setFont(fontLabels);
        jLAMaterno1.setFont(fontLabels); // Etiqueta puesto
        jLUsuario.setFont(fontLabels);
        jLContrasena.setFont(fontLabels);
        jLContrasena1.setFont(fontLabels);
        
        jTNombre.setFont(fontInputs);
        jTAPaterno.setFont(fontInputs);
        jTAMaterno.setFont(fontInputs);
        jCBPuesto.setFont(fontInputs);
        jTUsuario.setFont(fontInputs);
        jPContrasena.setFont(fontInputs);
        jPCContrasena.setFont(fontInputs);
        
        // Estilización de botones para empatar con el resto de PerfectStrangers
        jBRegistrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jBRegistrar.setBackground(new java.awt.Color(40, 167, 69)); // Verde llamativo para la acción principal
        jBRegistrar.setForeground(java.awt.Color.WHITE);
        
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jBRegresar.setBackground(null); // Mantener el gris estándar sin forzar el sistema
        jBRegresar.setForeground(java.awt.Color.WHITE);
        // ---------------------------------------
        
        jBRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarEmpleado();
            }
        });
        
        jBRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PSMenu menu = new PSMenu();
                menu.setVisible(true);
                dispose();
            }
        });
        
        // Centrar el contenido dinámicamente
        javax.swing.JPanel fondoCentrado = new javax.swing.JPanel(new java.awt.GridBagLayout());
        fondoCentrado.setBackground(new java.awt.Color(0, 0, 0));
        fondoCentrado.add(jPanel1);
        this.setContentPane(fondoCentrado);
    }

    private void registrarEmpleado() {
        String nombre = jTNombre.getText().trim();
        String aPaterno = jTAPaterno.getText().trim();
        String aMaterno = jTAMaterno.getText().trim();
        String puesto = jCBPuesto.getSelectedItem().toString();
        String usuario = jTUsuario.getText().trim();
        String contrasena = new String(jPContrasena.getPassword());
        String cContrasena = new String(jPCContrasena.getPassword());

        if (nombre.isEmpty() || aPaterno.isEmpty() || puesto.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor llena todos los campos obligatorios.");
            return;
        }

        if (!contrasena.equals(cContrasena)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }

        try (java.sql.Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false); // Para iniciar transacción
            
            try {
                int nuevoIdEmpleado = 1;
                String sqlMaxId = "SELECT MAX(id_empleado) FROM empleados";
                try (java.sql.PreparedStatement pstMax = con.prepareStatement(sqlMaxId);
                     java.sql.ResultSet rsMax = pstMax.executeQuery()) {
                    if (rsMax.next()) {
                        int maxId = rsMax.getInt(1);
                        if (maxId > 0) {
                            nuevoIdEmpleado = maxId + 1;
                        }
                    }
                }
                
                String sqlEmpleado = "INSERT INTO empleados (id_empleado, nombre, ap_paterno, ap_materno, puesto) VALUES (?, ?, ?, ?, ?)";
                try (java.sql.PreparedStatement pst1 = con.prepareStatement(sqlEmpleado)) {
                    pst1.setInt(1, nuevoIdEmpleado);
                    pst1.setString(2, nombre);
                    pst1.setString(3, aPaterno);
                    pst1.setString(4, aMaterno);
                    pst1.setString(5, puesto);
                    pst1.executeUpdate();

                    int nuevoIdUsuario = 1;
                    String sqlMaxUsuario = "SELECT MAX(id_usuario) FROM usuarios";
                    try (java.sql.PreparedStatement pstMaxUsr = con.prepareStatement(sqlMaxUsuario);
                         java.sql.ResultSet rsMaxUsr = pstMaxUsr.executeQuery()) {
                        if (rsMaxUsr.next()) {
                            int maxIdUsr = rsMaxUsr.getInt(1);
                            if (maxIdUsr > 0) {
                                nuevoIdUsuario = maxIdUsr + 1;
                            }
                        }
                    }

                    String sqlUsuario = "INSERT INTO usuarios (id_usuario, id_empleado, usuario, contrasena) VALUES (?, ?, ?, SHA2(?, 256))";
                    try (java.sql.PreparedStatement pst2 = con.prepareStatement(sqlUsuario)) {
                        pst2.setInt(1, nuevoIdUsuario);
                        pst2.setInt(2, nuevoIdEmpleado);
                        pst2.setString(3, usuario);
                        pst2.setString(4, contrasena);
                        pst2.executeUpdate();
                    }
                }
                
                con.commit();
                javax.swing.JOptionPane.showMessageDialog(this, "Empleado y usuario registrados con éxito.");
                limpiarCampos();
            } catch (Exception ex) {
                con.rollback();
                javax.swing.JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
                logger.log(java.util.logging.Level.SEVERE, "Error al registrar empleado", ex);
            }
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, "Error de conexión", ex);
        }
    }

    private void limpiarCampos() {
        jTNombre.setText("");
        jTAPaterno.setText("");
        jTAMaterno.setText("");
        jCBPuesto.setSelectedIndex(0);
        jTUsuario.setText("");
        jPContrasena.setText("");
        jPCContrasena.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLNombre = new javax.swing.JLabel();
        jTNombre = new javax.swing.JTextField();
        jLAPaterno = new javax.swing.JLabel();
        jTAPaterno = new javax.swing.JTextField();
        jLAMaterno = new javax.swing.JLabel();
        jTAMaterno = new javax.swing.JTextField();
        jLUsuario = new javax.swing.JLabel();
        jTUsuario = new javax.swing.JTextField();
        jLContrasena = new javax.swing.JLabel();
        jPContrasena = new javax.swing.JPasswordField();
        jLContrasena1 = new javax.swing.JLabel();
        jPCContrasena = new javax.swing.JPasswordField();
        jLAMaterno1 = new javax.swing.JLabel();
        jCBPuesto = new javax.swing.JComboBox<>();
        jBRegresar = new javax.swing.JButton();
        jBRegistrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ALTA DE EMPLEADOS");

        jLNombre.setForeground(new java.awt.Color(255, 255, 255));
        jLNombre.setText("Nombre(s)");

        jLAPaterno.setForeground(new java.awt.Color(255, 255, 255));
        jLAPaterno.setText("Apellido Paterno:");

        jLAMaterno.setForeground(new java.awt.Color(255, 255, 255));
        jLAMaterno.setText("Apellido Materno:");

        jLUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jLUsuario.setText("Usuario");

        jLContrasena.setForeground(new java.awt.Color(255, 255, 255));
        jLContrasena.setText("Contraseña");

        jLContrasena1.setForeground(new java.awt.Color(255, 255, 255));
        jLContrasena1.setText("Confirmar contraseña:");

        jLAMaterno1.setForeground(new java.awt.Color(255, 255, 255));
        jLAMaterno1.setText("Puesto:");

        jCBPuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrativo", "Mesero", "Cocinero" }));

        jBRegresar.setText("REGRESAR");

        jBRegistrar.setText("REGISTRAR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLAMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTAMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLAPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTAPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPContrasena))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLContrasena1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPCContrasena)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(jBRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLAMaterno1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 32, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLNombre)
                    .addComponent(jTNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLAPaterno)
                    .addComponent(jTAPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLAMaterno)
                    .addComponent(jTAMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLAMaterno1)
                    .addComponent(jCBPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLUsuario)
                    .addComponent(jTUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLContrasena)
                    .addComponent(jPContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLContrasena1)
                    .addComponent(jPCContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jBRegresar)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set FlatMacDarkLaf para mantener consistencia general del programa */
        try {
            javax.swing.UIManager.put("Button.arc", 20);
            javax.swing.UIManager.put("Component.arc", 20);
            javax.swing.UIManager.put("ProgressBar.arc", 20);
            javax.swing.UIManager.put("TextComponent.arc", 20);
            javax.swing.UIManager.put("ScrollBar.arc", 20);
            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PSEmpleados().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBRegistrar;
    private javax.swing.JButton jBRegresar;
    private javax.swing.JComboBox<String> jCBPuesto;
    private javax.swing.JLabel jLAMaterno;
    private javax.swing.JLabel jLAMaterno1;
    private javax.swing.JLabel jLAPaterno;
    private javax.swing.JLabel jLContrasena;
    private javax.swing.JLabel jLContrasena1;
    private javax.swing.JLabel jLNombre;
    private javax.swing.JLabel jLUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField jPCContrasena;
    private javax.swing.JPasswordField jPContrasena;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTAMaterno;
    private javax.swing.JTextField jTAPaterno;
    private javax.swing.JTextField jTNombre;
    private javax.swing.JTextField jTUsuario;
    // End of variables declaration//GEN-END:variables
}
