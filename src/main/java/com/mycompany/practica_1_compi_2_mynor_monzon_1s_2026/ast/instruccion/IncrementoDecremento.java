/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Operador;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Incremento o decremento:  contador++  o  contador--
 *
 * El foro confirmo que se pueden usar en cualquier ambito, no solo
 * dentro del ciclo per (duda 7).
 */

public class IncrementoDecremento extends Instruccion {

    private final Expresion objetivo;
    private final Operador operador;

    public IncrementoDecremento(Expresion objetivo, Operador operador,
                                int linea, int columna) {
        super(linea, columna);
        this.objetivo = objetivo;
        this.operador = operador;
    }

    public Expresion getObjetivo() {
        return objetivo;
    }

    public Operador getOperador() {
        return operador;
    }

    public boolean esIncremento() {
        return operador == Operador.INCREMENTO;
    }

    @Override
    public String etiqueta() {
        return "Incremento(" + operador.getSimbolo() + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.singletonList(objetivo);
    }
}