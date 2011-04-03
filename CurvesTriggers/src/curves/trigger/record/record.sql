--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `message_id` int(11) NOT NULL auto_increment,
  `target` varchar(64) character set utf8_unicode_ci NOT NULL,
  `user` varchar(64) collate utf8_unicode_ci NOT NULL,
  `message` varchar(512) collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY  (`message_id`),
  KEY `time` (`time`),
  KEY `target` (`target`),
  FULLTEXT KEY `text` (`user`,`message`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=54692 ;

CREATE TABLE IF NOT EXISTS `messages_archive` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `target` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `user` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `message` varchar(512) COLLATE utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=ARCHIVE  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `uptime`
--

CREATE TABLE IF NOT EXISTS `uptime` (
  `user` varchar(64) NOT NULL,
  `channel` varchar(64) NOT NULL,
  `seconds` int(11) NOT NULL,
  PRIMARY KEY  (`user`,`channel`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `mail`
--

DROP TABLE IF EXISTS `mail`;
CREATE TABLE IF NOT EXISTS `mail` (
  `receipient` varchar(64) NOT NULL,
  `sender` varchar(64) NOT NULL,
  `message` varchar(512) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY  (`receipient`,`sender`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;