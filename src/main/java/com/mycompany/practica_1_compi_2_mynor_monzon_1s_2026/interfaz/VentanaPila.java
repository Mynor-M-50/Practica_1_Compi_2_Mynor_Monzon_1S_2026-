/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.interfaz;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.pila.OperacionPila;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.pila.PasoPila;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.pila.RegistradorPila;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.reportes.GeneradorDot;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;

/**
 *
 * @author mynorm50
 */

/**
 * Visualizacion de la pila de analisis paso a paso.
 *
 * La tabla de la derecha es a la vez el log de operaciones y el
 * navegador: seleccionar una fila muestra el estado de la pila en ese
 * momento. Los botones mueven la seleccion.
 *
 * Esto funciona porque cada PasoPila guarda una copia del contenido de
 * la pila, no una referencia. Sin esa copia, al retroceder se veria
 * siempre el estado final.
 */

public class VentanaPila extends JFrame {

    private static final Color COLOR_SHIFT = new Color(0xE3, 0xF2, 0xFD);
    private static final Color COLOR_REPLACE = new Color(0xF3, 0xE5, 0xF5);
    private static final Color COLOR_ACCEPT = new Color(0xC8, 0xE6, 0xC9);
    private static final Color COLOR_TEXTO = new Color(0x21, 0x21, 0x21);

    private final RegistradorPila registrador;

    private final DefaultTableModel modeloLog = new DefaultTableModel(
            new String[]{"No.", "Accion", "Simbolo", "Linea", "Detalle"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tablaLog = new JTable(modeloLog);
    private final DefaultListModel<String> modeloPila = new DefaultListModel<>();
    private final JList<String> listaPila = new JList<>(modeloPila);
    private final JLabel etiquetaPaso = new JLabel();
    private final JLabel etiquetaDetalle = new JLabel();

    public VentanaPila(Component padre, RegistradorPila registrador) {
        this.registrador = registrador;

        setTitle("Pila de analisis paso a paso");
        setSize(1050, 600);
        setLocationRelativeTo(padre);

        construir();
        cargarPasos();

        if (modeloLog.getRowCount() > 0) {
            tablaLog.setRowSelectionInterval(0, 0);
        }
    }

    private void construir() {
        setLayout(new BorderLayout());

        add(barraControles(), BorderLayout.NORTH);

        // ---- Pila a la izquierda ----
        listaPila.setFont(new Font("Monospaced", Font.PLAIN, 14));
        listaPila.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPila.setBackground(new Color(0xFA, 0xFA, 0xFA));
        listaPila.setForeground(COLOR_TEXTO);

        JScrollPane scrollPila = new JScrollPane(listaPila);
        scrollPila.setBorder(BorderFactory.createTitledBorder(
                "Contenido de la pila (cima arriba)"));
        scrollPila.setPreferredSize(new Dimension(280, 0));

        // ---- Log a la derecha ----
        tablaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tablaLog.setRowHeight(20);
        tablaLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLog.setDefaultRenderer(Object.class, new RenderadorOperacion());
        tablaLog.setShowGrid(false);
        tablaLog.setIntercellSpacing(new Dimension(0, 0));
        tablaLog.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarPasoSeleccionado();
            }
        });

        tablaLog.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaLog.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaLog.getColumnModel().getColumn(2).setPreferredWidth(160);
        tablaLog.getColumnModel().getColumn(3).setPreferredWidth(50);
        tablaLog.getColumnModel().getColumn(4).setPreferredWidth(420);

        JScrollPane scrollLog = new JScrollPane(tablaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log de operaciones"));

        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollPila, scrollLog);
        division.setDividerLocation(280);
        add(division, BorderLayout.CENTER);

        // ---- Detalle abajo ----
        etiquetaDetalle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        etiquetaDetalle.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        add(etiquetaDetalle, BorderLayout.SOUTH);
    }

    private JPanel barraControles() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));

        JButton primero = new JButton("|< Primero");
        JButton atras = new JButton("< Atras");
        JButton siguiente = new JButton("Siguiente >");
        JButton ultimo = new JButton("Ultimo >|");
        JButton graficar = new JButton("Graficar pila actual");

        primero.addActionListener(e -> irA(0));
        atras.addActionListener(e -> mover(-1));
        siguiente.addActionListener(e -> mover(1));
        ultimo.addActionListener(e -> irA(modeloLog.getRowCount() - 1));
        graficar.addActionListener(e -> graficarPasoActual());

        etiquetaPaso.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiquetaPaso.setHorizontalAlignment(SwingConstants.LEFT);
        etiquetaPaso.setPreferredSize(new Dimension(160, 24));

        barra.add(primero);
        barra.add(atras);
        barra.add(siguiente);
        barra.add(ultimo);
        barra.add(etiquetaPaso);
        barra.add(graficar);

        return barra;
    }

    // ------------------------------------------------------------
    // Carga y navegacion
    // ------------------------------------------------------------

    private void cargarPasos() {
        modeloLog.setRowCount(0);
        if (registrador == null) {
            return;
        }
        for (PasoPila paso : registrador.getPasos()) {
            modeloLog.addRow(new Object[]{
                paso.getNumero(),
                paso.getOperacion().getNombre(),
                paso.getSimbolo(),
                paso.getLinea(),
                paso.getDetalle()
            });
        }
    }

    private void mover(int desplazamiento) {
        int actual = tablaLog.getSelectedRow();
        irA(actual + desplazamiento);
    }

    private void irA(int indice) {
        if (indice < 0 || indice >= modeloLog.getRowCount()) {
            return;
        }
        tablaLog.setRowSelectionInterval(indice, indice);
        Rectangle celda = tablaLog.getCellRect(indice, 0, true);
        tablaLog.scrollRectToVisible(celda);
    }

    /** Reconstruye la vista de la pila con el paso seleccionado. */
    private void mostrarPasoSeleccionado() {
        PasoPila paso = pasoActual();

        modeloPila.clear();
        if (paso == null) {
            etiquetaPaso.setText("Sin pasos");
            etiquetaDetalle.setText(" ");
            return;
        }

        // La cima se muestra arriba, por eso se recorre al reves
        for (int i = paso.getContenido().size() - 1; i >= 0; i--) {
            String simbolo = paso.getContenido().get(i);
            boolean esCima = (i == paso.getContenido().size() - 1);
            modeloPila.addElement(esCima ? "> " + simbolo : "  " + simbolo);
        }

        etiquetaPaso.setText("Paso " + paso.getNumero()
                + " de " + modeloLog.getRowCount());
        etiquetaDetalle.setText(paso.getOperacion().getNombre()
                + ": " + paso.getDetalle()
                + "   |   Altura de la pila: " + paso.getAltura());
    }

    private PasoPila pasoActual() {
        int fila = tablaLog.getSelectedRow();
        if (fila < 0 || registrador == null) {
            return null;
        }
        return registrador.getPaso(fila);
    }

    private void graficarPasoActual() {
        PasoPila paso = pasoActual();
        if (paso == null) {
            return;
        }
        String dot = new GeneradorDot().generarPila(paso);
        new VisorDot(this).mostrar("Pila - paso " + paso.getNumero(), dot, "pila");
    }

    /** Pinta cada fila segun la operacion que representa. */
    private static class RenderadorOperacion extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor,
                                                       boolean seleccionada,
                                                       boolean enfocada,
                                                       int fila, int columna) {
            Component componente = super.getTableCellRendererComponent(
                    tabla, valor, seleccionada, enfocada, fila, columna);

            // Sin esto el fondo no se dibuja hasta que algo fuerza un repintado
            if (componente instanceof javax.swing.JComponent) {
                ((javax.swing.JComponent) componente).setOpaque(true);
            }

            if (seleccionada) {
                componente.setBackground(tabla.getSelectionBackground());
                componente.setForeground(tabla.getSelectionForeground());
                return componente;
            }

            componente.setForeground(COLOR_TEXTO);
            Object operacion = tabla.getValueAt(fila, 1);
            if (OperacionPila.SHIFT.getNombre().equals(operacion)) {
                componente.setBackground(COLOR_SHIFT);
            } else if (OperacionPila.REPLACE.getNombre().equals(operacion)) {
                componente.setBackground(COLOR_REPLACE);
            } else {
                componente.setBackground(COLOR_ACCEPT);
            }
            return componente;
        }
    }
}