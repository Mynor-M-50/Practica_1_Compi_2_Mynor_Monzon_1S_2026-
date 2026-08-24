/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 Corta la ejecucion del ciclo.
 Igual que Perge, solo es valido dentro de un ciclo.
 */
public class Interrumpe extends Instruccion {

    public Interrumpe(int linea, int columna) {
        super(linea, columna);
    }

    @Override
    public String etiqueta() {
        return "Interrumpe";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.emptyList();
    }
}