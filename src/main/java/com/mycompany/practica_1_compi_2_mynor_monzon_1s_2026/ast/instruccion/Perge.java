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
 Continua con la siguiente vuelta del ciclo.
 El analizador semantico verifica que solo aparezca dentro de un
 ciclo. Fuera de uno es error.
 */

public class Perge extends Instruccion {

    public Perge(int linea, int columna) {
        super(linea, columna);
    }

    @Override
    public String etiqueta() {
        return "Perge";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.emptyList();
    }
}