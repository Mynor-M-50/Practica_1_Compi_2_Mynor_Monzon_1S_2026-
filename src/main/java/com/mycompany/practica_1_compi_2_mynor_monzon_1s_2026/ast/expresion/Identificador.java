/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Referencia a una variable, parametro o arreglo por su nombre.
 */
public class Identificador extends Expresion {

    private final String nombre;

    public Identificador(String nombre, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String etiqueta() {
        return "Id(" + nombre + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.emptyList();
    }
}