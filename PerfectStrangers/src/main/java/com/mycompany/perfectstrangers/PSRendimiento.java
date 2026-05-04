/*
 * Ventana para visualizar gráficas de rendimiento de meseros y cocineros
 */
package com.mycompany.perfectstrangers;

import java.awt.Color;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Ephex
 */
public class PSRendimiento extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSRendimiento.class.getName());
    private JTabbedPane tabbedPane;

    private static class OpcionPersonal {
        private final Integer idEmpleado;
        private final String nombre;

        OpcionPersonal(Integer idEmpleado, String nombre) {
            this.idEmpleado = idEmpleado;
            this.nombre = nombre;
        }

        public Integer getIdEmpleado() {
            return idEmpleado;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    /**
     * Creates new form PSRendimiento
     */
    public PSRendimiento() {
        initComponents();
        
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {}
        this.setTitle("PerfectStrangers - Rendimiento de Personal");

        configurarInterfaz();
        cargarGraficas();
    }

    private void configurarInterfaz() {
        Color tonoOro = new Color(204, 169, 90);
        Color casiNegro = new Color(25, 25, 25);
        Color metal = new Color(45, 45, 47);
        Color oscuro = new Color(15, 12, 10);

        jPPrincipal.removeAll();
        jPPrincipal.setLayout(new java.awt.BorderLayout());
        
        // Panel de fondo
        javax.swing.JPanel panelFondoAcero = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(oscuro);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
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
                
                g2.setStroke(new java.awt.BasicStroke(20f));
                g2.setColor(metal);
                g2.drawRect(10, 10, getWidth()-20, getHeight()-20);
                
                g2.dispose();
            }
        };
        panelFondoAcero.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel principal
        javax.swing.JPanel panelCentral = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelCentral.setOpaque(false);

        // Crear tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(casiNegro);
        tabbedPane.setForeground(tonoOro);
        
        // Tab 1: Meseros
        tabbedPane.addTab("📊 Meseros", crearPanelMeseros(tonoOro, casiNegro, metal));
        
        // Tab 2: Cocineros
        tabbedPane.addTab("👨‍🍳 Cocineros", crearPanelCocineros(tonoOro, casiNegro, metal));
        
        panelCentral.add(tabbedPane, java.awt.BorderLayout.CENTER);

        // Panel inferior
        javax.swing.JPanel panelInferior = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 10));
        panelInferior.setOpaque(false);
        
        javax.swing.JButton jBRegresar = new javax.swing.JButton("← Regresar");
        jBRegresar.setBackground(metal);
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        jBRegresar.setPreferredSize(new java.awt.Dimension(120, 35));
        jBRegresar.addActionListener(evt -> {
            java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
            dispose();
        });
        panelInferior.add(jBRegresar);

        panelFondoAcero.add(panelCentral, java.awt.BorderLayout.CENTER);
        panelFondoAcero.add(panelInferior, java.awt.BorderLayout.SOUTH);

        jPPrincipal.add(panelFondoAcero, java.awt.BorderLayout.CENTER);
        jPPrincipal.revalidate();
        jPPrincipal.repaint();
    }

    private javax.swing.JPanel crearPanelMeseros(Color tonoOro, Color casiNegro, Color metal) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Encabezado con titulo y filtro
        javax.swing.JPanel encabezado = new javax.swing.JPanel(new java.awt.BorderLayout());
        encabezado.setOpaque(false);

        javax.swing.JLabel titulo = new javax.swing.JLabel("📈 RENDIMIENTO DE MESEROS");
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        titulo.setForeground(tonoOro);
        encabezado.add(titulo, java.awt.BorderLayout.WEST);

        javax.swing.JPanel panelFiltro = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        panelFiltro.setOpaque(false);
        
        // Filtro de Mesero
        javax.swing.JLabel lblFiltro = new javax.swing.JLabel("Mesero:");
        lblFiltro.setForeground(tonoOro);
        lblFiltro.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        panelFiltro.add(lblFiltro);

        javax.swing.JComboBox<OpcionPersonal> comboMeseros = new javax.swing.JComboBox<>();
        comboMeseros.setPreferredSize(new java.awt.Dimension(150, 28));
        comboMeseros.setBackground(casiNegro);
        comboMeseros.setForeground(tonoOro);
        cargarOpcionesPersonal(comboMeseros, "Mesero");
        panelFiltro.add(comboMeseros);
        
        // Filtro de fechas
        panelFiltro.add(new javax.swing.JLabel("Desde:") {{ setForeground(tonoOro); setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)); }});
        com.toedter.calendar.JDateChooser dateChooserInicio = new com.toedter.calendar.JDateChooser();
        dateChooserInicio.setPreferredSize(new java.awt.Dimension(120, 28));
        dateChooserInicio.setDateFormatString("yyyy-MM-dd");
        panelFiltro.add(dateChooserInicio);
        
        panelFiltro.add(new javax.swing.JLabel("Hasta:") {{ setForeground(tonoOro); setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)); }});
        com.toedter.calendar.JDateChooser dateChooserFin = new com.toedter.calendar.JDateChooser();
        dateChooserFin.setPreferredSize(new java.awt.Dimension(120, 28));
        dateChooserFin.setDateFormatString("yyyy-MM-dd");
        panelFiltro.add(dateChooserFin);
        
        encabezado.add(panelFiltro, java.awt.BorderLayout.EAST);
        panel.add(encabezado, java.awt.BorderLayout.NORTH);

        // Panel con las gráficas
        javax.swing.JPanel panelGraficas = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 10, 10));
        panelGraficas.setOpaque(false);
        panel.add(panelGraficas, java.awt.BorderLayout.CENTER);

        Runnable refrescar = () -> {
            OpcionPersonal opcion = (OpcionPersonal) comboMeseros.getSelectedItem();
            Integer idMesero = opcion != null ? opcion.getIdEmpleado() : null;
            java.sql.Date fechaInicio = null;
            java.sql.Date fechaFin = null;
            try {
                java.util.Date dInicio = dateChooserInicio.getDate();
                if (dInicio != null) fechaInicio = new java.sql.Date(dInicio.getTime());
                java.util.Date dFin = dateChooserFin.getDate();
                if (dFin != null) fechaFin = new java.sql.Date(dFin.getTime());
            } catch (Exception ex) {}
            recargarGraficasMeseros(panelGraficas, tonoOro, metal, idMesero, fechaInicio, fechaFin);
        };

        comboMeseros.addActionListener(evt -> refrescar.run());
        dateChooserInicio.addPropertyChangeListener("date", evt -> refrescar.run());
        dateChooserFin.addPropertyChangeListener("date", evt -> refrescar.run());
        refrescar.run();

        return panel;
    }

    private void recargarGraficasMeseros(javax.swing.JPanel panelGraficas, Color tonoOro, Color metal, Integer idMesero, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        panelGraficas.removeAll();
        try {
            // Gráfica de órdenes atendidas
            javax.swing.JPanel panelOrdenes = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
            panelOrdenes.setOpaque(false);
            javax.swing.JLabel lblOrdenes = new javax.swing.JLabel("Órdenes Atendidas");
            lblOrdenes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            lblOrdenes.setForeground(tonoOro);
            panelOrdenes.add(lblOrdenes, java.awt.BorderLayout.NORTH);
            
            panelOrdenes.add(new PanelGraficaBarras(tonoOro, metal,
                ServicioRendimiento.obtenerOrdenesAtendidas(idMesero, fechaInicio, fechaFin),
                "No hay ordenes atendidas para mostrar"), 
                java.awt.BorderLayout.CENTER);
            panelGraficas.add(panelOrdenes);

            // Gráfica de órdenes canceladas
            javax.swing.JPanel panelCanceladas = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
            panelCanceladas.setOpaque(false);
            javax.swing.JLabel lblCanceladas = new javax.swing.JLabel("Órdenes Canceladas");
            lblCanceladas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            lblCanceladas.setForeground(tonoOro);
            panelCanceladas.add(lblCanceladas, java.awt.BorderLayout.NORTH);
            
            panelCanceladas.add(new PanelGraficaBarras(tonoOro, metal,
                ServicioRendimiento.obtenerOrdenesCanceladasPorMesero(idMesero, fechaInicio, fechaFin),
                "No hay ordenes canceladas por meseros"), 
                java.awt.BorderLayout.CENTER);
            panelGraficas.add(panelCanceladas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos de meseros: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error loading waiter data: " + e.getMessage());
        }

        panelGraficas.revalidate();
        panelGraficas.repaint();
    }

    private javax.swing.JPanel crearPanelCocineros(Color tonoOro, Color casiNegro, Color metal) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Encabezado con titulo y filtro
        javax.swing.JPanel encabezado = new javax.swing.JPanel(new java.awt.BorderLayout());
        encabezado.setOpaque(false);

        javax.swing.JLabel titulo = new javax.swing.JLabel("👨‍🍳 RENDIMIENTO DE COCINEROS");
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        titulo.setForeground(tonoOro);
        encabezado.add(titulo, java.awt.BorderLayout.WEST);

        javax.swing.JPanel panelFiltro = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
        panelFiltro.setOpaque(false);
        
        // Filtro de Cocinero
        javax.swing.JLabel lblFiltro = new javax.swing.JLabel("Cocinero:");
        lblFiltro.setForeground(tonoOro);
        lblFiltro.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        panelFiltro.add(lblFiltro);

        javax.swing.JComboBox<OpcionPersonal> comboCocineros = new javax.swing.JComboBox<>();
        comboCocineros.setPreferredSize(new java.awt.Dimension(150, 28));
        comboCocineros.setBackground(casiNegro);
        comboCocineros.setForeground(tonoOro);
        cargarOpcionesPersonal(comboCocineros, "Cocinero");
        panelFiltro.add(comboCocineros);
        
        // Filtro de fechas
        panelFiltro.add(new javax.swing.JLabel("Desde:") {{ setForeground(tonoOro); setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)); }});
        com.toedter.calendar.JDateChooser dateChooserInicio = new com.toedter.calendar.JDateChooser();
        dateChooserInicio.setPreferredSize(new java.awt.Dimension(120, 28));
        dateChooserInicio.setDateFormatString("yyyy-MM-dd");
        panelFiltro.add(dateChooserInicio);
        
        panelFiltro.add(new javax.swing.JLabel("Hasta:") {{ setForeground(tonoOro); setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)); }});
        com.toedter.calendar.JDateChooser dateChooserFin = new com.toedter.calendar.JDateChooser();
        dateChooserFin.setPreferredSize(new java.awt.Dimension(120, 28));
        dateChooserFin.setDateFormatString("yyyy-MM-dd");
        panelFiltro.add(dateChooserFin);
        
        encabezado.add(panelFiltro, java.awt.BorderLayout.EAST);
        panel.add(encabezado, java.awt.BorderLayout.NORTH);

        // Panel con las gráficas
        javax.swing.JPanel panelGraficas = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 10, 10));
        panelGraficas.setOpaque(false);
        panel.add(panelGraficas, java.awt.BorderLayout.CENTER);

        Runnable refrescar = () -> {
            OpcionPersonal opcion = (OpcionPersonal) comboCocineros.getSelectedItem();
            Integer idCocinero = opcion != null ? opcion.getIdEmpleado() : null;
            java.sql.Date fechaInicio = null;
            java.sql.Date fechaFin = null;
            try {
                java.util.Date dInicio = dateChooserInicio.getDate();
                if (dInicio != null) fechaInicio = new java.sql.Date(dInicio.getTime());
                java.util.Date dFin = dateChooserFin.getDate();
                if (dFin != null) fechaFin = new java.sql.Date(dFin.getTime());
            } catch (Exception ex) {}
            recargarGraficasCocineros(panelGraficas, tonoOro, metal, idCocinero, fechaInicio, fechaFin);
        };

        comboCocineros.addActionListener(evt -> refrescar.run());
        dateChooserInicio.addPropertyChangeListener("date", evt -> refrescar.run());
        dateChooserFin.addPropertyChangeListener("date", evt -> refrescar.run());
        refrescar.run();

        return panel;
    }

    private void recargarGraficasCocineros(javax.swing.JPanel panelGraficas, Color tonoOro, Color metal, Integer idCocinero, java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        panelGraficas.removeAll();
        try {
            // Gráfica de órdenes preparadas
            javax.swing.JPanel panelPrepardas = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
            panelPrepardas.setOpaque(false);
            javax.swing.JLabel lblPreparadas = new javax.swing.JLabel("Órdenes Preparadas");
            lblPreparadas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            lblPreparadas.setForeground(tonoOro);
            panelPrepardas.add(lblPreparadas, java.awt.BorderLayout.NORTH);
            
            panelPrepardas.add(new PanelGraficaBarras(tonoOro, metal,
                ServicioRendimiento.obtenerOrdenesPreparadas(idCocinero, fechaInicio, fechaFin),
                "No hay ordenes preparadas para mostrar"), 
                java.awt.BorderLayout.CENTER);
            panelGraficas.add(panelPrepardas);

            // Gráfica de órdenes canceladas
            javax.swing.JPanel panelCanceladas = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
            panelCanceladas.setOpaque(false);
            javax.swing.JLabel lblCanceladas = new javax.swing.JLabel("Órdenes Canceladas");
            lblCanceladas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            lblCanceladas.setForeground(tonoOro);
            panelCanceladas.add(lblCanceladas, java.awt.BorderLayout.NORTH);
            
            panelCanceladas.add(new PanelGraficaBarras(tonoOro, metal,
                ServicioRendimiento.obtenerOrdenesCanceladasPorCocinero(idCocinero, fechaInicio, fechaFin),
                "No hay ordenes canceladas por cocineros"), 
                java.awt.BorderLayout.CENTER);
            panelGraficas.add(panelCanceladas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos de cocineros: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error loading chef data: " + e.getMessage());
        }

        panelGraficas.revalidate();
        panelGraficas.repaint();
    }

    private void cargarOpcionesPersonal(javax.swing.JComboBox<OpcionPersonal> combo, String puesto) {
        combo.removeAllItems();
        combo.addItem(new OpcionPersonal(null, "Todos"));

        try {
            LinkedHashMap<Integer, String> personal = ServicioRendimiento.obtenerPersonalPorPuesto(puesto);
            for (Map.Entry<Integer, String> entry : personal.entrySet()) {
                combo.addItem(new OpcionPersonal(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException e) {
            logger.severe("Error loading staff filter options: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar el filtro de personal: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarGraficas() {
        // Las gráficas se cargan automáticamente en los paneles
    }

    /**
     * Panel personalizado para mostrar gráficas de barras
     */
    private class PanelGraficaBarras extends javax.swing.JPanel {
        private Map<String, Integer> datos;
        private Color colorBarra;
        private Color colorFondo;
        private String mensajeSinDatos;

        public PanelGraficaBarras(Color colorBarra, Color colorFondo, Map<String, Integer> datos, String mensajeSinDatos) {
            this.datos = datos;
            this.colorBarra = colorBarra;
            this.colorFondo = colorFondo;
            this.mensajeSinDatos = mensajeSinDatos;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fondo suave para separar visualmente la grafica.
            g2.setColor(new java.awt.Color(colorFondo.getRed(), colorFondo.getGreen(), colorFondo.getBlue(), 90));
            g2.fillRoundRect(8, 8, Math.max(0, getWidth() - 16), Math.max(0, getHeight() - 16), 12, 12);

            if (datos == null || datos.isEmpty()) {
                g2.setColor(colorBarra);
                g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
                g2.drawString("Sin datos", 24, 40);
                g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
                g2.setColor(new java.awt.Color(190, 190, 190));
                g2.drawString(mensajeSinDatos, 24, 64);
                g2.dispose();
                return;
            }

            int width = getWidth();
            int height = getHeight();
            int margenIzq = 150;
            int margenDer = 35;
            int margenArriba = 30;
            int filaAlto = 34;
            int barraAlto = 20;

            int barrasCount = datos.size();
            if (barrasCount == 0) {
                g2.dispose();
                return;
            }

            int maxValue = datos.values().stream().mapToInt(Integer::intValue).max().orElse(1);

            int anchoDisponible = Math.max(120, width - margenIzq - margenDer);
            int yBase = margenArriba;

            g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
            g2.setColor(new Color(120, 120, 120));
            g2.drawString("0", margenIzq, 22);
            g2.drawString(String.valueOf(maxValue), margenIzq + anchoDisponible - 12, 22);

            for (Map.Entry<String, Integer> entry : datos.entrySet()) {
                int y = yBase;
                int anchoBarra = (int) ((entry.getValue() / (double) maxValue) * anchoDisponible);

                g2.setColor(new Color(70, 70, 70));
                g2.fillRoundRect(margenIzq, y, anchoDisponible, barraAlto, 8, 8);

                g2.setColor(colorBarra);
                g2.fillRoundRect(margenIzq, y, Math.max(2, anchoBarra), barraAlto, 8, 8);

                g2.setColor(new Color(230, 230, 230));
                g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
                String nombre = entry.getKey();
                if (nombre.length() > 18) {
                    nombre = nombre.substring(0, 18) + "...";
                }
                g2.drawString(nombre, 20, y + 14);

                g2.setColor(colorBarra);
                g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                int xValor = Math.min(margenIzq + anchoDisponible + 8, width - 25);
                g2.drawString(String.valueOf(entry.getValue()), xValor, y + 14);

                yBase += filaAlto;
                if (yBase + barraAlto > height - 10) {
                    break;
                }
            }

            g2.dispose();
        }
    }

    // Variables
    private javax.swing.JPanel jPPrincipal;

    /**
     * This method is called from within the constructor to initialize the form.
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
        java.awt.EventQueue.invokeLater(() -> new PSRendimiento().setVisible(true));
    }
}
