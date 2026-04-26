/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

/**
 *
 * @author Ephex
 */
public class PSCobOrden extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSCobOrden.class.getName());
    private double totalActual = 0.0;
    private String ultimoTicketHtml = "";
    private String ultimoMetodoPago = "Efectivo";

    /**
     * Creates new form PSCobOrden
     */
    public PSCobOrden() {
        initComponents();
        
        jBCobrar.setBackground(null);
        jCBMetodoPago.setBackground(null);
        jBTicket.setBackground(null);
        jBRegresar.setBackground(null);
        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {}
        this.setTitle("PerfectStrangers - Cobro de Órdenes");

        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
        
        configurarInterfaz();
    }

    private void configurarInterfaz() {
        // Rediseño Visual - "COBRO DE MESA" Dark Metal & Gold
        java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);

        jPPrincipal.removeAll();
        jPPrincipal.setLayout(new java.awt.BorderLayout());
        
        // --- FONDO GENERAL METÁLICO ---
        javax.swing.JPanel panelFondoAcero = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo oscuro texturizado "cuero" negro/gris
                g2.setColor(new java.awt.Color(18, 18, 20));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Viñeta
                java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f, Math.max(getWidth(), getHeight()) / 1.1f,
                    new float[]{ 0.4f, 1.0f },
                    new java.awt.Color[]{
                        new java.awt.Color(0, 0, 0, 0), 
                        new java.awt.Color(5, 5, 5, 240)
                    }
                );
                g2.setPaint(rgp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Marco exterior acero grueso (simulando tablet industrial)
                g2.setStroke(new java.awt.BasicStroke(24f));
                g2.setColor(new java.awt.Color(60, 60, 65));
                g2.drawRoundRect(12, 12, getWidth()-24, getHeight()-24, 20, 20);
                
                // Ribete interior Dorado
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.setColor(tonoOro);
                g2.drawRoundRect(24, 24, getWidth()-48, getHeight()-48, 15, 15);
                
                g2.dispose();
            }
        };
        panelFondoAcero.setBorder(javax.swing.BorderFactory.createEmptyBorder(45, 45, 45, 45));

        // --- CONTENEDOR CENTRAL DE FACTURACIÓN ---
        javax.swing.JPanel panelFacturacion = new javax.swing.JPanel(new java.awt.BorderLayout(0, 20));
        panelFacturacion.setOpaque(false);

        // TOP: Título y Selector de Mesa
        javax.swing.JPanel panelTop = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        panelTop.setOpaque(false);
        
        jLNVentana.setText("COBRO DE MESA");
        jLNVentana.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
        jLNVentana.setForeground(tonoOro);
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelTop.add(jLNVentana, java.awt.BorderLayout.NORTH);

        javax.swing.JPanel panelToolBar = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 0));
        panelToolBar.setOpaque(false);
        
        // ComboBox "Mesa" estilizado
        jComboBox1.setBackground(new java.awt.Color(40, 40, 45));
        jComboBox1.setForeground(java.awt.Color.WHITE);
        jComboBox1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        jComboBox1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 80), 2),
            javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));
        
        jLAtendio.setText("ATENDIO: ");
        jLAtendio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        jLAtendio.setForeground(tonoOro);

        jLMetodoPago.setText("MÉTODO: ");
        jLMetodoPago.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        jLMetodoPago.setForeground(tonoOro);
        jCBMetodoPago.removeAllItems();
        jCBMetodoPago.addItem("Efectivo");
        jCBMetodoPago.addItem("Tarjeta");
        jCBMetodoPago.addItem("Transferencia");
        jCBMetodoPago.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        jCBMetodoPago.setForeground(java.awt.Color.WHITE);
        jCBMetodoPago.setBackground(new java.awt.Color(40, 40, 45));
        
        jLNAtendio.setText("-");
        jLNAtendio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        jLNAtendio.setForeground(java.awt.Color.WHITE);
        
        panelToolBar.add(jComboBox1);
        panelToolBar.add(jLMetodoPago);
        panelToolBar.add(jCBMetodoPago);
        panelToolBar.add(jLAtendio);
        // Sin icono para evitar caracteres no soportados
        panelToolBar.add(jLNAtendio);

        panelTop.add(panelToolBar, java.awt.BorderLayout.SOUTH);

        // CENTRO: Recibo / Lista de Insumos "Dark Table"
        javax.swing.JPanel panelRecibo = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelRecibo.setOpaque(false);
        // Margen lateral grande para simular la caja de cobro centrada
        panelRecibo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 80, 0, 80));

        javax.swing.JPanel panelTablaAcero = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(22, 22, 25)); // Fondo gris muy oscuro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setStroke(new java.awt.BasicStroke(4f));
                g2.setColor(new java.awt.Color(70, 70, 75)); // Borde Plomo
                g2.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 10, 10);
                g2.dispose();
            }
        };
        panelTablaAcero.setOpaque(false);
        panelTablaAcero.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));

        jLInsumos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        jLInsumos.setForeground(java.awt.Color.WHITE);
        jLInsumos.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLInsumos.setBorder(null); // Quitar borde default
        jLInsumos.setOpaque(false);

        jLTFacturacion.setVisible(false); // Ocultar este label que usabas, lo manejaremos todo en jLInsumos como tabla HTML

        javax.swing.JScrollPane scrollInsumos = new javax.swing.JScrollPane(jLInsumos);
        scrollInsumos.setOpaque(false);
        scrollInsumos.getViewport().setOpaque(false);
        scrollInsumos.setBorder(null);
        
        panelTablaAcero.add(scrollInsumos, java.awt.BorderLayout.CENTER);
        panelRecibo.add(panelTablaAcero, java.awt.BorderLayout.CENTER);

        // BOTTOM: Totales y Botones
        javax.swing.JPanel panelAbajo = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        panelAbajo.setOpaque(false);
        panelAbajo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 80, 0, 80));

        // Letreros de TOTAL
        javax.swing.JPanel panelTotalesTexto = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelTotalesTexto.setOpaque(false);
        
        jLTTotal.setText("TOTAL:");
        jLTTotal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 36));
        jLTTotal.setForeground(tonoOro);
        
        jLTotalCobrar.setText("$0.00");
        jLTotalCobrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 42));
        jLTotalCobrar.setForeground(tonoOro);
        
        panelTotalesTexto.add(jLTTotal, java.awt.BorderLayout.WEST);
        panelTotalesTexto.add(jLTotalCobrar, java.awt.BorderLayout.EAST);

        // Botones de Acción (Cobrar / Regresar)
        javax.swing.JPanel panelBotones = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelBotones.setOpaque(false);
        
        // Estilo botón Metálico Oscuro
        jBCobrar.setText("COBRAR");
        jBCobrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        jBCobrar.setBackground(new java.awt.Color(50, 50, 55));
        jBCobrar.setForeground(tonoOro);
        jBCobrar.setFocusPainted(false);
        jBCobrar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 3),
            javax.swing.BorderFactory.createEmptyBorder(15, 50, 15, 50)
        ));

        jBTicket.setText("TICKET");
        jBTicket.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        jBTicket.setBackground(new java.awt.Color(50, 50, 55));
        jBTicket.setForeground(tonoOro);
        jBTicket.setFocusPainted(false);
        jBTicket.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 3),
            javax.swing.BorderFactory.createEmptyBorder(12, 32, 12, 32)
        ));

        jBRegresar.setText("REGRESAR");
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        jBRegresar.setBackground(new java.awt.Color(40, 40, 45));
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setFocusPainted(false);
        jBRegresar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 40, 10, 40)
        ));

        panelBotones.add(jBCobrar, java.awt.BorderLayout.WEST);
    javax.swing.JPanel panelAccionesDerecha = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
    panelAccionesDerecha.setOpaque(false);
    panelAccionesDerecha.add(jBTicket);
    panelAccionesDerecha.add(jBRegresar);
    panelBotones.add(panelAccionesDerecha, java.awt.BorderLayout.EAST);

        panelAbajo.add(panelTotalesTexto, java.awt.BorderLayout.NORTH);
        panelAbajo.add(panelBotones, java.awt.BorderLayout.SOUTH);

        // Ensamblar la vista de facturación
        panelFacturacion.add(panelTop, java.awt.BorderLayout.NORTH);
        panelFacturacion.add(panelRecibo, java.awt.BorderLayout.CENTER);
        panelFacturacion.add(panelAbajo, java.awt.BorderLayout.SOUTH);

        panelFondoAcero.add(panelFacturacion, java.awt.BorderLayout.CENTER);
        jPPrincipal.add(panelFondoAcero, java.awt.BorderLayout.CENTER);

        jPPrincipal.revalidate();
        jPPrincipal.repaint();

        // Listeners
        for (java.awt.event.ActionListener al : jComboBox1.getActionListeners()) jComboBox1.removeActionListener(al);
        for (java.awt.event.ActionListener al : jBCobrar.getActionListeners()) jBCobrar.removeActionListener(al);
        for (java.awt.event.ActionListener al : jBTicket.getActionListeners()) jBTicket.removeActionListener(al);

        jComboBox1.removeAllItems();
        jComboBox1.addItem("Selecciona una mesa");
        for (int i = 1; i <= 15; i++) {
            jComboBox1.addItem("Mesa " + i);
        }

        jComboBox1.addActionListener(evt -> cargarOrdenCobrar());
        jBCobrar.addActionListener(evt -> cobrarOrden());
        jBTicket.addActionListener(evt -> mostrarTicket(false));
        
        limpiarPantalla();
    }

    private void limpiarPantalla() {
        jLInsumos.setText("");
        jLInsumos.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLNAtendio.setText("-");
        jLTotalCobrar.setText("$0.00");
        totalActual = 0.0;
        ultimoTicketHtml = "";
    }

    private String obtenerMetodoPagoSeleccionado() {
        Object valor = jCBMetodoPago.getSelectedItem();
        return valor != null ? valor.toString() : "Efectivo";
    }

    private void mostrarTicket(boolean pagado) {
        if (jComboBox1.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una mesa primero.");
            return;
        }
        if ((!pagado && totalActual <= 0.0) || ultimoTicketHtml.isBlank()) {
            JOptionPane.showMessageDialog(this, "No hay ticket disponible para mostrar en esta mesa.");
            return;
        }

        String ticketHtml = ultimoTicketHtml.replace("<html>", "<html><div style='padding:10px; font-family:Segoe UI,sans-serif;'>")
            .replace("</html>", "<div style='margin-top:12px; border-top:1px dashed #333; padding-top:8px; font-size:12px; text-align:center; color:#555;'>"
                + (pagado ? "Ticket final generado como muestra" : "Vista previa de ticket - muestra sin impresora")
                + "<br/>Método: " + obtenerMetodoPagoSeleccionado() + "</div></div></html>");

        JEditorPane area = new JEditorPane("text/html", ticketHtml);
        area.setEditable(false);
        area.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(520, 620));

        JOptionPane.showMessageDialog(this, scroll, pagado ? "Ticket final" : "Vista previa de ticket", JOptionPane.PLAIN_MESSAGE);
    }

    private void cargarOrdenCobrar() {
        limpiarPantalla();
        if (jComboBox1.getSelectedIndex() <= 0) return;
        
        String mesaStr = jComboBox1.getSelectedItem().toString();
        int numMesa = obtenerNumeroMesa(mesaStr);
        
        String sqlResumen = "SELECT COALESCE(SUM(o.total_calculado), 0) AS total_bruto, " +
                 "COALESCE(SUM(COALESCE(pagos.total_pagado, 0)), 0) AS total_pagado, " +
                 "COALESCE(SUM(o.total_calculado - COALESCE(pagos.total_pagado, 0)), 0) AS total_pendiente " +
                 "FROM ordenes o " +
                 "LEFT JOIN (SELECT id_orden, SUM(monto_pagado) AS total_pagado FROM pagos GROUP BY id_orden) pagos ON o.id_orden = pagos.id_orden " +
                 "WHERE o.mesa = ? AND o.estado_preparacion = 'Entregada' AND o.estado_pago IN ('Pendiente', 'Parcial')";

        // Consulta para obtener todas las órdenes entregadas que sigan pendientes o parciales en esa mesa
        String sql = "SELECT o.id_orden, o.mesa, e.nombre AS nomEmp, p.nombre AS nomProd, p.precio AS costoP, d.cantidad AS cant, d.notas_especiales AS nota " +
                 "FROM ordenes o " +
                 "INNER JOIN empleados e ON o.id_empleado = e.id_empleado " +
                 "INNER JOIN detalle_orden d ON o.id_orden = d.id_orden " +
                 "INNER JOIN productos p ON d.id_producto = p.id_producto " +
             "WHERE o.mesa = ? AND o.estado_preparacion = 'Entregada' AND o.estado_pago IN ('Pendiente', 'Parcial') " +
             "ORDER BY o.fecha_hora ASC, o.id_orden ASC, d.id_detalle ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstResumen = con.prepareStatement(sqlResumen);
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pstResumen.setInt(1, numMesa);
            try (ResultSet rsResumen = pstResumen.executeQuery()) {
                double totalBruto = 0.0;
                double totalPagado = 0.0;
                double totalPendiente = 0.0;
                if (rsResumen.next()) {
                    totalBruto = rsResumen.getDouble("total_bruto");
                    totalPagado = rsResumen.getDouble("total_pagado");
                    totalPendiente = rsResumen.getDouble("total_pendiente");
                }

                pst.setInt(1, numMesa);
                try (ResultSet rs = pst.executeQuery()) {
                    StringBuilder html = new StringBuilder("<html><div style='padding:15px; font-family: \"Segoe UI\", sans-serif; width: 100%; color: #F0F0F0;'>");
                
                    // Cabecera Interna de la tabla simulada
                    html.append("<div style='border-bottom: 2px solid #555555; padding-bottom: 10px; margin-bottom: 15px; font-size: 22px; color: #cca95a;'>");
                    html.append("Mesa ").append(numMesa);
                    html.append("</div>");
                
                    double total = 0.0;
                    boolean existeOrden = false;
                    String empleado = "-";

                    while (rs.next()) {
                        existeOrden = true;
                        String nombreItem = rs.getString("nomProd") != null ? rs.getString("nomProd") : "Desconocido";
                        double valor = rs.getDouble("costoP");
                        int cant = rs.getInt("cant");
                        String nota = rs.getString("nota");
                        double subtotalLinea = (valor * cant);
                        total += subtotalLinea;
                        empleado = rs.getString("nomEmp") != null ? rs.getString("nomEmp") : "Desconocido";

                        // Fila de item estilo recibo alineado
                        html.append("<div style='border-bottom: 1px solid #3a3a3a; padding-bottom: 8px; margin-bottom: 8px; font-size: 20px; display: flex; justify-content: space-between;'>");
                        // Cantidad y Nombre
                        html.append("<span style='color: #cca95a; font-weight: bold;'>").append(cant).append("x</span> &nbsp;");
                        html.append("<span style='color: #FFFFFF;'>").append(nombreItem).append("</span> &nbsp;&nbsp;--&nbsp;&nbsp; ");
                        // Precios
                        html.append("<span style='color: #999999;'>$").append(String.format("%.2f", valor)).append(" c/u.</span> &nbsp;&nbsp;--&nbsp;&nbsp; ");
                        html.append("<span style='color: #FFFFFF; float: right;'>$").append(String.format("%.2f", subtotalLinea)).append("</span>");
                        html.append("</div>");
                        if (nota != null && !nota.trim().isEmpty()) {
                            html.append("<div style='margin: -4px 0 8px 24px; font-size: 16px; color: #cca95a; font-style: italic;'>Nota: ").append(nota).append("</div>");
                        }
                    }
                    
                    if (existeOrden) {
                        // Generar subtotal ficticio de puro adorno para que se vea completo como en la imagen
                        double tax = total * 0.16; // Suponemos 16% de tax para el adorno
                        double tip = total * 0.15; // Suponemos 15% de tip para adorno
                        double subtotalBase = total - tax - tip;
                        if(subtotalBase < 0) subtotalBase = total;

                        html.append("<div style='text-align: right; margin-top: 25px; font-size: 20px; border-top: 2px solid #555555; padding-top: 10px;'>");
                        html.append("<div style='margin-bottom: 4px; color: #aaaaaa;'>Subtotal: &nbsp;&nbsp;&nbsp; <span style='color: #FFFFFF'>$").append(String.format("%.2f", subtotalBase)).append("</span></div>");
                        html.append("<div style='margin-bottom: 4px; color: #aaaaaa;'>IVA: &nbsp;&nbsp;&nbsp; <span style='color: #FFFFFF'>$").append(String.format("%.2f", tax)).append("</span></div>");
                        html.append("<div style='margin-bottom: 4px; color: #aaaaaa;'>Total cuenta: &nbsp;&nbsp;&nbsp; <span style='color: #FFFFFF'>$").append(String.format("%.2f", totalBruto)).append("</span></div>");
                        html.append("<div style='margin-bottom: 4px; color: #aaaaaa;'>Abonado: &nbsp;&nbsp;&nbsp; <span style='color: #FFFFFF'>$").append(String.format("%.2f", totalPagado)).append("</span></div>");
                        html.append("<div style='color: #cca95a; font-weight: bold; margin-top: 10px; font-size: 24px;'>PENDIENTE: &nbsp;&nbsp;&nbsp; $").append(String.format("%.2f", totalPendiente)).append("</div>");
                        html.append("</div>");
                    }

                    html.append("</div></html>");

                    if (existeOrden) {
                        totalActual = totalPendiente;
                        ultimoTicketHtml = html.toString();
                        jLInsumos.setText(html.toString());
                        jLNAtendio.setText(empleado);
                        jLTotalCobrar.setText("$" + String.format("%.2f", totalPendiente));
                    } else {
                        totalActual = 0.0;
                        ultimoTicketHtml = "";
                        jLInsumos.setText("<html><div style='padding:30px; font-size: 24px; color: #888888; text-align: center;'>No hay orden entregada para cobrar en esta mesa.</div></html>");
                        jLTotalCobrar.setText("$0.00");
                    }
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
        int numMesa = obtenerNumeroMesa(mesaStr);
        String metodoPago = obtenerMetodoPagoSeleccionado();
        
        // Verificar si de verdad hay cuenta que cobrar
        if (totalActual <= 0.0) {
            JOptionPane.showMessageDialog(this, "No hay saldo por cobrar en esta mesa.");
            return;
        }

        if ("Tarjeta".equals(metodoPago)) {
            cobrarConTerminalMercadoPago(numMesa, metodoPago);
            return;
        }

        if (!"Efectivo".equals(metodoPago)) {
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "Por favor, utiliza la terminal para procesar el pago con " + metodoPago + ".\n\nCuando la transacción haya sido aprobada en la terminal,\npresiona 'Sí' para confirmar y guardar el pago en el sistema.",
                "Confirmar transacción en terminal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
        }

        String entrada = JOptionPane.showInputDialog(this,
            "Monto recibido para la mesa " + numMesa + "\n(Saldo pendiente: $" + String.format("%.2f", totalActual) + ")",
            String.format("%.2f", totalActual));

        if (entrada == null) {
            return;
        }

        double montoRecibido;
        try {
            montoRecibido = Double.parseDouble(entrada.trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un monto válido.");
            return;
        }

        if (montoRecibido <= 0.0) {
            JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero.");
            return;
        }

        double cambioCalculado = 0.0;
        double montoAbonado = montoRecibido;

        if ("Efectivo".equals(metodoPago)) {
            if (montoRecibido > totalActual) {
                cambioCalculado = montoRecibido - totalActual;
                montoAbonado = totalActual; // Solo se abona lo que se debe
            }
        } else {
            // Para tarjeta o transferencia no hay cambio en efectivo
            if (montoRecibido > totalActual) {
                montoAbonado = totalActual;
            }
        }

        if (cambioCalculado > 0) {
            JOptionPane.showMessageDialog(this,
                "<html><b style='font-size:16px;'>Pago procesado correctamente.</b><br><br>"
                + "<span style='font-size:20px; color:#27ae60;'>Cambio a regresar: <b>$" + String.format("%.2f", cambioCalculado) + "</b></span></html>",
                "Entregar Cambio",
                JOptionPane.INFORMATION_MESSAGE);
        }

        registrarPagoEnSistema(numMesa, metodoPago, montoRecibido, montoAbonado, cambioCalculado);
    }

    private void cobrarConTerminalMercadoPago(int numMesa, String metodoPago) {
        int idOrdenSeleccionada = obtenerIdOrdenPendiente(numMesa);
        if (idOrdenSeleccionada <= 0) {
            JOptionPane.showMessageDialog(this, "No se encontró una orden pendiente para enviar a la terminal.");
            return;
        }

        double totalCobro = totalActual;
        JDialog dialogCargando = new JDialog(this, "Procesando con Mercado Pago", true);
        JLabel lblMensaje = new JLabel("Esperando pago en la terminal física...", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 14));
        dialogCargando.add(lblMensaje, BorderLayout.CENTER);
        dialogCargando.setSize(380, 150);
        dialogCargando.setLocationRelativeTo(this);
        dialogCargando.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        MercadoPagoAPI api = new MercadoPagoAPI();

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return api.procesarCobroEnTerminal(totalCobro, idOrdenSeleccionada);
            }

            @Override
            protected void done() {
                dialogCargando.dispose();
                try {
                    boolean pagoExitoso = get();
                    if (pagoExitoso) {
                        registrarPagoEnSistema(numMesa, metodoPago, totalCobro, totalCobro, 0.0);
                    } else {
                        String mensaje = api.getUltimoErrorUsuario();
                        if (mensaje == null || mensaje.isBlank()) {
                            mensaje = "El cobro fue cancelado, rechazado o expiró en la terminal.";
                        }
                        JOptionPane.showMessageDialog(PSCobOrden.this,
                            mensaje,
                            "Atención",
                            JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    logger.log(java.util.logging.Level.SEVERE, "Error al obtener respuesta de la terminal", ex);
                    JOptionPane.showMessageDialog(PSCobOrden.this,
                        "Error interno al procesar el pago: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        dialogCargando.setVisible(true);
    }

    private int obtenerIdOrdenPendiente(int numMesa) {
        String sql = "SELECT o.id_orden FROM ordenes o " +
                 "WHERE o.mesa = ? AND o.estado_preparacion = 'Entregada' AND o.estado_pago IN ('Pendiente', 'Parcial') " +
                 "ORDER BY o.fecha_hora ASC, o.id_orden ASC LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, numMesa);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_orden");
                }
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al obtener la orden pendiente para terminal", ex);
        }
        return -1;
    }

    private void registrarPagoEnSistema(int numMesa, String metodoPago, double montoRecibido, double montoAbonado, double cambioCalculado) {

        String sqlOrdenes = "SELECT o.id_orden, o.total_calculado, COALESCE(pagos.total_pagado, 0) AS total_pagado " +
                 "FROM ordenes o " +
                 "LEFT JOIN (SELECT id_orden, SUM(monto_pagado) AS total_pagado FROM pagos GROUP BY id_orden) pagos ON o.id_orden = pagos.id_orden " +
                 "WHERE o.mesa = ? AND o.estado_preparacion = 'Entregada' AND o.estado_pago IN ('Pendiente', 'Parcial') " +
                 "ORDER BY o.fecha_hora ASC, o.id_orden ASC";
        String sqlPago = "INSERT INTO pagos (id_orden, metodo_pago, monto_pagado, monto_recibido, cambio) VALUES (?, ?, ?, ?, ?)";
        String sqlOrden = "UPDATE ordenes SET estado_pago = ? WHERE id_orden = ?";
        double saldoRestante = montoAbonado;
        boolean primerPagoRegistrado = false;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement pstOrdenes = con.prepareStatement(sqlOrdenes);
                 PreparedStatement pstPago = con.prepareStatement(sqlPago);
                 PreparedStatement pstOrden = con.prepareStatement(sqlOrden)) {

                pstOrdenes.setInt(1, numMesa);
                try (ResultSet rsOrdenes = pstOrdenes.executeQuery()) {
                    boolean hayOrdenes = false;
                    while (rsOrdenes.next()) {
                        hayOrdenes = true;
                        int idOrden = rsOrdenes.getInt("id_orden");
                        double totalOrden = rsOrdenes.getDouble("total_calculado");
                        double totalPagadoAnterior = rsOrdenes.getDouble("total_pagado");
                        double restanteOrden = Math.max(totalOrden - totalPagadoAnterior, 0.0);

                        if (saldoRestante <= 0.0) {
                            break;
                        }

                        double aplicado = Math.min(restanteOrden, saldoRestante);
                        double restanteDespues = Math.max(restanteOrden - aplicado, 0.0);

                        pstPago.setInt(1, idOrden);
                        pstPago.setString(2, metodoPago);
                        pstPago.setDouble(3, aplicado);
                        
                        // Guardar el monto recibido real y el cambio solo en el primer registro de pago de esta transacción para no duplicarlo en BD
                        if (!primerPagoRegistrado) {
                            pstPago.setDouble(4, montoRecibido);
                            pstPago.setDouble(5, cambioCalculado);
                            primerPagoRegistrado = true;
                        } else {
                            pstPago.setDouble(4, aplicado);
                            pstPago.setDouble(5, 0.0);
                        }
                        
                        pstPago.addBatch();

                        pstOrden.setString(1, restanteDespues <= 0.0 ? "Pagado" : "Parcial");
                        pstOrden.setInt(2, idOrden);
                        pstOrden.addBatch();

                        saldoRestante -= aplicado;
                    }

                    if (!hayOrdenes) {
                        JOptionPane.showMessageDialog(this, "No hay saldo por cobrar en esta mesa.");
                        con.rollback();
                        return;
                    }
                }

                pstPago.executeBatch();
                pstOrden.executeBatch();
                con.commit();
                boolean pagoCompleto = saldoRestante <= 0.0 && montoAbonado >= totalActual;
                ultimoMetodoPago = metodoPago;
                if (pagoCompleto) {
                    JOptionPane.showMessageDialog(this, "La mesa quedó liquidada con " + metodoPago + ". Ticket completo generado como muestra.");
                    mostrarTicket(true);
                    cargarOrdenCobrar();
                } else {
                    cargarOrdenCobrar();
                    JOptionPane.showMessageDialog(this, "Se registró un abono de $" + String.format("%.2f", montoAbonado) + " con " + ultimoMetodoPago + ". Falta por pagar: $" + String.format("%.2f", totalActual));
                }
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al procesar el cobro", ex);
        }
    }

    private int obtenerNumeroMesa(String mesaStr) {
        try {
            return Integer.parseInt(mesaStr.replace("Mesa ", "").trim());
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPPrincipal = new javax.swing.JPanel();
        jLNVentana = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLInsumos = new javax.swing.JLabel();
        jLAtendio = new javax.swing.JLabel();
        jLMetodoPago = new javax.swing.JLabel();
        jLNAtendio = new javax.swing.JLabel();
        jLTFacturacion = new javax.swing.JLabel();
        jBCobrar = new javax.swing.JButton();
        jCBMetodoPago = new javax.swing.JComboBox<>();
        jBTicket = new javax.swing.JButton();
        jLTTotal = new javax.swing.JLabel();
        jLTotalCobrar = new javax.swing.JLabel();
        jBRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLNVentana.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLNVentana.setForeground(new java.awt.Color(255, 255, 255));
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLNVentana.setText("COBRO DE MESA");

        jLInsumos.setForeground(new java.awt.Color(255, 255, 255));
        jLInsumos.setText("-");

        jLAtendio.setForeground(new java.awt.Color(255, 255, 255));
        jLAtendio.setText("ATENDIO: ");

        jLMetodoPago.setForeground(new java.awt.Color(255, 255, 255));
        jLMetodoPago.setText("MÉTODO: ");

        jCBMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Tarjeta", "Transferencia" }));

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
        java.awt.EventQueue.invokeLater(() -> new PSCobOrden().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCobrar;
    private javax.swing.JButton jBRegresar;
    private javax.swing.JButton jBTicket;
    private javax.swing.JComboBox<String> jCBMetodoPago;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLAtendio;
    private javax.swing.JLabel jLInsumos;
    private javax.swing.JLabel jLMetodoPago;
    private javax.swing.JLabel jLNAtendio;
    private javax.swing.JLabel jLNVentana;
    private javax.swing.JLabel jLTFacturacion;
    private javax.swing.JLabel jLTTotal;
    private javax.swing.JLabel jLTotalCobrar;
    private javax.swing.JPanel jPPrincipal;
    // End of variables declaration//GEN-END:variables
}
