/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Retorno de una funcion:  reddere total;  o  reddere;
 *
 * El valor es opcional porque una funcion actio puede usar reddere sin
 * expresion para salir antes de tiempo. En una funcion ratio el valor
 * es obligatorio y su tipo debe coincidir con el declarado.
 */
public class Reddere extends Instruccion {

    private final Expresion valor;

    public Reddere(Expresion valor, int linea, int columna) {
        super(linea, columna);
        this.valor = valor;
    }

    public Expresion getValor() {
        return valor;
    }

    public boolean tieneValor() {
        return valor != null;
    }

    @Override
    public String etiqueta() {
        return "Reddere";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        if (valor != null) {
            lista.add(valor);
        }
        return lista;
    }
}