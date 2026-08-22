/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Declaracion de una variable simple:
 *     esto edad : numerus 20;
 *     esto activo : bool verum;
 *     esto activo : verum;              forma especial sin tipo
 *     esto p : Persona { nombre: "X" };
 *
 * La bandera tipoExplicito distingue la forma tipada de la especial.
 * Hace falta para que el traductor a PigLatin reconstruya el codigo
 * tal como lo escribio el usuario.
 */
public class DeclaracionVariable extends Instruccion {

    private final String nombre;
    private final Tipo tipo;
    private final Expresion valorInicial;
    private final boolean tipoExplicito;

    public DeclaracionVariable(String nombre, Tipo tipo, Expresion valorInicial,
                               boolean tipoExplicito, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.tipo = tipo;
        this.valorInicial = valorInicial;
        this.tipoExplicito = tipoExplicito;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Expresion getValorInicial() {
        return valorInicial;
    }

    public boolean tieneValorInicial() {
        return valorInicial != null;
    }

    public boolean isTipoExplicito() {
        return tipoExplicito;
    }

    @Override
    public String etiqueta() {
        return "Declaracion(" + nombre + " : " + tipo + ")";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        if (valorInicial != null) {
            lista.add(valorInicial);
        }
        return lista;
    }
}