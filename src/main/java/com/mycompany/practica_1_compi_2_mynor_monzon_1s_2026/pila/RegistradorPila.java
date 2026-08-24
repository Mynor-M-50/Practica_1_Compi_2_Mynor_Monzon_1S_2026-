/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.pila;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
simula la pila de un analizador ascendente usando los eventos del
 Listener de ANTLR
 */

public class RegistradorPila implements ParseTreeListener {

    private final String[] nombresReglas;

    // Pila real que se va modificando durante el recorrido. 
    private final List<String> pila = new ArrayList<>();

    // Altura de la pila al entrar a cada regla activa. 
    private final Deque<Integer> alturas = new ArrayDeque<>();

    // Historial completo, uno por cada operacion realizada. 
    private final List<PasoPila> pasos = new ArrayList<>();

    private int contador;

    public RegistradorPila(String[] nombresReglas) {
        this.nombresReglas = nombresReglas;
    }

    // Eventos del recorrido-----------------------------------------------

    @Override
    public void visitTerminal(TerminalNode nodo) {
        Token token = nodo.getSymbol();

        // El fin de archivo no representa un simbolo de la gramatica
        if (token.getType() == Token.EOF) {
            return;
        }

        String lexema = token.getText();
        pila.add(lexema);

        registrar(OperacionPila.SHIFT, lexema, token.getLine(),
                "Se apila el terminal '" + lexema + "'");
    }

    @Override
    public void visitErrorNode(ErrorNode nodo) {
        Token token = nodo.getSymbol();
        String lexema = (token != null) ? token.getText() : "?";
        int linea = (token != null) ? token.getLine() : 0;

        registrar(OperacionPila.SHIFT, lexema, linea,
                "Token con error, no se apila");
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        // Se recuerda desde donde empiezan los simbolos de esta regla
        alturas.push(pila.size());
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        int base = alturas.isEmpty() ? 0 : alturas.pop();
        String noTerminal = nombreRegla(ctx.getRuleIndex());

        // Todo lo apilado desde la base pertenece a esta regla
        int cantidad = pila.size() - base;
        List<String> consumidos = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            consumidos.add(0, pila.remove(pila.size() - 1));
        }

        pila.add(noTerminal);

        String detalle = consumidos.isEmpty()
                ? "Se reduce una regla vacia a " + noTerminal
                : "Se reemplazan [" + String.join(" ", consumidos) + "] por " + noTerminal;

        int linea = (ctx.getStart() != null) ? ctx.getStart().getLine() : 0;
        registrar(OperacionPila.REPLACE, noTerminal, linea, detalle);

        // La regla inicial cerro: el analisis termino
        if (alturas.isEmpty()) {
            registrar(OperacionPila.ACCEPT, noTerminal, linea,
                    "La entrada pertenece al lenguaje");
        }
    }

    // Consulta del historial-------------------------------------------------

    private void registrar(OperacionPila operacion, String simbolo,
                           int linea, String detalle) {
        contador++;
        pasos.add(new PasoPila(contador, operacion, simbolo, pila, linea, detalle));
    }

    private String nombreRegla(int indice) {
        if (nombresReglas != null && indice >= 0 && indice < nombresReglas.length) {
            return nombresReglas[indice];
        }
        return "regla" + indice;
    }

    public List<PasoPila> getPasos() {
        return Collections.unmodifiableList(pasos);
    }

    public int cantidadPasos() {
        return pasos.size();
    }

    /** Devuelve un paso por su indice, empezando en cero. */
    public PasoPila getPaso(int indice) {
        if (indice < 0 || indice >= pasos.size()) {
            return null;
        }
        return pasos.get(indice);
    }

    /** Log completo de operaciones, listo para mostrar en un area de texto. */
    public String getLog() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%4s  %-8s  %-24s  %-10s  %s",
                "No.", "Accion", "Simbolo", "Linea", "Detalle"));
        sb.append(System.lineSeparator());
        for (PasoPila paso : pasos) {
            sb.append(paso.comoLinea()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public void reiniciar() {
        pila.clear();
        alturas.clear();
        pasos.clear();
        contador = 0;
    }
}