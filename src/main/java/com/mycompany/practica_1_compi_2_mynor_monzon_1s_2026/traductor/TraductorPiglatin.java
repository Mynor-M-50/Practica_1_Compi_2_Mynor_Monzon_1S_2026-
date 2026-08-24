/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.traductor;

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

import java.util.List;

/**
 *
 * @author mynorm50
 */

/*
 Traduce el programa a piglatin recorriendo el AST

 IMPORTANTE
 La traduccion se hace nodo por nodo, reconstruyendo el codigo desde
 el arbol. En ningun momento se toca el texto original con replace ni
 con expresiones regulares, tal como exige el enunciado.
 
 Solo se traducen identificadores y palabras reservadas. Los literales
 de texto, los numeros y los simbolos del lenguaje quedan intactos.
 Las funciones especiales cambian por la ley que se mciona

 */

public class TraductorPiglatin {

    private static final String SANGRIA = "    ";

    private final StringBuilder salida = new StringBuilder();

    
     //Recorre el AST completo y devuelve el codigo traducido
     //Se espera que el programa ya haya pasado el analisis semantico
     
    public String traducir(NodoPrograma programa) {
        salida.setLength(0);

        if (programa == null) {
            return "";
        }

        //  Seccion de variables globales 
        if (!programa.getDeclaracionesGlobales().isEmpty()) {
            salida.append(palabra("VARIABILES")).append(">").append(salto());
            for (Instruccion declaracion : programa.getDeclaracionesGlobales()) {
                traducirInstruccion(declaracion, 0);
            }
            salida.append(salto());
        }

        //  Seccion de funciones 
        if (!programa.getFunciones().isEmpty()) {
            salida.append(palabra("MUNERA")).append(">").append(salto());
            for (DefinicionFuncion funcion : programa.getFunciones()) {
                traducirFuncion(funcion);
                salida.append(salto());
            }
        }

        //  Seccion principal 
        
        salida.append(palabra("MAIOR")).append(">").append(salto());
        for (Instruccion instruccion : programa.getInstruccionesPrincipales()) {
            traducirInstruccion(instruccion, 0);
        }

        salida.append(salto());
        salida.append(palabra("FINIS")).append(";").append(salto());

        return salida.toString();
    }

    // Funciones----------------------------/--------------------------------------------

    private void traducirFuncion(DefinicionFuncion funcion) {
        StringBuilder linea = new StringBuilder();

        if (funcion.esActio()) {
            linea.append(palabra("actio")).append(" ");
        } else {
            linea.append(palabra("ratio")).append(" ");
            linea.append(traducirTipo(funcion.getTipoRetorno())).append(" ");
        }

        linea.append(palabra(funcion.getNombre())).append("(");

        List<Parametro> parametros = funcion.getParametros();
        for (int i = 0; i < parametros.size(); i++) {
            if (i > 0) {
                linea.append(", ");
            }
            Parametro parametro = parametros.get(i);
            linea.append(palabra("esto")).append(" ");
            linea.append(palabra(parametro.getNombre())).append(" : ");
            linea.append(traducirTipo(parametro.getTipo()));
        }

        linea.append(") {");
        escribir(linea.toString(), 0);

        // Bloque local de declaraciones
        if (!funcion.getDeclaracionesLocales().isEmpty()) {
            escribir(palabra("VARIABILES") + "[", 1);
            for (Instruccion declaracion : funcion.getDeclaracionesLocales()) {
                traducirInstruccion(declaracion, 2);
            }
            escribir("]", 1);
        }

        for (Instruccion instruccion : funcion.getCuerpo()) {
            traducirInstruccion(instruccion, 1);
        }

        escribir("} " + palabra("finis") + ";", 0);
    }

    // Instrucciones-----------------------------------------------------------------------

    private void traducirInstruccion(Instruccion instruccion, int nivel) {
        if (instruccion == null) {
            return;
        }

        if (instruccion instanceof DeclaracionVariable) {
            traducirDeclaracionVariable((DeclaracionVariable) instruccion, nivel);
        } else if (instruccion instanceof DeclaracionArreglo) {
            traducirDeclaracionArreglo((DeclaracionArreglo) instruccion, nivel);
        } else if (instruccion instanceof DefinicionEstructura) {
            traducirDefinicionEstructura((DefinicionEstructura) instruccion, nivel);
        } else if (instruccion instanceof Asignacion) {
            Asignacion asignacion = (Asignacion) instruccion;
            escribir(traducirExpresion(asignacion.getObjetivo()) + " = "
                    + traducirExpresion(asignacion.getValor()) + ";", nivel);
        } else if (instruccion instanceof IncrementoDecremento) {
            IncrementoDecremento incremento = (IncrementoDecremento) instruccion;
            escribir(traducirExpresion(incremento.getObjetivo())
                    + incremento.getOperador().getSimbolo() + ";", nivel);
        } else if (instruccion instanceof Condicional) {
            traducirCondicional((Condicional) instruccion, nivel);
        } else if (instruccion instanceof CicloDum) {
            traducirCicloDum((CicloDum) instruccion, nivel);
        } else if (instruccion instanceof CicloFacere) {
            traducirCicloFacere((CicloFacere) instruccion, nivel);
        } else if (instruccion instanceof CicloPer) {
            traducirCicloPer((CicloPer) instruccion, nivel);
        } else if (instruccion instanceof Perge) {
            escribir(palabra("perge") + ";", nivel);
        } else if (instruccion instanceof Interrumpe) {
            escribir(palabra("interrumpe") + ";", nivel);
        } else if (instruccion instanceof Reddere) {
            traducirReddere((Reddere) instruccion, nivel);
        } else if (instruccion instanceof Imprimir) {
            traducirImprimir((Imprimir) instruccion, nivel);
        } else if (instruccion instanceof Leer) {
            traducirLeer((Leer) instruccion, nivel);
        } else if (instruccion instanceof LlamadaInstruccion) {
            escribir(traducirExpresion(((LlamadaInstruccion) instruccion).getLlamada())
                    + ";", nivel);
        } else if (instruccion instanceof Bloque) {
            for (Instruccion interna : ((Bloque) instruccion).getInstrucciones()) {
                traducirInstruccion(interna, nivel);
            }
        }
    }

    private void traducirDeclaracionVariable(DeclaracionVariable declaracion, int nivel) {
        StringBuilder linea = new StringBuilder();
        linea.append(palabra("esto")).append(" ");
        linea.append(palabra(declaracion.getNombre())).append(" : ");

        if (declaracion.isTipoExplicito()) {
            linea.append(traducirTipo(declaracion.getTipo()));
            if (declaracion.tieneValorInicial()) {
                linea.append(" ").append(traducirExpresion(declaracion.getValorInicial()));
            }
        } else {
            // Forma especial sin tipo:  esto activo : verum;
            linea.append(traducirExpresion(declaracion.getValorInicial()));
        }

        linea.append(";");
        escribir(linea.toString(), nivel);
    }

    private void traducirDeclaracionArreglo(DeclaracionArreglo declaracion, int nivel) {
        StringBuilder linea = new StringBuilder();
        linea.append(palabra("series")).append(" ");
        linea.append(palabra(declaracion.getNombre()));

        if (declaracion.tieneDimension()) {
            linea.append("[").append(traducirExpresion(declaracion.getDimension())).append("]");
        }

        linea.append(" : ");

        if (declaracion.isTipoExplicito()) {
            linea.append(traducirTipo(declaracion.getTipoElemento()));
            if (declaracion.tieneValoresIniciales()) {
                linea.append(" ");
            }
        }

        if (declaracion.tieneValoresIniciales()) {
            linea.append(traducirExpresion(declaracion.getValoresIniciales()));
        }

        linea.append(";");
        escribir(linea.toString(), nivel);
    }

    private void traducirDefinicionEstructura(DefinicionEstructura definicion, int nivel) {
        escribir(palabra("structura") + " " + palabra(definicion.getNombre()) + " {", nivel);

        List<CampoEstructura> campos = definicion.getCampos();
        for (int i = 0; i < campos.size(); i++) {
            CampoEstructura campo = campos.get(i);
            StringBuilder linea = new StringBuilder();

            if (campo.esArreglo()) {
                linea.append(palabra("series")).append(" ");
                linea.append(palabra(campo.getNombre()));
                if (campo.getDimension() != null) {
                    linea.append("[").append(traducirExpresion(campo.getDimension())).append("]");
                }
                linea.append(" : ").append(traducirTipo(campo.getTipo().tipoElemento()));
            } else {
                linea.append(palabra("esto")).append(" ");
                linea.append(palabra(campo.getNombre())).append(" : ");
                linea.append(traducirTipo(campo.getTipo()));
            }

            if (i < campos.size() - 1) {
                linea.append(",");
            }
            escribir(linea.toString(), nivel + 1);
        }

        escribir("} " + palabra("finis") + ";", nivel);
    }

    private void traducirCondicional(Condicional condicional, int nivel) {
        List<RamaCondicional> ramas = condicional.getRamas();

        for (int i = 0; i < ramas.size(); i++) {
            RamaCondicional rama = ramas.get(i);
            String encabezado = (i == 0)
                    ? palabra("si") + " ("
                    : "} " + palabra("aliter") + " (";

            escribir(encabezado + traducirExpresion(rama.getCondicion()) + ") {", nivel);
            traducirCuerpo(rama.getBloque(), nivel + 1);
        }

        if (condicional.tieneRamaFinal()) {
            escribir("} " + palabra("aliter") + " {", nivel);
            traducirCuerpo(condicional.getRamaFinal(), nivel + 1);
        }

        escribir("} " + palabra("finis") + ";", nivel);
    }

    private void traducirCicloDum(CicloDum ciclo, int nivel) {
        escribir(palabra("dum") + " (" + traducirExpresion(ciclo.getCondicion()) + ") {", nivel);
        traducirCuerpo(ciclo.getCuerpo(), nivel + 1);
        escribir("} " + palabra("finis") + ";", nivel);
    }

    private void traducirCicloFacere(CicloFacere ciclo, int nivel) {
        escribir(palabra("facere") + " {", nivel);
        traducirCuerpo(ciclo.getCuerpo(), nivel + 1);
        escribir("} " + palabra("dum") + " ("
                + traducirExpresion(ciclo.getCondicion()) + ");", nivel);
    }

    private void traducirCicloPer(CicloPer ciclo, int nivel) {
        String inicio = traducirInstruccionEnLinea(ciclo.getInicializacion());
        String actualizacion = traducirInstruccionEnLinea(ciclo.getActualizacion());

        escribir(palabra("per") + " (" + inicio + "; "
                + traducirExpresion(ciclo.getCondicion()) + "; "
                + actualizacion + ") {", nivel);
        traducirCuerpo(ciclo.getCuerpo(), nivel + 1);
        escribir("} " + palabra("finis") + ";", nivel);
    }

    
     // Version en una sola linea, sin punto y coma, para las partes del
     // encabezado del ciclo per
     
    private String traducirInstruccionEnLinea(Instruccion instruccion) {
        if (instruccion instanceof DeclaracionVariable) {
            DeclaracionVariable declaracion = (DeclaracionVariable) instruccion;
            StringBuilder linea = new StringBuilder();
            linea.append(palabra("esto")).append(" ");
            linea.append(palabra(declaracion.getNombre())).append(" : ");
            linea.append(traducirTipo(declaracion.getTipo()));
            if (declaracion.tieneValorInicial()) {
                linea.append(" ").append(traducirExpresion(declaracion.getValorInicial()));
            }
            return linea.toString();
        }

        if (instruccion instanceof Asignacion) {
            Asignacion asignacion = (Asignacion) instruccion;
            return traducirExpresion(asignacion.getObjetivo()) + " = "
                    + traducirExpresion(asignacion.getValor());
        }

        if (instruccion instanceof IncrementoDecremento) {
            IncrementoDecremento incremento = (IncrementoDecremento) instruccion;
            return traducirExpresion(incremento.getObjetivo())
                    + incremento.getOperador().getSimbolo();
        }

        return "";
    }

    private void traducirReddere(Reddere reddere, int nivel) {
        if (reddere.tieneValor()) {
            escribir(palabra("reddere") + " "
                    + traducirExpresion(reddere.getValor()) + ";", nivel);
        } else {
            escribir(palabra("reddere") + ";", nivel);
        }
    }

    // Ley porcina: el operador de impresion se cambia por %OINK
    private void traducirImprimir(Imprimir imprimir, int nivel) {
        StringBuilder linea = new StringBuilder();
        for (Expresion valor : imprimir.getValores()) {
            linea.append(ReglasPiglatin.IMPRIMIR).append(" ");
            linea.append(traducirExpresion(valor)).append(" ");
        }
        escribir(linea.toString().trim() + ";", nivel);
    }

    // Ley porcina: el operador de lectura se cambia por %OINK_OINK
    private void traducirLeer(Leer leer, int nivel) {
        if (leer.tieneObjetivo()) {
            escribir(traducirExpresion(leer.getObjetivo()) + " "
                    + ReglasPiglatin.LEER + ";", nivel);
        } else {
            escribir(ReglasPiglatin.LEER + ";", nivel);
        }
    }

    private void traducirCuerpo(Bloque bloque, int nivel) {
        if (bloque == null) {
            return;
        }
        for (Instruccion instruccion : bloque.getInstrucciones()) {
            traducirInstruccion(instruccion, nivel);
        }
    }

    // Expresiones/------------------------------------------------------------------------------

    private String traducirExpresion(Expresion expresion) {
        if (expresion == null) {
            return "";
        }

        if (expresion instanceof Literal) {
            return traducirLiteral((Literal) expresion);
        }

        if (expresion instanceof Identificador) {
            return palabra(((Identificador) expresion).getNombre());
        }

        if (expresion instanceof OperacionBinaria) {
            return traducirBinaria((OperacionBinaria) expresion);
        }

        if (expresion instanceof OperacionUnaria) {
            OperacionUnaria unaria = (OperacionUnaria) expresion;
            String simbolo = (unaria.getOperador() == Operador.NEGACION)
                    ? palabra("non") + " "
                    : unaria.getOperador().getSimbolo();
            return simbolo + traducirExpresion(unaria.getOperando());
        }

        if (expresion instanceof AccesoArreglo) {
            AccesoArreglo acceso = (AccesoArreglo) expresion;
            return traducirExpresion(acceso.getBase())
                    + "[" + traducirExpresion(acceso.getIndice()) + "]";
        }

        if (expresion instanceof AccesoAtributo) {
            AccesoAtributo acceso = (AccesoAtributo) expresion;
            return traducirExpresion(acceso.getBase())
                    + "." + palabra(acceso.getNombreAtributo());
        }

        if (expresion instanceof LlamadaFuncion) {
            return traducirLlamada((LlamadaFuncion) expresion);
        }

        if (expresion instanceof LiteralLista) {
            return traducirLista((LiteralLista) expresion);
        }

        if (expresion instanceof LiteralEstructura) {
            return traducirLiteralEstructura((LiteralEstructura) expresion);
        }

        return "";
    }

    
     // Los literales de texto, numero y caracter no se traducen
     // Los booleanos si, porque verum y falsus son palabras reservadas
    
    private String traducirLiteral(Literal literal) {
        if (literal.getTipoLiteral() == TipoPrimitivo.BOOLEANO) {
            boolean valor = Boolean.TRUE.equals(literal.getValor());
            return palabra(valor ? "verum" : "falsus");
        }
        return literal.comoCodigoFuente();
    }

    
     // Reconstruye la operacion agregando parentesis solo donde hacen
     // falta para conservar el significado original,la precedencia ya
     // esta representada por la forma del arbol
     
    private String traducirBinaria(OperacionBinaria operacion) {
        int precedenciaPadre = precedencia(operacion.getOperador());

        String izquierdo = traducirOperando(operacion.getIzquierdo(),
                precedenciaPadre, false);
        String derecho = traducirOperando(operacion.getDerecho(),
                precedenciaPadre, true);

        return izquierdo + " " + operacion.getOperador().getSimbolo() + " " + derecho;
    }

    private String traducirOperando(Expresion operando, int precedenciaPadre,
                                    boolean esDerecho) {
        String texto = traducirExpresion(operando);

        if (!(operando instanceof OperacionBinaria)) {
            return texto;
        }

        int precedenciaHijo = precedencia(((OperacionBinaria) operando).getOperador());

        // El hijo de menor precedencia necesita parentesis. En el lado
        // derecho tambien se ponen con igual precedencia, porque los
        // operadores del lenguaje asocian a la izquierda
        
        boolean necesita = esDerecho
                ? precedenciaHijo <= precedenciaPadre
                : precedenciaHijo < precedenciaPadre;

        return necesita ? "(" + texto + ")" : texto;
    }

    private int precedencia(Operador operador) {
        switch (operador) {
            case OR:
                return 1;
            case AND:
                return 2;
            case IGUALDAD:
            case DIFERENCIA:
                return 3;
            case MENOR:
            case MAYOR:
            case MENOR_IGUAL:
            case MAYOR_IGUAL:
                return 4;
            case SUMA:
            case RESTA:
                return 5;
            case MULTIPLICACION:
            case DIVISION:
                return 6;
            default:
                return 7;
        }
    }

    private String traducirLlamada(LlamadaFuncion llamada) {
        StringBuilder texto = new StringBuilder();
        texto.append(palabra(llamada.getNombre())).append("(");

        List<Expresion> argumentos = llamada.getArgumentos();
        for (int i = 0; i < argumentos.size(); i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(traducirExpresion(argumentos.get(i)));
        }

        texto.append(")");
        return texto.toString();
    }

    private String traducirLista(LiteralLista lista) {
        StringBuilder texto = new StringBuilder("{");
        List<Expresion> valores = lista.getValores();
        for (int i = 0; i < valores.size(); i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(traducirExpresion(valores.get(i)));
        }
        texto.append("}");
        return texto.toString();
    }

    private String traducirLiteralEstructura(LiteralEstructura literal) {
        StringBuilder texto = new StringBuilder("{ ");
        List<AtributoInicializado> atributos = literal.getAtributos();
        for (int i = 0; i < atributos.size(); i++) {
            if (i > 0) {
                texto.append(", ");
            }
            AtributoInicializado atributo = atributos.get(i);
            texto.append(palabra(atributo.getNombre())).append(": ");
            texto.append(traducirExpresion(atributo.getValor()));
        }
        texto.append(" }");
        return texto.toString();
    }

    // Tipos y utilidades/----------------------------------------------------------------

     // Los nombres de tipo son palabras reservadas y los nombres de
     // estructura son identificadores, asi que ambos se traducen.
  
    private String traducirTipo(Tipo tipo) {
        if (tipo == null) {
            return "";
        }
        if (tipo.esEstructura()) {
            return palabra(tipo.getNombreEstructura());
        }
        return palabra(tipo.getPrimitivo().getNombre());
    }

    /** Atajo para aplicar las leyes de traduccion. */
    private String palabra(String texto) {
        return ReglasPiglatin.traducir(texto);
    }

    private void escribir(String texto, int nivel) {
        salida.append(SANGRIA.repeat(Math.max(0, nivel)));
        salida.append(texto);
        salida.append(salto());
    }

    private String salto() {
        return System.lineSeparator();
    }
}