package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026;

import com.mycompany.codexlatinus.gramatica.CodexLatinusLexer;
import com.mycompany.codexlatinus.gramatica.CodexLatinusParser;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.analizador.ConstructorAST;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.NodoPrograma;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Prueba temporal del analisis.
 *
 * Recorre las tres etapas que ya estan listas: analisis lexico,
 * analisis sintactico y construccion del AST propio.
 * Se puede borrar cuando exista la interfaz grafica.
 */
public class Pruebagramatica {

    private static final List<String> errores = new ArrayList<>();

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

        ParseTree arbolAntlr = parser.programa();

        if (!errores.isEmpty()) {
            System.out.println("=== ERRORES ===");
            for (String error : errores) {
                System.out.println("  " + error);
            }
            System.out.println();
            System.out.println("No se construye el AST porque hay errores.");
            return;
        }

        // Construccion del AST propio recorriendo el parse tree
        ConstructorAST constructor = new ConstructorAST();
        ParseTreeWalker.DEFAULT.walk(constructor, arbolAntlr);
        NodoPrograma programa = constructor.getPrograma();

        if (programa == null) {
            System.out.println("El AST quedo vacio. Revisar el ConstructorAST.");
            return;
        }

        System.out.println("=== AST ===");
        System.out.print(programa.aTexto(0));
        System.out.println();

        System.out.println("=== RESUMEN ===");
        System.out.println("Declaraciones globales : " + programa.getDeclaracionesGlobales().size());
        System.out.println("Funciones              : " + programa.getFunciones().size());
        System.out.println("Instrucciones en MAIOR : " + programa.getInstruccionesPrincipales().size());
        System.out.println();
        System.out.println("AST construido sin errores.");
    }

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