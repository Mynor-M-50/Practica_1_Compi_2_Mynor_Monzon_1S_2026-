/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;


/**
 *
 * @author mynorm50
 */

/**
 * Base de todo nodo que produce un valor.
 *
 */
public abstract class Expresion extends Nodo {

    private Tipo tipoResuelto;

    protected Expresion(int linea, int columna) {
        super(linea, columna);
    }

    public Tipo getTipoResuelto() {
        return tipoResuelto;
    }

    public void setTipoResuelto(Tipo tipoResuelto) {
        this.tipoResuelto = tipoResuelto;
    }

    public boolean tieneTipoResuelto() {
        return tipoResuelto != null;
    }
}