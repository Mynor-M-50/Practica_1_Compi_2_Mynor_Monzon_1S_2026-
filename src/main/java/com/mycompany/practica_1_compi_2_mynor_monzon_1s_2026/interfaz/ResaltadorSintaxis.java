/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.interfaz;

import com.mycompany.codexlatinus.gramatica.CodexLatinusLexer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;

/**
 *
 * @author mynorm50
 */

/*
 Coloreado de sintaxis para el editor.
 
 En lugar de buscar patrones con expresiones regulares, se reutiliza
 el mismo lexer que genera ANTLR a partir de la gramatica. 

Cada tokenntrae su tipo, su posicion inicial y su posicion final, que es
 justamente lo que hace falta para pintar.
 */

public class ResaltadorSintaxis {

    // Paleta
    private static final Color COLOR_SECCION     = new Color(0x8E, 0x24, 0xAA);
    private static final Color COLOR_RESERVADA   = new Color(0x15, 0x65, 0xC0);
    private static final Color COLOR_TIPO        = new Color(0x00, 0x83, 0x8F);
    private static final Color COLOR_CADENA      = new Color(0x2E, 0x7D, 0x32);
    private static final Color COLOR_NUMERO      = new Color(0xE6, 0x51, 0x00);
    private static final Color COLOR_COMENTARIO  = new Color(0x75, 0x75, 0x75);
    private static final Color COLOR_OPERADOR    = new Color(0xC6, 0x28, 0x28);
    private static final Color COLOR_NORMAL      = new Color(0x21, 0x21, 0x21);

    private final SimpleAttributeSet estiloSeccion    = new SimpleAttributeSet();
    private final SimpleAttributeSet estiloReservada  = new SimpleAttributeSet();
    private final SimpleAttributeSet estiloTipo       = new SimpleAttributeSet();
    private final SimpleAttributeSet estiloCadena     = new SimpleAttributeSet();
    private final SimpleAttributeSet estiloNumero     = new SimpleAttributeSet();
    private final SimpleAttributeSet estiloComentario = new SimpleAttributeSet();
    private final SimpleAttributeSet estiloOperador   = new SimpleAttributeSet();
    private final SimpleAttributeSet estiloNormal     = new SimpleAttributeSet();

    public ResaltadorSintaxis() {
        StyleConstants.setForeground(estiloSeccion, COLOR_SECCION);
        StyleConstants.setBold(estiloSeccion, true);

        StyleConstants.setForeground(estiloReservada, COLOR_RESERVADA);
        StyleConstants.setBold(estiloReservada, true);

        StyleConstants.setForeground(estiloTipo, COLOR_TIPO);
        StyleConstants.setBold(estiloTipo, true);

        StyleConstants.setForeground(estiloCadena, COLOR_CADENA);
        StyleConstants.setForeground(estiloNumero, COLOR_NUMERO);

        StyleConstants.setForeground(estiloComentario, COLOR_COMENTARIO);
        StyleConstants.setItalic(estiloComentario, true);

        StyleConstants.setForeground(estiloOperador, COLOR_OPERADOR);
        StyleConstants.setForeground(estiloNormal, COLOR_NORMAL);
    }

    
     // Aplica el coloreado sobre todo el documento.
     
     // Los comentarios se buscan aparte porque en la gramatica llevan
     // la instruccion skip, lo que significa que el lexer no los entreganen el flujo normal de tokens
     
    public void resaltar(StyledDocument documento, String texto) {
        if (documento == null || texto == null) {
            return;
        }

        documento.setCharacterAttributes(0, texto.length(), estiloNormal, true);

        CodexLatinusLexer lexer = new CodexLatinusLexer(CharStreams.fromString(texto));
        lexer.removeErrorListeners();

        CommonTokenStream flujo = new CommonTokenStream(lexer);
        flujo.fill();

        for (Token token : flujo.getTokens()) {
            if (token.getType() == Token.EOF) {
                continue;
            }
            SimpleAttributeSet estilo = estiloPara(token.getType());
            int inicio = token.getStartIndex();
            int longitud = token.getStopIndex() - token.getStartIndex() + 1;
            if (inicio >= 0 && longitud > 0) {
                documento.setCharacterAttributes(inicio, longitud, estilo, true);
            }
        }

        resaltarComentarios(documento, texto);
    }

    private SimpleAttributeSet estiloPara(int tipoToken) {
        switch (tipoToken) {
            case CodexLatinusLexer.VARIABILES:
            case CodexLatinusLexer.MUNERA:
            case CodexLatinusLexer.MAIOR:
            case CodexLatinusLexer.FIN_PROGRAMA:
                return estiloSeccion;

            case CodexLatinusLexer.ESTO:
            case CodexLatinusLexer.SERIES:
            case CodexLatinusLexer.STRUCTURA:
            case CodexLatinusLexer.FINIS:
            case CodexLatinusLexer.SI:
            case CodexLatinusLexer.ALITER:
            case CodexLatinusLexer.DUM:
            case CodexLatinusLexer.FACERE:
            case CodexLatinusLexer.PER:
            case CodexLatinusLexer.PERGE:
            case CodexLatinusLexer.INTERRUMPE:
            case CodexLatinusLexer.ACTIO:
            case CodexLatinusLexer.RATIO:
            case CodexLatinusLexer.REDDERE:
            case CodexLatinusLexer.NON:
                return estiloReservada;

            case CodexLatinusLexer.NUMERUS:
            case CodexLatinusLexer.DECIMALIS:
            case CodexLatinusLexer.TEXTUM:
            case CodexLatinusLexer.LITTERA:
            case CodexLatinusLexer.BOOL:
                return estiloTipo;

            case CodexLatinusLexer.CADENA:
            case CodexLatinusLexer.CARACTER:
                return estiloCadena;

            case CodexLatinusLexer.ENTERO:
            case CodexLatinusLexer.DECIMAL:
            case CodexLatinusLexer.VERUM:
            case CodexLatinusLexer.FALSUS:
                return estiloNumero;

            case CodexLatinusLexer.MAYORMAYOR:
            case CodexLatinusLexer.MENORMENOR:
            case CodexLatinusLexer.MAS:
            case CodexLatinusLexer.MENOS:
            case CodexLatinusLexer.POR:
            case CodexLatinusLexer.DIV:
            case CodexLatinusLexer.IGUAL:
            case CodexLatinusLexer.IGUALIGUAL:
            case CodexLatinusLexer.DIFERENTE:
            case CodexLatinusLexer.MENOR:
            case CodexLatinusLexer.MAYOR:
            case CodexLatinusLexer.MENORIGUAL:
            case CodexLatinusLexer.MAYORIGUAL:
            case CodexLatinusLexer.AND:
            case CodexLatinusLexer.OR:
            case CodexLatinusLexer.MASMAS:
            case CodexLatinusLexer.MENOSMENOS:
                return estiloOperador;

            default:
                return estiloNormal;
        }
    }

    
     // Los comentarios se descartan en el lexer, asi que se localizan recorriendo el texto 
     //* Se respeta que un ## dentro de una cadena no abre comentario
    
    private void resaltarComentarios(StyledDocument documento, String texto) {
        int i = 0;
        while (i < texto.length()) {
            char actual = texto.charAt(i);

            // Saltar el contenido de las cadenas
            if (actual == '"') {
                i++;
                while (i < texto.length() && texto.charAt(i) != '"') {
                    i++;
                }
                i++;
                continue;
            }

            // Comentario de bloque  ## ... ##
            if (actual == '#' && i + 1 < texto.length() && texto.charAt(i + 1) == '#') {
                int fin = texto.indexOf("##", i + 2);
                int cierre = (fin < 0) ? texto.length() : fin + 2;
                documento.setCharacterAttributes(i, cierre - i, estiloComentario, true);
                i = cierre;
                continue;
            }

            // Comentario de linea  // ...
            if (actual == '/' && i + 1 < texto.length() && texto.charAt(i + 1) == '/') {
                int fin = texto.indexOf('\n', i);
                int cierre = (fin < 0) ? texto.length() : fin;
                documento.setCharacterAttributes(i, cierre - i, estiloComentario, true);
                i = cierre;
                continue;
            }

            i++;
        }
    }
}