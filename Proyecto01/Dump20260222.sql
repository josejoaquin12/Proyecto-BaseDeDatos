CREATE DATABASE  IF NOT EXISTS `bancodb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bancodb`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: bancodb
-- ------------------------------------------------------
-- Server version	9.5.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ 'e5de4831-c424-11f0-91ef-00e04c686939:1-709';

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nombres` varchar(100) NOT NULL,
  `apellido_paterno` varchar(100) NOT NULL,
  `apellido_materno` varchar(100) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `fecha_registro` datetime NOT NULL,
  `edad` int NOT NULL,
  `id_domicilio` int DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `id_domicilio` (`id_domicilio`),
  CONSTRAINT `clientes_ibfk_1` FOREIGN KEY (`id_domicilio`) REFERENCES `domicilios` (`id_domicilio`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Juan Carlos','Ramírez','López','1998-05-14','$2a$10$1cx.Zp0e3Kbv1hLRVFk62uLnvWTw1ZKiz58DCdMhYKcYe2W7WTVdy','2026-02-21 23:58:56',26,1),(2,'María Fernanda','Gómez','Soto','2000-09-22','$2a$10$rgekx3.h/7yY7GrWVsLWPOGHfUoiqc08ye4oFcIHTmnStbcd9Vc8m','2026-02-21 23:58:56',24,2),(3,'Luis Alberto','Martínez','Cruz','1995-01-10','$2a$10$gV7tjyCPOFUgSzeE3qfk0u2hx6PYq3Fy1KnK63VEDFKvcSIoTk.3W','2026-02-21 23:58:56',30,3),(4,'ad','mi','nis','1995-01-10','$2a$10$i5ohkxk5HqocMw/aWOr9u.Cf5o1So/52PPBTxvNB9hUaQyZ9No0DW','2026-02-21 23:58:56',30,4),(5,'jose','trista','rosales','2006-04-09','$2a$10$G1ka1OSDSK5WfhXwUpjSNuJKdZFwjKJf2554bJBPrsaS/ibWKU1Ru','2026-02-22 03:54:39',19,8),(6,'fds','fsd','sf','2006-12-06','$2a$10$aWGzGRrGTP37H/s0YNW5gen/.qpXXxC/z5o0FG1A5J.kWblGHgQdy','2026-02-22 04:09:33',19,10),(7,'jose','trista','rosales','2006-06-09','$2a$10$d2tZkdgY4fVDKravWVGAxuU5RVs.DQW3U3EfS6pnxQLOB3VBJk3bC','2026-02-22 04:19:26',19,11);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuentas`
--

DROP TABLE IF EXISTS `cuentas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuentas` (
  `id_cuenta` int NOT NULL AUTO_INCREMENT,
  `numero_cuenta` varchar(20) NOT NULL,
  `fecha_apertura` datetime NOT NULL,
  `saldo` decimal(15,2) NOT NULL,
  `estado` enum('ACTIVA','CANCELADA') NOT NULL,
  `id_cliente` int NOT NULL,
  PRIMARY KEY (`id_cuenta`),
  UNIQUE KEY `numero_cuenta` (`numero_cuenta`),
  KEY `id_cliente` (`id_cliente`),
  CONSTRAINT `cuentas_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuentas`
--

LOCK TABLES `cuentas` WRITE;
/*!40000 ALTER TABLE `cuentas` DISABLE KEYS */;
INSERT INTO `cuentas` VALUES (1,'000000001234567890','2026-02-21 23:58:56',15000.00,'ACTIVA',1),(2,'000000009876543210','2026-02-21 23:58:56',8200.50,'ACTIVA',2),(3,'000000004567891234','2026-02-21 23:58:56',23000.00,'ACTIVA',3),(4,'000000001234567891','2026-02-21 23:58:56',5500.75,'ACTIVA',1),(5,'000000001234567892','2026-02-21 23:58:56',98000.00,'ACTIVA',1),(6,'000000009876543211','2026-02-21 23:58:56',1500.00,'ACTIVA',2),(7,'000000009876543212','2026-02-21 23:58:56',34750.30,'ACTIVA',2),(8,'000000004567891235','2026-02-21 23:58:56',8900.00,'ACTIVA',3),(9,'000000004567891236','2026-02-21 23:58:56',120000.00,'ACTIVA',3),(10,'000000009876543454','2026-02-21 23:58:56',300.00,'ACTIVA',4),(11,'000000009876543023','2026-02-21 23:58:56',32950.30,'ACTIVA',4),(12,'000000004567891284','2026-02-21 23:58:56',8900.00,'ACTIVA',4),(13,'000000004567891296','2026-02-21 23:58:56',119820.00,'ACTIVA',4),(14,'000000004567891071','2026-02-22 00:35:39',1200.00,'CANCELADA',4);
/*!40000 ALTER TABLE `cuentas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domicilios`
--

DROP TABLE IF EXISTS `domicilios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `domicilios` (
  `id_domicilio` int NOT NULL AUTO_INCREMENT,
  `calle` varchar(100) NOT NULL,
  `numero` varchar(20) NOT NULL,
  `colonia` varchar(100) NOT NULL,
  `ciudad` varchar(100) NOT NULL,
  `estado` varchar(100) NOT NULL,
  `codigo_postal` varchar(10) NOT NULL,
  PRIMARY KEY (`id_domicilio`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domicilios`
--

LOCK TABLES `domicilios` WRITE;
/*!40000 ALTER TABLE `domicilios` DISABLE KEYS */;
INSERT INTO `domicilios` VALUES (1,'Calle Guerrero','123','Centro','Ciudad Obregón','Sonora','85000'),(2,'Avenida Miguel Alemán','456','Centro','Hermosillo','Sonora','83000'),(3,'Calle 5 de Febrero','789','Reforma','Navojoa','Sonora','85800'),(4,'Calle amberes','314','Reforma','Obregon','Sonora','85800'),(5,'amb','314','bellavista','Obregon','Sonora','85150'),(6,'amb','314','bellavista','Obregon','Sonora','85150'),(7,'si','314','da','dou','lol','85130'),(8,'si','314','da','dou','lol','85130'),(9,'das','54','das','as','ads','574'),(10,'sdf','sdf','sfd','fsd','sdffsd','1850'),(11,'as','18','as','as','as','18060');
/*!40000 ALTER TABLE `domicilios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operaciones`
--

DROP TABLE IF EXISTS `operaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operaciones` (
  `id_transaccion` int NOT NULL AUTO_INCREMENT,
  `tipo_operacion` enum('TRANSFERENCIA','RETIRO_SIN_CUENTA','ALTA_CUENTA') NOT NULL,
  `fecha_hora` datetime NOT NULL,
  `monto` decimal(15,2) NOT NULL,
  `id_cuenta` int NOT NULL,
  PRIMARY KEY (`id_transaccion`),
  KEY `id_cuenta` (`id_cuenta`),
  CONSTRAINT `operaciones_ibfk_1` FOREIGN KEY (`id_cuenta`) REFERENCES `cuentas` (`id_cuenta`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operaciones`
--

LOCK TABLES `operaciones` WRITE;
/*!40000 ALTER TABLE `operaciones` DISABLE KEYS */;
INSERT INTO `operaciones` VALUES (1,'RETIRO_SIN_CUENTA','2026-02-21 23:58:56',3000.00,1),(2,'RETIRO_SIN_CUENTA','2026-02-22 00:42:15',1800.00,11),(3,'RETIRO_SIN_CUENTA','2026-02-22 00:42:30',180.00,14),(4,'RETIRO_SIN_CUENTA','2026-02-22 01:07:18',180.00,13),(5,'RETIRO_SIN_CUENTA','2026-02-22 01:18:47',180.00,10),(6,'RETIRO_SIN_CUENTA','2026-02-22 01:22:49',320.00,10),(7,'RETIRO_SIN_CUENTA','2026-02-22 01:29:17',200.00,10),(8,'RETIRO_SIN_CUENTA','2026-02-22 01:43:09',200.00,10),(9,'TRANSFERENCIA','2026-02-22 02:03:03',300.00,2);
/*!40000 ALTER TABLE `operaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `retirossincuenta`
--

DROP TABLE IF EXISTS `retirossincuenta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `retirossincuenta` (
  `id_transaccion` int NOT NULL AUTO_INCREMENT,
  `folio` varchar(20) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `fecha_expiracion` datetime NOT NULL,
  `estado` enum('PENDIENTE','COBRADO','NO_COBRADO') NOT NULL,
  PRIMARY KEY (`id_transaccion`),
  UNIQUE KEY `folio` (`folio`),
  CONSTRAINT `retirossincuenta_ibfk_1` FOREIGN KEY (`id_transaccion`) REFERENCES `operaciones` (`id_transaccion`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `retirossincuenta`
--

LOCK TABLES `retirossincuenta` WRITE;
/*!40000 ALTER TABLE `retirossincuenta` DISABLE KEYS */;
INSERT INTO `retirossincuenta` VALUES (1,'RS-OBR-2026-001','1234','2026-02-22 00:08:56','NO_COBRADO'),(2,'324453638219702284','0968825279','2026-02-22 00:52:15','COBRADO'),(3,'339589338338761217','5937849318','2026-02-22 00:52:30','NO_COBRADO'),(4,'767245496485116952','3148359374','2026-02-22 01:17:18','COBRADO'),(5,'151312228566212418','9437086842','2026-02-22 01:28:47','COBRADO'),(6,'207287569528900595','1143893216','2026-02-22 01:32:49','COBRADO'),(7,'438273637206709304','6029127941','2026-02-22 01:39:17','COBRADO'),(8,'285587956595322507','2078486779','2026-02-22 01:53:09','COBRADO');
/*!40000 ALTER TABLE `retirossincuenta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transferencias`
--

DROP TABLE IF EXISTS `transferencias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transferencias` (
  `id_transaccion` int NOT NULL AUTO_INCREMENT,
  `id_cuenta_destino` int NOT NULL,
  PRIMARY KEY (`id_transaccion`),
  KEY `id_cuenta_destino` (`id_cuenta_destino`),
  CONSTRAINT `transferencias_ibfk_1` FOREIGN KEY (`id_transaccion`) REFERENCES `operaciones` (`id_transaccion`),
  CONSTRAINT `transferencias_ibfk_2` FOREIGN KEY (`id_cuenta_destino`) REFERENCES `cuentas` (`id_cuenta`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transferencias`
--

LOCK TABLES `transferencias` WRITE;
/*!40000 ALTER TABLE `transferencias` DISABLE KEYS */;
INSERT INTO `transferencias` VALUES (9,4);
/*!40000 ALTER TABLE `transferencias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'bancodb'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `ev_expirar_retiros_sin_cuenta` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `ev_expirar_retiros_sin_cuenta` ON SCHEDULE EVERY 4 MINUTE STARTS '2026-02-22 00:07:23' ON COMPLETION NOT PRESERVE ENABLE DO CALL ExpirarRetirosSinCuenta() */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'bancodb'
--
/*!50003 DROP PROCEDURE IF EXISTS `CobrarRetiroSinCuenta` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CobrarRetiroSinCuenta`(
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
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ExpirarRetirosSinCuenta` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ExpirarRetirosSinCuenta`()
BEGIN
    UPDATE RetirosSinCuenta
    SET estado = 'NO_COBRADO'
    WHERE estado = 'PENDIENTE'
      AND fecha_expiracion <= NOW();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-22  4:51:19
