/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.perfectstrangers;

/**
 *
 * @author Ephex
 */
public class PSPlatillos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSPlatillos.class.getName());
    private int idProductoActual = -1;
    private javax.swing.JTable jTableProductos;
    private javax.swing.table.DefaultTableModel modeloTabla;

    /**
     * Creates new form PSPlatillos
     */
    public PSPlatillos() {
        initComponents();
        
        // Estilos
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        java.awt.Font fontLabels = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
        java.awt.Font fontInputs = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24)); // Título principal más grande
        
        jLabel2.setFont(fontLabels);
        jLabel3.setFont(fontLabels);
        jLabel4.setFont(fontLabels);
        jLabel5.setFont(fontLabels);
        
        jTNomPlatillo.setFont(fontInputs);
        jTPrecio.setFont(fontInputs);
        jComboBox1.setFont(fontInputs);
        jComboBox2.setFont(fontInputs);
        
        jBGuardar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        jButton1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jButton1.setBackground(null);
        jButton1.setForeground(java.awt.Color.WHITE);
        
        jBAltaPlatillo.setFont(fontLabels);
        jBActPlatillo.setFont(fontLabels);
        jBEliPlatillo.setFont(fontLabels);
        
        // Configurar ComboBox de categoría real en el nuevo esquema
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Platillos", "Bebidas" }));
        
        jBGuardar.setText("REGISTRAR");
        jBGuardar.setBackground(new java.awt.Color(33, 122, 79));
        jBGuardar.addActionListener(e -> registrarProducto());
        jBGuardar.setForeground(java.awt.Color.WHITE);
        
        // Boton regresar
        jButton1.addActionListener(e -> {
            PSMenu menu = new PSMenu();
            menu.setVisible(true);
            dispose();
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

        jPPrincipal.setOpaque(false);
        jPPlatillos.setBackground(metal);
        jPPlatillos.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 80, 85), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        jLabel1.setForeground(tonoOro);
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));

        javax.swing.JLabel[] labels = {jLabel2, jLabel3, jLabel4, jLabel5};
        for (javax.swing.JLabel label : labels) {
            label.setForeground(tonoOro);
            label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        }

        javax.swing.JComponent[] campos = {jTNomPlatillo, jTPrecio, jComboBox1, jComboBox2};
        for (javax.swing.JComponent campo : campos) {
            campo.setBackground(new java.awt.Color(34, 34, 38));
            campo.setForeground(java.awt.Color.WHITE);
            campo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            campo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(95, 95, 100), 1),
                javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }
        jTNomPlatillo.setCaretColor(java.awt.Color.WHITE);
        jTPrecio.setCaretColor(java.awt.Color.WHITE);

        jButton1.setBackground(new java.awt.Color(44, 44, 48));
        jButton1.setForeground(tonoOro);
        jButton1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        jButton1.setFocusPainted(false);

        aplicarEstiloBotonModo(jBAltaPlatillo);
        aplicarEstiloBotonModo(jBActPlatillo);
        aplicarEstiloBotonModo(jBEliPlatillo);

        jBGuardar.setFocusPainted(false);
        jBGuardar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(10, 24, 10, 24)
        ));

        tarjeta.add(jPPrincipal, java.awt.BorderLayout.CENTER);
        
        // --- BOTONES EXTRA PARA CRUD ---
        javax.swing.JPanel panelBotonesExtra = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));
        panelBotonesExtra.setOpaque(false);
        
        javax.swing.JButton jBNuevo = new javax.swing.JButton("NUEVO");
        javax.swing.JButton jBActualizar = new javax.swing.JButton("ACTUALIZAR");
        javax.swing.JButton jBEliminar = new javax.swing.JButton("ELIMINAR");
        
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
        jBActualizar.addActionListener(e -> actualizarProducto());
        jBEliminar.addActionListener(e -> eliminarProducto());
        
        tarjeta.add(panelBotonesExtra, java.awt.BorderLayout.SOUTH);
        
        centro.add(tarjeta);
        
        // --- TABLA DE PRODUCTOS ---
        modeloTabla = new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Categoría", "Nombre", "Precio", "Es Combo"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableProductos = new javax.swing.JTable(modeloTabla);
        jTableProductos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        jTableProductos.setRowHeight(30);
        jTableProductos.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jTableProductos.getTableHeader().setBackground(new java.awt.Color(34, 34, 38));
        jTableProductos.getTableHeader().setForeground(tonoOro);
        jTableProductos.setBackground(new java.awt.Color(34, 34, 38));
        jTableProductos.setForeground(java.awt.Color.WHITE);
        jTableProductos.setSelectionBackground(new java.awt.Color(60, 60, 65));
        jTableProductos.setSelectionForeground(tonoOro);

        jTableProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarProductoDeTabla();
            }
        });

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(jTableProductos);
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
        
        jBAltaPlatillo.setVisible(false);
        jBActPlatillo.setVisible(false);
        jBEliPlatillo.setVisible(false);
        
        jLabel2.setText("ES COMBO:");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Sí" }));
        
        limpiarCampos();
    }

    private void aplicarEstiloBotonModo(javax.swing.JButton boton) {
        boton.setForeground(java.awt.Color.WHITE);
        boton.setBackground(new java.awt.Color(44, 44, 48));
        boton.setFocusPainted(false);
        boton.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
    }

    private void resaltarModo(javax.swing.JButton seleccionado) {}
    private void setModo(String modo) {}
    private void cargarIdsPlatillos() {}
    private void cargarDatosPlatillo() {}
    private void ejecutarAccion() {}

    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        try (java.sql.Connection con = DBConnection.getConnection()) {
            String sql = "SELECT id_producto, categoria, nombre, precio, es_combo FROM productos ORDER BY id_producto ASC";
            try (java.sql.PreparedStatement pst = con.prepareStatement(sql);
                 java.sql.ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                        rs.getInt("id_producto"),
                        rs.getString("categoria"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getBoolean("es_combo") ? "Sí" : "No"
                    });
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar productos", ex);
        }
    }

    private void seleccionarProductoDeTabla() {
        int fila = jTableProductos.getSelectedRow();
        if (fila == -1) return;
        
        int id = (int) modeloTabla.getValueAt(fila, 0);
        idProductoActual = id;
        
        jComboBox2.setSelectedItem((String) modeloTabla.getValueAt(fila, 1));
        jTNomPlatillo.setText((String) modeloTabla.getValueAt(fila, 2));
        jTPrecio.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        jComboBox1.setSelectedItem((String) modeloTabla.getValueAt(fila, 4));
        
        jLabel1.setText("EDITANDO ID: " + idProductoActual);
        jBGuardar.setEnabled(false);
    }

    private void registrarProducto() {
        String nombre = jTNomPlatillo.getText().trim();
        String categoria = jComboBox2.getSelectedItem() != null ? jComboBox2.getSelectedItem().toString() : "Platillos";
        String precioStr = jTPrecio.getText().trim();
        boolean esCombo = "Sí".equals(jComboBox1.getSelectedItem());

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor llena todos los campos.");
            return;
        }

        double precio = 0;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.");
            return;
        }

        try (java.sql.Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO productos (categoria, nombre, precio, es_combo) VALUES (?, ?, ?, ?)";
            try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, categoria);
                pst.setString(2, nombre);
                pst.setDouble(3, precio);
                pst.setBoolean(4, esCombo);
                pst.executeUpdate();
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Producto registrado con éxito.");
            limpiarCampos();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
        }
    }

    private void actualizarProducto() {
        if (idProductoActual == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Primero debe buscar o seleccionar un producto para actualizar.");
            return;
        }
        
        String nombre = jTNomPlatillo.getText().trim();
        String categoria = jComboBox2.getSelectedItem() != null ? jComboBox2.getSelectedItem().toString() : "Platillos";
        String precioStr = jTPrecio.getText().trim();
        boolean esCombo = "Sí".equals(jComboBox1.getSelectedItem());

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor llena todos los campos.");
            return;
        }
        
        double precio = 0;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.");
            return;
        }

        try (java.sql.Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE productos SET categoria=?, nombre=?, precio=?, es_combo=? WHERE id_producto=?";
            try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, categoria);
                pst.setString(2, nombre);
                pst.setDouble(3, precio);
                pst.setBoolean(4, esCombo);
                pst.setInt(5, idProductoActual);
                pst.executeUpdate();
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Producto actualizado con éxito.");
            limpiarCampos();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminarProducto() {
        if (idProductoActual == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Primero debe seleccionar un producto para dar de baja.");
            return;
        }
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este producto?", "Confirmar Baja", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;
        
        try (java.sql.Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM productos WHERE id_producto=?";
            try (java.sql.PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, idProductoActual);
                pst.executeUpdate();
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Producto eliminado con éxito.");
            limpiarCampos();
        } catch (Exception ex) {
            if (ex.getMessage().contains("foreign key") || ex.getMessage().contains("constraint")) {
                javax.swing.JOptionPane.showMessageDialog(this, "No se puede eliminar el producto porque tiene registros asociados (ej. órdenes).");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        jTNomPlatillo.setText("");
        if(jComboBox2.getItemCount() > 0) jComboBox2.setSelectedIndex(0);
        if(jComboBox1.getItemCount() > 0) jComboBox1.setSelectedIndex(0);
        jTPrecio.setText("");
        
        idProductoActual = -1;
        jLabel1.setText("CONTROL DE PLATILLOS");
        jBGuardar.setEnabled(true);
        if (jTableProductos != null) {
            jTableProductos.clearSelection();
        }
        cargarProductos();
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
        jLabel1 = new javax.swing.JLabel();
        jBAltaPlatillo = new javax.swing.JButton();
        jBActPlatillo = new javax.swing.JButton();
        jBEliPlatillo = new javax.swing.JButton();
        jPPlatillos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jTNomPlatillo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTPrecio = new javax.swing.JTextField();
        jBGuardar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPPrincipal.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PLATILLOS");

        jBAltaPlatillo.setText("NUEVO");

        jBActPlatillo.setText("ACTUALIZAR");

        jBEliPlatillo.setText("ELIMINAR");

        jPPlatillos.setBackground(new java.awt.Color(51, 51, 51));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID PLATILLO:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NOMBRE:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TIPO ALIMENTO:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("PRECIO:");

        jBGuardar.setText("GUARDAR");

        javax.swing.GroupLayout jPPlatillosLayout = new javax.swing.GroupLayout(jPPlatillos);
        jPPlatillos.setLayout(jPPlatillosLayout);
        jPPlatillosLayout.setHorizontalGroup(
            jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPlatillosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPlatillosLayout.createSequentialGroup()
                        .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPPlatillosLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPPlatillosLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTNomPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPPlatillosLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(73, 73, 73)
                        .addComponent(jTPrecio)))
                .addContainerGap())
            .addGroup(jPPlatillosLayout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(jBGuardar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPPlatillosLayout.setVerticalGroup(
            jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPlatillosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTNomPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPPlatillosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jBGuardar)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jButton1.setText("REGRESAR");

        javax.swing.GroupLayout jPPrincipalLayout = new javax.swing.GroupLayout(jPPrincipal);
        jPPrincipal.setLayout(jPPrincipalLayout);
        jPPrincipalLayout.setHorizontalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPPlatillos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPPrincipalLayout.createSequentialGroup()
                        .addComponent(jBAltaPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jBActPlatillo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBEliPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPrincipalLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPPrincipalLayout.setVerticalGroup(
            jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBAltaPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBEliPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBActPlatillo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPPlatillos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        java.awt.EventQueue.invokeLater(() -> new PSPlatillos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBActPlatillo;
    private javax.swing.JButton jBAltaPlatillo;
    private javax.swing.JButton jBEliPlatillo;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPPlatillos;
    private javax.swing.JPanel jPPrincipal;
    private javax.swing.JTextField jTNomPlatillo;
    private javax.swing.JTextField jTPrecio;
    // End of variables declaration//GEN-END:variables
}
