/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.analizador;

import com.mycompany.codexlatinus.gramatica.CodexLatinusBaseListener;
import com.mycompany.codexlatinus.gramatica.CodexLatinusParser;

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
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Operador;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.Tipo;
import com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.tipos.TipoPrimitivo;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mynorm50
 */

/**
 * Convierte el parse tree de ANTLR en el AST propio del compilador.
 * Se usan los metodos exitX y no enterX porque el recorrido del Listener es en post orden.
 * Los tipos se guardan en un mapa aparte porque Tipo no hereda de Nodo.
 */
public class ConstructorAST extends CodexLatinusBaseListener {

    private final ParseTreeProperty<Nodo> nodos = new ParseTreeProperty<>();
    private final ParseTreeProperty<Tipo> tipos = new ParseTreeProperty<>();

    private NodoPrograma programa;

    public NodoPrograma getPrograma() {
        return programa;
    }

    // Utilidades internas -----------------------------------------------------
    private void guardar(ParseTree ctx, Nodo nodo) {
        nodos.put(ctx, nodo);
    }

    private Nodo obtener(ParseTree ctx) {
        return (ctx == null) ? null : nodos.get(ctx);
    }

    private Expresion expresionDe(ParseTree ctx) {
        Nodo nodo = obtener(ctx);
        return (nodo instanceof Expresion) ? (Expresion) nodo : null;
    }

    private Instruccion instruccionDe(ParseTree ctx) {
        Nodo nodo = obtener(ctx);
        return (nodo instanceof Instruccion) ? (Instruccion) nodo : null;
    }

    private Tipo tipoDe(ParseTree ctx) {
        return (ctx == null) ? null : tipos.get(ctx);
    }

    private int linea(ParserRuleContext ctx) {
        return ctx.getStart().getLine();
    }

    private int columna(ParserRuleContext ctx) {
        return ctx.getStart().getCharPositionInLine() + 1;
    }

    /** Copia el nodo del hijo hacia el padre, para reglas de paso. */
    private void propagar(ParserRuleContext ctx, ParseTree hijo) {
        Nodo nodo = obtener(hijo);
        if (nodo != null) {
            guardar(ctx, nodo);
        }
    }

    // Raiz del programa 0------------------------------------------------------
    @Override
    public void exitPrograma(CodexLatinusParser.ProgramaContext ctx) {
        List<Instruccion> globales = new ArrayList<>();
        List<DefinicionFuncion> funciones = new ArrayList<>();
        List<Instruccion> principales = new ArrayList<>();

        if (ctx.seccionVariables() != null) {
            for (CodexLatinusParser.DeclaracionContext dec : ctx.seccionVariables().declaracion()) {
                Instruccion instruccion = instruccionDe(dec);
                if (instruccion != null) {
                    globales.add(instruccion);
                }
            }
        }

        if (ctx.seccionMunera() != null) {
            for (CodexLatinusParser.DefinicionFuncionContext fun : ctx.seccionMunera().definicionFuncion()) {
                Nodo nodo = obtener(fun);
                if (nodo instanceof DefinicionFuncion) {
                    funciones.add((DefinicionFuncion) nodo);
                }
            }
        }

        if (ctx.seccionMaior() != null) {
            for (CodexLatinusParser.InstruccionContext ins : ctx.seccionMaior().instruccion()) {
                Instruccion instruccion = instruccionDe(ins);
                if (instruccion != null) {
                    principales.add(instruccion);
                }
            }
        }

        programa = new NodoPrograma(globales, funciones, principales, linea(ctx), columna(ctx));
        guardar(ctx, programa);
    }

    // Tipos--------------------------------------------------------------------

    @Override
    public void exitTipoNumerus(CodexLatinusParser.TipoNumerusContext ctx) {
        tipos.put(ctx, Tipo.numerus());
    }

    @Override
    public void exitTipoDecimalis(CodexLatinusParser.TipoDecimalisContext ctx) {
        tipos.put(ctx, Tipo.decimalis());
    }

    @Override
    public void exitTipoTextum(CodexLatinusParser.TipoTextumContext ctx) {
        tipos.put(ctx, Tipo.textum());
    }

    @Override
    public void exitTipoLittera(CodexLatinusParser.TipoLitteraContext ctx) {
        tipos.put(ctx, Tipo.littera());
    }

    @Override
    public void exitTipoBool(CodexLatinusParser.TipoBoolContext ctx) {
        tipos.put(ctx, Tipo.booleano());
    }

    @Override
    public void exitTipoEstructura(CodexLatinusParser.TipoEstructuraContext ctx) {
        tipos.put(ctx, Tipo.estructura(ctx.ID().getText()));
    }

    // Literales----------------------------------------------------------------

    @Override
    public void exitLitEntero(CodexLatinusParser.LitEnteroContext ctx) {
        guardar(ctx, Literal.entero(ctx.getText(), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitLitDecimal(CodexLatinusParser.LitDecimalContext ctx) {
        guardar(ctx, Literal.decimal(ctx.getText(), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitLitCadena(CodexLatinusParser.LitCadenaContext ctx) {
        guardar(ctx, Literal.cadena(ctx.getText(), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitLitCaracter(CodexLatinusParser.LitCaracterContext ctx) {
        guardar(ctx, Literal.caracter(ctx.getText(), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitLitVerum(CodexLatinusParser.LitVerumContext ctx) {
        guardar(ctx, Literal.booleano(true, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitLitFalsus(CodexLatinusParser.LitFalsusContext ctx) {
        guardar(ctx, Literal.booleano(false, linea(ctx), columna(ctx)));
    }

    // Expresiones----------------------------------------------------------------

    @Override
    public void exitExprAgrupada(CodexLatinusParser.ExprAgrupadaContext ctx) {
        propagar(ctx, ctx.expresion());
    }

    @Override
    public void exitExprUnaria(CodexLatinusParser.ExprUnariaContext ctx) {
        String simbolo = ctx.getChild(0).getText();
        Operador operador = Operador.desdeSimbolo(simbolo, true);
        Expresion operando = expresionDe(ctx.expresion());
        guardar(ctx, new OperacionUnaria(operador, operando, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitExprMulDiv(CodexLatinusParser.ExprMulDivContext ctx) {
        construirBinaria(ctx);
    }

    @Override
    public void exitExprSumaResta(CodexLatinusParser.ExprSumaRestaContext ctx) {
        construirBinaria(ctx);
    }

    @Override
    public void exitExprRelacional(CodexLatinusParser.ExprRelacionalContext ctx) {
        construirBinaria(ctx);
    }

    @Override
    public void exitExprIgualdad(CodexLatinusParser.ExprIgualdadContext ctx) {
        construirBinaria(ctx);
    }

    @Override
    public void exitExprAnd(CodexLatinusParser.ExprAndContext ctx) {
        construirBinaria(ctx);
    }

    @Override
    public void exitExprOr(CodexLatinusParser.ExprOrContext ctx) {
        construirBinaria(ctx);
    }

    private void construirBinaria(ParserRuleContext ctx) {
        Expresion izquierdo = expresionDe(ctx.getChild(0));
        Expresion derecho = expresionDe(ctx.getChild(2));
        Operador operador = Operador.desdeSimbolo(ctx.getChild(1).getText(), false);
        guardar(ctx, new OperacionBinaria(izquierdo, operador, derecho,
                linea(ctx), columna(ctx)));
    }

    @Override
    public void exitExprLlamada(CodexLatinusParser.ExprLlamadaContext ctx) {
        propagar(ctx, ctx.llamadaFuncion());
    }

    @Override
    public void exitExprAcceso(CodexLatinusParser.ExprAccesoContext ctx) {
        propagar(ctx, ctx.objetivo());
    }

    @Override
    public void exitExprLiteral(CodexLatinusParser.ExprLiteralContext ctx) {
        propagar(ctx, ctx.literal());
    }

    // Accesos encadenados------------------------------------------------------

    /**
     * Construye la cadena de accesos de izquierda a derecha.
     */
    @Override
    public void exitObjetivo(CodexLatinusParser.ObjetivoContext ctx) {
        Expresion actual = new Identificador(ctx.ID().getText(), linea(ctx), columna(ctx));

        for (CodexLatinusParser.SufijoAccesoContext sufijo : ctx.sufijoAcceso()) {
            if (sufijo.PUNTO() != null) {
                actual = new AccesoAtributo(actual, sufijo.ID().getText(),
                        linea(sufijo), columna(sufijo));
            } else {
                Expresion indice = expresionDe(sufijo.expresion());
                actual = new AccesoArreglo(actual, indice, linea(sufijo), columna(sufijo));
            }
        }

        guardar(ctx, actual);
    }

    // Llamadas y listas--------------------------------------------------------

    @Override
    public void exitLlamadaFuncion(CodexLatinusParser.LlamadaFuncionContext ctx) {
        List<Expresion> argumentos = new ArrayList<>();
        if (ctx.listaArgumentos() != null) {
            for (CodexLatinusParser.ExpresionContext arg : ctx.listaArgumentos().expresion()) {
                Expresion expresion = expresionDe(arg);
                if (expresion != null) {
                    argumentos.add(expresion);
                }
            }
        }
        guardar(ctx, new LlamadaFuncion(ctx.ID().getText(), argumentos,
                linea(ctx), columna(ctx)));
    }

    @Override
    public void exitListaValores(CodexLatinusParser.ListaValoresContext ctx) {
        List<Expresion> valores = new ArrayList<>();
        for (CodexLatinusParser.ValorListaContext val : ctx.valorLista()) {
            Expresion expresion = expresionDe(val);
            if (expresion != null) {
                valores.add(expresion);
            }
        }
        guardar(ctx, new LiteralLista(valores, linea(ctx), columna(ctx)));
    }
    
    @Override
    public void exitValorLista(CodexLatinusParser.ValorListaContext ctx) {
        propagar(ctx, ctx.getChild(0));
    }

    @Override
    public void exitAsignacionAtributo(CodexLatinusParser.AsignacionAtributoContext ctx) {
        Expresion valor = expresionDe(ctx.valorAtributo());
        guardar(ctx, new AtributoInicializado(ctx.ID().getText(), valor,
                linea(ctx), columna(ctx)));
    }

    @Override
    public void exitValorAtributo(CodexLatinusParser.ValorAtributoContext ctx) {
        propagar(ctx, ctx.getChild(0));
    }

    @Override
    public void exitDimensionPrimitiva(CodexLatinusParser.DimensionPrimitivaContext ctx) {
        String nombreTipo = ctx.getChild(0).getText();
        Identificador base = new Identificador(nombreTipo, linea(ctx), columna(ctx));
        Expresion indice = expresionDe(ctx.expresion());
        guardar(ctx, new AccesoArreglo(base, indice, linea(ctx), columna(ctx)));
    }
    
    @Override
    public void exitLiteralEstructura(CodexLatinusParser.LiteralEstructuraContext ctx) {
        List<AtributoInicializado> atributos = new ArrayList<>();
        for (CodexLatinusParser.AsignacionAtributoContext at : ctx.asignacionAtributo()) {
            Nodo nodo = obtener(at);
            if (nodo instanceof AtributoInicializado) {
                atributos.add((AtributoInicializado) nodo);
            }
        }
        guardar(ctx, new LiteralEstructura(atributos, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitDimension(CodexLatinusParser.DimensionContext ctx) {
        propagar(ctx, ctx.expresion());
    }

    // Declaraciones------------------------------------------------------------------

    @Override
    public void exitDeclaracion(CodexLatinusParser.DeclaracionContext ctx) {
        propagar(ctx, ctx.getChild(0));
    }

    @Override
    public void exitDeclEstructura(CodexLatinusParser.DeclEstructuraContext ctx) {
        String nombre = ctx.ID(0).getText();
        String nombreEstructura = ctx.ID(1).getText();
        Expresion valor = expresionDe(ctx.literalEstructura());
        guardar(ctx, new DeclaracionVariable(nombre, Tipo.estructura(nombreEstructura),
                valor, true, linea(ctx), columna(ctx)));
    }

    /**
     * Forma especial sin tipo explicito:  esto activo : verum;
     * El literal booleano ocupa el lugar del tipo y ademas sirve de
     * valor inicial cuando no viene una expresion despues.
     */
    @Override
    public void exitDeclBooleana(CodexLatinusParser.DeclBooleanaContext ctx) {
        String nombre = ctx.ID().getText();
        Expresion valor = expresionDe(ctx.expresion());

        if (valor == null) {
            boolean literal = "verum".equals(ctx.valorBooleano().getText());
            valor = Literal.booleano(literal, linea(ctx), columna(ctx));
        }

        guardar(ctx, new DeclaracionVariable(nombre, Tipo.booleano(), valor,
                false, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitDeclConValor(CodexLatinusParser.DeclConValorContext ctx) {
        guardar(ctx, new DeclaracionVariable(ctx.ID().getText(), tipoDe(ctx.tipo()),
                expresionDe(ctx.expresion()), true, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitDeclSinValor(CodexLatinusParser.DeclSinValorContext ctx) {
        guardar(ctx, new DeclaracionVariable(ctx.ID().getText(), tipoDe(ctx.tipo()),
                null, true, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitArregloTipado(CodexLatinusParser.ArregloTipadoContext ctx) {
        Expresion dimension = expresionDe(ctx.dimension());
        LiteralLista valores = (LiteralLista) obtener(ctx.listaValores());
        guardar(ctx, new DeclaracionArreglo(ctx.ID().getText(), tipoDe(ctx.tipo()),
                dimension, valores, true, linea(ctx), columna(ctx)));
    }

    /**
     * Arreglo sin tipo explicito, erl tipo se deduce del primer valor de la 
     * lista. si aparece un verum se asume booleano
     */
    @Override
    public void exitArregloInferido(CodexLatinusParser.ArregloInferidoContext ctx) {
        Expresion dimension = expresionDe(ctx.dimension());
        LiteralLista valores = (LiteralLista) obtener(ctx.listaValores());
        Tipo tipoElemento = deducirTipoDeLista(valores);
        guardar(ctx, new DeclaracionArreglo(ctx.ID().getText(), tipoElemento,
                dimension, valores, false, linea(ctx), columna(ctx)));
    }

    private Tipo deducirTipoDeLista(LiteralLista lista) {
        if (lista == null || lista.getValores().isEmpty()) {
            return Tipo.error();
        }
        for (Expresion valor : lista.getValores()) {
            if (valor instanceof Literal) {
                TipoPrimitivo primitivo = ((Literal) valor).getTipoLiteral();
                return Tipo.de(primitivo);
            }
        }
        return Tipo.error();
    }

    // Estructuras--------------------------------------------------------------

    @Override
    public void exitCampoSimple(CodexLatinusParser.CampoSimpleContext ctx) {
        guardar(ctx, new CampoEstructura(ctx.ID().getText(), tipoDe(ctx.tipo()),
                null, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitCampoBooleano(CodexLatinusParser.CampoBooleanoContext ctx) {
        guardar(ctx, new CampoEstructura(ctx.ID().getText(), Tipo.booleano(),
                null, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitCampoArreglo(CodexLatinusParser.CampoArregloContext ctx) {
        Tipo base = tipoDe(ctx.tipo());
        Tipo tipoArreglo = (base != null) ? Tipo.arregloDe(base) : Tipo.error();
        Expresion dimension = expresionDe(ctx.dimension());
        guardar(ctx, new CampoEstructura(ctx.ID().getText(), tipoArreglo,
                dimension, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitDefinicionEstructura(CodexLatinusParser.DefinicionEstructuraContext ctx) {
        List<CampoEstructura> campos = new ArrayList<>();
        for (CodexLatinusParser.CampoEstructuraContext campo : ctx.campoEstructura()) {
            Nodo nodo = obtener(campo);
            if (nodo instanceof CampoEstructura) {
                campos.add((CampoEstructura) nodo);
            }
        }
        guardar(ctx, new DefinicionEstructura(ctx.ID().getText(), campos,
                linea(ctx), columna(ctx)));
    }

    // Funciones----------------------------------------------------------------

    @Override
    public void exitParametro(CodexLatinusParser.ParametroContext ctx) {
        guardar(ctx, new Parametro(ctx.ID().getText(), tipoDe(ctx.tipo()),
                linea(ctx), columna(ctx)));
    }

    @Override
    public void exitFuncionSinRetorno(CodexLatinusParser.FuncionSinRetornoContext ctx) {
        guardar(ctx, construirFuncion(ctx, ctx.ID().getText(), Tipo.vacio(),
                ctx.listaParametros(), ctx.cuerpoFuncion()));
    }

    @Override
    public void exitFuncionConRetorno(CodexLatinusParser.FuncionConRetornoContext ctx) {
        guardar(ctx, construirFuncion(ctx, ctx.ID().getText(), tipoDe(ctx.tipo()),
                ctx.listaParametros(), ctx.cuerpoFuncion()));
    }

    /**
     * Arma la funcion separando las declaraciones locales del cuerpo
     */
    private DefinicionFuncion construirFuncion(ParserRuleContext ctx, String nombre,
                                               Tipo tipoRetorno,
                                               CodexLatinusParser.ListaParametrosContext listaParams,
                                               CodexLatinusParser.CuerpoFuncionContext cuerpoCtx) {
        List<Parametro> parametros = new ArrayList<>();
        if (listaParams != null) {
            for (CodexLatinusParser.ParametroContext par : listaParams.parametro()) {
                Nodo nodo = obtener(par);
                if (nodo instanceof Parametro) {
                    parametros.add((Parametro) nodo);
                }
            }
        }

        List<Instruccion> declaraciones = new ArrayList<>();
        List<Instruccion> cuerpo = new ArrayList<>();

        if (cuerpoCtx != null) {
            if (cuerpoCtx.bloqueVariablesLocal() != null) {
                for (CodexLatinusParser.DeclaracionContext dec
                        : cuerpoCtx.bloqueVariablesLocal().declaracion()) {
                    Instruccion instruccion = instruccionDe(dec);
                    if (instruccion != null) {
                        declaraciones.add(instruccion);
                    }
                }
            }
            for (CodexLatinusParser.InstruccionContext ins : cuerpoCtx.instruccion()) {
                Instruccion instruccion = instruccionDe(ins);
                if (instruccion != null) {
                    cuerpo.add(instruccion);
                }
            }
        }

        return new DefinicionFuncion(nombre, tipoRetorno, parametros,
                declaraciones, cuerpo, linea(ctx), columna(ctx));
    }

    // Instrucciones--------------------------------------------------------------

    @Override
    public void exitInstruccion(CodexLatinusParser.InstruccionContext ctx) {
        propagar(ctx, ctx.getChild(0));
    }

    @Override
    public void exitBloque(CodexLatinusParser.BloqueContext ctx) {
        List<Instruccion> instrucciones = new ArrayList<>();
        for (CodexLatinusParser.InstruccionContext ins : ctx.instruccion()) {
            Instruccion instruccion = instruccionDe(ins);
            if (instruccion != null) {
                instrucciones.add(instruccion);
            }
        }
        guardar(ctx, new Bloque(instrucciones, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitAsignacionSimple(CodexLatinusParser.AsignacionSimpleContext ctx) {
        guardar(ctx, new Asignacion(expresionDe(ctx.objetivo()),
                expresionDe(ctx.expresion()), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitAsignacionEstructura(CodexLatinusParser.AsignacionEstructuraContext ctx) {
        guardar(ctx, new Asignacion(expresionDe(ctx.objetivo()),
                expresionDe(ctx.literalEstructura()), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitAsignacionLista(CodexLatinusParser.AsignacionListaContext ctx) {
        guardar(ctx, new Asignacion(expresionDe(ctx.objetivo()),
                expresionDe(ctx.listaValores()), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitIncremento(CodexLatinusParser.IncrementoContext ctx) {
        boolean esIncremento = ctx.MASMAS() != null;
        Operador operador = esIncremento ? Operador.INCREMENTO : Operador.DECREMENTO;
        guardar(ctx, new IncrementoDecremento(expresionDe(ctx.objetivo()), operador,
                linea(ctx), columna(ctx)));
    }

    @Override
    public void exitLlamadaInstruccion(CodexLatinusParser.LlamadaInstruccionContext ctx) {
        Nodo nodo = obtener(ctx.llamadaFuncion());
        if (nodo instanceof LlamadaFuncion) {
            guardar(ctx, new LlamadaInstruccion((LlamadaFuncion) nodo,
                    linea(ctx), columna(ctx)));
        }
    }

    // Condicionales-------------------------------------------------------------

    @Override
    public void exitRamaAliterSi(CodexLatinusParser.RamaAliterSiContext ctx) {
        Bloque bloque = (Bloque) obtener(ctx.bloque());
        guardar(ctx, new RamaCondicional(expresionDe(ctx.expresion()), bloque,
                linea(ctx), columna(ctx)));
    }

    @Override
    public void exitCondicional(CodexLatinusParser.CondicionalContext ctx) {
        List<RamaCondicional> ramas = new ArrayList<>();

        // Primera rama: el si inicial
        Bloque bloqueSi = (Bloque) obtener(ctx.bloque());
        ramas.add(new RamaCondicional(expresionDe(ctx.expresion()), bloqueSi,
                linea(ctx), columna(ctx)));

        // Ramas aliter con condicion
        for (CodexLatinusParser.RamaAliterSiContext rama : ctx.ramaAliterSi()) {
            Nodo nodo = obtener(rama);
            if (nodo instanceof RamaCondicional) {
                ramas.add((RamaCondicional) nodo);
            }
        }

        // Rama aliter final sin condicion
        Bloque ramaFinal = null;
        if (ctx.ramaAliter() != null) {
            ramaFinal = (Bloque) obtener(ctx.ramaAliter().bloque());
        }

        guardar(ctx, new Condicional(ramas, ramaFinal, linea(ctx), columna(ctx)));
    }

    // Ciclos-------------------------------------------------------------------

    @Override
    public void exitCicloDum(CodexLatinusParser.CicloDumContext ctx) {
        guardar(ctx, new CicloDum(expresionDe(ctx.expresion()),
                (Bloque) obtener(ctx.bloque()), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitCicloFacere(CodexLatinusParser.CicloFacereContext ctx) {
        guardar(ctx, new CicloFacere((Bloque) obtener(ctx.bloque()),
                expresionDe(ctx.expresion()), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitCicloPer(CodexLatinusParser.CicloPerContext ctx) {
        Instruccion inicializacion = instruccionDe(ctx.inicializacionPer());
        Instruccion actualizacion = instruccionDe(ctx.actualizacionPer());
        guardar(ctx, new CicloPer(inicializacion, expresionDe(ctx.expresion()),
                actualizacion, (Bloque) obtener(ctx.bloque()),
                linea(ctx), columna(ctx)));
    }

    @Override
    public void exitPerDeclara(CodexLatinusParser.PerDeclaraContext ctx) {
        guardar(ctx, new DeclaracionVariable(ctx.ID().getText(), tipoDe(ctx.tipo()),
                expresionDe(ctx.expresion()), true, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitPerAsigna(CodexLatinusParser.PerAsignaContext ctx) {
        guardar(ctx, new Asignacion(expresionDe(ctx.objetivo()),
                expresionDe(ctx.expresion()), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitPerIncremento(CodexLatinusParser.PerIncrementoContext ctx) {
        boolean esIncremento = ctx.MASMAS() != null;
        Operador operador = esIncremento ? Operador.INCREMENTO : Operador.DECREMENTO;
        guardar(ctx, new IncrementoDecremento(expresionDe(ctx.objetivo()), operador,
                linea(ctx), columna(ctx)));
    }

    @Override
    public void exitPerAsignacion(CodexLatinusParser.PerAsignacionContext ctx) {
        guardar(ctx, new Asignacion(expresionDe(ctx.objetivo()),
                expresionDe(ctx.expresion()), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitPerge(CodexLatinusParser.PergeContext ctx) {
        guardar(ctx, new Perge(linea(ctx), columna(ctx)));
    }

    @Override
    public void exitInterrumpe(CodexLatinusParser.InterrumpeContext ctx) {
        guardar(ctx, new Interrumpe(linea(ctx), columna(ctx)));
    }

    @Override
    public void exitReddere(CodexLatinusParser.ReddereContext ctx) {
        guardar(ctx, new Reddere(expresionDe(ctx.expresion()), linea(ctx), columna(ctx)));
    }

    // Entrada y salida---------------------------------------------------------

    @Override
    public void exitImprimir(CodexLatinusParser.ImprimirContext ctx) {
        List<Expresion> valores = new ArrayList<>();
        for (CodexLatinusParser.ExpresionContext exp : ctx.expresion()) {
            Expresion expresion = expresionDe(exp);
            if (expresion != null) {
                valores.add(expresion);
            }
        }
        guardar(ctx, new Imprimir(valores, linea(ctx), columna(ctx)));
    }

    @Override
    public void exitLeerEnVariable(CodexLatinusParser.LeerEnVariableContext ctx) {
        guardar(ctx, new Leer(expresionDe(ctx.objetivo()), linea(ctx), columna(ctx)));
    }

    @Override
    public void exitLeerDescartado(CodexLatinusParser.LeerDescartadoContext ctx) {
        guardar(ctx, new Leer(null, linea(ctx), columna(ctx)));
    }
}
