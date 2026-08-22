/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */


/**
 * Parametro de una funcion:  esto fuerza : numerus
 *
 * No lleva valor inicial: el valor lo aporta la llamada.
 */

public class Parametro extends Nodo {

    private final String nombre;
    private final Tipo tipo;

    public Parametro(String nombre, Tipo tipo, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    @Override
    public String etiqueta() {
        return "Parametro(" + nombre + " : " + tipo + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.emptyList();
    }
}