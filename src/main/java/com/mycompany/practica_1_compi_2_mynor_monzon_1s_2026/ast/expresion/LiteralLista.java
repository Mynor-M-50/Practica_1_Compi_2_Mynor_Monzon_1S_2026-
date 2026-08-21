/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Lista de valores iniciales de un arreglo:  {1, 1}  o  {verum, falsus}
 *
 * Cuando la lista se escribe sin tipo, como en
 *     series banderas[2] : {verum, falsus};
 * el tipo se infiere de los elementos.
 *
 */
public class LiteralLista extends Expresion {

    private final List<Expresion> valores;

    public LiteralLista(List<Expresion> valores, int linea, int columna) {
        super(linea, columna);
        this.valores = (valores != null) ? new ArrayList<>(valores) : new ArrayList<>();
    }

    public List<Expresion> getValores() {
        return Collections.unmodifiableList(valores);
    }

    public int cantidadValores() {
        return valores.size();
    }

    @Override
    public String etiqueta() {
        return "Lista[" + valores.size() + "]";
    }

    @Override
    public List<Nodo> hijos() {
        return new ArrayList<>(valores);
    }
}