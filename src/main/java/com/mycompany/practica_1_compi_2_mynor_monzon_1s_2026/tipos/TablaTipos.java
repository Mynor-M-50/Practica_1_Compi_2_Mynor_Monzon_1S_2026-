/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos;

/**
 *
 * @author mynorm50
 */

/*
 Reglas de compatibilidad e inferencia de tipos.
 
 Concentra en un solo lugar todas las decisiones de tipado para que
 e analizador semantico no las tenga dispersas
 */
public final class TablaTipos {

    private TablaTipos() {
        // Clase de utilidades, no se instancia
    }

    // Operaciones binarias/--------------------------------------------------------------

    
     // Calcula el tipo resultante de aplicar un operador binario
     // Devuelve Tipo.error() si la combinacion no es valida
     
    public static Tipo resultadoBinario(Tipo izquierdo, Operador operador, Tipo derecho) {
        if (izquierdo == null || derecho == null || operador == null) {
            return Tipo.error();
        }

        // El error se propaga sin generar mensajes adicionales
        if (izquierdo.esError() || derecho.esError()) {
            return Tipo.error();
        }

        // No se opera directamente sobre arreglos ni estructuras
        // Hay que acceder primero a un elemento o a un atributo
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

    /*
     Aritmetica y concatenacion
    
     solo se puede combinar mediante
     concatenacion con el operador mas, cualquier otro operador
     plicado a un textum es error
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

        return Tipo.de(TipoPrimitivo.mayorNivel(a, b));
    }
    
    
     // Comparaciones de orden: menor, mayor, menor igual, mayor igual
     // Solo tienen sentido sobre tipos ordenables, rl resultado siempre
     // es booleano
     
    private static Tipo resultadoRelacional(Tipo izquierdo, Tipo derecho) {
        TipoPrimitivo a = izquierdo.getPrimitivo();
        TipoPrimitivo b = derecho.getPrimitivo();

        if (a.esOrdenable() && b.esOrdenable()) {
            return Tipo.booleano();
        }
        return Tipo.error();
    }

     // Igualdad y diferencia, de permite entre tipos del mismo nivel o
     // entre tipos numericos mezclados e,l resultado siempre es booleano
     
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

    
     // Operadores logicos. Ambos operandos deben ser booleanos
     // estrictos, sin conversion implicita
     
    private static Tipo resultadoLogico(Tipo izquierdo, Tipo derecho) {
        if (izquierdo.esBooleano() && derecho.esBooleano()) {
            return Tipo.booleano();
        }
        return Tipo.error();
    }

    // Operaciones unarias-------------------------------------------------------------

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

    // Asignacion------------------------------------------------------------------

     // Determina si un valor de tipo origen se puede guardar en una
     // variable de tipo destino
     
        public static boolean esAsignable(Tipo destino, Tipo origen) {
        if (destino == null || origen == null) {
            return false;
        }

        // El error ya fue reportado en otro lado, no se encadena
        if (destino.esError() || origen.esError()) {
            return true;
        }

        // Arreglos. mismo tipo de elemento y ambos arreglos
        if (destino.esArreglo() || origen.esArreglo()) {
            return destino.mismoTipoQue(origen);
        }

        // Estructuras. tienen que ser exactamente la misma
        if (destino.esEstructura() || origen.esEstructura()) {
            return destino.mismoTipoQue(origen);
        }

        TipoPrimitivo d = destino.getPrimitivo();
        TipoPrimitivo o = origen.getPrimitivo();

        if (!d.participaEnJerarquia() || !o.participaEnJerarquia()) {
            return false;
        }

        // Solo se ensancha hacia arriba en la jerarquia
        // textum(5) > decimalis(4) > numerus(3) > littera(2) > bool(1)
        
        return d.getNivel() >= o.getNivel();
    }

    
     // Estop verifica que una expresion sirva como condicion de si, dum,
     //facere o per. el enunciado exige que sea estrictamente booleana
     
    public static boolean esCondicionValida(Tipo tipo) {
        return tipo != null && (tipo.esError() || tipo.esBooleano());
    }

    // Mensajes de error----------------------------------------------------------

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