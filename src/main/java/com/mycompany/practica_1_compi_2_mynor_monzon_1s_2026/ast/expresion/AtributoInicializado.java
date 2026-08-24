/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
 Para nombre y valor dentro de un literal de estructura
 */

public class AtributoInicializado extends Nodo {

    private final String nombre;
    private final Expresion valor;

    public AtributoInicializado(String nombre, Expresion valor, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public Expresion getValor() {
        return valor;
    }

    @Override
    public String etiqueta() {
        return "Atributo: " + nombre;
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.singletonList(valor);
    }
}