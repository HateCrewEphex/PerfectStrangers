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

        configurarInterfaz();
    }

    private void configurarInterfaz() {
        java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);
        java.awt.Color casiNegro = new java.awt.Color(20, 20, 22);
        java.awt.Color metal = new java.awt.Color(45, 45, 47);

        javax.swing.JPanel fondoMetalico = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new java.awt.Color(16, 16, 18));
                g2.fillRect(0, 0, getWidth(), getHeight());

                java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f, Math.max(getWidth(), getHeight()) / 1.1f,
                    new float[]{0.35f, 1.0f},
                    new java.awt.Color[]{
                        new java.awt.Color(0, 0, 0, 0),
                        new java.awt.Color(5, 5, 5, 235)
                    }
                );
                g2.setPaint(rgp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setStroke(new java.awt.BasicStroke(22f));
                g2.setColor(new java.awt.Color(60, 60, 65));
                g2.drawRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 20, 20);

                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.setColor(tonoOro);
                g2.drawRoundRect(22, 22, getWidth() - 44, getHeight() - 44, 16, 16);
                g2.dispose();
            }
        };
        fondoMetalico.setBorder(javax.swing.BorderFactory.createEmptyBorder(45, 45, 45, 45));

        javax.swing.JPanel centro = new javax.swing.JPanel(new java.awt.GridBagLayout());
        centro.setOpaque(false);

        javax.swing.JPanel tarjeta = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(casiNegro);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new java.awt.Color(70, 70, 75));
                g2.setStroke(new java.awt.BasicStroke(3f));
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 16, 16);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22));

        jPanel1.setOpaque(false);
        jLabel1.setForeground(tonoOro);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));

        javax.swing.JLabel[] labels = {jLNombre, jLAPaterno, jLAMaterno, jLAMaterno1, jLUsuario, jLContrasena, jLContrasena1};
        for (javax.swing.JLabel label : labels) {
            label.setForeground(tonoOro);
            label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        }

        javax.swing.JComponent[] campos = {jTNombre, jTAPaterno, jTAMaterno, jCBPuesto, jTUsuario, jPContrasena, jPCContrasena};
        for (javax.swing.JComponent campo : campos) {
            campo.setBackground(new java.awt.Color(34, 34, 38));
            campo.setForeground(java.awt.Color.WHITE);
            campo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            campo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 95, 100), 1),
                javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }

        jTNombre.setCaretColor(java.awt.Color.WHITE);
        jTAPaterno.setCaretColor(java.awt.Color.WHITE);
        jTAMaterno.setCaretColor(java.awt.Color.WHITE);
        jTUsuario.setCaretColor(java.awt.Color.WHITE);
        jPContrasena.setCaretColor(java.awt.Color.WHITE);
        jPCContrasena.setCaretColor(java.awt.Color.WHITE);

        jBRegistrar.setBackground(new java.awt.Color(33, 122, 79));
        jBRegistrar.setForeground(java.awt.Color.WHITE);
        jBRegistrar.setFocusPainted(false);
        jBRegistrar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        jBRegresar.setBackground(new java.awt.Color(44, 44, 48));
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setFocusPainted(false);
        jBRegresar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 85), 2),
            javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        jPanel1.setBackground(metal);

        tarjeta.add(jPanel1, java.awt.BorderLayout.CENTER);
        centro.add(tarjeta);
        fondoMetalico.add(centro, java.awt.BorderLayout.CENTER);
        setContentPane(fondoMetalico);
        revalidate();
        repaint();
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
                String sqlEmpleado = "INSERT INTO empleados (nombre, ap_paterno, ap_materno, puesto) VALUES (?, ?, ?, ?)";
                int nuevoIdEmpleado;
                try (java.sql.PreparedStatement pst1 = con.prepareStatement(sqlEmpleado, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    pst1.setString(1, nombre);
                    pst1.setString(2, aPaterno);
                    pst1.setString(3, aMaterno);
                    pst1.setString(4, puesto);
                    pst1.executeUpdate();

                    try (java.sql.ResultSet rsKeys = pst1.getGeneratedKeys()) {
                        if (!rsKeys.next()) {
                            throw new java.sql.SQLException("No fue posible obtener el ID del empleado recién creado.");
                        }
                        nuevoIdEmpleado = rsKeys.getInt(1);
                    }

                    String sqlUsuario = "INSERT INTO usuarios (usuario, contrasena, id_empleado) VALUES (?, SHA2(?, 256), ?)";
                    try (java.sql.PreparedStatement pst2 = con.prepareStatement(sqlUsuario)) {
                        pst2.setString(1, usuario);
                        pst2.setString(2, contrasena);
                        pst2.setInt(3, nuevoIdEmpleado);
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
