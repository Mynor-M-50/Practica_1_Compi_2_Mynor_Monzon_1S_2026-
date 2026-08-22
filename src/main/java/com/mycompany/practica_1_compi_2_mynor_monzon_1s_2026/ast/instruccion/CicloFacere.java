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
 * Ciclo hacer mientras:  facere { } dum (condicion);
 *
 * La condicion se evalua despues del cuerpo, asi que el cuerpo se
 * ejecuta al menos una vez. Por eso el orden de los campos esta
 * invertido respecto a CicloDum.
 */

public class CicloFacere extends Instruccion {

    private final Bloque cuerpo;
    private final Expresion condicion;

    public CicloFacere(Bloque cuerpo, Expresion condicion, int linea, int columna) {
        super(linea, columna);
        this.cuerpo = cuerpo;
        this.condicion = condicion;
    }

    public Bloque getCuerpo() {
        return cuerpo;
    }

    public Expresion getCondicion() {
        return condicion;
    }

    @Override
    public String etiqueta() {
        return "CicloFacere";
    }

    @Override
    public List<Nodo> hijos() {
        return Arrays.asList(cuerpo, condicion);
    }
}