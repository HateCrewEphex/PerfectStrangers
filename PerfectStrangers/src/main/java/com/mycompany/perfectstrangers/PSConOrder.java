/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ephex
 */
public class PSConOrder extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSConOrder.class.getName());

    /**
     * Creates new form PSConOrder
     */
    public PSConOrder() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {}
        this.setTitle("PerfectStrangers - Consola de Órdenes");

        configurarInterfaz();
        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });

        jBEntregarOrden.addActionListener(evt -> entregarOrdenPrimera());
        cargarOrdenes();
    }

    private void configurarInterfaz() {
        jPPrincipal.removeAll();
        jPPrincipal.setLayout(new java.awt.BorderLayout(10, 10));
        jPPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel for Title
        javax.swing.JPanel topPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        topPanel.setOpaque(false);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        jLabel1.setForeground(java.awt.Color.WHITE);
        jLabel1.setText("CONSOLA DE ÓRDENES - PREPARACIÓN");
        topPanel.add(jLabel1);

        // Center Panel for Orders (GridLayout 1x3)
        javax.swing.JPanel centerPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 3, 20, 20));
        centerPanel.setOpaque(false);

        // Card 1
        javax.swing.JPanel card1 = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        card1.setOpaque(false);
        jLOrdenaEntregar.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 18));
        card1.add(jLOrdenaEntregar, java.awt.BorderLayout.CENTER);
        jBEntregarOrden.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        card1.add(jBEntregarOrden, java.awt.BorderLayout.SOUTH);

        // Card 2
        javax.swing.JPanel card2 = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        card2.setOpaque(false);
        jLSegundaOrden.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 18));
        card2.add(jLSegundaOrden, java.awt.BorderLayout.CENTER);
        
        // Card 3
        javax.swing.JPanel card3 = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        card3.setOpaque(false);
        jLTercerOrden.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 18));
        card3.add(jLTercerOrden, java.awt.BorderLayout.CENTER);

        centerPanel.add(card1);
        centerPanel.add(card2);
        centerPanel.add(card3);

        // Bottom Panel for info & return button
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        bottomPanel.setOpaque(false);
        
        javax.swing.JPanel queueInfoPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        queueInfoPanel.setOpaque(false);
        jLOrdenes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        jLCOrdenes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        queueInfoPanel.add(jLOrdenes);
        queueInfoPanel.add(jLCOrdenes);
        
        javax.swing.JPanel backBtnPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        backBtnPanel.setOpaque(false);
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        backBtnPanel.add(jBRegresar);

        bottomPanel.add(queueInfoPanel, java.awt.BorderLayout.WEST);
        bottomPanel.add(backBtnPanel, java.awt.BorderLayout.EAST);

        jPPrincipal.add(topPanel, java.awt.BorderLayout.NORTH);
        jPPrincipal.add(centerPanel, java.awt.BorderLayout.CENTER);
        jPPrincipal.add(bottomPanel, java.awt.BorderLayout.SOUTH);
        
        jPPrincipal.revalidate();
        jPPrincipal.repaint();
    }

    private class ItemOrden {
        String nombre;
        int cant;
        public ItemOrden(String n, int c) { nombre = n; cant = c; }
    }
    
    private class OrdenPendiente {
        String mesa;
        java.sql.Date fecha;
        java.sql.Time hora;
        List<ItemOrden> items = new ArrayList<>();
    }

    private List<OrdenPendiente> listaOrdenes = new ArrayList<>();

    private void cargarOrdenes() {
        listaOrdenes.clear();
        String sql = "SELECT o.mesa, o.fecha, o.hora, p.nombre AS nomP, pq.nombrePaq AS nomPaq, o.cant " +
                     "FROM ordenes o " +
                     "LEFT JOIN platillos p ON o.id_platillo = p.id_platillo " +
                     "LEFT JOIN paquetes pq ON o.id_platillo = pq.id " +
                     "WHERE o.estado = 'Levantada' " +
                     "ORDER BY o.fecha ASC, o.hora ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            Map<String, OrdenPendiente> mapa = new LinkedHashMap<>();

            while (rs.next()) {
                String mesa = rs.getString("mesa");
                java.sql.Date fecha = rs.getDate("fecha");
                java.sql.Time hora = rs.getTime("hora");
                String nP = rs.getString("nomP");
                String nPaq = rs.getString("nomPaq");
                String nombreItem = (nP != null) ? nP : (nPaq != null ? nPaq : "Desconocido");
                int cant = rs.getInt("cant");

                String key = mesa + "|" + fecha + "|" + hora;
                OrdenPendiente op = mapa.get(key);
                if (op == null) {
                    op = new OrdenPendiente();
                    op.mesa = mesa;
                    op.fecha = fecha;
                    op.hora = hora;
                    mapa.put(key, op);
                    listaOrdenes.add(op);
                }
                op.items.add(new ItemOrden(nombreItem, cant));
            }

            mostrarOrdenes();

        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar ordenes", ex);
        }
    }

    private void mostrarOrdenes() {
        jLOrdenaEntregar.setText("-");
        jLOrdenaEntregar.setOpaque(true);
        jLOrdenaEntregar.setBackground(Color.BLACK);
        jLOrdenaEntregar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jBEntregarOrden.setEnabled(false);

        jLSegundaOrden.setText("-");
        jLSegundaOrden.setOpaque(true);
        jLSegundaOrden.setBackground(Color.BLACK);
        jLSegundaOrden.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLTercerOrden.setText("-");
        jLTercerOrden.setOpaque(true);
        jLTercerOrden.setBackground(Color.BLACK);
        jLTercerOrden.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLCOrdenes.setText("0");

        if (listaOrdenes.size() > 0) {
            OrdenPendiente o1 = listaOrdenes.get(0);
            jLOrdenaEntregar.setText(formatHTML(o1));
            jLOrdenaEntregar.setBackground(new Color(34, 139, 34)); // Verde
            jBEntregarOrden.setEnabled(true);
        }
        if (listaOrdenes.size() > 1) {
            OrdenPendiente o2 = listaOrdenes.get(1);
            jLSegundaOrden.setText(formatHTML(o2));
            jLSegundaOrden.setBackground(new Color(204, 102, 0)); // Naranja
        }
        if (listaOrdenes.size() > 2) {
            OrdenPendiente o3 = listaOrdenes.get(2);
            jLTercerOrden.setText(formatHTML(o3));
            jLTercerOrden.setBackground(new Color(204, 102, 0)); // Naranja
        }

        int faltantes = listaOrdenes.size() > 3 ? listaOrdenes.size() - 3 : 0;
        jLCOrdenes.setText(String.valueOf(faltantes));
    }

    private String formatHTML(OrdenPendiente o) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><div style='text-align: center; padding: 20px; width: 100%; font-size: 16px;'>");
        sb.append("<h1 style='font-size: 28px; margin: 0;'>").append(o.mesa).append("</h1>");
        sb.append("<h3 style='color: #DDDDDD; font-size: 20px; margin: 5px 0 20px 0;'>").append(o.hora.toString()).append("</h3>");
        for (ItemOrden item : o.items) {
            sb.append("<div style='font-size: 22px; padding: 5px;'>").append(item.cant).append(" x ").append(item.nombre).append("</div>");
        }
        sb.append("</div></html>");
        return sb.toString();
    }

    private void entregarOrdenPrimera() {
        if (listaOrdenes.isEmpty()) return;
        OrdenPendiente o = listaOrdenes.get(0);
        
        String sql = "UPDATE ordenes SET estado = 'Entregada' WHERE mesa = ? AND fecha = ? AND hora = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, o.mesa);
            pst.setDate(2, o.fecha);
            pst.setTime(3, o.hora);
            pst.executeUpdate();
            
            cargarOrdenes(); 
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al entregar orden", ex);
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
        jLOrdenaEntregar = new javax.swing.JLabel();
        jBEntregarOrden = new javax.swing.JButton();
        jLSegundaOrden = new javax.swing.JLabel();
        jLTercerOrden = new javax.swing.JLabel();
        jLOrdenes = new javax.swing.JLabel();
        jLCOrdenes = new javax.swing.JLabel();
        jBRegresar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLOrdenaEntregar.setForeground(new java.awt.Color(255, 255, 255));
        jLOrdenaEntregar.setText("-");

        jBEntregarOrden.setBackground(new java.awt.Color(204, 204, 204));
        jBEntregarOrden.setText("ENTREGAR");

        jLSegundaOrden.setForeground(new java.awt.Color(255, 255, 255));
        jLSegundaOrden.setText("-");

        jLTercerOrden.setForeground(new java.awt.Color(255, 255, 255));
        jLTercerOrden.setText("-");

        jLOrdenes.setForeground(new java.awt.Color(255, 255, 255));
        jLOrdenes.setText("Ordenes por delante: ");

        jLCOrdenes.setForeground(new java.awt.Color(255, 255, 255));
        jLCOrdenes.setText("-");

        jBRegresar.setBackground(new java.awt.Color(204, 204, 204));
        jBRegresar.setText("REGRESAR");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ORDENES A PREPARAR");

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jBEntregarOrden, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(jLOrdenaEntregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(32, 32, 32)
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLSegundaOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLOrdenes))
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPPrincipalLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLTercerOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPPrincipalLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLCOrdenes, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 4, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLOrdenaEntregar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLSegundaOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLTercerOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBEntregarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLOrdenes)
                    .addComponent(jLCOrdenes))
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
            .addComponent(jPPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        java.awt.EventQueue.invokeLater(() -> new PSConOrder().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBEntregarOrden;
    private javax.swing.JButton jBRegresar;
    private javax.swing.JLabel jLCOrdenes;
    private javax.swing.JLabel jLOrdenaEntregar;
    private javax.swing.JLabel jLOrdenes;
    private javax.swing.JLabel jLSegundaOrden;
    private javax.swing.JLabel jLTercerOrden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPPrincipal;
    // End of variables declaration//GEN-END:variables
}
