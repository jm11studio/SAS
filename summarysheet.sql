-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Gen 03, 2021 alle 17:21
-- Versione del server: 10.4.8-MariaDB
-- Versione PHP: 7.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `catering`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `summarysheet`
--

CREATE TABLE `summarysheet` (
  `title` text NOT NULL,
  `description` text DEFAULT 'null',
  `public` tinyint(1) DEFAULT 0,
  `MenuID` int(11) NOT NULL DEFAULT -1,
  `owner` int(11) DEFAULT NULL,
  `ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dump dei dati per la tabella `summarysheet`
--

INSERT INTO `summarysheet` (`title`, `description`, `public`, `MenuID`, `owner`, `ID`) VALUES
('pastoLeggero', 'null', 1, 1, 2, 1),
('SH', 'Ciao come va ? ', 1, 1, 2, 2);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `summarysheet`
--
ALTER TABLE `summarysheet`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `title` (`title`) USING HASH;

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `summarysheet`
--
ALTER TABLE `summarysheet`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
