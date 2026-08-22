/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Definicion de una estructura:
 *     structura Persona {
 *         esto nombre : textum;
 *         esto edad : numerus
 *     } finis;
 *
 * Puede aparecer a nivel global dentro de VARIABILES> o dentro del
 * bloque VARIABILES[ ] de una funcion (foro, duda 1).
 */
public class DefinicionEstructura extends Instruccion {

    private final String nombre;
    private final List<CampoEstructura> campos;

    public DefinicionEstructura(String nombre, List<CampoEstructura> campos,
                                int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.campos = (campos != null) ? new ArrayList<>(campos) : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<CampoEstructura> getCampos() {
        return Collections.unmodifiableList(campos);
    }

    /** Busca un campo por nombre. Devuelve null si no existe. */
    public CampoEstructura buscarCampo(String nombreCampo) {
        for (CampoEstructura campo : campos) {
            if (campo.getNombre().equals(nombreCampo)) {
                return campo;
            }
        }
        return null;
    }

    /**
     * Devuelve el nombre del primer campo repetido, o null si todos son
     * distintos. El enunciado exige que los nombres no se repitan.
     */
    public String primerCampoRepetido() {
        List<String> vistos = new ArrayList<>();
        for (CampoEstructura campo : campos) {
            if (vistos.contains(campo.getNombre())) {
                return campo.getNombre();
            }
            vistos.add(campo.getNombre());
        }
        return null;
    }

    @Override
    public String etiqueta() {
        return "Estructura(" + nombre + ")";
    }

    @Override
    public List<Nodo> hijos() {
        return new ArrayList<>(campos);
    }
}