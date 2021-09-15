-- TABLA PARA IMAGENES
CREATE TABLE img_perfiles (
  foto longblob DEFAULT NULL,
  codigo_Empleado int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sistemanetu
-- ------------------------------------------------------
-- Server version	8.0.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `administrador`
--

DROP TABLE IF EXISTS `administrador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrador` (
  `id_Empleado` int DEFAULT NULL,
  KEY `administrador_FK` (`id_Empleado`),
  CONSTRAINT `administrador_FK` FOREIGN KEY (`id_Empleado`) REFERENCES `empleado` (`codigo_Empleado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrador`
--

LOCK TABLES `administrador` WRITE;
/*!40000 ALTER TABLE `administrador` DISABLE KEYS */;
INSERT INTO `administrador` VALUES (987);
/*!40000 ALTER TABLE `administrador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `codigo_Empleado_1` int NOT NULL,
  `codigo_Empleado_2` int NOT NULL,
  `abierto` tinyint(1) DEFAULT '0',
  KEY `codigo_Empleado_1` (`codigo_Empleado_1`),
  KEY `codigo_Empleado_2` (`codigo_Empleado_2`),
  CONSTRAINT `Empleado1MensajeFK` FOREIGN KEY (`codigo_Empleado_1`) REFERENCES `empleado` (`codigo_Empleado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Empleado2MensajeFK` FOREIGN KEY (`codigo_Empleado_2`) REFERENCES `empleado` (`codigo_Empleado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dependencia`
--

DROP TABLE IF EXISTS `dependencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dependencia` (
  `id_Dependencia` int NOT NULL,
  `nombre_Dependencia` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_Dependencia`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre_Dependencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dependencia`
--

LOCK TABLES `dependencia` WRITE;
/*!40000 ALTER TABLE `dependencia` DISABLE KEYS */;
INSERT INTO `dependencia` VALUES (5,'DEPARTAMENTOS'),(4,'ESCUELAS'),(2,'FACULTADES'),(3,'INSTITUTOS'),(1,'VICERRECTORÃ�A');
/*!40000 ALTER TABLE `dependencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleado`
--

DROP TABLE IF EXISTS `empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleado` (
  `codigo_Empleado` int NOT NULL,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `correo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sexo` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `dependencia` int NOT NULL,
  `subdependencia` int NOT NULL,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`codigo_Empleado`),
  KEY `dependencia` (`dependencia`),
  KEY `subdependencia` (`subdependencia`),
  CONSTRAINT `empleado_ibfk_1` FOREIGN KEY (`dependencia`) REFERENCES `dependencia` (`id_Dependencia`),
  CONSTRAINT `empleado_ibfk_2` FOREIGN KEY (`subdependencia`) REFERENCES `subdependencia` (`id_Subdependencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES (11,'empleado','aa@gmail.com','Femenino',5,38,''),(987,'Camila','cami@gmail.com','Masculino',4,14,'Hola soy cami y tengo un mac');
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login` (
  `codigo_Empleado` int NOT NULL,
  `contraseÃ±a` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`codigo_Empleado`),
  KEY `codigo_Empleado` (`codigo_Empleado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` VALUES (11,'7dGloV9JR8',0),(987,'NCVDgEoYL4',0);
/*!40000 ALTER TABLE `login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mensaje`
--

DROP TABLE IF EXISTS `mensaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mensaje` (
  `codigo_Empleado_1` int NOT NULL,
  `codigo_Empleado_2` int NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `contenido` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  KEY `codigo_Empleado_1` (`codigo_Empleado_1`),
  KEY `codigo_Empleado_2` (`codigo_Empleado_2`),
  CONSTRAINT `empleado1FK` FOREIGN KEY (`codigo_Empleado_1`) REFERENCES `empleado` (`codigo_Empleado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `empleado2FK` FOREIGN KEY (`codigo_Empleado_2`) REFERENCES `empleado` (`codigo_Empleado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mensaje`
--

LOCK TABLES `mensaje` WRITE;
/*!40000 ALTER TABLE `mensaje` DISABLE KEYS */;
/*!40000 ALTER TABLE `mensaje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificacionmensaje`
--

DROP TABLE IF EXISTS `notificacionmensaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacionmensaje` (
  `codigo_Empleado_1` int NOT NULL,
  `codigo_Empleado_2` int NOT NULL,
  KEY `codigo_Empleado_1` (`codigo_Empleado_1`),
  KEY `codigo_Empleado_2` (`codigo_Empleado_2`),
  CONSTRAINT `Empleado1NotificacionFK` FOREIGN KEY (`codigo_Empleado_1`) REFERENCES `empleado` (`codigo_Empleado`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Empleado2NotificacionFK` FOREIGN KEY (`codigo_Empleado_2`) REFERENCES `empleado` (`codigo_Empleado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificacionmensaje`
--

LOCK TABLES `notificacionmensaje` WRITE;
/*!40000 ALTER TABLE `notificacionmensaje` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificacionmensaje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publicacion`
--

DROP TABLE IF EXISTS `publicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publicacion` (
  `id_Publicacion` int NOT NULL AUTO_INCREMENT,
  `codigo_Empleado` int NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `contenido` text COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_Publicacion`),
  KEY `publicacion_FK` (`codigo_Empleado`),
  CONSTRAINT `publicacion_FK` FOREIGN KEY (`codigo_Empleado`) REFERENCES `empleado` (`codigo_Empleado`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publicacion`
--

LOCK TABLES `publicacion` WRITE;
/*!40000 ALTER TABLE `publicacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `publicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subdependencia`
--

DROP TABLE IF EXISTS `subdependencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subdependencia` (
  `id_Subdependencia` int NOT NULL,
  `nombre_Subdependencia` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id_Dependencia` int DEFAULT NULL,
  PRIMARY KEY (`id_Subdependencia`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre_Subdependencia`),
  KEY `DependenciaSubdependenciaFK` (`id_Dependencia`),
  CONSTRAINT `DependenciaSubdependenciaFK` FOREIGN KEY (`id_Dependencia`) REFERENCES `dependencia` (`id_Dependencia`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subdependencia`
--

LOCK TABLES `subdependencia` WRITE;
/*!40000 ALTER TABLE `subdependencia` DISABLE KEYS */;
INSERT INTO `subdependencia` VALUES (1,'ACADÃ‰MICA',1),(2,'ADMINISTRATIVA',1),(3,'BIENESTAR UNIVERSITARIO',1),(4,'INVESTIGACIONES',1),(5,'ARTES INTEGRADAS',2),(6,'CIENCIAS NATURALES Y EXACTAS',2),(7,'CIENCIAS DE LA ADMINISTRACIÃ“N',2),(8,'SALUD',2),(9,'CIENCIAS SOCIALES Y ECONÃ“MICAS',2),(10,'HUMANIDADES',2),(11,'INGENIERÃ�A',2),(12,'EDUCACIÃ“N Y PEDAGOGÃ�A',3),(13,'PSICOLOGÃ�A',3),(14,'ARQUITECTURA',4),(15,'BACTEROLIOGÃ�A Y LABORATORIO CLÃ�NICO',4),(16,'CIENCIAS BÃ�SICAS',4),(17,'CIENCIAS DEL LENGUAJE',4),(18,'COMUNICACIÃ“N SOCIAL',4),(19,'ENFERMERÃ�A',4),(20,'ESTADÃ�STICA',4),(21,'ESTUDIOS LITERARIOS',4),(22,'INGENIERÃ�A CIVIL Y GEOMÃ�TICA',4),(23,'INGENIERÃ�A DE ALIMENTOS',4),(24,'INGENIERÃ�A DE MATERIALES',4),(25,'INGENIERÃ�A DE RECURSOS NATURALES Y DEL AMBIENTE',4),(26,'INGENIERÃ�A DE SISTEMAS Y COMPUTACIÃ“N',4),(27,'INGENIERÃ�A ELÃ‰CTRICA Y ELECTRÃ“NICA',4),(28,'INGENIERÃ�A INDUSTRIAL',4),(29,'INGENIERÃ�A MECÃ�NICA',4),(30,'INGENIERÃ�A QUÃ�MICA',4),(31,'MEDICINA',4),(32,'MÃšSICA',4),(33,'ODONTOLOGÃ�A',4),(34,'REHABILITACIÃ“N HUMANA',4),(35,'SALUD PÃšBLICA',4),(36,'ARTES ESCÃ‰NICAS',5),(37,'ARTES VISUALES Y ESTÃ‰TICA',5),(38,'BIOLOGÃ�A',5),(39,'CIENCIAS SOCIALES',5),(40,'DISEÃ‘O',5),(41,'DIRECCIÃ“N Y GESTIÃ“N ADMINISTRATIVA',5),(42,'ECONOMÃ�A',5),(43,'FILOSOFÃ�A',5),(44,'FÃ�SICA',5),(45,'GEOGRAFÃ�A',5),(46,'HISTORIA',5),(47,'MATEMÃ�TICAS',5),(48,'MEDICINA INTERNA',5),(49,'PROCESOS, INFORMACIÃ“N, CONTABILIDAD Y FINANZAS',5),(50,'QUÃ�MICA',5);
/*!40000 ALTER TABLE `subdependencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'sistemanetu'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-09-04  0:08:23
