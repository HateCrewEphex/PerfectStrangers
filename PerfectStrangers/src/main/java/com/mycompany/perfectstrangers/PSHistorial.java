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
        
        jLNVentana.setText("HISTORIAL DE PAGOS");
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

        jBImprimirTicket.setText("VER DETALLES DE PAGO");
        jBImprimirTicket.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jBImprimirTicket.setPreferredSize(new java.awt.Dimension(250, 50));
        jBImprimirTicket.setBackground(new java.awt.Color(44, 44, 48));
        jBImprimirTicket.setForeground(tonoOro);
        jBImprimirTicket.setFocusPainted(false);
        jBImprimirTicket.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        for (java.awt.event.ActionListener al : jBImprimirTicket.getActionListeners()) jBImprimirTicket.removeActionListener(al);
        jBImprimirTicket.addActionListener(evt -> verDetallesSeleccionado());
        
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
        for (java.awt.event.ActionListener al : jCFiltro.getActionListeners()) jCFiltro.removeActionListener(al);
        jCFiltro.addActionListener(e -> cargarHistorial());
        cargarHistorial();
        
        // Agregar doble clic a la tabla
        jTHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    verDetallesSeleccionado();
                }
            }
        });
    }

    private void cargarFiltros() {
        jCFiltro.removeAllItems();
        jCFiltro.addItem("Mostrar Todo");
        jCFiltro.addItem("Dia de Hoy");
        jCFiltro.addItem("Esta Semana");
        jCFiltro.addItem("Este Mes");

        String sql = "SELECT DISTINCT e.nombre FROM pagos p JOIN ordenes o ON p.id_orden = o.id_orden JOIN empleados e ON o.id_empleado = e.id_empleado";
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
            new Object[]{"ID Pago", "ID Orden", "Mesa", "Mesero", "Método", "Monto Abonado", "Cambio", "Fecha", "Hora"}, 0
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
        
        jTHistorial.getColumnModel().getColumn(0).setPreferredWidth(60);
        jTHistorial.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTHistorial.getColumnModel().getColumn(2).setPreferredWidth(70);

        String sql = "SELECT p.id_pago, o.id_orden, o.mesa, CONCAT(e.nombre, ' ', e.ap_paterno, IFNULL(CONCAT(' ', e.ap_materno), '')) AS nombre_mesero, " +
             "p.metodo_pago, p.monto_pagado, p.cambio, DATE(p.fecha_hora_pago) AS fecha, TIME(p.fecha_hora_pago) AS hora " +
                 "FROM pagos p " +
                 "JOIN ordenes o ON p.id_orden = o.id_orden " +
                 "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                 "WHERE 1=1 ";
                     
        if (seleccion.equals("Dia de Hoy")) {
            sql += "AND DATE(p.fecha_hora_pago) = CURDATE() ";
        } else if (seleccion.equals("Esta Semana")) {
            sql += "AND YEARWEEK(p.fecha_hora_pago, 1) = YEARWEEK(CURDATE(), 1) ";
        } else if (seleccion.equals("Este Mes")) {
            sql += "AND MONTH(p.fecha_hora_pago) = MONTH(CURDATE()) AND YEAR(p.fecha_hora_pago) = YEAR(CURDATE()) ";
        } else if (seleccion.startsWith("Mesero: ")) {
            sql += "AND CONCAT(e.nombre, ' ', e.ap_paterno, IFNULL(CONCAT(' ', e.ap_materno), '')) = ? ";
        }

        sql += "ORDER BY p.fecha_hora_pago DESC";

        double granTotal = 0.0;
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            if (seleccion.startsWith("Mesero: ")) {
                 pst.setString(1, seleccion.substring(8));
            }

            try (ResultSet rs = pst.executeQuery()) {
                while(rs.next()) {
                    int idPago = rs.getInt("id_pago");
                    int idOrden = rs.getInt("id_orden");
                    String mesa = rs.getString("mesa");
                    String empleado = rs.getString("nombre_mesero");
                    String metodo = rs.getString("metodo_pago");
                    double monto = rs.getDouble("monto_pagado");
                    double cambio = rs.getDouble("cambio");
                    java.sql.Date fecha = rs.getDate("fecha");
                    java.sql.Time hora = rs.getTime("hora");
                    
                    granTotal += monto;
                    
                    modelo.addRow(new Object[]{
                        idPago,
                        idOrden,
                        mesa, 
                        empleado != null ? empleado : "N/A", 
                        metodo,
                        String.format("$%.2f", monto),
                        String.format("$%.2f", cambio),
                        fecha, 
                        hora
                    });
                }
            }
            
            // Fila vacía de separación
            modelo.addRow(new Object[]{"", "", "", "", "", "", "", "", ""});
            // Fila de TOTAL
            modelo.addRow(new Object[]{"", "", "", "", "TOTAL PAGOS:", String.format("$%.2f", granTotal), "", "", ""});
            
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar historial", ex);
        }
    }

    private void verDetallesSeleccionado() {
        int row = jTHistorial.getSelectedRow();
        if (row < 0 || jTHistorial.getValueAt(row, 1) == null || jTHistorial.getValueAt(row, 1).toString().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un pago de la tabla para ver sus detalles.");
            return;
        }
        
        try {
            int idPago = Integer.parseInt(jTHistorial.getValueAt(row, 0).toString());
            int idOrden = Integer.parseInt(jTHistorial.getValueAt(row, 1).toString());
            mostrarDetalleTicket(idOrden, idPago);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona una fila válida de pago.");
        }
    }

    private void mostrarDetalleTicket(int idOrden, int idPago) {
        String sqlPago = "SELECT metodo_pago, monto_pagado, monto_recibido, cambio, fecha_hora_pago FROM pagos WHERE id_pago = ?";
        String sqlOrden = "SELECT o.mesa, e.nombre AS nomEmp, p.nombre AS nomProd, p.precio AS costoP, d.cantidad AS cant, d.notas_especiales AS nota, o.total_calculado " +
                 "FROM ordenes o " +
                 "INNER JOIN empleados e ON o.id_empleado = e.id_empleado " +
                 "INNER JOIN detalle_orden d ON o.id_orden = d.id_orden " +
                 "INNER JOIN productos p ON d.id_producto = p.id_producto " +
             "WHERE o.id_orden = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstPago = con.prepareStatement(sqlPago);
             PreparedStatement pstOrden = con.prepareStatement(sqlOrden)) {
             
            pstPago.setInt(1, idPago);
            String metodo = "";
            double montoPagado = 0, montoRecibido = 0, cambio = 0;
            String fechaHora = "";
            try (ResultSet rsPago = pstPago.executeQuery()) {
                if (rsPago.next()) {
                    metodo = rsPago.getString("metodo_pago");
                    montoPagado = rsPago.getDouble("monto_pagado");
                    montoRecibido = rsPago.getDouble("monto_recibido");
                    cambio = rsPago.getDouble("cambio");
                    fechaHora = rsPago.getString("fecha_hora_pago");
                }
            }

            pstOrden.setInt(1, idOrden);
            try (ResultSet rs = pstOrden.executeQuery()) {
                StringBuilder html = new StringBuilder("<html><div style='padding:15px; font-family: \"Segoe UI\", sans-serif; width: 100%; color: #333333; background-color: #FFFFFF;'>");
            
                html.append("<div style='border-bottom: 2px dashed #555555; padding-bottom: 10px; margin-bottom: 15px; font-size: 22px; color: #000; text-align: center; font-weight: bold;'>");
                html.append("TICKET DE PAGO<br><span style='font-size:16px; font-weight: normal;'>ID Pago: ").append(idPago).append(" | Orden: ").append(idOrden).append("</span>");
                html.append("<br><span style='font-size:14px; font-weight: normal;'>").append(fechaHora).append("</span>");
                html.append("</div>");
            
                double total = 0.0;
                boolean existeOrden = false;
                String empleado = "-";
                String mesa = "-";
                double totalCalculado = 0;

                while (rs.next()) {
                    existeOrden = true;
                    mesa = rs.getString("mesa");
                    String nombreItem = rs.getString("nomProd") != null ? rs.getString("nomProd") : "Desconocido";
                    double valor = rs.getDouble("costoP");
                    int cant = rs.getInt("cant");
                    String nota = rs.getString("nota");
                    totalCalculado = rs.getDouble("total_calculado");
                    double subtotalLinea = (valor * cant);
                    total += subtotalLinea;
                    empleado = rs.getString("nomEmp") != null ? rs.getString("nomEmp") : "Desconocido";

                    html.append("<div style='border-bottom: 1px solid #EEEEEE; padding-bottom: 8px; margin-bottom: 8px; font-size: 16px; display: flex; justify-content: space-between;'>");
                    html.append("<span style='font-weight: bold;'>").append(cant).append("x</span> &nbsp;");
                    html.append("<span>").append(nombreItem).append("</span> &nbsp;&nbsp;--&nbsp;&nbsp; ");
                    html.append("<span style='color: #666666;'>$").append(String.format("%.2f", valor)).append(" c/u.</span> &nbsp;&nbsp;--&nbsp;&nbsp; ");
                    html.append("<span style='float: right; font-weight: bold;'>$").append(String.format("%.2f", subtotalLinea)).append("</span>");
                    html.append("</div>");
                    if (nota != null && !nota.trim().isEmpty()) {
                        html.append("<div style='margin: -4px 0 8px 24px; font-size: 14px; color: #555; font-style: italic;'>Nota: ").append(nota).append("</div>");
                    }
                }

                if (!existeOrden) {
                     javax.swing.JOptionPane.showMessageDialog(this, "No se encontraron detalles de productos para esta orden.");
                     return;
                }

                html.append("<div style='text-align: right; margin-top: 25px; font-size: 18px; border-top: 2px dashed #555555; padding-top: 10px;'>");
                html.append("<div style='margin-bottom: 4px;'>Mesa: &nbsp;&nbsp;&nbsp; <b>").append(mesa).append("</b></div>");
                html.append("<div style='margin-bottom: 4px;'>Mesero: &nbsp;&nbsp;&nbsp; <b>").append(empleado).append("</b></div>");
                html.append("<div style='margin-bottom: 4px; color: #555;'>Total de la Orden General: &nbsp;&nbsp;&nbsp; $").append(String.format("%.2f", totalCalculado)).append("</div>");
                
                html.append("<div style='margin-top: 10px; border-top: 1px solid #EEEEEE; padding-top: 10px;'></div>");
                html.append("<div style='margin-bottom: 4px;'>Método de Pago: &nbsp;&nbsp;&nbsp; <b>").append(metodo).append("</b></div>");
                html.append("<div style='margin-bottom: 4px;'>Monto Entregado: &nbsp;&nbsp;&nbsp; <b>$").append(String.format("%.2f", montoRecibido)).append("</b></div>");
                html.append("<div style='margin-bottom: 4px;'>Monto Abonado (Cobrado): &nbsp;&nbsp;&nbsp; <b>$").append(String.format("%.2f", montoPagado)).append("</b></div>");
                if(cambio > 0) {
                    html.append("<div style='margin-bottom: 4px; color: #27ae60; font-weight: bold;'>Cambio Regresado: &nbsp;&nbsp;&nbsp; $").append(String.format("%.2f", cambio)).append("</div>");
                }
                
                html.append("</div></div></html>");

                javax.swing.JEditorPane area = new javax.swing.JEditorPane("text/html", html.toString());
                area.setEditable(false);
                area.setCaretPosition(0);
                javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
                scroll.setPreferredSize(new java.awt.Dimension(450, 600));

                javax.swing.JOptionPane.showMessageDialog(this, scroll, "Detalle de Pago y Orden", javax.swing.JOptionPane.PLAIN_MESSAGE);
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar detalle de ticket", ex);
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
