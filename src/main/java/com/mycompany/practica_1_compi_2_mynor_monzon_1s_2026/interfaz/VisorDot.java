/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.interfaz;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author mynorm50
 */

/*
 Muestra un grafico generado con Graphviz.
 */
public class VisorDot {

    private static final String CARPETA_REPORTES = "reportes";

    private final Component padre;

    public VisorDot(Component padre) {
        this.padre = padre;
    }

    /*
     Genera los archivos y abre la ventana del reporte.
     */

    public void mostrar(String titulo, String dot, String nombreBase) {
        File carpeta = new File(CARPETA_REPORTES);
        carpeta.mkdirs();

        File archivoDot = new File(carpeta, nombreBase + ".dot");
        File archivoPng = new File(carpeta, nombreBase + ".png");
        File archivoSvg = new File(carpeta, nombreBase + ".svg");

        try {
            Files.writeString(archivoDot.toPath(), dot);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(padre,
                    "No se pudo escribir el archivo .dot:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean hayPng = ejecutarGraphviz(archivoDot, archivoPng, "png");
        boolean haySvg = ejecutarGraphviz(archivoDot, archivoSvg, "svg");

        construirVentana(titulo, dot, carpeta, archivoPng, archivoSvg, hayPng, haySvg);
    }

    private boolean ejecutarGraphviz(File entrada, File salida, String formato) {
        try {
            ProcessBuilder proceso = new ProcessBuilder(
                    "dot", "-T" + formato,
                    entrada.getAbsolutePath(),
                    "-o", salida.getAbsolutePath());
            proceso.redirectErrorStream(true);
            proceso.start().waitFor();
            return salida.exists() && salida.length() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void construirVentana(String titulo, String dot, File carpeta,
                                  File archivoPng, File archivoSvg,
                                  boolean hayPng, boolean haySvg) {

        JFrame ventana = new JFrame(titulo);
        ventana.setSize(1000, 700);
        ventana.setLocationRelativeTo(padre);

        JTabbedPane pestanias = new JTabbedPane();

        if (hayPng) {
            JLabel imagen = new JLabel(new ImageIcon(archivoPng.getAbsolutePath()));
            JScrollPane scroll = new JScrollPane(imagen);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scroll.getHorizontalScrollBar().setUnitIncrement(16);
            pestanias.addTab("Imagen (PNG)", scroll);
        }

        if (haySvg) {
            pestanias.addTab("Vectorial (SVG)", panelSvg(ventana, archivoSvg));
        }

        JTextArea areaDot = new JTextArea(dot);
        areaDot.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDot.setEditable(false);
        areaDot.setCaretPosition(0);
        pestanias.addTab("Codigo DOT", new JScrollPane(areaDot));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(pestanias, BorderLayout.CENTER);
        panel.add(barraAcciones(ventana, dot, carpeta, hayPng || haySvg),
                BorderLayout.SOUTH);

        ventana.setContentPane(panel);
        ventana.setVisible(true);
    }

    private JPanel panelSvg(JFrame ventana, File archivoSvg) {
        JLabel texto = new JLabel(
                "El SVG conserva la calidad al acercarse, util para arboles grandes.",
                SwingConstants.CENTER);
        texto.setFont(new Font("SansSerif", Font.PLAIN, 13));
        texto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton boton = new JButton("Abrir SVG en el navegador");
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(archivoSvg.toURI());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ventana,
                        "No se pudo abrir:\n" + ex.getMessage());
            }
        });

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(Box.createVerticalGlue());
        centro.add(texto);
        centro.add(Box.createRigidArea(new Dimension(0, 15)));
        centro.add(boton);
        centro.add(Box.createVerticalGlue());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel barraAcciones(JFrame ventana, String dot, File carpeta,
                                 boolean graphvizDisponible) {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        barra.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        if (!graphvizDisponible) {
            JLabel aviso = new JLabel(
                    "Graphviz no esta instalado. Pega el DOT en "
                    + "https://dreampuf.github.io/GraphvizOnline");
            aviso.setFont(new Font("SansSerif", Font.ITALIC, 12));
            barra.add(aviso);
        }

        JButton copiar = new JButton("Copiar DOT");
        copiar.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(dot), null);
            JOptionPane.showMessageDialog(ventana, "Codigo DOT copiado al portapapeles.");
        });

        JButton abrirCarpeta = new JButton("Abrir carpeta reportes");
        abrirCarpeta.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(carpeta);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ventana,
                        "No se pudo abrir la carpeta:\n" + ex.getMessage());
            }
        });

        barra.add(copiar);
        barra.add(abrirCarpeta);
        return barra;
    }
}