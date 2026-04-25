/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;

/**
 *
 * @author Ephex
 */
public class PSMenu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSMenu.class.getName());

    /**
     * Creates new form PSMenu
     */
    public PSMenu() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {}
        this.setTitle("PerfectStrangers - Menú Principal");
        
        configurarNavegacionMenu();
        configurarFondo();
        configurarSesionDatos();
    }

    private void configurarSesionDatos() {
        // Cargar datos de la sesión guardada del login
        if (Sesion.nombreEmpleado != null && !Sesion.nombreEmpleado.isEmpty()) {
            jUsuario.setText(Sesion.nombreEmpleado);
        }
        if (Sesion.puestoEmpleado != null && !Sesion.puestoEmpleado.isEmpty()) {
            jPuesto.setText(Sesion.puestoEmpleado);
        }

        // Configurar reloj / timer
        Timer timer = new Timer(1000, new ActionListener() {
            private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            @Override
            public void actionPerformed(ActionEvent e) {
                jTiempo.setText(sdf.format(new Date()));
            }
        });
        timer.start();
    }

    private void configurarFondo() {
        try {
            // Removemos el layout generado de NetBeans para jPPrincipal
            jPPrincipal.removeAll();
            jPPrincipal.setLayout(new java.awt.BorderLayout());
            
            // Colores temáticos
            java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);
            java.awt.Color casiNegro = new java.awt.Color(25, 25, 25);
            java.awt.Color acero = new java.awt.Color(45, 45, 47);
            
            // Panel especial que dibuja el marco metálico industrial y el gif al centro
            javax.swing.JPanel panelFondoAcero = new javax.swing.JPanel(new java.awt.BorderLayout()) {
                private java.awt.Image bgImage;
                {
                    try {
                        java.net.URL urlGif = getClass().getResource("/com/mycompany/perfectstrangers/Animación_en_Bucle_Lista.gif");
                        if (urlGif != null) {
                            bgImage = new javax.swing.ImageIcon(urlGif).getImage();
                        }
                    } catch (Exception e) {}
                }
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    super.paintComponent(g);
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Fondo negro profundo
                    g2.setColor(new java.awt.Color(15, 12, 10));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Dibujar la imagen gif/logo centrado, redimensionándolo si es necesario sin perder proporción
                    if (bgImage != null) {
                        int imgW = bgImage.getWidth(this);
                        int imgH = bgImage.getHeight(this);
                        if (imgW > 0 && imgH > 0) {
                            int drawH = getHeight() - 150;
                            int drawW = (int) (drawH * ((double) imgW / imgH));
                            if (drawW > getWidth() - 150) {
                                drawW = getWidth() - 150;
                                drawH = (int) (drawW * ((double) imgH / imgW));
                            }
                            int x = (getWidth() - drawW) / 2;
                            int y = (getHeight() - drawH) / 2;
                            g2.drawImage(bgImage, x, y, drawW, drawH, this);
                        }
                    }
                    
                    // Efecto viñeta oscura
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
                    
                    // Marco Acero
                    g2.setStroke(new java.awt.BasicStroke(20f));
                    g2.setColor(acero);
                    g2.drawRect(10, 10, getWidth()-20, getHeight()-20);
                    
                    // Ribete interior Dorado
                    g2.setStroke(new java.awt.BasicStroke(2f));
                    g2.setColor(tonoOro);
                    g2.drawRect(22, 22, getWidth()-44, getHeight()-44);
                    
                    g2.dispose();
                }
            };
            
            // Reestilizar la barra inferior (jMenuInf)
            // Cambiamos a FlowLayout y quitamos tamaños fijos para evitar que los textos largos se recorten ("Señora...")
            jMenuInf.removeAll();
            jMenuInf.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 10));
            jMenuInf.setOpaque(true);
            jMenuInf.setBackground(casiNegro);
            jMenuInf.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, tonoOro), // Línea dorada arriba
                javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
            
            java.awt.Font fuenteBarra = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15);
            
            jLUsuario.setForeground(tonoOro); jLUsuario.setFont(fuenteBarra);
            jUsuario.setForeground(java.awt.Color.WHITE); jUsuario.setFont(fuenteBarra);
            jUsuario.setPreferredSize(null); // Quitamos la restricción de NetBeans
            jUsuario.setMinimumSize(null);
            
            jLPuesto.setForeground(tonoOro); jLPuesto.setFont(fuenteBarra);
            jPuesto.setForeground(java.awt.Color.WHITE); jPuesto.setFont(fuenteBarra);
            jPuesto.setPreferredSize(null);
            jPuesto.setMinimumSize(null);
            
            jLTiempo.setForeground(tonoOro); jLTiempo.setFont(fuenteBarra);
            jTiempo.setForeground(java.awt.Color.WHITE); jTiempo.setFont(fuenteBarra);
            jTiempo.setPreferredSize(null);
            
            // Reagregamos los componentes a nuestro nuevo layout elástico
            jMenuInf.add(jLUsuario);
            jMenuInf.add(jUsuario);
            jMenuInf.add(new javax.swing.JLabel("  |  ")); // Separador decorativo
            jMenuInf.add(jLPuesto);
            jMenuInf.add(jPuesto);
            jMenuInf.add(new javax.swing.JLabel("  |  "));
            jMenuInf.add(jLTiempo);
            jMenuInf.add(jTiempo);
            
            // Ajustar el color de los separadores
            for(java.awt.Component c : jMenuInf.getComponents()) {
                if (c instanceof javax.swing.JLabel && ((javax.swing.JLabel)c).getText().equals("  |  ")) {
                    c.setForeground(tonoOro);
                    c.setFont(fuenteBarra);
                }
            }
            
            // Retirar el menú de la ventana por defecto
            this.setJMenuBar(null);
            
            // ¡Poner la barra de menús DENTRO del estilo metálico!
            jMenuSup.setOpaque(false); // Fondo transparente para que herede el negro del panel
            jMenuSup.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(25, 25, 0, 25), // Alejar del marco de acero interior
                javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, tonoOro) // Línea inferior dorada
            ));
            
            // Customizar cada menú desplegable principal (JMenu) y su pop-up interior (JPopupMenu)
            for(int i = 0; i < jMenuSup.getMenuCount(); i++) {
                javax.swing.JMenu menu = jMenuSup.getMenu(i);
                if (menu != null) {
                    menu.setOpaque(false);
                    menu.setForeground(tonoOro);
                    menu.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17)); // Letra más grande y notoria
                    menu.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
                    
                    // Modificar la ventanita pop-up al abrirse estilo metal dorado oscuro
                    javax.swing.JPopupMenu popup = menu.getPopupMenu();
                    popup.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createLineBorder(tonoOro, 1),
                        javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2)
                    ));
                    popup.setBackground(new java.awt.Color(40, 40, 45)); // Gris Metálico Plomo
                    popup.setOpaque(true);
                    
                    // Modificar todos los Items de menú por dentro (JMenuItem)
                    for(java.awt.Component subItem : popup.getComponents()) {
                        if (subItem instanceof javax.swing.JMenuItem) {
                            javax.swing.JMenuItem mi = (javax.swing.JMenuItem) subItem;
                            mi.setOpaque(true);
                            mi.setBackground(new java.awt.Color(40, 40, 45)); // Mismo fondo plomo
                            mi.setForeground(new java.awt.Color(230, 230, 220)); // Letra crema en lugar de gris feo
                            mi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
                            mi.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Espacio para que queden más gordos
                            
                            // Efecto hover (iluminación cuando pasas el mouse):
                            mi.addChangeListener(new javax.swing.event.ChangeListener() {
                                @Override
                                public void stateChanged(javax.swing.event.ChangeEvent e) {
                                    if (mi.isArmed()) {
                                        mi.setBackground(casiNegro); // Se vuelve muy negro
                                        mi.setForeground(tonoOro);   // Se pinta de dorado
                                    } else {
                                        mi.setBackground(new java.awt.Color(40, 40, 45)); // Regresa a gris
                                        mi.setForeground(new java.awt.Color(230, 230, 220)); // Regresa a blanco
                                    }
                                }
                            });
                        }
                    }
                }
            }

            // Reagregamos los componentes maestos
            panelFondoAcero.add(jMenuSup, java.awt.BorderLayout.NORTH); // El menú pegado arriba, incrustado en nuestra chapa
            jPPrincipal.add(panelFondoAcero, java.awt.BorderLayout.CENTER);
            jPPrincipal.add(jMenuInf, java.awt.BorderLayout.SOUTH);

            jPPrincipal.revalidate();
            jPPrincipal.repaint();
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING, "Error al configurar fondo.", e);
        }
    }

    private void configurarNavegacionMenu() {
        // En vez de MenuListener (que se acciona con solo deslizar el mouse si otro menú está abierto),
        // utilizamos MouseListener para forzar un click real si queremos abrir ventanas de golpe desde un encabezado.
        
        java.awt.event.MouseAdapter clickHandlerInventario = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
                    javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
                    abrirVentana(new PSInventario());
                }
            }
        };
        
        java.awt.event.MouseAdapter clickHandlerHistorial = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
                    javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
                    abrirVentana(new PSHistorial());
                }
            }
        };

        java.awt.event.MouseAdapter clickHandlerSalir = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
                    System.exit(0);
                }
            }
        };

        // Eliminar listeners de hover y usar clics
        for (javax.swing.event.MenuListener ml : JInicio.getMenuListeners()) JInicio.removeMenuListener(ml);
        for (javax.swing.event.MenuListener ml : JInventario.getMenuListeners()) JInventario.removeMenuListener(ml);
        for (javax.swing.event.MenuListener ml : jMenu1.getMenuListeners()) jMenu1.removeMenuListener(ml);
        for (javax.swing.event.MenuListener ml : jMenu2.getMenuListeners()) jMenu2.removeMenuListener(ml);

        JInventario.addMouseListener(clickHandlerInventario);
        jMenu1.addMouseListener(clickHandlerHistorial);
        jMenu2.addMouseListener(clickHandlerSalir);

        // Configurar menú de Empleados dentro de la pestaña "Menú"
        javax.swing.JMenuItem jItemEmpleados = new javax.swing.JMenuItem("Alta de Empleados");
        jItemEmpleados.addActionListener(evt -> abrirVentana(new PSEmpleados()));
        jMenu.add(jItemEmpleados);
        
        // Configurar menú de Platillos dentro de la pestaña "Menú"
        javax.swing.JMenuItem jItemPlatillos = new javax.swing.JMenuItem("Control de Platillos");
        jItemPlatillos.addActionListener(evt -> abrirVentana(new PSPlatillos()));
        jMenu.add(jItemPlatillos);

        // Configurar menú de Combos dentro de la pestaña "Menú"
        javax.swing.JMenuItem jItemCombos = new javax.swing.JMenuItem("Control de Combos");
        jItemCombos.addActionListener(evt -> abrirVentana(new PSCombos()));
        jMenu.add(jItemCombos);

        // Validar que solo un administrador o gerente pueda ver la pestaña "Menú"
        if (Sesion.puestoEmpleado != null && (Sesion.puestoEmpleado.equalsIgnoreCase("Administrador") || Sesion.puestoEmpleado.equalsIgnoreCase("Administrativo") || Sesion.puestoEmpleado.equalsIgnoreCase("Gerente"))) {
            jMenu.setVisible(true);
        } else {
            jMenu.setVisible(false);
        }

        jMOTomar.addActionListener(evt -> abrirVentana(new PSTOrden()));
        jMOConsultar.addActionListener(evt -> abrirVentana(new PSConOrder()));
        jMOCobrar.addActionListener(evt -> abrirVentana(new PSCobOrden()));
    }

    private boolean isAperturaEnProgreso = false;

    private void abrirVentana(javax.swing.JFrame ventana) {
        if (isAperturaEnProgreso) return;
        isAperturaEnProgreso = true;
        java.awt.EventQueue.invokeLater(() -> ventana.setVisible(true));
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPPrincipal = new javax.swing.JPanel();
        jMenuInf = new javax.swing.JPanel();
        jLUsuario = new javax.swing.JLabel();
        jUsuario = new javax.swing.JLabel();
        jLPuesto = new javax.swing.JLabel();
        jPuesto = new javax.swing.JLabel();
        jLTiempo = new javax.swing.JLabel();
        jTiempo = new javax.swing.JLabel();
        jMenuSup = new javax.swing.JMenuBar();
        JInicio = new javax.swing.JMenu();
        jMenu = new javax.swing.JMenu();
        jMOrdenes = new javax.swing.JMenu();
        jMOTomar = new javax.swing.JMenuItem();
        jMOConsultar = new javax.swing.JMenuItem();
        jMOCobrar = new javax.swing.JMenuItem();
        JInventario = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jMenuInf.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.lightGray, null, null));

        jLUsuario.setText("Usuario: ");

        jUsuario.setText("-");

        jLPuesto.setText("Puesto: ");

        jPuesto.setText("-");

        jLTiempo.setText("Tiempo: ");

        jTiempo.setText("99:99:99");

        javax.swing.GroupLayout jMenuInfLayout = new javax.swing.GroupLayout(jMenuInf);
        jMenuInf.setLayout(jMenuInfLayout);
        jMenuInfLayout.setHorizontalGroup(
            jMenuInfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMenuInfLayout.createSequentialGroup()
                .addComponent(jLUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLPuesto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLTiempo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 92, Short.MAX_VALUE))
        );
        jMenuInfLayout.setVerticalGroup(
            jMenuInfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMenuInfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addComponent(jUsuario)
                .addComponent(jLPuesto)
                .addComponent(jPuesto)
                .addComponent(jLTiempo)
                .addComponent(jTiempo))
        );

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMenuInf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPrincipalLayout.createSequentialGroup()
                .addGap(0, 348, Short.MAX_VALUE)
                .addComponent(jMenuInf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        JInicio.setText("Inicio");
        jMenuSup.add(JInicio);

        jMenu.setText("Menú");
        jMenuSup.add(jMenu);

        jMOrdenes.setText("Ordenes");

        jMOTomar.setText("Tomar");
        jMOrdenes.add(jMOTomar);

        jMOConsultar.setText("Consultar");
        jMOrdenes.add(jMOConsultar);

        jMOCobrar.setText("Cobrar");
        jMOrdenes.add(jMOCobrar);

        jMenuSup.add(jMOrdenes);

        JInventario.setText("Inventario");
        jMenuSup.add(JInventario);

        jMenu1.setText("Historial");
        jMenuSup.add(jMenu1);

        jMenu2.setText("Salir");
        jMenuSup.add(jMenu2);

        setJMenuBar(jMenuSup);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new PSMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu JInicio;
    private javax.swing.JMenu JInventario;
    private javax.swing.JLabel jLPuesto;
    private javax.swing.JLabel jLTiempo;
    private javax.swing.JLabel jLUsuario;
    private javax.swing.JMenuItem jMOCobrar;
    private javax.swing.JMenuItem jMOConsultar;
    private javax.swing.JMenuItem jMOTomar;
    private javax.swing.JMenu jMOrdenes;
    private javax.swing.JMenu jMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jMenuInf;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuBar jMenuSup;
    private javax.swing.JPanel jPPrincipal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jPuesto;
    private javax.swing.JLabel jTiempo;
    private javax.swing.JLabel jUsuario;
    // End of variables declaration//GEN-END:variables
}
