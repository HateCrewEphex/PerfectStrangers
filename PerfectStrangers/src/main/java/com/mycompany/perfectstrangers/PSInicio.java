package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Ephex
 */
public class PSInicio extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSInicio.class.getName());

    /**
     * Creates new form PSInicio
     */
    public PSInicio() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);        
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono de la ventana.");
        }
        
        this.setTitle("PerfectStrangers - VENTA Y ORDENES");
                jPFContraseña.setEchoChar('*');
        jPFContraseña.setText("");
        jLAccesoIncorrecto.setText("");
        jBAcceder.addActionListener(evt -> autenticarUsuario());
        
        // Permite presionar 'Enter' en los campos de texto para iniciar sesión
        jPFContraseña.addActionListener(evt -> autenticarUsuario());
        jTFUsuario.addActionListener(evt -> autenticarUsuario());
        
        // Re-estilización extrema basada en el diseño visual "Acorazado/Industrial" aportado
        java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);
        java.awt.Color casiNegro = new java.awt.Color(25, 25, 25);
        java.awt.Font fuenteTitulos = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15);
        
        jPPrincipal.setVisible(false); // Ocultar el diseño por defecto de NetBeans
        
        // Panel interior (tarjeta central translúcida)
        javax.swing.JPanel tarjetaLogin = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                // Fondo oscuro con menor opacidad para la tarjeta
                g2.setColor(new java.awt.Color(20, 20, 20, 210)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Borde dorado
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.setColor(tonoOro);
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
                g2.dispose();
            }
        };
        tarjetaLogin.setOpaque(false);
        tarjetaLogin.setPreferredSize(new java.awt.Dimension(360, 420));
        tarjetaLogin.setLayout(new javax.swing.BoxLayout(tarjetaLogin, javax.swing.BoxLayout.Y_AXIS));
        
        // Ajuste de etiquetas
        jLUsuario.setText("USUARIO");
        jLUsuario.setFont(fuenteTitulos);
        jLUsuario.setForeground(tonoOro);
        jLUsuario.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        jLContraseña.setText("CONTRASEÑA");
        jLContraseña.setFont(fuenteTitulos);
        jLContraseña.setForeground(tonoOro);
        jLContraseña.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        javax.swing.border.Border bordeInputs = javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(tonoOro, 1, true),
            javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
        
        // Ajuste de campos de texto
        jTFUsuario.setBackground(casiNegro);
        jTFUsuario.setForeground(tonoOro);
        jTFUsuario.setCaretColor(tonoOro);
        jTFUsuario.setBorder(bordeInputs);
        jTFUsuario.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jTFUsuario.setMaximumSize(new java.awt.Dimension(260, 40));
        jTFUsuario.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        jTFUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        
        jPFContraseña.setBackground(casiNegro);
        jPFContraseña.setForeground(tonoOro);
        jPFContraseña.setCaretColor(tonoOro);
        jPFContraseña.setBorder(bordeInputs);
        jPFContraseña.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jPFContraseña.setMaximumSize(new java.awt.Dimension(260, 40));
        jPFContraseña.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        jPFContraseña.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        
        // Botón Acceder imitando la placa "gastada / metálica"
        jBAcceder.setText("ACCEDER");
        try {
            jBAcceder.setFont(new java.awt.Font("Impact", java.awt.Font.PLAIN, 26)); // Letra similar a la placa
        } catch(Exception ex) {
            jBAcceder.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        }
        jBAcceder.setBackground(new java.awt.Color(64, 66, 64)); 
        jBAcceder.setForeground(new java.awt.Color(230, 230, 220));
        jBAcceder.setFocusPainted(false);
        jBAcceder.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(160, 160, 160), 2, true),
            javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        jBAcceder.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        jBAcceder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        jLAccesoIncorrecto.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        // Organizar los elementos en la tarjeta
        tarjetaLogin.add(javax.swing.Box.createVerticalGlue());
        tarjetaLogin.add(jLUsuario);
        tarjetaLogin.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 5)));
        tarjetaLogin.add(jTFUsuario);
        tarjetaLogin.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 25)));
        tarjetaLogin.add(jLContraseña);
        tarjetaLogin.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 5)));
        tarjetaLogin.add(jPFContraseña);
        tarjetaLogin.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 35)));
        tarjetaLogin.add(jBAcceder);
        tarjetaLogin.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 15)));
        tarjetaLogin.add(jLAccesoIncorrecto);
        tarjetaLogin.add(javax.swing.Box.createVerticalGlue());
        
        // Panel Fondo Maestro simulando la plancha de acero exterior
        javax.swing.JPanel fondoCentrado = new javax.swing.JPanel(new java.awt.GridBagLayout()) {
            private java.awt.Image bgImage;
            {
                try {
                    java.net.URL url = getClass().getResource("/com/mycompany/perfectstrangers/logo500x500.png");
                    if (url != null) { bgImage = new javax.swing.ImageIcon(url).getImage(); }
                } catch (Exception e) {}
            }
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo oscuro base profundo
                g2.setColor(new java.awt.Color(20, 15, 10));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Efecto "Fuego interior" A DETALLES CLAROS COMO RESPALDO
                java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f, Math.max(getWidth(), getHeight()) / 1.1f,
                    new float[]{ 0.1f, 1.0f },
                    new java.awt.Color[]{
                        new java.awt.Color(160, 40, 10, 80), // Brillo central rojizo
                        new java.awt.Color(10, 10, 10, 230)    // Oscuridad extrema bordes
                    }
                );
                g2.setPaint(rgp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Si existe el logo, dibujarlo sin que se estire (Centrado, con bordes fundidos visualmente)
                if (bgImage != null) {
                    int imgW = bgImage.getWidth(this);
                    int imgH = bgImage.getHeight(this);
                    if (imgW > 0 && imgH > 0) {
                        int panelW = getWidth();
                        int panelH = getHeight();
                        
                        // Escalar manteniendo proporción
                        int drawH = panelH - 120; // Más margen arriba y abajo
                        int drawW = (int) (drawH * ((double) imgW / imgH));
                        
                        if (drawW > panelW - 120) {
                            drawW = panelW - 120;
                            drawH = (int) (drawW * ((double) imgH / imgW));
                        }
                        
                        int x = (panelW - drawW) / 2;
                        int y = (panelH - drawH) / 2;
                        
                        g2.drawImage(bgImage, x, y, drawW, drawH, this);
                    }
                }
                
                // Cortina oscura transparente final sobre la imagen para bajar un poco la luz y asentar la tarjeta
                g2.setColor(new java.awt.Color(0,0,0, 60));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Borde 1: Chasis gris acero grueso
                g2.setStroke(new java.awt.BasicStroke(40f));
                g2.setColor(new java.awt.Color(45, 45, 47));
                g2.drawRect(20, 20, getWidth()-40, getHeight()-40);
                
                // Borde 2: Ribete negro fino interno/externo
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.setColor(new java.awt.Color(10, 10, 10));
                g2.drawRect(41, 41, getWidth()-82, getHeight()-82);
                g2.drawRect(0, 0, getWidth(), getHeight());
                
                // Borde 3: Ribete dorado interior "marco"
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.setColor(tonoOro);
                g2.drawRect(43, 43, getWidth()-86, getHeight()-86);
                
                // Remaches (Círculos dorados en las 4 esquinas del chasis)
                int radio = 8;
                g2.fillOval(20, 20, radio*2, radio*2);
                g2.fillOval(getWidth()-20-radio*2, 20, radio*2, radio*2);
                g2.fillOval(20, getHeight()-20-radio*2, radio*2, radio*2);
                g2.fillOval(getWidth()-20-radio*2, getHeight()-20-radio*2, radio*2, radio*2);
                
                g2.dispose();
            }
        };
        fondoCentrado.setBackground(new java.awt.Color(0, 0, 0));
        fondoCentrado.add(tarjetaLogin);
        this.setContentPane(fondoCentrado);
        
        try {
            java.net.URL logoURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (logoURL != null) {
                javax.swing.ImageIcon logoOriginal = new javax.swing.ImageIcon(logoURL);
                java.awt.Image imagenOriginal = logoOriginal.getImage();
                java.awt.Image redimensionada = imagenOriginal.getScaledInstance(250, 250, java.awt.Image.SCALE_SMOOTH);
                jLLogo.setIcon(new javax.swing.ImageIcon(redimensionada));
            }
        } catch (Exception e) {}    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPPrincipal = new javax.swing.JPanel();
        jLContraseña = new javax.swing.JLabel();
        jLUsuario = new javax.swing.JLabel();
        jTFUsuario = new javax.swing.JTextField();
        jPFContraseña = new javax.swing.JPasswordField();
        jLLogo = new javax.swing.JLabel();
        jBAcceder = new javax.swing.JButton();
        jLAccesoIncorrecto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLContraseña.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLContraseña.setForeground(new java.awt.Color(255, 255, 255));
        jLContraseña.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLContraseña.setText("Contraseña");

        jLUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jLUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLUsuario.setText("Usuario");

        jTFUsuario.setText("Usuario");
        jTFUsuario.addActionListener(this::jTFUsuarioActionPerformed);

        jPFContraseña.setText("");

        jLLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/perfectstrangers/logo150.png"))); // NOI18N
        jLLogo.setMaximumSize(new java.awt.Dimension(600, 600));
        jLLogo.setMinimumSize(new java.awt.Dimension(600, 600));
        jLLogo.setPreferredSize(new java.awt.Dimension(600, 600));

        jBAcceder.setText("Acceder");

        jLAccesoIncorrecto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLAccesoIncorrecto.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jBAcceder, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPPrincipalLayout.createSequentialGroup()
                                .addComponent(jLUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPPrincipalLayout.createSequentialGroup()
                                .addComponent(jLContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPFContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLAccesoIncorrecto, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLUsuario)
                            .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLContraseña)
                            .addComponent(jPFContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jBAcceder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLAccesoIncorrecto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFUsuarioActionPerformed

    private void autenticarUsuario() {
        String usuario = jTFUsuario.getText().trim();
        String password = new String(jPFContraseña.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            jLAccesoIncorrecto.setText("Usuario y contraseña son obligatorios.");
            return;
        }

        String sql = "SELECT u.id_empleado, e.nombre, e.ap_paterno, e.puesto " +
                     "FROM usuarios u " +
                     "JOIN empleados e ON u.id_empleado = e.id_empleado " +
                     "WHERE u.usuario = ? AND u.contrasena = SHA2(?, 256)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Sesion.idEmpleado = resultSet.getInt("id_empleado");
                    String nombre = resultSet.getString("nombre");
                    String ap = resultSet.getString("ap_paterno");
                    Sesion.nombreEmpleado = (nombre != null ? nombre.trim() : "") + 
                                            (ap != null ? " " + ap.trim() : "");
                    Sesion.puestoEmpleado = resultSet.getString("puesto") != null ? resultSet.getString("puesto").trim() : "Empleado";

                    jLAccesoIncorrecto.setText("");
                    java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
                    dispose();
                } else {
                    jLAccesoIncorrecto.setText("Usuario o contraseña incorrectos.");
                }
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al validar credenciales", ex);
            jLAccesoIncorrecto.setText("No fue posible conectar con la base de datos.");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
        java.awt.EventQueue.invokeLater(() -> new PSInicio().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAcceder;
    private javax.swing.JLabel jLAccesoIncorrecto;
    private javax.swing.JLabel jLContraseña;
    private javax.swing.JLabel jLLogo;
    private javax.swing.JLabel jLUsuario;
    private javax.swing.JPasswordField jPFContraseña;
    private javax.swing.JPanel jPPrincipal;
    private javax.swing.JTextField jTFUsuario;
    // End of variables declaration//GEN-END:variables
}
