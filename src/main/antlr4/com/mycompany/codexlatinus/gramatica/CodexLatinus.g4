grammar CodexLatinus;

// -----------------
// REGLAS DEL PARSER
// -----------------

programa
    : seccionVariables? seccionMunera? seccionMaior EOF
    ;

//  Seccion global de declaracines 
seccionVariables
    : VARIABILES MAYOR declaracion*
    ;

//  Seccion de funciones 
seccionMunera
    : MUNERA MAYOR definicionFuncion*
    ;

//  Seccion prinipal obligatoria
seccionMaior
    : MAIOR MAYOR instruccion* FIN_PROGRAMA PYC?
    ;


// -----
// TIPOS
// -----

tipo
    : NUMERUS       # TipoNumerus
    | DECIMALIS     # TipoDecimalis
    | TEXTUM        # TipoTextum
    | LITTERA       # TipoLittera
    | BOOL          # TipoBool
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
//  1 estructura  
//  2 bbooleana    
//  3 con valor   
//  4 sin valor   

declaracionVariable
    : ESTO ID DOS_PUNTOS ID literalEstructura PYC?      # DeclEstructura
    | ESTO ID DOS_PUNTOS valorBooleano expresion? PYC?  # DeclBooleana
    | ESTO ID DOS_PUNTOS tipo expresion PYC?            # DeclConValor
    | ESTO ID DOS_PUNTOS tipo PYC?                      # DeclSinValor
    ;

// series id[dim] : tipo {valores};
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
// -----------

definicionEstructura
    : STRUCTURA ID LLAVE_A campoEstructura* LLAVE_C FINIS PYC?
    ;

campoEstructura
    : ESTO ID DOS_PUNTOS tipo separadorCampo?               # CampoSimple
    | ESTO ID DOS_PUNTOS valorBooleano separadorCampo?      # CampoBooleano
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
    | dimensionPrimitiva
    | expresion
    ;

// Fija la dimension de un arreglo de tipo primitivo dentro de una
// instancia de estructura
dimensionPrimitiva
    : (NUMERUS | DECIMALIS | TEXTUM | LITTERA | BOOL) COR_A expresion COR_C
    ;

// ----------------------------------------------------
// FUNCIONES
// actio -> sin retorno   |   ratio TIPO -> con retorno
// ----------------------------------------------------

definicionFuncion
    : ACTIO ID PAR_A listaParametros? PAR_C cuerpoFuncion FINIS PYC?        # FuncionSinRetorno
    | RATIO tipo ID PAR_A listaParametros? PAR_C cuerpoFuncion FINIS PYC?   # FuncionConRetorno
    ;

cuerpoFuncion
    : LLAVE_A bloqueVariablesLocal? instruccion* LLAVE_C
    ;

// Version local con corchetes, distinta de la seccion global
bloqueVariablesLocal
    : VARIABILES COR_A declaracion* COR_C
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


// -------------
// INSTRUCCIONES
// -------------

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

// Cubre: id, id[expr], id.attr, id.attr[expr].attr
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

// ++ y -- se aplican en cualquier ambito
incremento
    : objetivo (MASMAS | MENOSMENOS) PYC?
    ;

llamadaInstruccion
    : llamadaFuncion PYC?
    ;


// -------------
// CONDICIONALES
// si (...) {} aliter (...) {} aliter {} finis;
// ------------

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


// -----
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


// --------------------------------
// FUNCIONES ESPECIALES DEL SISTEMA
// >> imprime   |   << lee
// --------------------------------

imprimir
    : (MAYORMAYOR expresion)+ PYC?
    ;

leer
    : objetivo MENORMENOR PYC?   # LeerEnVariable
    | MENORMENOR PYC?            # LeerDescartado
    ;


// -----------
// EXPRESIONES
// Recursividad por la izquierda de ANTLR4 con alternativas
// ordenadas por precedencia, de mayor a menor.
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
// El lenguaje es case sensitive.
// ----------------

//  Marcadores de seccion (en mayusculas)
VARIABILES : 'VARIABILES';
MUNERA     : 'MUNERA';
MAIOR      : 'MAIOR';

// cierre del programa completo, distito de finis minuscula
FIN_PROGRAMA : 'FINIS';

//  Declaracion 
ESTO      : 'esto';
SERIES    : 'series';
STRUCTURA : 'structura';
FINIS     : 'finis';

//  Tipos primitivos
NUMERUS   : 'numerus';
DECIMALIS : 'decimalis';
TEXTUM    : 'textum';
LITTERA   : 'littera';
BOOL      : 'bool';

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

//  funciones 
ACTIO   : 'actio';
RATIO   : 'ratio';
REDDERE : 'reddere';

//  Negcion logica 
NON : 'non';

//  Operadores de dos caracteres (antes que los de uno) 
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

//  Operadores de un caracter 
MAS   : '+';
MENOS : '-';
POR   : '*';
DIV   : '/';
IGUAL : '=';
MENOR : '<';
MAYOR : '>';

//  Agrupacion y puntuacion 
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

//  Literaes 
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

//  comentarios y espacios 
// Bloque con ## ... ##  |  linea con //
COMENTARIO_BLOQUE : '##' .*? '##' -> skip ;
COMENTARIO_LINEA  : '//' ~[\r\n]* -> skip ;
ESPACIOS          : [ \t\r\n]+ -> skip ;