/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos;

/**
 *
 * @author mynorm50
 */

/**
 * Clasificacion de lo que representa un simbolo dentro de la tabla.
 *
 * Sirve para dos cosas: distinguir usos invalidos, como llamar a una
 * variable como si fuera funcion, y para agrupar los simbolos por
 * categoria al graficar la tabla.
 */
public enum RolSimbolo {

    VARIABLE("Variable"),
    ARREGLO("Arreglo"),
    PARAMETRO("Parametro"),
    FUNCION("Funcion"),
    ESTRUCTURA("Estructura"),
    CAMPO("Campo");

    private final String nombre;

    RolSimbolo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    /** Roles que se pueden usar como valor dentro de una expresion. */
    public boolean esValor() {
        return this == VARIABLE || this == ARREGLO || this == PARAMETRO;
    }

    @Override
    public String toString() {
        return nombre;
    }
}