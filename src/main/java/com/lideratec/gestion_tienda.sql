-- =====================================================================
-- StoreManager - Sistema de Gestion de Ventas e Inventario
-- Script de creacion de base de datos (MySQL 8)
-- =====================================================================

DROP DATABASE IF EXISTS gestion_tienda;
CREATE DATABASE gestion_tienda CHARACTER SET utf8mb4;
USE gestion_tienda;

-- ---------------------------------------------------------------------
-- Tabla: usuarios  (login del sistema)
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    usuario      VARCHAR(30)  NOT NULL UNIQUE,
    contrasena   VARCHAR(100) NOT NULL,
    tipo_acceso  VARCHAR(20)  NOT NULL
);

-- ---------------------------------------------------------------------
-- Tabla: clientes
-- ---------------------------------------------------------------------
CREATE TABLE clientes (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    dni       VARCHAR(8)  NOT NULL UNIQUE,
    nombre    VARCHAR(80) NOT NULL,
    celular   VARCHAR(20),
    correo    VARCHAR(80)
);

-- ---------------------------------------------------------------------
-- Tabla: empleados
-- ---------------------------------------------------------------------
CREATE TABLE empleados (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    dni       VARCHAR(8)  NOT NULL UNIQUE,
    nombre    VARCHAR(80) NOT NULL,
    celular   VARCHAR(20),
    correo    VARCHAR(80),
    cargo     VARCHAR(50),
    sueldo    DECIMAL(10,2) NOT NULL DEFAULT 0
);

-- ---------------------------------------------------------------------
-- Tabla: productos
-- ---------------------------------------------------------------------
CREATE TABLE productos (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    codigo     VARCHAR(20) NOT NULL UNIQUE,
    nombre     VARCHAR(80) NOT NULL,
    categoria  VARCHAR(40),
    precio     DECIMAL(10,2) NOT NULL DEFAULT 0,
    stock      INT NOT NULL DEFAULT 0
);

-- ---------------------------------------------------------------------
-- Tabla: ventas
-- ---------------------------------------------------------------------
CREATE TABLE ventas (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    fecha        DATE NOT NULL,
    id_cliente   INT NOT NULL,
    id_empleado  INT NOT NULL,
    subtotal     DECIMAL(10,2) NOT NULL,
    igv          DECIMAL(10,2) NOT NULL,
    total        DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_venta_cliente  FOREIGN KEY (id_cliente)  REFERENCES clientes(id),
    CONSTRAINT fk_venta_empleado FOREIGN KEY (id_empleado) REFERENCES empleados(id)
);

-- ---------------------------------------------------------------------
-- Tabla: detalle_venta
-- ---------------------------------------------------------------------
CREATE TABLE detalle_venta (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    id_venta    INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad    INT NOT NULL,
    precio      DECIMAL(10,2) NOT NULL,
    subtotal    DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_venta    FOREIGN KEY (id_venta)    REFERENCES ventas(id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto) REFERENCES productos(id)
);

-- ---------------------------------------------------------------------
-- Datos iniciales de prueba (opcional, util para probar ConexionBD)
-- ---------------------------------------------------------------------
INSERT INTO usuarios (usuario, contrasena, tipo_acceso) VALUES
('admin', 'admin123', 'ADMINISTRADOR'),
('vendedor1', 'venta123', 'VENDEDOR');

INSERT INTO clientes (dni, nombre, celular, correo) VALUES
('12345678', 'Juan Perez', '987654321', 'juan.perez@correo.com');

INSERT INTO empleados (dni, nombre, celular, correo, cargo, sueldo) VALUES
('87654321', 'Maria Lopez', '912345678', 'maria.lopez@correo.com', 'Vendedor', 1500.00);

INSERT INTO productos (codigo, nombre, categoria, precio, stock) VALUES
('P001', 'Arroz 5kg', 'Abarrotes', 18.50, 50),
('P002', 'Aceite 1L', 'Abarrotes', 9.90, 30);
