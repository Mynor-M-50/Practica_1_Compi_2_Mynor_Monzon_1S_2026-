/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.LiteralLista;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Declaracion de un arreglo:
 *     series numeros[2] : numerus {1, 1};
 *     series numeros[2] : numerus;
 *     series banderas[2] : {verum, falsus};   tipo inferido
 *     series animales : Animal;               sin dimension, dentro de estructura
 *
 * La dimension es una expresion y no un entero porque la gramatica
 * acepta cualquier expresion entre los corchetes. El analizador
 * semantico la evalua si es constante.
 */

public class DeclaracionArreglo extends Instruccion {

    private final String nombre;
    private final Tipo tipoElemento;
    private final Expresion dimension;
    private final LiteralLista valoresIniciales;
    private final boolean tipoExplicito;

    public DeclaracionArreglo(String nombre, Tipo tipoElemento, Expresion dimension,
                              LiteralLista valoresIniciales, boolean tipoExplicito,
                              int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.tipoElemento = tipoElemento;
        this.dimension = dimension;
        this.valoresIniciales = valoresIniciales;
        this.tipoExplicito = tipoExplicito;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipoElemento() {
        return tipoElemento;
    }

    public Expresion getDimension() {
        return dimension;
    }

    public boolean tieneDimension() {
        return dimension != null;
    }

    public LiteralLista getValoresIniciales() {
        return valoresIniciales;
    }

    public boolean tieneValoresIniciales() {
        return valoresIniciales != null;
    }

    public boolean isTipoExplicito() {
        return tipoExplicito;
    }

    @Override
    public String etiqueta() {
        return "Arreglo(" + nombre + " : " + tipoElemento + ")";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        if (dimension != null) {
            lista.add(dimension);
        }
        if (valoresIniciales != null) {
            lista.add(valoresIniciales);
        }
        return lista;
    }
}