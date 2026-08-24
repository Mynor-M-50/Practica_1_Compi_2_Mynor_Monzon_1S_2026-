/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
Tabla de simbolos con manejo de ambitos anidados.
 
 Funciona como una pila, entrarAmbito crea un nivel nuevo que apunta
 al anterior como padre, y salirAmbito regresa al padre
 
 Ademas de la estructura jerarquica se mantiene una lista plana con
 todos los simbolos declarados durante el analisis ,esa lista es la
 que se usa para graficar la tabla, porque conserva incluso los
 simbolos de ambitos que ya se cerraron
 */

public class TablaSimbolos {

    private final Ambito global;
    private Ambito actual;
    private final List<Simbolo> historial = new ArrayList<>();
    private int contadorAmbitos;

    public TablaSimbolos() {
        this.global = new Ambito("global", 0, null);
        this.actual = global;
        this.contadorAmbitos = 0;
    }

    public Ambito getGlobal() {
        return global;
    }

    public Ambito getActual() {
        return actual;
    }

    // Abre un ambito nuevo hijo del actual
    public void entrarAmbito(String nombre) {
        contadorAmbitos++;
        actual = new Ambito(nombre, actual.getNivel() + 1, actual);
    }

    // Cierra el ambito actual y regresa al padre
    public void salirAmbito() {
        if (actual.getPadre() != null) {
            actual = actual.getPadre();
        }
    }

    
     // Declara un simbolo en el ambito actual
     // Devuelve false si ya existe uno con ese nombre en el mismo
     // ambito ,re declarar en un ambito interno si es valido, eso es
     // ocultamiento, no error
     
    public boolean declarar(Simbolo simbolo) {
        boolean agregado = actual.declarar(simbolo);
        if (agregado) {
            historial.add(simbolo);
        }
        return agregado;
    }

    // Busca subiendo por la cadena de ambitos
    public Simbolo buscar(String nombre) {
        return actual.buscar(nombre);
    }

    // Busca solo en el ambito actual
    public Simbolo buscarLocal(String nombre) {
        return actual.buscarLocal(nombre);
    }

    // Busca un simbolo que sea funcion. Devuelve null si no lo es
    public Simbolo buscarFuncion(String nombre) {
        Simbolo simbolo = buscar(nombre);
        return (simbolo != null && simbolo.esFuncion()) ? simbolo : null;
    }

    // Busca un simbolo que sea estructura. Devuelve null si no lo es
    public Simbolo buscarEstructura(String nombre) {
        Simbolo simbolo = buscar(nombre);
        return (simbolo != null && simbolo.esEstructura()) ? simbolo : null;
    }

    // Todos los simbolos declarados, en orden de aparicion
    public List<Simbolo> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public int getCantidadAmbitos() {
        return contadorAmbitos + 1;
    }

    // Deja la tabla lista para analizar otro archivo
    public void reiniciar() {
        historial.clear();
        contadorAmbitos = 0;
        actual = global;
    }

    // Representacion en texto, util para depurar en consola
    public String aTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Simbolos declarados: ").append(historial.size())
          .append(System.lineSeparator());
        for (Simbolo simbolo : historial) {
            sb.append("  ")
              .append(String.format("%-16s %-14s %-12s %-10s linea %d",
                      simbolo.getNombre(),
                      simbolo.getTipo(),
                      simbolo.getRol(),
                      simbolo.getNombreAmbito(),
                      simbolo.getLinea()))
              .append(System.lineSeparator());
        }
        return sb.toString();
    }
}