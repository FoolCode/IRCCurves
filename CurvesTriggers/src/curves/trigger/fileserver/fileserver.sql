--
-- Table structure for table `downloads`
--

CREATE TABLE IF NOT EXISTS `downloads` (
  `nickname` varchar(64) NOT NULL,
  `hostname` varchar(256) NOT NULL,
  `started` datetime NOT NULL,
  `ended` datetime NOT NULL,
  `file` varchar(256) NOT NULL,
  `transferred` int(11) NOT NULL,
  `completed` tinyint(1) NOT NULL,
  KEY `file` (`file`),
  KEY `name` (`nickname`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;