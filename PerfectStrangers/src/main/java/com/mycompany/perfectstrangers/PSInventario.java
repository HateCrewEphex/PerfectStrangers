/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

/**
 *
 * @author Ephex
 */
public class PSInventario extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSInventario.class.getName());

    /**
     * Creates new form PSInventario
     */
    public PSInventario() {
        initComponents();
        
        jBRegresar.setBackground(null);
        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {}
        this.setTitle("PerfectStrangers - Inventario");

        configurarInterfaz();
        mostrarInventarioPendiente();
        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
    }

    private void mostrarInventarioPendiente() {
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
            new Object[]{"Estado", "Detalle"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addRow(new Object[]{"Pendiente", "La tabla de inventario aún no está definida. Aquí no se muestran productos."});

        jTInventario.setModel(modelo);
        javax.swing.table.DefaultTableCellRenderer centerRender = new javax.swing.table.DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        for (int i = 0; i < jTInventario.getColumnCount(); i++) {
            jTInventario.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }
    }

    private void configurarInterfaz() {
        java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);
        java.awt.Color metal = new java.awt.Color(45, 45, 47);

        jPanel1.removeAll();
        jPanel1.setLayout(new java.awt.BorderLayout());

        javax.swing.JPanel panelFondoAcero = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new java.awt.Color(18, 18, 20));
                g2.fillRect(0, 0, getWidth(), getHeight());

                java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f, Math.max(getWidth(), getHeight()) / 1.1f,
                    new float[]{0.4f, 1.0f},
                    new java.awt.Color[]{
                        new java.awt.Color(0, 0, 0, 0),
                        new java.awt.Color(5, 5, 5, 240)
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
        panelFondoAcero.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));

        javax.swing.JPanel panelContenido = new javax.swing.JPanel(new java.awt.BorderLayout(20, 20));
        panelContenido.setOpaque(false);

        // Top Panel
        javax.swing.JPanel topPanel = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        topPanel.setOpaque(false);
        
        jLNVentana.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        jLNVentana.setForeground(tonoOro);
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topPanel.add(jLNVentana, java.awt.BorderLayout.NORTH);

        javax.swing.JPanel filterPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);
        jLFiltrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        jLFiltrar.setForeground(tonoOro);
        jCFiltro.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        jCFiltro.setPreferredSize(new java.awt.Dimension(250, 40));
        jCFiltro.setBackground(new java.awt.Color(35, 35, 40));
        jCFiltro.setForeground(java.awt.Color.WHITE);
        jCFiltro.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(90, 90, 95), 1),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        filterPanel.add(jLFiltrar);
        filterPanel.add(jCFiltro);
        
        topPanel.add(filterPanel, java.awt.BorderLayout.SOUTH);

        // Center Panel (Table)
        jTInventario.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        jTInventario.setRowHeight(30);
        jTInventario.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        jTInventario.setBackground(new java.awt.Color(30, 30, 34));
        jTInventario.setForeground(java.awt.Color.WHITE);
        jTInventario.setGridColor(new java.awt.Color(70, 70, 75));
        jTInventario.setSelectionBackground(new java.awt.Color(70, 58, 35));
        jTInventario.setSelectionForeground(java.awt.Color.WHITE);
        jTInventario.getTableHeader().setBackground(metal);
        jTInventario.getTableHeader().setForeground(tonoOro);

        jScrollPane1.getViewport().setBackground(new java.awt.Color(30, 30, 34));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 80), 2),
            javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Bottom Panel
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17));
        jBRegresar.setBackground(new java.awt.Color(44, 44, 48));
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setFocusPainted(false);
        jBRegresar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 24, 10, 24)
        ));
        bottomPanel.add(jBRegresar);

        panelContenido.add(topPanel, java.awt.BorderLayout.NORTH);
        panelContenido.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        panelContenido.add(bottomPanel, java.awt.BorderLayout.SOUTH);

        panelFondoAcero.add(panelContenido, java.awt.BorderLayout.CENTER);
        jPanel1.add(panelFondoAcero, java.awt.BorderLayout.CENTER);

        jPanel1.revalidate();
        jPanel1.repaint();
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
        jBRegresar = new javax.swing.JButton();
        jLNVentana = new javax.swing.JLabel();
        jLFiltrar = new javax.swing.JLabel();
        jCFiltro = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTInventario = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jBRegresar.setBackground(new java.awt.Color(204, 204, 204));
        jBRegresar.setText("REGRESAR");

        jLNVentana.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLNVentana.setForeground(new java.awt.Color(255, 255, 255));
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLNVentana.setText("INVENTARIO");

        jLFiltrar.setForeground(new java.awt.Color(255, 255, 255));
        jLFiltrar.setText("FILTRAR POR:");

        jTInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTInventario);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLNVentana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLFiltrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLNVentana)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLFiltrar)
                    .addComponent(jCFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        /* Set the FlatLaf look and feel */
        try {
            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
            javax.swing.UIManager.put( "Component.arc", 20 );
            javax.swing.UIManager.put( "Button.arc", 20 );
            javax.swing.UIManager.put( "TextComponent.arc", 20 );
        } catch( Exception ex ) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to initialize FlatLaf", ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PSInventario().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBRegresar;
    private javax.swing.JComboBox<String> jCFiltro;
    private javax.swing.JLabel jLFiltrar;
    private javax.swing.JLabel jLNVentana;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTInventario;
    // End of variables declaration//GEN-END:variables
}
