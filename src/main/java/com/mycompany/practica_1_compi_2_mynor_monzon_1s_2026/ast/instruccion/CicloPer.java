/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Ciclo para:  per (inicializacion; condicion; actualizacion) { }
 *
 * La inicializacion puede ser una declaracion o una asignacion, y la
 * actualizacion puede ser un incremento o una asignacion. Por eso
 * ambas se guardan como Instruccion generica.
 *
 * La variable declarada en la inicializacion vive solo dentro del
 * ciclo, asi que el ciclo abre su propio ambito.
 */

public class CicloPer extends Instruccion {

    private final Instruccion inicializacion;
    private final Expresion condicion;
    private final Instruccion actualizacion;
    private final Bloque cuerpo;

    public CicloPer(Instruccion inicializacion, Expresion condicion,
                    Instruccion actualizacion, Bloque cuerpo,
                    int linea, int columna) {
        super(linea, columna);
        this.inicializacion = inicializacion;
        this.condicion = condicion;
        this.actualizacion = actualizacion;
        this.cuerpo = cuerpo;
    }

    public Instruccion getInicializacion() {
        return inicializacion;
    }

    public Expresion getCondicion() {
        return condicion;
    }

    public Instruccion getActualizacion() {
        return actualizacion;
    }

    public Bloque getCuerpo() {
        return cuerpo;
    }

    @Override
    public String etiqueta() {
        return "CicloPer";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        if (inicializacion != null) {
            lista.add(inicializacion);
        }
        if (condicion != null) {
            lista.add(condicion);
        }
        if (actualizacion != null) {
            lista.add(actualizacion);
        }
        if (cuerpo != null) {
            lista.add(cuerpo);
        }
        return lista;
    }
}