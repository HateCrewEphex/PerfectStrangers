/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 *
 * @author Ephex
 */
public class PSCobOrden extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSCobOrden.class.getName());

    /**
     * Creates new form PSCobOrden
     */
    public PSCobOrden() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
        
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        jPPrincipal.removeAll();
        jPPrincipal.setLayout(new java.awt.BorderLayout(20, 20));
        jPPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel
        javax.swing.JPanel topPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        topPanel.setOpaque(false);
        jLNVentana.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topPanel.add(jLNVentana, java.awt.BorderLayout.NORTH);

        javax.swing.JPanel selectionPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 10));
        selectionPanel.setOpaque(false);
        
        jComboBox1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        
        jLAtendio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        jLNAtendio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        
        selectionPanel.add(jComboBox1);
        selectionPanel.add(jLAtendio);
        selectionPanel.add(jLNAtendio);
        
        topPanel.add(selectionPanel, java.awt.BorderLayout.SOUTH);

        // Center Panel (Splitting Insumos and Facturacion)
        javax.swing.JPanel centerPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 20, 20));
        centerPanel.setOpaque(false);
        
        jLInsumos.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 18));
        jLInsumos.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLInsumos.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
        
        jLTFacturacion.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 18));
        jLTFacturacion.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLTFacturacion.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));

        centerPanel.add(jLInsumos);
        centerPanel.add(jLTFacturacion);

        // Bottom Panel
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel(new java.awt.BorderLayout(20, 20));
        bottomPanel.setOpaque(false);

        javax.swing.JPanel totalPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        totalPanel.setOpaque(false);
        jLTTotal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        jLTotalCobrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 36));
        jLTotalCobrar.setForeground(new java.awt.Color(50, 205, 50)); // Green color for total
        totalPanel.add(jLTTotal);
        totalPanel.add(jLTotalCobrar);
        
        javax.swing.JPanel buttonsPanel = new javax.swing.JPanel(new java.awt.BorderLayout(20, 0));
        buttonsPanel.setOpaque(false);
        
        jBCobrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        jBCobrar.setPreferredSize(new java.awt.Dimension(200, 60));
        jBCobrar.setBackground(new java.awt.Color(40, 167, 69)); // Action green
        jBCobrar.setForeground(java.awt.Color.WHITE);

        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        buttonsPanel.add(jBCobrar, java.awt.BorderLayout.WEST);
        buttonsPanel.add(jBRegresar, java.awt.BorderLayout.EAST);

        bottomPanel.add(totalPanel, java.awt.BorderLayout.NORTH);
        bottomPanel.add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        jPPrincipal.add(topPanel, java.awt.BorderLayout.NORTH);
        jPPrincipal.add(centerPanel, java.awt.BorderLayout.CENTER);
        jPPrincipal.add(bottomPanel, java.awt.BorderLayout.SOUTH);

        jPPrincipal.revalidate();
        jPPrincipal.repaint();

        jComboBox1.addItem("Selecciona una mesa");
        for (int i = 1; i <= 10; i++) {
            jComboBox1.addItem("Mesa " + i);
        }

        jComboBox1.addActionListener(evt -> cargarOrdenCobrar());
        jBCobrar.addActionListener(evt -> cobrarOrden());
        
        limpiarPantalla();
    }

    private void limpiarPantalla() {
        jLInsumos.setText("");
        jLInsumos.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLNAtendio.setText("-");
        jLTotalCobrar.setText("$0.00");
    }

    private void cargarOrdenCobrar() {
        limpiarPantalla();
        if (jComboBox1.getSelectedIndex() <= 0) return;
        
        String mesaStr = jComboBox1.getSelectedItem().toString();
        
        // Consulta para obtener las ordenes "Entregada"
        String sql = "SELECT o.mesa, e.nombre AS nomEmp, p.nombre AS nomP, pq.nombrePaq AS nomPaq, p.costo AS costoP, pq.precio_paquete AS costoPaq, o.cant " +
                     "FROM ordenes o " +
                     "LEFT JOIN platillos p ON o.id_platillo = p.id_platillo " +
                     "LEFT JOIN paquetes pq ON o.id_platillo = pq.id " +
                     "LEFT JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "WHERE o.estado = 'Entregada' AND o.mesa = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, mesaStr);
            try (ResultSet rs = pst.executeQuery()) {
StringBuilder html = new StringBuilder("<html><div style='padding:15px; font-size: 18px;'>");
                double total = 0.0;
                boolean existeOrden = false;
                String empleado = "-";

                while (rs.next()) {
                    existeOrden = true;
                    String nP = rs.getString("nomP");
                    String nPaq = rs.getString("nomPaq");
                    String nombreItem = (nP != null) ? nP : (nPaq != null ? nPaq : "Desconocido");

                    double valor = 0;
                    if (nP != null) valor = rs.getDouble("costoP");
                    else if (nPaq != null) valor = rs.getDouble("costoPaq");

                    int cant = rs.getInt("cant");
                    total += (valor * cant);
                    empleado = rs.getString("nomEmp") != null ? rs.getString("nomEmp") : "Desconocido";

                    html.append("<p style='margin-bottom: 5px;'>")
                        .append("<b>").append(cant).append("x</b> ").append(nombreItem)
                        .append(" <span style='color: #DDDDDD;'>($").append(String.format("%.2f", valor)).append(" c/u)</span></p>");
                }
                html.append("</div></html>");

                if (existeOrden) {
                    jLInsumos.setText(html.toString());
                    jLNAtendio.setText(empleado);
                    jLTotalCobrar.setText("$" + String.format("%.2f", total));
                } else {
                    jLInsumos.setText("No hay orden pendiente de cobro en esta mesa.");
                }
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar datos para cobrar", ex);
        }
    }

    private void cobrarOrden() {
        if (jComboBox1.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una mesa para cobrar.");
            return;
        }
        String mesaStr = jComboBox1.getSelectedItem().toString();
        
        // Verificar si de verdad hay cuenta que cobrar
        if (jLTotalCobrar.getText().equals("$0.00")) {
            JOptionPane.showMessageDialog(this, "No hay saldo por cobrar en esta mesa.");
            return;
        }

        String sql = "UPDATE ordenes SET estado = 'Cobrada' WHERE mesa = ? AND estado = 'Entregada'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, mesaStr);
            int filasModificadas = pst.executeUpdate();
            
            if (filasModificadas > 0) {
                JOptionPane.showMessageDialog(this, "Orden cobrada con éxito. Total: " + jLTotalCobrar.getText());
                cargarOrdenCobrar(); // refescar
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo realizar el cobro.");
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al procesar el cobro", ex);
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
        jComboBox1 = new javax.swing.JComboBox<>();
        jLInsumos = new javax.swing.JLabel();
        jLAtendio = new javax.swing.JLabel();
        jLNAtendio = new javax.swing.JLabel();
        jLTFacturacion = new javax.swing.JLabel();
        jBCobrar = new javax.swing.JButton();
        jLTTotal = new javax.swing.JLabel();
        jLTotalCobrar = new javax.swing.JLabel();
        jBRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLNVentana.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLNVentana.setForeground(new java.awt.Color(255, 255, 255));
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLNVentana.setText("COBRO DE MESA");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLInsumos.setForeground(new java.awt.Color(255, 255, 255));
        jLInsumos.setText("-");

        jLAtendio.setForeground(new java.awt.Color(255, 255, 255));
        jLAtendio.setText("ATENDIO: ");

        jLNAtendio.setForeground(new java.awt.Color(255, 255, 255));
        jLNAtendio.setText("-");

        jLTFacturacion.setForeground(new java.awt.Color(255, 255, 255));
        jLTFacturacion.setText("-");

        jBCobrar.setBackground(new java.awt.Color(204, 204, 204));
        jBCobrar.setText("COBRAR");

        jLTTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLTTotal.setForeground(new java.awt.Color(255, 255, 255));
        jLTTotal.setText("TOTAL:");

        jLTotalCobrar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLTotalCobrar.setForeground(new java.awt.Color(255, 255, 255));
        jLTotalCobrar.setText("-");

        jBRegresar.setBackground(new java.awt.Color(204, 204, 204));
        jBRegresar.setText("REGRESAR");

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLNVentana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addComponent(jLInsumos, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLTFacturacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPPrincipalLayout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLAtendio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLNAtendio, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPPrincipalLayout.createSequentialGroup()
                                .addComponent(jLTTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLTotalCobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 259, Short.MAX_VALUE))
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addComponent(jBCobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLNVentana, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLAtendio)
                    .addComponent(jLNAtendio))
                .addGap(18, 18, 18)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLInsumos, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addComponent(jLTFacturacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLTTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLTotalCobrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(jBCobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBRegresar)))
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
        java.awt.EventQueue.invokeLater(() -> new PSCobOrden().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCobrar;
    private javax.swing.JButton jBRegresar;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLAtendio;
    private javax.swing.JLabel jLInsumos;
    private javax.swing.JLabel jLNAtendio;
    private javax.swing.JLabel jLNVentana;
    private javax.swing.JLabel jLTFacturacion;
    private javax.swing.JLabel jLTTotal;
    private javax.swing.JLabel jLTotalCobrar;
    private javax.swing.JPanel jPPrincipal;
    // End of variables declaration//GEN-END:variables
}
