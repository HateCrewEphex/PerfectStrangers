package com.mycompany.perfectstrangers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Diálogo para que el mesero seleccione una promoción genérica
 * Aparece cuando hay promos activas sin producto específico
 */
public class DialogoSeleccionarPromocion extends JDialog {
    private Promocion promoSeleccionada = null;
    private boolean confirmado = false;
    
    public DialogoSeleccionarPromocion(Frame owner, List<Promocion> promos) {
        super(owner, "Aplicar Promoción", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initComponents(promos);
    }
    
    private void initComponents(List<Promocion> promos) {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior: Etiqueta
        JLabel etiqueta = new JLabel("¿Deseas aplicar una promoción a esta orden?");
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD));
        mainPanel.add(etiqueta, BorderLayout.NORTH);
        
        // Panel central: Combo box de promos
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        centerPanel.add(new JLabel("Selecciona una promoción:"));
        
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("--- Sin promoción ---");
        for (Promocion promo : promos) {
            model.addElement(formatearPromo(promo));
        }
        
        JComboBox<String> comboPromos = new JComboBox<>(model);
        centerPanel.add(comboPromos);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel inferior: Botones
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnAceptar.addActionListener(e -> {
            int idx = comboPromos.getSelectedIndex();
            if (idx > 0) {
                promoSeleccionada = promos.get(idx - 1);
            }
            confirmado = true;
            dispose();
        });
        
        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });
        
        bottomPanel.add(btnAceptar);
        bottomPanel.add(btnCancelar);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private String formatearPromo(Promocion promo) {
        String tipo = promo.getTipoDescuento();
        double valor = promo.getValorDescuento();
        String descuento = tipo.equals("Porcentaje") ? valor + "%" : "$" + valor;
        return promo.getNombrePromo() + " (" + descuento + ")";
    }
    
    public Promocion getPromoSeleccionada() {
        return promoSeleccionada;
    }
    
    public boolean fueConfirmado() {
        return confirmado;
    }
}
