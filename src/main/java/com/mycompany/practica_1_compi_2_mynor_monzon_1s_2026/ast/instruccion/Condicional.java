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

/*
 Cadena completa de condicionales:
 *     si (...) { }
 *     aliter (...) { }
 *     aliter { }
 *     finis;
 *
 La primera rama es el si. Las siguientes son los aliter con
 condicion. El aliter sin condicion, si existe, va en ramaFinal.
 */

public class Condicional extends Instruccion {

    private final List<RamaCondicional> ramas;
    private final Bloque ramaFinal;

    public Condicional(List<RamaCondicional> ramas, Bloque ramaFinal,
                       int linea, int columna) {
        super(linea, columna);
        this.ramas = (ramas != null) ? new ArrayList<>(ramas) : new ArrayList<>();
        this.ramaFinal = ramaFinal;
    }

    public List<RamaCondicional> getRamas() {
        return Collections.unmodifiableList(ramas);
    }

    public Bloque getRamaFinal() {
        return ramaFinal;
    }

    public boolean tieneRamaFinal() {
        return ramaFinal != null;
    }

    @Override
    public String etiqueta() {
        return "Condicional(" + ramas.size() + " ramas)";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>(ramas);
        if (ramaFinal != null) {
            lista.add(ramaFinal);
        }
        return lista;
    }
}