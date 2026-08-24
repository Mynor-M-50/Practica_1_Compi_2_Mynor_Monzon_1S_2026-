/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
 Acceso a un atributo de estructura:  persona.nombre
 */
public class AccesoAtributo extends Expresion {

    private final Expresion base;
    private final String nombreAtributo;

    public AccesoAtributo(Expresion base, String nombreAtributo, int linea, int columna) {
        super(linea, columna);
        this.base = base;
        this.nombreAtributo = nombreAtributo;
    }

    public Expresion getBase() {
        return base;
    }

    public String getNombreAtributo() {
        return nombreAtributo;
    }

    @Override
    public String etiqueta() {
        return "Atributo(" + nombreAtributo + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.singletonList(base);
    }
}