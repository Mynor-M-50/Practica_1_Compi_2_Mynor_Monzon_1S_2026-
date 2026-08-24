/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Operador;

import java.util.Collections;
import java.util.List;
/**
 *
 * @author mynorm50
 */

/*
 Operacion con un solo operando: negacion logica con non, o menos
 unario para invertir el signo.
 */
public class OperacionUnaria extends Expresion {

    private final Operador operador;
    private final Expresion operando;

    public OperacionUnaria(Operador operador, Expresion operando, int linea, int columna) {
        super(linea, columna);
        this.operador = operador;
        this.operando = operando;
    }

    public Operador getOperador() {
        return operador;
    }

    public Expresion getOperando() {
        return operando;
    }

    @Override
    public String etiqueta() {
        return "Unaria(" + operador.getSimbolo() + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.singletonList(operando);
    }
}