-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.51a-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema zh_mydemo
--

CREATE DATABASE IF NOT EXISTS zh_mydemo;
USE zh_mydemo;

--
-- Temporary table structure for view `userview`
--
DROP TABLE IF EXISTS `userview`;
DROP VIEW IF EXISTS `userview`;
CREATE TABLE `userview` (
  `id` int(11),
  `loginName` varchar(30),
  `name` varchar(30),
  `password` varchar(30),
  `companyId` int(11),
  `departmentId` int(11),
  `isManager` int(11),
  `hireDate` datetime,
  `companyLevel` int(11),
  `companyName` varchar(30)
);

--
-- Definition of table `company`
--

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(30) default NULL,
  `parentId` int(11) default NULL,
  `companyLevel` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `company`
--

/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` (`id`,`name`,`parentId`,`companyLevel`) VALUES 
 (1,'总部',0,1),
 (2,'上海分公司',1,2),
 (3,'合肥分公司',1,2),
 (4,'徐汇区营业部',2,3),
 (5,'浦东区营业部',2,3),
 (6,'宝山区营业部',2,3),
 (7,'蜀山区营业部',3,3),
 (8,'高新区营业部',3,3),
 (9,'政务区营业部',3,3),
 (10,'闵行区营业部',2,3);
/*!40000 ALTER TABLE `company` ENABLE KEYS */;


--
-- Definition of table `demouser`
--

DROP TABLE IF EXISTS `demouser`;
CREATE TABLE `demouser` (
  `id` int(11) NOT NULL auto_increment,
  `loginName` varchar(30) default NULL,
  `name` varchar(30) default NULL,
  `password` varchar(30) default NULL,
  `companyId` int(11) default NULL,
  `departmentId` int(11) default NULL,
  `isManager` int(11) default NULL,
  `hireDate` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `demouser`
--

/*!40000 ALTER TABLE `demouser` DISABLE KEYS */;
INSERT INTO `demouser` (`id`,`loginName`,`name`,`password`,`companyId`,`departmentId`,`isManager`,`hireDate`) VALUES 
 (1,'曹辛雷','曹辛雷','password',1,2,1,'2008-01-02 00:00:00'),
 (2,'贾洪亮','贾洪亮','password',1,4,1,'2008-01-05 00:00:00'),
 (3,'刘灿','刘灿','password',1,3,1,'2008-01-22 00:00:00'),
 (4,'王朵朵','王朵朵','password',1,3,0,'2008-02-02 00:00:00'),
 (5,'王刚','王刚','password',1,3,0,'2008-02-12 00:00:00'),
 (6,'王胜利','王胜利','password',2,4,0,'2008-02-20 00:00:00'),
 (7,'齐明华','齐明华','password',2,3,0,'2008-02-02 00:00:00'),
 (8,'易斯','易斯','password',2,3,1,'2008-03-02 00:00:00'),
 (9,'杨华','杨华','password',2,1,0,'2008-03-12 00:00:00'),
 (10,'毕鑫鑫','毕鑫鑫','password',3,4,0,'2008-03-22 00:00:00'),
 (11,'查昆','查昆','password',3,3,0,'2008-04-02 00:00:00'),
 (12,'李文参','李文参','password',3,3,1,'2008-04-12 00:00:00'),
 (13,'王三秦','王三秦','password',3,1,0,'2008-04-22 00:00:00'),
 (14,'秦如宝','秦如宝','password',4,3,0,'2008-05-02 00:00:00'),
 (15,'付一察','付一察','password',4,4,0,'2008-05-12 00:00:00'),
 (16,'李丁','李丁','password',5,3,0,'2008-05-22 00:00:00'),
 (17,'汪来','汪来','password',5,4,0,'2008-06-12 00:00:00'),
 (18,'开小美','开小美','password',6,3,0,'2008-06-22 00:00:00'),
 (19,'欧学','欧学','password',6,4,0,'2008-06-15 00:00:00'),
 (20,'甘甜','甘甜','password',7,3,0,'2008-07-15 00:00:00'),
 (21,'蒋红雨','蒋红雨','password',7,4,0,'2008-07-25 00:00:00'),
 (22,'沙一宝','沙一宝','password',8,3,0,'2008-07-03 00:00:00'),
 (23,'李明彩','李明彩','password',8,4,0,'2008-08-03 00:00:00'),
 (24,'刘开','刘开','password',9,3,0,'2008-08-13 00:00:00'),
 (25,'纪蓝','纪蓝','password',9,4,0,'2008-08-23 00:00:00'),
 (26,'张风','张风','password',10,3,0,'2008-08-03 00:00:00'),
 (27,'曲新华','曲新华','password',10,4,0,'2008-09-03 00:00:00'),
 (29,'冯明路','冯明路','password',1,2,0,'2008-09-13 00:00:00'),
 (30,'张希望','张希望','password',2,2,1,'2008-09-23 00:00:00'),
 (31,'刘恒大','刘恒大','password',2,2,0,'2008-09-07 00:00:00'),
 (32,'杨子','杨子','password',3,2,1,'2008-10-03 00:00:00'),
 (33,'张易','张易','password',3,2,0,'2008-10-13 00:00:00'),
 (34,'丁开','丁开','password',4,2,1,'2008-10-14 00:00:00'),
 (35,'高甜甜','高甜甜','password',4,2,0,'2008-10-15 00:00:00'),
 (36,'李不华','李不华','password',5,2,1,'2008-10-16 00:00:00'),
 (37,'汪友人','汪友人','password',5,2,0,'2008-11-17 00:00:00'),
 (38,'吴齐开','吴齐开','password',6,2,1,'2008-11-10 00:00:00'),
 (39,'伍其','伍其','password',6,2,0,'2008-11-21 00:00:00'),
 (40,'李来明','李来明','password',7,2,1,'2008-11-07 00:00:00'),
 (41,'赖新','赖新','password',7,2,0,'2008-12-07 00:00:00'),
 (42,'杨波波','杨波波','password',8,2,1,'2008-12-09 00:00:00'),
 (43,'乔信','乔信','password',8,2,0,'2008-12-16 00:00:00'),
 (44,'欧阳之利','欧阳之利','password',9,2,1,'2008-12-26 00:00:00'),
 (45,'杨大凯','杨大凯','password',9,2,0,'2009-01-11 00:00:00'),
 (46,'雷绮丽','雷绮丽','password',10,2,1,'2009-01-22 00:00:00'),
 (47,'罗三','罗三','password',10,2,0,'2009-01-23 00:00:00'),
 (48,'刘京','刘京','password',1,4,0,'2009-01-13 00:00:00'),
 (49,'丁李李','丁李李','password',2,4,1,'2009-02-23 00:00:00'),
 (50,'顾思议','顾思议','password',3,4,1,'2009-02-02 00:00:00'),
 (51,'王流','王流','password',4,4,1,'2009-02-12 00:00:00'),
 (52,'戴庐山','戴庐山','password',5,4,1,'2009-02-17 00:00:00'),
 (53,'易涵涵','易涵涵','password',6,4,1,'2009-03-12 00:00:00'),
 (54,'毛一','毛一','password',7,4,1,'2009-04-12 00:00:00'),
 (55,'谢琳琳','谢琳琳','password',8,4,1,'2009-05-12 00:00:00'),
 (56,'熊风','熊风','password',9,4,1,'2009-03-22 00:00:00'),
 (57,'王斯人','王斯人','password',10,4,1,'2009-04-07 00:00:00');
/*!40000 ALTER TABLE `demouser` ENABLE KEYS */;


--
-- Definition of table `department`
--

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(30) default NULL,
  `parentId` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `department`
--

/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` (`id`,`name`,`parentId`) VALUES 
 (1,'财务部',0),
 (2,'研发部',0),
 (3,'销售部',0),
 (4,'人力资源部',0);
/*!40000 ALTER TABLE `department` ENABLE KEYS */;


--
-- Definition of table `loan_money`
--

DROP TABLE IF EXISTS `loan_money`;
CREATE TABLE `loan_money` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) default NULL,
  `money` int(11) default NULL,
  `loanDate` date default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `loan_money`
--

/*!40000 ALTER TABLE `loan_money` DISABLE KEYS */;
INSERT INTO `loan_money` (`id`,`userId`,`money`,`loanDate`) VALUES 
 (46,2,4500,'2010-06-20'),
 (47,2,4500,'2010-06-20'),
 (48,2,4500,'2010-06-20'),
 (49,2,4500,'2010-06-20'),
 (50,2,5000,'2010-11-28'),
 (51,2,5000,'2010-11-28'),
 (52,2,5000,'2010-11-28'),
 (53,6,5000,'2010-11-28'),
 (54,6,5000,'2010-11-28'),
 (55,6,5000,'2010-11-28'),
 (56,6,5000,'2010-11-28'),
 (57,6,5000,'2011-03-24'),
 (58,6,5000,'2011-03-24'),
 (59,6,5000,'2011-03-24'),
 (60,6,5000,'2011-03-24'),
 (61,6,5000,'2011-04-16'),
 (62,6,5000,'2011-04-16'),
 (63,6,5000,'2011-04-16'),
 (64,6,5000,'2011-04-16'),
 (65,6,5000,'2011-04-26'),
 (66,6,5000,'2011-04-26'),
 (67,6,5000,'2011-04-26'),
 (68,6,5000,'2011-04-26'),
 (69,6,5000,'2011-04-28'),
 (70,2,5000,'2011-04-28'),
 (71,2,5000,'2011-04-28'),
 (72,2,5000,'2011-04-28'),
 (73,2,5000,'2011-04-28'),
 (74,6,5000,'2011-05-13'),
 (75,6,5000,'2011-06-03'),
 (76,6,5000,'2011-06-03'),
 (77,6,5000,'2011-06-03'),
 (78,6,5000,'2011-06-03'),
 (79,2,5000,'2011-06-20'),
 (80,2,5000,'2011-06-20'),
 (81,2,5000,'2011-06-20'),
 (82,2,5000,'2011-06-20'),
 (83,17,5000,'2011-06-20'),
 (84,17,5000,'2011-06-20'),
 (85,17,5000,'2011-06-20'),
 (86,17,5000,'2011-06-20'),
 (87,2,5000,'2011-06-21'),
 (88,2,5000,'2011-06-21'),
 (89,2,5000,'2011-06-21'),
 (90,2,5000,'2011-06-21'),
 (91,2,11,'2011-06-21'),
 (92,2,1,'2011-06-21'),
 (93,2,1,'2011-06-21'),
 (94,6,5000,'2011-06-21'),
 (95,6,5000,'2011-06-21'),
 (96,6,5000,'2011-06-21'),
 (97,6,5000,'2011-06-21');
/*!40000 ALTER TABLE `loan_money` ENABLE KEYS */;


--
-- Definition of table `ralasafe_sequence`
--

DROP TABLE IF EXISTS `ralasafe_sequence`;
CREATE TABLE `ralasafe_sequence` (
  `currentValue` int(11) default NULL,
  `name` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_sequence`
--

/*!40000 ALTER TABLE `ralasafe_sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `ralasafe_sequence` ENABLE KEYS */;


--
-- Definition of table `spec_user`
--

DROP TABLE IF EXISTS `spec_user`;
CREATE TABLE `spec_user` (
  `userId` int(10) unsigned NOT NULL auto_increment,
  `areaId` varchar(50) NOT NULL,
  PRIMARY KEY  (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `spec_user`
--

/*!40000 ALTER TABLE `spec_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `spec_user` ENABLE KEYS */;

--
-- Create schema zh_ralasafe
--

CREATE DATABASE IF NOT EXISTS zh_ralasafe;
USE zh_ralasafe;

--
-- Definition of table `application`
--

DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `name` varchar(100) NOT NULL,
  `description` varchar(500) default NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `application`
--

/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` (`name`,`description`) VALUES 
 ('ralasafe','ralasafe application');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;


--
-- Definition of table `applicationusertype`
--

DROP TABLE IF EXISTS `applicationusertype`;
CREATE TABLE `applicationusertype` (
  `appName` varchar(100) NOT NULL,
  `userTypeName` varchar(100) NOT NULL,
  `userMetadataStr` varchar(1000) default NULL,
  PRIMARY KEY  (`appName`,`userTypeName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `applicationusertype`
--

/*!40000 ALTER TABLE `applicationusertype` DISABLE KEYS */;
INSERT INTO `applicationusertype` (`appName`,`userTypeName`,`userMetadataStr`) VALUES 
 ('ralasafe','ralasafe','id name companyName loginName password isManager companyId departmentId companyLevel ');
/*!40000 ALTER TABLE `applicationusertype` ENABLE KEYS */;


--
-- Definition of table `ralasafe_backup`
--

DROP TABLE IF EXISTS `ralasafe_backup`;
CREATE TABLE `ralasafe_backup` (
  `id` int(11) NOT NULL,
  `createTime` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `description` varchar(500) default NULL,
  `content` blob,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_backup`
--

/*!40000 ALTER TABLE `ralasafe_backup` DISABLE KEYS */;
/*!40000 ALTER TABLE `ralasafe_backup` ENABLE KEYS */;


--
-- Definition of table `ralasafe_businessdata`
--

DROP TABLE IF EXISTS `ralasafe_businessdata`;
CREATE TABLE `ralasafe_businessdata` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) default NULL,
  `installDate` date default NULL,
  `fileName` varchar(40) default NULL,
  `pid` int(11) default NULL,
  `isLeaf` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_businessdata`
--

/*!40000 ALTER TABLE `ralasafe_businessdata` DISABLE KEYS */;
INSERT INTO `ralasafe_businessdata` (`id`,`name`,`description`,`installDate`,`fileName`,`pid`,`isLeaf`) VALUES 
 (1,'借款业务数据','','2011-06-20',NULL,0,0),
 (2,'单笔借款大于5000','','2011-06-20','2_ralasafe.xml',1,1),
 (3,'加上本笔当天借款额不超过20000','','2011-06-20','3_ralasafe.xml',1,1);
/*!40000 ALTER TABLE `ralasafe_businessdata` ENABLE KEYS */;


--
-- Definition of table `ralasafe_decision_entitlement`
--

DROP TABLE IF EXISTS `ralasafe_decision_entitlement`;
CREATE TABLE `ralasafe_decision_entitlement` (
  `id` int(11) NOT NULL,
  `privilegeId` int(11) default NULL,
  `userCategoryId` int(11) default NULL,
  `businessDataId` int(11) default NULL,
  `effect` varchar(100) default NULL,
  `denyReason` varchar(1000) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_decision_entitlement`
--

/*!40000 ALTER TABLE `ralasafe_decision_entitlement` DISABLE KEYS */;
INSERT INTO `ralasafe_decision_entitlement` (`id`,`privilegeId`,`userCategoryId`,`businessDataId`,`effect`,`denyReason`) VALUES 
 (3,4,7,2,'deny','单笔借款上限是5000'),
 (4,4,7,3,'permit','每天借款上限是20000	');
/*!40000 ALTER TABLE `ralasafe_decision_entitlement` ENABLE KEYS */;


--
-- Definition of table `ralasafe_privilege`
--

DROP TABLE IF EXISTS `ralasafe_privilege`;
CREATE TABLE `ralasafe_privilege` (
  `id` int(11) NOT NULL,
  `pid` int(11) default NULL,
  `description` varchar(500) default NULL,
  `name` varchar(100) NOT NULL,
  `isLeaf` int(11) default NULL,
  `decisionPolicyCombAlg` int(11) default NULL,
  `queryPolicyCombAlg` int(11) default NULL,
  `type` int(11) default NULL,
  `constantName` varchar(40) default NULL,
  `url` varchar(100) default NULL,
  `target` varchar(20) default NULL,
  `orderNum` int(11) default NULL,
  `display` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_privilege`
--

/*!40000 ALTER TABLE `ralasafe_privilege` DISABLE KEYS */;
INSERT INTO `ralasafe_privilege` (`id`,`pid`,`description`,`name`,`isLeaf`,`decisionPolicyCombAlg`,`queryPolicyCombAlg`,`type`,`constantName`,`url`,`target`,`orderNum`,`display`) VALUES 
 (-6,-3,'','角色管理',1,0,0,0,'ROLE_ADMIN','','_self',3,1),
 (-5,-3,'','给用户分配角色',1,0,0,0,'ASSIGN_ROLE_TO_USER','','_self',2,1),
 (-4,-3,'制定授权策略、定义用户分类、业务数据分类和数据查询','策略管理',1,0,0,0,'POLICY_ADMIN','','_self',1,1),
 (-3,0,'','Ralasafe 管理',0,0,0,0,'','','_self',0,1),
 (1,0,'','员工管理',0,0,0,0,NULL,NULL,NULL,0,1),
 (2,1,'','查询员工',1,0,0,0,'QUERY_EMPLOYEE','','_self',0,1),
 (3,0,'','借款管理',0,0,0,0,NULL,NULL,NULL,0,1),
 (4,3,'','借款',1,0,0,0,'LOAN','','_self',0,1),
 (5,3,'','查询借款',1,0,0,0,'QUERY_LOAN','','_self',0,1),
 (6,-1,'','查询公司',1,0,0,1,'QUERY_COMPANY','','_self',0,1);
/*!40000 ALTER TABLE `ralasafe_privilege` ENABLE KEYS */;


--
-- Definition of table `ralasafe_query`
--

DROP TABLE IF EXISTS `ralasafe_query`;
CREATE TABLE `ralasafe_query` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) default NULL,
  `installDate` date default NULL,
  `fileName` varchar(40) default NULL,
  `pid` int(11) default NULL,
  `isLeaf` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_query`
--

/*!40000 ALTER TABLE `ralasafe_query` DISABLE KEYS */;
INSERT INTO `ralasafe_query` (`id`,`name`,`description`,`installDate`,`fileName`,`pid`,`isLeaf`) VALUES 
 (-10,'current user\'s roles','Get current user\'s roles','2011-06-20','-10_ralasafe.xml',0,1),
 (1,'查询员工','','2011-06-20',NULL,0,0),
 (2,'查询所有员工','','2011-06-20','2_ralasafe.xml',1,1),
 (3,'查询所在分公司及下属营业部员工','','2011-06-20','3_ralasafe.xml',1,1),
 (4,'查询我所在公司员工','','2011-06-20','4_ralasafe.xml',1,1),
 (5,'查询借款','','2011-06-20',NULL,0,0),
 (6,'本人当天借款记录','','2011-06-20','6_ralasafe.xml',5,1),
 (7,'本人当天借款总额','','2011-06-20','7_ralasafe.xml',5,1),
 (8,'查询公司','','2011-06-20',NULL,0,0),
 (9,'查询我所在公司','','2011-06-20','9_ralasafe.xml',8,1);
/*!40000 ALTER TABLE `ralasafe_query` ENABLE KEYS */;


--
-- Definition of table `ralasafe_query_entitlement`
--

DROP TABLE IF EXISTS `ralasafe_query_entitlement`;
CREATE TABLE `ralasafe_query_entitlement` (
  `id` int(11) NOT NULL,
  `privilegeId` int(11) default NULL,
  `userCategoryId` int(11) default NULL,
  `queryId` int(11) default NULL,
  `description` varchar(500) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_query_entitlement`
--

/*!40000 ALTER TABLE `ralasafe_query_entitlement` DISABLE KEYS */;
INSERT INTO `ralasafe_query_entitlement` (`id`,`privilegeId`,`userCategoryId`,`queryId`,`description`) VALUES 
 (1,2,6,4,''),
 (2,2,5,3,''),
 (3,2,4,2,''),
 (4,5,7,6,''),
 (5,6,7,9,'');
/*!40000 ALTER TABLE `ralasafe_query_entitlement` ENABLE KEYS */;


--
-- Definition of table `ralasafe_ralasafe_userrole`
--

DROP TABLE IF EXISTS `ralasafe_ralasafe_userrole`;
CREATE TABLE `ralasafe_ralasafe_userrole` (
  `userid` int(11) NOT NULL,
  `roleid` int(11) NOT NULL,
  PRIMARY KEY  (`userid`,`roleid`),
  KEY `roleid` (`roleid`),
  CONSTRAINT `ralasafe_ralasafe_userrole_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `ralasafe_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_ralasafe_userrole`
--

/*!40000 ALTER TABLE `ralasafe_ralasafe_userrole` DISABLE KEYS */;
INSERT INTO `ralasafe_ralasafe_userrole` (`userid`,`roleid`) VALUES 
 (2,1),
 (6,1),
 (17,1);
/*!40000 ALTER TABLE `ralasafe_ralasafe_userrole` ENABLE KEYS */;


--
-- Definition of table `ralasafe_role`
--

DROP TABLE IF EXISTS `ralasafe_role`;
CREATE TABLE `ralasafe_role` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_role`
--

/*!40000 ALTER TABLE `ralasafe_role` DISABLE KEYS */;
INSERT INTO `ralasafe_role` (`id`,`name`,`description`) VALUES 
 (-1,'Ralasafe 管理员',NULL),
 (1,'HR人力专员','');
/*!40000 ALTER TABLE `ralasafe_role` ENABLE KEYS */;


--
-- Definition of table `ralasafe_roleprivilege`
--

DROP TABLE IF EXISTS `ralasafe_roleprivilege`;
CREATE TABLE `ralasafe_roleprivilege` (
  `roleid` int(11) NOT NULL,
  `privilegeid` int(11) NOT NULL,
  PRIMARY KEY  (`roleid`,`privilegeid`),
  KEY `privilegeid` (`privilegeid`),
  CONSTRAINT `ralasafe_roleprivilege_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `ralasafe_role` (`id`),
  CONSTRAINT `ralasafe_roleprivilege_ibfk_2` FOREIGN KEY (`privilegeid`) REFERENCES `ralasafe_privilege` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_roleprivilege`
--

/*!40000 ALTER TABLE `ralasafe_roleprivilege` DISABLE KEYS */;
INSERT INTO `ralasafe_roleprivilege` (`roleid`,`privilegeid`) VALUES 
 (-1,-6),
 (-1,-5),
 (-1,-4),
 (1,1),
 (1,2),
 (1,3),
 (1,4),
 (1,5);
/*!40000 ALTER TABLE `ralasafe_roleprivilege` ENABLE KEYS */;


--
-- Definition of table `ralasafe_sequence`
--

DROP TABLE IF EXISTS `ralasafe_sequence`;
CREATE TABLE `ralasafe_sequence` (
  `currentValue` int(11) default NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_sequence`
--

/*!40000 ALTER TABLE `ralasafe_sequence` DISABLE KEYS */;
INSERT INTO `ralasafe_sequence` (`currentValue`,`name`) VALUES 
 (3,'ralasafe_businessdata_id'),
 (4,'ralasafe_decision_entitlement_id'),
 (5,'ralasafe_query_entitlement_id'),
 (9,'ralasafe_query_id'),
 (1,'ralasafe_role_id'),
 (8,'ralasafe_usercategory_id');
/*!40000 ALTER TABLE `ralasafe_sequence` ENABLE KEYS */;


--
-- Definition of table `ralasafe_usercategory`
--

DROP TABLE IF EXISTS `ralasafe_usercategory`;
CREATE TABLE `ralasafe_usercategory` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) default NULL,
  `installDate` date default NULL,
  `fileName` varchar(40) default NULL,
  `pid` int(11) default NULL,
  `isLeaf` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ralasafe_usercategory`
--

/*!40000 ALTER TABLE `ralasafe_usercategory` DISABLE KEYS */;
INSERT INTO `ralasafe_usercategory` (`id`,`name`,`description`,`installDate`,`fileName`,`pid`,`isLeaf`) VALUES 
 (-10,'角色','','2011-06-20',NULL,0,0),
 (1,'Ralasafe 管理员',NULL,'2011-06-20','1_ralasafe.xml',-10,1),
 (3,'按组织机构分类','','2011-06-20',NULL,0,0),
 (4,'总公司用户','','2011-06-20','4_ralasafe.xml',3,1),
 (5,'分公司用户','','2011-06-20','5_ralasafe.xml',3,1),
 (6,'营业部用户','','2011-06-20','6_ralasafe.xml',3,1),
 (7,'所有人','','2011-06-20','7_ralasafe.xml',0,1),
 (8,'HR人力专员','','2011-06-20','8_ralasafe.xml',-10,1);
/*!40000 ALTER TABLE `ralasafe_usercategory` ENABLE KEYS */;


--
-- Definition of table `usertype`
--

DROP TABLE IF EXISTS `usertype`;
CREATE TABLE `usertype` (
  `name` varchar(100) NOT NULL,
  `description` varchar(500) default NULL,
  `userMetadataXML` varchar(4000) default NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `usertype`
--

/*!40000 ALTER TABLE `usertype` DISABLE KEYS */;
INSERT INTO `usertype` (`name`,`description`,`userMetadataXML`) VALUES 
 ('ralasafe','ralasafe demo','<?xml version=\"1.0\"?>\r\n<user>\r\n	<table ds=\"app\" name=\"mainTable\" sqlName=\"UserView\"\r\n		uniqueFields=\"loginName\">\r\n		<field name=\"id\" columnName=\"id\" sqlType=\"int\" javaType=\"java.lang.Integer\" />\r\n		<field name=\"name\" columnName=\"name\" sqlType=\"varchar(40)\"\r\n			javaType=\"java.lang.String\" displayName=\"Name\" show=\"true\" />\r\n		<field name=\"companyName\" columnName=\"companyName\" sqlType=\"varchar(100)\"\r\n			javaType=\"java.lang.String\" displayName=\"Company\" show=\"true\" />\r\n		<field name=\"loginName\" columnName=\"loginName\" sqlType=\"varchar(40)\"\r\n			javaType=\"java.lang.String\" />\r\n		<field name=\"password\" columnName=\"password\" sqlType=\"varchar(40)\"\r\n			javaType=\"java.lang.String\" />\r\n		<field name=\"isManager\" columnName=\"isManager\" sqlType=\"int\"\r\n			javaType=\"java.lang.Boolean\" />\r\n		<field name=\"companyId\" columnName=\"companyId\" sqlType=\"int\"\r\n			javaType=\"java.lang.Integer\" />\r\n		<field name=\"departmentId\" columnName=\"departmentId\" sqlType=\"int\"\r\n			javaType=\"java.lang.Integer\" />\r\n		<field name=\"companyLevel\" columnName=\"companyLevel\" sqlType=\"int\"\r\n			javaType=\"java.lang.Integer\" />\r\n	</table>\r\n</user>\r\n\r\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ');
/*!40000 ALTER TABLE `usertype` ENABLE KEYS */;

--
-- Create schema zh_mydemo
--

CREATE DATABASE IF NOT EXISTS zh_mydemo;
USE zh_mydemo;

--
-- Definition of view `userview`
--

DROP TABLE IF EXISTS `userview`;
DROP VIEW IF EXISTS `userview`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `userview` AS select `u`.`id` AS `id`,`u`.`loginName` AS `loginName`,`u`.`name` AS `name`,`u`.`password` AS `password`,`u`.`companyId` AS `companyId`,`u`.`departmentId` AS `departmentId`,`u`.`isManager` AS `isManager`,`u`.`hireDate` AS `hireDate`,`c`.`companyLevel` AS `companyLevel`,`c`.`name` AS `companyName` from (`demouser` `u` join `company` `c`) where (`u`.`companyId` = `c`.`id`);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
