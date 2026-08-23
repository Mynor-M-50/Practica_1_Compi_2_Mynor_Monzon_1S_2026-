/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.reportes;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.pila.PasoPila;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos.Simbolo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos.TablaSimbolos;

import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Genera codigo DOT para graficar con Graphviz.
 *
 * El AST se recorre con los metodos etiqueta y hijos que define Nodo,
 * asi que un nodo nuevo no obliga a tocar esta clase: basta con que
 * implemente esos dos metodos.
 *
 * La tabla de simbolos y la pila usan etiquetas HTML de Graphviz, que
 * permiten dibujar celdas alineadas sin tener que calcular posiciones.
 */
public class GeneradorDot {

    private int contador;

    // ============================================================
    // AST
    // ============================================================

    public String generarAST(Nodo raiz) {
        contador = 0;
        StringBuilder dot = new StringBuilder();

        dot.append("digraph AST {").append(salto());
        dot.append("  rankdir=TB;").append(salto());
        dot.append("  bgcolor=\"white\";").append(salto());
        dot.append("  node [shape=box, style=\"rounded,filled\", ")
           .append("fillcolor=\"#E3F2FD\", color=\"#1565C0\", ")
           .append("fontname=\"Helvetica\", fontsize=11];").append(salto());
        dot.append("  edge [color=\"#546E7A\", arrowsize=0.7];").append(salto());
        dot.append(salto());

        if (raiz == null) {
            dot.append("  vacio [label=\"Sin AST\"];").append(salto());
        } else {
            recorrerNodo(raiz, dot);
        }

        dot.append("}").append(salto());
        return dot.toString();
    }

    /**
     * Escribe el nodo y sus aristas hacia los hijos.
     * Devuelve el identificador asignado para que el padre lo enlace.
     */
    private String recorrerNodo(Nodo nodo, StringBuilder dot) {
        String id = "n" + (contador++);

        dot.append("  ").append(id)
           .append(" [label=\"").append(escapar(nodo.etiqueta())).append("\"");

        // Las hojas se pintan distinto para que se distingan de un vistazo
        List<Nodo> hijos = nodo.hijos();
        if (hijos == null || hijos.isEmpty()) {
            dot.append(", fillcolor=\"#FFF8E1\", color=\"#F57F17\"");
        }
        dot.append("];").append(salto());

        if (hijos != null) {
            for (Nodo hijo : hijos) {
                if (hijo == null) {
                    continue;
                }
                String idHijo = recorrerNodo(hijo, dot);
                dot.append("  ").append(id).append(" -> ").append(idHijo)
                   .append(";").append(salto());
            }
        }

        return id;
    }

    // ============================================================
    // Tabla de simbolos
    // ============================================================

    public String generarTablaSimbolos(TablaSimbolos tabla) {
        StringBuilder dot = new StringBuilder();

        dot.append("digraph TablaSimbolos {").append(salto());
        dot.append("  rankdir=TB;").append(salto());
        dot.append("  bgcolor=\"white\";").append(salto());
        dot.append("  node [shape=plaintext, fontname=\"Helvetica\"];").append(salto());
        dot.append(salto());

        if (tabla == null || tabla.getHistorial().isEmpty()) {
            dot.append("  vacia [label=\"Sin simbolos declarados\"];").append(salto());
            dot.append("}").append(salto());
            return dot.toString();
        }

        dot.append("  tabla [label=<").append(salto());
        dot.append("    <TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" ")
           .append("CELLPADDING=\"6\">").append(salto());

        dot.append("      <TR BGCOLOR=\"#1565C0\">")
           .append(celdaEncabezado("Nombre"))
           .append(celdaEncabezado("Tipo"))
           .append(celdaEncabezado("Rol"))
           .append(celdaEncabezado("Ambito"))
           .append(celdaEncabezado("Linea"))
           .append(celdaEncabezado("Columna"))
           .append("</TR>").append(salto());

        boolean alterna = false;
        for (Simbolo simbolo : tabla.getHistorial()) {
            String fondo = alterna ? "#F5F5F5" : "#FFFFFF";
            alterna = !alterna;

            dot.append("      <TR BGCOLOR=\"").append(fondo).append("\">")
               .append(celda(simbolo.getNombre()))
               .append(celda(String.valueOf(simbolo.getTipo())))
               .append(celda(String.valueOf(simbolo.getRol())))
               .append(celda(simbolo.getNombreAmbito()))
               .append(celda(String.valueOf(simbolo.getLinea())))
               .append(celda(String.valueOf(simbolo.getColumna())))
               .append("</TR>").append(salto());
        }

        dot.append("    </TABLE>").append(salto());
        dot.append("  >];").append(salto());
        dot.append("}").append(salto());

        return dot.toString();
    }

    // ============================================================
    // Pila
    // ============================================================

    /**
     * Dibuja el contenido de la pila en un paso concreto.
     * La cima queda arriba, que es como se suele representar.
     */
    public String generarPila(PasoPila paso) {
        StringBuilder dot = new StringBuilder();

        dot.append("digraph Pila {").append(salto());
        dot.append("  rankdir=TB;").append(salto());
        dot.append("  bgcolor=\"white\";").append(salto());
        dot.append("  node [shape=plaintext, fontname=\"Helvetica\"];").append(salto());
        dot.append(salto());

        if (paso == null || paso.getContenido().isEmpty()) {
            dot.append("  vacia [label=\"Pila vacia\"];").append(salto());
            dot.append("}").append(salto());
            return dot.toString();
        }

        dot.append("  pila [label=<").append(salto());
        dot.append("    <TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" ")
           .append("CELLPADDING=\"6\">").append(salto());

        dot.append("      <TR BGCOLOR=\"#6A1B9A\">")
           .append(celdaEncabezado("Paso " + paso.getNumero()
                   + " - " + paso.getOperacion().getNombre()))
           .append("</TR>").append(salto());

        List<String> contenido = paso.getContenido();
        for (int i = contenido.size() - 1; i >= 0; i--) {
            boolean esCima = (i == contenido.size() - 1);
            String fondo = esCima ? "#FFE082" : "#FFFFFF";
            String texto = esCima
                    ? contenido.get(i) + "   (cima)"
                    : contenido.get(i);

            dot.append("      <TR BGCOLOR=\"").append(fondo).append("\">")
               .append(celda(texto))
               .append("</TR>").append(salto());
        }

        dot.append("    </TABLE>").append(salto());
        dot.append("  >];").append(salto());
        dot.append("}").append(salto());

        return dot.toString();
    }

    // ============================================================
    // Utilidades
    // ============================================================

    private String celdaEncabezado(String texto) {
        return "<TD><FONT COLOR=\"white\"><B>" + escaparHtml(texto)
                + "</B></FONT></TD>";
    }

    private String celda(String texto) {
        return "<TD>" + escaparHtml(texto) + "</TD>";
    }

    /** Escapa lo que rompe una etiqueta normal de DOT. */
    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");
    }

    /** Escapa lo que rompe una etiqueta HTML de DOT. */
    private String escaparHtml(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
    }

    private String salto() {
        return System.lineSeparator();
    }
}