package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.analizador.Compilador;

import java.nio.file.Files;
import java.nio.file.Path;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.traductor.TraductorPiglatin;

/**
 * Prueba del compilador completo desde consola.
 */

public class Pruebagramatica {

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

        Compilador compilador = new Compilador();
        boolean exito = compilador.compilar(fuente);

        if (compilador.getPrograma() != null) {
            System.out.println("AST");
            System.out.print(compilador.getPrograma().aTexto(0));
            System.out.println();
        }

        if (compilador.getTablaSimbolos() != null) {
            System.out.println("TABLA DE SIMBOLOS");
            System.out.print(compilador.getTablaSimbolos().aTexto());
            System.out.println();
        }
        
        if (exito) {
            System.out.println();
            System.out.println("TRADUCCION A PIGLATIN");
            TraductorPiglatin traductor = new TraductorPiglatin();
            System.out.println(traductor.traducir(compilador.getPrograma()));
        }

        System.out.println("ERRORES");
        System.out.println(compilador.getErrores().aTexto());

        System.out.println();
        System.out.println(exito ? "Compilacion exitosa." : "La compilacion termino con errores.");
    }
}