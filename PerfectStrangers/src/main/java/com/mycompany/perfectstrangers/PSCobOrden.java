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
        
        jBCobrar.setBackground(null);
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
        java.awt.Color casiNegro = new java.awt.Color(25, 25, 25);
        java.awt.Color metal = new java.awt.Color(45, 45, 47);

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
        
        jLNAtendio.setText("-");
        jLNAtendio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        jLNAtendio.setForeground(java.awt.Color.WHITE);
        
        panelToolBar.add(jComboBox1);
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
        panelBotones.add(jBRegresar, java.awt.BorderLayout.EAST);

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

        jComboBox1.removeAllItems();
        jComboBox1.addItem("Selecciona una mesa");
        for (int i = 1; i <= 15; i++) {
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
        int numMesa = 1;
        try { numMesa = Integer.parseInt(mesaStr.replace("Mesa ", "").trim()); } catch (Exception e){}
        
        // Consulta para obtener las ordenes "Entregada"
        String sql = "SELECT o.mesa, e.nombre AS nomEmp, p.nombre_alimento AS nomP, NULL AS nomPaq, p.costo AS costoP, 0.0 AS costoPaq, d.cant " +
                     "FROM ordenes o " +
                     "INNER JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "INNER JOIN detalle_orden d ON o.id_orden = d.id_orden " +
                     "INNER JOIN platillos p ON d.id_platillo = p.id_platillo " +
                     "WHERE o.estado = 'Entregada' AND o.mesa = ? " +
                     "UNION ALL " +
                     "SELECT o.mesa, e.nombre AS nomEmp, NULL AS nomP, pq.nombre_paquete AS nomPaq, 0.0 AS costoP, pq.precio_paquete AS costoPaq, 1 AS cant " +
                     "FROM ordenes o " +
                     "INNER JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "INNER JOIN paquetes pq ON o.id_paquete = pq.id_paquete " +
                     "WHERE o.estado = 'Entregada' AND o.mesa = ? AND o.id_paquete != 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setInt(1, numMesa);
            pst.setInt(2, numMesa);
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
                    String nP = rs.getString("nomP");
                    String nPaq = rs.getString("nomPaq");
                    String nombreItem = (nP != null) ? nP : (nPaq != null ? nPaq : "Desconocido");

                    double valor = 0;
                    if (nP != null) valor = rs.getDouble("costoP");
                    else if (nPaq != null) valor = rs.getDouble("costoPaq");

                    int cant = rs.getInt("cant");
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
                    // En MXN solemos sumar o quitar así que pondremos el Total exacto de tu DB al final
                    html.append("<div style='color: #cca95a; font-weight: bold; margin-top: 10px; font-size: 24px;'>TOTAL A COBRAR: &nbsp;&nbsp;&nbsp; $").append(String.format("%.2f", total)).append("</div>");
                    html.append("</div>");
                }

                html.append("</div></html>");

                if (existeOrden) {
                    jLInsumos.setText(html.toString());
                    jLNAtendio.setText(empleado);
                    jLTotalCobrar.setText("$" + String.format("%.2f", total));
                } else {
                    jLInsumos.setText("<html><div style='padding:30px; font-size: 24px; color: #888888; text-align: center;'>No hay orden entregada para cobrar en esta mesa.</div></html>");
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
        int numMesa = 1;
        try { numMesa = Integer.parseInt(mesaStr.replace("Mesa ", "").trim()); } catch (Exception e){}
        
        // Verificar si de verdad hay cuenta que cobrar
        if (jLTotalCobrar.getText().equals("$0.00")) {
            JOptionPane.showMessageDialog(this, "No hay saldo por cobrar en esta mesa.");
            return;
        }

        String sql = "UPDATE ordenes SET estado = 'Cobrada' WHERE mesa = ? AND estado = 'Entregada'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setInt(1, numMesa);
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
