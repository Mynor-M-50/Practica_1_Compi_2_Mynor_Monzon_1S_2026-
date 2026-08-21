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
 * Operadores de Latinus
 *
 * La categoria se usa en TablaTipos para decidir que regla de
 * compatibilidad aplicar sin tener que enumerar cada operador.
 */
public enum Operador {

    // Aritmeticos
    SUMA("+", Categoria.ARITMETICO),
    RESTA("-", Categoria.ARITMETICO),
    MULTIPLICACION("*", Categoria.ARITMETICO),
    DIVISION("/", Categoria.ARITMETICO),

    // Relacionales de orden
    MENOR("<", Categoria.RELACIONAL),
    MAYOR(">", Categoria.RELACIONAL),
    MENOR_IGUAL("<=", Categoria.RELACIONAL),
    MAYOR_IGUAL(">=", Categoria.RELACIONAL),

    // Relacionales de igualdad
    IGUALDAD("==", Categoria.IGUALDAD),
    DIFERENCIA("!=", Categoria.IGUALDAD),

    // Logicos binarios
    AND("&&", Categoria.LOGICO),
    OR("||", Categoria.LOGICO),

    // Unarios
    NEGACION("non", Categoria.UNARIO_LOGICO),
    MENOS_UNARIO("-", Categoria.UNARIO_ARITMETICO),

    // Incremento y decremento
    INCREMENTO("++", Categoria.UNARIO_ARITMETICO),
    DECREMENTO("--", Categoria.UNARIO_ARITMETICO);

    public enum Categoria {
        ARITMETICO,
        RELACIONAL,
        IGUALDAD,
        LOGICO,
        UNARIO_LOGICO,
        UNARIO_ARITMETICO
    }

    private final String simbolo;
    private final Categoria categoria;

    Operador(String simbolo, Categoria categoria) {
        this.simbolo = simbolo;
        this.categoria = categoria;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public boolean esBinario() {
        return categoria == Categoria.ARITMETICO
                || categoria == Categoria.RELACIONAL
                || categoria == Categoria.IGUALDAD
                || categoria == Categoria.LOGICO;
    }

    /**
     * Convierte el texto del token en el operador correspondiente.
     * El parametro unario distingue el menos binario del menos unario,
     * que comparten el mismo simbolo.
     */
    public static Operador desdeSimbolo(String simbolo, boolean unario) {
        if (unario) {
            if ("-".equals(simbolo)) {
                return MENOS_UNARIO;
            }
            if ("non".equals(simbolo)) {
                return NEGACION;
            }
            if ("++".equals(simbolo)) {
                return INCREMENTO;
            }
            if ("--".equals(simbolo)) {
                return DECREMENTO;
            }
            return null;
        }

        for (Operador operador : values()) {
            if (operador.esBinario() && operador.simbolo.equals(simbolo)) {
                return operador;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return simbolo;
    }
}