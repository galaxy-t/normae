/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80014
 Source Host           : localhost:3306
 Source Schema         : normae_sms

 Target Server Type    : MySQL
 Target Server Version : 80014
 File Encoding         : 65001

 Date: 10/06/2020 17:18:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_message_log
-- ----------------------------
DROP TABLE IF EXISTS `t_message_log`;
CREATE TABLE `t_message_log`  (
  `id` bigint(20) NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接收短信的手机号码',
  `message` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短信发送内容,实际上存储的是短信模版参数字符串',
  `sms_platform` int(1) NOT NULL COMMENT '短信服务商平台 , 本平台只有阿里云一个',
  `message_type` int(2) NOT NULL COMMENT '短信发送类型',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message_log
-- ----------------------------
INSERT INTO `t_message_log` VALUES (1266195713482190850, '13678666356', '{\'code\':\'475063\'}', 1, 1, '2020-05-29 10:32:37');
INSERT INTO `t_message_log` VALUES (1266196743422898177, '13678666356', '{\'code\':\'678339\'}', 1, 1, '2020-05-29 10:36:43');
INSERT INTO `t_message_log` VALUES (1266197760550006786, '13678666356', '{\'code\':\'319644\'}', 1, 1, '2020-05-29 10:40:46');
INSERT INTO `t_message_log` VALUES (1266198037369876482, '13678666356', '{\'code\':\'660030\'}', 1, 1, '2020-05-29 10:41:52');
INSERT INTO `t_message_log` VALUES (1266198175089844225, '13678666356', '{\'code\':\'354419\'}', 1, 1, '2020-05-29 10:42:24');
INSERT INTO `t_message_log` VALUES (1266198616527794178, '13678666356', '{\'code\':\'100230\'}', 1, 1, '2020-05-29 10:44:10');
INSERT INTO `t_message_log` VALUES (1266198980740182017, '13678666356', '{\'code\':\'054437\'}', 1, 1, '2020-05-29 10:45:37');
INSERT INTO `t_message_log` VALUES (1266199555028504577, '13678666356', '{\'code\':\'851267\'}', 1, 1, '2020-05-29 10:47:53');
INSERT INTO `t_message_log` VALUES (1266199755113496577, '13678666356', '{\'code\':\'347949\'}', 1, 1, '2020-05-29 10:48:41');

SET FOREIGN_KEY_CHECKS = 1;
