/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

/**
 *
 * @author mynorm50
 */

/**
 Base de todo nodo que ejecuta una accion pero no produce un valor.
 La diferencia con Expresion es justamente esa: una expresion se
 evalua y devuelve algo, una instruccion se ejecuta y no devuelve nada.
 */
public abstract class Instruccion extends Nodo {

    protected Instruccion(int linea, int columna) {
        super(linea, columna);
    }
}