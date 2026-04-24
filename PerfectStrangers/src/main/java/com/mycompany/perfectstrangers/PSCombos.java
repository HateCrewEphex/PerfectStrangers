/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

/**
 *
 * @author Ephex
 */
public class PSCombos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSCombos.class.getName());
    private java.sql.Connection conexion;
    private String modoActual = "NUEVO";  // Puede ser: NUEVO, ACTUALIZAR, ELIMINAR
    private java.util.ArrayList<Integer> platillosSeleccionados = new java.util.ArrayList<>();

    /**
     * Creates new form PSPlatillos
     */
    public PSCombos() {
        initComponents();
        
        // Ventana en pantalla completa
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        this.setTitle("PerfectStrangers - COMBOS");
        
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono de la ventana.");
        }
        
        configurarInterfaz();
        
        try { 
            conexion = DBConnection.getConnection(); 
        } catch (Exception e) {
            System.err.println("Error de conexion en Combos: " + e.getMessage());
        }
        
        jCTipoCombo.removeAllItems();
        jCTipoCombo.addItem("Combo");
        jCTipoCombo.addItem("Promo");
        
        jBAltaCombo.addActionListener(e -> setModo("NUEVO"));
        jBActCombo.addActionListener(e -> setModo("ACTUALIZAR"));
        jBEliCombo.addActionListener(e -> setModo("ELIMINAR"));
        
        jBGuardar.addActionListener(e -> ejecutarAccionPrincipal());
        // Quitar la llamada duplicada de eliminar al boton de toggle, 
        // para que solo cambie el modo y se muestre un botón para confirmar la eliminación si hace falta
        
        // Agregar listener para el botón de Regresar
        jBRegresar.addActionListener(e -> {
            new PSMenu().setVisible(true);
            this.dispose();
        });
        
        setModo("NUEVO");
        cargarIdsPlatillos();
        cargarIdsCombos();
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

        jLCombos.setForeground(tonoOro);
        jLCombos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));

        javax.swing.JLabel[] labels = {jLIDPlatillo, jLComboSel, jLNomCombo, jLTipoPaquete, jLPrecio};
        for (javax.swing.JLabel label : labels) {
            label.setForeground(tonoOro);
            label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        }

        jLPlatillosCombo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        jLPlatillosCombo.setForeground(java.awt.Color.WHITE);
        jLPlatillosCombo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLPlatillosCombo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 95, 100), 1),
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        javax.swing.JComponent[] campos = {jTNomPlatillo, jTPrecio, jCIDPlatillo, jCComboSel, jCTipoCombo};
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

        aplicarEstiloBotonModo(jBAltaCombo);
        aplicarEstiloBotonModo(jBActCombo);
        aplicarEstiloBotonModo(jBEliCombo);

        javax.swing.JButton[] accion = {jBAgregar, jBGuardar, jBRegresar};
        for (javax.swing.JButton boton : accion) {
            boton.setBackground(new java.awt.Color(44, 44, 48));
            boton.setForeground(tonoOro);
            boton.setFocusPainted(false);
            boton.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
                javax.swing.BorderFactory.createEmptyBorder(8, 14, 8, 14)
            ));
        }
        jBGuardar.setForeground(java.awt.Color.WHITE);

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
        javax.swing.JButton[] botones = {jBAltaCombo, jBActCombo, jBEliCombo};
        for (javax.swing.JButton boton : botones) {
            boton.setBackground(normal);
            boton.setForeground(java.awt.Color.WHITE);
        }
        seleccionado.setBackground(activo);
        seleccionado.setForeground(new java.awt.Color(20, 20, 20));
    }

    private void cargarIdsPlatillos() {
        if (conexion == null) return;
        jCIDPlatillo.removeAllItems();
        try {
            java.sql.PreparedStatement ps = conexion.prepareStatement("SELECT id_platillo, nombre_alimento FROM platillos");
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jCIDPlatillo.addItem(rs.getInt("id_platillo") + " - " + rs.getString("nombre_alimento"));
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void cargarIdsCombos() {
        if (conexion == null) return;
        jCComboSel.removeAllItems();
        jCComboSel.addItem("Nuevo");
        try {
            java.sql.PreparedStatement ps = conexion.prepareStatement("SELECT id_paquete FROM paquetes WHERE id_paquete != 0");
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jCComboSel.addItem(String.valueOf(rs.getInt("id_paquete")));
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
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
        jLCombos = new javax.swing.JLabel();
        jBAltaCombo = new javax.swing.JButton();
        jBActCombo = new javax.swing.JButton();
        jBEliCombo = new javax.swing.JButton();
        jPPlatillos = new javax.swing.JPanel();
        jLIDPlatillo = new javax.swing.JLabel();
        jCIDPlatillo = new javax.swing.JComboBox<>();
        jLComboSel = new javax.swing.JLabel();
        jCComboSel = new javax.swing.JComboBox<>();
        jLNomCombo = new javax.swing.JLabel();
        jTNomPlatillo = new javax.swing.JTextField();
        jLTipoPaquete = new javax.swing.JLabel();
        jCTipoCombo = new javax.swing.JComboBox<>();
        jLPrecio = new javax.swing.JLabel();
        jTPrecio = new javax.swing.JTextField();
        jBGuardar = new javax.swing.JButton();
        jBAgregar = new javax.swing.JButton();
        jLPlatillosCombo = new javax.swing.JLabel();
        jBRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLCombos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLCombos.setForeground(new java.awt.Color(255, 255, 255));
        jLCombos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLCombos.setText("COMBOS");

        jBAltaCombo.setText("NUEVO");

        jBActCombo.setText("ACTUALIZAR");

        jBEliCombo.setText("ELIMINAR");

        jPPlatillos.setBackground(new java.awt.Color(51, 51, 51));

        jLIDPlatillo.setForeground(new java.awt.Color(255, 255, 255));
        jLIDPlatillo.setText("ID PLATILLO:");

        jCIDPlatillo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLComboSel.setForeground(new java.awt.Color(255, 255, 255));
        jLComboSel.setText("ID COMBO:");

        jCComboSel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nuevo" }));
        jCComboSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCComboSelActionPerformed(evt);
            }
        });

        jLNomCombo.setForeground(new java.awt.Color(255, 255, 255));
        jLNomCombo.setText("NOMBRE COMBO:");

        jLTipoPaquete.setForeground(new java.awt.Color(255, 255, 255));
        jLTipoPaquete.setText("TIPO COMBO:");

        jCTipoCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLPrecio.setForeground(new java.awt.Color(255, 255, 255));
        jLPrecio.setText("PRECIO:");

        jBGuardar.setText("GUARDAR COMBO");

        jBAgregar.setText("AGREGAR PLATILLO");
        jBAgregar.addActionListener(this::jBAgregarActionPerformed);

        jLPlatillosCombo.setForeground(new java.awt.Color(255, 255, 255));
        jLPlatillosCombo.setText("-");

        javax.swing.GroupLayout jPPlatillosLayout = new javax.swing.GroupLayout(jPPlatillos);
        jPPlatillos.setLayout(jPPlatillosLayout);
        jPPlatillosLayout.setHorizontalGroup(
            jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPlatillosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jLComboSel, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCComboSel, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jLNomCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTNomPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jLTipoPaquete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCTipoCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jLPrecio)
                        .addGap(73, 73, 73)
                        .addComponent(jTPrecio))
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPPlatillosLayout.createSequentialGroup()
                                .addComponent(jLIDPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCIDPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLPlatillosCombo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPPlatillosLayout.setVerticalGroup(
            jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPlatillosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLComboSel)
                    .addComponent(jCComboSel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLNomCombo)
                    .addComponent(jTNomPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLTipoPaquete)
                    .addComponent(jCTipoCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLPrecio)
                    .addComponent(jTPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLIDPlatillo)
                    .addComponent(jCIDPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jBAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLPlatillosCombo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jBRegresar.setText("REGRESAR");

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPPlatillos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLCombos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addComponent(jBAltaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jBActCombo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBEliCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLCombos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBAltaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBEliCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBActCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPPlatillos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jBAgregarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        String platilloSel = (String) jCIDPlatillo.getSelectedItem();
        if (platilloSel != null) {
            String[] parts = platilloSel.split(" - ");
            int idPlatillo = Integer.parseInt(parts[0]);
            String nombrePlatillo = parts[1];
            
            platillosSeleccionados.add(idPlatillo);
            String platillosActuales = jLPlatillosCombo.getText();
            if (platillosActuales.equals("-") || platillosActuales.isEmpty()) {
                jLPlatillosCombo.setText(nombrePlatillo);
            } else {
                jLPlatillosCombo.setText(platillosActuales + ", " + nombrePlatillo);
            }
        }
    }                                         

        private void ejecutarAccionPrincipal() {
        if (modoActual.equals("ELIMINAR")) {
            eliminarPaquete();
        } else {
            guardarPaquete();
        }
    }

    private int generarNuevoIdCombo() {
        int nuevoId = 1;
        try {
            java.sql.PreparedStatement ps = conexion.prepareStatement("SELECT MAX(id_paquete) FROM paquetes");
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (maxId > 0) {
                    nuevoId = maxId + 1;
                }
            }
        } catch (java.sql.SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        return nuevoId;
    }

    private void guardarPaquete() {
        String nombreCombo = jTNomPlatillo.getText();
        String tipoCombo = (String) jCTipoCombo.getSelectedItem();
        String precioStr = jTPrecio.getText();
        
        if (nombreCombo.isEmpty() || precioStr.isEmpty() || platillosSeleccionados.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos y agregue al menos un platillo", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double precio = Double.parseDouble(precioStr);
        String idComboStr = (String) jCComboSel.getSelectedItem();
        
        try {
            if (modoActual.equals("NUEVO")) {
                int idNuevoPaquete = generarNuevoIdCombo();
                
                java.sql.PreparedStatement ps = conexion.prepareStatement("INSERT INTO paquetes (id_paquete, nombre_paquete, tipo_paquete, precio_paquete) VALUES (?, ?, ?, ?)");
                ps.setInt(1, idNuevoPaquete);
                ps.setString(2, nombreCombo);
                ps.setString(3, tipoCombo);
                ps.setDouble(4, precio);
                ps.executeUpdate();
                
                if (idNuevoPaquete != -1) {
                    guardarPlatillosPaquete(idNuevoPaquete);
                    javax.swing.JOptionPane.showMessageDialog(this, "Combo creado correctamente con ID: " + idNuevoPaquete);
                }
            } else if (modoActual.equals("ACTUALIZAR") && idComboStr != null && !idComboStr.equals("Nuevo")) {
                int idCombo = Integer.parseInt(idComboStr);
                
                if (idCombo == 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El combo 0 no se puede actualizar.", "Acción denegada", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                java.sql.PreparedStatement ps = conexion.prepareStatement("UPDATE paquetes SET nombre_paquete = ?, tipo_paquete = ?, precio_paquete = ? WHERE id_paquete = ?");
                ps.setString(1, nombreCombo);
                ps.setString(2, tipoCombo);
                ps.setDouble(3, precio);
                ps.setInt(4, idCombo);
                ps.executeUpdate();
                
                // Borrar los anteriores y guardar los nuevos
                java.sql.PreparedStatement psDel = conexion.prepareStatement("DELETE FROM detalle_paquetes WHERE id_paquete = ?");
                psDel.setInt(1, idCombo);
                psDel.executeUpdate();
                
                guardarPlatillosPaquete(idCombo);
                javax.swing.JOptionPane.showMessageDialog(this, "Combo actualizado correctamente");
            }
            limpiarFormulario();
            cargarIdsCombos();
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void guardarPlatillosPaquete(int idPaquete) throws java.sql.SQLException {
        java.sql.PreparedStatement ps = conexion.prepareStatement("INSERT INTO detalle_paquetes (id_paquete, id_platillo, cant) VALUES (?, ?, ?)");
        
        // Agrupar platillos duplicados para sumar la cantidad
        java.util.Map<Integer, Integer> conteoPlatillos = new java.util.HashMap<>();
        for (int idPlatillo : platillosSeleccionados) {
            conteoPlatillos.put(idPlatillo, conteoPlatillos.getOrDefault(idPlatillo, 0) + 1);
        }
        
        for (java.util.Map.Entry<Integer, Integer> entry : conteoPlatillos.entrySet()) {
            ps.setInt(1, idPaquete);
            ps.setInt(2, entry.getKey());
            ps.setInt(3, entry.getValue());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void eliminarPaquete() {
        if (!modoActual.equals("ELIMINAR")) return;
        
        String idComboStr = (String) jCComboSel.getSelectedItem();
        if (idComboStr == null || idComboStr.equals("Nuevo")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un combo para eliminar", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int idCombo = Integer.parseInt(idComboStr);
            
            if (idCombo == 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "El combo 0 no se puede eliminar.", "Acción denegada", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Borrar de detalle_paquetes primero por la fk
            java.sql.PreparedStatement psDelDet = conexion.prepareStatement("DELETE FROM detalle_paquetes WHERE id_paquete = ?");
            psDelDet.setInt(1, idCombo);
            psDelDet.executeUpdate();
            
            // Lógica de eliminación en tabla maestra
            java.sql.PreparedStatement ps = conexion.prepareStatement("DELETE FROM paquetes WHERE id_paquete = ?");
            ps.setInt(1, idCombo);
            ps.executeUpdate();
            
            javax.swing.JOptionPane.showMessageDialog(this, "Combo eliminado correctamente");
            limpiarFormulario();
            cargarIdsCombos();
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error eliminando el combo: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void limpiarFormulario() {
        jCComboSel.setSelectedIndex(0);
        jTNomPlatillo.setText("");
        jTPrecio.setText("");
        jCTipoCombo.setSelectedIndex(0);
        jLPlatillosCombo.setText("-");
        platillosSeleccionados.clear();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the look and feel */
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
        java.awt.EventQueue.invokeLater(() -> new PSCombos().setVisible(true));
    }

    private void jCComboSelActionPerformed(java.awt.event.ActionEvent evt) {
        String idCombo = (String) jCComboSel.getSelectedItem();
        if (idCombo != null && !idCombo.equals("Nuevo")) {
            cargarDatosCombo(idCombo);
        } else if ("Nuevo".equals(idCombo)) {
            jTNomPlatillo.setText("");
            jTPrecio.setText("");
            jCTipoCombo.setSelectedIndex(0);
            jLPlatillosCombo.setText("-");
            platillosSeleccionados.clear();
        }
    }    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBActCombo;
    private javax.swing.JButton jBAgregar;
    private javax.swing.JButton jBAltaCombo;
    private javax.swing.JButton jBEliCombo;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jBRegresar;
    private javax.swing.JComboBox<String> jCIDPlatillo;
    private javax.swing.JComboBox<String> jCTipoCombo;
    private javax.swing.JLabel jLCombos;
    private javax.swing.JLabel jLIDPlatillo;
    private javax.swing.JComboBox<String> jCComboSel;
    private javax.swing.JLabel jLComboSel;
    private javax.swing.JLabel jLNomCombo;
    private javax.swing.JLabel jLPlatillosCombo;
    private javax.swing.JLabel jLPrecio;
    private javax.swing.JLabel jLTipoPaquete;
    private javax.swing.JPanel jPPlatillos;
    private javax.swing.JPanel jPPrincipal;
    private javax.swing.JTextField jTNomPlatillo;
    private javax.swing.JTextField jTPrecio;
    // End of variables declaration//GEN-END:variables

    private void setModo(String modo) {
        this.modoActual = modo;
        if (modo.equals("NUEVO")) {
            resaltarModo(jBAltaCombo);
            jCComboSel.setSelectedIndex(0);
            jCComboSel.setEnabled(false);
            jTNomPlatillo.setText("");
            jTPrecio.setText("");
            jCTipoCombo.setSelectedIndex(0);
            jLPlatillosCombo.setText("-");
            platillosSeleccionados.clear();
            
            jTNomPlatillo.setEnabled(true);
            jTPrecio.setEnabled(true);
            jCTipoCombo.setEnabled(true);
            jBGuardar.setEnabled(true);
            jBGuardar.setText("GUARDAR COMBO");
            jBGuardar.setBackground(new java.awt.Color(33, 122, 79));
            jBAgregar.setEnabled(true);
            jBEliCombo.setEnabled(true);
        } else if (modo.equals("ACTUALIZAR")) {
            resaltarModo(jBActCombo);
            jCComboSel.setEnabled(true);
            jTNomPlatillo.setEnabled(true);
            jTPrecio.setEnabled(true);
            jCTipoCombo.setEnabled(true);
            jBGuardar.setEnabled(true);
            jBGuardar.setText("ACTUALIZAR COMBO");
            jBGuardar.setBackground(new java.awt.Color(45, 108, 156));
            jBAgregar.setEnabled(true);
            jBEliCombo.setEnabled(true);
        } else if (modo.equals("ELIMINAR")) {
            resaltarModo(jBEliCombo);
            jCComboSel.setEnabled(true);
            jTNomPlatillo.setEnabled(false);
            jTPrecio.setEnabled(false);
            jCTipoCombo.setEnabled(false);
            jBGuardar.setEnabled(true);
            jBGuardar.setText("ELIMINAR COMBO");
            jBGuardar.setBackground(new java.awt.Color(138, 52, 52));
            jBAgregar.setEnabled(false);
            jBEliCombo.setEnabled(true);
        }
        jBGuardar.setForeground(java.awt.Color.WHITE);
    }

    private void cargarDatosCombo(String idCombo) {
        try {
            java.sql.PreparedStatement ps = conexion.prepareStatement("SELECT * FROM paquetes WHERE id_paquete = ?");
            ps.setInt(1, Integer.parseInt(idCombo));
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jTNomPlatillo.setText(rs.getString("nombre_paquete"));
                jTPrecio.setText(rs.getString("precio_paquete"));
                jCTipoCombo.setSelectedItem(rs.getString("tipo_paquete"));
            }
            
            // Cargar los platillos del paquete
            platillosSeleccionados.clear();
            java.sql.PreparedStatement psPlat = conexion.prepareStatement(
                "SELECT dp.id_platillo, dp.cant, p.nombre_alimento " +
                "FROM detalle_paquetes dp " +
                "JOIN platillos p ON dp.id_platillo = p.id_platillo " +
                "WHERE dp.id_paquete = ?");
            psPlat.setInt(1, Integer.parseInt(idCombo));
            java.sql.ResultSet rsPlat = psPlat.executeQuery();
            
            StringBuilder nombresPlatillos = new StringBuilder();
            while (rsPlat.next()) {
                int idPlatillo = rsPlat.getInt("id_platillo");
                int cant = rsPlat.getInt("cant");
                String nombrePlatillo = rsPlat.getString("nombre_alimento");
                
                for(int i = 0; i < cant; i++) {
                    platillosSeleccionados.add(idPlatillo);
                }
                
                if (nombresPlatillos.length() > 0) {
                    nombresPlatillos.append(", ");
                }
                nombresPlatillos.append(cant).append("x ").append(nombrePlatillo);
            }
            
            if (nombresPlatillos.length() > 0) {
                jLPlatillosCombo.setText(nombresPlatillos.toString());
            } else {
                jLPlatillosCombo.setText("-");
            }
            
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error cargando los datos del combo: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
