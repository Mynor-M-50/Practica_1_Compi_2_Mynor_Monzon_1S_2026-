/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Operador;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
 Operacion con dos operandos: aritmetica, relacional, de igualdad o
 logica.
 */
public class OperacionBinaria extends Expresion {

    private final Expresion izquierdo;
    private final Operador operador;
    private final Expresion derecho;

    public OperacionBinaria(Expresion izquierdo, Operador operador, Expresion derecho,
                            int linea, int columna) {
        super(linea, columna);
        this.izquierdo = izquierdo;
        this.operador = operador;
        this.derecho = derecho;
    }

    public Expresion getIzquierdo() {
        return izquierdo;
    }

    public Operador getOperador() {
        return operador;
    }

    public Expresion getDerecho() {
        return derecho;
    }

    @Override
    public String etiqueta() {
        return "Operacion(" + operador.getSimbolo() + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return Arrays.asList(izquierdo, derecho);
    }
}
