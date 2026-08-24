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

/*
 Ciclo mientras:  dum (condicion) { } finis;
 
 La condicion se evalua antes de cada vuelta, asi que el cuerpo puede
 no ejecutarse nunca.
 */
public class CicloDum extends Instruccion {

    private final Expresion condicion;
    private final Bloque cuerpo;

    public CicloDum(Expresion condicion, Bloque cuerpo, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.cuerpo = cuerpo;
    }

    public Expresion getCondicion() {
        return condicion;
    }

    public Bloque getCuerpo() {
        return cuerpo;
    }

    @Override
    public String etiqueta() {
        return "CicloDum";
    }

    @Override
    public List<Nodo> hijos() {
        return Arrays.asList(condicion, cuerpo);
    }
}