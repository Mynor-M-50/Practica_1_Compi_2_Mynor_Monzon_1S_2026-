/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 Definicion de una funcion, tanto actio como ratio.
 * 
 *     actio saludar(esto nombre : textum) { ... } finis;
 *     ratio numerus calcular(esto x : numerus) { ... } finis;
 *
 Las declaraciones locales se guardan aparte del cuerpo porque el
 enunciado exige que solo puedan ir al inicio, dentro del bloque
 VARIABILES[ ]
 */

public class DefinicionFuncion extends Instruccion {

    private final String nombre;
    private final Tipo tipoRetorno;
    private final List<Parametro> parametros;
    private final List<Instruccion> declaracionesLocales;
    private final List<Instruccion> cuerpo;

    public DefinicionFuncion(String nombre, Tipo tipoRetorno, List<Parametro> parametros,
                             List<Instruccion> declaracionesLocales, List<Instruccion> cuerpo,
                             int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.tipoRetorno = tipoRetorno;
        this.parametros = (parametros != null) ? new ArrayList<>(parametros) : new ArrayList<>();
        this.declaracionesLocales = (declaracionesLocales != null)
                ? new ArrayList<>(declaracionesLocales)
                : new ArrayList<>();
        this.cuerpo = (cuerpo != null) ? new ArrayList<>(cuerpo) : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipoRetorno() {
        return tipoRetorno;
    }

    public List<Parametro> getParametros() {
        return Collections.unmodifiableList(parametros);
    }

    public List<Instruccion> getDeclaracionesLocales() {
        return Collections.unmodifiableList(declaracionesLocales);
    }

    public List<Instruccion> getCuerpo() {
        return Collections.unmodifiableList(cuerpo);
    }

    public int cantidadParametros() {
        return parametros.size();
    }

    /** Una funcion actio no devuelve valor. */
    public boolean esActio() {
        return tipoRetorno == null || tipoRetorno.esVacio();
    }

    @Override
    public String etiqueta() {
        String clase = esActio() ? "actio" : "ratio " + tipoRetorno;
        return "Funcion(" + clase + " " + nombre + ")";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        lista.addAll(parametros);
        lista.addAll(declaracionesLocales);
        lista.addAll(cuerpo);
        return lista;
    }
}