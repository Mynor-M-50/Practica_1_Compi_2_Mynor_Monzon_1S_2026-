/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.pila;

/**
 *
 * @author mynorm50
 */

/**
 * Operaciones que se muestran en la simulacion de la pila.
 *
 * ANTLR4 usa analisis LL(*), que es descendente y no hace shift ni
 * reduce de verdad. Lo que se registra aqui es una simulacion armada
 * con los eventos del Listener, tal como lo confirmo el auxiliar en la
 * duda 8 del foro.
 */
public enum OperacionPila {

    /** Se apila un token leido de la entrada. */
    SHIFT("Shift", "Se apila el token leido"),

    /** Se sustituyen los simbolos de una regla por su no terminal. */
    REPLACE("Replace", "Se reemplazan los simbolos por el no terminal"),

    /** El analisis termino correctamente. */
    ACCEPT("Accept", "Analisis completado");

    private final String nombre;
    private final String descripcion;

    OperacionPila(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
