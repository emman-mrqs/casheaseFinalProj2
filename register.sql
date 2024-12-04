-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 04, 2024 at 02:30 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `register`
--

-- --------------------------------------------------------

--
-- Table structure for table `accountdetails`
--

CREATE TABLE `accountdetails` (
  `accUsername` varchar(251) NOT NULL,
  `accPassword` varchar(251) NOT NULL,
  `accEmail` varchar(251) NOT NULL,
  `accContact` varchar(251) NOT NULL,
  `accNumber` varchar(20) NOT NULL,
  `balance` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `accountdetails`
--

INSERT INTO `accountdetails` (`accUsername`, `accPassword`, `accEmail`, `accContact`, `accNumber`, `balance`) VALUES
('data', 'datastruc', 'data@gmail.com', '09691310005', 'ACC1733274454790', 2800.00),
('Vincent', 'vincent', 'vincent@gmail.com', '09236454772', 'ACC1733275262421', 500.00);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `accNumber` varchar(20) NOT NULL,
  `transaction_type` varchar(10) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `balance` double DEFAULT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`id`, `accNumber`, `transaction_type`, `amount`, `balance`, `transaction_date`) VALUES
(21, 'ACC1733274454790', 'Deposit', 1000.00, 1000, '2024-12-04 01:07:52'),
(22, 'ACC1733274454790', 'Deposit', 2300.00, 3300, '2024-12-04 01:07:59'),
(23, 'ACC1733274454790', 'Withdrawal', 500.00, 2800, '2024-12-04 01:08:05'),
(24, 'ACC1733274454790', 'Deposit', 5000.00, 7800, '2024-12-04 01:13:45'),
(25, 'ACC1733274454790', 'Withdrawal', 5000.00, 2800, '2024-12-04 01:14:22'),
(26, 'ACC1733275262421', 'Deposit', 1000.00, 1000, '2024-12-04 01:25:04'),
(27, 'ACC1733275262421', 'Withdrawal', 500.00, 500, '2024-12-04 01:25:55');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accountdetails`
--
ALTER TABLE `accountdetails`
  ADD PRIMARY KEY (`accUsername`),
  ADD UNIQUE KEY `accNumber` (`accNumber`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
