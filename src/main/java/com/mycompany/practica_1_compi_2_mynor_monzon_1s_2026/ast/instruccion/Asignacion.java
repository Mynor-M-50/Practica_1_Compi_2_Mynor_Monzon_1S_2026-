/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Asignacion a una variable, elemento de arreglo o atributo:
 *     edad = 23;
 *     nombres[0] = "Hola";
 *     selva.animales[1] = { nombre: "Perro" };
 *
 * El objetivo es una expresion porque puede ser una cadena de accesos.
 * El analizador semantico verifica que sea asignable, es decir que no
 * sea un literal ni el resultado de una operacion.
 */
public class Asignacion extends Instruccion {

    private final Expresion objetivo;
    private final Expresion valor;

    public Asignacion(Expresion objetivo, Expresion valor, int linea, int columna) {
        super(linea, columna);
        this.objetivo = objetivo;
        this.valor = valor;
    }

    public Expresion getObjetivo() {
        return objetivo;
    }

    public Expresion getValor() {
        return valor;
    }

    @Override
    public String etiqueta() {
        return "Asignacion";
    }

    @Override
    public List<Nodo> hijos() {
        return Arrays.asList(objetivo, valor);
    }
}