/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.analizador;

import com.mycompany.codexlatinus.gramatica.CodexLatinusLexer;
import com.mycompany.codexlatinus.gramatica.CodexLatinusParser;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.NodoPrograma;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores.EscuchaErrores;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores.ModoPanico;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores.RecolectorErrores;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores.TipoError;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.semantico.AnalizadorSemantico;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos.TablaSimbolos;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author mynorm50
 */

/**
 * Punto unico de entrada al compilador.
 *
 * Encadena las fases en orden y corta cuando ya no tiene sentido
 * seguir: si hubo errores sintacticos, el AST estaria incompleto y
 * analizarlo produciria errores falsos.
 *
 * La interfaz grafica solo necesita llamar a compilar y leer el
 * resultado.
 */

public class Compilador {

    private final RecolectorErrores errores = new RecolectorErrores();

    private ParseTree parseTree;
    private NodoPrograma programa;
    private TablaSimbolos tablaSimbolos;

    public RecolectorErrores getErrores() {
        return errores;
    }

    public ParseTree getParseTree() {
        return parseTree;
    }

    public NodoPrograma getPrograma() {
        return programa;
    }

    public TablaSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }

    /**
     * Ejecuta todas las fases sobre el codigo fuente recibido.
     * Devuelve true si el programa quedo libre de errores.
     */
    public boolean compilar(String fuente) {
        reiniciar();

        // ---- Analisis lexico y sintactico ----
        CharStream entrada = CharStreams.fromString(fuente);

        CodexLatinusLexer lexer = new CodexLatinusLexer(entrada);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new EscuchaErrores(errores));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CodexLatinusParser parser = new CodexLatinusParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new EscuchaErrores(errores));
        parser.setErrorHandler(new ModoPanico());

        parseTree = parser.programa();

        if (errores.hayErroresDe(TipoError.LEXICO)
                || errores.hayErroresDe(TipoError.SINTACTICO)) {
            return false;
        }

        // ---- Construccion del AST ----
        ConstructorAST constructor = new ConstructorAST();
        ParseTreeWalker.DEFAULT.walk(constructor, parseTree);
        programa = constructor.getPrograma();

        if (programa == null) {
            return false;
        }

        // ---- Analisis semantico ----
        AnalizadorSemantico semantico = new AnalizadorSemantico(errores);
        semantico.analizar(programa);
        tablaSimbolos = semantico.getTabla();

        return !errores.hayErrores();
    }

    private void reiniciar() {
        errores.limpiar();
        parseTree = null;
        programa = null;
        tablaSimbolos = null;
    }
}