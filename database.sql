DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int DEFAULT NULL,
  `username` char(30) DEFAULT NULL,
  `password` char(80) DEFAULT NULL,
  `email` char(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `username` char(30) DEFAULT NULL,
  `question` char(200) DEFAULT NULL,
  `answer` text,
  `star` tinyint(1) DEFAULT '0',
  `hate` tinyint(1) DEFAULT '0',
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `question_requirements`;
CREATE TABLE `question_requirements` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_id` int DEFAULT NULL,
  `job` char(30) DEFAULT NULL,
  `tone` char(5) DEFAULT NULL,
  `extraInfo` char(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(80) NOT NULL,
  `email` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` char(30) DEFAULT NULL,
  `is_register` tinyint DEFAULT '0',
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
