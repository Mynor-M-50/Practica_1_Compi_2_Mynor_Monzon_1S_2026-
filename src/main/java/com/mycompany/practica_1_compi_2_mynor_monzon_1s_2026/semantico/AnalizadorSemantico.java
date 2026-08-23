/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.semantico;

import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.Nodo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.NodoPrograma;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.AccesoArreglo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.AccesoAtributo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.AtributoInicializado;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Expresion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Identificador;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.Literal;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.LiteralEstructura;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.LiteralLista;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.LlamadaFuncion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.OperacionBinaria;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.expresion.OperacionUnaria;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Asignacion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Bloque;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.CampoEstructura;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.CicloDum;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.CicloFacere;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.CicloPer;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Condicional;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.DeclaracionArreglo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.DeclaracionVariable;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.DefinicionEstructura;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.DefinicionFuncion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Imprimir;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.IncrementoDecremento;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Instruccion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Interrumpe;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Leer;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.LlamadaInstruccion;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Parametro;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Perge;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.RamaCondicional;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.ast.instruccion.Reddere;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores.RecolectorErrores;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.errores.TipoError;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos.RolSimbolo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos.Simbolo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.simbolos.TablaSimbolos;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Operador;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.TablaTipos;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.TipoPrimitivo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Recorre el AST y aplica todas las validaciones semanticas.
 *
 * ESTRATEGIA DE DOS PASADAS
 *
 * Primero se registran las estructuras y las firmas de las funciones,
 * y solo despues se validan los cuerpos. Sin esa separacion, una
 * funcion no podria llamar a otra definida mas abajo en el archivo,
 * ni una estructura podria usarse antes de su definicion.
 *
 * PROPAGACION DE ERRORES
 *
 * Cuando una expresion no es valida se devuelve Tipo.error(), que
 * TablaTipos trata como comodin. Asi un solo error real no produce una
 * cascada de mensajes derivados.
 */

public class AnalizadorSemantico {

    private final TablaSimbolos tabla = new TablaSimbolos();
    private final RecolectorErrores errores;

    /** Funcion que se esta validando, para verificar los retornos. */
    private DefinicionFuncion funcionActual;

    /** Cuantos ciclos anidados hay activos, para perge e interrumpe. */
    private int profundidadCiclo;
    private boolean permiteDeclaraciones;
    
    public AnalizadorSemantico(RecolectorErrores errores) {
        this.errores = errores;
    }

    public TablaSimbolos getTabla() {
        return tabla;
    }

    // ============================================================
    // Punto de entrada
    // ============================================================

        public void analizar(NodoPrograma programa) {
        if (programa == null) {
            return;
        }

        // Pasada 1: estructuras globales
        for (Instruccion instruccion : programa.getDeclaracionesGlobales()) {
            if (instruccion instanceof DefinicionEstructura) {
                registrarEstructura((DefinicionEstructura) instruccion);
            }
        }

        // Pasada 2: variables globales
        permiteDeclaraciones = true;
        for (Instruccion instruccion : programa.getDeclaracionesGlobales()) {
            if (!(instruccion instanceof DefinicionEstructura)) {
                validarInstruccion(instruccion);
            }
        }
        permiteDeclaraciones = false;

        // Pasada 3: firmas de funciones
        for (DefinicionFuncion funcion : programa.getFunciones()) {
            registrarFirmaFuncion(funcion);
        }

        // Pasada 4: cuerpos de funciones
        for (DefinicionFuncion funcion : programa.getFunciones()) {
            validarFuncion(funcion);
        }

        // Pasada 5: seccion principal
        validarListaInstrucciones(programa.getInstruccionesPrincipales());
    }

    // ============================================================
    // Registro previo
    // ============================================================

    private void registrarEstructura(DefinicionEstructura definicion) {
        String repetido = definicion.primerCampoRepetido();
        if (repetido != null) {
            error("El atributo '" + repetido + "' esta repetido en la estructura '"
                    + definicion.getNombre() + "'", definicion);
        }

        Simbolo simbolo = new Simbolo(definicion.getNombre(),
                Tipo.estructura(definicion.getNombre()), RolSimbolo.ESTRUCTURA,
                definicion.getLinea(), definicion.getColumna());
        simbolo.setDefinicionEstructura(definicion);

        if (!tabla.declarar(simbolo)) {
            error("Ya existe una estructura o variable llamada '"
                    + definicion.getNombre() + "'", definicion);
        }
    }

    private void registrarFirmaFuncion(DefinicionFuncion funcion) {
        Simbolo simbolo = new Simbolo(funcion.getNombre(), funcion.getTipoRetorno(),
                RolSimbolo.FUNCION, funcion.getLinea(), funcion.getColumna());
        simbolo.setDefinicionFuncion(funcion);

        if (!tabla.declarar(simbolo)) {
            error("Ya existe una funcion o variable llamada '"
                    + funcion.getNombre() + "'", funcion);
        }

        // El tipo de retorno debe existir si es una estructura
        Tipo retorno = funcion.getTipoRetorno();
        if (retorno != null && retorno.esEstructura()) {
            verificarEstructuraExiste(retorno, funcion);
        }
    }

    // ============================================================
    // Funciones
    // ============================================================

        private void validarFuncion(DefinicionFuncion funcion) {
        funcionActual = funcion;
        tabla.entrarAmbito(funcion.getNombre());

        for (Parametro parametro : funcion.getParametros()) {
            if (parametro.getTipo() != null && parametro.getTipo().esEstructura()) {
                verificarEstructuraExiste(parametro.getTipo(), parametro);
            }
            Simbolo simbolo = new Simbolo(parametro.getNombre(), parametro.getTipo(),
                    RolSimbolo.PARAMETRO, parametro.getLinea(), parametro.getColumna());
            if (!tabla.declarar(simbolo)) {
                error("El parametro '" + parametro.getNombre()
                        + "' esta repetido", parametro);
            }
        }

        // Declaraciones del bloque VARIABILES local
        permiteDeclaraciones = true;
        for (Instruccion declaracion : funcion.getDeclaracionesLocales()) {
            if (declaracion instanceof DefinicionEstructura) {
                registrarEstructura((DefinicionEstructura) declaracion);
            } else {
                validarInstruccion(declaracion);
            }
        }
        permiteDeclaraciones = false;

        validarListaInstrucciones(funcion.getCuerpo());

        // Toda funcion ratio debe retornar por todos sus caminos
        if (!funcion.esActio() && !todosLosCaminosRetornan(funcion.getCuerpo())) {
            error("La funcion '" + funcion.getNombre()
                    + "' no retorna un valor en todos sus caminos posibles", funcion);
        }

        tabla.salirAmbito();
        funcionActual = null;
    }

    /**
     * Verifica que todos los caminos terminen en un reddere con valor.
     * Un condicional solo cuenta si tiene rama final y todas sus ramas
     * retornan; de lo contrario existe un camino que se escapa.
     */
    private boolean todosLosCaminosRetornan(List<Instruccion> instrucciones) {
        for (Instruccion instruccion : instrucciones) {
            if (instruccion instanceof Reddere) {
                return ((Reddere) instruccion).tieneValor();
            }
            if (instruccion instanceof Condicional) {
                Condicional condicional = (Condicional) instruccion;
                if (!condicional.tieneRamaFinal()) {
                    continue;
                }
                boolean todasRetornan = true;
                for (RamaCondicional rama : condicional.getRamas()) {
                    if (rama.getBloque() == null
                            || !todosLosCaminosRetornan(rama.getBloque().getInstrucciones())) {
                        todasRetornan = false;
                        break;
                    }
                }
                if (todasRetornan
                        && todosLosCaminosRetornan(condicional.getRamaFinal().getInstrucciones())) {
                    return true;
                }
            }
        }
        return false;
    }

    // ============================================================
    // Instrucciones
    // ============================================================

    /**
     * Valida una lista de instrucciones y detecta codigo inalcanzable
     * despues de un reddere.
     */
    private void validarListaInstrucciones(List<Instruccion> instrucciones) {
        boolean yaRetorno = false;

        for (Instruccion instruccion : instrucciones) {
            if (yaRetorno) {
                error("Codigo inalcanzable: ya se ejecuto un reddere antes",
                        instruccion);
                yaRetorno = false;
            }
            validarInstruccion(instruccion);
            if (instruccion instanceof Reddere) {
                yaRetorno = true;
            }
        }
    }

        private void validarInstruccion(Instruccion instruccion) {
        if (instruccion == null) {
            return;
        }

        if (instruccion instanceof DeclaracionVariable) {
            if (!permiteDeclaraciones) {
                error("Las variables solo se pueden declarar en la seccion VARIABILES",
                        instruccion);
            }
            validarDeclaracionVariable((DeclaracionVariable) instruccion);
        } else if (instruccion instanceof DeclaracionArreglo) {
            if (!permiteDeclaraciones) {
                error("Los arreglos solo se pueden declarar en la seccion VARIABILES",
                        instruccion);
            }
            validarDeclaracionArreglo((DeclaracionArreglo) instruccion);
        } else if (instruccion instanceof DefinicionEstructura) {
            error("Las estructuras solo se pueden definir en la seccion VARIABILES",
                    instruccion);
        } else if (instruccion instanceof Asignacion) {
            validarAsignacion((Asignacion) instruccion);
        } else if (instruccion instanceof IncrementoDecremento) {
            validarIncremento((IncrementoDecremento) instruccion);
        } else if (instruccion instanceof Condicional) {
            validarCondicional((Condicional) instruccion);
        } else if (instruccion instanceof CicloDum) {
            validarCicloDum((CicloDum) instruccion);
        } else if (instruccion instanceof CicloFacere) {
            validarCicloFacere((CicloFacere) instruccion);
        } else if (instruccion instanceof CicloPer) {
            validarCicloPer((CicloPer) instruccion);
        } else if (instruccion instanceof Perge) {
            validarControlCiclo(instruccion, "perge");
        } else if (instruccion instanceof Interrumpe) {
            validarControlCiclo(instruccion, "interrumpe");
        } else if (instruccion instanceof Reddere) {
            validarReddere((Reddere) instruccion);
        } else if (instruccion instanceof Imprimir) {
            validarImprimir((Imprimir) instruccion);
        } else if (instruccion instanceof Leer) {
            validarLeer((Leer) instruccion);
        } else if (instruccion instanceof LlamadaInstruccion) {
            evaluar(((LlamadaInstruccion) instruccion).getLlamada());
        } else if (instruccion instanceof Bloque) {
            validarListaInstrucciones(((Bloque) instruccion).getInstrucciones());
        }
    }

    // ------------------------------------------------------------
    // Declaraciones
    // ------------------------------------------------------------

    private void validarDeclaracionVariable(DeclaracionVariable declaracion) {
        Tipo tipo = declaracion.getTipo();

        if (tipo != null && tipo.esEstructura()) {
            verificarEstructuraExiste(tipo, declaracion);
        }

        if (declaracion.tieneValorInicial()) {
            Expresion valor = declaracion.getValorInicial();

            if (valor instanceof LiteralEstructura) {
                validarLiteralEstructura((LiteralEstructura) valor, tipo);
            } else {
                Tipo tipoValor = evaluar(valor);
                if (!TablaTipos.esAsignable(tipo, tipoValor)) {
                    error(TablaTipos.mensajeAsignacion(tipo, tipoValor), declaracion);
                }
            }
        }

        Simbolo simbolo = new Simbolo(declaracion.getNombre(), tipo, RolSimbolo.VARIABLE,
                declaracion.getLinea(), declaracion.getColumna());
        if (!tabla.declarar(simbolo)) {
            error("La variable '" + declaracion.getNombre()
                    + "' ya fue declarada en este ambito", declaracion);
        }
    }

    private void validarDeclaracionArreglo(DeclaracionArreglo declaracion) {
        Tipo tipoElemento = declaracion.getTipoElemento();

        if (tipoElemento != null && tipoElemento.esEstructura()) {
            verificarEstructuraExiste(tipoElemento, declaracion);
        }

        Integer dimension = null;
        if (declaracion.tieneDimension()) {
            Tipo tipoDimension = evaluar(declaracion.getDimension());
            if (!tipoDimension.esError()
                    && tipoDimension.getPrimitivo() != TipoPrimitivo.NUMERUS) {
                error("La dimension de un arreglo debe ser de tipo numerus",
                        declaracion.getDimension());
            }
            dimension = evaluarConstanteEntera(declaracion.getDimension());
            if (dimension != null && dimension <= 0) {
                error("La dimension de un arreglo debe ser mayor que cero",
                        declaracion.getDimension());
            }
        }

        if (declaracion.tieneValoresIniciales()) {
            LiteralLista lista = declaracion.getValoresIniciales();
            validarListaValores(lista, tipoElemento);

            if (dimension != null && lista.cantidadValores() > dimension) {
                error("El arreglo '" + declaracion.getNombre() + "' declara "
                        + dimension + " posiciones pero recibe "
                        + lista.cantidadValores() + " valores", lista);
            }
        }

        Tipo tipoArreglo = (tipoElemento != null)
                ? Tipo.arregloDe(tipoElemento,
                        (dimension != null) ? dimension : Tipo.DIMENSION_DESCONOCIDA)
                : Tipo.error();

        Simbolo simbolo = new Simbolo(declaracion.getNombre(), tipoArreglo,
                RolSimbolo.ARREGLO, declaracion.getLinea(), declaracion.getColumna());
        if (!tabla.declarar(simbolo)) {
            error("El arreglo '" + declaracion.getNombre()
                    + "' ya fue declarado en este ambito", declaracion);
        }
    }

    private void validarListaValores(LiteralLista lista, Tipo tipoElemento) {
        for (Expresion valor : lista.getValores()) {
            if (valor instanceof LiteralEstructura) {
                validarLiteralEstructura((LiteralEstructura) valor, tipoElemento);
            } else {
                Tipo tipoValor = evaluar(valor);
                if (!TablaTipos.esAsignable(tipoElemento, tipoValor)) {
                    error("El valor de tipo " + tipoValor
                            + " no corresponde al tipo del arreglo (" + tipoElemento + ")",
                            valor);
                }
            }
        }
    }

    /**
     * Valida una instancia de estructura contra su definicion.
     * Comprueba que la estructura exista, que no falte ni sobre ningun
     * atributo, y que los tipos coincidan. El orden no importa.
     */
    private void validarLiteralEstructura(LiteralEstructura literal, Tipo tipoEsperado) {
        if (tipoEsperado == null || !tipoEsperado.esEstructura()) {
            error("Solo se puede usar una instancia entre llaves cuando el tipo "
                    + "declarado es una estructura", literal);
            return;
        }

        Simbolo simbolo = tabla.buscarEstructura(tipoEsperado.getNombreEstructura());
        if (simbolo == null || simbolo.getDefinicionEstructura() == null) {
            error("No existe la estructura '" + tipoEsperado.getNombreEstructura() + "'",
                    literal);
            return;
        }

        DefinicionEstructura definicion = simbolo.getDefinicionEstructura();

        String repetido = literal.primerAtributoRepetido();
        if (repetido != null) {
            error("El atributo '" + repetido + "' se inicializa mas de una vez", literal);
        }

        // Todo atributo declarado debe recibir valor
        for (CampoEstructura campo : definicion.getCampos()) {
            if (literal.buscarAtributo(campo.getNombre()) == null) {
                error("Falta inicializar el atributo '" + campo.getNombre()
                        + "' de la estructura '" + definicion.getNombre() + "'", literal);
            }
        }

        // Todo atributo dado debe existir y ser del tipo correcto
        for (AtributoInicializado atributo : literal.getAtributos()) {
            CampoEstructura campo = definicion.buscarCampo(atributo.getNombre());
            if (campo == null) {
                error("La estructura '" + definicion.getNombre()
                        + "' no tiene el atributo '" + atributo.getNombre() + "'",
                        atributo);
                continue;
            }
            validarValorAtributo(atributo, campo);
        }
    }

    private void validarValorAtributo(AtributoInicializado atributo, CampoEstructura campo) {
        Expresion valor = atributo.getValor();
        Tipo tipoCampo = campo.getTipo();

        // Caso especial:  animales: Animal[7]
        // No es un acceso a arreglo sino la declaracion de su dimension
        if (esDeclaracionDeDimension(valor)) {
            if (!campo.esArreglo()) {
                error("El atributo '" + campo.getNombre()
                        + "' no es un arreglo", atributo);
            }
            return;
        }

        if (valor instanceof LiteralEstructura) {
            validarLiteralEstructura((LiteralEstructura) valor, tipoCampo);
            return;
        }

        if (valor instanceof LiteralLista) {
            validarListaValores((LiteralLista) valor, tipoCampo.tipoElemento());
            return;
        }

        Tipo tipoValor = evaluar(valor);
        if (!TablaTipos.esAsignable(tipoCampo, tipoValor)) {
            error("El atributo '" + campo.getNombre() + "' es de tipo " + tipoCampo
                    + " pero recibe un valor de tipo " + tipoValor, atributo);
        }
    }

    /**
     * Detecta la forma  Animal[7]  usada para fijar la dimension de un
     * arreglo dentro de una instancia de estructura. Se distingue de un
     * acceso normal porque la base es el nombre de un tipo, no de una
     * variable.
     */
    private boolean esDeclaracionDeDimension(Expresion valor) {
        if (!(valor instanceof AccesoArreglo)) {
            return false;
        }
        Expresion base = ((AccesoArreglo) valor).getBase();
        if (!(base instanceof Identificador)) {
            return false;
        }
        String nombre = ((Identificador) base).getNombre();

        if (TipoPrimitivo.desdeNombre(nombre) != null) {
            return true;
        }
        Simbolo simbolo = tabla.buscar(nombre);
        return simbolo != null && simbolo.esEstructura();
    }

    // ------------------------------------------------------------
    // Asignaciones
    // ------------------------------------------------------------

    private void validarAsignacion(Asignacion asignacion) {
        Expresion objetivo = asignacion.getObjetivo();

        if (!esAsignable(objetivo)) {
            error("El lado izquierdo de una asignacion debe ser una variable, "
                    + "un elemento de arreglo o un atributo", asignacion);
            return;
        }

        Tipo tipoObjetivo = evaluar(objetivo);
        Expresion valor = asignacion.getValor();

        if (valor instanceof LiteralEstructura) {
            validarLiteralEstructura((LiteralEstructura) valor, tipoObjetivo);
            return;
        }

        if (valor instanceof LiteralLista) {
            validarListaValores((LiteralLista) valor, tipoObjetivo.tipoElemento());
            return;
        }

        Tipo tipoValor = evaluar(valor);
        if (!TablaTipos.esAsignable(tipoObjetivo, tipoValor)) {
            error(TablaTipos.mensajeAsignacion(tipoObjetivo, tipoValor), asignacion);
        }
    }

    private boolean esAsignable(Expresion expresion) {
        return expresion instanceof Identificador
                || expresion instanceof AccesoArreglo
                || expresion instanceof AccesoAtributo;
    }

    private void validarIncremento(IncrementoDecremento incremento) {
        Expresion objetivo = incremento.getObjetivo();

        if (!esAsignable(objetivo)) {
            error("Solo se puede incrementar o decrementar una variable", incremento);
            return;
        }

        Tipo tipo = evaluar(objetivo);
        if (!tipo.esError() && !tipo.getPrimitivo().esNumerico()) {
            error("El operador " + incremento.getOperador().getSimbolo()
                    + " solo se aplica a valores numericos, no a " + tipo, incremento);
        }
    }

    // ------------------------------------------------------------
    // Control de flujo
    // ------------------------------------------------------------

    private void validarCondicional(Condicional condicional) {
        for (RamaCondicional rama : condicional.getRamas()) {
            verificarCondicion(rama.getCondicion());
            if (rama.getBloque() != null) {
                tabla.entrarAmbito("si");
                validarListaInstrucciones(rama.getBloque().getInstrucciones());
                tabla.salirAmbito();
            }
        }
        if (condicional.tieneRamaFinal()) {
            tabla.entrarAmbito("aliter");
            validarListaInstrucciones(condicional.getRamaFinal().getInstrucciones());
            tabla.salirAmbito();
        }
    }

    private void validarCicloDum(CicloDum ciclo) {
        verificarCondicion(ciclo.getCondicion());
        entrarCiclo("dum", ciclo.getCuerpo());
    }

    private void validarCicloFacere(CicloFacere ciclo) {
        verificarCondicion(ciclo.getCondicion());
        entrarCiclo("facere", ciclo.getCuerpo());
    }

    private void validarCicloPer(CicloPer ciclo) {
        tabla.entrarAmbito("per");
        
        permiteDeclaraciones = true;
        validarInstruccion(ciclo.getInicializacion());
        permiteDeclaraciones = false;
        
        verificarCondicion(ciclo.getCondicion());
        validarInstruccion(ciclo.getActualizacion());

        profundidadCiclo++;
        if (ciclo.getCuerpo() != null) {
            validarListaInstrucciones(ciclo.getCuerpo().getInstrucciones());
        }
        profundidadCiclo--;

        tabla.salirAmbito();
    }

    private void entrarCiclo(String nombre, Bloque cuerpo) {
        tabla.entrarAmbito(nombre);
        profundidadCiclo++;
        if (cuerpo != null) {
            validarListaInstrucciones(cuerpo.getInstrucciones());
        }
        profundidadCiclo--;
        tabla.salirAmbito();
    }

    private void verificarCondicion(Expresion condicion) {
        if (condicion == null) {
            return;
        }
        Tipo tipo = evaluar(condicion);
        if (!TablaTipos.esCondicionValida(tipo)) {
            error("La condicion debe ser de tipo booleano, pero es de tipo " + tipo,
                    condicion);
        }
    }

    private void validarControlCiclo(Instruccion instruccion, String nombre) {
        if (profundidadCiclo == 0) {
            error("La instruccion '" + nombre + "' solo se puede usar dentro de un ciclo",
                    instruccion);
        }
    }

    private void validarReddere(Reddere reddere) {
        if (funcionActual == null) {
            error("La instruccion 'reddere' solo se puede usar dentro de una funcion",
                    reddere);
            return;
        }

        if (funcionActual.esActio()) {
            if (reddere.tieneValor()) {
                error("La funcion '" + funcionActual.getNombre()
                        + "' es de tipo actio y no debe retornar ningun valor", reddere);
            }
            return;
        }

        if (!reddere.tieneValor()) {
            error("La funcion '" + funcionActual.getNombre() + "' debe retornar un valor de tipo "
                    + funcionActual.getTipoRetorno(), reddere);
            return;
        }

        Tipo tipoValor = evaluar(reddere.getValor());
        if (!TablaTipos.esAsignable(funcionActual.getTipoRetorno(), tipoValor)) {
            error("La funcion '" + funcionActual.getNombre() + "' declara retornar "
                    + funcionActual.getTipoRetorno() + " pero retorna " + tipoValor,
                    reddere);
        }
    }

    // ------------------------------------------------------------
    // Entrada y salida
    // ------------------------------------------------------------

    private void validarImprimir(Imprimir imprimir) {
        for (Expresion valor : imprimir.getValores()) {
            Tipo tipo = evaluar(valor);
            if (tipo.esArreglo()) {
                error("No se puede imprimir un arreglo completo, "
                        + "hay que acceder a una posicion", valor);
            } else if (tipo.esEstructura()) {
                error("No se puede imprimir una estructura completa, "
                        + "hay que acceder a un atributo", valor);
            }
        }
    }

    private void validarLeer(Leer leer) {
        if (!leer.tieneObjetivo()) {
            return;
        }
        Expresion objetivo = leer.getObjetivo();

        if (!esAsignable(objetivo)) {
            error("La lectura debe guardarse en una variable, "
                    + "un elemento de arreglo o un atributo", leer);
            return;
        }

        Tipo tipo = evaluar(objetivo);
        if (!tipo.esError() && (tipo.esArreglo() || tipo.esEstructura())) {
            error("No se puede leer directamente sobre un arreglo o una estructura", leer);
        }
    }

    // ============================================================
    // Evaluacion de expresiones
    // ============================================================

    /**
     * Calcula el tipo de una expresion y lo guarda en el propio nodo
     * para que el traductor no tenga que recalcularlo.
     */
    private Tipo evaluar(Expresion expresion) {
        if (expresion == null) {
            return Tipo.error();
        }

        Tipo resultado;

        if (expresion instanceof Literal) {
            resultado = Tipo.de(((Literal) expresion).getTipoLiteral());
        } else if (expresion instanceof Identificador) {
            resultado = evaluarIdentificador((Identificador) expresion);
        } else if (expresion instanceof OperacionBinaria) {
            resultado = evaluarBinaria((OperacionBinaria) expresion);
        } else if (expresion instanceof OperacionUnaria) {
            resultado = evaluarUnaria((OperacionUnaria) expresion);
        } else if (expresion instanceof AccesoArreglo) {
            resultado = evaluarAccesoArreglo((AccesoArreglo) expresion);
        } else if (expresion instanceof AccesoAtributo) {
            resultado = evaluarAccesoAtributo((AccesoAtributo) expresion);
        } else if (expresion instanceof LlamadaFuncion) {
            resultado = evaluarLlamada((LlamadaFuncion) expresion);
        } else if (expresion instanceof LiteralLista) {
            resultado = Tipo.error();
        } else if (expresion instanceof LiteralEstructura) {
            resultado = Tipo.error();
        } else {
            resultado = Tipo.error();
        }

        expresion.setTipoResuelto(resultado);
        return resultado;
    }

    private Tipo evaluarIdentificador(Identificador identificador) {
        Simbolo simbolo = tabla.buscar(identificador.getNombre());

        if (simbolo == null) {
            error("La variable '" + identificador.getNombre() + "' no ha sido declarada",
                    identificador);
            return Tipo.error();
        }

        if (simbolo.esFuncion()) {
            error("'" + identificador.getNombre()
                    + "' es una funcion y debe llamarse con parentesis", identificador);
            return Tipo.error();
        }

        return simbolo.getTipo();
    }

    private Tipo evaluarBinaria(OperacionBinaria operacion) {
        Tipo izquierdo = evaluar(operacion.getIzquierdo());
        Tipo derecho = evaluar(operacion.getDerecho());

        Tipo resultado = TablaTipos.resultadoBinario(izquierdo,
                operacion.getOperador(), derecho);

        if (resultado.esError() && !izquierdo.esError() && !derecho.esError()) {
            error(TablaTipos.mensajeBinario(izquierdo, operacion.getOperador(), derecho),
                    operacion);
        }
        return resultado;
    }

    private Tipo evaluarUnaria(OperacionUnaria operacion) {
        Tipo operando = evaluar(operacion.getOperando());
        Tipo resultado = TablaTipos.resultadoUnario(operacion.getOperador(), operando);

        if (resultado.esError() && !operando.esError()) {
            error(TablaTipos.mensajeUnario(operacion.getOperador(), operando), operacion);
        }
        return resultado;
    }

    private Tipo evaluarAccesoArreglo(AccesoArreglo acceso) {
        Tipo tipoBase = evaluar(acceso.getBase());
        Tipo tipoIndice = evaluar(acceso.getIndice());

        if (!tipoIndice.esError() && tipoIndice.getPrimitivo() != TipoPrimitivo.NUMERUS) {
            error("El indice de un arreglo debe ser de tipo numerus, no " + tipoIndice,
                    acceso.getIndice());
        }

        if (tipoBase.esError()) {
            return Tipo.error();
        }

        if (!tipoBase.esArreglo()) {
            error("La variable no es un arreglo, no se puede indexar con corchetes",
                    acceso);
            return Tipo.error();
        }

        // Indice fuera de rango, solo si se puede calcular en analisis
        Integer indice = evaluarConstanteEntera(acceso.getIndice());
        if (indice != null) {
            if (indice < 0) {
                error("El indice de un arreglo no puede ser negativo", acceso.getIndice());
            } else if (tipoBase.tieneDimensionConocida() && indice >= tipoBase.getDimension()) {
                error("Indice fuera de rango: el arreglo tiene "
                        + tipoBase.getDimension() + " posiciones y se pide la "
                        + indice, acceso.getIndice());
            }
        }

        return tipoBase.tipoElemento();
    }

    private Tipo evaluarAccesoAtributo(AccesoAtributo acceso) {
        Tipo tipoBase = evaluar(acceso.getBase());

        if (tipoBase.esError()) {
            return Tipo.error();
        }

        if (!tipoBase.esEstructura()) {
            error("Solo se puede acceder con punto a los atributos de una estructura",
                    acceso);
            return Tipo.error();
        }

        Simbolo simbolo = tabla.buscarEstructura(tipoBase.getNombreEstructura());
        if (simbolo == null || simbolo.getDefinicionEstructura() == null) {
            error("No existe la estructura '" + tipoBase.getNombreEstructura() + "'",
                    acceso);
            return Tipo.error();
        }

        CampoEstructura campo = simbolo.getDefinicionEstructura()
                .buscarCampo(acceso.getNombreAtributo());
        if (campo == null) {
            error("La estructura '" + tipoBase.getNombreEstructura()
                    + "' no tiene el atributo '" + acceso.getNombreAtributo() + "'",
                    acceso);
            return Tipo.error();
        }

        return campo.getTipo();
    }

    private Tipo evaluarLlamada(LlamadaFuncion llamada) {
        Simbolo simbolo = tabla.buscar(llamada.getNombre());

        if (simbolo == null) {
            error("La funcion '" + llamada.getNombre() + "' no ha sido declarada", llamada);
            return Tipo.error();
        }

        if (!simbolo.esFuncion()) {
            error("'" + llamada.getNombre() + "' no es una funcion", llamada);
            return Tipo.error();
        }

        DefinicionFuncion definicion = simbolo.getDefinicionFuncion();
        List<Parametro> parametros = definicion.getParametros();

        if (llamada.cantidadArgumentos() != parametros.size()) {
            error("La funcion '" + llamada.getNombre() + "' espera "
                    + parametros.size() + " argumentos pero recibe "
                    + llamada.cantidadArgumentos(), llamada);
            return definicion.getTipoRetorno();
        }

        List<Expresion> argumentos = llamada.getArgumentos();
        for (int i = 0; i < argumentos.size(); i++) {
            Tipo tipoArgumento = evaluar(argumentos.get(i));
            Tipo tipoParametro = parametros.get(i).getTipo();
            if (!TablaTipos.esAsignable(tipoParametro, tipoArgumento)) {
                error("El argumento " + (i + 1) + " de '" + llamada.getNombre()
                        + "' debe ser de tipo " + tipoParametro
                        + " pero es de tipo " + tipoArgumento, argumentos.get(i));
            }
        }

        return definicion.getTipoRetorno();
    }

    // ============================================================
    // Utilidades
    // ============================================================

    /**
     * Intenta calcular el valor de una expresion entera constante.
     * Devuelve null si depende de variables y por lo tanto no se puede
     * conocer durante el analisis.
     */
    private Integer evaluarConstanteEntera(Expresion expresion) {
        if (expresion instanceof Literal) {
            Literal literal = (Literal) expresion;
            if (literal.getTipoLiteral() == TipoPrimitivo.NUMERUS) {
                return (Integer) literal.getValor();
            }
            return null;
        }

        if (expresion instanceof OperacionBinaria) {
            OperacionBinaria operacion = (OperacionBinaria) expresion;
            Integer izquierdo = evaluarConstanteEntera(operacion.getIzquierdo());
            Integer derecho = evaluarConstanteEntera(operacion.getDerecho());
            if (izquierdo == null || derecho == null) {
                return null;
            }
            Operador operador = operacion.getOperador();
            if (operador == Operador.SUMA) {
                return izquierdo + derecho;
            }
            if (operador == Operador.RESTA) {
                return izquierdo - derecho;
            }
            if (operador == Operador.MULTIPLICACION) {
                return izquierdo * derecho;
            }
            if (operador == Operador.DIVISION && derecho != 0) {
                return izquierdo / derecho;
            }
            return null;
        }

        if (expresion instanceof OperacionUnaria) {
            OperacionUnaria operacion = (OperacionUnaria) expresion;
            if (operacion.getOperador() == Operador.MENOS_UNARIO) {
                Integer valor = evaluarConstanteEntera(operacion.getOperando());
                return (valor == null) ? null : -valor;
            }
        }

        return null;
    }

    private void verificarEstructuraExiste(Tipo tipo, Nodo nodo) {
        if (tabla.buscarEstructura(tipo.getNombreEstructura()) == null) {
            error("No existe la estructura '" + tipo.getNombreEstructura() + "'", nodo);
        }
    }

    private void error(String mensaje, Nodo nodo) {
        int linea = (nodo != null) ? nodo.getLinea() : 0;
        int columna = (nodo != null) ? nodo.getColumna() : 0;
        errores.agregar(TipoError.SEMANTICO, mensaje, linea, columna);
    }
}