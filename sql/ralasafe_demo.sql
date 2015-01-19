/*
Navicat MySQL Data Transfer

Source Server         : MariaDB
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : ralasafe_demo

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2013-09-26 23:49:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `company`
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `companyLevel` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES ('1', '总部', '0', '1');
INSERT INTO `company` VALUES ('2', '上海分公司', '1', '2');
INSERT INTO `company` VALUES ('3', '合肥分公司', '1', '2');
INSERT INTO `company` VALUES ('4', '徐汇区营业部', '2', '3');
INSERT INTO `company` VALUES ('5', '浦东区营业部', '2', '3');
INSERT INTO `company` VALUES ('6', '宝山区营业部', '2', '3');
INSERT INTO `company` VALUES ('7', '蜀山区营业部', '3', '3');
INSERT INTO `company` VALUES ('8', '高新区营业部', '3', '3');
INSERT INTO `company` VALUES ('9', '政务区营业部', '3', '3');
INSERT INTO `company` VALUES ('10', '闵行区营业部', '2', '3');

-- ----------------------------
-- Table structure for `demouser`
-- ----------------------------
DROP TABLE IF EXISTS `demouser`;
CREATE TABLE `demouser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginName` varchar(30) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `companyId` int(11) DEFAULT NULL,
  `departmentId` int(11) DEFAULT NULL,
  `isManager` int(11) DEFAULT NULL,
  `hireDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demouser
-- ----------------------------
INSERT INTO `demouser` VALUES ('1', '曹辛雷', '曹辛雷', 'password', '1', '2', '1', '2008-01-02 00:00:00');
INSERT INTO `demouser` VALUES ('2', '贾洪亮', '贾洪亮', 'password', '1', '4', '1', '2008-01-05 00:00:00');
INSERT INTO `demouser` VALUES ('3', '刘灿', '刘灿', 'password', '1', '3', '1', '2008-01-22 00:00:00');
INSERT INTO `demouser` VALUES ('4', '王朵朵', '王朵朵', 'password', '1', '3', '0', '2008-02-02 00:00:00');
INSERT INTO `demouser` VALUES ('5', '王刚', '王刚', 'password', '1', '3', '0', '2008-02-12 00:00:00');
INSERT INTO `demouser` VALUES ('6', '王胜利', '王胜利', 'password', '2', '4', '0', '2008-02-20 00:00:00');
INSERT INTO `demouser` VALUES ('7', '齐明华', '齐明华', 'password', '2', '3', '0', '2008-02-02 00:00:00');
INSERT INTO `demouser` VALUES ('8', '易斯', '易斯', 'password', '2', '3', '1', '2008-03-02 00:00:00');
INSERT INTO `demouser` VALUES ('9', '杨华', '杨华', 'password', '2', '1', '0', '2008-03-12 00:00:00');
INSERT INTO `demouser` VALUES ('10', '毕鑫鑫', '毕鑫鑫', 'password', '3', '4', '0', '2008-03-22 00:00:00');
INSERT INTO `demouser` VALUES ('11', '查昆', '查昆', 'password', '3', '3', '0', '2008-04-02 00:00:00');
INSERT INTO `demouser` VALUES ('12', '李文参', '李文参', 'password', '3', '3', '1', '2008-04-12 00:00:00');
INSERT INTO `demouser` VALUES ('13', '王三秦', '王三秦', 'password', '3', '1', '0', '2008-04-22 00:00:00');
INSERT INTO `demouser` VALUES ('14', '秦如宝', '秦如宝', 'password', '4', '3', '0', '2008-05-02 00:00:00');
INSERT INTO `demouser` VALUES ('15', '付一察', '付一察', 'password', '4', '4', '0', '2008-05-12 00:00:00');
INSERT INTO `demouser` VALUES ('16', '李丁', '李丁', 'password', '5', '3', '0', '2008-05-22 00:00:00');
INSERT INTO `demouser` VALUES ('17', '汪来', '汪来', 'password', '5', '4', '0', '2008-06-12 00:00:00');
INSERT INTO `demouser` VALUES ('18', '开小美', '开小美', 'password', '6', '3', '0', '2008-06-22 00:00:00');
INSERT INTO `demouser` VALUES ('19', '欧学', '欧学', 'password', '6', '4', '0', '2008-06-15 00:00:00');
INSERT INTO `demouser` VALUES ('20', '甘甜', '甘甜', 'password', '7', '3', '0', '2008-07-15 00:00:00');
INSERT INTO `demouser` VALUES ('21', '蒋红雨', '蒋红雨', 'password', '7', '4', '0', '2008-07-25 00:00:00');
INSERT INTO `demouser` VALUES ('22', '沙一宝', '沙一宝', 'password', '8', '3', '0', '2008-07-03 00:00:00');
INSERT INTO `demouser` VALUES ('23', '李明彩', '李明彩', 'password', '8', '4', '0', '2008-08-03 00:00:00');
INSERT INTO `demouser` VALUES ('24', '刘开', '刘开', 'password', '9', '3', '0', '2008-08-13 00:00:00');
INSERT INTO `demouser` VALUES ('25', '纪蓝', '纪蓝', 'password', '9', '4', '0', '2008-08-23 00:00:00');
INSERT INTO `demouser` VALUES ('26', '张风', '张风', 'password', '10', '3', '0', '2008-08-03 00:00:00');
INSERT INTO `demouser` VALUES ('27', '曲新华', '曲新华', 'password', '10', '4', '0', '2008-09-03 00:00:00');
INSERT INTO `demouser` VALUES ('29', '冯明路', '冯明路', 'password', '1', '2', '0', '2008-09-13 00:00:00');
INSERT INTO `demouser` VALUES ('30', '张希望', '张希望', 'password', '2', '2', '1', '2008-09-23 00:00:00');
INSERT INTO `demouser` VALUES ('31', '刘恒大', '刘恒大', 'password', '2', '2', '0', '2008-09-07 00:00:00');
INSERT INTO `demouser` VALUES ('32', '杨子', '杨子', 'password', '3', '2', '1', '2008-10-03 00:00:00');
INSERT INTO `demouser` VALUES ('33', '张易', '张易', 'password', '3', '2', '0', '2008-10-13 00:00:00');
INSERT INTO `demouser` VALUES ('34', '丁开', '丁开', 'password', '4', '2', '1', '2008-10-14 00:00:00');
INSERT INTO `demouser` VALUES ('35', '高甜甜', '高甜甜', 'password', '4', '2', '0', '2008-10-15 00:00:00');
INSERT INTO `demouser` VALUES ('36', '李不华', '李不华', 'password', '5', '2', '1', '2008-10-16 00:00:00');
INSERT INTO `demouser` VALUES ('37', '汪友人', '汪友人', 'password', '5', '2', '0', '2008-11-17 00:00:00');
INSERT INTO `demouser` VALUES ('38', '吴齐开', '吴齐开', 'password', '6', '2', '1', '2008-11-10 00:00:00');
INSERT INTO `demouser` VALUES ('39', '伍其', '伍其', 'password', '6', '2', '0', '2008-11-21 00:00:00');
INSERT INTO `demouser` VALUES ('40', '李来明', '李来明', 'password', '7', '2', '1', '2008-11-07 00:00:00');
INSERT INTO `demouser` VALUES ('41', '赖新', '赖新', 'password', '7', '2', '0', '2008-12-07 00:00:00');
INSERT INTO `demouser` VALUES ('42', '杨波波', '杨波波', 'password', '8', '2', '1', '2008-12-09 00:00:00');
INSERT INTO `demouser` VALUES ('43', '乔信', '乔信', 'password', '8', '2', '0', '2008-12-16 00:00:00');
INSERT INTO `demouser` VALUES ('44', '欧阳之利', '欧阳之利', 'password', '9', '2', '1', '2008-12-26 00:00:00');
INSERT INTO `demouser` VALUES ('45', '杨大凯', '杨大凯', 'password', '9', '2', '0', '2009-01-11 00:00:00');
INSERT INTO `demouser` VALUES ('46', '雷绮丽', '雷绮丽', 'password', '10', '2', '1', '2009-01-22 00:00:00');
INSERT INTO `demouser` VALUES ('47', '罗三', '罗三', 'password', '10', '2', '0', '2009-01-23 00:00:00');
INSERT INTO `demouser` VALUES ('48', '刘京', '刘京', 'password', '1', '4', '0', '2009-01-13 00:00:00');
INSERT INTO `demouser` VALUES ('49', '丁李李', '丁李李', 'password', '2', '4', '1', '2009-02-23 00:00:00');
INSERT INTO `demouser` VALUES ('50', '顾思议', '顾思议', 'password', '3', '4', '1', '2009-02-02 00:00:00');
INSERT INTO `demouser` VALUES ('51', '王流', '王流', 'password', '4', '4', '1', '2009-02-12 00:00:00');
INSERT INTO `demouser` VALUES ('52', '戴庐山', '戴庐山', 'password', '5', '4', '1', '2009-02-17 00:00:00');
INSERT INTO `demouser` VALUES ('53', '易涵涵', '易涵涵', 'password', '6', '4', '1', '2009-03-12 00:00:00');
INSERT INTO `demouser` VALUES ('54', '毛一', '毛一', 'password', '7', '4', '1', '2009-04-12 00:00:00');
INSERT INTO `demouser` VALUES ('55', '谢琳琳', '谢琳琳', 'password', '8', '4', '1', '2009-05-12 00:00:00');
INSERT INTO `demouser` VALUES ('56', '熊风', '熊风', 'password', '9', '4', '1', '2009-03-22 00:00:00');
INSERT INTO `demouser` VALUES ('57', '王斯人', '王斯人', 'password', '10', '4', '1', '2009-04-07 00:00:00');

-- ----------------------------
-- Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('1', '财务部', '0');
INSERT INTO `department` VALUES ('2', '研发部', '0');
INSERT INTO `department` VALUES ('3', '销售部', '0');
INSERT INTO `department` VALUES ('4', '人力资源部', '0');

-- ----------------------------
-- Table structure for `loan_money`
-- ----------------------------
DROP TABLE IF EXISTS `loan_money`;
CREATE TABLE `loan_money` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `loanDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of loan_money
-- ----------------------------
INSERT INTO `loan_money` VALUES ('46', '2', '4500', '2010-06-20');
INSERT INTO `loan_money` VALUES ('47', '2', '4500', '2010-06-20');
INSERT INTO `loan_money` VALUES ('48', '2', '4500', '2010-06-20');
INSERT INTO `loan_money` VALUES ('49', '2', '4500', '2010-06-20');
INSERT INTO `loan_money` VALUES ('50', '2', '5000', '2010-11-28');
INSERT INTO `loan_money` VALUES ('51', '2', '5000', '2010-11-28');
INSERT INTO `loan_money` VALUES ('52', '2', '5000', '2010-11-28');
INSERT INTO `loan_money` VALUES ('53', '6', '5000', '2010-11-28');
INSERT INTO `loan_money` VALUES ('54', '6', '5000', '2010-11-28');
INSERT INTO `loan_money` VALUES ('55', '6', '5000', '2010-11-28');
INSERT INTO `loan_money` VALUES ('56', '6', '5000', '2010-11-28');
INSERT INTO `loan_money` VALUES ('57', '6', '5000', '2011-03-24');
INSERT INTO `loan_money` VALUES ('58', '6', '5000', '2011-03-24');
INSERT INTO `loan_money` VALUES ('59', '6', '5000', '2011-03-24');
INSERT INTO `loan_money` VALUES ('60', '6', '5000', '2011-03-24');
INSERT INTO `loan_money` VALUES ('61', '6', '5000', '2011-04-16');
INSERT INTO `loan_money` VALUES ('62', '6', '5000', '2011-04-16');
INSERT INTO `loan_money` VALUES ('63', '6', '5000', '2011-04-16');
INSERT INTO `loan_money` VALUES ('64', '6', '5000', '2011-04-16');
INSERT INTO `loan_money` VALUES ('65', '6', '5000', '2011-04-26');
INSERT INTO `loan_money` VALUES ('66', '6', '5000', '2011-04-26');
INSERT INTO `loan_money` VALUES ('67', '6', '5000', '2011-04-26');
INSERT INTO `loan_money` VALUES ('68', '6', '5000', '2011-04-26');
INSERT INTO `loan_money` VALUES ('69', '6', '5000', '2011-04-28');
INSERT INTO `loan_money` VALUES ('70', '2', '5000', '2011-04-28');
INSERT INTO `loan_money` VALUES ('71', '2', '5000', '2011-04-28');
INSERT INTO `loan_money` VALUES ('72', '2', '5000', '2011-04-28');
INSERT INTO `loan_money` VALUES ('73', '2', '5000', '2011-04-28');
INSERT INTO `loan_money` VALUES ('74', '6', '5000', '2011-05-13');
INSERT INTO `loan_money` VALUES ('75', '6', '5000', '2011-06-03');
INSERT INTO `loan_money` VALUES ('76', '6', '5000', '2011-06-03');
INSERT INTO `loan_money` VALUES ('77', '6', '5000', '2011-06-03');
INSERT INTO `loan_money` VALUES ('78', '6', '5000', '2011-06-03');
INSERT INTO `loan_money` VALUES ('79', '2', '5000', '2011-06-20');
INSERT INTO `loan_money` VALUES ('80', '2', '5000', '2011-06-20');
INSERT INTO `loan_money` VALUES ('81', '2', '5000', '2011-06-20');
INSERT INTO `loan_money` VALUES ('82', '2', '5000', '2011-06-20');
INSERT INTO `loan_money` VALUES ('83', '17', '5000', '2011-06-20');
INSERT INTO `loan_money` VALUES ('84', '17', '5000', '2011-06-20');
INSERT INTO `loan_money` VALUES ('85', '17', '5000', '2011-06-20');
INSERT INTO `loan_money` VALUES ('86', '17', '5000', '2011-06-20');
INSERT INTO `loan_money` VALUES ('87', '2', '5000', '2011-06-21');
INSERT INTO `loan_money` VALUES ('88', '2', '5000', '2011-06-21');
INSERT INTO `loan_money` VALUES ('89', '2', '5000', '2011-06-21');
INSERT INTO `loan_money` VALUES ('90', '2', '5000', '2011-06-21');
INSERT INTO `loan_money` VALUES ('91', '2', '11', '2011-06-21');
INSERT INTO `loan_money` VALUES ('92', '2', '1', '2011-06-21');
INSERT INTO `loan_money` VALUES ('93', '2', '1', '2011-06-21');
INSERT INTO `loan_money` VALUES ('94', '6', '5000', '2011-06-21');
INSERT INTO `loan_money` VALUES ('95', '6', '5000', '2011-06-21');
INSERT INTO `loan_money` VALUES ('96', '6', '5000', '2011-06-21');
INSERT INTO `loan_money` VALUES ('97', '6', '5000', '2011-06-21');

-- ----------------------------
-- Table structure for `ralasafe_sequence`
-- ----------------------------
DROP TABLE IF EXISTS `ralasafe_sequence`;
CREATE TABLE `ralasafe_sequence` (
  `currentValue` int(11) DEFAULT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ralasafe_sequence
-- ----------------------------

-- ----------------------------
-- Table structure for `spec_user`
-- ----------------------------
DROP TABLE IF EXISTS `spec_user`;
CREATE TABLE `spec_user` (
  `userId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `areaId` varchar(50) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of spec_user
-- ----------------------------

-- ----------------------------
-- View structure for `userview`
-- ----------------------------
DROP VIEW IF EXISTS `userview`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `userview` AS select `u`.`id` AS `id`,`u`.`loginName` AS `loginName`,`u`.`name` AS `name`,`u`.`password` AS `password`,`u`.`companyId` AS `companyId`,`u`.`departmentId` AS `departmentId`,`u`.`isManager` AS `isManager`,`u`.`hireDate` AS `hireDate`,`c`.`companyLevel` AS `companyLevel`,`c`.`name` AS `companyName` from (`demouser` `u` join `company` `c`) where (`u`.`companyId` = `c`.`id`);
