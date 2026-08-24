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

 //Lectura desde consola:  comandante <<   o solo  <<
 
 //Cuando no hay objetivo, el valor leido se descarta
 //Al traducir a pigLatin, el operador << se convierte en %OINK_OINK

public class Leer extends Instruccion {

    private final Expresion objetivo;

    public Leer(Expresion objetivo, int linea, int columna) {
        super(linea, columna);
        this.objetivo = objetivo;
    }

    public Expresion getObjetivo() {
        return objetivo;
    }

    public boolean tieneObjetivo() {
        return objetivo != null;
    }

    @Override
    public String etiqueta() {
        return tieneObjetivo() ? "Leer" : "Leer(descartado)";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        if (objetivo != null) {
            lista.add(objetivo);
        }
        return lista;
    }
}