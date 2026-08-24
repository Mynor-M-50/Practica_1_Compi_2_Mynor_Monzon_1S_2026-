/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.traductor;

/**
 *
 * @author mynorm50
 */

/*
 Leyes de transformacion a piglatin.
 
 * LEY DE CONSONANTES
 * Si la palabra empieza con una o mas consonantes, se mueven al final
 * y se agrega el sufijo ay.
 *     fuerza -> uerzafay
 *     tabla  -> ablatay
 
 * LEY DE VOCALES
 * Si la palabra empieza con vocal, solo se agrega el sufijo way.
 *     inicio  -> inicioway
 *     archivo -> archivoway
 
 * LEY PORCINA
 * Las funciones especiales cambian de simbolo.
 *     <<  -> %OINK_OINK
 *     >>  -> %OINK
 
 Se aplica tanto a identificadores como a palabras reservadas. Los
 simbolos del lenguaje no se tocan.
 */

public final class ReglasPiglatin {

    public static final String LEER = "%OINK_OINK";
    public static final String IMPRIMIR = "%OINK";

    private static final String VOCALES = "aeiouAEIOU";

    private ReglasPiglatin() {
        // Clase de utilidades
    }

    /*
     Traduce una palabra aplicando la ley que corresponda
     Si la palabra original estaba completamente en mayusculas, el
     resultado tambien lo queda. Eso mantiene legibles los macadores
     de seccion, que son palabras reservadas en mayusculas
     */
    
    public static String traducir(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return palabra;
        }

        boolean eraMayusculas = esTodoMayusculas(palabra);

        // Los guiones bajos iniciales no son ni vocal ni consonante
        // se conservan al inicio y no participan en el movimiento
        
        int inicio = 0;
        while (inicio < palabra.length() && !esLetra(palabra.charAt(inicio))) {
            inicio++;
        }

        // Palabra sin ninguna letra, no hay nada que mover
        if (inicio == palabra.length()) {
            return palabra;
        }

        String prefijo = palabra.substring(0, inicio);
        String cuerpo = palabra.substring(inicio);

        String resultado;
        if (esVocal(cuerpo.charAt(0))) {
            resultado = aplicarLeyVocales(cuerpo);
        } else {
            resultado = aplicarLeyConsonantes(cuerpo);
        }

        resultado = prefijo + resultado;
        return eraMayusculas ? resultado.toUpperCase() : resultado;
    }

    // La palabra empieza con vocal: solo se agrega el sufijo
    private static String aplicarLeyVocales(String palabra) {
        return palabra + "way";
    }

    
     // La palabra empieza con consonantes: se mueve todo el grupo
     // inicial al final y se agrega el sufijo.
     
    private static String aplicarLeyConsonantes(String palabra) {
        int corte = 0;
        while (corte < palabra.length()
                && esLetra(palabra.charAt(corte))
                && !esVocal(palabra.charAt(corte))) {
            corte++;
        }

        // Palabra formada solo por consonantes
        if (corte == palabra.length()) {
            return palabra + "ay";
        }

        String consonantes = palabra.substring(0, corte);
        String resto = palabra.substring(corte);
        return resto + consonantes + "ay";
    }

    private static boolean esVocal(char caracter) {
        return VOCALES.indexOf(caracter) >= 0;
    }

    private static boolean esLetra(char caracter) {
        return (caracter >= 'a' && caracter <= 'z')
                || (caracter >= 'A' && caracter <= 'Z');
    }

    private static boolean esTodoMayusculas(String palabra) {
        boolean hayLetra = false;
        for (int i = 0; i < palabra.length(); i++) {
            char caracter = palabra.charAt(i);
            if (esLetra(caracter)) {
                hayLetra = true;
                if (Character.isLowerCase(caracter)) {
                    return false;
                }
            }
        }
        return hayLetra;
    }
}