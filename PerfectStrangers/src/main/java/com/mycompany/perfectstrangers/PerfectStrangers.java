/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.perfectstrangers;

/**
 *
 * @author Ephex
 */
public class PerfectStrangers {

    public static void main(String[] args) {
        
        try {
            // Configuraciones estéticas de FlatLaf (bordes redondeados, estilo oscuro moderno)
            javax.swing.UIManager.put("Button.arc", 20); // Botones muy redondeados
            javax.swing.UIManager.put("Component.arc", 20); // ComboBox, TextFields
            javax.swing.UIManager.put("ProgressBar.arc", 20);
            javax.swing.UIManager.put("TextComponent.arc", 20); // Inputs de texto
            javax.swing.UIManager.put("ScrollBar.arc", 20);
            javax.swing.UIManager.put("Table.alternateRowColor", new java.awt.Color(40, 40, 40)); 
            
            // Iniciar el tema oscuro estilo Mac/Windows 11
            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
        } catch( Exception ex ) {
            System.err.println( "No se pudo inicializar FlatLaf" );
        }

        java.awt.EventQueue.invokeLater(() -> new PSInicio().setVisible(true)); 
    }
}

