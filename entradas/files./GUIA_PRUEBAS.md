# Guia de pruebas - Codex Latinus

Como correr cada archivo y que debe pasar exactamente.

---

## Como ejecutar

**Por consola** (mas rapido para depurar, la salida se copia facil):

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=com.mycompany.practica_1_compi_2_mynor_monzon_1s_2026.Pruebagramatica -Dexec.args="entradas/validos/01_declaraciones.lat"
```

**Por interfaz** (para probar lo visual):

Archivo > Abrir, luego F5, luego los menus de Reportes y Traducir.

---

## Criterio general

| Carpeta | Que debe pasar |
|---|---|
| `entradas/validos/` | CERO errores. Si alguno reporta error, es un bug del compilador |
| `entradas/errores/` | Errores del tipo indicado, en las lineas indicadas |

Un archivo de `errores/` que compile sin problemas tambien es un bug:
significa que una validacion no esta funcionando.

---

# ARCHIVOS VALIDOS

## 01_declaraciones.lat

Ejercita todos los tipos primitivos y las dos formas de booleano.

Debe compilar sin errores y la tabla de simbolos debe mostrar **14
variables globales**, todas con ambito `global`.

Puntos criticos que verifica:
- `esto activo : bool verum;` y `esto encendido : verum;` conviven
- Declaracion sin valor inicial es valida
- `esto ensanchado : decimalis entero;` funciona porque numerus(3) sube a decimalis(4)
- `esto concatenado : textum texto + " version 1";` concatena

En la traduccion PigLatin: `bool` debe aparecer como `oolbay`.

## 02_arreglos.lat

Debe compilar sin errores. Tabla con **9 arreglos** y 1 variable.

Puntos criticos:
- `series inferido[2] : {verum, verum};` deduce booleano sin escribir el tipo
- `series parcial[5] : numerus {1, 2};` con menos valores que la dimension es valido
- Lectura y escritura por indice constante y por variable
- `enteros[0] + enteros[1]` opera sobre elementos, no sobre el arreglo

## 03_estructuras.lat

El archivo mas exigente. Debe compilar sin errores.

Puntos criticos:
- Campos separados con `;` (Carro) y con `,` (Animal), ambos validos
- `Persona` contiene un `Carro`: estructura anidada
- `Selva` declara `series animales : Animal` sin dimension
- `animales: Animal[3]` fija la dimension al instanciar
- `otroCarro` da los atributos en orden invertido y debe aceptarse
- `yo.miCarro.marca` encadena dos niveles
- `miSelva.animales[0] = { ... }` asigna una instancia a un elemento
- `miSelva.animales[0].nombre` mezcla indice y atributo

Si algo falla aqui, lo mas probable es `esDeclaracionDeDimension()`
en el analizador semantico.

## 04_operadores.lat

Debe compilar sin errores.

Verificacion importante: en el AST graficado, `a + b * 2` debe mostrar
la multiplicacion **mas abajo** que la suma. Si aparece al mismo nivel
o invertida, la precedencia de la gramatica esta mal.

`(a + b) * 2` debe mostrar la suma mas abajo que la multiplicacion.

Tambien verifica que `t2 : textum texto + a` concatena texto con numero.

## 05_condicionales.lat

Debe compilar sin errores.

En el AST, el tercer condicional debe aparecer como
`Condicional(3 ramas)` con una rama final adicional. Si dice
`Condicional(1 ramas)`, el encadenamiento de `aliter` no se esta
construyendo bien.

Tambien verifica entrada y salida dentro de bloques, que el foro
confirmo como valido.

## 06_ciclos.lat

Debe compilar sin errores. Es el que ejercita mas casos nuevos.

Puntos criticos:
- `facere { } dum (i < 3);` con la condicion al final
- `per` con declaracion en la inicializacion
- `per` con asignacion en la inicializacion
- `per` con `k = k + 2` en la actualizacion en vez de `k++`
- `interrumpe` y `perge` dentro de ciclos: NO deben dar error
- Ciclos anidados con control de flujo en el interno

La variable del `per` vive solo dentro del ciclo. En la tabla de
simbolos, `j`, `k`, `m`, `n` y `p` deben aparecer con ambito `per`,
no `global`.

## 07_funciones.lat

Debe compilar sin errores. El segundo mas exigente.

Puntos criticos:
- `actio` sin parametros y con parametros
- `ratio` con y sin bloque `VARIABILES[ ]`
- `clasificar` retorna desde las tres ramas de un condicional: la
  validacion de caminos debe aceptarlo
- `crearPunto` retorna una estructura
- `usarEstructuraLocal` define una estructura dentro de `VARIABILES[ ]`
- `usarPosterior` llama a `ayudante`, que se define **despues**. Esto
  solo funciona por las pasadas separadas del analizador semantico
- `validar` usa `reddere;` sin valor en una funcion actio
- Llamadas anidadas: `duplicar(duplicar(5))`

Si `usarPosterior` da error de funcion no declarada, el registro de
firmas no esta corriendo antes de validar los cuerpos.

## 08_entrada_salida.lat

Debe compilar sin errores.

Puntos criticos:
- `>>` con un valor y con varios encadenados
- `>>` sobre expresiones: `edad + 10` y `edad > 18`
- `<<` en variable, en elemento de arreglo y en atributo
- `<<` sin destino
- Ambos dentro de condicional, dentro de ciclo y dentro de funcion

En la traduccion, todos los `>>` deben ser `%OINK` y todos los `<<`
deben ser `%OINK_OINK`.

## 09_minimo.lat

Solo la seccion `MAIOR>`. Comprueba que `VARIABILES>` y `MUNERA>` son
realmente opcionales. Debe compilar sin errores.

## 10_sin_funciones.lat

Con `VARIABILES>` pero sin `MUNERA>`. Debe compilar sin errores.

---

# ARCHIVOS CON ERRORES

## e01_lexicos.lat

**Esperado: 4 errores LEXICOS**, uno por cada caracter invalido:
`#`, `@`, `$`, `?` (lineas 7, 8, 9, 10).

Nota: `#` solo es valido en pares (`##` abre comentario). Uno solo es
error lexico.

Como el analisis se detiene tras errores lexicos, NO debe construirse
el AST ni la tabla de simbolos.

## e02_sintacticos.lat

**Esperado: varios errores SINTACTICOS.**

Este archivo prueba el **modo panico**. La verificacion clave no es el
numero exacto de errores, sino que reporte **mas de uno**. Si solo
reporta el primero, el modo panico no esta funcionando.

Errores plantados: tipo faltante, dos puntos faltantes, operador sin
operando, condicion incompleta en `si`, `dum` sin condicion, `>>` sin
expresion.

## e03_tipos.lat

**Esperado: 11 errores SEMANTICOS.**

- `texto * 2`, `texto - 1`, `texto / 2` → textum solo concatena con `+`
- `entero && bandera`, `texto || bandera` → logicos exigen booleano
- `non entero` → negacion exige booleano
- `bandera > entero` → booleano no es ordenable
- `decimal` asignado a `numerus` → estrechamiento no permitido
- `si (entero)` y `dum (texto)` → condicion no booleana

Nota: `verum + 1` **si** es valido, porque la jerarquia del enunciado
pone bool en el nivel 1. No aparece en este archivo por eso.

## e04_declaraciones.lat

**Esperado: alrededor de 10 errores SEMANTICOS.**

- `repetida` y `otra` declaradas dos veces
- `funcionRepetida` definida dos veces
- Funcion `otra` choca con la variable global `otra`
- `noExiste` usada sin declarar, dos veces
- `fueraDeSeccion` y `arregloFuera` declarados en `MAIOR>`
- `dentroDelSi` declarado dentro de un bloque de control
- `FueraDeLugar` definida fuera de `VARIABILES`
- `funcionRepetida` usada sin parentesis
- `repetida()` usada como funcion

## e05_estructuras.lat

**Esperado: alrededor de 12 errores SEMANTICOS.**

- `Mala` tiene `campo` repetido
- `incompleta` no inicializa `edad` ni `activo`
- `sobrante` incluye el atributo `inventado`
- `duplicada` inicializa `nombre` dos veces
- `tipoMalo` da tipos incorrectos en los tres atributos
- `NoDefinida` no existe
- `buena.noExiste` no es un atributo de Persona
- `simple.campo` usa punto sobre un numerus
- `>> buena` imprime la estructura completa

## e06_arreglos.lat

**Esperado: alrededor de 12 errores SEMANTICOS.**

- `desbordado` recibe 4 valores para 2 posiciones
- `tipoMalo` recibe cadenas en un arreglo de numerus
- `dimensionMala` usa una cadena como dimension
- `dimensionCero` declara dimension cero
- `numeros[5]`, `numeros[10]`, `numeros[7]` fuera de rango
- `numeros[-1]` indice negativo
- `numeros[texto]` indice no numerico
- `indice[0]` indexa algo que no es arreglo
- `>> numeros` imprime el arreglo completo
- `numeros + 1` opera sobre el arreglo completo

## e07_funciones.lat

**Esperado: alrededor de 13 errores SEMANTICOS.**

- `sinRetorno` nunca retorna
- `retornoMalo` retorna textum donde declara numerus
- `retornoParcial` tiene un camino sin retorno (falta el aliter final)
- `inalcanzable` tiene dos instrucciones despues del reddere
- `noDebeRetornar` es actio y retorna un valor
- `parametroRepetido` repite el parametro `a`
- `NoExiste` y `TampocoExiste` no son estructuras validas
- `noDeclarada` no existe
- `buena(1)` y `buena(1,2,3)` con cantidad incorrecta de argumentos
- `buena("uno","dos")` con tipos incorrectos
- `reddere valor;` fuera de una funcion

## e08_ciclos.lat

**Esperado: alrededor de 10 errores SEMANTICOS.**

- `interrumpe` y `perge` dentro de una funcion sin ciclo
- `interrumpe` y `perge` en `MAIOR>` sin ciclo
- `interrumpe` dentro de un `si` que no esta en un ciclo
- `dum (contador)` condicion numerus
- `dum (texto)` en el facere, condicion textum
- `per` con condicion textum
- `texto++` y `bandera--` incremento no numerico
- `5++` incremento sobre un literal

---

# Lista de verificacion de la interfaz

Marcar cada uno probandolo:

- [ ] Abrir un `.lat` y ver el coloreado aplicado solo
- [ ] Escribir codigo y ver el coloreado actualizarse al teclear
- [ ] Numeros de linea que crecen al agregar lineas
- [ ] Barra de estado con la linea y columna del cursor
- [ ] F5 compila y muestra el resultado en consola
- [ ] Reportes > Reporte de errores con la tabla formateada
- [ ] Reportes > Graficar AST genera `.dot`, `.png` y `.svg`
- [ ] Reportes > Graficar tabla de simbolos
- [ ] Reportes > Pila paso a paso, con Primero, Atras, Siguiente, Ultimo
- [ ] La pila de la izquierda cambia al navegar los pasos
- [ ] Colores de la tabla de pila: azul Shift, rosa Replace, verde Accept
- [ ] El ultimo paso debe ser ACCEPT
- [ ] Graficar pila actual desde la ventana de la pila
- [ ] Traducir > Ver traduccion a PigLatin
- [ ] Traducir > Descargar genera un `.pig`
- [ ] Guardar y Guardar como sobre un `.lat`
- [ ] Varias pestanias abiertas al mismo tiempo
- [ ] Compilar un archivo con errores no debe cerrar el programa
- [ ] Pedir el AST sin haber compilado muestra un aviso, no un error

---

# Cosas que NO se implementan, por si preguntan

- **No hay ejecucion.** El auxiliar confirmo que no se ejecuta nada:
  `>>` y `<<` se analizan y se traducen, pero no leen ni escriben en
  una terminal.
- **No hay arreglos de mas de una dimension.** El enunciado no los
  define.
- **Los literales de texto no se traducen** a PigLatin. Solo
  identificadores y palabras reservadas, como pide el enunciado.
