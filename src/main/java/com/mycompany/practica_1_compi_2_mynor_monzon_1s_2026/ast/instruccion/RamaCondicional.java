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
 Una rama con condicion dentro de un condicional
 Cubre tanto el si inicial como cada aliter que trae condicion
 El aliter final sin condicion no usa esta clase, se guarda aparte
 en Condicional.
 */

public class RamaCondicional extends Nodo {

    private final Expresion condicion;
    private final Bloque bloque;

    public RamaCondicional(Expresion condicion, Bloque bloque, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.bloque = bloque;
    }

    public Expresion getCondicion() {
        return condicion;
    }

    public Bloque getBloque() {
        return bloque;
    }

    @Override
    public String etiqueta() {
        return "Rama";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        if (condicion != null) {
            lista.add(condicion);
        }
        if (bloque != null) {
            lista.add(bloque);
        }
        return lista;
    }
}