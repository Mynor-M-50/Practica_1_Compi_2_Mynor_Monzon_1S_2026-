/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos;

import java.util.Objects;

/**
 *
 * @author mynorm50
 */

/**
 * Representa el tipo completo de una expresion, variable o retorno.
 *
 * Un tipo puede ser:
 *   - primitivo:   numerus, decimalis, textum, littera, bool
 *   - estructura:  una definida por el usuario, identificada por nombre
 *   - arreglo:     de cualquiera de los anteriores
 */
public final class Tipo {

    /**
     * Dimension usada cuando todavia no se conoce.
     */
    public static final int DIMENSION_DESCONOCIDA = -1;

    private final TipoPrimitivo primitivo;
    private final String nombreEstructura;
    private final boolean arreglo;
    private final int dimension;

    private Tipo(TipoPrimitivo primitivo, String nombreEstructura,
                 boolean arreglo, int dimension) {
        this.primitivo = primitivo;
        this.nombreEstructura = nombreEstructura;
        this.arreglo = arreglo;
        this.dimension = dimension;
    }

    // ------------------
    // Metodos de fabrica
    // ------------------
    public static Tipo de(TipoPrimitivo primitivo) {
        return new Tipo(primitivo, null, false, DIMENSION_DESCONOCIDA);
    }

    public static Tipo numerus() {
        return de(TipoPrimitivo.NUMERUS);
    }

    public static Tipo decimalis() {
        return de(TipoPrimitivo.DECIMALIS);
    }

    public static Tipo textum() {
        return de(TipoPrimitivo.TEXTUM);
    }

    public static Tipo littera() {
        return de(TipoPrimitivo.LITTERA);
    }

    public static Tipo booleano() {
        return de(TipoPrimitivo.BOOLEANO);
    }

    public static Tipo vacio() {
        return de(TipoPrimitivo.VACIO);
    }

    public static Tipo error() {
        return de(TipoPrimitivo.ERROR);
    }

    public static Tipo estructura(String nombre) {
        return new Tipo(TipoPrimitivo.ESTRUCTURA, nombre, false, DIMENSION_DESCONOCIDA);
    }

    /**
     * Construye un arreglo a partir de un tipo base.
     * El tipo base no puede ser a su vez un arreglo: el lenguaje no
     * define arreglos de mas de una dimension.
     */
    public static Tipo arregloDe(Tipo base, int dimension) {
        if (base.arreglo) {
            return Tipo.error();
        }
        return new Tipo(base.primitivo, base.nombreEstructura, true, dimension);
    }

    public static Tipo arregloDe(Tipo base) {
        return arregloDe(base, DIMENSION_DESCONOCIDA);
    }

    // ---------
    // Consultas
    // ---------
    public TipoPrimitivo getPrimitivo() {
        return primitivo;
    }

    public String getNombreEstructura() {
        return nombreEstructura;
    }

    public boolean esArreglo() {
        return arreglo;
    }

    public int getDimension() {
        return dimension;
    }

    public boolean tieneDimensionConocida() {
        return dimension != DIMENSION_DESCONOCIDA;
    }

    public boolean esEstructura() {
        return primitivo == TipoPrimitivo.ESTRUCTURA;
    }

    public boolean esError() {
        return primitivo == TipoPrimitivo.ERROR;
    }

    public boolean esVacio() {
        return primitivo == TipoPrimitivo.VACIO;
    }

    public boolean esBooleano() {
        return !arreglo && primitivo == TipoPrimitivo.BOOLEANO;
    }

    /** Tipo primitivo o estructura simple, sin ser arreglo. */
    public boolean esEscalar() {
        return !arreglo;
    }

    /**
     * Devuelve el tipo de los elementos del arreglo.
     * Si el tipo no es arreglo se devuelve a si mismo.
     */
    public Tipo tipoElemento() {
        if (!arreglo) {
            return this;
        }
        if (esEstructura()) {
            return estructura(nombreEstructura);
        }
        return de(primitivo);
    }

    /** Copia el tipo cambiando unicamente la dimension. */
    public Tipo conDimension(int nuevaDimension) {
        return new Tipo(primitivo, nombreEstructura, arreglo, nuevaDimension);
    }

    // -------------------------
    // Igualdad y representacion
    // -------------------------
    
    /**
     * Compara tipos sin tomar en cuenta la dimension.
     * Dos arreglos de numerus son el mismo tipo aunque midan distinto.
     */
    public boolean mismoTipoQue(Tipo otro) {
        if (otro == null) {
            return false;
        }
        return primitivo == otro.primitivo
                && arreglo == otro.arreglo
                && Objects.equals(nombreEstructura, otro.nombreEstructura);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tipo)) {
            return false;
        }
        Tipo otro = (Tipo) obj;
        return primitivo == otro.primitivo
                && arreglo == otro.arreglo
                && dimension == otro.dimension
                && Objects.equals(nombreEstructura, otro.nombreEstructura);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primitivo, nombreEstructura, arreglo, dimension);
    }

    @Override
    public String toString() {
        String base = esEstructura() ? nombreEstructura : primitivo.getNombre();
        if (!arreglo) {
            return base;
        }
        if (tieneDimensionConocida()) {
            return base + "[" + dimension + "]";
        }
        return base + "[]";
    }
}