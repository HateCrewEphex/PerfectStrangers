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
    private int idComboActual = -1;
    private java.util.ArrayList<Integer> platillosSeleccionados = new java.util.ArrayList<>();
    private javax.swing.JTable jTableCombos;
    private javax.swing.table.DefaultTableModel modeloTabla;

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
        
        try (java.sql.Connection conexion = DBConnection.getConnection()) { 
        } catch (Exception e) {
            System.err.println("Error de conexion en Combos: " + e.getMessage());
        }
        
        jCTipoCombo.removeAllItems();
        jCTipoCombo.addItem("Combo");
        jCTipoCombo.addItem("Promo");
        
        jBGuardar.setText("REGISTRAR COMBO");
        jBGuardar.setBackground(new java.awt.Color(33, 122, 79));
        jBGuardar.addActionListener(e -> registrarCombo());
        jBGuardar.setForeground(java.awt.Color.WHITE);
        
        jBRegresar.addActionListener(e -> {
            new PSMenu().setVisible(true);
            this.dispose();
        });
        
        cargarIdsPlatillos();
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

        javax.swing.JButton[] accion = {jBAgregar, jBQuitar, jBGuardar, jBRegresar};
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
        
        jLNomCombo.setVisible(false);
        jTNomPlatillo.setVisible(false);
        jLTipoPaquete.setVisible(false);
        jCTipoCombo.setVisible(false);
        jLPrecio.setVisible(false);
        jTPrecio.setVisible(false);
        jBGuardar.setText("GUARDAR CONTENIDO COMBO");
        
        centro.add(tarjeta);
        
        // --- TABLA DE COMBOS ---
        modeloTabla = new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Categoría", "Nombre", "Precio", "Productos Incluidos"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableCombos = new javax.swing.JTable(modeloTabla);
        jTableCombos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        jTableCombos.setRowHeight(30);
        jTableCombos.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jTableCombos.getTableHeader().setBackground(new java.awt.Color(34, 34, 38));
        jTableCombos.getTableHeader().setForeground(tonoOro);
        jTableCombos.setBackground(new java.awt.Color(34, 34, 38));
        jTableCombos.setForeground(java.awt.Color.WHITE);
        jTableCombos.setSelectionBackground(new java.awt.Color(60, 60, 65));
        jTableCombos.setSelectionForeground(tonoOro);

        jTableCombos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarComboDeTabla();
            }
        });

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(jTableCombos);
        scrollPane.getViewport().setBackground(casiNegro);
        scrollPane.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0),
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 70, 75), 3)
        ));
        
        javax.swing.JPanel panelTabla = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.add(scrollPane, java.awt.BorderLayout.CENTER);

        javax.swing.JPanel mainSplit = new javax.swing.JPanel(new java.awt.BorderLayout());
        mainSplit.setOpaque(false);
        mainSplit.add(centro, java.awt.BorderLayout.WEST);
        mainSplit.add(panelTabla, java.awt.BorderLayout.CENTER);

        fondoMetalico.add(mainSplit, java.awt.BorderLayout.CENTER);
        setContentPane(fondoMetalico);
        revalidate();
        repaint();
        
        jBAltaCombo.setVisible(false);
        jBActCombo.setVisible(false);
        jBEliCombo.setVisible(false);
        jLComboSel.setVisible(false);
        jCComboSel.setVisible(false);
        
        limpiarCampos();
    }

    private void cargarIdsPlatillos() {
        jCIDPlatillo.removeAllItems();
        try (java.sql.Connection con = DBConnection.getConnection()) {
            java.sql.PreparedStatement ps = con.prepareStatement(
                "SELECT id_producto, nombre FROM productos WHERE disponible = TRUE AND categoria != 'Combo' ORDER BY nombre"
            );
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jCIDPlatillo.addItem(rs.getInt("id_producto") + " - " + rs.getString("nombre"));
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar productos en combo", ex);
        }
    }
    
    private void aplicarEstiloBotonModo(javax.swing.JButton boton) {}

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
        jBQuitar = new javax.swing.JButton();
        jLPlatillosCombo = new javax.swing.JLabel();
        jBRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLCombos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22)); // Reduced font size to avoid cut-off
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

        jBQuitar.setText("QUITAR PLATILLO");
        jBQuitar.addActionListener(this::jBQuitarActionPerformed);

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
                            .addComponent(jBQuitar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(jBQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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

            if (esProductoCombo(idPlatillo)) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "No se puede agregar un combo dentro de otro combo.",
                    "Selección no válida",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            
            platillosSeleccionados.add(idPlatillo);
            String platillosActuales = jLPlatillosCombo.getText();
            if (platillosActuales.equals("-") || platillosActuales.isEmpty()) {
                jLPlatillosCombo.setText(nombrePlatillo);
            } else {
                jLPlatillosCombo.setText(platillosActuales + ", " + nombrePlatillo);
            }
        }
    }                                         

    private void jBQuitarActionPerformed(java.awt.event.ActionEvent evt) {
        String platilloSel = (String) jCIDPlatillo.getSelectedItem();
        if (platilloSel != null) {
            String[] parts = platilloSel.split(" - ");
            int idPlatillo = Integer.parseInt(parts[0]);
            
            if (platillosSeleccionados.contains(idPlatillo)) {
                // Solo remover uno
                platillosSeleccionados.remove(Integer.valueOf(idPlatillo));
                
                // Reconstruir texto
                StringBuilder nombres = new StringBuilder();
                java.util.Map<Integer, Integer> conteo = new java.util.LinkedHashMap<>();
                for (Integer id : platillosSeleccionados) {
                    conteo.put(id, conteo.getOrDefault(id, 0) + 1);
                }
                
                try (java.sql.Connection con = DBConnection.getConnection();
                     java.sql.PreparedStatement ps = con.prepareStatement("SELECT nombre FROM productos WHERE id_producto = ?")) {
                     for (java.util.Map.Entry<Integer, Integer> entry : conteo.entrySet()) {
                         ps.setInt(1, entry.getKey());
                         try (java.sql.ResultSet rs = ps.executeQuery()) {
                             if (rs.next()) {
                                 if (nombres.length() > 0) nombres.append(", ");
                                 nombres.append(rs.getString("nombre"));
                                 if (entry.getValue() > 1) {
                                     nombres.append(" x").append(entry.getValue());
                                 }
                             }
                         }
                     }
                } catch (Exception ex) {
                    logger.log(java.util.logging.Level.SEVERE, "Error al quitar platillo", ex);
                }
                
                jLPlatillosCombo.setText(nombres.length() == 0 ? "-" : nombres.toString());
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Ese producto no está en el combo.");
            }
        }
    }

    private void cargarCombos() {
        modeloTabla.setRowCount(0);
        try (java.sql.Connection con = DBConnection.getConnection()) {
            String sql = "SELECT c.id_producto, c.categoria, c.nombre, c.precio, " +
                         "COALESCE(GROUP_CONCAT(CONCAT(p.nombre, IF(cd.cantidad > 1, CONCAT(' x', cd.cantidad), '')) ORDER BY p.nombre SEPARATOR ', '), '') AS productos_incluidos " +
                         "FROM productos c " +
                         "LEFT JOIN combo_detalles cd ON c.id_producto = cd.id_combo " +
                         "LEFT JOIN productos p ON cd.id_producto_incluido = p.id_producto " +
                         "WHERE c.categoria = 'Combo' AND c.disponible = TRUE " +
                         "GROUP BY c.id_producto, c.categoria, c.nombre, c.precio " +
                         "ORDER BY c.id_producto ASC";
            try (java.sql.PreparedStatement pst = con.prepareStatement(sql);
                 java.sql.ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int idCombo = rs.getInt("id_producto");
                    String productosNombres = rs.getString("productos_incluidos");
                    
                    modeloTabla.addRow(new Object[]{
                        idCombo,
                        rs.getString("categoria"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        productosNombres
                    });
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar combos", ex);
        }
    }

    private void seleccionarComboDeTabla() {
        int fila = jTableCombos.getSelectedRow();
        if (fila == -1) return;
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        idComboActual = id;
        
        jCTipoCombo.setSelectedItem((String) modeloTabla.getValueAt(fila, 1));
        jTNomPlatillo.setText((String) modeloTabla.getValueAt(fila, 2));
        jTPrecio.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));

        cargarDetalleCombo(idComboActual);
        
        jLCombos.setText("EDITANDO COMBO: " + modeloTabla.getValueAt(fila, 2));
        jBGuardar.setEnabled(true);
    }

    private void registrarCombo() {
        if (idComboActual == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Primero debe seleccionar un combo de la tabla.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (platillosSeleccionados.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Agregue al menos un producto al combo.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (java.sql.Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (java.sql.PreparedStatement psDelete = con.prepareStatement("DELETE FROM combo_detalles WHERE id_combo = ?")) {
                    psDelete.setInt(1, idComboActual);
                    psDelete.executeUpdate();
                }

                guardarPlatillosPaquete(con, idComboActual);
                con.commit();
                javax.swing.JOptionPane.showMessageDialog(this, "Contenido del combo actualizado correctamente.");
                limpiarCampos();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar contenido del combo: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void actualizarCombo() {
        if (idComboActual == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Primero debe seleccionar un combo para actualizar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreCombo = jTNomPlatillo.getText().trim();
        String tipoCombo = jCTipoCombo.getSelectedItem() != null ? jCTipoCombo.getSelectedItem().toString() : "Combo";
        String categoriaCombo = categoriaDesdeTipoCombo(tipoCombo);
        String precioStr = jTPrecio.getText().trim();
        
        if (nombreCombo.isEmpty() || precioStr.isEmpty() || platillosSeleccionados.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Complete todos los campos y agregue al menos un platillo", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double precio = 0;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.");
            return;
        }
        
        try (java.sql.Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                String sql = "UPDATE productos SET categoria = ?, nombre = ?, precio = ? WHERE id_producto = ? AND es_combo = TRUE";
                try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, categoriaCombo);
                    ps.setString(2, nombreCombo);
                    ps.setDouble(3, precio);
                    ps.setInt(4, idComboActual);
                    ps.executeUpdate();
                }

                try (java.sql.PreparedStatement psDelete = con.prepareStatement("DELETE FROM combo_detalles WHERE id_combo = ?")) {
                    psDelete.setInt(1, idComboActual);
                    psDelete.executeUpdate();
                }
                guardarPlatillosPaquete(con, idComboActual);

                con.commit();
                javax.swing.JOptionPane.showMessageDialog(this, "Combo actualizado correctamente.");
                limpiarCampos();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al actualizar combo: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void eliminarCombo() {
        if (idComboActual == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Primero debe seleccionar un combo para eliminar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este combo?", "Confirmar Eliminación", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
        
        try (java.sql.Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                String sql = "DELETE FROM productos WHERE id_producto = ? AND es_combo = TRUE";
                try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idComboActual);
                    ps.executeUpdate();
                }
                con.commit();
                javax.swing.JOptionPane.showMessageDialog(this, "Combo eliminado correctamente.");
                limpiarCampos();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al eliminar combo: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void guardarPlatillosPaquete(java.sql.Connection con, int idPaquete) throws java.sql.SQLException {
        java.util.Map<Integer, Integer> conteo = new java.util.LinkedHashMap<>();
        for (Integer idPlatillo : platillosSeleccionados) {
            conteo.put(idPlatillo, conteo.getOrDefault(idPlatillo, 0) + 1);
        }

        String sql = "INSERT INTO combo_detalles (id_combo, id_producto_incluido, cantidad) VALUES (?, ?, ?)";
        try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            for (java.util.Map.Entry<Integer, Integer> entry : conteo.entrySet()) {
                ps.setInt(1, idPaquete);
                ps.setInt(2, entry.getKey());
                ps.setInt(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void cargarDetalleCombo(int idCombo) {
        platillosSeleccionados.clear();
        StringBuilder nombres = new StringBuilder();

        String sql = "SELECT cd.id_producto_incluido, cd.cantidad, p.nombre " +
                     "FROM combo_detalles cd " +
                     "JOIN productos p ON cd.id_producto_incluido = p.id_producto " +
                     "WHERE cd.id_combo = ? ORDER BY p.nombre";
        try (java.sql.Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCombo);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idProductoIncluido = rs.getInt("id_producto_incluido");
                    int cantidad = rs.getInt("cantidad");
                    String nombre = rs.getString("nombre");

                    for (int i = 0; i < cantidad; i++) {
                        platillosSeleccionados.add(idProductoIncluido);
                    }

                    if (nombres.length() > 0) {
                        nombres.append(", ");
                    }
                    nombres.append(nombre);
                    if (cantidad > 1) {
                        nombres.append(" x").append(cantidad);
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar detalle del combo", ex);
        }

        jLPlatillosCombo.setText(nombres.length() == 0 ? "-" : nombres.toString());
    }

    private String categoriaDesdeTipoCombo(String tipoCombo) {
        if ("Promo".equalsIgnoreCase(tipoCombo)) {
            return "Complementos";
        }
        return "Combo";
    }

    private boolean esProductoCombo(int idProducto) {
        String sql = "SELECT es_combo FROM productos WHERE id_producto = ?";
        try (java.sql.Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("es_combo");
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.WARNING, "No fue posible validar tipo de producto", ex);
        }
        return false;
    }

    private void limpiarCampos() {
        jTNomPlatillo.setText("");
        jTPrecio.setText("");
        if (jCTipoCombo.getItemCount() > 0) jCTipoCombo.setSelectedIndex(0);
        jLPlatillosCombo.setText("-");
        platillosSeleccionados.clear();
        
        idComboActual = -1;
        jLCombos.setText("COMBOS");
        jBGuardar.setEnabled(true);
        if (jTableCombos != null) {
            jTableCombos.clearSelection();
        }
        cargarCombos();
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
            // cargarDatosCombo(idCombo); // Método no implementado, el JComboBox está oculto
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
    private javax.swing.JButton jBQuitar;
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
}
