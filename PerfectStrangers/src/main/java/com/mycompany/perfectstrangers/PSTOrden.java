/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ephex
 */
public class PSTOrden extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSTOrden.class.getName());

    private JPanel panelProductos;
    private List<ProductoItem> productosActuales;
    private DefaultTableModel modeloOrden;
    private JTable tablaOrden;

    class ProductoItem {
        int id;
        String nombre;
        double precio;
        boolean esCombo;
        JCheckBox checkBox;
        JSpinner spinnerCantidad;
        javax.swing.JTextField notaEspecial;
        JPanel panel;
        
        public ProductoItem(int id, String nombre, double precio, boolean esCombo) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.esCombo = esCombo;
            
            // Creamos una "Tarjeta" simulando el botón de cuadrito de la imagen
            panel = new JPanel(new java.awt.BorderLayout(5, 5));
            panel.setBackground(new java.awt.Color(30, 30, 32)); // Gris profundo
            panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
                javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10)
            ));
            // Limitamos y definimos altura de tarjetas en el grid
            panel.setPreferredSize(new java.awt.Dimension(200, 180));
            
            // Simular icono o texto principal (Nombre y precio centrado)
            String htmlText = "<html><center><br/><b>" + nombre + "</b><br/><br/><font color='#cca95a'>$" + String.format("%.2f", precio) + "</font></center></html>";
            
            checkBox = new JCheckBox(htmlText);
            checkBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            checkBox.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
            checkBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            checkBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            checkBox.setBackground(new java.awt.Color(30, 30, 32));
            checkBox.setForeground(new java.awt.Color(230, 230, 220));
            checkBox.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
            checkBox.setFocusPainted(false);
            
            // Spinner para la cantidad, sutil abajo
            spinnerCantidad = new JSpinner(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));
            spinnerCantidad.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            spinnerCantidad.setPreferredSize(new java.awt.Dimension(60, 25));
            
            // Comportamiento Visual de selección
            checkBox.addChangeListener(e -> {
                if(checkBox.isSelected()) {
                    panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 169, 90), 2), // Borde dorado activo
                        javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10)
                    ));
                    panel.setBackground(new java.awt.Color(40, 40, 42));
                    checkBox.setBackground(new java.awt.Color(40, 40, 42));
                } else {
                    panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2), // Borde negro inactivo
                        javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10)
                    ));
                    panel.setBackground(new java.awt.Color(30, 30, 32));
                    checkBox.setBackground(new java.awt.Color(30, 30, 32));
                }
            });

            panel.add(checkBox, java.awt.BorderLayout.CENTER);
            
            // Para el spinner
            JPanel pSpinner = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
            pSpinner.setOpaque(false);
            pSpinner.add(new javax.swing.JLabel("<html><font color='#888888'>Cant:</font></html>"));
            pSpinner.add(spinnerCantidad);
            
            javax.swing.JPanel pNota = new javax.swing.JPanel(new java.awt.BorderLayout(0, 4));
            pNota.setOpaque(false);
            javax.swing.JLabel lNota = new javax.swing.JLabel("<html><font color='#888888'>Nota:</font></html>");
            lNota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            notaEspecial = new javax.swing.JTextField();
            notaEspecial.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
            notaEspecial.setBackground(new java.awt.Color(25, 25, 28));
            notaEspecial.setForeground(new java.awt.Color(230, 230, 220));
            notaEspecial.setCaretColor(new java.awt.Color(230, 230, 220));
            notaEspecial.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 70, 75), 1),
                javax.swing.BorderFactory.createEmptyBorder(4, 6, 4, 6)
            ));
            pNota.add(lNota, java.awt.BorderLayout.NORTH);
            pNota.add(notaEspecial, java.awt.BorderLayout.CENTER);

            javax.swing.JPanel southWrap = new javax.swing.JPanel(new java.awt.BorderLayout(0, 4));
            southWrap.setOpaque(false);
            southWrap.add(pSpinner, java.awt.BorderLayout.NORTH);
            southWrap.add(pNota, java.awt.BorderLayout.CENTER);

            panel.add(southWrap, java.awt.BorderLayout.SOUTH);
        }
    }

    /**
     * Creates new form PSTOrden
     */
    public PSTOrden() {
        initComponents();jBAgregar.setBackground(null);
        jBRegistrar.setBackground(null);
        jBRegresar.setBackground(null);
        jBCombos.setBackground(null);
        jBPlatillos.setBackground(null);
        jBBebidas.setBackground(null);
        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {}
        this.setTitle("PerfectStrangers - Tomar Orden");

        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
        configurarInterfaz();
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
        jPCont = new javax.swing.JPanel();
        jBAgregar = new javax.swing.JButton();
        jBCombos = new javax.swing.JButton();
        jBPlatillos = new javax.swing.JButton();
        jBBebidas = new javax.swing.JButton();
        jPOrden = new javax.swing.JPanel();
        jLOrden = new javax.swing.JLabel();
        jBRegistrar = new javax.swing.JButton();
        jCNoMesa = new javax.swing.JComboBox<>();
        jLNVentana = new javax.swing.JLabel();
        jBRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jPCont.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jBAgregar.setBackground(new java.awt.Color(204, 204, 204));
        jBAgregar.setText("AGREGAR");
        jBAgregar.addActionListener(this::jBAgregarActionPerformed);

        javax.swing.GroupLayout jPContLayout = new javax.swing.GroupLayout(jPCont);
        jPCont.setLayout(jPContLayout);
        jPContLayout.setHorizontalGroup(
            jPContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPContLayout.createSequentialGroup()
                .addContainerGap(378, Short.MAX_VALUE)
                .addComponent(jBAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPContLayout.setVerticalGroup(
            jPContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPContLayout.createSequentialGroup()
                .addContainerGap(354, Short.MAX_VALUE)
                .addComponent(jBAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jBCombos.setText("Combos");

        jBPlatillos.setText("Platillos");

        jBBebidas.setText("Bebidas");
        jBBebidas.addActionListener(this::jBBebidasActionPerformed);

        jPOrden.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLOrden.setText("-");

        jBRegistrar.setBackground(new java.awt.Color(204, 204, 204));
        jBRegistrar.setText("REGISTRAR");

        jCNoMesa.addActionListener(this::jCNoMesaActionPerformed);

        javax.swing.GroupLayout jPOrdenLayout = new javax.swing.GroupLayout(jPOrden);
        jPOrden.setLayout(jPOrdenLayout);
        jPOrdenLayout.setHorizontalGroup(
            jPOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPOrdenLayout.createSequentialGroup()
                .addGroup(jPOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPOrdenLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jBRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                            .addComponent(jLOrden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jCNoMesa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPOrdenLayout.setVerticalGroup(
            jPOrdenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPOrdenLayout.createSequentialGroup()
                .addComponent(jCNoMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLNVentana.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLNVentana.setForeground(new java.awt.Color(255, 255, 255));
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLNVentana.setText("TOMA DE ORDENES");

        jBRegresar.setBackground(new java.awt.Color(204, 204, 204));
        jBRegresar.setText("REGRESAR");

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPrincipalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(299, 299, 299))
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLNVentana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addComponent(jPOrden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPPrincipalLayout.createSequentialGroup()
                                .addComponent(jBPlatillos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBCombos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jBBebidas, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLNVentana)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBPlatillos, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBCombos, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBBebidas, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPOrden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBRegresar)
                .addContainerGap(11, Short.MAX_VALUE))
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

    private void jBBebidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBBebidasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBBebidasActionPerformed

    private void jBAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAgregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBAgregarActionPerformed

    private void configurarInterfaz() {
        // Rediseño visual "Acorazado/Industrial" para Toma de Órdenes
        java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);
        java.awt.Color casiNegro = new java.awt.Color(25, 25, 25);
        java.awt.Color metal = new java.awt.Color(45, 45, 47);
        java.awt.Color metalClaro = new java.awt.Color(60, 60, 65);
        java.awt.Color claro = new java.awt.Color(230, 230, 220);

        // Fondo Principal (Plancha de acero)
        jPPrincipal.removeAll();
        jPPrincipal.setLayout(new java.awt.BorderLayout());
        
        javax.swing.JPanel panelFondoAcero = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo oscuro base profundo
                g2.setColor(new java.awt.Color(20, 15, 10));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Viñeta oscura radial
                java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f, Math.max(getWidth(), getHeight()) / 1.1f,
                    new float[]{ 0.4f, 1.0f },
                    new java.awt.Color[]{
                        new java.awt.Color(0, 0, 0, 100), 
                        new java.awt.Color(10, 10, 10, 240)
                    }
                );
                g2.setPaint(rgp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Marco Acero Base 1
                g2.setStroke(new java.awt.BasicStroke(20f));
                g2.setColor(metal);
                g2.drawRect(10, 10, getWidth()-20, getHeight()-20);
                
                // Ribete interior Dorado Principal
                g2.setStroke(new java.awt.BasicStroke(2f));
                g2.setColor(tonoOro);
                g2.drawRect(22, 22, getWidth()-44, getHeight()-44);
                
                g2.dispose();
            }
        };
        // Separación grande hacia adentro para el aire respirable
        panelFondoAcero.setBorder(javax.swing.BorderFactory.createEmptyBorder(35, 40, 35, 40));

        // Título Superior "TOMA DE ÓRDENES" centrado
        jLNVentana.setText("TOMA DE ORDENES");
        jLNVentana.setForeground(tonoOro);
        jLNVentana.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        jLNVentana.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        // CONTENEDOR PRINCIPAL DIVIDIDO EN 2 COLUMNAS (Izquierda: Resumen | Derecha: Categorías y Productos)
        javax.swing.JPanel panelSplit = new javax.swing.JPanel(new java.awt.BorderLayout(20, 0));
        panelSplit.setOpaque(false);
        // Despegamos la cuadrícula de arriba
        panelSplit.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // =====================================================================
        // ================= PANEL IZQUIERDO (RESUMEN ORDEN) ===================
        // =====================================================================
        jPOrden.removeAll();
        jPOrden.setLayout(new java.awt.BorderLayout(0, 10));
        jPOrden.setOpaque(true);
        jPOrden.setBackground(new java.awt.Color(35, 35, 36)); // Plomo ligeramente claro de caja registradora
        
        // Contorno de la caja izquierda ("Metálico con remache/borde")
        jPOrden.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, metalClaro),
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        jPOrden.setPreferredSize(new java.awt.Dimension(350, 0)); // Ancho fijo del ticket

        // COMBO BOX (Mesa)
        // Eliminamos "Mesa 1..10" default para recrearlo despues estilo chido
        jCNoMesa.setBackground(new java.awt.Color(50, 50, 50));
        jCNoMesa.setForeground(claro);
        jCNoMesa.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        jCNoMesa.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(tonoOro, 1),
            javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 4)
        ));
        // Llenado forzado del Combo
        jCNoMesa.removeAllItems();
        for (int i = 1; i <= 15; i++) { jCNoMesa.addItem("Mesa " + i); }

        // ÁREA VISUAL DEL TICKET (JTable en lugar de Label)
        modeloOrden = new DefaultTableModel(new Object[]{"ID", "Nombre", "C", "P", "IsCombo", "Notas", "X"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Nada es editable directo desde click en celda
        };
        tablaOrden = new JTable(modeloOrden);
        
        // Esconder columnas técnicas
        tablaOrden.getColumnModel().getColumn(0).setMinWidth(0); tablaOrden.getColumnModel().getColumn(0).setMaxWidth(0); tablaOrden.getColumnModel().getColumn(0).setWidth(0);
        tablaOrden.getColumnModel().getColumn(4).setMinWidth(0); tablaOrden.getColumnModel().getColumn(4).setMaxWidth(0); tablaOrden.getColumnModel().getColumn(4).setWidth(0);
        tablaOrden.getColumnModel().getColumn(5).setMinWidth(0); tablaOrden.getColumnModel().getColumn(5).setMaxWidth(0); tablaOrden.getColumnModel().getColumn(5).setWidth(0);
        
        // Ajuste de proporciones (Nombre muy largo, Cantidad (C) y Precio (P) breves)
        tablaOrden.getColumnModel().getColumn(1).setPreferredWidth(170);
        tablaOrden.getColumnModel().getColumn(2).setPreferredWidth(30);
        tablaOrden.getColumnModel().getColumn(3).setPreferredWidth(50);
        tablaOrden.getColumnModel().getColumn(6).setPreferredWidth(35); // Para el botón X

        // Evento para borrar fila al presionar en "X"
        tablaOrden.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tablaOrden.rowAtPoint(evt.getPoint());
                int col = tablaOrden.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 6) {
                    modeloOrden.removeRow(row);
                }
            }
        });
        
        // Estilizar tabla
        tablaOrden.setBackground(new java.awt.Color(20, 20, 20)); // Fondo ticket perrón
        tablaOrden.setForeground(claro);
        tablaOrden.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        tablaOrden.setRowHeight(35);
        tablaOrden.setShowGrid(false); // Quitar rayas celda
        tablaOrden.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tablaOrden.setSelectionBackground(metalClaro);
        tablaOrden.setSelectionForeground(tonoOro);
        
        // Encabezados de tabla invisibles o muy sutiles
        tablaOrden.getTableHeader().setBackground(casiNegro);
        tablaOrden.getTableHeader().setForeground(tonoOro);
        tablaOrden.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        tablaOrden.getTableHeader().setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, tonoOro));
        tablaOrden.getTableHeader().setReorderingAllowed(false);

        javax.swing.JScrollPane scrollTicket = new javax.swing.JScrollPane(tablaOrden);
        scrollTicket.getViewport().setBackground(new java.awt.Color(20, 20, 20));
        scrollTicket.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(casiNegro, 2),
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        javax.swing.JPanel panelCentroTicket = new javax.swing.JPanel(new java.awt.BorderLayout(0, 8));
        panelCentroTicket.setOpaque(false);
        panelCentroTicket.add(scrollTicket, java.awt.BorderLayout.CENTER);

        // BOTÓN: REGISTRAR (Debajo del ticket)
        jBRegistrar.setText("REGISTRAR");
        jBRegistrar.setBackground(new java.awt.Color(40, 40, 40)); 
        jBRegistrar.setForeground(tonoOro);
        jBRegistrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jBRegistrar.setFocusPainted(false);
        jBRegistrar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(tonoOro, 1),
            javax.swing.BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));
        
        jPOrden.add(jCNoMesa, java.awt.BorderLayout.NORTH);
        jPOrden.add(panelCentroTicket, java.awt.BorderLayout.CENTER);
        jPOrden.add(jBRegistrar, java.awt.BorderLayout.SOUTH);

        // =====================================================================
        // ================= PANEL DERECHO (SELECCIÓN DE PRODUCTOS) ============
        // =====================================================================
        javax.swing.JPanel panelDerecho = new javax.swing.JPanel(new java.awt.BorderLayout(0, 15));
        panelDerecho.setOpaque(false);

        // PESTAÑAS / BOTONES DE CATEGORÍA
        javax.swing.JPanel panelCategorias = new javax.swing.JPanel(new java.awt.GridLayout(1, 3, 10, 0));
        panelCategorias.setOpaque(false);

        // Reestilizar pestañas basado en el wireframe (Plomo con hover y outline)
        javax.swing.JButton[] botonesCat = {jBPlatillos, jBCombos, jBBebidas};
        jBPlatillos.setText("Platillos");
        jBCombos.setText("Combos");
        jBBebidas.setText("Bebidas");
        for (javax.swing.JButton btn : botonesCat) {
            btn.setBackground(new java.awt.Color(40, 40, 42)); // Tono Gris plomo
            btn.setForeground(tonoOro);
            btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
            btn.setFocusPainted(false);
            // Simular botón metálico gordo
            btn.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(casiNegro, 2),
                javax.swing.BorderFactory.createEmptyBorder(12, 10, 12, 10)
            ));
            btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        panelCategorias.add(jBPlatillos);
        panelCategorias.add(jBCombos);
        panelCategorias.add(jBBebidas);

        // ÁREA DE "CARDS" O BLOQUES DE PRODUCTO
        jPCont.removeAll();
        // Usamos una cuadrícula (ej. 3 columnas y "N" filas dinámicas)
        jPCont.setLayout(new java.awt.GridLayout(0, 3, 15, 15)); 
        jPCont.setOpaque(false);
        jPCont.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));

        javax.swing.JScrollPane scrollContenedorCartas = new javax.swing.JScrollPane(jPCont);
        scrollContenedorCartas.getViewport().setOpaque(false);
        scrollContenedorCartas.setOpaque(false);
        scrollContenedorCartas.setBorder(null);

        // PANEL INFERIOR DERECHO (Botón Regresar Izquierda, Botón Agregar a Ticket Derecha)
        javax.swing.JPanel panelBotonesDerechaInferior = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelBotonesDerechaInferior.setOpaque(false);

        // Botón REGRESAR
        jBRegresar.setText("REGRESAR");
        jBRegresar.setBackground(new java.awt.Color(40, 40, 42));
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        jBRegresar.setFocusPainted(false);
        jBRegresar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(casiNegro, 2),
            javax.swing.BorderFactory.createEmptyBorder(8, 30, 8, 30)
        ));

        // Botón AGREGAR (Manda lo seleccionado al ticket izquierdo)
        jBAgregar.setText("AGREGAR");
        jBAgregar.setBackground(new java.awt.Color(40, 40, 42)); // Puede ser más dorado si deseas luego
        jBAgregar.setForeground(tonoOro);
        jBAgregar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        jBAgregar.setFocusPainted(false);
        jBAgregar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(casiNegro, 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        // Acomodo botones inferiores
        javax.swing.JPanel panelAccionesBajo = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelAccionesBajo.setOpaque(false);
        // Ponemos REGRESAR a la izquierda (WEST) 
        panelAccionesBajo.add(jBRegresar, java.awt.BorderLayout.WEST);
        
        // AGREGAR en el centro
        javax.swing.JPanel centroWrap = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        centroWrap.setOpaque(false);
        centroWrap.add(jBAgregar);
        panelAccionesBajo.add(centroWrap, java.awt.BorderLayout.CENTER);
        
        panelBotonesDerechaInferior.add(panelAccionesBajo, java.awt.BorderLayout.CENTER);

        panelDerecho.add(panelCategorias, java.awt.BorderLayout.NORTH);
        panelDerecho.add(scrollContenedorCartas, java.awt.BorderLayout.CENTER);
        panelDerecho.add(panelBotonesDerechaInferior, java.awt.BorderLayout.SOUTH);

        // ENSAMBLAR EL SPLIT
        panelSplit.add(jPOrden, java.awt.BorderLayout.WEST);
        panelSplit.add(panelDerecho, java.awt.BorderLayout.CENTER);

        panelFondoAcero.add(jLNVentana, java.awt.BorderLayout.NORTH);
        panelFondoAcero.add(panelSplit, java.awt.BorderLayout.CENTER);

        jPPrincipal.add(panelFondoAcero, java.awt.BorderLayout.CENTER);

        // Re-asignar eventos
        for (java.awt.event.ActionListener al : jBPlatillos.getActionListeners()) jBPlatillos.removeActionListener(al);
        for (java.awt.event.ActionListener al : jBCombos.getActionListeners()) jBCombos.removeActionListener(al);
        for (java.awt.event.ActionListener al : jBBebidas.getActionListeners()) jBBebidas.removeActionListener(al);
        for (java.awt.event.ActionListener al : jBAgregar.getActionListeners()) jBAgregar.removeActionListener(al);
        for (java.awt.event.ActionListener al : jBRegistrar.getActionListeners()) jBRegistrar.removeActionListener(al);

        jBPlatillos.addActionListener(e -> { resaltarPestaña(botonesCat, jBPlatillos); cargarProductos("Platillos"); });
        jBCombos.addActionListener(e -> { resaltarPestaña(botonesCat, jBCombos); cargarProductos("Combos"); });
        jBBebidas.addActionListener(e -> { resaltarPestaña(botonesCat, jBBebidas); cargarProductos("Bebidas"); });
        jBAgregar.addActionListener(e -> agregarAOrden());
        jBRegistrar.addActionListener(e -> registrarOrden());

        jPPrincipal.revalidate();
        jPPrincipal.repaint();

        productosActuales = new ArrayList<>();
        resaltarPestaña(botonesCat, jBPlatillos);
        cargarProductos("Platillos");
    }
    
    // Función auxiliar para "prender en oro" la pestaña aciva
    private void resaltarPestaña(javax.swing.JButton[] botones, javax.swing.JButton activo) {
        for(javax.swing.JButton b : botones) {
            b.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 25, 25), 2),
                javax.swing.BorderFactory.createEmptyBorder(12, 10, 12, 10)
            ));
            b.setForeground(new java.awt.Color(160, 160, 150));
        }
        activo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 169, 90), 2),
            javax.swing.BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));
        activo.setForeground(new java.awt.Color(204, 169, 90));
    }

    private void cargarProductos(String categoria) {
        jPCont.removeAll();
        java.util.List<ProductoItem> tempList = new ArrayList<>();
        boolean esCombo = "Combos".equals(categoria);

        try {
            List<Producto> productos;
            if ("Platillos".equals(categoria)) {
                productos = ProductoDAO.obtenerProductosPorCategoria("Platillo");
            } else if ("Bebidas".equals(categoria)) {
                productos = ProductoDAO.obtenerProductosPorCategoria("Bebidas");
            } else {
                // En el nuevo esquema, este tab se mapea a Complementos.
                productos = ProductoDAO.obtenerProductosPorCategoria("Complementos");
            }

            for (Producto producto : productos) {
                ProductoItem item = new ProductoItem(
                    producto.getIdProducto(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    esCombo
                );
                tempList.add(item);
                jPCont.add(item.panel);
            }
        } catch (SQLException ex) {
            System.err.println("Error base de datos: " + ex.getMessage());
            // Llenado falso para que puedas ver el diseño aunque la base de datos falte o falle
            for (int i = 1; i <= 6; i++) {
                ProductoItem item = new ProductoItem(i, categoria + " " + i, 50.0 + (i * 10.5), esCombo);
                tempList.add(item);
                jPCont.add(item.panel);
            }
        }
        
        productosActuales = tempList;
        
        // Rellenar espacios vacíos en el grid si son pocos elementos
        int sobrantes = 3 - (tempList.size() % 3);
        if (sobrantes < 3) {
            for (int i = 0; i < sobrantes; i++) {
                javax.swing.JPanel vacio = new javax.swing.JPanel();
                vacio.setOpaque(false);
                jPCont.add(vacio);
            }
        }

        jPCont.revalidate();
        jPCont.repaint();
    }

    private void agregarAOrden() {
        if (productosActuales == null) return;
        for (ProductoItem item : productosActuales) {
            if (item.checkBox.isSelected()) {
                int cantidad = (int) item.spinnerCantidad.getValue();
                String notas = item.notaEspecial != null ? item.notaEspecial.getText().trim() : "";
                modeloOrden.addRow(new Object[]{ item.id, item.nombre, cantidad, item.precio, item.esCombo, notas, "❌" });
                item.checkBox.setSelected(false);
                item.spinnerCantidad.setValue(1); // Resetear spinner
                if (item.notaEspecial != null) {
                    item.notaEspecial.setText("");
                }
            }
        }
    }

    private void borrarSeleccionados() {
        int[] seleccionados = tablaOrden.getSelectedRows();
        for (int i = seleccionados.length - 1; i >= 0; i--) {
            modeloOrden.removeRow(seleccionados[i]);
        }
    }

    private void registrarOrden() {
        if (modeloOrden.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "La orden está vacía. Selecciona productos primero.");
            return;
        }
        String mesaStr = jCNoMesa.getSelectedItem().toString();
        int numMesa = 1;
        try { numMesa = Integer.parseInt(mesaStr.replace("Mesa ", "").trim()); } catch (Exception e){}

        Integer idEmpleadoSesion = ServicioSesion.getIdEmpleadoActual();
        int idEmpleado = (idEmpleadoSesion != null && idEmpleadoSesion > 0)
            ? idEmpleadoSesion
            : (Sesion.idEmpleado > 0 ? Sesion.idEmpleado : 1);

        try {
            int idOrden;
            List<Orden> abiertasMesa = ServicioOrden.obtenerOrdenesMesa(numMesa);
            if (!abiertasMesa.isEmpty()) {
                idOrden = abiertasMesa.get(0).getIdOrden();
            } else {
                idOrden = ServicioOrden.crearOrden(idEmpleado, numMesa).getIdOrden();
            }

            for (int i = 0; i < modeloOrden.getRowCount(); i++) {
                int idItem = (int) modeloOrden.getValueAt(i, 0);

                int cantidad = 1;
                Object cantObj = modeloOrden.getValueAt(i, 2);
                if (cantObj instanceof Integer) {
                    cantidad = (Integer) cantObj;
                } else if (cantObj instanceof String) {
                    try { cantidad = Integer.parseInt((String) cantObj); } catch (Exception e) { }
                }

                double precio = 0.0;
                Object precioObj = modeloOrden.getValueAt(i, 3);
                if (precioObj instanceof Number) {
                    precio = ((Number) precioObj).doubleValue();
                } else if (precioObj != null) {
                    try { precio = Double.parseDouble(precioObj.toString()); } catch (Exception e) { }
                }

                String notas = "";
                Object notasObj = modeloOrden.getValueAt(i, 5);
                if (notasObj != null) {
                    notas = notasObj.toString();
                }

                ServicioOrden.agregarProductoAOrden(idOrden, idItem, cantidad, precio, notas);
            }

            javax.swing.JOptionPane.showMessageDialog(this, abiertasMesa.isEmpty()
                ? "¡Orden " + idOrden + " registrada con éxito para la " + mesaStr + " y enviada a preparación!"
                : "¡Se agregaron productos a la cuenta abierta de la " + mesaStr + " y se enviaron a preparación!");
            modeloOrden.setRowCount(0);
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar en BD: " + ex.getMessage());
        }
    }

    private void jCNoMesaActionPerformed(java.awt.event.ActionEvent evt) {
        // ...
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.put("Button.arc", 20);
            javax.swing.UIManager.put("Component.arc", 20);
            javax.swing.UIManager.put("ProgressBar.arc", 20);
            javax.swing.UIManager.put("TextComponent.arc", 20);
            javax.swing.UIManager.put("ScrollBar.arc", 20);
            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PSTOrden().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAgregar;
    private javax.swing.JButton jBBebidas;
    private javax.swing.JButton jBCombos;
    private javax.swing.JButton jBPlatillos;
    private javax.swing.JButton jBRegistrar;
    private javax.swing.JButton jBRegresar;
    private javax.swing.JComboBox<String> jCNoMesa;
    private javax.swing.JLabel jLNVentana;
    private javax.swing.JLabel jLOrden;
    private javax.swing.JPanel jPCont;
    private javax.swing.JPanel jPOrden;
    private javax.swing.JPanel jPPrincipal;
    // End of variables declaration//GEN-END:variables
}
