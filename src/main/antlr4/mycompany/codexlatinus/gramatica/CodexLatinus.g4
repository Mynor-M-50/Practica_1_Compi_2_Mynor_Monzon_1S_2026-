grammar CodexLatinus;

// -----------------
// REGLAS DEL PARSER
// -----------------

programa
    : elementoGlobal* EOF
    ;

elementoGlobal
    : bloqueEstructuras
    | bloqueVariables
    | definicionEstructura
    | definicionFuncion
    | instruccion
    ;

// --------------------
// SECCIONES ESPECIALES
// --------------------

bloqueEstructuras
    : ESTRUCTURAS COR_A definicionEstructura* COR_C
    ;

bloqueVariables
    : VARIABILES COR_A declaracion* COR_C
    ;


// -----
// TIPOS
// -----

tipo
    : NUMERUS       # TipoNumerus
    | DECIMALIS     # TipoDecimalis
    | TEXTUM        # TipoTextum
    | LITTERA       # TipoLittera
    | ID            # TipoEstructura
    ;

valorBooleano
    : VERUM
    | FALSUS
    ;


// -------------
// DECLARACIONES
// -------------

declaracion
    : declaracionVariable
    | declaracionArreglo
    | definicionEstructura
    ;

// El orden de las alternativas importa
//  Primero estructura
//  segundo booleana
//  Terero con valor
//  cuarto sin valor
declaracionVariable
    : ESTO ID DOS_PUNTOS ID literalEstructura PYC?      # DeclEstructura
    | ESTO ID DOS_PUNTOS valorBooleano expresion? PYC?  # DeclBooleana
    | ESTO ID DOS_PUNTOS tipo expresion PYC?            # DeclConValor
    | ESTO ID DOS_PUNTOS tipo PYC?                      # DeclSinValor
    ;

// series id[dim] : tipo {valores};
// La dimension es opcional porque dentro de una definicion de
// estructura no se especifica

declaracionArreglo
    : SERIES ID dimension? DOS_PUNTOS tipo listaValores? PYC?  # ArregloTipado
    | SERIES ID dimension? DOS_PUNTOS listaValores PYC?        # ArregloInferido
    ;

dimension
    : COR_A expresion COR_C
    ;

listaValores
    : LLAVE_A (expresion (COMA expresion)*)? COMA? LLAVE_C
    ;


// -----------
// ESTRUCTURAS
// Los campos se pueden separar con ; o con , 
// -----------

definicionEstructura
    : STRUCTURA ID LLAVE_A campoEstructura* LLAVE_C FINIS PYC?
    ;

campoEstructura
    : ESTO ID DOS_PUNTOS tipo separadorCampo?           # CampoSimple
    | ESTO ID DOS_PUNTOS valorBooleano separadorCampo?  # CampoBooleano
    | SERIES ID dimension? DOS_PUNTOS tipo separadorCampo?  # CampoArreglo
    ;

separadorCampo
    : PYC
    | COMA
    ;

literalEstructura
    : LLAVE_A (asignacionAtributo (separadorCampo asignacionAtributo)*)? separadorCampo? LLAVE_C
    ;

asignacionAtributo
    : ID DOS_PUNTOS valorAtributo
    ;

valorAtributo
    : literalEstructura
    | listaValores
    | expresion
    ;

definicionFuncion
    : ACTIO ID PAR_A listaParametros? PAR_C cuerpoFuncion FINIS PYC?        # FuncionSinRetorno
    | RATIO tipo ID PAR_A listaParametros? PAR_C cuerpoFuncion FINIS PYC?   # FuncionConRetorno
    ;

cuerpoFuncion
    : LLAVE_A bloqueVariables? instruccion* LLAVE_C
    ;

listaParametros
    : parametro (COMA parametro)*
    ;

parametro
    : ESTO ID DOS_PUNTOS tipo
    ;

llamadaFuncion
    : ID PAR_A listaArgumentos? PAR_C
    ;

listaArgumentos
    : expresion (COMA expresion)*
    ;

instruccion
    : asignacion
    | incremento
    | condicional
    | cicloDum
    | cicloFacere
    | cicloPer
    | perge
    | interrumpe
    | reddere
    | imprimir
    | leer
    | llamadaInstruccion
    | declaracionVariable
    | declaracionArreglo
    | definicionEstructura
    ;

bloque
    : LLAVE_A instruccion* LLAVE_C
    ;

// Objetivo de asignacion o acceso encadenado.
objetivo
    : ID sufijoAcceso*
    ;

sufijoAcceso
    : PUNTO ID
    | COR_A expresion COR_C
    ;

asignacion
    : objetivo IGUAL literalEstructura PYC?   # AsignacionEstructura
    | objetivo IGUAL listaValores PYC?        # AsignacionLista
    | objetivo IGUAL expresion PYC?           # AsignacionSimple
    ;

// ++ y -- se aplican en cualquier ambito (foro, duda 7)
incremento
    : objetivo (MASMAS | MENOSMENOS) PYC?
    ;

llamadaInstruccion
    : llamadaFuncion PYC?
    ;


// -------------
// CONDICIONALES
// -------------

condicional
    : SI PAR_A expresion PAR_C bloque
      ramaAliterSi*
      ramaAliter?
      FINIS PYC?
    ;

ramaAliterSi
    : ALITER PAR_A expresion PAR_C bloque
    ;

ramaAliter
    : ALITER bloque
    ;


// ------
// CICLOS
// ------

cicloDum
    : DUM PAR_A expresion PAR_C bloque FINIS PYC?
    ;

cicloFacere
    : FACERE bloque DUM PAR_A expresion PAR_C PYC?
    ;

cicloPer
    : PER PAR_A inicializacionPer PYC expresion PYC actualizacionPer PAR_C
      bloque (FINIS PYC?)?
    ;

inicializacionPer
    : ESTO ID DOS_PUNTOS tipo expresion   # PerDeclara
    | objetivo IGUAL expresion            # PerAsigna
    ;

actualizacionPer
    : objetivo (MASMAS | MENOSMENOS)      # PerIncremento
    | objetivo IGUAL expresion            # PerAsignacion
    ;

perge
    : PERGE PYC?
    ;

interrumpe
    : INTERRUMPE PYC?
    ;

reddere
    : REDDERE expresion? PYC?
    ;


// ---------------------------------
// FUNCIONES ESPECIALES DEL SISTEMA
// >> imprime   |   << lee
// ---------------------------------

imprimir
    : (MAYORMAYOR expresion)+ PYC?
    ;

leer
    : objetivo MENORMENOR PYC?   # LeerEnVariable
    | MENORMENOR PYC?            # LeerDescartado
    ;


// -----------
// EXPRESIONES
// uso la recursividad por la izquierda de ANTLR4 con
// alternativas ordenadas por precedencia (de mayor a menor)
// ANTLR4 resuelve la precedencia por el orden de aparicion
// -----------

expresion
    : PAR_A expresion PAR_C                                          # ExprAgrupada
    | (MENOS | NON) expresion                                        # ExprUnaria
    | expresion (POR | DIV) expresion                                # ExprMulDiv
    | expresion (MAS | MENOS) expresion                              # ExprSumaResta
    | expresion (MENOR | MAYOR | MENORIGUAL | MAYORIGUAL) expresion  # ExprRelacional
    | expresion (IGUALIGUAL | DIFERENTE) expresion                   # ExprIgualdad
    | expresion AND expresion                                        # ExprAnd
    | expresion OR expresion                                         # ExprOr
    | llamadaFuncion                                                 # ExprLlamada
    | objetivo                                                       # ExprAcceso
    | literal                                                        # ExprLiteral
    ;

literal
    : ENTERO    # LitEntero
    | DECIMAL   # LitDecimal
    | CADENA    # LitCadena
    | CARACTER  # LitCaracter
    | VERUM     # LitVerum
    | FALSUS    # LitFalsus
    ;


// ----------------
// REGLAS DEL LEXER
// ----------------

//   Secciones (en mayusculas
VARIABILES  : 'VARIABILES';
ESTRUCTURAS : 'ESTRUCTURAS';

//   declaracion
ESTO      : 'esto';
SERIES    : 'series';
STRUCTURA : 'structura';
FINIS     : 'finis';

//  Tipos primitivos 
NUMERUS   : 'numerus';
DECIMALIS : 'decimalis';
TEXTUM    : 'textum';
LITTERA   : 'littera';

//  Booleanos 
VERUM  : 'verum';
FALSUS : 'falsus';

//  Control de flujo 
SI         : 'si';
ALITER     : 'aliter';
DUM        : 'dum';
FACERE     : 'facere';
PER        : 'per';
PERGE      : 'perge';
INTERRUMPE : 'interrumpe';

//  Funcioes 
ACTIO   : 'actio';
RATIO   : 'ratio';
REDDERE : 'reddere';

//  Operador logico de negacion 
NON : 'non';

//  Operadores de dos carateres (deben ir antes que los de uno) 
MASMAS     : '++';
MENOSMENOS : '--';
MAYORMAYOR : '>>';
MENORMENOR : '<<';
IGUALIGUAL : '==';
DIFERENTE  : '!=';
MENORIGUAL : '<=';
MAYORIGUAL : '>=';
AND        : '&&';
OR         : '||';

//  Operadres de un caracter 
MAS   : '+';
MENOS : '-';
POR   : '*';
DIV   : '/';
IGUAL : '=';
MENOR : '<';
MAYOR : '>';

//  signos de agrupacion y puntuacion 
PAR_A      : '(';
PAR_C      : ')';
LLAVE_A    : '{';
LLAVE_C    : '}';
COR_A      : '[';
COR_C      : ']';
PYC        : ';';
COMA       : ',';
PUNTO      : '.';
DOS_PUNTOS : ':';

//  Literales 
DECIMAL : [0-9]+ '.' [0-9]+ ;
ENTERO  : [0-9]+ ;

CADENA
    : '"' ( '\\' . | ~["\\\r\n] )* '"'
    | '\u201C' ( ~[\u201C\u201D\r\n] )* '\u201D'
    ;

CARACTER
    : '\'' ( '\\' . | ~['\\\r\n] ) '\''
    ;

ID : [a-zA-Z_] [a-zA-Z_0-9]* ;

//  Comentrios y espacios 
COMENTARIO_LINEA  : '//' ~[\r\n]* -> skip ;
COMENTARIO_BLOQUE : '/*' .*? '*/' -> skip ;
ESPACIOS          : [ \t\r\n]+ -> skip ;

// Token de captura par errores lexicos.
ERROR_LEXICO : . ;
