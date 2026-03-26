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
        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
        
        jBEntregarOrden.addActionListener(evt -> entregarOrdenPrimera());
        cargarOrdenes();
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
        sb.append("<html><div style='text-align: center; padding: 10px; width: 140px;'>");
        sb.append("<h2>").append(o.mesa).append("</h2>");
        sb.append("<i>").append(o.hora.toString()).append("</i><br><br>");
        for (ItemOrden item : o.items) {
            sb.append(item.cant).append("x ").append(item.nombre).append("<br>");
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
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

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
