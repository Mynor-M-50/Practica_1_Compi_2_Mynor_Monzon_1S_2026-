/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author mynorm50
 */

/*
 Un nivel de alcance con sus simbolos y una referencia al ambito que
 lo contiene
 
 usa LinkedHashMap para conservar el orden de declaracion, que es
 como conviene mostrar la tabla al graficarla
 */

public class Ambito {

    private final String nombre;
    private final int nivel;
    private final Ambito padre;
    private final Map<String, Simbolo> simbolos = new LinkedHashMap<>();

    public Ambito(String nombre, int nivel, Ambito padre) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.padre = padre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public Ambito getPadre() {
        return padre;
    }

    public Collection<Simbolo> getSimbolos() {
        return simbolos.values();
    }

    
     // Agrega un simbolo. Devuelve false si ya existia uno con el mismo
     // nombre en este mismo ambito, lo que significa declaracion
     // duplicada
     
    public boolean declarar(Simbolo simbolo) {
        if (simbolos.containsKey(simbolo.getNombre())) {
            return false;
        }
        simbolo.setNombreAmbito(nombre);
        simbolo.setNivelAmbito(nivel);
        simbolos.put(simbolo.getNombre(), simbolo);
        return true;
    }

    // Busca solo en este ambito, sin subir a los padres
    public Simbolo buscarLocal(String nombreSimbolo) {
        return simbolos.get(nombreSimbolo);
    }

    // Busca en este ambito y luego hacia arriba en la cadena
    public Simbolo buscar(String nombreSimbolo) {
        Ambito actual = this;
        while (actual != null) {
            Simbolo encontrado = actual.simbolos.get(nombreSimbolo);
            if (encontrado != null) {
                return encontrado;
            }
            actual = actual.padre;
        }
        return null;
    }

    public boolean esGlobal() {
        return padre == null;
    }

    @Override
    public String toString() {
        return "Ambito(" + nombre + ", nivel " + nivel + ", " + simbolos.size() + " simbolos)";
    }
}