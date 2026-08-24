/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.DefinicionFuncion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Instruccion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
 Raiz del AST. Refleja las tres secciones del archivo .lat
 */

public class NodoPrograma extends Nodo {

    private final List<Instruccion> declaracionesGlobales;
    private final List<DefinicionFuncion> funciones;
    private final List<Instruccion> instruccionesPrincipales;

    public NodoPrograma(List<Instruccion> declaracionesGlobales,
                        List<DefinicionFuncion> funciones,
                        List<Instruccion> instruccionesPrincipales,
                        int linea, int columna) {
        super(linea, columna);
        this.declaracionesGlobales = (declaracionesGlobales != null)
                ? new ArrayList<>(declaracionesGlobales) : new ArrayList<>();
        this.funciones = (funciones != null)
                ? new ArrayList<>(funciones) : new ArrayList<>();
        this.instruccionesPrincipales = (instruccionesPrincipales != null)
                ? new ArrayList<>(instruccionesPrincipales) : new ArrayList<>();
    }

    public List<Instruccion> getDeclaracionesGlobales() {
        return Collections.unmodifiableList(declaracionesGlobales);
    }

    public List<DefinicionFuncion> getFunciones() {
        return Collections.unmodifiableList(funciones);
    }

    public List<Instruccion> getInstruccionesPrincipales() {
        return Collections.unmodifiableList(instruccionesPrincipales);
    }

    // Busca una funcion por nombre devuelve null si no existe
    public DefinicionFuncion buscarFuncion(String nombre) {
        for (DefinicionFuncion funcion : funciones) {
            if (funcion.getNombre().equals(nombre)) {
                return funcion;
            }
        }
        return null;
    }

    @Override
    public String etiqueta() {
        return "Programa";
    }

    @Override
    public List<Nodo> hijos() {
        List<Nodo> lista = new ArrayList<>();
        lista.addAll(declaracionesGlobales);
        lista.addAll(funciones);
        lista.addAll(instruccionesPrincipales);
        return lista;
    }
}