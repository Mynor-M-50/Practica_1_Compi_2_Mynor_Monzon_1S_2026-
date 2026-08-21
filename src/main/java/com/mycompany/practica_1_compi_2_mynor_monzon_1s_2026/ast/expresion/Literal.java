/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.TipoPrimitivo;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Valor escrito directamente en el codigo: 10, 9.81, "texto", 'a',
 * verum, falsus.
 */
public class Literal extends Expresion {

    private final Object valor;
    private final TipoPrimitivo tipoLiteral;

    private Literal(Object valor, TipoPrimitivo tipoLiteral, int linea, int columna) {
        super(linea, columna);
        this.valor = valor;
        this.tipoLiteral = tipoLiteral;
    }

    // ------------------------------------------------------------
    // Metodos de fabrica, uno por cada token literal de la gramatica
    // ------------------------------------------------------------

    public static Literal entero(String texto, int linea, int columna) {
        return new Literal(Integer.parseInt(texto), TipoPrimitivo.NUMERUS, linea, columna);
    }

    public static Literal decimal(String texto, int linea, int columna) {
        return new Literal(Double.parseDouble(texto), TipoPrimitivo.DECIMALIS, linea, columna);
    }

    /** Recibe el texto con comillas incluidas y las quita. */
    public static Literal cadena(String texto, int linea, int columna) {
        return new Literal(limpiarDelimitadores(texto), TipoPrimitivo.TEXTUM, linea, columna);
    }

    /** Recibe el texto con comillas simples incluidas y las quita. */
    public static Literal caracter(String texto, int linea, int columna) {
        String contenido = limpiarDelimitadores(texto);
        char valor = contenido.isEmpty() ? ' ' : contenido.charAt(0);
        return new Literal(valor, TipoPrimitivo.LITTERA, linea, columna);
    }

    public static Literal booleano(boolean valor, int linea, int columna) {
        return new Literal(valor, TipoPrimitivo.BOOLEANO, linea, columna);
    }

    /**
     * Quita el primer y ultimo caracter, que son los delimitadores, y
     * traduce las secuencias de escape mas comunes.
     */
    private static String limpiarDelimitadores(String texto) {
        if (texto == null || texto.length() < 2) {
            return "";
        }
        String contenido = texto.substring(1, texto.length() - 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contenido.length(); i++) {
            char c = contenido.charAt(i);
            if (c == '\\' && i + 1 < contenido.length()) {
                char siguiente = contenido.charAt(i + 1);
                switch (siguiente) {
                    case 'n': sb.append('\n'); i++; break;
                    case 't': sb.append('\t'); i++; break;
                    case 'r': sb.append('\r'); i++; break;
                    case '\\': sb.append('\\'); i++; break;
                    case '"': sb.append('"'); i++; break;
                    case '\'': sb.append('\''); i++; break;
                    default: sb.append(c); break;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------

    public Object getValor() {
        return valor;
    }

    public TipoPrimitivo getTipoLiteral() {
        return tipoLiteral;
    }

    /**
     * Devuelve el literal tal como se escribiria en el codigo fuente.
     * Lo usa el traductor a PigLatin para reconstruir el archivo.
     */
    public String comoCodigoFuente() {
        switch (tipoLiteral) {
            case TEXTUM:
                return "\"" + valor + "\"";
            case LITTERA:
                return "'" + valor + "'";
            case BOOLEANO:
                return Boolean.TRUE.equals(valor) ? "verum" : "falsus";
            default:
                return String.valueOf(valor);
        }
    }

    @Override
    public String etiqueta() {
        return "Literal(" + tipoLiteral.getNombre() + ": " + valor + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return Collections.emptyList();
    }
}