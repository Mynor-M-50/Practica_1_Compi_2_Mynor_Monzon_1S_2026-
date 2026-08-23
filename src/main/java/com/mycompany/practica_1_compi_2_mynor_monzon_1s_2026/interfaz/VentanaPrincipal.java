/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.interfaz;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.analizador.Compilador;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores.ErrorCompilacion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.reportes.GeneradorDot;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.traductor.TraductorPiglatin;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;

/**
 *
 * @author mynorm50
 */

/**
 * Ventana principal del entorno de Codex Latinus.
 *
 * El editor usa JTextPane y no JTextArea porque necesita colores por
 * caracter. El coloreado se dispara con cada cambio del documento, pero
 * en diferido con invokeLater: cambiar atributos desde dentro del
 * evento del documento lanza una excepcion de estado.
 */

public class VentanaPrincipal extends JFrame {

    private static final Color FONDO_EDITOR = new Color(0xFC, 0xFC, 0xFC);
    private static final Color FONDO_NUMEROS = new Color(0xEE, 0xEE, 0xEE);
    private static final Color FONDO_CONSOLA = new Color(0x1E, 0x1E, 0x1E);
    private static final Color TEXTO_CONSOLA = new Color(0x9C, 0xCC, 0x65);

    /** Una pestania del editor, con su archivo asociado. */
    private class Pestania {

        final JTextPane editor = new JTextPane();
        final JTextArea numeros = new JTextArea("1");
        final GestorArchivos gestor = new GestorArchivos();

        Pestania() {
            editor.setFont(new Font("Monospaced", Font.PLAIN, 14));
            editor.setBackground(FONDO_EDITOR);
            editor.setMargin(new Insets(4, 6, 4, 6));

            numeros.setFont(new Font("Monospaced", Font.PLAIN, 14));
            numeros.setBackground(FONDO_NUMEROS);
            numeros.setForeground(new Color(0x90, 0x90, 0x90));
            numeros.setEditable(false);
            numeros.setMargin(new Insets(4, 5, 4, 5));

            editor.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    alCambiar();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    alCambiar();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    // Cambios de atributos, no de texto: se ignora
                }
            });

            CaretListener seguidor = e -> actualizarBarraEstado();
            editor.addCaretListener(seguidor);
        }

        /**
         * Se llama en cada edicion. El coloreado se hace en diferido
         * porque no se pueden modificar atributos mientras el documento
         * esta notificando un cambio.
         */
        void alCambiar() {
            SwingUtilities.invokeLater(() -> {
                colorear();
                actualizarNumeros();
                actualizarBarraEstado();
            });
        }

        void colorear() {
            StyledDocument documento = editor.getStyledDocument();
            resaltador.resaltar(documento, editor.getText());
        }

        void actualizarNumeros() {
            int total = editor.getDocument().getDefaultRootElement()
                    .getElementCount();
            StringBuilder texto = new StringBuilder();
            for (int i = 1; i <= total; i++) {
                texto.append(i).append(System.lineSeparator());
            }
            numeros.setText(texto.toString());
        }

        String getNombre() {
            return gestor.getNombreArchivo();
        }

        JScrollPane crearScroll() {
            JScrollPane scroll = new JScrollPane(editor);
            scroll.setRowHeaderView(numeros);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            return scroll;
        }
    }

    private final ResaltadorSintaxis resaltador = new ResaltadorSintaxis();
    private final Compilador compilador = new Compilador();

    private JTabbedPane pestanias;
    private JTextArea consola;
    private JLabel barraEstado;

    public VentanaPrincipal() {
        setTitle("Codex Latinus - Compilador");
        setSize(1250, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        crearComponentes();
        crearMenu();
        nuevaPestania();
    }

    // ============================================================
    // Construccion de la interfaz
    // ============================================================

    private void crearComponentes() {
        pestanias = new JTabbedPane();
        pestanias.addChangeListener(e -> actualizarTitulo());

        consola = new JTextArea();
        consola.setFont(new Font("Monospaced", Font.PLAIN, 13));
        consola.setBackground(FONDO_CONSOLA);
        consola.setForeground(TEXTO_CONSOLA);
        consola.setEditable(false);
        consola.setText("Compilador Codex Latinus" + salto()
                + "Presiona F5 para compilar." + salto() + salto());

        JScrollPane scrollConsola = new JScrollPane(consola);
        scrollConsola.setBorder(BorderFactory.createTitledBorder("Consola"));
        scrollConsola.setPreferredSize(new Dimension(0, 220));

        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                pestanias, scrollConsola);
        division.setResizeWeight(0.72);
        division.setDividerSize(5);

        barraEstado = new JLabel("  Listo   |   Linea 1, Columna 1");
        barraEstado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        barraEstado.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(division, BorderLayout.CENTER);
        panel.add(barraEstado, BorderLayout.SOUTH);
        setContentPane(panel);
    }

    private void crearMenu() {
        JMenuBar barra = new JMenuBar();

        // ---- Archivo ----
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.add(item("Nuevo", "ctrl N", e -> nuevaPestania()));
        menuArchivo.add(item("Abrir", "ctrl O", e -> abrir()));
        menuArchivo.addSeparator();
        menuArchivo.add(item("Guardar", "ctrl S", e -> guardar()));
        menuArchivo.add(item("Guardar como...", null, e -> guardarComo()));
        menuArchivo.addSeparator();
        menuArchivo.add(item("Cerrar pestania", "ctrl W", e -> cerrarPestania()));
        menuArchivo.add(item("Salir", null, e -> System.exit(0)));

        // ---- Compilar ----
        JMenu menuCompilar = new JMenu("Compilar");
        menuCompilar.add(item("Compilar", "F5", e -> compilar()));
        menuCompilar.add(item("Limpiar consola", null, e -> limpiarConsola()));

        // ---- Reportes ----
        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.add(item("Reporte de errores", null, e -> mostrarErrores()));
        menuReportes.addSeparator();
        menuReportes.add(item("Graficar AST", null, e -> graficarAST()));
        menuReportes.add(item("Graficar tabla de simbolos", null,
                e -> graficarTablaSimbolos()));
        menuReportes.addSeparator();
        menuReportes.add(item("Pila de analisis paso a paso", null,
                e -> mostrarPila()));

        // ---- Traducir ----
        JMenu menuTraducir = new JMenu("Traducir");
        menuTraducir.add(item("Ver traduccion a PigLatin", null,
                e -> verTraduccion()));
        menuTraducir.add(item("Descargar traduccion (.pig)", null,
                e -> descargarTraduccion()));

        barra.add(menuArchivo);
        barra.add(menuCompilar);
        barra.add(menuReportes);
        barra.add(menuTraducir);
        setJMenuBar(barra);
    }

    private JMenuItem item(String texto, String atajo,
                           java.awt.event.ActionListener accion) {
        JMenuItem elemento = new JMenuItem(texto);
        if (atajo != null) {
            elemento.setAccelerator(KeyStroke.getKeyStroke(atajo));
        }
        elemento.addActionListener(accion);
        return elemento;
    }

    // ============================================================
    // Manejo de pestanias
    // ============================================================

    private void nuevaPestania() {
        Pestania pestania = new Pestania();
        JScrollPane scroll = pestania.crearScroll();
        scroll.putClientProperty("pestania", pestania);

        pestanias.addTab(pestania.getNombre(), scroll);
        pestanias.setSelectedIndex(pestanias.getTabCount() - 1);
        actualizarTitulo();
    }

    private Pestania pestaniaActual() {
        int indice = pestanias.getSelectedIndex();
        if (indice < 0) {
            return null;
        }
        JScrollPane scroll = (JScrollPane) pestanias.getComponentAt(indice);
        return (Pestania) scroll.getClientProperty("pestania");
    }

    private void cerrarPestania() {
        int indice = pestanias.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        if (pestanias.getTabCount() == 1) {
            Pestania pestania = pestaniaActual();
            if (pestania != null) {
                pestania.editor.setText("");
                pestania.gestor.cerrarArchivo();
                pestanias.setTitleAt(0, pestania.getNombre());
            }
        } else {
            pestanias.remove(indice);
        }
        actualizarTitulo();
    }

    // ============================================================
    // Archivos
    // ============================================================

    private void abrir() {
        Pestania pestania = new Pestania();
        String contenido = pestania.gestor.abrir(this);
        if (contenido == null) {
            return;
        }

        JScrollPane scroll = pestania.crearScroll();
        scroll.putClientProperty("pestania", pestania);
        pestanias.addTab(pestania.getNombre(), scroll);
        pestanias.setSelectedIndex(pestanias.getTabCount() - 1);

        pestania.editor.setText(contenido);
        pestania.editor.setCaretPosition(0);
        pestania.alCambiar();

        actualizarTitulo();
        imprimir("Archivo abierto: " + pestania.getNombre());
    }

    private void guardar() {
        Pestania pestania = pestaniaActual();
        if (pestania == null) {
            return;
        }
        if (pestania.gestor.guardar(this, pestania.editor.getText())) {
            pestanias.setTitleAt(pestanias.getSelectedIndex(), pestania.getNombre());
            actualizarTitulo();
            imprimir("Archivo guardado: " + pestania.getNombre());
        }
    }

    private void guardarComo() {
        Pestania pestania = pestaniaActual();
        if (pestania == null) {
            return;
        }
        if (pestania.gestor.guardarComo(this, pestania.editor.getText())) {
            pestanias.setTitleAt(pestanias.getSelectedIndex(), pestania.getNombre());
            actualizarTitulo();
            imprimir("Archivo guardado: " + pestania.getNombre());
        }
    }

    // ============================================================
    // Compilacion
    // ============================================================

    private void compilar() {
        Pestania pestania = pestaniaActual();
        if (pestania == null) {
            return;
        }

        String fuente = pestania.editor.getText();
        if (fuente.isBlank()) {
            imprimir("No hay codigo que compilar.");
            return;
        }

        limpiarConsola();
        imprimir("Compilando " + pestania.getNombre() + "...");

        boolean exito = compilador.compilar(fuente);

        if (exito) {
            imprimir("Compilacion exitosa, sin errores.");
            imprimir("Ya puedes ver el AST, la tabla de simbolos y la traduccion.");
        } else {
            imprimir("Se encontraron errores:");
            imprimir("");
            for (ErrorCompilacion error : compilador.getErrores().getErrores()) {
                imprimir("  " + error);
            }
            imprimir("");
            imprimir("Total: " + compilador.getErrores().cantidad() + " errores.");
        }
    }

    // ============================================================
    // Reportes
    // ============================================================

    private void mostrarErrores() {
        if (compilador.getErrores().cantidad() == 0) {
            informar("No hay errores registrados. Compila primero con F5.");
            return;
        }

        StringBuilder texto = new StringBuilder();
        texto.append(String.format("%-6s %-12s %-8s %-8s %s",
                "No.", "Tipo", "Linea", "Columna", "Descripcion"));
        texto.append(salto());

        int numero = 1;
        for (ErrorCompilacion error : compilador.getErrores().getErrores()) {
            texto.append(String.format("%-6d %-12s %-8d %-8d %s",
                    numero++, error.getTipo(), error.getLinea(),
                    error.getColumna(), error.getMensaje()));
            texto.append(salto());
        }

        mostrarTexto("Reporte de errores", texto.toString());
    }

    private void graficarAST() {
        if (compilador.getPrograma() == null) {
            informar("Primero compila un programa valido con F5.");
            return;
        }
        String dot = new GeneradorDot().generarAST(compilador.getPrograma());
        new VisorDot(this).mostrar("Arbol de sintaxis abstracta", dot, "ast");
    }

    private void graficarTablaSimbolos() {
        if (compilador.getTablaSimbolos() == null) {
            informar("Primero compila un programa valido con F5.");
            return;
        }
        String dot = new GeneradorDot()
                .generarTablaSimbolos(compilador.getTablaSimbolos());
        new VisorDot(this).mostrar("Tabla de simbolos", dot, "simbolos");
    }

    private void mostrarPila() {
        if (compilador.getRegistradorPila() == null) {
            informar("Primero compila un programa con F5.");
            return;
        }
        new VentanaPila(this, compilador.getRegistradorPila()).setVisible(true);
    }

    // ============================================================
    // Traduccion
    // ============================================================

    private void verTraduccion() {
        if (compilador.getPrograma() == null) {
            informar("Primero compila un programa valido con F5.");
            return;
        }
        String traduccion = new TraductorPiglatin()
                .traducir(compilador.getPrograma());
        mostrarTexto("Traduccion a PigLatin", traduccion);
    }

    private void descargarTraduccion() {
        if (compilador.getPrograma() == null) {
            informar("Primero compila un programa valido con F5.");
            return;
        }

        Pestania pestania = pestaniaActual();
        if (pestania == null) {
            return;
        }

        String traduccion = new TraductorPiglatin()
                .traducir(compilador.getPrograma());

        if (pestania.gestor.descargarTraduccion(this, traduccion)) {
            imprimir("Traduccion descargada correctamente.");
        }
    }

    // ============================================================
    // Utilidades de interfaz
    // ============================================================

    private void mostrarTexto(String titulo, String contenido) {
        JFrame ventana = new JFrame(titulo);
        JTextArea area = new JTextArea(contenido);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);
        area.setCaretPosition(0);
        ventana.add(new JScrollPane(area));
        ventana.setSize(860, 560);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void informar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void imprimir(String mensaje) {
        consola.append(mensaje + salto());
        consola.setCaretPosition(consola.getDocument().getLength());
    }

    private void limpiarConsola() {
        consola.setText("");
    }

    private void actualizarTitulo() {
        Pestania pestania = pestaniaActual();
        String nombre = (pestania != null) ? pestania.getNombre() : "Sin titulo";
        setTitle("Codex Latinus - Compilador  [" + nombre + "]");
        actualizarBarraEstado();
    }

    /**
     * JTextPane no tiene getLineOfOffset, asi que la linea y la columna
     * se calculan con el elemento raiz del documento.
     */
    private void actualizarBarraEstado() {
        Pestania pestania = pestaniaActual();
        if (pestania == null) {
            return;
        }
        int posicion = pestania.editor.getCaretPosition();
        Element raiz = pestania.editor.getDocument().getDefaultRootElement();
        int linea = raiz.getElementIndex(posicion);
        int columna = posicion - raiz.getElement(linea).getStartOffset();

        File archivo = pestania.gestor.getArchivoActual();
        String ruta = (archivo != null) ? archivo.getAbsolutePath() : "sin guardar";

        barraEstado.setText(String.format("  %s   |   Linea %d, Columna %d",
                ruta, linea + 1, columna + 1));
    }

    private String salto() {
        return System.lineSeparator();
    }

    // ============================================================
    // Arranque
    // ============================================================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla se usa el aspecto por defecto
        }
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}