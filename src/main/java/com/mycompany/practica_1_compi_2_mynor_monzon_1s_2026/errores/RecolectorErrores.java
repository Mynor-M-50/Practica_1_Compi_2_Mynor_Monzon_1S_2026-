/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
 Lista central de errores de todas las fases

 e comparte entre el lexer, el parser y el analizador semantico para
 que al final se pueda mostrar un solo reporte ordenado por linea, sin
 importar en que etapa se detecto cada error
 */
public class RecolectorErrores {

    private final List<ErrorCompilacion> errores = new ArrayList<>();

    public void agregar(ErrorCompilacion error) {
        errores.add(error);
    }

    public void agregar(TipoError tipo, String mensaje, int linea, int columna) {
        errores.add(new ErrorCompilacion(tipo, mensaje, linea, columna));
    }

    public void agregar(TipoError tipo, String mensaje, String lexema, int linea, int columna) {
        errores.add(new ErrorCompilacion(tipo, mensaje, lexema, linea, columna));
    }

    public boolean hayErrores() {
        return !errores.isEmpty();
    }

    public boolean hayErroresDe(TipoError tipo) {
        return errores.stream().anyMatch(e -> e.getTipo() == tipo);
    }

    public int cantidad() {
        return errores.size();
    }

    public int cantidadDe(TipoError tipo) {
        return (int) errores.stream().filter(e -> e.getTipo() == tipo).count();
    }

    // Todos los errores ordenados por linea y luego por columna. 
    public List<ErrorCompilacion> getErrores() {
        List<ErrorCompilacion> copia = new ArrayList<>(errores);
        copia.sort(Comparator
                .comparingInt(ErrorCompilacion::getLinea)
                .thenComparingInt(ErrorCompilacion::getColumna));
        return Collections.unmodifiableList(copia);
    }

    public List<ErrorCompilacion> getErroresDe(TipoError tipo) {
        List<ErrorCompilacion> filtrados = new ArrayList<>();
        for (ErrorCompilacion error : getErrores()) {
            if (error.getTipo() == tipo) {
                filtrados.add(error);
            }
        }
        return filtrados;
    }

    public void limpiar() {
        errores.clear();
    }

    public String aTexto() {
        if (errores.isEmpty()) {
            return "Sin errores.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Errores encontrados: ").append(errores.size())
          .append(" (lexicos ").append(cantidadDe(TipoError.LEXICO))
          .append(", sintacticos ").append(cantidadDe(TipoError.SINTACTICO))
          .append(", semanticos ").append(cantidadDe(TipoError.SEMANTICO))
          .append(")").append(System.lineSeparator());
        for (ErrorCompilacion error : getErrores()) {
            sb.append("  ").append(error).append(System.lineSeparator());
        }
        return sb.toString();
    }
}