/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

/**
 *
 * @author Ephex
 */
public class PSPlatillos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSPlatillos.class.getName());

    private String modoActual = "NUEVO"; // NUEVO, ACTUALIZAR, ELIMINAR

    /**
     * Creates new form PSPlatillos
     */
    public PSPlatillos() {
        initComponents();
        
        // Estilos
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        java.awt.Font fontLabels = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
        java.awt.Font fontInputs = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24)); // Título principal más grande
        
        jLabel2.setFont(fontLabels);
        jLabel3.setFont(fontLabels);
        jLabel4.setFont(fontLabels);
        jLabel5.setFont(fontLabels);
        
        jTNomPlatillo.setFont(fontInputs);
        jTPrecio.setFont(fontInputs);
        jComboBox1.setFont(fontInputs);
        jComboBox2.setFont(fontInputs);
        
        jBGuardar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jButton1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jButton1.setBackground(null);
        jButton1.setForeground(java.awt.Color.WHITE);
        
        jBAltaPlatillo.setFont(fontLabels);
        jBActPlatillo.setFont(fontLabels);
        jBEliPlatillo.setFont(fontLabels);
        
        // Configurar ComboBox tipo alimento
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alimento", "Bebida" }));
        
        // Eventos de botones principales
        jBAltaPlatillo.addActionListener(e -> setModo("NUEVO"));
        jBActPlatillo.addActionListener(e -> setModo("ACTUALIZAR"));
        jBEliPlatillo.addActionListener(e -> setModo("ELIMINAR"));
        
        jComboBox1.addActionListener(e -> cargarDatosPlatillo());
        
        jBGuardar.addActionListener(e -> ejecutarAccion());
        jBGuardar.setForeground(java.awt.Color.WHITE);
        
        // Boton regresar
        jButton1.addActionListener(e -> {
            PSMenu menu = new PSMenu();
            menu.setVisible(true);
            dispose();
        });
        
        // Centrar UI
        javax.swing.JPanel fondoCentrado = new javax.swing.JPanel(new java.awt.GridBagLayout());
        fondoCentrado.setBackground(new java.awt.Color(0, 0, 0));
        fondoCentrado.add(jPPrincipal);
        this.setContentPane(fondoCentrado);
        
        setModo("NUEVO");
    }

    private void setModo(String modo) {
        this.modoActual = modo;
        jTNomPlatillo.setText("");
        jTPrecio.setText("");
        
        if (modo.equals("NUEVO")) {
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nuevo" }));
            jComboBox1.setEnabled(false);
            jTNomPlatillo.setEnabled(true);
            jComboBox2.setEnabled(true);
            jTPrecio.setEnabled(true);
            jBGuardar.setText("GUARDAR");
            jBGuardar.setBackground(new java.awt.Color(40, 167, 69)); // Verde
        } else {
            jComboBox1.setEnabled(true);
            cargarIdsPlatillos();
            
            if (modo.equals("ACTUALIZAR")) {
                jTNomPlatillo.setEnabled(true);
                jComboBox2.setEnabled(true);
                jTPrecio.setEnabled(true);
                jBGuardar.setText("ACTUALIZAR");
                jBGuardar.setBackground(new java.awt.Color(0, 123, 255)); // Azul
            } else if (modo.equals("ELIMINAR")) {
                jTNomPlatillo.setEnabled(false);
                jComboBox2.setEnabled(false);
                jTPrecio.setEnabled(false);
                jBGuardar.setText("ELIMINAR");
                jBGuardar.setBackground(new java.awt.Color(220, 53, 69)); // Rojo
            }
        }
    }
    
    private void cargarIdsPlatillos() {
        jComboBox1.removeAllItems();
        try (java.sql.Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement pst = con.prepareStatement("SELECT id_platillo FROM platillos ORDER BY id_platillo ASC");
             java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                jComboBox1.addItem(String.valueOf(rs.getInt("id_platillo")));
            }
        } catch (java.sql.SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar IDs", ex);
        }
    }
    
    private void cargarDatosPlatillo() {
        if (jComboBox1.getSelectedItem() == null || jComboBox1.getSelectedItem().equals("Nuevo")) return;
        
        String idPlatillo = jComboBox1.getSelectedItem().toString();
        try (java.sql.Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement pst = con.prepareStatement("SELECT nombre_alimento, tipo_alimento, costo FROM platillos WHERE id_platillo = ?")) {
            pst.setInt(1, Integer.parseInt(idPlatillo));
            try (java.sql.ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    jTNomPlatillo.setText(rs.getString("nombre_alimento"));
                    jComboBox2.setSelectedItem(rs.getString("tipo_alimento"));
                    jTPrecio.setText(rs.getString("costo"));
                }
            }
        } catch (java.sql.SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar datos del platillo", ex);
        }
    }

    private void ejecutarAccion() {
        String nombre = jTNomPlatillo.getText().trim();
        String tipo = jComboBox2.getSelectedItem() != null ? jComboBox2.getSelectedItem().toString() : "Alimento";
        String precioStr = jTPrecio.getText().trim();
        
        if (modoActual.equals("NUEVO") || modoActual.equals("ACTUALIZAR")) {
            if (nombre.isEmpty() || precioStr.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Por favor llena todos los campos.");
                return;
            }
        }
        
        double precio = 0;
        if (!modoActual.equals("ELIMINAR")) {
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.");
                return;
            }
        }

        try (java.sql.Connection con = DBConnection.getConnection()) {
            if (modoActual.equals("NUEVO")) {
                int nuevoId = 1;
                try (java.sql.PreparedStatement pstMax = con.prepareStatement("SELECT MAX(id_platillo) FROM platillos");
                     java.sql.ResultSet rsMax = pstMax.executeQuery()) {
                    if (rsMax.next() && rsMax.getInt(1) > 0) {
                        nuevoId = rsMax.getInt(1) + 1;
                    }
                }
                
                String sql = "INSERT INTO platillos (id_platillo, nombre_alimento, tipo_alimento, costo, descripcion) VALUES (?, ?, ?, ?, '')";
                try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setInt(1, nuevoId);
                    pst.setString(2, nombre);
                    pst.setString(3, tipo);
                    pst.setDouble(4, precio);
                    pst.executeUpdate();
                    javax.swing.JOptionPane.showMessageDialog(this, "Platillo agregado con éxito (ID: " + nuevoId + ").");
                }
            } else if (modoActual.equals("ACTUALIZAR")) {
                if (jComboBox1.getSelectedItem() == null) return;
                int id = Integer.parseInt(jComboBox1.getSelectedItem().toString());
                
                String sql = "UPDATE platillos SET nombre_alimento = ?, tipo_alimento = ?, costo = ? WHERE id_platillo = ?";
                try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setString(1, nombre);
                    pst.setString(2, tipo);
                    pst.setDouble(3, precio);
                    pst.setInt(4, id);
                    pst.executeUpdate();
                    javax.swing.JOptionPane.showMessageDialog(this, "Platillo actualizado con éxito.");
                }
            } else if (modoActual.equals("ELIMINAR")) {
                if (jComboBox1.getSelectedItem() == null) return;
                int id = Integer.parseInt(jComboBox1.getSelectedItem().toString());
                String nombrePlatillo = jTNomPlatillo.getText().trim();
                
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "¿Estás seguro que deseas eliminar el platillo: " + nombrePlatillo + "?", "Validar Eliminación", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM platillos WHERE id_platillo = ?";
                    try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setInt(1, id);
                        pst.executeUpdate();
                        javax.swing.JOptionPane.showMessageDialog(this, "Platillo eliminado con éxito.");
                    }
                } else {
                    return;
                }
            }
            
            // Refrescar UI después de la acción
            setModo(modoActual);
            
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error en base de datos: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, "Error DB", ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPPrincipal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jBAltaPlatillo = new javax.swing.JButton();
        jBActPlatillo = new javax.swing.JButton();
        jBEliPlatillo = new javax.swing.JButton();
        jPPlatillos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jTNomPlatillo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTPrecio = new javax.swing.JTextField();
        jBGuardar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PLATILLOS");

        jBAltaPlatillo.setText("NUEVO");

        jBActPlatillo.setText("ACTUALIZAR");

        jBEliPlatillo.setText("ELIMINAR");

        jPPlatillos.setBackground(new java.awt.Color(51, 51, 51));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID PLATILLO:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NOMBRE:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TIPO ALIMENTO:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("PRECIO:");

        jBGuardar.setText("GUARDAR");

        javax.swing.GroupLayout jPPlatillosLayout = new javax.swing.GroupLayout(jPPlatillos);
        jPPlatillos.setLayout(jPPlatillosLayout);
        jPPlatillosLayout.setHorizontalGroup(
            jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPlatillosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPlatillosLayout.createSequentialGroup()
                        .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPPlatillosLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPPlatillosLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTNomPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(73, 73, 73)
                        .addComponent(jTPrecio)))
                .addContainerGap())
            .addGroup(jPPlatillosLayout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(jBGuardar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPPlatillosLayout.setVerticalGroup(
            jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPlatillosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTNomPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jBGuardar)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jButton1.setText("REGRESAR");

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPPlatillos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addComponent(jBAltaPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jBActPlatillo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBEliPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBAltaPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBEliPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBActPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPPlatillos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
        java.awt.EventQueue.invokeLater(() -> new PSPlatillos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBActPlatillo;
    private javax.swing.JButton jBAltaPlatillo;
    private javax.swing.JButton jBEliPlatillo;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPPlatillos;
    private javax.swing.JPanel jPPrincipal;
    private javax.swing.JTextField jTNomPlatillo;
    private javax.swing.JTextField jTPrecio;
    // End of variables declaration//GEN-END:variables
}
