/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores;

/**
 *
 * @author mynorm50
 */

/**
 * Un error detectado durante la compilacion.
 *
 * Se guarda el token o lexema que lo provoco ademas del mensaje,
 * porque el reporte de errores que pide el enunciado necesita mostrar
 * exactamente que fue lo que no se pudo procesar.
 */

public class ErrorCompilacion {

    private final TipoError tipo;
    private final String mensaje;
    private final String lexema;
    private final int linea;
    private final int columna;

    public ErrorCompilacion(TipoError tipo, String mensaje, String lexema,
                            int linea, int columna) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    public ErrorCompilacion(TipoError tipo, String mensaje, int linea, int columna) {
        this(tipo, mensaje, null, linea, columna);
    }

    public TipoError getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tipo).append("] ");
        sb.append("Linea ").append(linea).append(", columna ").append(columna);
        if (lexema != null && !lexema.isEmpty()) {
            sb.append(" cerca de '").append(lexema).append("'");
        }
        sb.append(": ").append(mensaje);
        return sb.toString();
    }
}