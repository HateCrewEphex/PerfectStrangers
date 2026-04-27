package com.mycompany.perfectstrangers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

public class PSPromociones extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PSPromociones.class.getName());

    private JTextField txtIdPromo;
    private JTextField txtNombrePromo;
    private JComboBox<String> cbTipoDescuento;
    private JTextField txtValorDescuento;
    private JDateChooser dcFechaInicio;
    private JDateChooser dcFechaFin;
    
    private JComboBox<String> cbHoraInicioH;
    private JComboBox<String> cbHoraInicioM;
    private JComboBox<String> cbHoraFinH;
    private JComboBox<String> cbHoraFinM;
    
    private JCheckBox chkLunes;
    private JCheckBox chkMartes;
    private JCheckBox chkMiercoles;
    private JCheckBox chkJueves;
    private JCheckBox chkViernes;
    private JCheckBox chkSabado;
    private JCheckBox chkDomingo;

    private JComboBox<ProductoItem> cbProductoAfectado;
    private JCheckBox chkEstado;

    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnEliminar;
    private JButton btnRegresar;

    private JTable tablaPromos;
    private DefaultTableModel modeloTabla;

    public PSPromociones() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("PerfectStrangers - Control de Promociones");

        try {
            java.net.URL iconURL = getClass().getResource("/com/mycompany/perfectstrangers/icon.png");
            if (iconURL != null) {
                this.setIconImage(new javax.swing.ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            logger.warning("No se pudo cargar el icono.");
        }

        cargarProductos();
        cargarDatos();
    }

    private void initComponents() {
        Color fondoNegro = new Color(20, 20, 22);
        Color tonoOro = new Color(204, 169, 90);
        Color metal = new Color(45, 45, 47);
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(fondoNegro);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitulo = new JLabel("CONTROL DE PROMOCIONES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(tonoOro);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Formulario a la izquierda
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(metal);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(tonoOro, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Init components
        txtIdPromo = new JTextField(15);
        txtIdPromo.setEditable(false);
        txtNombrePromo = new JTextField(15);
        cbTipoDescuento = new JComboBox<>(new String[]{"Porcentaje", "Fijo", "2x1"});
        txtValorDescuento = new JTextField(15);
        
        dcFechaInicio = new JDateChooser();
        dcFechaInicio.setDateFormatString("yyyy-MM-dd");
        dcFechaInicio.setDate(new Date());

        dcFechaFin = new JDateChooser();
        dcFechaFin.setDateFormatString("yyyy-MM-dd");
        dcFechaFin.setDate(new Date());

        // Comboboxes para la hora
        cbHoraInicioH = new JComboBox<>();
        for(int i=0; i<24; i++) cbHoraInicioH.addItem(String.format("%02d", i));
        cbHoraInicioM = new JComboBox<>();
        for(int i=0; i<60; i++) cbHoraInicioM.addItem(String.format("%02d", i));
        
        cbHoraFinH = new JComboBox<>();
        for(int i=0; i<24; i++) cbHoraFinH.addItem(String.format("%02d", i));
        cbHoraFinM = new JComboBox<>();
        for(int i=0; i<60; i++) cbHoraFinM.addItem(String.format("%02d", i));

        JComboBox<?>[] combosHora = {cbHoraInicioH, cbHoraInicioM, cbHoraFinH, cbHoraFinM};
        for (JComboBox<?> cb : combosHora) {
            cb.setFont(fontInput);
            cb.setBackground(new Color(34, 34, 38));
            cb.setForeground(Color.WHITE);
        }
        
        JPanel pnlHoraInicio = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        pnlHoraInicio.setOpaque(false);
        pnlHoraInicio.add(cbHoraInicioH);
        JLabel lblSep1 = new JLabel(":"); lblSep1.setForeground(Color.WHITE); lblSep1.setFont(fontLabel);
        pnlHoraInicio.add(lblSep1);
        pnlHoraInicio.add(cbHoraInicioM);
        
        JPanel pnlHoraFin = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));
        pnlHoraFin.setOpaque(false);
        pnlHoraFin.add(cbHoraFinH);
        JLabel lblSep2 = new JLabel(":"); lblSep2.setForeground(Color.WHITE); lblSep2.setFont(fontLabel);
        pnlHoraFin.add(lblSep2);
        pnlHoraFin.add(cbHoraFinM);

        cbProductoAfectado = new JComboBox<>();
        chkEstado = new JCheckBox();
        chkEstado.setSelected(true);
        chkEstado.setOpaque(false);
        chkEstado.setForeground(Color.WHITE);

        // Panel de dias
        JPanel panelDias = new JPanel(new GridLayout(2, 4, 5, 5));
        panelDias.setOpaque(false);
        chkLunes = createDayCheck("Lunes");
        chkMartes = createDayCheck("Martes");
        chkMiercoles = createDayCheck("Miércoles");
        chkJueves = createDayCheck("Jueves");
        chkViernes = createDayCheck("Viernes");
        chkSabado = createDayCheck("Sábado");
        chkDomingo = createDayCheck("Domingo");
        panelDias.add(chkLunes);
        panelDias.add(chkMartes);
        panelDias.add(chkMiercoles);
        panelDias.add(chkJueves);
        panelDias.add(chkViernes);
        panelDias.add(chkSabado);
        panelDias.add(chkDomingo);

        String[] labels = {"Nombre:", "Tipo Descuento:", "Valor:", "Fecha Inicio:", "Fecha Fin:", "Hora Inicio:", "Hora Fin:", "Días:", "Producto Afectado (Opc.):", "Activa:"};
        java.awt.Component[] fields = {txtNombrePromo, cbTipoDescuento, txtValorDescuento, dcFechaInicio, dcFechaFin, pnlHoraInicio, pnlHoraFin, panelDias, cbProductoAfectado, chkEstado};

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(tonoOro);
            lbl.setFont(fontLabel);
            gbc.gridx = 0;
            gbc.gridy = i;
            panelFormulario.add(lbl, gbc);

            gbc.gridx = 1;
            if (fields[i] instanceof JTextField) {
                JTextField tf = (JTextField) fields[i];
                tf.setFont(fontInput);
                tf.setBackground(new Color(34, 34, 38));
                tf.setForeground(Color.WHITE);
                tf.setCaretColor(Color.WHITE);
                tf.setBorder(BorderFactory.createLineBorder(new Color(95, 95, 100)));
            } else if (fields[i] instanceof JComboBox) {
                JComboBox<?> cb = (JComboBox<?>) fields[i];
                cb.setFont(fontInput);
                cb.setBackground(new Color(34, 34, 38));
                cb.setForeground(Color.WHITE);
            } else if (fields[i] instanceof JDateChooser) {
                JDateChooser dc = (JDateChooser) fields[i];
                dc.setFont(fontInput);
                dc.setForeground(Color.WHITE);
                dc.setBackground(new Color(34, 34, 38));
                if (dc.getDateEditor().getUiComponent() instanceof JTextField) {
                    JTextField tfDate = (JTextField) dc.getDateEditor().getUiComponent();
                    tfDate.setBackground(new Color(34, 34, 38));
                    tfDate.setForeground(Color.WHITE);
                    tfDate.setCaretColor(Color.WHITE);
                    tfDate.setOpaque(true);
                    tfDate.setDisabledTextColor(Color.WHITE);
                    tfDate.setBorder(BorderFactory.createLineBorder(new Color(95, 95, 100)));
                }
                if (dc.getJCalendar() != null) {
                    aplicarTemaOscuroCalendario(dc.getJCalendar());
                    if (dc.getJCalendar().getDayChooser() != null) {
                        dc.getJCalendar().getDayChooser().setDecorationBackgroundColor(new Color(45, 45, 47));
                        dc.getJCalendar().getDayChooser().setSundayForeground(new Color(204, 169, 90));
                        dc.getJCalendar().getDayChooser().setWeekdayForeground(Color.WHITE);
                    }
                }
            }
            panelFormulario.add(fields[i], gbc);
        }

        // Botones
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false);
        
        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");
        btnEliminar = new JButton("Eliminar");
        btnRegresar = new JButton("Regresar");

        JButton[] botones = {btnGuardar, btnLimpiar, btnEliminar, btnRegresar};
        for (JButton btn : botones) {
            btn.setFont(fontLabel);
            btn.setBackground(new Color(34, 34, 38));
            btn.setForeground(tonoOro);
            btn.setFocusPainted(false);
        }

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.insets = new Insets(5, 5, 5, 5);
        gbcBtn.gridx = 0; gbcBtn.gridy = 0; panelBotones.add(btnGuardar, gbcBtn);
        gbcBtn.gridx = 1; gbcBtn.gridy = 0; panelBotones.add(btnLimpiar, gbcBtn);
        gbcBtn.gridx = 0; gbcBtn.gridy = 1; panelBotones.add(btnEliminar, gbcBtn);
        gbcBtn.gridx = 1; gbcBtn.gridy = 1; panelBotones.add(btnRegresar, gbcBtn);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(panelBotones, gbc);

        JScrollPane scrollFormulario = new JScrollPane(panelFormulario);
        scrollFormulario.setBorder(null);
        scrollFormulario.getViewport().setBackground(fondoNegro);
        panelPrincipal.add(scrollFormulario, BorderLayout.WEST);

        // Tabla a la derecha
        String[] columnas = {"ID", "Nombre", "Tipo", "Valor", "F.Inicio", "F.Fin", "H.Inicio", "H.Fin", "Prod.", "Activa"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPromos = new JTable(modeloTabla);
        // Ocultar la columna del ID (índice 0 en el modelo) para que el usuario no la vea
        tablaPromos.removeColumn(tablaPromos.getColumnModel().getColumn(0));
        
        tablaPromos.setFont(fontInput);
        tablaPromos.setBackground(new Color(34, 34, 38));
        tablaPromos.setForeground(Color.WHITE);
        tablaPromos.getTableHeader().setFont(fontLabel);
        tablaPromos.getTableHeader().setBackground(metal);
        tablaPromos.getTableHeader().setForeground(tonoOro);
        tablaPromos.setRowHeight(30);

        tablaPromos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaPromos.getSelectedRow() != -1) {
                cargarPromoSeleccionada();
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaPromos);
        scrollTabla.getViewport().setBackground(fondoNegro);
        scrollTabla.setBorder(BorderFactory.createLineBorder(tonoOro, 2));

        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);

        this.add(panelPrincipal);

        // Action Listeners
        btnGuardar.addActionListener(e -> guardarPromocion());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnEliminar.addActionListener(e -> eliminarPromocion());
        btnRegresar.addActionListener(e -> {
            new PSMenu().setVisible(true);
            this.dispose();
        });
    }

    private JCheckBox createDayCheck(String text) {
        JCheckBox chk = new JCheckBox(text, true);
        chk.setOpaque(false);
        chk.setForeground(Color.WHITE);
        chk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return chk;
    }

    private void cargarProductos() {
        cbProductoAfectado.removeAllItems();
        cbProductoAfectado.addItem(new ProductoItem(null, "Ninguno"));
        try {
            List<Producto> productos = ProductoDAO.obtenerTodosProductos();
            for (Producto p : productos) {
                if (p.getCategoria() != null && !p.getCategoria().equalsIgnoreCase("Combo")) {
                    cbProductoAfectado.addItem(new ProductoItem(p.getIdProducto(), p.getNombre()));
                }
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING, "Error al cargar los productos", e);
        }
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        try {
            List<Promocion> promociones = PromocionDAO.obtenerTodasPromociones();
            for (Promocion p : promociones) {
                Object[] fila = {
                    p.getIdPromocion(),
                    p.getNombrePromo(),
                    p.getTipoDescuento(),
                    p.getValorDescuento(),
                    p.getFechaInicio(),
                    p.getFechaFin(),
                    p.getHoraInicio(),
                    p.getHoraFin(),
                    p.getNombreProducto() != null ? p.getNombreProducto() : "Ninguno",
                    p.isEstadoPromo() ? "Sí" : "No"
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al cargar promociones", e);
            JOptionPane.showMessageDialog(this, "Error al cargar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPromoSeleccionada() {
        int fila = tablaPromos.getSelectedRow();
        if (fila != -1) {
            try {
                int modelRow = tablaPromos.convertRowIndexToModel(fila);
                int id = Integer.parseInt(modeloTabla.getValueAt(modelRow, 0).toString());
                Promocion p = PromocionDAO.obtenerPromocionById(id);
                if (p != null) {
                    txtIdPromo.setText(String.valueOf(p.getIdPromocion()));
                    txtNombrePromo.setText(p.getNombrePromo());
                    cbTipoDescuento.setSelectedItem(p.getTipoDescuento());
                    txtValorDescuento.setText(String.valueOf(p.getValorDescuento()));
                    
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        if(p.getFechaInicio() != null) dcFechaInicio.setDate(sdfDate.parse(p.getFechaInicio()));
                        if(p.getFechaFin() != null) dcFechaFin.setDate(sdfDate.parse(p.getFechaFin()));
                    } catch (Exception ex) {}

                    try {
                        if(p.getHoraInicio() != null) {
                            String[] parts = p.getHoraInicio().split(":");
                            cbHoraInicioH.setSelectedItem(parts[0]);
                            cbHoraInicioM.setSelectedItem(parts[1]);
                        }
                        if(p.getHoraFin() != null) {
                            String[] parts = p.getHoraFin().split(":");
                            cbHoraFinH.setSelectedItem(parts[0]);
                            cbHoraFinM.setSelectedItem(parts[1]);
                        }
                    } catch (Exception ex) {}

                    chkLunes.setSelected(p.isAplicaLunes());
                    chkMartes.setSelected(p.isAplicaMartes());
                    chkMiercoles.setSelected(p.isAplicaMiercoles());
                    chkJueves.setSelected(p.isAplicaJueves());
                    chkViernes.setSelected(p.isAplicaViernes());
                    chkSabado.setSelected(p.isAplicaSabado());
                    chkDomingo.setSelected(p.isAplicaDomingo());

                    // Seleccionar el producto correcto en el combobox
                    boolean encontrado = false;
                    for (int i = 0; i < cbProductoAfectado.getItemCount(); i++) {
                        ProductoItem item = cbProductoAfectado.getItemAt(i);
                        if (p.getIdProductoAfectado() == null && item.getId() == null) {
                            cbProductoAfectado.setSelectedIndex(i);
                            encontrado = true;
                            break;
                        } else if (p.getIdProductoAfectado() != null && item.getId() != null && 
                                   item.getId().equals(p.getIdProductoAfectado())) {
                            cbProductoAfectado.setSelectedIndex(i);
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        cbProductoAfectado.setSelectedIndex(0); // Volver a "Ninguno" por defecto
                    }

                    chkEstado.setSelected(p.isEstadoPromo());
                }
            } catch (Exception e) {
                logger.log(java.util.logging.Level.SEVERE, "Error al cargar promocion seleccionada", e);
            }
        }
    }

    private void limpiarCampos() {
        txtIdPromo.setText("");
        txtNombrePromo.setText("");
        cbTipoDescuento.setSelectedIndex(0);
        txtValorDescuento.setText("");
        
        dcFechaInicio.setDate(new Date());
        dcFechaFin.setDate(new Date());
        cbHoraInicioH.setSelectedIndex(0);
        cbHoraInicioM.setSelectedIndex(0);
        cbHoraFinH.setSelectedIndex(0);
        cbHoraFinM.setSelectedIndex(0);
        
        chkLunes.setSelected(true);
        chkMartes.setSelected(true);
        chkMiercoles.setSelected(true);
        chkJueves.setSelected(true);
        chkViernes.setSelected(true);
        chkSabado.setSelected(true);
        chkDomingo.setSelected(true);
        
        cbProductoAfectado.setSelectedIndex(0);
        chkEstado.setSelected(true);
        tablaPromos.clearSelection();
    }

    private void guardarPromocion() {
        try {
            Date dInicio = dcFechaInicio.getDate();
            Date dFin = dcFechaFin.getDate();
            
            if (txtNombrePromo.getText().trim().isEmpty() || txtValorDescuento.getText().trim().isEmpty() || 
                dInicio == null || dFin == null) {
                JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

            Promocion promo = new Promocion();
            if (!txtIdPromo.getText().isEmpty()) {
                promo.setIdPromocion(Integer.parseInt(txtIdPromo.getText()));
            }
            promo.setNombrePromo(txtNombrePromo.getText().trim());
            promo.setTipoDescuento(cbTipoDescuento.getSelectedItem().toString());
            promo.setValorDescuento(Double.parseDouble(txtValorDescuento.getText().trim()));
            promo.setFechaInicio(sdfDate.format(dInicio));
            promo.setFechaFin(sdfDate.format(dFin));
            
            String horaInicio = cbHoraInicioH.getSelectedItem() + ":" + cbHoraInicioM.getSelectedItem() + ":00";
            String horaFin = cbHoraFinH.getSelectedItem() + ":" + cbHoraFinM.getSelectedItem() + ":00";
            promo.setHoraInicio(horaInicio);
            promo.setHoraFin(horaFin);

            promo.setAplicaLunes(chkLunes.isSelected());
            promo.setAplicaMartes(chkMartes.isSelected());
            promo.setAplicaMiercoles(chkMiercoles.isSelected());
            promo.setAplicaJueves(chkJueves.isSelected());
            promo.setAplicaViernes(chkViernes.isSelected());
            promo.setAplicaSabado(chkSabado.isSelected());
            promo.setAplicaDomingo(chkDomingo.isSelected());
            
            ProductoItem item = (ProductoItem) cbProductoAfectado.getSelectedItem();
            if (item != null && item.getId() != null) {
                promo.setIdProductoAfectado(item.getId());
            } else {
                promo.setIdProductoAfectado(null);
            }
            
            promo.setEstadoPromo(chkEstado.isSelected());

            if (txtIdPromo.getText().isEmpty()) {
                PromocionDAO.crearPromocion(promo);
                JOptionPane.showMessageDialog(this, "Promoción creada correctamente.");
            } else {
                PromocionDAO.actualizarPromocion(promo);
                JOptionPane.showMessageDialog(this, "Promoción actualizada correctamente.");
            }
            
            limpiarCampos();
            cargarDatos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error en el formato de los números (Valor).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al guardar promoción", ex);
            JOptionPane.showMessageDialog(this, "Error al guardar la promoción.\nAsegúrese de que las fechas y horas sean válidas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPromocion() {
        if (txtIdPromo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una promoción de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta promoción?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idPromo = Integer.parseInt(txtIdPromo.getText());
                PromocionDAO.eliminarPromocion(idPromo);
                JOptionPane.showMessageDialog(this, "Promoción eliminada.");
                limpiarCampos();
                cargarDatos();
            } catch (Exception e) {
                logger.log(java.util.logging.Level.SEVERE, "Error al eliminar promoción", e);
                JOptionPane.showMessageDialog(this, "Error al eliminar la promoción.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void aplicarTemaOscuroCalendario(java.awt.Component c) {
        if (c != null) {
            c.setBackground(new Color(34, 34, 38));
            c.setForeground(Color.WHITE);
            if (c instanceof javax.swing.JComponent) {
                ((javax.swing.JComponent) c).setOpaque(true);
            }
            if (c instanceof java.awt.Container) {
                for (java.awt.Component child : ((java.awt.Container) c).getComponents()) {
                    aplicarTemaOscuroCalendario(child);
                }
            }
        }
    }

    // Clase auxiliar para el ComboBox de productos
    private class ProductoItem {
        private Integer id;
        private String nombre;

        public ProductoItem(Integer id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Integer getId() {
            return id;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}
