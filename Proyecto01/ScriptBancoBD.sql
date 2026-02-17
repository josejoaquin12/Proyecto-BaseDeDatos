CREATE DATABASE BancoDB;
USE BancoDB;
CREATE TABLE Domicilio (
 id_domicilio INT PRIMARY KEY AUTO_INCREMENT,
 calle VARCHAR(100) NOT NULL,
 numero VARCHAR(20) NOT NULL,
 colonia VARCHAR(100) NOT NULL,
 ciudad VARCHAR(100) NOT NULL,
 estado VARCHAR(100) NOT NULL,
 codigo_postal VARCHAR(10) NOT NULL
);
CREATE TABLE Cliente (
 id_cliente INT PRIMARY KEY AUTO_INCREMENT,
 nombres VARCHAR(100) NOT NULL,
 apellido_paterno VARCHAR(100) NOT NULL,
 apellido_materno VARCHAR(100) NOT NULL,
 fecha_nacimiento DATE NOT NULL,
 contrasena VARCHAR(255) NOT NULL,
 fecha_registro DATETIME NOT NULL,
 edad INT NOT NULL,
 id_domicilio INT UNIQUE,
 FOREIGN KEY (id_domicilio) REFERENCES Domicilio(id_domicilio)
);
CREATE TABLE Cuenta (
 id_cuenta INT PRIMARY KEY AUTO_INCREMENT,
 numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
 fecha_apertura DATETIME NOT NULL,
 saldo DECIMAL(15,2) NOT NULL,
 estado ENUM('ACTIVA','CANCELADA') NOT NULL,
 id_cliente INT NOT NULL,
 FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);
CREATE TABLE Operacion (
 id_transaccion INT PRIMARY KEY AUTO_INCREMENT,
 tipo_operacion ENUM('TRANSFERENCIA','RETIRO_SIN_CUENTA','ALTA_CUENTA') NOT NULL,
 fecha_hora DATETIME NOT NULL,
 monto DECIMAL(15,2) NOT NULL,
 id_cuenta INT NOT NULL,
 FOREIGN KEY (id_cuenta) REFERENCES Cuenta(id_cuenta)
);
CREATE TABLE Transferencia (
 id_transaccion INT PRIMARY KEY AUTO_INCREMENT,
 id_cuenta_destino INT NOT NULL,
 FOREIGN KEY (id_transaccion) REFERENCES Operacion(id_transaccion),
 FOREIGN KEY (id_cuenta_destino) REFERENCES Cuenta(id_cuenta)
);
CREATE TABLE RetiroSinCuenta (
 id_transaccion INT PRIMARY KEY AUTO_INCREMENT,
 folio VARCHAR(20) UNIQUE NOT NULL,
 contrasena VARCHAR(255) NOT NULL,
 fecha_expiracion DATETIME NOT NULL,
 estado ENUM('PENDIENTE','COBRADO','NO_COBRADO') NOT NULL,
 FOREIGN KEY (id_transaccion) REFERENCES Operacion(id_transaccion)
);