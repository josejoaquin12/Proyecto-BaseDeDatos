CREATE DATABASE BancoDB;
USE BancoDB;
CREATE TABLE Domicilios (
 id_domicilio INT PRIMARY KEY AUTO_INCREMENT,
 calle VARCHAR(100) NOT NULL,
 numero VARCHAR(20) NOT NULL,
 colonia VARCHAR(100) NOT NULL,
 ciudad VARCHAR(100) NOT NULL,
 estado VARCHAR(100) NOT NULL,
 codigo_postal VARCHAR(10) NOT NULL
);
CREATE TABLE Clientes (
 id_cliente INT PRIMARY KEY AUTO_INCREMENT,
 nombres VARCHAR(100) NOT NULL,
 apellido_paterno VARCHAR(100) NOT NULL,
 apellido_materno VARCHAR(100) NOT NULL,
 fecha_nacimiento DATE NOT NULL,
 contrasena VARCHAR(255) NOT NULL,
 fecha_registro DATETIME NOT NULL,
 edad INT NOT NULL,
 id_domicilio INT UNIQUE,
 FOREIGN KEY (id_domicilio) REFERENCES Domicilios(id_domicilio)
);
CREATE TABLE Cuentas (
 id_cuenta INT PRIMARY KEY AUTO_INCREMENT,
 numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
 fecha_apertura DATETIME NOT NULL,
 saldo DECIMAL(15,2) NOT NULL,
 estado ENUM('ACTIVA','CANCELADA') NOT NULL,
 id_cliente INT NOT NULL,
 FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente)
);
CREATE TABLE Operaciones (
 id_transaccion INT PRIMARY KEY AUTO_INCREMENT,
 tipo_operacion ENUM('TRANSFERENCIA','RETIRO_SIN_CUENTA','ALTA_CUENTA') NOT NULL,
 fecha_hora DATETIME NOT NULL,
 monto DECIMAL(15,2) NOT NULL,
 id_cuenta INT NOT NULL,
 FOREIGN KEY (id_cuenta) REFERENCES Cuentas(id_cuenta)
);
CREATE TABLE Transferencias (
 id_transaccion INT PRIMARY KEY AUTO_INCREMENT,
 id_cuenta_destino INT NOT NULL,
 FOREIGN KEY (id_transaccion) REFERENCES Operaciones(id_transaccion),
 FOREIGN KEY (id_cuenta_destino) REFERENCES Cuentas(id_cuenta)
);

CREATE TABLE RetirosSinCuenta (
 id_transaccion INT PRIMARY KEY AUTO_INCREMENT,
 folio VARCHAR(20) UNIQUE NOT NULL,
 contrasena VARCHAR(255) NOT NULL,
 fecha_expiracion DATETIME NOT NULL,
 estado ENUM('PENDIENTE','COBRADO','NO_COBRADO') NOT NULL,
 FOREIGN KEY (id_transaccion) REFERENCES Operaciones(id_transaccion)
);

DELIMITER $$

CREATE PROCEDURE CobrarRetiroSinCuenta(
    IN p_folio VARCHAR(20)
)
BEGIN
    DECLARE v_monto DECIMAL(15,2);
    DECLARE v_idCuenta INT;

    START TRANSACTION;

    -- Obtener monto y cuenta origen
    SELECT o.monto, o.id_cuenta
    INTO v_monto, v_idCuenta
    FROM RetirosSinCuenta r
    JOIN Operaciones o ON r.id_transaccion = o.id_transaccion
    WHERE r.folio = p_folio
      AND r.estado = 'PENDIENTE'
    FOR UPDATE;

    -- Descontar saldo
    UPDATE Cuentas
    SET saldo = saldo - v_monto
    WHERE id_cuenta = v_idCuenta;

    -- Marcar retiro como cobrado
    UPDATE RetirosSinCuenta
    SET estado = 'COBRADO'
    WHERE folio = p_folio;

    COMMIT;
END$$

DELIMITER ;