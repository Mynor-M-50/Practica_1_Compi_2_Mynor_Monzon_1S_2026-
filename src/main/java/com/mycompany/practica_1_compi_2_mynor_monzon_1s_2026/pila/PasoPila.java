/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.pila;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
 imahen de pila en un momento del analisis
 */

public class PasoPila {

    private final int numero;
    private final OperacionPila operacion;
    private final String simbolo;
    private final List<String> contenido;
    private final int linea;
    private final String detalle;

    public PasoPila(int numero, OperacionPila operacion, String simbolo,
                    List<String> contenido, int linea, String detalle) {
        this.numero = numero;
        this.operacion = operacion;
        this.simbolo = simbolo;
        this.contenido = new ArrayList<>(contenido);
        this.linea = linea;
        this.detalle = detalle;
    }

    public int getNumero() {
        return numero;
    }

    public OperacionPila getOperacion() {
        return operacion;
    }

    public String getSimbolo() {
        return simbolo;
    }

    // Contenido de la pila en este paso, del fondo hacia la cima
    public List<String> getContenido() {
        return Collections.unmodifiableList(contenido);
    }

    public int getLinea() {
        return linea;
    }

    public String getDetalle() {
        return detalle;
    }

    public int getAltura() {
        return contenido.size();
    }

    public String getCima() {
        return contenido.isEmpty() ? "" : contenido.get(contenido.size() - 1);
    }

    // Linea del log de operaciones.
    public String comoLinea() {
        return String.format("%4d  %-8s  %-24s  linea %-4d  %s",
                numero, operacion.getNombre(), simbolo, linea, detalle);
    }

    // Contenido en una sola linea, util para mostrarlo en una tabla. 
    public String contenidoComoTexto() {
        return String.join(" ", contenido);
    }

    @Override
    public String toString() {
        return comoLinea();
    }
}