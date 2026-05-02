package com.mycompany.perfectstrangers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * Diálogo para seleccionar uno o varios platillos a los que se aplicará una promoción genérica.
 */
public class DialogoSeleccionarPlatillosPromo extends JDialog {
    private final List<Integer> indicesSeleccionados = new ArrayList<>();
    private boolean confirmado = false;

    public static final class ItemPlatilloPromo {
        private final int filaOrden;
        private final String etiqueta;
        private final String categoria;

        public ItemPlatilloPromo(int filaOrden, String etiqueta, String categoria) {
            this.filaOrden = filaOrden;
            this.etiqueta = etiqueta;
            this.categoria = categoria != null ? categoria : "Sin categoría";
        }

        public int getFilaOrden() {
            return filaOrden;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public String getCategoria() {
            return categoria;
        }

        @Override
        public String toString() {
            return etiqueta + " [" + categoria + "]";
        }
    }

    public DialogoSeleccionarPlatillosPromo(Frame owner, Promocion promo, List<ItemPlatilloPromo> platillos) {
        super(owner, "Seleccionar platillos para promoción", true);
        setSize(520, 420);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents(promo, platillos);
    }

    private void initComponents(Promocion promo, List<ItemPlatilloPromo> platillos) {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel etiqueta = new JLabel("Selecciona uno o varios platillos para aplicar la promoción " + promo.getNombrePromo() + ":");
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD));
        mainPanel.add(etiqueta, BorderLayout.NORTH);

        DefaultListModel<ItemPlatilloPromo> model = new DefaultListModel<>();
        for (ItemPlatilloPromo platillo : platillos) {
            model.addElement(platillo);
        }

        JList<ItemPlatilloPromo> listaPlatillos = new JList<>(model);
        listaPlatillos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaPlatillos.setVisibleRowCount(10);

        Set<String> categorias = new LinkedHashSet<>();
        categorias.add("Todas");
        for (ItemPlatilloPromo platillo : platillos) {
            categorias.add(platillo.getCategoria());
        }

        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JComboBox<String> cbCategoria = new JComboBox<>(categorias.toArray(String[]::new));
        JButton btnSeleccionarCategoria = new JButton("Seleccionar por categoría");
        filtroPanel.add(new JLabel("Categoría:"));
        filtroPanel.add(cbCategoria);
        filtroPanel.add(btnSeleccionarCategoria);
        centerPanel.add(filtroPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(listaPlatillos), BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton btnSeleccionarTodo = new JButton("Seleccionar todo");
        JButton btnDesmarcarSeleccionados = new JButton("Desmarcar seleccionados");
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSeleccionarTodo.addActionListener(e -> {
            if (!platillos.isEmpty()) {
                listaPlatillos.setSelectionInterval(0, platillos.size() - 1);
            }
        });

        btnDesmarcarSeleccionados.addActionListener(e -> listaPlatillos.removeSelectionInterval(
            listaPlatillos.getMinSelectionIndex(),
            listaPlatillos.getMaxSelectionIndex()
        ));

        btnSeleccionarCategoria.addActionListener(e -> {
            String categoria = String.valueOf(cbCategoria.getSelectedItem());
            listaPlatillos.clearSelection();
            if ("Todas".equalsIgnoreCase(categoria)) {
                if (!platillos.isEmpty()) {
                    listaPlatillos.setSelectionInterval(0, platillos.size() - 1);
                }
                return;
            }

            int inicio = -1;
            int fin = -1;
            for (int i = 0; i < platillos.size(); i++) {
                ItemPlatilloPromo item = platillos.get(i);
                if (categoria.equalsIgnoreCase(item.getCategoria())) {
                    if (inicio == -1) {
                        inicio = i;
                    }
                    fin = i;
                }
            }

            if (inicio != -1) {
                listaPlatillos.setSelectionInterval(inicio, fin);
            }
        });

        btnAceptar.addActionListener(e -> {
            int[] seleccionados = listaPlatillos.getSelectedIndices();
            if (seleccionados.length == 0) {
                JOptionPane.showMessageDialog(this, "Selecciona al menos un platillo.", "Selección requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            indicesSeleccionados.clear();
            for (int indice : seleccionados) {
                indicesSeleccionados.add(platillos.get(indice).getFilaOrden());
            }
            confirmado = true;
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            confirmado = false;
            indicesSeleccionados.clear();
            dispose();
        });

        bottomPanel.add(btnSeleccionarTodo);
        bottomPanel.add(btnDesmarcarSeleccionados);
        bottomPanel.add(btnAceptar);
        bottomPanel.add(btnCancelar);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    public boolean fueConfirmado() {
        return confirmado;
    }

    public List<Integer> getIndicesSeleccionados() {
        return Collections.unmodifiableList(indicesSeleccionados);
    }
}
