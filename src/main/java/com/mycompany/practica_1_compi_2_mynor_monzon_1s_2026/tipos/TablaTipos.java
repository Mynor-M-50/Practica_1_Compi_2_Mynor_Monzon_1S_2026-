/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos;

/**
 *
 * @author mynorm50
 */

/**
 * Reglas de compatibilidad e inferencia de tipos.
 *
 * Concentra en un solo lugar todas las decisiones de tipado para que
 * el analizador semantico no las tenga dispersas. Tambien es la clase
 * que respalda la tabla de compatibilidad que pide el manual tecnico.
 *
 * Convencion: cuando una operacion no es valida se devuelve
 * Tipo.error(). Ese tipo se propaga en silencio para no encadenar
 * mensajes de error a partir de una sola falla real.
 *
 * Mynor Miguel Monzon Martinez - 202230884
 */
public final class TablaTipos {

    private TablaTipos() {
        // Clase de utilidades, no se instancia
    }

    // ------------------------------------------------------------
    // Operaciones binarias
    // ------------------------------------------------------------

    /**
     * Calcula el tipo resultante de aplicar un operador binario.
     * Devuelve Tipo.error() si la combinacion no es valida.
     */
    public static Tipo resultadoBinario(Tipo izquierdo, Operador operador, Tipo derecho) {
        if (izquierdo == null || derecho == null || operador == null) {
            return Tipo.error();
        }

        // El error se propaga sin generar mensajes adicionales
        if (izquierdo.esError() || derecho.esError()) {
            return Tipo.error();
        }

        // No se opera directamente sobre arreglos ni estructuras.
        // Hay que acceder primero a un elemento o a un atributo.
        if (izquierdo.esArreglo() || derecho.esArreglo()) {
            return Tipo.error();
        }
        if (izquierdo.esEstructura() || derecho.esEstructura()) {
            return Tipo.error();
        }

        switch (operador.getCategoria()) {
            case ARITMETICO:
                return resultadoAritmetico(izquierdo, operador, derecho);
            case RELACIONAL:
                return resultadoRelacional(izquierdo, derecho);
            case IGUALDAD:
                return resultadoIgualdad(izquierdo, derecho);
            case LOGICO:
                return resultadoLogico(izquierdo, derecho);
            default:
                return Tipo.error();
        }
    }

    /**
     * Aritmetica y concatenacion.
     *
     * Regla del enunciado: textum solo se puede combinar mediante
     * concatenacion con el operador mas. Cualquier otro operador
     * aplicado a un textum es error.
     *
     * Para el resto de tipos el resultado toma el nivel jerarquico
     * mas alto de los dos operandos.
     */
    private static Tipo resultadoAritmetico(Tipo izquierdo, Operador operador, Tipo derecho) {
        TipoPrimitivo a = izquierdo.getPrimitivo();
        TipoPrimitivo b = derecho.getPrimitivo();

        boolean hayTextum = (a == TipoPrimitivo.TEXTUM || b == TipoPrimitivo.TEXTUM);
        if (hayTextum) {
            return (operador == Operador.SUMA) ? Tipo.textum() : Tipo.error();
        }

        if (!a.participaEnJerarquia() || !b.participaEnJerarquia()) {
            return Tipo.error();
        }

        // PENDIENTE DE CONFIRMAR CON EL PROFESOR:
        // La jerarquia del enunciado incluye bool en el nivel 1, lo que
        // implicaria que  verum + 1  es valido y devuelve numerus.
        // Como el lenguaje ahora es estrictamente tipado, aqui se
        // prohibe la aritmetica sobre booleanos. Si el profesor
        // confirma lo contrario, basta con borrar este bloque.
        if (a == TipoPrimitivo.BOOLEANO || b == TipoPrimitivo.BOOLEANO) {
            return Tipo.error();
        }

        return Tipo.de(TipoPrimitivo.mayorNivel(a, b));
    }

    /**
     * Comparaciones de orden: menor, mayor, menor igual, mayor igual.
     * Solo tienen sentido sobre tipos ordenables. El resultado siempre
     * es booleano.
     */
    private static Tipo resultadoRelacional(Tipo izquierdo, Tipo derecho) {
        TipoPrimitivo a = izquierdo.getPrimitivo();
        TipoPrimitivo b = derecho.getPrimitivo();

        if (a.esOrdenable() && b.esOrdenable()) {
            return Tipo.booleano();
        }
        return Tipo.error();
    }

    /**
     * Igualdad y diferencia. Se permite entre tipos del mismo nivel o
     * entre tipos numericos mezclados. El resultado siempre es booleano.
     */
    private static Tipo resultadoIgualdad(Tipo izquierdo, Tipo derecho) {
        TipoPrimitivo a = izquierdo.getPrimitivo();
        TipoPrimitivo b = derecho.getPrimitivo();

        if (!a.participaEnJerarquia() || !b.participaEnJerarquia()) {
            return Tipo.error();
        }

        if (a == b) {
            return Tipo.booleano();
        }

        // Se permite comparar numerus con decimalis o con littera
        boolean ambosComparables = a.esOrdenable() && b.esOrdenable();
        return ambosComparables ? Tipo.booleano() : Tipo.error();
    }

    /**
     * Operadores logicos. Ambos operandos deben ser booleanos
     * estrictos, sin conversion implicita.
     */
    private static Tipo resultadoLogico(Tipo izquierdo, Tipo derecho) {
        if (izquierdo.esBooleano() && derecho.esBooleano()) {
            return Tipo.booleano();
        }
        return Tipo.error();
    }

    // ------------------------------------------------------------
    // Operaciones unarias
    // ------------------------------------------------------------

    public static Tipo resultadoUnario(Operador operador, Tipo operando) {
        if (operando == null || operador == null) {
            return Tipo.error();
        }
        if (operando.esError()) {
            return Tipo.error();
        }
        if (operando.esArreglo() || operando.esEstructura()) {
            return Tipo.error();
        }

        if (operador.getCategoria() == Operador.Categoria.UNARIO_LOGICO) {
            return operando.esBooleano() ? Tipo.booleano() : Tipo.error();
        }

        if (operador.getCategoria() == Operador.Categoria.UNARIO_ARITMETICO) {
            TipoPrimitivo primitivo = operando.getPrimitivo();
            return primitivo.esNumerico() ? Tipo.de(primitivo) : Tipo.error();
        }

        return Tipo.error();
    }

    // ------------------------------------------------------------
    // Asignacion
    // ------------------------------------------------------------

    /**
     * Determina si un valor de tipo origen se puede guardar en una
     * variable de tipo destino.
     *
     * Se permite el ensanchamiento implicito hacia arriba en la
     * jerarquia, por ejemplo guardar un numerus en un decimalis.
     * No se permite el estrechamiento, porque implicaria perdida de
     * informacion silenciosa.
     */
    public static boolean esAsignable(Tipo destino, Tipo origen) {
        if (destino == null || origen == null) {
            return false;
        }

        // El error ya fue reportado en otro lado, no se encadena
        if (destino.esError() || origen.esError()) {
            return true;
        }

        // Arreglos: mismo tipo de elemento y ambos arreglos
        if (destino.esArreglo() || origen.esArreglo()) {
            return destino.mismoTipoQue(origen);
        }

        // Estructuras: tienen que ser exactamente la misma
        if (destino.esEstructura() || origen.esEstructura()) {
            return destino.mismoTipoQue(origen);
        }

        TipoPrimitivo d = destino.getPrimitivo();
        TipoPrimitivo o = origen.getPrimitivo();

        if (!d.participaEnJerarquia() || !o.participaEnJerarquia()) {
            return false;
        }

        // bool es estricto en ambos sentidos: no recibe ni entrega
        // valores de otro tipo
        if (d == TipoPrimitivo.BOOLEANO || o == TipoPrimitivo.BOOLEANO) {
            return d == o;
        }

        // PENDIENTE DE CONFIRMAR CON EL PROFESOR:
        // Por jerarquia pura, textum es el nivel mas alto y aceptaria
        // cualquier valor. Como el enunciado dice que textum solo se
        // combina por concatenacion, aqui se exige que el origen ya
        // sea textum.
        if (d == TipoPrimitivo.TEXTUM) {
            return o == TipoPrimitivo.TEXTUM;
        }

        // Resto de casos: solo se ensancha hacia arriba
        return d.getNivel() >= o.getNivel();
    }

    /**
     * Verifica que una expresion sirva como condicion de si, dum,
     * facere o per. El enunciado exige que sea estrictamente booleana.
     */
    public static boolean esCondicionValida(Tipo tipo) {
        return tipo != null && (tipo.esError() || tipo.esBooleano());
    }

    // ------------------------------------------------------------
    // Mensajes de error
    // ------------------------------------------------------------

    public static String mensajeBinario(Tipo izquierdo, Operador operador, Tipo derecho) {
        return "No se puede aplicar el operador " + operador.getSimbolo()
                + " entre " + izquierdo + " y " + derecho;
    }

    public static String mensajeUnario(Operador operador, Tipo operando) {
        return "No se puede aplicar el operador " + operador.getSimbolo()
                + " sobre " + operando;
    }

    public static String mensajeAsignacion(Tipo destino, Tipo origen) {
        return "No se puede asignar un valor de tipo " + origen
                + " a una variable de tipo " + destino;
    }
}