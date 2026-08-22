/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

/**
 *
 * @author mynorm50
 */

/**
 * Captura los errores que reporta ANTLR y los manda al recolector.
 *
 * Por defecto ANTLR imprime los errores en la salida de error estandar,
 * lo cual no sirve para mostrarlos en una interfaz grafica. Al quitar
 * los listeners por defecto y poner este en su lugar, los errores
 * quedan disponibles como objetos.
 *
 * La misma clase sirve para el lexer y para el parser: se distingue
 * segun quien reporta.
 */
public class EscuchaErrores extends BaseErrorListener {

    private final RecolectorErrores recolector;

    public EscuchaErrores(RecolectorErrores recolector) {
        this.recolector = recolector;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object simboloOfensivo,
                            int linea,
                            int columna,
                            String mensaje,
                            RecognitionException e) {

        boolean vieneDelLexer = recognizer instanceof Lexer;
        TipoError tipo = vieneDelLexer ? TipoError.LEXICO : TipoError.SINTACTICO;

        String lexema = null;
        if (simboloOfensivo instanceof Token) {
            lexema = ((Token) simboloOfensivo).getText();
        }

        String traducido = traducir(mensaje, vieneDelLexer);

        recolector.agregar(tipo, traducido, lexema, linea, columna + 1);
    }

    /**
     * Traduce los mensajes mas comunes de ANTLR al espanol.
     * Si el mensaje no coincide con ninguno conocido se deja tal cual.
     */
    private String traducir(String mensaje, boolean vieneDelLexer) {
        if (mensaje == null) {
            return "Error desconocido";
        }
        if (vieneDelLexer && mensaje.startsWith("token recognition error")) {
            return "Caracter no reconocido por el lenguaje";
        }
        if (mensaje.startsWith("missing")) {
            return "Falta un simbolo: " + mensaje.substring(8);
        }
        if (mensaje.startsWith("extraneous input")) {
            return "Simbolo inesperado: " + mensaje.substring(17);
        }
        if (mensaje.startsWith("mismatched input")) {
            return "Simbolo no esperado en esta posicion: " + mensaje.substring(17);
        }
        if (mensaje.startsWith("no viable alternative")) {
            return "La instruccion no corresponde a ninguna forma valida del lenguaje";
        }
        return mensaje;
    }
}