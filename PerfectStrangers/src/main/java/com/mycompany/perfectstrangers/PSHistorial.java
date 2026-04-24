/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ephex
 */
public class PSHistorial extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSHistorial.class.getName());

    /**
     * Creates new form PSHistorial
     */
    public PSHistorial() {
        initComponents();
        
        jBImprimirTicket.setBackground(null);
        jBRegresar.setBackground(null);
        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {}
        this.setTitle("PerfectStrangers - Historial");

        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
        
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);
        java.awt.Color metal = new java.awt.Color(45, 45, 47);

        jPPrincipal.removeAll();
        jPPrincipal.setLayout(new java.awt.BorderLayout());

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

        // Top Panel (Title & Filters)
        javax.swing.JPanel topPanel = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        topPanel.setOpaque(false);
        
        jLNVentana.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        jLNVentana.setForeground(tonoOro);
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topPanel.add(jLNVentana, java.awt.BorderLayout.NORTH);

        javax.swing.JPanel filterPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
        filterPanel.setOpaque(false);
        jLOrdenar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        jLOrdenar.setForeground(tonoOro);
        jCFiltro.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        jCFiltro.setPreferredSize(new java.awt.Dimension(250, 40));
        jCFiltro.setBackground(new java.awt.Color(35, 35, 40));
        jCFiltro.setForeground(java.awt.Color.WHITE);
        jCFiltro.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(90, 90, 95), 1),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        filterPanel.add(jLOrdenar);
        filterPanel.add(jCFiltro);
        
        topPanel.add(filterPanel, java.awt.BorderLayout.SOUTH);

        // Center Panel (Table)
        jTHistorial.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        jTHistorial.setRowHeight(30);
        jTHistorial.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        jTHistorial.setBackground(new java.awt.Color(30, 30, 34));
        jTHistorial.setForeground(java.awt.Color.WHITE);
        jTHistorial.setGridColor(new java.awt.Color(70, 70, 75));
        jTHistorial.setSelectionBackground(new java.awt.Color(70, 58, 35));
        jTHistorial.setSelectionForeground(java.awt.Color.WHITE);
        jTHistorial.getTableHeader().setBackground(metal);
        jTHistorial.getTableHeader().setForeground(tonoOro);

        jScrollPane1.getViewport().setBackground(new java.awt.Color(30, 30, 34));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(75, 75, 80), 2),
            javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));

        // Bottom Panel (Buttons)
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        bottomPanel.setOpaque(false);

        jBImprimirTicket.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jBImprimirTicket.setPreferredSize(new java.awt.Dimension(200, 50));
        jBImprimirTicket.setBackground(new java.awt.Color(44, 44, 48));
        jBImprimirTicket.setForeground(tonoOro);
        jBImprimirTicket.setFocusPainted(false);
        jBImprimirTicket.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jBRegresar.setBackground(new java.awt.Color(44, 44, 48));
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setFocusPainted(false);
        jBRegresar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        bottomPanel.add(jBImprimirTicket, java.awt.BorderLayout.WEST);
        bottomPanel.add(jBRegresar, java.awt.BorderLayout.EAST);

        panelContenido.add(topPanel, java.awt.BorderLayout.NORTH);
        panelContenido.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        panelContenido.add(bottomPanel, java.awt.BorderLayout.SOUTH);

        panelFondoAcero.add(panelContenido, java.awt.BorderLayout.CENTER);
        jPPrincipal.add(panelFondoAcero, java.awt.BorderLayout.CENTER);

        jPPrincipal.revalidate();
        jPPrincipal.repaint();

        cargarFiltros();
        jCFiltro.addActionListener(e -> cargarHistorial());
        cargarHistorial();
    }

    private void cargarFiltros() {
        jCFiltro.removeAllItems();
        jCFiltro.addItem("Mostrar Todo");
        jCFiltro.addItem("Dia de Hoy");
        jCFiltro.addItem("Esta Semana");
        jCFiltro.addItem("Este Mes");

        String sql = "SELECT DISTINCT e.nombre FROM ordenes o JOIN empleados e ON o.id_empleado = e.id_empleado WHERE o.estado = 'Cobrada'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
             
            while(rs.next()) {
                String nom = rs.getString("nombre");
                if(nom != null) {
                    jCFiltro.addItem("Mesero: " + nom);
                }
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar meseros", ex);
        }
    }

    private void cargarHistorial() {
        String seleccion = (String) jCFiltro.getSelectedItem();
        if (seleccion == null) return;

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Mesa", "Mesero", "Fecha", "Hora", "Total Orden"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTHistorial.setModel(modelo);

        // Alinear texto de las celdas al centro
        javax.swing.table.DefaultTableCellRenderer centerRender = new javax.swing.table.DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        for(int i = 0; i < jTHistorial.getColumnCount(); i++){
            jTHistorial.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }

        String sql = "SELECT o.mesa, e.nombre, o.fecha, o.hora, " +
                     "(MAX(IFNULL(pq.precio_paquete, 0)) + IFNULL(SUM(p.costo * d.cant), 0)) AS total_orden " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "LEFT JOIN paquetes pq ON o.id_paquete = pq.id_paquete " +
                     "LEFT JOIN detalle_orden d ON o.id_orden = d.id_orden " +
                     "LEFT JOIN platillos p ON d.id_platillo = p.id_platillo " +
                     "WHERE o.estado = 'Cobrada' ";
                     
        if (seleccion.equals("Dia de Hoy")) {
            sql += "AND o.fecha = CURDATE() ";
        } else if (seleccion.equals("Esta Semana")) {
            sql += "AND YEARWEEK(o.fecha, 1) = YEARWEEK(CURDATE(), 1) ";
        } else if (seleccion.equals("Este Mes")) {
            sql += "AND MONTH(o.fecha) = MONTH(CURDATE()) AND YEAR(o.fecha) = YEAR(CURDATE()) ";
        } else if (seleccion.startsWith("Mesero: ")) {
            String mesero = seleccion.substring(8);
            sql += "AND e.nombre = ? ";
        }

        sql += "GROUP BY o.mesa, e.nombre, o.fecha, o.hora ORDER BY o.fecha DESC, o.hora DESC";

        double granTotal = 0.0;
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            if (seleccion.startsWith("Mesero: ")) {
                 pst.setString(1, seleccion.substring(8));
            }

            try (ResultSet rs = pst.executeQuery()) {
                while(rs.next()) {
                    String mesa = rs.getString("mesa");
                    String empleado = rs.getString("nombre");
                    java.sql.Date fecha = rs.getDate("fecha");
                    java.sql.Time hora = rs.getTime("hora");
                    double totalOrd = rs.getDouble("total_orden");
                    
                    granTotal += totalOrd;
                    
                    modelo.addRow(new Object[]{
                        mesa, 
                        empleado != null ? empleado : "N/A", 
                        fecha, 
                        hora, 
                        String.format("$%.2f", totalOrd)
                    });
                }
            }
            
            // Fila vacía de separación
            modelo.addRow(new Object[]{"", "", "", "", ""});
            // Fila de TOTAL
            modelo.addRow(new Object[]{"", "", "", "TOTAL VENTA:", String.format("$%.2f", granTotal)});
            
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar historial", ex);
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
        jLNVentana = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTHistorial = new javax.swing.JTable();
        jLOrdenar = new javax.swing.JLabel();
        jCFiltro = new javax.swing.JComboBox<>();
        jBRegresar = new javax.swing.JButton();
        jBImprimirTicket = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLNVentana.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLNVentana.setForeground(new java.awt.Color(255, 255, 255));
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLNVentana.setText("HISTORIAL DE ORDENES");

        jTHistorial.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTHistorial);

        jLOrdenar.setForeground(new java.awt.Color(255, 255, 255));
        jLOrdenar.setText("ORDENAR POR:");

        jBRegresar.setBackground(new java.awt.Color(204, 204, 204));
        jBRegresar.setText("REGRESAR");

        jBImprimirTicket.setBackground(new java.awt.Color(204, 204, 204));
        jBImprimirTicket.setText("IMPRIMIR TICKET");

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLNVentana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPPrincipalLayout.createSequentialGroup()
                                .addComponent(jLOrdenar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jBImprimirTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLNVentana)
                .addGap(18, 18, 18)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLOrdenar)
                    .addComponent(jCFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBImprimirTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBRegresar)
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
            .addComponent(jPPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        java.awt.EventQueue.invokeLater(() -> new PSHistorial().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBImprimirTicket;
    private javax.swing.JButton jBRegresar;
    private javax.swing.JComboBox<String> jCFiltro;
    private javax.swing.JLabel jLNVentana;
    private javax.swing.JLabel jLOrdenar;
    private javax.swing.JPanel jPPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTHistorial;
    // End of variables declaration//GEN-END:variables
}
