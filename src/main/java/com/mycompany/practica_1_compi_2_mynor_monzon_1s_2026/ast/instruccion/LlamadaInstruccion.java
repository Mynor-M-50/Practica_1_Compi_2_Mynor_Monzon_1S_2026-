/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.LlamadaFuncion;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
 Llamada a funcion usada como instruccion, no como expresion:
 *     atacarCerdos(10, 0.5);

 Envuelve una LlamadaFuncion para que pueda aparecer en una lista de
 instrucciones. El valor de retorno, si lo hay, simplemente se ignora.
 */

public class LlamadaInstruccion extends Instruccion {

    private final LlamadaFuncion llamada;

    public LlamadaInstruccion(LlamadaFuncion llamada, int linea, int columna) {
        super(linea, columna);
        this.llamada = llamada;
    }

    public LlamadaFuncion getLlamada() {
        return llamada;
    }

    @Override
    public String etiqueta() {
        return "LlamadaInstruccion";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.singletonList(llamada);
    }
}