/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.interfaz;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author mynorm50
 */

/*
 Abrir, guardar y descargar archivos del proyecto
 
 * Se manejan dos extensiones
 *     .lat   codigo fuente en Codex Latinus
 *     .pig   codigo traducido a piglatin
 */

public class GestorArchivos {

    public static final String EXTENSION_FUENTE = "lat";
    public static final String EXTENSION_TRADUCIDO = "pig";

    private File archivoActual;

    public File getArchivoActual() {
        return archivoActual;
    }

    public boolean hayArchivoAbierto() {
        return archivoActual != null;
    }

    public String getNombreArchivo() {
        return (archivoActual != null) ? archivoActual.getName() : "Sin titulo";
    }

    public void cerrarArchivo() {
        archivoActual = null;
    }

    
     // Pide un archivo .lat y devuelve su contenido
     // Devuelve null si el usuario cancela o si ocurre un error
     
    public String abrir(Component padre) {
        JFileChooser selector = new JFileChooser(directorioInicial());
        selector.setDialogTitle("Abrir archivo Codex Latinus");
        selector.setFileFilter(new FileNameExtensionFilter(
                "Codigo Codex Latinus (*.lat)", EXTENSION_FUENTE));

        if (selector.showOpenDialog(padre) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File archivo = selector.getSelectedFile();
        try {
            String contenido = Files.readString(archivo.toPath());
            archivoActual = archivo;
            return contenido;
        } catch (IOException e) {
            mostrarError(padre, "No se pudo abrir el archivo:\n" + e.getMessage());
            return null;
        }
    }

    
     // Guarda sobre el archivo abierto. Si no hay ninguno, se comporta
     // como guardar como
     
    public boolean guardar(Component padre, String contenido) {
        if (archivoActual == null) {
            return guardarComo(padre, contenido);
        }
        return escribir(padre, archivoActual, contenido);
    }

    public boolean guardarComo(Component padre, String contenido) {
        File archivo = pedirDestino(padre, "Guardar archivo Codex Latinus",
                "Codigo Codex Latinus (*.lat)", EXTENSION_FUENTE, "programa");
        if (archivo == null) {
            return false;
        }
        if (escribir(padre, archivo, contenido)) {
            archivoActual = archivo;
            return true;
        }
        return false;
    }

    
      //Descarga la traduccion a piglatin. Propone como nombre el del
     // archivo fuente pero con la otra extension.
     
    public boolean descargarTraduccion(Component padre, String contenido) {
        String sugerido = "traduccion";
        if (archivoActual != null) {
            String nombre = archivoActual.getName();
            int punto = nombre.lastIndexOf('.');
            sugerido = (punto > 0) ? nombre.substring(0, punto) : nombre;
        }

        File archivo = pedirDestino(padre, "Descargar traduccion PigLatin",
                "Codigo PigLatin (*.pig)", EXTENSION_TRADUCIDO, sugerido);
        if (archivo == null) {
            return false;
        }
        return escribir(padre, archivo, contenido);
    }

    // ------------------------------------------------------------

    private File pedirDestino(Component padre, String titulo, String descripcion,
                              String extension, String nombreSugerido) {
        JFileChooser selector = new JFileChooser(directorioInicial());
        selector.setDialogTitle(titulo);
        selector.setFileFilter(new FileNameExtensionFilter(descripcion, extension));
        selector.setSelectedFile(new File(nombreSugerido + "." + extension));

        if (selector.showSaveDialog(padre) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File archivo = selector.getSelectedFile();

        // Agregar la extension si el usuario no la escribio
        if (!archivo.getName().toLowerCase().endsWith("." + extension)) {
            archivo = new File(archivo.getAbsolutePath() + "." + extension);
        }

        if (archivo.exists()) {
            int respuesta = JOptionPane.showConfirmDialog(padre,
                    "El archivo ya existe. Desea reemplazarlo?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta != JOptionPane.YES_OPTION) {
                return null;
            }
        }

        return archivo;
    }

    private boolean escribir(Component padre, File archivo, String contenido) {
        try {
            Files.writeString(archivo.toPath(),
                    (contenido != null) ? contenido : "");
            return true;
        } catch (IOException e) {
            mostrarError(padre, "No se pudo guardar el archivo:\n" + e.getMessage());
            return false;
        }
    }

    private File directorioInicial() {
        File entradas = new File("entradas");
        return entradas.isDirectory() ? entradas : new File(".");
    }

    private void mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}