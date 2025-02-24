-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-02-2025 a las 16:57:11
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `seguros`
--
CREATE DATABASE IF NOT EXISTS `seguros` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `seguros`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asegurado`
--

CREATE TABLE `asegurado` (
  `id` int(11) NOT NULL,
  `DNI` varchar(9) NOT NULL,
  `nombre` varchar(20) DEFAULT NULL,
  `apellido1` varchar(50) DEFAULT NULL,
  `apellido2` varchar(50) DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asegurado`
--

INSERT INTO `asegurado` (`id`, `DNI`, `nombre`, `apellido1`, `apellido2`, `fechaNacimiento`) VALUES
(1, '74341990X', 'FERNANDO', 'PEREZ', 'FERNANDEZ', '1958-07-12'),
(2, '75224694C', 'JOSE', 'MARTINEZ', 'GOMEZ', '1969-12-31'),
(3, '42842936P', 'JUAN', 'RUIZ', 'ALONSO', '1980-01-06'),
(4, '18864318V', 'LUIS', 'HURTADO', 'NADAL', '1979-06-20'),
(5, '40041016V', 'MARIA', 'CALVO', 'TORRES', '1947-05-21'),
(6, '75437765L', 'RAMON', 'JIMENEZ', 'CASTILLO', '1964-08-25'),
(7, '70861873T', 'AARON', 'HERRERO', 'CABALLERO', '1971-10-12'),
(8, '53262182R', 'ABEL', 'DELGADO', 'SERRANO', '1966-12-11'),
(9, '18863017G', 'RAQUEL', 'SALVADOR', 'CATALAN', '1988-01-26'),
(10, '28037916L', 'SANTIAGO', 'FERNANDEZ', 'GUILLEN', '1953-11-25');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_poliza`
--

CREATE TABLE `detalle_poliza` (
  `referencia` varchar(9) NOT NULL,
  `digitoControl` int(11) NOT NULL,
  `fechaAlta` date DEFAULT NULL,
  `fechaVencimiento` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `linea`
--

CREATE TABLE `linea` (
  `codigo` int(11) NOT NULL,
  `descriptivo` varchar(100) DEFAULT NULL,
  `familia` varchar(1) DEFAULT NULL,
  `fechaLimiteContratacion` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `linea`
--

INSERT INTO `linea` (`codigo`, `descriptivo`, `familia`, `fechaLimiteContratacion`) VALUES
(300, 'SEGURO PARA EXPLOTACIONES FRUT�COLAS', 'A', '2025-07-31'),
(301, 'SEGURO EXPLOTACIONES CITR�COLAS', 'A', '2025-03-31'),
(302, 'SEGURO EXPLOTACIONES HORT�COLAS EN CANARIAS', 'A', '2025-01-01'),
(401, 'SEGURO EXPLOTACI�N GANADO VACUNO REPRODUCTOR Y RECR�A', 'G', '2025-12-31'),
(407, 'SEGURO EXPLOTACI�N AVIAR DE PUESTA', 'G', '2025-06-30');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `poliza`
--

CREATE TABLE `poliza` (
  `referencia` varchar(9) NOT NULL,
  `digitoControl` int(11) NOT NULL,
  `asegurado` int(11) NOT NULL,
  `linea` int(11) NOT NULL,
  `importe` decimal(13,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subvencion`
--

CREATE TABLE `subvencion` (
  `asegurado` int(11) NOT NULL,
  `linea` int(11) NOT NULL,
  `importe` decimal(3,0) DEFAULT NULL,
  `asunto` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `subvencion`
--

INSERT INTO `subvencion` (`asegurado`, `linea`, `importe`, `asunto`) VALUES
(5, 401, 10, 'SUBVENCI�N AGRICULTORES MAYORES DE 65 A�OS'),
(9, 301, 15, 'SUBVENCI�N J�VENES AGRICULTORES');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asegurado`
--
ALTER TABLE `asegurado`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `detalle_poliza`
--
ALTER TABLE `detalle_poliza`
  ADD PRIMARY KEY (`referencia`);

--
-- Indices de la tabla `linea`
--
ALTER TABLE `linea`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `poliza`
--
ALTER TABLE `poliza`
  ADD PRIMARY KEY (`referencia`),
  ADD KEY `asegurado` (`asegurado`),
  ADD KEY `linea` (`linea`);

--
-- Indices de la tabla `subvencion`
--
ALTER TABLE `subvencion`
  ADD PRIMARY KEY (`asegurado`,`linea`),
  ADD KEY `linea` (`linea`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asegurado`
--
ALTER TABLE `asegurado`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detalle_poliza`
--
ALTER TABLE `detalle_poliza`
  ADD CONSTRAINT `detalle_poliza_ibfk_1` FOREIGN KEY (`referencia`) REFERENCES `poliza` (`referencia`);

--
-- Filtros para la tabla `poliza`
--
ALTER TABLE `poliza`
  ADD CONSTRAINT `poliza_ibfk_1` FOREIGN KEY (`asegurado`) REFERENCES `asegurado` (`id`),
  ADD CONSTRAINT `poliza_ibfk_2` FOREIGN KEY (`linea`) REFERENCES `linea` (`codigo`);

--
-- Filtros para la tabla `subvencion`
--
ALTER TABLE `subvencion`
  ADD CONSTRAINT `subvencion_ibfk_1` FOREIGN KEY (`asegurado`) REFERENCES `asegurado` (`id`),
  ADD CONSTRAINT `subvencion_ibfk_2` FOREIGN KEY (`linea`) REFERENCES `linea` (`codigo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
