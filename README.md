# gestion-tienda-java — Sistema de Gestión de Productos, Clientes y Ventas

Proyecto final del curso de Programación Orientada a Objetos.

## Descripción

Aplicación de consola en Java que permite administrar clientes, empleados, productos e inventario de
una tienda comercial, registrar ventas con cálculo automático de IGV, actualizar el stock y persistir
toda la información en una base de datos mediante JDBC.

## Integrantes y reparto de tareas

| Integrante | Parte a cargo |
|---|---|
| _Nombre 1_ | **Modelo (POO)**: clases `Persona` (abstracta), `Cliente`, `Empleado`, `Producto`, `Venta`, `DetalleVenta`, `Usuario`. Herencia, encapsulamiento, polimorfismo (sobrescritura de método en `Cliente`/`Empleado`). |
| _Nombre 2_ | **Persistencia (JDBC)**: `ConexionBD`, DAOs (`ClienteDAO`, `EmpleadoDAO`, `ProductoDAO`, `VentaDAO`, `UsuarioDAO`), script SQL de creación de la base de datos. |
| _Nombre 3_ | **Lógica de negocio**: cálculo de IGV/totales (Math), validaciones de datos (String), colecciones para inventario y detalle de venta, estructuras de control para las reglas del negocio. |
| _Nombre 4_ | **Setup, interfaz de consola e integración**: configuración inicial del repositorio y del proyecto en IntelliJ (Maven, estructura de carpetas, `.gitignore`), menús por rol (administrador/vendedor), conexión entre vista-modelo-persistencia, pruebas de funcionamiento, y coordinación del README/GitHub/video. |

Todos los integrantes participan también en la revisión cruzada del código y en la sustentación grupal.

## Tecnologías utilizadas

- Java 17
- JDBC
- MySQL (u otro motor relacional)
- IntelliJ IDEA
- Maven

## Estructura del proyecto

```
gestion-tienda-java/
├── .idea/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── lideratec/
│   │   │           ├── modelo/
│   │   │           │   ├── Persona.java
│   │   │           │   ├── Cliente.java
│   │   │           │   ├── Empleado.java
│   │   │           │   ├── Producto.java
│   │   │           │   ├── Venta.java
│   │   │           │   ├── DetalleVenta.java
│   │   │           │   └── Usuario.java
│   │   │           ├── dao/
│   │   │           │   ├── ClienteDAO.java
│   │   │           │   ├── EmpleadoDAO.java
│   │   │           │   ├── ProductoDAO.java
│   │   │           │   ├── VentaDAO.java
│   │   │           │   └── UsuarioDAO.java
│   │   │           ├── bd/
│   │   │           │   └── ConexionBD.java
│   │   │           ├── service/
│   │   │           │   └── VentaService.java
│   │   │           ├── vista/
│   │   │           │   └── MenuPrincipal.java
│   │   │           └── Main.java
│   │   └── resources/
│   │       └── db.properties
│   └── test/
├── sql/
│   └── script_creacion_bd.sql
├── .gitignore
├── pom.xml
└── README.md
```

## Instrucciones de ejecución

1. Clonar el repositorio:
   ```
   git clone <url-del-repositorio>
   ```
2. Crear la base de datos ejecutando `sql/script_creacion_bd.sql` en el motor elegido.
3. Configurar las credenciales de conexión en `src/main/resources/db.properties`.
4. Abrir el proyecto en IntelliJ IDEA como proyecto Maven.
5. Ejecutar `Main.java`.

## Video de exposición

Enlace: _pendiente_