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
        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
        
        configurarInterfaz();
    }

    private void configurarInterfaz() {
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

        String sql = "SELECT o.mesa, e.nombre, o.fecha, o.hora, " +
                     "SUM(IFNULL(p.costo, 0) * o.cant + IFNULL(pq.precio_paquete, 0) * o.cant) AS total_orden " +
                     "FROM ordenes o " +
                     "LEFT JOIN platillos p ON o.id_platillo = p.id_platillo " +
                     "LEFT JOIN paquetes pq ON o.id_platillo = pq.id " +
                     "LEFT JOIN empleados e ON o.id_empleado = e.id_empleado " +
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

        jCFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
