/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

/**
 *
 * @author Ephex
 */
public class PSEmpleados extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSEmpleados.class.getName());
    private int idEmpleadoActual = -1;
    private javax.swing.JTable jTableEmpleados;
    private javax.swing.table.DefaultTableModel modeloTabla;

    /**
     * Creates new form PSEmpleados
     */
    public PSEmpleados() {
        initComponents();
        
        // --- ESTILOS GENERALES DE LA VENTANA ---
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); // Iniciar en pantalla completa
        setLocationRelativeTo(null); // Centrar en pantalla al abrir
        
        java.awt.Font fontLabels = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
        java.awt.Font fontInputs = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24)); // Título principal más grande
        
        jLNombre.setFont(fontLabels);
        jLAPaterno.setFont(fontLabels);
        jLAMaterno.setFont(fontLabels);
        jLAMaterno1.setFont(fontLabels); // Etiqueta puesto
        jLUsuario.setFont(fontLabels);
        jLContrasena.setFont(fontLabels);
        jLContrasena1.setFont(fontLabels);
        
        jTNombre.setFont(fontInputs);
        jTAPaterno.setFont(fontInputs);
        jTAMaterno.setFont(fontInputs);
        jCBPuesto.setFont(fontInputs);
        jTUsuario.setFont(fontInputs);
        jPContrasena.setFont(fontInputs);
        jPCContrasena.setFont(fontInputs);
        
        // Estilización de botones para empatar con el resto de PerfectStrangers
        jBRegistrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jBRegistrar.setBackground(new java.awt.Color(40, 167, 69)); // Verde llamativo para la acción principal
        jBRegistrar.setForeground(java.awt.Color.WHITE);
        
        jBRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jBRegresar.setBackground(null); // Mantener el gris estándar sin forzar el sistema
        jBRegresar.setForeground(java.awt.Color.WHITE);
        // ---------------------------------------
        
        jBRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarEmpleado();
            }
        });
        
        jBRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PSMenu menu = new PSMenu();
                menu.setVisible(true);
                dispose();
            }
        });

        configurarInterfaz();
    }

    private void configurarInterfaz() {
        java.awt.Color tonoOro = new java.awt.Color(204, 169, 90);
        java.awt.Color casiNegro = new java.awt.Color(20, 20, 22);
        java.awt.Color metal = new java.awt.Color(45, 45, 47);

        javax.swing.JPanel fondoMetalico = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new java.awt.Color(16, 16, 18));
                g2.fillRect(0, 0, getWidth(), getHeight());

                java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f, Math.max(getWidth(), getHeight()) / 1.1f,
                    new float[]{0.35f, 1.0f},
                    new java.awt.Color[]{
                        new java.awt.Color(0, 0, 0, 0),
                        new java.awt.Color(5, 5, 5, 235)
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
        fondoMetalico.setBorder(javax.swing.BorderFactory.createEmptyBorder(45, 45, 45, 45));

        javax.swing.JPanel centro = new javax.swing.JPanel(new java.awt.GridBagLayout());
        centro.setOpaque(false);

        javax.swing.JPanel tarjeta = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(casiNegro);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new java.awt.Color(70, 70, 75));
                g2.setStroke(new java.awt.BasicStroke(3f));
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 16, 16);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 22));

        jPanel1.setOpaque(false);
        jLabel1.setForeground(tonoOro);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));

        javax.swing.JLabel[] labels = {jLNombre, jLAPaterno, jLAMaterno, jLAMaterno1, jLUsuario, jLContrasena, jLContrasena1};
        for (javax.swing.JLabel label : labels) {
            label.setForeground(tonoOro);
            label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        }

        javax.swing.JComponent[] campos = {jTNombre, jTAPaterno, jTAMaterno, jCBPuesto, jTUsuario, jPContrasena, jPCContrasena};
        for (javax.swing.JComponent campo : campos) {
            campo.setBackground(new java.awt.Color(34, 34, 38));
            campo.setForeground(java.awt.Color.WHITE);
            campo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            campo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 95, 100), 1),
                javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }

        jTNombre.setCaretColor(java.awt.Color.WHITE);
        jTAPaterno.setCaretColor(java.awt.Color.WHITE);
        jTAMaterno.setCaretColor(java.awt.Color.WHITE);
        jTUsuario.setCaretColor(java.awt.Color.WHITE);
        jPContrasena.setCaretColor(java.awt.Color.WHITE);
        jPCContrasena.setCaretColor(java.awt.Color.WHITE);

        jBRegistrar.setBackground(new java.awt.Color(33, 122, 79));
        jBRegistrar.setForeground(java.awt.Color.WHITE);
        jBRegistrar.setFocusPainted(false);
        jBRegistrar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        jBRegresar.setBackground(new java.awt.Color(44, 44, 48));
        jBRegresar.setForeground(tonoOro);
        jBRegresar.setFocusPainted(false);
        jBRegresar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 85), 2),
            javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        jPanel1.setBackground(metal);

        tarjeta.add(jPanel1, java.awt.BorderLayout.CENTER);
        
        // --- BOTONES EXTRA PARA CRUD ---
        javax.swing.JPanel panelBotonesExtra = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));
        panelBotonesExtra.setOpaque(false);
        
        javax.swing.JButton jBNuevo = new javax.swing.JButton("NUEVO");
        javax.swing.JButton jBActualizar = new javax.swing.JButton("ACTUALIZAR");
        javax.swing.JButton jBEliminar = new javax.swing.JButton("DAR DE BAJA");
        
        javax.swing.JButton[] extras = {jBNuevo, jBActualizar, jBEliminar};
        for (javax.swing.JButton btn : extras) {
            btn.setBackground(new java.awt.Color(44, 44, 48));
            btn.setForeground(tonoOro);
            btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
                javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16)
            ));
            panelBotonesExtra.add(btn);
        }
        
        jBNuevo.setBackground(new java.awt.Color(30, 80, 160)); 
        jBNuevo.setForeground(java.awt.Color.WHITE);
        
        jBActualizar.setBackground(new java.awt.Color(160, 120, 30)); 
        jBActualizar.setForeground(java.awt.Color.WHITE);
        
        jBEliminar.setBackground(new java.awt.Color(160, 40, 40)); 
        jBEliminar.setForeground(java.awt.Color.WHITE);
        
        jBNuevo.addActionListener(e -> limpiarCampos());
        jBActualizar.addActionListener(e -> actualizarEmpleado());
        jBEliminar.addActionListener(e -> eliminarEmpleado());
        
        tarjeta.add(panelBotonesExtra, java.awt.BorderLayout.SOUTH);
        // -------------------------------
        
        centro.add(tarjeta);
        
        // --- TABLA DE EMPLEADOS ---
        modeloTabla = new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Nombre", "A. Paterno", "A. Materno", "Puesto", "Usuario"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableEmpleados = new javax.swing.JTable(modeloTabla);
        jTableEmpleados.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        jTableEmpleados.setRowHeight(30);
        jTableEmpleados.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jTableEmpleados.getTableHeader().setBackground(new java.awt.Color(34, 34, 38));
        jTableEmpleados.getTableHeader().setForeground(tonoOro);
        jTableEmpleados.setBackground(new java.awt.Color(34, 34, 38));
        jTableEmpleados.setForeground(java.awt.Color.WHITE);
        jTableEmpleados.setSelectionBackground(new java.awt.Color(60, 60, 65));
        jTableEmpleados.setSelectionForeground(tonoOro);

        jTableEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarEmpleadoDeTabla();
            }
        });

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(jTableEmpleados);
        scrollPane.getViewport().setBackground(casiNegro);
        scrollPane.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0),
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 70, 75), 3)
        ));
        
        javax.swing.JPanel panelTabla = new javax.swing.JPanel(new java.awt.BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.add(scrollPane, java.awt.BorderLayout.CENTER);

        javax.swing.JPanel mainSplit = new javax.swing.JPanel(new java.awt.BorderLayout());
        mainSplit.setOpaque(false);
        mainSplit.add(centro, java.awt.BorderLayout.WEST);
        mainSplit.add(panelTabla, java.awt.BorderLayout.CENTER);

        fondoMetalico.add(mainSplit, java.awt.BorderLayout.CENTER);
        setContentPane(fondoMetalico);
        revalidate();
        repaint();
        
        cargarEmpleados();
    }

    private void registrarEmpleado() {
        String nombre = jTNombre.getText().trim();
        String aPaterno = jTAPaterno.getText().trim();
        String aMaterno = jTAMaterno.getText().trim();
        String puesto = jCBPuesto.getSelectedItem().toString();
        String usuario = jTUsuario.getText().trim();
        String contrasena = new String(jPContrasena.getPassword());
        String cContrasena = new String(jPCContrasena.getPassword());
        String nombreCompleto = construirNombreCompleto(nombre, aPaterno, aMaterno);

        if (nombreCompleto.isEmpty() || puesto.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor llena todos los campos obligatorios.");
            return;
        }

        if (!contrasena.equals(cContrasena)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }

        try (java.sql.Connection con = DBConnection.getConnection()) {
            String sqlEmpleado = "INSERT INTO empleados (nombre, puesto, usuario, contrasena, estado_empleado) VALUES (?, ?, ?, ?, TRUE)";
            try (java.sql.PreparedStatement pst = con.prepareStatement(sqlEmpleado)) {
                pst.setString(1, nombreCompleto);
                pst.setString(2, puesto);
                pst.setString(3, usuario);
                pst.setString(4, contrasena);
                pst.executeUpdate();
            }

            javax.swing.JOptionPane.showMessageDialog(this, "Empleado y usuario registrados con éxito.");
            limpiarCampos();
        } catch (java.sql.SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
            logger.log(java.util.logging.Level.SEVERE, "Error de conexión", ex);
        }
    }

    private void cargarEmpleados() {
        modeloTabla.setRowCount(0);
        try (java.sql.Connection con = DBConnection.getConnection()) {
            String sql = "SELECT id_empleado, nombre, puesto, usuario FROM empleados ORDER BY id_empleado ASC";
            try (java.sql.PreparedStatement pst = con.prepareStatement(sql);
                 java.sql.ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String[] partesNombre = descomponerNombre(rs.getString("nombre"));
                    modeloTabla.addRow(new Object[]{
                        rs.getInt("id_empleado"),
                        partesNombre[0],
                        partesNombre[1],
                        partesNombre[2],
                        rs.getString("puesto"),
                        rs.getString("usuario")
                    });
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar empleados", ex);
        }
    }

    private void seleccionarEmpleadoDeTabla() {
        int fila = jTableEmpleados.getSelectedRow();
        if (fila == -1) return;
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        idEmpleadoActual = id;
        
        jTNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        jTAPaterno.setText((String) modeloTabla.getValueAt(fila, 2));
        jTAMaterno.setText((String) modeloTabla.getValueAt(fila, 3));
        String puesto = (String) modeloTabla.getValueAt(fila, 4);
        if (puesto != null) {
            jCBPuesto.setSelectedItem(puesto);
        }
        jTUsuario.setText((String) modeloTabla.getValueAt(fila, 5));
        jPContrasena.setText("");
        jPCContrasena.setText("");
        
        jLabel1.setText("EDITANDO ID: " + idEmpleadoActual);
        jBRegistrar.setEnabled(false);
    }

    private void actualizarEmpleado() {
        if (idEmpleadoActual == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Primero debe buscar un empleado para actualizar.");
            return;
        }
        
        String nombre = jTNombre.getText().trim();
        String aPaterno = jTAPaterno.getText().trim();
        String aMaterno = jTAMaterno.getText().trim();
        String puesto = jCBPuesto.getSelectedItem().toString();
        String usuario = jTUsuario.getText().trim();
        String contrasena = new String(jPContrasena.getPassword());
        String cContrasena = new String(jPCContrasena.getPassword());
        String nombreCompleto = construirNombreCompleto(nombre, aPaterno, aMaterno);

        if (nombreCompleto.isEmpty() || puesto.isEmpty() || usuario.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor llena los campos obligatorios (nombre, apellido, puesto, usuario).");
            return;
        }
        
        if (!contrasena.isEmpty() && !contrasena.equals(cContrasena)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }

        try (java.sql.Connection con = DBConnection.getConnection()) {
            try {
                if (!contrasena.isEmpty()) {
                    String sql = "UPDATE empleados SET nombre=?, puesto=?, usuario=?, contrasena=? WHERE id_empleado=?";
                    try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, nombreCompleto);
                        pst.setString(2, puesto);
                        pst.setString(3, usuario);
                        pst.setString(4, contrasena);
                        pst.setInt(5, idEmpleadoActual);
                        pst.executeUpdate();
                    }
                } else {
                    String sql = "UPDATE empleados SET nombre=?, puesto=?, usuario=? WHERE id_empleado=?";
                    try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, nombreCompleto);
                        pst.setString(2, puesto);
                        pst.setString(3, usuario);
                        pst.setInt(4, idEmpleadoActual);
                        pst.executeUpdate();
                    }
                }

                javax.swing.JOptionPane.showMessageDialog(this, "Empleado actualizado con éxito.");
                limpiarCampos();
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void eliminarEmpleado() {
        if (idEmpleadoActual == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Primero debe buscar un empleado para dar de baja.");
            return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "¿Está seguro que desea dar de baja (eliminar) a este empleado?", "Confirmar Baja", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
        
        try (java.sql.Connection con = DBConnection.getConnection()) {
            String sqlBaja = "UPDATE empleados SET estado_empleado = FALSE WHERE id_empleado=?";
            try (java.sql.PreparedStatement pst = con.prepareStatement(sqlBaja)) {
                pst.setInt(1, idEmpleadoActual);
                pst.executeUpdate();
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Empleado dado de baja con éxito.");
            limpiarCampos();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private String construirNombreCompleto(String nombre, String aPaterno, String aMaterno) {
        StringBuilder completo = new StringBuilder();
        if (nombre != null && !nombre.isBlank()) {
            completo.append(nombre.trim());
        }
        if (aPaterno != null && !aPaterno.isBlank()) {
            if (completo.length() > 0) completo.append(' ');
            completo.append(aPaterno.trim());
        }
        if (aMaterno != null && !aMaterno.isBlank()) {
            if (completo.length() > 0) completo.append(' ');
            completo.append(aMaterno.trim());
        }
        return completo.toString();
    }

    private String[] descomponerNombre(String nombreCompleto) {
        String[] partes = new String[]{"", "", ""};
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            return partes;
        }

        String[] tokens = nombreCompleto.trim().split("\\s+");
        if (tokens.length == 1) {
            partes[0] = tokens[0];
            return partes;
        }

        if (tokens.length == 2) {
            partes[0] = tokens[0];
            partes[1] = tokens[1];
            return partes;
        }

        partes[0] = tokens[0];
        partes[1] = tokens[1];
        StringBuilder restante = new StringBuilder();
        for (int i = 2; i < tokens.length; i++) {
            if (restante.length() > 0) restante.append(' ');
            restante.append(tokens[i]);
        }
        partes[2] = restante.toString();
        return partes;
    }

    private void limpiarCampos() {
        jTNombre.setText("");
        jTAPaterno.setText("");
        jTAMaterno.setText("");
        jCBPuesto.setSelectedIndex(0);
        jTUsuario.setText("");
        jPContrasena.setText("");
        jPCContrasena.setText("");
        
        idEmpleadoActual = -1;
        jLabel1.setText("CONTROL DE USUARIOS");
        jBRegistrar.setEnabled(true);
        if (jTableEmpleados != null) {
            jTableEmpleados.clearSelection();
        }
        cargarEmpleados();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLNombre = new javax.swing.JLabel();
        jTNombre = new javax.swing.JTextField();
        jLAPaterno = new javax.swing.JLabel();
        jTAPaterno = new javax.swing.JTextField();
        jLAMaterno = new javax.swing.JLabel();
        jTAMaterno = new javax.swing.JTextField();
        jLUsuario = new javax.swing.JLabel();
        jTUsuario = new javax.swing.JTextField();
        jLContrasena = new javax.swing.JLabel();
        jPContrasena = new javax.swing.JPasswordField();
        jLContrasena1 = new javax.swing.JLabel();
        jPCContrasena = new javax.swing.JPasswordField();
        jLAMaterno1 = new javax.swing.JLabel();
        jCBPuesto = new javax.swing.JComboBox<>();
        jBRegresar = new javax.swing.JButton();
        jBRegistrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CONTROL DE USUARIOS");

        jLNombre.setForeground(new java.awt.Color(255, 255, 255));
        jLNombre.setText("Nombre(s)");

        jLAPaterno.setForeground(new java.awt.Color(255, 255, 255));
        jLAPaterno.setText("Apellido Paterno:");

        jLAMaterno.setForeground(new java.awt.Color(255, 255, 255));
        jLAMaterno.setText("Apellido Materno:");

        jLUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jLUsuario.setText("Usuario");

        jLContrasena.setForeground(new java.awt.Color(255, 255, 255));
        jLContrasena.setText("Contraseña");

        jLContrasena1.setForeground(new java.awt.Color(255, 255, 255));
        jLContrasena1.setText("Confirmar contraseña:");

        jLAMaterno1.setForeground(new java.awt.Color(255, 255, 255));
        jLAMaterno1.setText("Puesto:");

        jCBPuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gerente", "Mesero", "Cocinero" }));

        jBRegresar.setText("REGRESAR");

        jBRegistrar.setText("REGISTRAR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLAMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTAMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLAPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTAPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPContrasena))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLContrasena1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPCContrasena)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(jBRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLAMaterno1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 32, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jBRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLNombre)
                    .addComponent(jTNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLAPaterno)
                    .addComponent(jTAPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLAMaterno)
                    .addComponent(jTAMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLAMaterno1)
                    .addComponent(jCBPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLUsuario)
                    .addComponent(jTUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLContrasena)
                    .addComponent(jPContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLContrasena1)
                    .addComponent(jPCContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jBRegresar)
                .addContainerGap())
        );

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
        /* Set FlatMacDarkLaf para mantener consistencia general del programa */
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
        java.awt.EventQueue.invokeLater(() -> new PSEmpleados().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBRegistrar;
    private javax.swing.JButton jBRegresar;
    private javax.swing.JComboBox<String> jCBPuesto;
    private javax.swing.JLabel jLAMaterno;
    private javax.swing.JLabel jLAMaterno1;
    private javax.swing.JLabel jLAPaterno;
    private javax.swing.JLabel jLContrasena;
    private javax.swing.JLabel jLContrasena1;
    private javax.swing.JLabel jLNombre;
    private javax.swing.JLabel jLUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField jPCContrasena;
    private javax.swing.JPasswordField jPContrasena;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTAMaterno;
    private javax.swing.JTextField jTAPaterno;
    private javax.swing.JTextField jTNombre;
    private javax.swing.JTextField jTUsuario;
    // End of variables declaration//GEN-END:variables
}
