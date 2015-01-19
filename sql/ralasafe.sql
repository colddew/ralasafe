/*
Navicat MySQL Data Transfer

Source Server         : MariaDB
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : ralasafe

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2013-09-26 23:48:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `application`
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of application
-- ----------------------------
INSERT INTO `application` VALUES ('ralasafe', 'ralasafe application');

-- ----------------------------
-- Table structure for `applicationusertype`
-- ----------------------------
DROP TABLE IF EXISTS `applicationusertype`;
CREATE TABLE `applicationusertype` (
  `appName` varchar(100) NOT NULL,
  `userTypeName` varchar(100) NOT NULL,
  `userMetadataStr` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`appName`,`userTypeName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of applicationusertype
-- ----------------------------
INSERT INTO `applicationusertype` VALUES ('ralasafe', 'ralasafe', 'id name companyName loginName password isManager companyId departmentId companyLevel ');

-- ----------------------------
-- Table structure for `ralasafe_backup`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_backup`;
CREATE TABLE `ralasafe_backup` (
  `id` int(11) NOT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` varchar(500) DEFAULT NULL,
  `content` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_backup
-- ----------------------------

-- ----------------------------
-- Table structure for `ralasafe_businessdata`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_businessdata`;
CREATE TABLE `ralasafe_businessdata` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `installDate` date DEFAULT NULL,
  `fileName` varchar(40) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `isLeaf` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_businessdata
-- ----------------------------
INSERT INTO `ralasafe_businessdata` VALUES ('1', '借款业务数据', '', '2011-06-20', null, '0', '0');
INSERT INTO `ralasafe_businessdata` VALUES ('2', '单笔借款大于5000', '', '2011-06-20', '2_ralasafe.xml', '1', '1');
INSERT INTO `ralasafe_businessdata` VALUES ('3', '加上本笔当天借款额不超过20000', '', '2011-06-20', '3_ralasafe.xml', '1', '1');

-- ----------------------------
-- Table structure for `ralasafe_decision_entitlement`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_decision_entitlement`;
CREATE TABLE `ralasafe_decision_entitlement` (
  `id` int(11) NOT NULL,
  `privilegeId` int(11) DEFAULT NULL,
  `userCategoryId` int(11) DEFAULT NULL,
  `businessDataId` int(11) DEFAULT NULL,
  `effect` varchar(100) DEFAULT NULL,
  `denyReason` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_decision_entitlement
-- ----------------------------
INSERT INTO `ralasafe_decision_entitlement` VALUES ('3', '4', '7', '2', 'deny', '单笔借款上限是5000');
INSERT INTO `ralasafe_decision_entitlement` VALUES ('4', '4', '7', '3', 'permit', '每天借款上限是20000	');

-- ----------------------------
-- Table structure for `ralasafe_privilege`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_privilege`;
CREATE TABLE `ralasafe_privilege` (
  `id` int(11) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `isLeaf` int(11) DEFAULT NULL,
  `decisionPolicyCombAlg` int(11) DEFAULT NULL,
  `queryPolicyCombAlg` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `constantName` varchar(40) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `target` varchar(20) DEFAULT NULL,
  `orderNum` int(11) DEFAULT NULL,
  `display` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_privilege
-- ----------------------------
INSERT INTO `ralasafe_privilege` VALUES ('-6', '-3', '', '角色管理', '1', '0', '0', '0', 'ROLE_ADMIN', '', '_self', '3', '1');
INSERT INTO `ralasafe_privilege` VALUES ('-5', '-3', '', '给用户分配角色', '1', '0', '0', '0', 'ASSIGN_ROLE_TO_USER', '', '_self', '2', '1');
INSERT INTO `ralasafe_privilege` VALUES ('-4', '-3', '制定授权策略、定义用户分类、业务数据分类和数据查询', '策略管理', '1', '0', '0', '0', 'POLICY_ADMIN', '', '_self', '1', '1');
INSERT INTO `ralasafe_privilege` VALUES ('-3', '0', '', 'Ralasafe 管理', '0', '0', '0', '0', '', '', '_self', '0', '1');
INSERT INTO `ralasafe_privilege` VALUES ('1', '0', '', '员工管理', '0', '0', '0', '0', null, null, null, '0', '1');
INSERT INTO `ralasafe_privilege` VALUES ('2', '1', '', '查询员工', '1', '0', '0', '0', 'QUERY_EMPLOYEE', '', '_self', '0', '1');
INSERT INTO `ralasafe_privilege` VALUES ('3', '0', '', '借款管理', '0', '0', '0', '0', null, null, null, '0', '1');
INSERT INTO `ralasafe_privilege` VALUES ('4', '3', '', '借款', '1', '0', '0', '0', 'LOAN', '', '_self', '0', '1');
INSERT INTO `ralasafe_privilege` VALUES ('5', '3', '', '查询借款', '1', '0', '0', '0', 'QUERY_LOAN', '', '_self', '0', '1');
INSERT INTO `ralasafe_privilege` VALUES ('6', '-1', '', '查询公司', '1', '0', '0', '1', 'QUERY_COMPANY', '', '_self', '0', '1');

-- ----------------------------
-- Table structure for `ralasafe_query`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_query`;
CREATE TABLE `ralasafe_query` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `installDate` date DEFAULT NULL,
  `fileName` varchar(40) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `isLeaf` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_query
-- ----------------------------
INSERT INTO `ralasafe_query` VALUES ('-10', 'current user\'s roles', 'Get current user\'s roles', '2011-06-20', '-10_ralasafe.xml', '0', '1');
INSERT INTO `ralasafe_query` VALUES ('1', '查询员工', '', '2011-06-20', null, '0', '0');
INSERT INTO `ralasafe_query` VALUES ('2', '查询所有员工', '', '2011-06-20', '2_ralasafe.xml', '1', '1');
INSERT INTO `ralasafe_query` VALUES ('3', '查询所在分公司及下属营业部员工', '', '2011-06-20', '3_ralasafe.xml', '1', '1');
INSERT INTO `ralasafe_query` VALUES ('4', '查询我所在公司员工', '', '2011-06-20', '4_ralasafe.xml', '1', '1');
INSERT INTO `ralasafe_query` VALUES ('5', '查询借款', '', '2011-06-20', null, '0', '0');
INSERT INTO `ralasafe_query` VALUES ('6', '本人当天借款记录', '', '2011-06-20', '6_ralasafe.xml', '5', '1');
INSERT INTO `ralasafe_query` VALUES ('7', '本人当天借款总额', '', '2011-06-20', '7_ralasafe.xml', '5', '1');
INSERT INTO `ralasafe_query` VALUES ('8', '查询公司', '', '2011-06-20', null, '0', '0');
INSERT INTO `ralasafe_query` VALUES ('9', '查询我所在公司', '', '2011-06-20', '9_ralasafe.xml', '8', '1');

-- ----------------------------
-- Table structure for `ralasafe_query_entitlement`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_query_entitlement`;
CREATE TABLE `ralasafe_query_entitlement` (
  `id` int(11) NOT NULL,
  `privilegeId` int(11) DEFAULT NULL,
  `userCategoryId` int(11) DEFAULT NULL,
  `queryId` int(11) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_query_entitlement
-- ----------------------------
INSERT INTO `ralasafe_query_entitlement` VALUES ('1', '2', '6', '4', '');
INSERT INTO `ralasafe_query_entitlement` VALUES ('2', '2', '5', '3', '');
INSERT INTO `ralasafe_query_entitlement` VALUES ('3', '2', '4', '2', '');
INSERT INTO `ralasafe_query_entitlement` VALUES ('4', '5', '7', '6', '');
INSERT INTO `ralasafe_query_entitlement` VALUES ('5', '6', '7', '9', '');

-- ----------------------------
-- Table structure for `ralasafe_ralasafe_userrole`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_ralasafe_userrole`;
CREATE TABLE `ralasafe_ralasafe_userrole` (
  `userid` int(11) NOT NULL,
  `roleid` int(11) NOT NULL,
  PRIMARY KEY (`userid`,`roleid`),
  KEY `roleid` (`roleid`),
  CONSTRAINT `ralasafe_ralasafe_userrole_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `ralasafe_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_ralasafe_userrole
-- ----------------------------
INSERT INTO `ralasafe_ralasafe_userrole` VALUES ('2', '1');
INSERT INTO `ralasafe_ralasafe_userrole` VALUES ('6', '1');
INSERT INTO `ralasafe_ralasafe_userrole` VALUES ('17', '1');

-- ----------------------------
-- Table structure for `ralasafe_role`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_role`;
CREATE TABLE `ralasafe_role` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_role
-- ----------------------------
INSERT INTO `ralasafe_role` VALUES ('-1', 'Ralasafe 管理员', null);
INSERT INTO `ralasafe_role` VALUES ('1', 'HR人力专员', '');

-- ----------------------------
-- Table structure for `ralasafe_roleprivilege`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_roleprivilege`;
CREATE TABLE `ralasafe_roleprivilege` (
  `roleid` int(11) NOT NULL,
  `privilegeid` int(11) NOT NULL,
  PRIMARY KEY (`roleid`,`privilegeid`),
  KEY `privilegeid` (`privilegeid`),
  CONSTRAINT `ralasafe_roleprivilege_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `ralasafe_role` (`id`),
  CONSTRAINT `ralasafe_roleprivilege_ibfk_2` FOREIGN KEY (`privilegeid`) REFERENCES `ralasafe_privilege` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_roleprivilege
-- ----------------------------
INSERT INTO `ralasafe_roleprivilege` VALUES ('-1', '-6');
INSERT INTO `ralasafe_roleprivilege` VALUES ('-1', '-5');
INSERT INTO `ralasafe_roleprivilege` VALUES ('-1', '-4');
INSERT INTO `ralasafe_roleprivilege` VALUES ('1', '1');
INSERT INTO `ralasafe_roleprivilege` VALUES ('1', '2');
INSERT INTO `ralasafe_roleprivilege` VALUES ('1', '3');
INSERT INTO `ralasafe_roleprivilege` VALUES ('1', '4');
INSERT INTO `ralasafe_roleprivilege` VALUES ('1', '5');

-- ----------------------------
-- Table structure for `ralasafe_sequence`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_sequence`;
CREATE TABLE `ralasafe_sequence` (
  `currentValue` int(11) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_sequence
-- ----------------------------
INSERT INTO `ralasafe_sequence` VALUES ('3', 'ralasafe_businessdata_id');
INSERT INTO `ralasafe_sequence` VALUES ('4', 'ralasafe_decision_entitlement_id');
INSERT INTO `ralasafe_sequence` VALUES ('5', 'ralasafe_query_entitlement_id');
INSERT INTO `ralasafe_sequence` VALUES ('9', 'ralasafe_query_id');
INSERT INTO `ralasafe_sequence` VALUES ('1', 'ralasafe_role_id');
INSERT INTO `ralasafe_sequence` VALUES ('8', 'ralasafe_usercategory_id');

-- ----------------------------
-- Table structure for `ralasafe_usercategory`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_usercategory`;
CREATE TABLE `ralasafe_usercategory` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `installDate` date DEFAULT NULL,
  `fileName` varchar(40) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `isLeaf` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_usercategory
-- ----------------------------
INSERT INTO `ralasafe_usercategory` VALUES ('-10', '角色', '', '2011-06-20', null, '0', '0');
INSERT INTO `ralasafe_usercategory` VALUES ('1', 'Ralasafe 管理员', null, '2011-06-20', '1_ralasafe.xml', '-10', '1');
INSERT INTO `ralasafe_usercategory` VALUES ('3', '按组织机构分类', '', '2011-06-20', null, '0', '0');
INSERT INTO `ralasafe_usercategory` VALUES ('4', '总公司用户', '', '2011-06-20', '4_ralasafe.xml', '3', '1');
INSERT INTO `ralasafe_usercategory` VALUES ('5', '分公司用户', '', '2011-06-20', '5_ralasafe.xml', '3', '1');
INSERT INTO `ralasafe_usercategory` VALUES ('6', '营业部用户', '', '2011-06-20', '6_ralasafe.xml', '3', '1');
INSERT INTO `ralasafe_usercategory` VALUES ('7', '所有人', '', '2011-06-20', '7_ralasafe.xml', '0', '1');
INSERT INTO `ralasafe_usercategory` VALUES ('8', 'HR人力专员', '', '2011-06-20', '8_ralasafe.xml', '-10', '1');

-- ----------------------------
-- Table structure for `usertype`
-- ----------------------------
DROP TABLE IF EXISTS `usertype`;
CREATE TABLE `usertype` (
  `name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `userMetadataXML` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of usertype
-- ----------------------------
INSERT INTO `usertype` VALUES ('ralasafe', 'ralasafe demo', '<?xml version=\"1.0\"?>\r\n<user>\r\n	<table ds=\"app\" name=\"mainTable\" sqlName=\"UserView\"\r\n		uniqueFields=\"loginName\">\r\n		<field name=\"id\" columnName=\"id\" sqlType=\"int\" javaType=\"java.lang.Integer\" />\r\n		<field name=\"name\" columnName=\"name\" sqlType=\"varchar(40)\"\r\n			javaType=\"java.lang.String\" displayName=\"Name\" show=\"true\" />\r\n		<field name=\"companyName\" columnName=\"companyName\" sqlType=\"varchar(100)\"\r\n			javaType=\"java.lang.String\" displayName=\"Company\" show=\"true\" />\r\n		<field name=\"loginName\" columnName=\"loginName\" sqlType=\"varchar(40)\"\r\n			javaType=\"java.lang.String\" />\r\n		<field name=\"password\" columnName=\"password\" sqlType=\"varchar(40)\"\r\n			javaType=\"java.lang.String\" />\r\n		<field name=\"isManager\" columnName=\"isManager\" sqlType=\"int\"\r\n			javaType=\"java.lang.Boolean\" />\r\n		<field name=\"companyId\" columnName=\"companyId\" sqlType=\"int\"\r\n			javaType=\"java.lang.Integer\" />\r\n		<field name=\"departmentId\" columnName=\"departmentId\" sqlType=\"int\"\r\n			javaType=\"java.lang.Integer\" />\r\n		<field name=\"companyLevel\" columnName=\"companyLevel\" sqlType=\"int\"\r\n			javaType=\"java.lang.Integer\" />\r\n	</table>\r\n</user>\r\n\r\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ');
