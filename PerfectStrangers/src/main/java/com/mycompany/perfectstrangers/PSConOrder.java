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
import java.util.Comparator;
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
        
        // Reset background so FlatLaf can apply its dark theme completely
        jBEntregarOrden.setBackground(null);
        jBRegresar.setBackground(null);
        
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
        // Rediseño Visual - "ORDENES A PREPARAR" Neón y Metal
        java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);
        java.awt.Color casiNegro = new java.awt.Color(25, 25, 25);
        java.awt.Color metal = new java.awt.Color(45, 45, 47);

        jPPrincipal.removeAll();
        jPPrincipal.setLayout(new java.awt.BorderLayout());
        
        // --- FONDO GENERAL (Tipo Plancha Acero) ---
        javax.swing.JPanel panelFondoAcero = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo oscuro
                g2.setColor(new java.awt.Color(15, 12, 10));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Viñeta
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
                
                // Marco exterior de acero
                g2.setStroke(new java.awt.BasicStroke(20f));
                g2.setColor(metal);
                g2.drawRect(10, 10, getWidth()-20, getHeight()-20);
                
                g2.dispose();
            }
        };
        panelFondoAcero.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // --- CAJA CENTRAL (Pantalla de pedidos) ---
        javax.swing.JPanel panelPantalla = new javax.swing.JPanel(new java.awt.BorderLayout(0, 20)) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setColor(new java.awt.Color(15, 15, 15)); // Negro puro casi pantalla
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.setColor(tonoOro);
                g2.drawRect(1, 1, getWidth()-2, getHeight()-2);
                g2.dispose();
            }
        };
        panelPantalla.setOpaque(false);
        panelPantalla.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Título "ORDENES A PREPARAR" (Color Plata/Metálico)
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
        jLabel1.setForeground(new java.awt.Color(180, 180, 190));
        jLabel1.setText("ORDENES A PREPARAR");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelPantalla.add(jLabel1, java.awt.BorderLayout.NORTH);

        // --- ZONA DE LAS TARJETAS (Tickets) ---
        javax.swing.JPanel panelTarjetas = new javax.swing.JPanel(new java.awt.GridLayout(1, 3, 30, 0));
        panelTarjetas.setOpaque(false);

        // jLOrdenaEntregar, jLSegundaOrden, jLTercerOrden ya son JLabel pero los usaremos para pintar
        jLOrdenaEntregar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLSegundaOrden.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTercerOrden.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        panelTarjetas.add(jLOrdenaEntregar);
        panelTarjetas.add(jLSegundaOrden);
        panelTarjetas.add(jLTercerOrden);

        panelPantalla.add(panelTarjetas, java.awt.BorderLayout.CENTER);

        // --- ZONA INFERIOR (Botones y status de la fila) ---
        javax.swing.JPanel panelInferior = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelInferior.setOpaque(false);

        // Botón ENTREGAR (Izquierda debajo de la primera tarjeta)
        jBEntregarOrden.setText("ENTREGAR");
        jBEntregarOrden.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jBEntregarOrden.setForeground(tonoOro);
        jBEntregarOrden.setBackground(new java.awt.Color(40, 40, 45));
        jBEntregarOrden.setFocusPainted(false);
        jBEntregarOrden.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 3),
            javax.swing.BorderFactory.createEmptyBorder(12, 60, 12, 60)
        ));
        javax.swing.JPanel pEntregar = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        pEntregar.setOpaque(false);
        pEntregar.add(jBEntregarOrden);

        // Información "Órdenes por delante" (Centro)
        javax.swing.JPanel pCentroInfo = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));
        pCentroInfo.setOpaque(false);
        jLOrdenes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 20));
        jLOrdenes.setForeground(new java.awt.Color(220, 220, 220));
        jLOrdenes.setText("≡ Ordenes por delante: ");
        jLCOrdenes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        jLCOrdenes.setForeground(tonoOro);
        pCentroInfo.add(jLOrdenes);
        pCentroInfo.add(jLCOrdenes);

        // Botón REGRESAR (Derecha)
        jBRegresar.setText("REGRESAR");
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setBackground(new java.awt.Color(40, 40, 45));
        jBRegresar.setFocusPainted(false);
        jBRegresar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 3),
            javax.swing.BorderFactory.createEmptyBorder(10, 40, 10, 40)
        ));
        javax.swing.JPanel pRegresar = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));
        pRegresar.setOpaque(false);
        pRegresar.add(jBRegresar);

        panelInferior.add(pEntregar, java.awt.BorderLayout.WEST);
        panelInferior.add(pCentroInfo, java.awt.BorderLayout.CENTER);
        panelInferior.add(pRegresar, java.awt.BorderLayout.EAST);

        panelPantalla.add(panelInferior, java.awt.BorderLayout.SOUTH);

        // Ensamblar
        panelFondoAcero.add(panelPantalla, java.awt.BorderLayout.CENTER);
        jPPrincipal.add(panelFondoAcero, java.awt.BorderLayout.CENTER);

        jPPrincipal.revalidate();
        jPPrincipal.repaint();
    }

    private class ItemOrden {
        String nombre;
        int cant;
        String nota;
        public ItemOrden(String n, int c, String nota) { nombre = n; cant = c; this.nota = nota; }
    }
    
    private class OrdenPendiente {
        int idOrden;
        String mesa;
        java.sql.Timestamp fechaHora;
        Long idTicketTemporal;
        List<ItemOrden> items = new ArrayList<>();
    }

    private List<OrdenPendiente> listaOrdenes = new ArrayList<>();

    private void cargarOrdenes() {
        listaOrdenes.clear();
        java.util.Set<Integer> ordenesConTicketTemporal = new java.util.HashSet<>();

        for (CocinaTicketTemporalService.TicketCocina ticket : CocinaTicketTemporalService.obtenerTicketsPendientes()) {
            OrdenPendiente op = new OrdenPendiente();
            op.idOrden = ticket.getIdOrden();
            op.mesa = String.valueOf(ticket.getMesa());
            op.fechaHora = ticket.getFechaHora();
            op.idTicketTemporal = ticket.getIdTicket();

            for (CocinaTicketTemporalService.TicketItem item : ticket.getItems()) {
                op.items.add(new ItemOrden(item.getNombreProducto(), item.getCantidad(), item.getNotas()));
            }

            listaOrdenes.add(op);
            ordenesConTicketTemporal.add(op.idOrden);
        }

        String sql = "SELECT o.id_orden, o.mesa, o.fecha_hora, p.nombre AS nomProd, d.cantidad AS cant, d.notas_especiales AS nota " +
                 "FROM ordenes o " +
                 "INNER JOIN detalle_orden d ON o.id_orden = d.id_orden " +
                 "INNER JOIN productos p ON d.id_producto = p.id_producto " +
                 "WHERE o.estado_preparacion IN ('Pendiente', 'En Preparacion') " +
                 "ORDER BY o.fecha_hora ASC, o.id_orden ASC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            Map<String, OrdenPendiente> mapa = new LinkedHashMap<>();

            while (rs.next()) {
                int idOrden = rs.getInt("id_orden");

                if (ordenesConTicketTemporal.contains(idOrden)) {
                    continue;
                }

                String mesa = rs.getString("mesa");
                java.sql.Timestamp fechaHora = rs.getTimestamp("fecha_hora");
                String nombreItem = rs.getString("nomProd") != null ? rs.getString("nomProd") : "Desconocido";
                int cant = rs.getInt("cant");
                String nota = rs.getString("nota");

                String key = String.valueOf(idOrden);
                OrdenPendiente op = mapa.get(key);
                if (op == null) {
                    op = new OrdenPendiente();
                    op.idOrden = idOrden;
                    op.mesa = mesa;
                    op.fechaHora = fechaHora;
                    mapa.put(key, op);
                    listaOrdenes.add(op);
                }
                op.items.add(new ItemOrden(nombreItem, cant, nota));
            }

                listaOrdenes.sort(Comparator.comparing(o -> o.fechaHora, Comparator.nullsLast(Comparator.naturalOrder())));
            mostrarOrdenes();

        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar ordenes", ex);
        }
    }

    private void aplicarEstiloTarjeta(javax.swing.JLabel label, String html, Color colorBordeNeon) {
        label.setText(""); // Limpiar texto original
        label.setOpaque(false);
        
        label.removeAll(); // Remover cualquier JLabel interno previo
        label.setLayout(new java.awt.BorderLayout());
        
        javax.swing.JLabel labelInterno = new javax.swing.JLabel(html);
        labelInterno.setOpaque(false);
        labelInterno.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelInterno.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelInterno.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        label.add(labelInterno, java.awt.BorderLayout.CENTER);
        
        label.setBorder(new javax.swing.border.Border() {
            @Override
            public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo interior (Oscuro carbón)
                g2.setColor(new java.awt.Color(25, 25, 28));
                g2.fillRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
                
                // Borde Neón Resplandeciente
                g2.setStroke(new java.awt.BasicStroke(4f));
                g2.setColor(new Color(colorBordeNeon.getRed(), colorBordeNeon.getGreen(), colorBordeNeon.getBlue(), 80)); // Sombra neón suave
                g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
                
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.setColor(colorBordeNeon); // Borde duro neón
                g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

                g2.dispose();
            }

            @Override
            public java.awt.Insets getBorderInsets(java.awt.Component c) {
                return new java.awt.Insets(5, 5, 5, 5); 
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });
        
        label.revalidate();
        label.repaint();
    }

    private void mostrarOrdenes() {
        String msgVacio = "<html><div style='text-align:center; padding-top:40px; font-size:24px; color:#555555;'>Sin órdenes pendientes</div></html>";
        
        aplicarEstiloTarjeta(jLOrdenaEntregar, msgVacio, new Color(50, 50, 50)); 
        jBEntregarOrden.setEnabled(false);
        // Borrar hover effects default si estaba inactivo
        jBEntregarOrden.setForeground(new java.awt.Color(100, 100, 100));

        aplicarEstiloTarjeta(jLSegundaOrden, msgVacio, new Color(50, 50, 50));
        aplicarEstiloTarjeta(jLTercerOrden, msgVacio, new Color(50, 50, 50));

        jLCOrdenes.setText("0");

        // Color verde fuerte neón de la Activa
        Color colActiva = new Color(0, 255, 60);
        // Color amarillo intenso neón en Espera
        Color colEspera = new Color(255, 150, 0);

        if (listaOrdenes.size() > 0) {
            OrdenPendiente o1 = listaOrdenes.get(0);
            aplicarEstiloTarjeta(jLOrdenaEntregar, formatHTML(o1, true), colActiva);
            jBEntregarOrden.setEnabled(true);
            jBEntregarOrden.setForeground(new java.awt.Color(204, 169, 90)); // Oro si esta activa
        }
        if (listaOrdenes.size() > 1) {
            OrdenPendiente o2 = listaOrdenes.get(1);
            aplicarEstiloTarjeta(jLSegundaOrden, formatHTML(o2, false), colEspera);
        }
        if (listaOrdenes.size() > 2) {
            OrdenPendiente o3 = listaOrdenes.get(2);
            aplicarEstiloTarjeta(jLTercerOrden, formatHTML(o3, false), colEspera);
        }

        int faltantes = listaOrdenes.size() > 3 ? listaOrdenes.size() - 3 : 0;
        jLCOrdenes.setText(String.valueOf(faltantes));
    }

    private String formatHTML(OrdenPendiente o, boolean esPrimera) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><div style='font-family: \"Segoe UI\", sans-serif; text-align: left; width: 100%; padding: 5px;'>");
        
        // Cabecera "Mesa X" con los íconos
        String colorAcento = esPrimera ? "#00ff3c" : "#ff9600";
        sb.append("<div style='border-bottom: 1px solid #4a4a4a; padding-bottom: 15px; margin-bottom: 20px; font-size: 26px; color: #e6e6dc; display: flex; justify-content: space-between;'>");
        sb.append("<span style='font-weight: normal;'>Mesa: <span style='font-weight: 300;'>").append(o.mesa).append("</span></span>");
        // Emulamos íconos de hamburguesa/vaso como en el diseño (pueden ser emojis standard de comida)
        sb.append("<span style='float:right; color:").append(colorAcento).append(";'>&#127828;&#129380;</span>");
        sb.append("</div>");
        
        // Contenido de la lista de platillos
        sb.append("<div style='margin-bottom: 30px;'>");
        for (ItemOrden item : o.items) {
            sb.append("<div style='font-size: 18px; color: #e6e6dc; margin-bottom: 10px; display: flex; align-items: center;'>");
            // Icono sutil
            sb.append("<span style='color: #888888; font-size: 22px; margin-right: 15px;'>&#9676;</span>");
            sb.append("<span style='padding-left: 10px;'>").append(item.cant).append("x ").append(item.nombre).append("</span>");
            sb.append("</div>");
            if (item.nota != null && !item.nota.trim().isEmpty()) {
                sb.append("<div style='font-size: 16px; color: #cca95a; margin: -4px 0 14px 38px; font-style: italic;'>Nota: ").append(item.nota).append("</div>");
            }
        }
        sb.append("</div>");
        
        // Footers de status
        sb.append("<div style='border-top: 1px solid #4a4a4a; padding-top: 15px;'>");
        
        // Estado
        sb.append("<div style='font-size: 20px; color: #aaaaaa; margin-bottom: 8px;'>");
        sb.append("&#9888; &nbsp;&nbsp;Estado: ");
        sb.append("<span style='color: ").append(colorAcento).append(";'>").append(esPrimera ? "Preparar" : "En Espera").append("</span>");
        sb.append("</div>");
        
        // Tiempo
        sb.append("<div style='font-size: 20px; color: #aaaaaa;'>");
        sb.append("&#9201; &nbsp;&nbsp;Tiempo: ");
        String tiempo = o.fechaHora != null ? o.fechaHora.toString().substring(11, 19) : "--:--:--";
        sb.append("<span style='color: ").append(colorAcento).append(";'>").append(esPrimera ? tiempo : "--:--:--").append("</span>");
        sb.append("</div>");
        
        sb.append("</div>");
        
        sb.append("</div></html>");
        return sb.toString();
    }

    private void entregarOrdenPrimera() {
        if (listaOrdenes.isEmpty()) return;
        OrdenPendiente o = listaOrdenes.get(0);
        try {
            if (o.idTicketTemporal != null) {
                CocinaTicketTemporalService.consumirTicket(o.idTicketTemporal);
            } else {
                ServicioOrden.entregarOrden(o.idOrden);
            }
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
