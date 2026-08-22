/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores;

/**
 *
 * @author mynorm50
 */

/**
 * Las tres fases en las que se puede detectar un error.
 * El enunciado pide reportarlas por separado.
 */

public enum TipoError {

    LEXICO("Lexico"),
    SINTACTICO("Sintactico"),
    SEMANTICO("Semantico");

    private final String nombre;

    TipoError(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}