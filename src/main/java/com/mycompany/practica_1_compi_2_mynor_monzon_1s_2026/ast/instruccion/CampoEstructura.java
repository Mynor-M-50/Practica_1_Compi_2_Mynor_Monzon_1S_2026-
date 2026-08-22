/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Campo dentro de la definicion de una estructura:
 *     esto nombre : textum
 *     series animales : Animal
 *
 * No lleva valor inicial: el enunciado prohibe valores por defecto al
 * definir la estructura (confirmado en el foro, duda 3).
 *
 * La dimension puede venir vacia, porque al definir la estructura no
 * hace falta conocerla (foro, duda 5). Se fija hasta declarar la
 * variable.
 */

public class CampoEstructura extends Nodo {

    private final String nombre;
    private final Tipo tipo;
    private final Expresion dimension;

    public CampoEstructura(String nombre, Tipo tipo, Expresion dimension,
                           int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.tipo = tipo;
        this.dimension = dimension;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Expresion getDimension() {
        return dimension;
    }

    public boolean esArreglo() {
        return tipo != null && tipo.esArreglo();
    }

    @Override
    public String etiqueta() {
        return "Campo(" + nombre + " : " + tipo + ")";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        if (dimension != null) {
            lista.add(dimension);
        }
        return lista;
    }
}