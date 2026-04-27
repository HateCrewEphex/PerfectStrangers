package com.mycompany.perfectstrangers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class DialogoInventarioMovimiento extends JDialog {

    public enum Modo {
        NUEVO,
        EXISTENTE
    }

    private static final Logger logger = Logger.getLogger(DialogoInventarioMovimiento.class.getName());
    private static final Color COLOR_FONDO = new Color(18, 18, 20);
    private static final Color COLOR_PANEL = new Color(30, 30, 34);
    private static final Color COLOR_CAMPO = new Color(36, 36, 40);
    private static final Color COLOR_ORO = new Color(204, 169, 90);
    private static final Color COLOR_TEXTO = new Color(240, 240, 240);
    private static final Color COLOR_BORDE = new Color(70, 70, 75);

    private final Modo modo;
    private final List<Insumo> insumosDisponibles;
    private boolean guardado;

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtTipo;
    private JTextField txtUbicacion;
    private JTextField txtMedicion;
    private JTextField txtCodBarras;
    private JTextField txtStock;
    private JTextField txtAlerta;
    private JTextField txtCritico;
    private JTextField txtNombreEmpaqueNuevo;
    private JTextField txtCantidadEmpaqueNuevo;
    private JTextField txtCodBarrasEmpaqueNuevo;

    private JComboBox<String> cboInsumos;
    private JTextField txtCodigoEmpaque;
    private JTextField txtPaquetes;
    private JTextField txtNombreEmpaqueExistente;
    private JTextField txtCantidadEmpaqueExistente;
    private JTextField txtCodBarrasEmpaqueExistente;
    private JLabel lblResultado;

    private DialogoInventarioMovimiento(Frame owner, Modo modo, List<Insumo> insumosDisponibles) {
        super(owner, true);
        this.modo = modo;
        this.insumosDisponibles = insumosDisponibles;
        this.guardado = false;
        configurarVentana();
    }

    public static boolean mostrarNuevoInsumo(Frame owner) {
        DialogoInventarioMovimiento dialogo = new DialogoInventarioMovimiento(owner, Modo.NUEVO, null);
        dialogo.setVisible(true);
        return dialogo.guardado;
    }

    public static boolean mostrarEntradaExistente(Frame owner) {
        try {
            List<Insumo> insumos = InsumoDAO.obtenerTodosInsumos();
            if (insumos.isEmpty()) {
                JOptionPane.showMessageDialog(owner, "No hay insumos registrados.");
                return false;
            }

            DialogoInventarioMovimiento dialogo = new DialogoInventarioMovimiento(owner, Modo.EXISTENTE, insumos);
            dialogo.setVisible(true);
            return dialogo.guardado;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al abrir la entrada existente", ex);
            JOptionPane.showMessageDialog(owner, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void configurarVentana() {
        setTitle(modo == Modo.NUEVO ? "Nuevo insumo" : "Entrada a insumo existente");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setContentPane(modo == Modo.NUEVO ? construirPanelNuevo() : construirPanelExistente());
        pack();
        setMinimumSize(new Dimension(760, getHeight()));
        setLocationRelativeTo(getOwner());
    }

    private JPanel construirPanelNuevo() {
        txtNombre = crearCampo();
        txtDescripcion = crearCampo();
        txtTipo = crearCampo("Abarrotes");
        txtUbicacion = crearCampo();
        txtMedicion = crearCampo("Pieza");
        txtCodBarras = crearCampo();
        txtStock = crearCampo();
        txtAlerta = crearCampo();
        txtCritico = crearCampo();
        txtNombreEmpaqueNuevo = crearCampo("Caja");
        txtCantidadEmpaqueNuevo = crearCampo();
        txtCodBarrasEmpaqueNuevo = crearCampo();

        JPanel panelInsumo = crearPanelFormulario("Datos del insumo");
        panelInsumo.add(crearEtiqueta("Nombre insumo"));
        panelInsumo.add(txtNombre);
        panelInsumo.add(crearEtiqueta("Descripción"));
        panelInsumo.add(txtDescripcion);
        panelInsumo.add(crearEtiqueta("Tipo producto"));
        panelInsumo.add(txtTipo);
        panelInsumo.add(crearEtiqueta("Ubicación"));
        panelInsumo.add(txtUbicacion);
        panelInsumo.add(crearEtiqueta("Medición"));
        panelInsumo.add(txtMedicion);
        panelInsumo.add(crearEtiqueta("Código de barras"));
        panelInsumo.add(txtCodBarras);
        panelInsumo.add(crearEtiqueta("Stock inicial"));
        panelInsumo.add(txtStock);
        panelInsumo.add(crearEtiqueta("Stock alerta"));
        panelInsumo.add(txtAlerta);
        panelInsumo.add(crearEtiqueta("Stock crítico"));
        panelInsumo.add(txtCritico);

        JPanel panelEmpaque = crearPanelFormulario("Datos del empaque");
        panelEmpaque.add(crearEtiqueta("Nombre empaque"));
        panelEmpaque.add(txtNombreEmpaqueNuevo);
        panelEmpaque.add(crearEtiqueta("Cantidad que trae"));
        panelEmpaque.add(txtCantidadEmpaqueNuevo);
        panelEmpaque.add(crearEtiqueta("Código barras empaque"));
        panelEmpaque.add(txtCodBarrasEmpaqueNuevo);

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setOpaque(false);
        contenido.add(panelInsumo, BorderLayout.CENTER);
        contenido.add(panelEmpaque, BorderLayout.SOUTH);

        return envolverPanel(contenido, this::guardarNuevoInsumo);
    }

    private JPanel construirPanelExistente() {
        cboInsumos = new JComboBox<>();
        for (Insumo insumo : insumosDisponibles) {
            cboInsumos.addItem(insumo.getIdIngrediente() + " - " + insumo.getNombreInsumo());
        }
        estilizarCombo(cboInsumos);

        txtCodigoEmpaque = crearCampo();
        txtPaquetes = crearCampo();
        txtNombreEmpaqueExistente = crearCampo("Caja");
        txtCantidadEmpaqueExistente = crearCampo();
        txtCodBarrasEmpaqueExistente = crearCampo();
        lblResultado = crearEtiqueta("Escanea el código o usa captura manual.");
        lblResultado.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton btnBuscarCodigo = new JButton("Buscar código");
        JButton btnUsarManual = new JButton("Usar manual");
        estilizarBotonSecundario(btnBuscarCodigo);
        estilizarBotonSecundario(btnUsarManual);
        btnBuscarCodigo.addActionListener(evt -> procesarCodigo(txtCodigoEmpaque.getText().trim()));
        btnUsarManual.addActionListener(evt -> lblResultado.setText("Modo manual activo: completa los campos y confirma."));

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 8));
        panelBusqueda.setOpaque(false);
        JPanel panelCodigo = new JPanel(new GridLayout(1, 2, 8, 8));
        panelCodigo.setOpaque(false);
        panelCodigo.add(crearEtiqueta("Código barras empaque"));
        panelCodigo.add(txtCodigoEmpaque);

        JPanel panelAcciones = new JPanel(new GridLayout(1, 2, 8, 8));
        panelAcciones.setOpaque(false);
        panelAcciones.add(btnBuscarCodigo);
        panelAcciones.add(btnUsarManual);
        panelBusqueda.add(panelCodigo, BorderLayout.CENTER);
        panelBusqueda.add(panelAcciones, BorderLayout.EAST);

        JPanel panel = crearPanelFormulario("Entrada de inventario");
        panel.add(crearEtiqueta("Insumo"));
        panel.add(cboInsumos);
        panel.add(crearEtiqueta("Paquetes a sumar"));
        panel.add(txtPaquetes);
        panel.add(crearEtiqueta("Nombre empaque"));
        panel.add(txtNombreEmpaqueExistente);
        panel.add(crearEtiqueta("Cantidad que trae"));
        panel.add(txtCantidadEmpaqueExistente);
        panel.add(crearEtiqueta("Código barras empaque"));
        panel.add(txtCodBarrasEmpaqueExistente);
        panel.add(crearEtiqueta("Estado"));
        panel.add(lblResultado);

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setOpaque(false);
        contenido.add(panelBusqueda, BorderLayout.NORTH);
        contenido.add(panel, BorderLayout.CENTER);
        return envolverPanel(contenido, this::guardarEntradaExistente);
    }

    private JPanel envolverPanel(JPanel contenido, Runnable accionGuardar) {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(COLOR_FONDO);
        raiz.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(COLOR_BORDE, 1),
            javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel titulo = new JLabel(modo == Modo.NUEVO ? "Alta de Insumo" : "Entrada a Existente", JLabel.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_ORO);
        raiz.add(titulo, BorderLayout.NORTH);

        raiz.add(contenido, BorderLayout.CENTER);
        raiz.add(crearBarraAcciones(accionGuardar), BorderLayout.SOUTH);
        return raiz;
    }

    private JPanel crearBarraAcciones(Runnable accionGuardar) {
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        estilizarBotonPrimario(btnGuardar);
        estilizarBotonSecundario(btnCancelar);
        btnGuardar.addActionListener(evt -> accionGuardar.run());
        btnCancelar.addActionListener(evt -> dispose());

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        barra.setOpaque(false);
        barra.add(btnGuardar);
        barra.add(btnCancelar);
        getRootPane().setDefaultButton(btnGuardar);
        return barra;
    }

    private JPanel crearPanelFormulario(String titulo) {
        Border borde = javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(COLOR_BORDE, 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 8));
        panel.setOpaque(true);
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(
            borde,
            titulo,
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13),
            COLOR_ORO
        ));
        return panel;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiqueta.setForeground(COLOR_TEXTO);
        return etiqueta;
    }

    private JTextField crearCampo() {
        return crearCampo("");
    }

    private JTextField crearCampo(String valorInicial) {
        JTextField campo = new JTextField(valorInicial);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(COLOR_TEXTO);
        campo.setBackground(COLOR_CAMPO);
        campo.setCaretColor(COLOR_ORO);
        campo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(COLOR_BORDE, 1),
            javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return campo;
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setForeground(COLOR_TEXTO);
        combo.setBackground(COLOR_CAMPO);
        combo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(COLOR_BORDE, 1),
            javax.swing.BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));
    }

    private void estilizarBotonPrimario(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(new Color(44, 44, 48));
        boton.setForeground(COLOR_ORO);
        boton.setFocusPainted(false);
        boton.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(20, 20, 20), 2),
            javax.swing.BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
    }

    private void estilizarBotonSecundario(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(COLOR_CAMPO);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(COLOR_BORDE, 1),
            javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
    }

    private void guardarNuevoInsumo() {
        try {
            Insumo insumo = new Insumo();
            insumo.setNombreInsumo(obtenerTexto(txtNombre, "Nombre insumo"));
            insumo.setDescripcion(obtenerTexto(txtDescripcion, "Descripción"));
            insumo.setTipoProducto(normalizarTipoProducto(obtenerTexto(txtTipo, "Tipo producto")));
            insumo.setUbicacion(obtenerTexto(txtUbicacion, "Ubicación"));
            insumo.setUnidadMedida(normalizarMedicion(obtenerTexto(txtMedicion, "Medición")));
            insumo.setCodigoBarras(txtCodBarras.getText().trim());
            insumo.setCantidadActual(obtenerNumero(txtStock, "Stock inicial"));
            insumo.setCantidadMinima(obtenerNumero(txtAlerta, "Stock alerta"));
            insumo.setCantidadCritica(obtenerNumero(txtCritico, "Stock crítico"));

            EmpaqueInsumo empaque = new EmpaqueInsumo();
            empaque.setNombreEmpaque(obtenerTexto(txtNombreEmpaqueNuevo, "Nombre empaque"));
            empaque.setCantidadQueTrae(obtenerNumero(txtCantidadEmpaqueNuevo, "Cantidad que trae"));
            empaque.setCodBarrasEmpaque(txtCodBarrasEmpaqueNuevo.getText().trim());

            ServicioInventario.registrarNuevoInsumoConEmpaque(insumo, empaque);
            guardado = true;
            JOptionPane.showMessageDialog(this, "Insumo y empaque creados correctamente.");
            dispose();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al crear insumo con empaque", ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarEntradaExistente() {
        try {
            String seleccion = obtenerSeleccionInsumo();
            int idIngrediente = Integer.parseInt(seleccion.split(" - ")[0]);
            double paquetes = obtenerNumero(txtPaquetes, "Paquetes a sumar");
            String codigoEmpaque = txtCodBarrasEmpaqueExistente.getText().trim();

            EmpaqueInsumo empaqueExistente = null;
            if (!codigoEmpaque.isEmpty()) {
                empaqueExistente = EmpaqueInsumoDAO.obtenerEmpaquePorCodigo(codigoEmpaque);
            }

            if (empaqueExistente == null && !codigoEmpaque.isEmpty()) {
                int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "No se encontró el código en la base. ¿Deseas crear el empaque nuevo con este código y continuar?",
                    "Crear empaque nuevo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (confirmar != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            EmpaqueInsumo empaque = new EmpaqueInsumo();
            empaque.setIdIngrediente(idIngrediente);
            empaque.setNombreEmpaque(obtenerTexto(txtNombreEmpaqueExistente, "Nombre empaque"));
            empaque.setCantidadQueTrae(obtenerNumero(txtCantidadEmpaqueExistente, "Cantidad que trae"));
            empaque.setCodBarrasEmpaque(codigoEmpaque);

            ServicioInventario.registrarEntradaExistente(idIngrediente, empaque, paquetes);
            guardado = true;
            JOptionPane.showMessageDialog(this, "Entrada registrada correctamente.");
            dispose();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error al registrar entrada existente", ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            lblResultado.setText("Ingresa o escanea un código primero.");
            return;
        }

        try {
            EmpaqueInsumo encontrado = EmpaqueInsumoDAO.obtenerEmpaquePorCodigo(codigo);
            if (encontrado == null) {
                lblResultado.setText("No se encontró el código. Puedes continuar en modo manual.");
                txtCodBarrasEmpaqueExistente.setText(codigo);
                return;
            }

            txtCodBarrasEmpaqueExistente.setText(encontrado.getCodBarrasEmpaque());
            txtNombreEmpaqueExistente.setText(encontrado.getNombreEmpaque());
            txtCantidadEmpaqueExistente.setText(String.valueOf(encontrado.getCantidadQueTrae()));
            txtPaquetes.setText("1");

            for (int i = 0; i < cboInsumos.getItemCount(); i++) {
                String item = cboInsumos.getItemAt(i);
                if (item.startsWith(encontrado.getIdIngrediente() + " - ")) {
                    cboInsumos.setSelectedIndex(i);
                    break;
                }
            }
            lblResultado.setText("Código reconocido. Se sumará la entrada.");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error buscando empaque por código", ex);
            lblResultado.setText("Error al buscar el código.");
        }
    }

    private String obtenerSeleccionInsumo() {
        Object seleccion = cboInsumos.getSelectedItem();
        if (seleccion == null) {
            throw new IllegalArgumentException("Selecciona un insumo.");
        }
        return seleccion.toString();
    }

    private String obtenerTexto(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Completa el campo " + nombreCampo + ".");
        }
        return texto;
    }

    private double obtenerNumero(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Completa el campo " + nombreCampo + ".");
        }
        return Double.parseDouble(texto);
    }

    private String normalizarTipoProducto(String valor) {
        String v = valor == null ? "" : valor.trim().toLowerCase();
        return switch (v) {
            case "refrigerado" -> "Refrigerado";
            case "carnes", "carne" -> "Carnes";
            case "abarrotes", "abarrote" -> "Abarrotes";
            case "lacteos", "lácteos", "lacteo", "lácteo" -> "Lacteos";
            case "bebidas", "bebida" -> "Bebidas";
            case "seco", "secos" -> "Seco";
            default -> throw new IllegalArgumentException("Tipo producto inválido. Usa: Refrigerado, Carnes, Abarrotes, Lacteos, Bebidas o Seco.");
        };
    }

    private String normalizarMedicion(String valor) {
        String v = valor == null ? "" : valor.trim().toLowerCase();
        return switch (v) {
            case "pieza", "piezas" -> "Pieza";
            case "kilogramo", "kilogramos", "kg" -> "Kilogramo";
            case "litro", "litros", "l" -> "Litro";
            case "gramo", "gramos", "g" -> "Gramo";
            default -> throw new IllegalArgumentException("Medición inválida. Usa: Pieza, Kilogramo, Litro o Gramo.");
        };
    }
}
