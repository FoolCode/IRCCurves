--
-- Table structure for table `mana`
--

DROP TABLE IF EXISTS `mana`;
CREATE TABLE IF NOT EXISTS `mana` (
  `user` varchar(64) NOT NULL,
  `mana` int(11) NOT NULL default '1',
  `creature_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `mana_creature`
--

DROP TABLE IF EXISTS `mana_creature`;
CREATE TABLE IF NOT EXISTS `mana_creature` (
  `creature_id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `cost` int(11) NOT NULL,
  `chance` float NOT NULL,
  `attribute` int(32) NOT NULL,
  `atk` int(11) NOT NULL,
  `def` int(11) NOT NULL,
  `levelup_id` int(11) NOT NULL,
  `levelup_chance` float NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mana_creature`
--

INSERT INTO `mana_creature` (`creature_id`, `name`, `cost`, `chance`, `attribute`, `atk`, `def`, `levelup_id`, `levelup_chance`) VALUES
(1, 'Baby Dragon', 3, 0.68, 0, 2, 3, 2, 0.6),
(2, 'Wyvern', 5, 0.5, 0, 5, 5, 3, 0.3),
(3, 'Hydra', 7, 0.33, 32, 7, 6, 4, 0.15),
(4, 'Behemoth', 9, 0.15, 32, 11, 10, 99, 0.05),
(11, 'Hello Kitty', 1, 0.9, 16, -1, -1, 12, 0.75),
(12, 'Albino Lion', 3, 0.55, 0, 3, 4, 23, 0.44),
(23, 'Griffin', 6, 0.3, 0, 4, 8, 24, 0.35),
(24, 'Sphinx', 7, 0.2, 0, 5, 9, 25, 0.35),
(21, 'Little Hawk', 2, 0.9, 0, 3, 1, 22, 0.7),
(22, 'Falcon', 4, 0.7, 0, 5, 2, 23, 0.52),
(31, 'Phoenix Egg', 1, 1, 16, 0, 1, 32, 0.2),
(32, 'Phoenix', 5, 0, 32, 3, 20, 99, 0.037),
(7, 'Salamander', 1, 0.9, 0, 2, 0, 1, 0.8),
(41, 'Teddy Bear', 1, 0.8, 16, -1, 2, 42, 0.73),
(42, 'Skunk', 3, 0.5, 32, 7, -3, 43, 0.45),
(43, 'Grizzly', 5, 0.34, 0, 5, 6, 44, 0.16),
(44, 'Kungfu Panda', 8, 0.19, 0, 14, 6, 99, 0.045),
(51, 'Poodle Puppy', 2, 0.8, 16, 2, 1, 52, 0.73),
(52, 'Bulldog', 4, 0.52, 16, 5, 3, 55, 0.45),
(54, 'Kerberos', 9, 0.17, 16, 12, 9, 99, 0.05),
(53, 'Fenrir', 7, 0.35, 16, 9, 6, 54, 0.15),
(55, 'Grey Wolf', 6, 0.4, 16, 6, 6, 53, 0.3),
(25, 'Chimera', 9, 0.12, 0, 6, 13, 99, 0.05),
(99, 'Woxxy', 20, 0, 48, 12, -12, 99, 0.5),
(61, 'Little Pony', 1, 0.9, 16, -2, 2, 62, 0.75),
(62, 'Mustang', 3, 0.66, 0, 0, 6, 63, 0.55),
(63, 'Pegasus', 5, 0.35, 0, 2, 8, 64, 0.31),
(64, 'Unicorn', 7, 0.26, 0, 4, 10, 65, 0.15),
(65, 'Armored Rhinoceros', 9, 0.11, 32, 7, 12, 99, 0.05),
(0, 'Bare Fist', 0, 0, 0, 0, 0, -1, 1),
(-1, 'Bronze Fist', 0, 0, 0, 2, 0, -2, 0.8),
(-2, 'Iron Fist', 0, 0, 0, 4, 0, -3, 0.64),
(-3, 'Silver Fist', 0, 0, 0, 6, 0, -4, 0.512),
(-4, 'Golden Fist', 0, 0, 0, 8, 0, -5, 1),
(-5, 'Platinum Fist', 0, 0, 0, 10, 0, -6, 0.4096),
(-6, 'Diamond Fist', 0, 0, 0, 12, 0, 0, 0);