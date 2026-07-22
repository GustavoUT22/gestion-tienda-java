# Análisis del proyecto — alineado a la consigna (sección 5 del sílabo)

## Caso elegido

Sistema de gestión de productos, clientes y ventas — es el caso que el propio sílabo sugiere
textualmente ("Se sugiere trabajar con un sistema de gestión de productos, clientes y ventas, o con un
caso equivalente aprobado por el docente").

## Checklist: los 11 puntos obligatorios de la consigna → cómo los cubre el proyecto

| # | Exige el sílabo | Cómo lo cubre el proyecto |
|---|---|---|
| 1 | Análisis del caso y definición de clases principales | Ver sección "Clases principales" abajo |
| 2 | Objetos con atributos, constructores y métodos | Cada clase abajo tendrá su constructor y métodos propios |
| 3 | Estructuras selectivas y repetitivas para reglas del negocio | Ej: validar stock disponible antes de vender (if), recorrer el detalle de una venta para sumar el total (for) |
| 4 | Métodos de Math y String en validaciones/cálculos | Ej: redondeo del IGV con Math, validación de formato de DNI/correo con String |
| 5 | Colecciones para almacenar y manipular información | Ej: lista de productos en inventario, lista de líneas dentro de una venta |
| 6 | Jerarquía con herencia (+ clase abstracta o interfaz si corresponde) | `Persona` (abstracta) → `Cliente`, `Empleado` |
| 7 | Encapsulamiento + al menos un caso de sobrescritura/polimorfismo | Atributos privados con getters/setters; `Cliente` y `Empleado` sobrescriben un método de `Persona` |
| 8 | Persistencia con JDBC o JPA | **JDBC** (decidido por el equipo) |
| 9 | Interfaz funcional: consola avanzada o web JSP/Tomcat | **Consola avanzada** (decidido por el equipo, dado el poco avance del curso con JSP/Tomcat) |
| 10 | Repositorio GitHub con estructura clara | Entregable final, no parte del análisis |
| 11 | README completo con integrantes, ejecución y video | Entregable final, no parte del análisis |

**Decisiones ya tomadas:**
- **Persistencia: JDBC.** Sin configuración de proveedor extra (Hibernate, `persistence.xml`); cada
  entidad tiene su propio DAO (`ClienteDAO`, `ProductoDAO`, `VentaDAO`, etc.) que ejecuta directamente
  sus `SELECT`/`INSERT`/`UPDATE` sobre la base de datos. Más predecible para depurar en el tiempo que
  tienen para el proyecto.
- **Interfaz: consola avanzada.** El curso vio poco JSP/Tomcat, así que ir por web sumaría riesgo técnico
  sin beneficio adicional en la rúbrica. La consola puede tener menú por roles (administrador/vendedor),
  búsquedas y reportes formateados — es suficiente para demostrar los aprendizajes del curso.

## Clases principales (punto 1, 2, 6, 7)

| Clase | Atributos | Justificación |
|---|---|---|
| `Persona` (abstracta) | id, dni, nombre, telefono, correo | Atributos comunes a cliente y empleado; declara un método sin implementar para que cada subclase lo sobrescriba |
| `Cliente` | (hereda de Persona) | Sobrescribe el método de Persona a su manera → cubre polimorfismo |
| `Empleado` | cargo, salario (+ hereda de Persona) | Sobrescribe el método de Persona de forma distinta → segundo caso de polimorfismo |
| `Producto` | codigo, nombre, categoria, precio, stock | Entidad central del inventario |
| `Venta` | fecha, cliente, empleado, total | Cabecera de una venta |
| `DetalleVenta` | producto, cantidad, subtotal | Una venta puede tener varios productos, por eso se separa de `Venta` (relación 1 a muchos) |

Esta estructura por sí sola ya resuelve los puntos 1, 2, 6 y 7 de la consigna: hay jerarquía real
(`Persona` → `Cliente`/`Empleado`), hay un punto claro de sobrescritura de método (polimorfismo), y todos
los atributos son privados (encapsulamiento).

## Lo que falta definir antes de programar

- **Motor de base de datos** para usar con JDBC (MySQL, PostgreSQL, etc. — cualquiera funciona con JDBC, solo cambia el driver).
- **Puntos 3, 4, 5**: no se definen en el análisis — se resuelven directamente al programar cada clase
  (dónde va cada `if`, cada `for`, cada validación con `String`, cada colección).