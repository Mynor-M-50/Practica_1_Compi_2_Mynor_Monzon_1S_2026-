/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.codexlatinus;

import com.mycompany.codexlatinus.gramatica.CodexLatinusLexer;
import com.mycompany.codexlatinus.gramatica.CodexLatinusParser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Prueba temporal de la gramatica.
 *
 * No forma parte del compilador final: sirve para verificar que la
 * gramatica acepta los archivos .lat antes de construir el AST encima.
 * Se puede borrar cuando ya exista la interfaz grafica.
 *
 * Mynor Miguel Monzon Martinez - 202230884
 */
public class Pruebagramatica {

    private static final List<String> errores = new ArrayList<>();

    // Poner en true para ver la lista completa de tokens
    private static final boolean MOSTRAR_TOKENS = false;

    public static void main(String[] args) {
        String ruta = (args.length > 0) ? args[0] : "entradas/ejemplo_completo.lat";

        String fuente;
        try {
            fuente = Files.readString(Path.of(ruta));
        } catch (Exception e) {
            System.out.println("No se pudo leer el archivo: " + ruta);
            System.out.println("Detalle: " + e.getMessage());
            return;
        }

        System.out.println("Analizando: " + ruta);
        System.out.println();

        CharStream entrada = CharStreams.fromString(fuente);

        CodexLatinusLexer lexer = new CodexLatinusLexer(entrada);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new EscuchaSimple("LEXICO"));

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CodexLatinusParser parser = new CodexLatinusParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new EscuchaSimple("SINTACTICO"));

        ParseTree arbol = parser.programa();

        if (MOSTRAR_TOKENS) {
            tokens.fill();
            System.out.println("=== TOKENS ===");
            for (Token t : tokens.getTokens()) {
                if (t.getType() == Token.EOF) {
                    continue;
                }
                String nombre = CodexLatinusLexer.VOCABULARY.getSymbolicName(t.getType());
                System.out.printf("  %-14s %s%n", nombre, t.getText());
            }
            System.out.println();
        }

        System.out.println("=== ARBOL DE ANALISIS ===");
        System.out.print(dibujarArbol(arbol, parser, 0));
        System.out.println();

        System.out.println("=== RESULTADO ===");
        if (errores.isEmpty()) {
            System.out.println("Sin errores. La gramatica acepta el archivo.");
        } else {
            System.out.println("Errores encontrados: " + errores.size());
            for (String error : errores) {
                System.out.println("  " + error);
            }
        }
    }

    /**
     * Recorre el parse tree de ANTLR y lo imprime con sangria.
     * Solo es para inspeccion visual, el AST propio viene despues.
     */
    private static String dibujarArbol(ParseTree nodo, Parser parser, int nivel) {
        StringBuilder sb = new StringBuilder();
        List<String> reglas = Arrays.asList(parser.getRuleNames());

        sb.append("  ".repeat(nivel));
        sb.append(Trees.getNodeText(nodo, reglas));
        sb.append(System.lineSeparator());

        for (int i = 0; i < nodo.getChildCount(); i++) {
            sb.append(dibujarArbol(nodo.getChild(i), parser, nivel + 1));
        }
        return sb.toString();
    }

    /**
     * Recolecta los errores en lugar de imprimirlos en consola de error.
     * Es una version minima de lo que sera EscuchaErrores en el Bloque 5.
     */
    private static class EscuchaSimple extends BaseErrorListener {

        private final String tipo;

        EscuchaSimple(String tipo) {
            this.tipo = tipo;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object simboloOfensivo,
                                int linea,
                                int columna,
                                String mensaje,
                                RecognitionException e) {
            errores.add(String.format("[%s] Linea %d, columna %d -> %s",
                    tipo, linea, columna, mensaje));
        }
    }
}