/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos;

/**
 *
 * @author mynorm50
 */

/**
 * tipos primitivos del lenguaje Codex Latinus.
 *
 * El nivel representa la jerarquia de conversion implicita que define
 * el enunciado:
 *
 *     textum (5) > decimalis (4) > numerus (3) > littera (2) > bool (1)
 *
 * El resultado de una operacion entre dos tipos toma siempre el nivel
 * mas alto de los dos
 */
public enum TipoPrimitivo {

    TEXTUM(5, "textum"),
    DECIMALIS(4, "decimalis"),
    NUMERUS(3, "numerus"),
    LITTERA(2, "littera"),
    BOOLEANO(1, "bool"),

    /** Instancia de una estructura definida por el usuario. */
    ESTRUCTURA(0, "estructura"),

    /** Retorno de una funcion actio, que no devuelve valor. */
    VACIO(0, "void"),

    /**
     * Tipo de error. Se propaga sin generar mensajes nuevos para
     * evitar cascadas de errores a partir de una sola falla.
     */
    ERROR(0, "error");

    private final int nivel;
    private final String nombre;

    TipoPrimitivo(int nivel, String nombre) {
        this.nivel = nivel;
        this.nombre = nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public String getNombre() {
        return nombre;
    }

    /** Indica si el tipo entra en la jerarquia de conversion implicita. */
    public boolean participaEnJerarquia() {
        return nivel > 0;
    }

    /** Tipos sobre los que tiene sentido hacer aritmetica pura. */
    public boolean esNumerico() {
        return this == NUMERUS || this == DECIMALIS;
    }

    /**
     * Tipos que se pueden comparar con menor, mayor, menor igual y
     * mayor igual. Se incluye littera porque se compara por su valor
     * en la tabla de caracteres.
     */
    public boolean esOrdenable() {
        return this == NUMERUS || this == DECIMALIS || this == LITTERA;
    }

    /**
     * Busca el tipo primitivo que corresponde a una palabra reservada.
     * Devuelve null si el nombre no es un tipo primitivo, lo que
     * significa que se trata del nombre de una estructura.
     */
    public static TipoPrimitivo desdeNombre(String nombre) {
        for (TipoPrimitivo tipo : values()) {
            if (tipo.participaEnJerarquia() && tipo.nombre.equals(nombre)) {
                return tipo;
            }
        }
        return null;
    }

    /** Devuelve el tipo de mayor nivel jerarquico entre los dos. */
    public static TipoPrimitivo mayorNivel(TipoPrimitivo a, TipoPrimitivo b) {
        return (a.nivel >= b.nivel) ? a : b;
    }

    @Override
    public String toString() {
        return nombre;
    }
}