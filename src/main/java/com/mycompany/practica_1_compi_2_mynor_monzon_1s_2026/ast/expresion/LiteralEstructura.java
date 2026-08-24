/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mynorm50
 */


/*
 Instancia de estructura escrita entre llaves
*/

public class LiteralEstructura extends Expresion {

    private final List<AtributoInicializado> atributos;

    public LiteralEstructura(List<AtributoInicializado> atributos, int linea, int columna) {
        super(linea, columna);
        this.atributos = (atributos != null) ? new ArrayList<>(atributos) : new ArrayList<>();
    }

    public List<AtributoInicializado> getAtributos() {
        return Collections.unmodifiableList(atributos);
    }

    // Busca un atributo por nombre. Devuelve null si no esta
    public AtributoInicializado buscarAtributo(String nombre) {
        for (AtributoInicializado atributo : atributos) {
            if (atributo.getNombre().equals(nombre)) {
                return atributo;
            }
        }
        return null;
    }

    
      //Devuelve el nombre del primer atributo que aparece repetido, o
      //null si no hay repetidos
     
    public String primerAtributoRepetido() {
        List<String> vistos = new ArrayList<>();
        for (AtributoInicializado atributo : atributos) {
            if (vistos.contains(atributo.getNombre())) {
                return atributo.getNombre();
            }
            vistos.add(atributo.getNombre());
        }
        return null;
    }

    @Override
    public String etiqueta() {
        return "Estructura{" + atributos.size() + " atributos}";
    }

    @Override
    public List<Nodo> hijos() {
        return new ArrayList<>(atributos);
    }
}
