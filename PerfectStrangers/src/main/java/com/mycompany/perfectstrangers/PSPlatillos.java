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
        
        // Configurar ComboBox de categoría real en el nuevo esquema
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Platillos", "Bebidas" }));
        
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
        
        configurarInterfaz();
        
        setModo("NUEVO");
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

        jPPrincipal.setOpaque(false);
        jPPlatillos.setBackground(metal);
        jPPlatillos.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 85), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        jLabel1.setForeground(tonoOro);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));

        javax.swing.JLabel[] labels = {jLabel2, jLabel3, jLabel4, jLabel5};
        for (javax.swing.JLabel label : labels) {
            label.setForeground(tonoOro);
            label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        }

        javax.swing.JComponent[] campos = {jTNomPlatillo, jTPrecio, jComboBox1, jComboBox2};
        for (javax.swing.JComponent campo : campos) {
            campo.setBackground(new java.awt.Color(34, 34, 38));
            campo.setForeground(java.awt.Color.WHITE);
            campo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            campo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 95, 100), 1),
                javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }
        jTNomPlatillo.setCaretColor(java.awt.Color.WHITE);
        jTPrecio.setCaretColor(java.awt.Color.WHITE);

        jButton1.setBackground(new java.awt.Color(44, 44, 48));
        jButton1.setForeground(tonoOro);
        jButton1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        jButton1.setFocusPainted(false);

        aplicarEstiloBotonModo(jBAltaPlatillo);
        aplicarEstiloBotonModo(jBActPlatillo);
        aplicarEstiloBotonModo(jBEliPlatillo);

        jBGuardar.setFocusPainted(false);
        jBGuardar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 24, 10, 24)
        ));

        tarjeta.add(jPPrincipal, java.awt.BorderLayout.CENTER);
        centro.add(tarjeta);
        fondoMetalico.add(centro, java.awt.BorderLayout.CENTER);
        setContentPane(fondoMetalico);
        revalidate();
        repaint();
    }

    private void aplicarEstiloBotonModo(javax.swing.JButton boton) {
        boton.setForeground(java.awt.Color.WHITE);
        boton.setBackground(new java.awt.Color(44, 44, 48));
        boton.setFocusPainted(false);
        boton.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
    }

    private void resaltarModo(javax.swing.JButton seleccionado) {
        java.awt.Color activo = new java.awt.Color(204, 169, 90);
        java.awt.Color normal = new java.awt.Color(44, 44, 48);
        javax.swing.JButton[] botones = {jBAltaPlatillo, jBActPlatillo, jBEliPlatillo};
        for (javax.swing.JButton boton : botones) {
            boton.setBackground(normal);
            boton.setForeground(java.awt.Color.WHITE);
        }
        seleccionado.setBackground(activo);
        seleccionado.setForeground(new java.awt.Color(20, 20, 20));
    }

    private void setModo(String modo) {
        this.modoActual = modo;
        jTNomPlatillo.setText("");
        jTPrecio.setText("");
        
        if (modo.equals("NUEVO")) {
            resaltarModo(jBAltaPlatillo);
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nuevo" }));
            jComboBox1.setEnabled(false);
            jTNomPlatillo.setEnabled(true);
            jComboBox2.setEnabled(true);
            jTPrecio.setEnabled(true);
            jBGuardar.setText("GUARDAR");
            jBGuardar.setBackground(new java.awt.Color(33, 122, 79));
        } else {
            jComboBox1.setEnabled(true);
            cargarIdsPlatillos();
            
            if (modo.equals("ACTUALIZAR")) {
                resaltarModo(jBActPlatillo);
                jTNomPlatillo.setEnabled(true);
                jComboBox2.setEnabled(true);
                jTPrecio.setEnabled(true);
                jBGuardar.setText("ACTUALIZAR");
                jBGuardar.setBackground(new java.awt.Color(45, 108, 156));
            } else if (modo.equals("ELIMINAR")) {
                resaltarModo(jBEliPlatillo);
                jTNomPlatillo.setEnabled(false);
                jComboBox2.setEnabled(false);
                jTPrecio.setEnabled(false);
                jBGuardar.setText("ELIMINAR");
                jBGuardar.setBackground(new java.awt.Color(138, 52, 52));
            }
        }
        jBGuardar.setForeground(java.awt.Color.WHITE);
    }
    
    private void cargarIdsPlatillos() {
        jComboBox1.removeAllItems();
        try (java.sql.Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement pst = con.prepareStatement("SELECT id_producto FROM productos WHERE es_combo = FALSE ORDER BY id_producto ASC");
             java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                jComboBox1.addItem(String.valueOf(rs.getInt("id_producto")));
            }
        } catch (java.sql.SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar IDs", ex);
        }
    }
    
    private void cargarDatosPlatillo() {
        if (jComboBox1.getSelectedItem() == null || jComboBox1.getSelectedItem().equals("Nuevo")) return;
        
        String idPlatillo = jComboBox1.getSelectedItem().toString();
        try (java.sql.Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement pst = con.prepareStatement("SELECT nombre, categoria, precio FROM productos WHERE id_producto = ? AND es_combo = FALSE")) {
            pst.setInt(1, Integer.parseInt(idPlatillo));
            try (java.sql.ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    jTNomPlatillo.setText(rs.getString("nombre"));
                    jComboBox2.setSelectedItem(rs.getString("categoria"));
                    jTPrecio.setText(rs.getString("precio"));
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
                String sql = "INSERT INTO productos (categoria, nombre, precio, es_combo) VALUES (?, ?, ?, FALSE)";
                try (java.sql.PreparedStatement pst = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    pst.setString(1, tipo);
                    pst.setString(2, nombre);
                    pst.setDouble(3, precio);
                    pst.executeUpdate();
                    try (java.sql.ResultSet rsKeys = pst.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            javax.swing.JOptionPane.showMessageDialog(this, "Producto agregado con éxito (ID: " + rsKeys.getInt(1) + ").");
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(this, "Producto agregado con éxito.");
                        }
                    }
                }
            } else if (modoActual.equals("ACTUALIZAR")) {
                if (jComboBox1.getSelectedItem() == null) return;
                int id = Integer.parseInt(jComboBox1.getSelectedItem().toString());
                
                String sql = "UPDATE productos SET nombre = ?, categoria = ?, precio = ? WHERE id_producto = ? AND es_combo = FALSE";
                try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setString(1, nombre);
                    pst.setString(2, tipo);
                    pst.setDouble(3, precio);
                    pst.setInt(4, id);
                    pst.executeUpdate();
                    javax.swing.JOptionPane.showMessageDialog(this, "Producto actualizado con éxito.");
                }
            } else if (modoActual.equals("ELIMINAR")) {
                if (jComboBox1.getSelectedItem() == null) return;
                int id = Integer.parseInt(jComboBox1.getSelectedItem().toString());
                String nombrePlatillo = jTNomPlatillo.getText().trim();
                
                int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "¿Estás seguro que deseas eliminar el platillo: " + nombrePlatillo + "?", "Validar Eliminación", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM productos WHERE id_producto = ? AND es_combo = FALSE";
                    try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setInt(1, id);
                        pst.executeUpdate();
                        javax.swing.JOptionPane.showMessageDialog(this, "Producto eliminado con éxito.");
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
