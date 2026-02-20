USE BancoDB;
INSERT INTO domicilios
(calle, numero, colonia, ciudad, estado, codigo_postal)
VALUES
('Calle Falsa', '123', 'Centro', 'Ciudad Obregón', 'Sonora', '85000');

INSERT INTO domicilios
(calle, numero, colonia, ciudad, estado, codigo_postal)
VALUES
('Calle itson', '101', 'Villa ITSON', 'Ciudad Obregón', 'Sonora', '85111');

INSERT INTO clientes
(nombres, apellido_paterno, apellido_materno, fecha_nacimiento, contrasena, fecha_registro, edad, id_domicilio)
VALUES
('Jose', 'Trista', 'Rosales','2003-05-15','1234',NOW(),22,1);

INSERT INTO clientes
(nombres, apellido_paterno, apellido_materno, fecha_nacimiento, contrasena, fecha_registro, edad, id_domicilio)
VALUES
('Gibran', 'Duran', 'Solano', '1990-01-01', 'BDA123', NOW(), 36,2  );

INSERT INTO cuentas
(numero_cuenta, fecha_apertura, saldo, estado, id_cliente)
VALUES
('1234567890', NOW(), 50000.00, 'ACTIVA', 1);

INSERT INTO cuentas
(numero_cuenta, fecha_apertura, saldo, estado, id_cliente)
VALUES
('9876543210', NOW(), 0.00, 'ACTIVA', 1);

INSERT INTO cuentas
(numero_cuenta, fecha_apertura, saldo, estado, id_cliente)
VALUES
('102938475612345678', NOW(), 300000.00, 'ACTIVA', 2transferencias);