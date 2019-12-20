-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 30, 2019 at 06:42 PM
-- Server version: 5.7.28-0ubuntu0.16.04.2
-- PHP Version: 7.0.33-0ubuntu0.16.04.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `BidMe`
--

-- --------------------------------------------------------

--
-- Table structure for table `add_product`
--

CREATE TABLE `add_product` (
  `image` varchar(255) NOT NULL DEFAULT './upload/image.jpeg',
  `category` varchar(255) NOT NULL DEFAULT '',
  `title` varchar(255) NOT NULL DEFAULT '',
  `price` varchar(255) NOT NULL DEFAULT '',
  `created_at` varchar(255) NOT NULL DEFAULT '',
  `add_pro_id` varchar(255) NOT NULL DEFAULT '',
  `status` int(11) NOT NULL DEFAULT '1',
  `description` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT '1',
  `created_at` varchar(255) NOT NULL,
  `updated_at` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`name`, `email`, `password`, `status`, `created_at`, `updated_at`) VALUES
('Admin', 'admin@gmail.com', '123456', '1', '1519209394', '1519209394');

-- --------------------------------------------------------

--
-- Table structure for table `admin_notification`
--

CREATE TABLE `admin_notification` (
  `id` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `created_at` varchar(255) DEFAULT NULL,
  `updated_on` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `description` longtext
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `auction_cat`
--

CREATE TABLE `auction_cat` (
  `cat_id` varchar(255) NOT NULL DEFAULT '',
  `cat_title` varchar(255) NOT NULL DEFAULT '',
  `status` varchar(255) NOT NULL DEFAULT '1' COMMENT '0 False,1 True',
  `image` varchar(255) NOT NULL DEFAULT '',
  `updated_at` varchar(255) NOT NULL DEFAULT '',
  `created_at` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `auction_pro`
--

CREATE TABLE `auction_pro` (
  `id` int(11) NOT NULL,
  `pro_pub_id` varchar(255) NOT NULL DEFAULT '',
  `user_pub_id` varchar(255) NOT NULL DEFAULT 'dnhx4izfk0x1jwedb',
  `title` varchar(255) NOT NULL DEFAULT '',
  `brand_id` varchar(255) NOT NULL DEFAULT '',
  `model_id` varchar(255) NOT NULL DEFAULT '',
  `image` varchar(255) NOT NULL DEFAULT '',
  `cat_id` varchar(255) NOT NULL DEFAULT '',
  `sub_cat_id` varchar(255) NOT NULL DEFAULT '',
  `price` varchar(255) NOT NULL DEFAULT '',
  `address` varchar(255) NOT NULL DEFAULT '',
  `latitude` varchar(255) NOT NULL DEFAULT '22.7441973',
  `longitude` varchar(255) NOT NULL DEFAULT '75.8940479',
  `description` longtext NOT NULL,
  `sort_description` varchar(255) NOT NULL DEFAULT '',
  `s_date` varchar(255) NOT NULL DEFAULT '',
  `e_date` varchar(255) NOT NULL DEFAULT '',
  `no_of_owner` varchar(255) NOT NULL DEFAULT '1',
  `insured` varchar(255) NOT NULL DEFAULT 'YES',
  `created_at` varchar(255) NOT NULL DEFAULT '',
  `updated_at` varchar(255) NOT NULL DEFAULT '',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0.Active,1.Deactive,2.Delete'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `auction_pro_img`
--

CREATE TABLE `auction_pro_img` (
  `id` int(11) NOT NULL,
  `pro_pub_id` varchar(255) NOT NULL DEFAULT '',
  `image` varchar(255) NOT NULL DEFAULT '',
  `created_at` varchar(255) NOT NULL DEFAULT '',
  `updated_at` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `auction_sub_cat`
--

CREATE TABLE `auction_sub_cat` (
  `cat_id` varchar(255) NOT NULL DEFAULT '',
  `sub_cat_id` varchar(255) NOT NULL DEFAULT '',
  `sub_cat_title` varchar(255) NOT NULL DEFAULT '',
  `image` varchar(255) NOT NULL DEFAULT '',
  `status` int(11) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bid_information`
--

CREATE TABLE `bid_information` (
  `bid_pub_id` varchar(255) DEFAULT '',
  `pro_pub_id` varchar(255) DEFAULT '',
  `user_pub_id` varchar(255) DEFAULT '',
  `price` varchar(255) DEFAULT '',
  `created_at` varchar(255) DEFAULT '',
  `updated_at` varchar(255) DEFAULT '',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '0.False,1.True,2.deleted',
  `isWin` int(11) NOT NULL DEFAULT '0' COMMENT '0.Not Win ,1.Won'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `block_user_bid`
--

CREATE TABLE `block_user_bid` (
  `id` int(11) NOT NULL,
  `user_pub_id` varchar(255) NOT NULL,
  `block_user_pub_id` varchar(255) NOT NULL,
  `pro_pub_id` varchar(255) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '1.block,0.unblock'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `brand`
--

CREATE TABLE `brand` (
  `brand_id` varchar(255) NOT NULL DEFAULT '',
  `cat_id` varchar(255) NOT NULL DEFAULT '',
  `sub_cat_id` varchar(255) NOT NULL DEFAULT '',
  `brand_name` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `chat`
--

CREATE TABLE `chat` (
  `id` int(11) NOT NULL,
  `chat_id` varchar(255) NOT NULL DEFAULT '',
  `sender_user_pub_id` varchar(255) NOT NULL DEFAULT '',
  `receiver_user_pub_id` varchar(255) NOT NULL DEFAULT '',
  `message` text NOT NULL,
  `date` varchar(255) NOT NULL DEFAULT '',
  `thread_id` varchar(255) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '0.block,1,active'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `currency_setting`
--

CREATE TABLE `currency_setting` (
  `curr_pub_id` varchar(255) NOT NULL,
  `currency_type` varchar(255) NOT NULL,
  `currency_code` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT '1',
  `created_at` varchar(255) NOT NULL,
  `updated_at` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `favourite`
--

CREATE TABLE `favourite` (
  `id` int(11) NOT NULL,
  `pro_pub_id` varchar(255) NOT NULL,
  `user_pub_id` varchar(255) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  `isFavourite` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `model`
--

CREATE TABLE `model` (
  `model_id` varchar(255) NOT NULL DEFAULT '',
  `cat_id` varchar(255) NOT NULL DEFAULT '',
  `sub_cat_id` varchar(255) NOT NULL DEFAULT '',
  `brand_id` varchar(255) NOT NULL DEFAULT '',
  `model_name` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `notication_id` varchar(255) NOT NULL,
  `user_pub_id` varchar(255) NOT NULL,
  `tittle` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  `updated_at` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `pro_pub_id` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `rating`
--

CREATE TABLE `rating` (
  `rating_id` varchar(255) NOT NULL DEFAULT '',
  `pro_pub_id` varchar(255) NOT NULL,
  `user_pub_id` varchar(255) NOT NULL DEFAULT '',
  `star` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL DEFAULT '',
  `created_at` varchar(255) NOT NULL DEFAULT '',
  `updated_at` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `reports_tbl`
--

CREATE TABLE `reports_tbl` (
  `id` int(11) NOT NULL,
  `user_pub_id` varchar(255) NOT NULL,
  `report_user_pub_id` varchar(255) NOT NULL,
  `comment` text NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0.pending,1.resolved',
  `pro_pub_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sortingAuctionByPrice`
--

CREATE TABLE `sortingAuctionByPrice` (
  `id` int(11) NOT NULL,
  `sorting_name` varchar(255) NOT NULL,
  `from_price` varchar(255) NOT NULL,
  `to_price` varchar(255) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `subscription_history`
--

CREATE TABLE `subscription_history` (
  `invoice_id` varchar(255) NOT NULL,
  `package_pub_id` varchar(255) NOT NULL,
  `user_pub_id` varchar(255) NOT NULL,
  `total_price` float NOT NULL DEFAULT '0',
  `discount` float NOT NULL DEFAULT '0',
  `tax` float NOT NULL DEFAULT '0',
  `final_price` float NOT NULL DEFAULT '0',
  `start_date` varchar(255) NOT NULL,
  `end_date` varchar(255) NOT NULL,
  `created_at` varchar(255) NOT NULL,
  `updated_at` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `subscription_package`
--

CREATE TABLE `subscription_package` (
  `package_pub_id` varchar(255) NOT NULL,
  `package_name` varchar(255) NOT NULL,
  `price` varchar(255) NOT NULL,
  `curr_pub_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL,
  `auction_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL,
  `total_days` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `support_tbl`
--

CREATE TABLE `support_tbl` (
  `support_pub_id` varchar(255) NOT NULL,
  `user_pub_id` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT '0',
  `created_at` varchar(255) NOT NULL,
  `updated_at` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_pub_id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `first_name` varchar(255) NOT NULL DEFAULT '',
  `last_name` varchar(255) NOT NULL DEFAULT '',
  `email` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL DEFAULT '',
  `country` varchar(255) DEFAULT '',
  `town` varchar(255) NOT NULL DEFAULT '',
  `address` varchar(255) NOT NULL DEFAULT '',
  `latitude` varchar(255) NOT NULL DEFAULT '',
  `longitude` varchar(255) NOT NULL DEFAULT '',
  `zip` varchar(255) NOT NULL DEFAULT '',
  `gender` varchar(255) NOT NULL DEFAULT '',
  `country_code` varchar(255) NOT NULL DEFAULT '',
  `image` varchar(255) NOT NULL DEFAULT '',
  `verify` varchar(255) NOT NULL DEFAULT '',
  `device_token` varchar(255) NOT NULL DEFAULT 'adminuser',
  `created_at` varchar(255) NOT NULL DEFAULT '',
  `updated_at` varchar(255) NOT NULL DEFAULT '',
  `mobile_no` varchar(255) NOT NULL DEFAULT '',
  `device_type` varchar(255) NOT NULL DEFAULT '',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '0.deactivate,1.activate',
  `verify_email_token` varchar(255) NOT NULL DEFAULT '',
  `email_verified` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `view`
--

CREATE TABLE `view` (
  `pro_pub_id` varchar(255) DEFAULT '',
  `user_pub_id` varchar(255) DEFAULT '',
  `created_at` varchar(255) DEFAULT '',
  `updated_on` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vote`
--

CREATE TABLE `vote` (
  `user_pub_id` varchar(255) DEFAULT '',
  `pro_pub_id` varchar(255) DEFAULT '',
  `created_at` varchar(255) DEFAULT '',
  `updated_on` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `won_users`
--

CREATE TABLE `won_users` (
  `won_id` varchar(255) NOT NULL,
  `owner_user_pub_id` varchar(255) NOT NULL,
  `won_user_pub_id` varchar(255) NOT NULL,
  `pro_pub_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `auction_pro`
--
ALTER TABLE `auction_pro`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `auction_pro_img`
--
ALTER TABLE `auction_pro_img`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `auction_sub_cat`
--
ALTER TABLE `auction_sub_cat`
  ADD PRIMARY KEY (`sub_cat_id`);

--
-- Indexes for table `block_user_bid`
--
ALTER TABLE `block_user_bid`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `brand`
--
ALTER TABLE `brand`
  ADD PRIMARY KEY (`brand_id`);

--
-- Indexes for table `chat`
--
ALTER TABLE `chat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `currency_setting`
--
ALTER TABLE `currency_setting`
  ADD PRIMARY KEY (`curr_pub_id`);

--
-- Indexes for table `favourite`
--
ALTER TABLE `favourite`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reports_tbl`
--
ALTER TABLE `reports_tbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sortingAuctionByPrice`
--
ALTER TABLE `sortingAuctionByPrice`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `auction_pro`
--
ALTER TABLE `auction_pro`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `auction_pro_img`
--
ALTER TABLE `auction_pro_img`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `block_user_bid`
--
ALTER TABLE `block_user_bid`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `chat`
--
ALTER TABLE `chat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `favourite`
--
ALTER TABLE `favourite`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `reports_tbl`
--
ALTER TABLE `reports_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `sortingAuctionByPrice`
--
ALTER TABLE `sortingAuctionByPrice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
