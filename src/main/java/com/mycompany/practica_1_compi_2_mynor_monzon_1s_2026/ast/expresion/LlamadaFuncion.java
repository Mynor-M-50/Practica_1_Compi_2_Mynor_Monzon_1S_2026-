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
 * Llamada a una funcion:  calcularPoder(fuerza, 0.5)
 *
 * Es una expresion porque puede aparecer dentro de otra expresion. Si
 * la funcion es de tipo actio y se usa donde se espera un valor, el
 * analizador semantico lo reporta como error.
 */

public class LlamadaFuncion extends Expresion {

    private final String nombre;
    private final List<Expresion> argumentos;

    public LlamadaFuncion(String nombre, List<Expresion> argumentos, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.argumentos = (argumentos != null)
                ? new ArrayList<>(argumentos)
                : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Expresion> getArgumentos() {
        return Collections.unmodifiableList(argumentos);
    }

    public int cantidadArgumentos() {
        return argumentos.size();
    }

    @Override
    public String etiqueta() {
        return "Llamada(" + nombre + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return new ArrayList<>(argumentos);
    }
}