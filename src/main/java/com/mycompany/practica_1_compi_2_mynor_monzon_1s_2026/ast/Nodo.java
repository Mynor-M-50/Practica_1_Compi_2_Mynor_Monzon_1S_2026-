/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Raiz de la jerarquia del AST propio.
 */
public abstract class Nodo {

    private final int linea;
    private final int columna;

    protected Nodo(int linea, int columna) {
        this.linea = linea;
        this.columna = columna;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    /** Texto que se muestra dentro del nodo al graficar el arbol. */
    public abstract String etiqueta();

    /** Hijos directos, en el orden en que aparecen en el codigo. */
    public abstract List<Nodo> hijos();

    /** Representacion con sangria, util para depurar en consola. */
    public String aTexto(int nivel) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ".repeat(nivel)).append(etiqueta()).append(System.lineSeparator());
        for (Nodo hijo : hijos()) {
            if (hijo != null) {
                sb.append(hijo.aTexto(nivel + 1));
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return etiqueta();
    }
}