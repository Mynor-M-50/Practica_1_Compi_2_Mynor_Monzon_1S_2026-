/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.DefinicionEstructura;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.DefinicionFuncion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;

/**
 *
 * @author mynorm50
 */

/**
 * Entrada de la tabla de simbolos.
 *
 * Guarda todo lo que el analizador semantico necesita saber de un
 * nombre: que tipo tiene, que representa, en que ambito vive y en que
 * linea se declaro. La linea y la columna hacen falta para poder
 * reportar declaraciones duplicadas apuntando a la primera.
 *
 * Las referencias a la definicion de funcion y de estructura solo se
 * llenan cuando el rol corresponde. Permiten verificar argumentos y
 * atributos sin volver a recorrer el AST.
 */

public class Simbolo {

    private final String nombre;
    private final Tipo tipo;
    private final RolSimbolo rol;
    private final int linea;
    private final int columna;

    private String nombreAmbito;
    private int nivelAmbito;

    private DefinicionFuncion definicionFuncion;
    private DefinicionEstructura definicionEstructura;

    public Simbolo(String nombre, Tipo tipo, RolSimbolo rol, int linea, int columna) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.rol = rol;
        this.linea = linea;
        this.columna = columna;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public RolSimbolo getRol() {
        return rol;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getNombreAmbito() {
        return nombreAmbito;
    }

    public void setNombreAmbito(String nombreAmbito) {
        this.nombreAmbito = nombreAmbito;
    }

    public int getNivelAmbito() {
        return nivelAmbito;
    }

    public void setNivelAmbito(int nivelAmbito) {
        this.nivelAmbito = nivelAmbito;
    }

    public DefinicionFuncion getDefinicionFuncion() {
        return definicionFuncion;
    }

    public void setDefinicionFuncion(DefinicionFuncion definicionFuncion) {
        this.definicionFuncion = definicionFuncion;
    }

    public DefinicionEstructura getDefinicionEstructura() {
        return definicionEstructura;
    }

    public void setDefinicionEstructura(DefinicionEstructura definicionEstructura) {
        this.definicionEstructura = definicionEstructura;
    }

    public boolean esFuncion() {
        return rol == RolSimbolo.FUNCION;
    }

    public boolean esEstructura() {
        return rol == RolSimbolo.ESTRUCTURA;
    }

    @Override
    public String toString() {
        return nombre + " : " + tipo + " (" + rol + ", ambito " + nombreAmbito + ")";
    }
}