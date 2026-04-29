package com.mycompany.perfectstrangers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class PSRecetas extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSRecetas.class.getName());

    private JComboBox<ProductoItem> cbProductos;
    private JComboBox<InsumoItem> cbInsumos;
    private JTextField txtCantidad;
    private JTextField txtDescripcionReceta;
    private JTable tablaReceta;
    private DefaultTableModel modeloTabla;

    public PSRecetas() {
        super("PerfectStrangers - Control de Recetas");
        initComponents();
        configurarVentana();
        cargarDatosIniciales();
    }

    private void configurarVentana() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            logger.warning("No se pudo cargar el icono de la ventana.");
        }
    }

    private void initComponents() {
        Color fondoNegro = new Color(20, 20, 22);
        Color tonoOro = new Color(204, 169, 90);
        Color metal = new Color(45, 45, 47);
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 10));
        panelPrincipal.setBackground(fondoNegro);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("CONTROL DE RECETAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(tonoOro);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel de Formulario (Izquierda)
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(tonoOro), "Editor de Receta", 0, 0, fontLabel, tonoOro
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Seleccionar Producto
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(crearLabel("Producto:", fontLabel, tonoOro), gbc);
        cbProductos = new JComboBox<>();
        estilizarComboBox(cbProductos, fontInput);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(cbProductos, gbc);
        
        // Fila 2: Descripción de la Receta
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.gridwidth = 1;
        panelFormulario.add(crearLabel("Descripción:", fontLabel, tonoOro), gbc);
        txtDescripcionReceta = new JTextField();
        estilizarTextField(txtDescripcionReceta, fontInput);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtDescripcionReceta, gbc);

        // Fila 3: Tabla de ingredientes
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 3;
        gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        modeloTabla = new DefaultTableModel(new Object[]{"ID Insumo", "Ingrediente", "Cantidad", "Unidad"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaReceta = new JTable(modeloTabla);
        estilizarTabla(tablaReceta, fontInput, metal, tonoOro);
        tablaReceta.getColumnModel().getColumn(0).setMinWidth(0);
        tablaReceta.getColumnModel().getColumn(0).setMaxWidth(0);
        JScrollPane scrollTabla = new JScrollPane(tablaReceta);
        scrollTabla.getViewport().setBackground(metal);
        scrollTabla.setPreferredSize(new Dimension(450, 300));
        panelFormulario.add(scrollTabla, gbc);

        // Fila 4: Agregar Insumo
        gbc.gridy = 3; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0; panelFormulario.add(crearLabel("Insumo:", fontLabel, tonoOro), gbc);
        cbInsumos = new JComboBox<>();
        estilizarComboBox(cbInsumos, fontInput);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(cbInsumos, gbc);

        // Fila 5: Cantidad
        gbc.gridy = 4;
        gbc.gridx = 0; panelFormulario.add(crearLabel("Cantidad:", fontLabel, tonoOro), gbc);
        txtCantidad = new JTextField();
        estilizarTextField(txtCantidad, fontInput);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFormulario.add(txtCantidad, gbc);

        // Fila 5: Botones de acción de la tabla
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 3;
        JPanel panelBotonesTabla = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));
        panelBotonesTabla.setOpaque(false);
        JButton btnAgregar = crearBoton("Agregar Insumo", tonoOro);
        JButton btnQuitar = crearBoton("Quitar Insumo", tonoOro);
        panelBotonesTabla.add(btnAgregar);
        panelBotonesTabla.add(btnQuitar);
        panelFormulario.add(panelBotonesTabla, gbc);

        // Fila 6: Botones principales
        gbc.gridy = 6;
        JPanel panelBotonesPrincipal = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 10));
        panelBotonesPrincipal.setOpaque(false);
        JButton btnGuardar = crearBoton("Guardar Receta", new Color(40, 167, 69));
        JButton btnRegresar = crearBoton("Regresar", tonoOro);
        panelBotonesPrincipal.add(btnGuardar);
        panelBotonesPrincipal.add(btnRegresar);
        panelFormulario.add(panelBotonesPrincipal, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        add(panelPrincipal);

        // Action Listeners
        cbProductos.addActionListener(e -> cargarRecetaSeleccionada());
        btnAgregar.addActionListener(e -> agregarIngredienteATabla());
        btnQuitar.addActionListener(e -> quitarIngredienteDeTabla());
        btnGuardar.addActionListener(e -> guardarReceta());
        btnRegresar.addActionListener(e -> { new PSMenu().setVisible(true); dispose(); });
    }

    private void cargarDatosIniciales() {
        try {
            // Cargar productos que pueden tener receta
            List<Producto> productos = ProductoDAO.obtenerTodosProductos();
            cbProductos.addItem(new ProductoItem(0, "Seleccione un producto..."));
            for (Producto p : productos) {
                if ("Platillo".equals(p.getCategoria()) || "Bebidas".equals(p.getCategoria())) {
                    cbProductos.addItem(new ProductoItem(p.getIdProducto(), p.getNombre()));
                }
            }

            // Cargar todos los insumos disponibles
            List<Insumo> insumos = InsumoDAO.obtenerTodosInsumos();
            for (Insumo i : insumos) {
                cbInsumos.addItem(new InsumoItem(i.getIdInsumo(), i.getNombreInsumo(), i.getUnidadMedida()));
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar datos iniciales para recetas", ex);
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarRecetaSeleccionada() {
        modeloTabla.setRowCount(0);
        txtDescripcionReceta.setText("");
        ProductoItem itemSeleccionado = (ProductoItem) cbProductos.getSelectedItem();
        if (itemSeleccionado == null || itemSeleccionado.getId() == 0) {
            return;
        }

        try {
            List<Receta> recetaActual = ServicioReceta.obtenerRecetaDeProducto(itemSeleccionado.getId());
            if (!recetaActual.isEmpty()) {
                txtDescripcionReceta.setText(recetaActual.get(0).getDescripcionReceta());
            }
            for (Receta ingrediente : recetaActual) {
                modeloTabla.addRow(new Object[]{
                    ingrediente.getIdInsumo(),
                    ingrediente.getNombreInsumo(),
                    ingrediente.getCantidadRequerida(),
                    ingrediente.getUnidadMedida()
                });
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar la receta del producto", ex);
            JOptionPane.showMessageDialog(this, "No se pudo cargar la receta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarIngredienteATabla() {
        InsumoItem insumoSeleccionado = (InsumoItem) cbInsumos.getSelectedItem();
        String cantidadStr = txtCantidad.getText().trim();

        if (insumoSeleccionado == null || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo e ingrese una cantidad.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double cantidad = Double.parseDouble(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Dato inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Evitar duplicados en la tabla visual
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if ((int) modeloTabla.getValueAt(i, 0) == insumoSeleccionado.getId()) {
                    JOptionPane.showMessageDialog(this, "El insumo ya está en la receta. Si quieres cambiar la cantidad, quítalo y vuelve a agregarlo.", "Insumo duplicado", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            modeloTabla.addRow(new Object[]{
                insumoSeleccionado.getId(),
                insumoSeleccionado.getNombre(),
                cantidad,
                insumoSeleccionado.getUnidad()
            });
            txtCantidad.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido.", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarIngredienteDeTabla() {
        int filaSeleccionada = tablaReceta.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ingrediente de la tabla para quitarlo.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modeloTabla.removeRow(filaSeleccionada);
    }

    private void guardarReceta() {
        ProductoItem productoSeleccionado = (ProductoItem) cbProductos.getSelectedItem();
        if (productoSeleccionado == null || productoSeleccionado.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto para guardar su receta.", "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String descripcion = txtDescripcionReceta.getText().trim();
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción de la receta es obligatoria.", "Dato incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Receta> nuevaReceta = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            int idInsumo = (int) modeloTabla.getValueAt(i, 0);
            double cantidad = (double) modeloTabla.getValueAt(i, 2);
            Receta ingrediente = new Receta(productoSeleccionado.getId(), idInsumo, cantidad);
            nuevaReceta.add(ingrediente);
        }

        try {
            ServicioReceta.guardarRecetaCompleta(productoSeleccionado.getId(), descripcion, nuevaReceta);
            JOptionPane.showMessageDialog(this, "Receta guardada correctamente para: " + productoSeleccionado.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al guardar la receta completa", ex);
            JOptionPane.showMessageDialog(this, "Error al guardar la receta en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Métodos de utilidad para la UI ---

    private JLabel crearLabel(String texto, Font font, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private void estilizarComboBox(JComboBox<?> cb, Font font) {
        cb.setFont(font);
        cb.setBackground(new Color(34, 34, 38));
        cb.setForeground(Color.WHITE);
        cb.setBorder(BorderFactory.createLineBorder(new Color(95, 95, 100)));
    }

    private void estilizarTextField(JTextField tf, Font font) {
        tf.setFont(font);
        tf.setBackground(new Color(34, 34, 38));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(95, 95, 100)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    private void estilizarTabla(JTable table, Font font, Color metal, Color oro) {
        table.setFont(font);
        table.setBackground(new Color(34, 34, 38));
        table.setForeground(Color.WHITE);
        table.setGridColor(metal);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(font.deriveFont(Font.BOLD));
        table.getTableHeader().setBackground(metal);
        table.getTableHeader().setForeground(oro);
        table.setSelectionBackground(oro.darker());
        table.setSelectionForeground(Color.WHITE);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(color.equals(new Color(40, 167, 69)) ? color : new Color(44, 44, 48));
        boton.setForeground(color.equals(new Color(40, 167, 69)) ? Color.WHITE : color);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(20, 20, 20), 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        return boton;
    }

    // --- Clases internas para los ComboBox ---

    private static class ProductoItem {
        private final int id;
        private final String nombre;

        public ProductoItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }

        @Override
        public String toString() {
            return nombre;
        }
    }

    private static class InsumoItem {
        private final int id;
        private final String nombre;
        private final String unidad;

        public InsumoItem(int id, String nombre, String unidad) {
            this.id = id;
            this.nombre = nombre;
            this.unidad = unidad;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getUnidad() { return unidad; }

        @Override
        public String toString() {
            return nombre + " (" + unidad + ")";
        }
    }

    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
        } catch (Exception ex) {
            logger.severe("Failed to initialize FlatLaf");
        }
        java.awt.EventQueue.invokeLater(() -> new PSRecetas().setVisible(true));
    }
}