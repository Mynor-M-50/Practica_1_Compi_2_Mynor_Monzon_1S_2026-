/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores;

import com.mycompany.codexlatinus.gramatica.CodexLatinusParser;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.IntervalSet;

/**
 *
 * @author mynorm50
 */

/*
 Recuperacion de errores en modo panico.
 
 Cuando el parser encuentra un error, en vez de detenerse descarta
 tokens hasta llegar a uno que marque el final de una construccion
 Desde ahi vuelve a intentar el analisis
 */

public class ModoPanico extends DefaultErrorStrategy {

    private static final IntervalSet TOKENS_SINCRONIZACION = new IntervalSet(
            CodexLatinusParser.PYC,
            CodexLatinusParser.FINIS,
            CodexLatinusParser.FIN_PROGRAMA,
            CodexLatinusParser.LLAVE_C
    );

    private int vecesRecuperado;

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        // Marca el error en toda la cadena de reglas activas
        for (ParserRuleContext contexto = recognizer.getContext();
             contexto != null;
             contexto = contexto.getParent()) {
            contexto.exception = e;
        }

        TokenStream entrada = recognizer.getInputStream();
        int tipoActual = entrada.LA(1);

        // Descarta tokens hasta encontrar un punto de sincronizacion
        while (tipoActual != Token.EOF && !TOKENS_SINCRONIZACION.contains(tipoActual)) {
            recognizer.consume();
            tipoActual = entrada.LA(1);
        }

        // El punto y coma se consume tambien: ya cerro la instruccion danada y el analisis debe seguir con la siguiente
        if (tipoActual == CodexLatinusParser.PYC) {
            recognizer.consume();
        }

        vecesRecuperado++;
    }

    public int getVecesRecuperado() {
        return vecesRecuperado;
    }

    public void reiniciarContador() {
        vecesRecuperado = 0;
    }
}