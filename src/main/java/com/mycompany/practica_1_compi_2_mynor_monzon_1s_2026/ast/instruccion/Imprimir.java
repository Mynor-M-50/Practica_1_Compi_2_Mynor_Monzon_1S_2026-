/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Impresion en consola:  >> "Hola" >> nombre;
 *
 * Se guarda la lista completa de valores porque una sola instruccion
 * puede encadenar varios. Al traducir a PigLatin, el operador >> se
 * convierte en %OINK por la ley porcina.
 */
public class Imprimir extends Instruccion {

    private final List<Expresion> valores;

    public Imprimir(List<Expresion> valores, int linea, int columna) {
        super(linea, columna);
        this.valores = (valores != null) ? new ArrayList<>(valores) : new ArrayList<>();
    }

    public List<Expresion> getValores() {
        return Collections.unmodifiableList(valores);
    }

    @Override
    public String etiqueta() {
        return "Imprimir(" + valores.size() + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return new ArrayList<>(valores);
    }
}