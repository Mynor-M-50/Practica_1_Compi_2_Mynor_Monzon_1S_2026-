/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Acceso a un elemento de arreglo:  nombres[0]  o  selva.animales[1]
 *
 * La base es una expresion y no un simple nombre porque los accesos se
 * encadenan.
 */
public class AccesoArreglo extends Expresion {

    private final Expresion base;
    private final Expresion indice;

    public AccesoArreglo(Expresion base, Expresion indice, int linea, int columna) {
        super(linea, columna);
        this.base = base;
        this.indice = indice;
    }

    public Expresion getBase() {
        return base;
    }

    public Expresion getIndice() {
        return indice;
    }

    /**
     * Devuelve el nombre de la variable raiz de la cadena de accesos.
     * Sirve para reportar errores y para la reinterpretacion de
     * dimensiones descrita arriba.
     */
    public String nombreRaiz() {
        Expresion actual = base;
        while (true) {
            if (actual instanceof Identificador) {
                return ((Identificador) actual).getNombre();
            }
            if (actual instanceof AccesoArreglo) {
                actual = ((AccesoArreglo) actual).getBase();
            } else if (actual instanceof AccesoAtributo) {
                actual = ((AccesoAtributo) actual).getBase();
            } else {
                return null;
            }
        }
    }

    @Override
    public String etiqueta() {
        return "AccesoArreglo";
    }

    @Override
    public List<Nodo> hijos() {
        return Arrays.asList(base, indice);
    }
}