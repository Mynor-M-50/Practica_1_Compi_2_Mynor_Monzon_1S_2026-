/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Conjunto de instrucciones entre llaves.
 *
 * Es el cuerpo de los condicionales, los ciclos y las funciones. Cada
 * bloque abre un ambito nuevo en la tabla de simbolos, aunque en este
 * lenguaje no se pueden declarar variables dentro de el.
 */
public class Bloque extends Instruccion {

    private final List<Instruccion> instrucciones;

    public Bloque(List<Instruccion> instrucciones, int linea, int columna) {
        super(linea, columna);
        this.instrucciones = (instrucciones != null)
                ? new ArrayList<>(instrucciones)
                : new ArrayList<>();
    }

    public List<Instruccion> getInstrucciones() {
        return Collections.unmodifiableList(instrucciones);
    }

    public boolean estaVacio() {
        return instrucciones.isEmpty();
    }

    @Override
    public String etiqueta() {
        return "Bloque";
    }

    @Override
    public List<Nodo> hijos() {
        return new ArrayList<>(instrucciones);
    }
}
