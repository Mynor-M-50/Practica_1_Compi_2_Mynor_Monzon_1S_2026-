/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.interfaz.VentanaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author mynorm50
 */

public class Practica_1_Compi_2_Mynor_Monzon_1S_2026 {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla se usa el aspecto por defecto de Swing
        }

        // La interfaz debe construirse en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}