/*
 * Ventana para cancelar órdenes completas o platillos específicos en preparación
 */
package com.mycompany.perfectstrangers;

import java.awt.Color;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ephex
 */
public class PSCancelarOrden extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSCancelarOrden.class.getName());
    private List<Orden> ordenesActivas;
    private Orden ordenSeleccionada;
    private List<DetalleOrden> detallesActuales;

    /**
     * Creates new form PSCancelarOrden
     */
    public PSCancelarOrden() {
        initComponents();
        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {}
        this.setTitle("PerfectStrangers - Cancelar Órdenes");

        configurarInterfaz();
        cargarOrdenes();
    }

    private void configurarInterfaz() {
        // Colores temáticos
        Color tonoOro = new Color(204, 169, 90);
        Color casiNegro = new Color(25, 25, 25);
        Color metal = new Color(45, 45, 47);
        Color oscuro = new Color(15, 12, 10);

        jPPrincipal.removeAll();
        jPPrincipal.setLayout(new java.awt.BorderLayout());
        
        // Panel de fondo con efecto acero
        javax.swing.JPanel panelFondoAcero = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(oscuro);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Efecto viñeta
                java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f, Math.max(getWidth(), getHeight()) / 1.1f,
                    new float[]{ 0.4f, 1.0f },
                    new java.awt.Color[]{
                        new java.awt.Color(0, 0, 0, 0), 
                        new java.awt.Color(10, 10, 10, 240)
                    }
                );
                g2.setPaint(rgp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Marco de acero
                g2.setStroke(new java.awt.BasicStroke(20f));
                g2.setColor(metal);
                g2.drawRect(10, 10, getWidth()-20, getHeight()-20);
                
                g2.dispose();
            }
        };
        panelFondoAcero.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel principal - CONTENIDO CENTRAL
        javax.swing.JPanel panelCentral = new javax.swing.JPanel(new java.awt.BorderLayout(20, 20));
        panelCentral.setOpaque(false);

        // ===== SECCIÓN SUPERIOR: LISTA DE ÓRDENES =====
        javax.swing.JPanel panelOrdenes = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        panelOrdenes.setOpaque(false);
        panelOrdenes.setPreferredSize(new java.awt.Dimension(400, 250));

        javax.swing.JLabel lblOrdenes = new javax.swing.JLabel("ÓRDENES ACTIVAS");
        lblOrdenes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblOrdenes.setForeground(tonoOro);
        panelOrdenes.add(lblOrdenes, java.awt.BorderLayout.NORTH);

        // Tabla de órdenes
        tblOrdenes = new javax.swing.JTable();
        tblOrdenes.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Mesa", "Empleado", "Estado", "Pago", "Total"}
        ) {
            Class<?>[] types = new Class[] { Integer.class, Integer.class, String.class, String.class, String.class, String.class };
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false };
            
            @Override
            public Class<?> getColumnClass(int columnIndex) { return types[columnIndex]; }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        });
        tblOrdenes.setBackground(metal);
        tblOrdenes.setForeground(tonoOro);
        tblOrdenes.setSelectionBackground(new Color(100, 100, 100));
        tblOrdenes.setRowHeight(25);
        tblOrdenes.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetallesOrden();
            }
        });

        javax.swing.JScrollPane scrollOrdenes = new javax.swing.JScrollPane(tblOrdenes);
        scrollOrdenes.setBackground(casiNegro);
        panelOrdenes.add(scrollOrdenes, java.awt.BorderLayout.CENTER);

        panelCentral.add(panelOrdenes, java.awt.BorderLayout.NORTH);

        // ===== SECCIÓN INFERIOR: DETALLES DE LA ORDEN =====
        javax.swing.JPanel panelDetalles = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        panelDetalles.setOpaque(false);
        panelDetalles.setPreferredSize(new java.awt.Dimension(400, 300));

        javax.swing.JLabel lblDetalles = new javax.swing.JLabel("DETALLES DE LA ORDEN");
        lblDetalles.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblDetalles.setForeground(tonoOro);
        panelDetalles.add(lblDetalles, java.awt.BorderLayout.NORTH);

        // Tabla de detalles
        tblDetalles = new javax.swing.JTable();
        tblDetalles.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Producto", "Cantidad", "Precio Unit.", "Subtotal"}
        ) {
            Class<?>[] types = new Class[] { Integer.class, String.class, Integer.class, String.class, String.class };
            boolean[] canEdit = new boolean[] { false, false, false, false, false };
            
            @Override
            public Class<?> getColumnClass(int columnIndex) { return types[columnIndex]; }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        });
        tblDetalles.setBackground(metal);
        tblDetalles.setForeground(tonoOro);
        tblDetalles.setSelectionBackground(new Color(100, 100, 100));
        tblDetalles.setRowHeight(25);
        
        javax.swing.JScrollPane scrollDetalles = new javax.swing.JScrollPane(tblDetalles);
        scrollDetalles.setBackground(casiNegro);
        panelDetalles.add(scrollDetalles, java.awt.BorderLayout.CENTER);

        // Panel de botones de acciones
        javax.swing.JPanel panelBotones = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));
        panelBotones.setOpaque(false);

        jBCancelarItem = new javax.swing.JButton("❌ Cancelar Platillo");
        jBCancelarItem.setBackground(new Color(220, 53, 69));
        jBCancelarItem.setForeground(java.awt.Color.WHITE);
        jBCancelarItem.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        jBCancelarItem.setPreferredSize(new java.awt.Dimension(180, 35));
        jBCancelarItem.addActionListener(evt -> cancelarItemSeleccionado());
        panelBotones.add(jBCancelarItem);

        jBCancelarOrden = new javax.swing.JButton("❌ Cancelar Orden");
        jBCancelarOrden.setBackground(new Color(220, 53, 69));
        jBCancelarOrden.setForeground(java.awt.Color.WHITE);
        jBCancelarOrden.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        jBCancelarOrden.setPreferredSize(new java.awt.Dimension(180, 35));
        jBCancelarOrden.addActionListener(evt -> cancelarOrdenCompleta());
        panelBotones.add(jBCancelarOrden);

        panelDetalles.add(panelBotones, java.awt.BorderLayout.SOUTH);

        panelCentral.add(panelDetalles, java.awt.BorderLayout.CENTER);

        panelFondoAcero.add(panelCentral, java.awt.BorderLayout.CENTER);

        // ===== PANEL INFERIOR CON BOTÓN REGRESAR =====
        javax.swing.JPanel panelInferior = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 10));
        panelInferior.setOpaque(false);
        
        jBRegresar = new javax.swing.JButton("← Regresar");
        jBRegresar.setBackground(metal);
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        jBRegresar.setPreferredSize(new java.awt.Dimension(120, 35));
        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
        panelInferior.add(jBRegresar);

        panelFondoAcero.add(panelInferior, java.awt.BorderLayout.SOUTH);

        jPPrincipal.add(panelFondoAcero, java.awt.BorderLayout.CENTER);
        jPPrincipal.revalidate();
        jPPrincipal.repaint();
    }

    private void cargarOrdenes() {
        try {
            // Obtener órdenes que no estén entregadas
            ordenesActivas = OrdenDAO.obtenerOrdenesEnPreparacion();
            List<Orden> pendientes = OrdenDAO.obtenerOrdenesPendientes();
            
            // Combinar y eliminar duplicados
            for (Orden o : pendientes) {
                if (!ordenesActivas.contains(o)) {
                    ordenesActivas.add(o);
                }
            }

            DefaultTableModel model = (DefaultTableModel) tblOrdenes.getModel();
            model.setRowCount(0);

            for (Orden orden : ordenesActivas) {
                model.addRow(new Object[] {
                    orden.getIdOrden(),
                    orden.getMesa(),
                    orden.getNombreEmpleado(),
                    orden.getEstadoPreparacion(),
                    orden.getEstadoPago(),
                    String.format("$%.2f", orden.getTotalCalculado())
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar órdenes: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error loading orders: " + e.getMessage());
        }
    }

    private void cargarDetallesOrden() {
        int filaSeleccionada = tblOrdenes.getSelectedRow();
        if (filaSeleccionada < 0) return;

        try {
            int idOrden = (Integer) tblOrdenes.getValueAt(filaSeleccionada, 0);
            ordenSeleccionada = OrdenDAO.obtenerOrdenById(idOrden);
            detallesActuales = DetalleOrdenDAO.obtenerDetallesPorOrden(idOrden);

            DefaultTableModel model = (DefaultTableModel) tblDetalles.getModel();
            model.setRowCount(0);

            for (DetalleOrden detalle : detallesActuales) {
                double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
                model.addRow(new Object[] {
                    detalle.getIdDetalle(),
                    detalle.getNombreProducto(),
                    detalle.getCantidad(),
                    String.format("$%.2f", detalle.getPrecioUnitario()),
                    String.format("$%.2f", subtotal)
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar detalles: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error loading order details: " + e.getMessage());
        }
    }

    private void cancelarItemSeleccionado() {
        int filaOrdenSeleccionada = tblOrdenes.getSelectedRow();
        int filaDetalleSeleccionada = tblDetalles.getSelectedRow();

        if (filaOrdenSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona una orden.", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (filaDetalleSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un platillo para cancelar.", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Confirmar cancelación
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas cancelar este platillo?",
            "Confirmar Cancelación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int idDetalle = (Integer) tblDetalles.getValueAt(filaDetalleSeleccionada, 0);
            
            // Obtener detalles del platillo a cancelar
            DetalleOrden detalleACancelar = DetalleOrdenDAO.obtenerDetalleById(idDetalle);
            
            // Devolver el inventario
            devolverInventario(detalleACancelar);
            
            // Eliminar el detalle
            DetalleOrdenDAO.eliminarDetalleOrden(idDetalle);
            
            // Recalcular el total de la orden
            double nuevoTotal = DetalleOrdenDAO.calcularTotalOrden(ordenSeleccionada.getIdOrden());
            OrdenDAO.actualizarTotal(ordenSeleccionada.getIdOrden(), nuevoTotal);
            
            JOptionPane.showMessageDialog(this, "Platillo cancelado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Recargar datos
            cargarOrdenes();
            cargarDetallesOrden();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cancelar platillo: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error canceling item: " + e.getMessage());
        }
    }

    private void cancelarOrdenCompleta() {
        int filaSeleccionada = tblOrdenes.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Por favor selecciona una orden para cancelar.", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Confirmar cancelación
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas cancelar TODA la orden?\n" +
            "Se devolverá el inventario de todos los platillos.",
            "Confirmar Cancelación de Orden Completa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int idOrden = (Integer) tblOrdenes.getValueAt(filaSeleccionada, 0);
            Orden ordenACancelar = OrdenDAO.obtenerOrdenById(idOrden);
            
            // Obtener todos los detalles de la orden
            List<DetalleOrden> detalles = DetalleOrdenDAO.obtenerDetallesPorOrden(idOrden);
            
            // Devolver todo el inventario
            for (DetalleOrden detalle : detalles) {
                devolverInventario(detalle);
            }
            
            // Marcar la orden como cancelada
            OrdenDAO.actualizarEstadoPreparacion(idOrden, "Cancelado");
            
            // Actualizar estado de pago a Cancelado también (opcional)
            OrdenDAO.actualizarEstadoPago(idOrden, "Cancelado");
            
            JOptionPane.showMessageDialog(this, "Orden cancelada exitosamente.\n" +
                "El inventario ha sido devuelto.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Recargar datos
            cargarOrdenes();
            if (tblOrdenes.getRowCount() > 0) {
                cargarDetallesOrden();
            } else {
                DefaultTableModel model = (DefaultTableModel) tblDetalles.getModel();
                model.setRowCount(0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cancelar orden: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error canceling order: " + e.getMessage());
        }
    }

    private void devolverInventario(DetalleOrden detalle) throws SQLException {
        // Usar el método de RecetaDAO que automáticamente devuelve los insumos
        RecetaDAO.devolverInsumos(detalle.getIdProducto(), detalle.getCantidad());
    }

    // Variables de componentes
    private javax.swing.JPanel jPPrincipal;
    private javax.swing.JTable tblOrdenes;
    private javax.swing.JTable tblDetalles;
    private javax.swing.JButton jBCancelarItem;
    private javax.swing.JButton jBCancelarOrden;
    private javax.swing.JButton jBRegresar;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is generated by the Form Editor.
     */
    private void initComponents() {
        jPPrincipal = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        getContentPane().add(jPPrincipal, java.awt.BorderLayout.CENTER);
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new PSCancelarOrden().setVisible(true));
    }
}
