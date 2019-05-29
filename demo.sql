/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50711
Source Host           : localhost:3306
Source Database       : demo

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2019-05-29 17:53:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for role_perm
-- ----------------------------
DROP TABLE IF EXISTS `role_perm`;
CREATE TABLE `role_perm` (
  `id` varchar(36) NOT NULL,
  `perm_id` varchar(32) DEFAULT NULL COMMENT '权限id',
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
-- Records of role_perm
-- ----------------------------
INSERT INTO `role_perm` VALUES ('1', '1', '1');
INSERT INTO `role_perm` VALUES ('2', '2', '1');
INSERT INTO `role_perm` VALUES ('3', '1', '2');
INSERT INTO `role_perm` VALUES ('4', '2', '2');
INSERT INTO `role_perm` VALUES ('5', '3', '2');
INSERT INTO `role_perm` VALUES ('6', '4', '2');

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '日志信息描述',
  `method` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `log_type` varchar(10) DEFAULT NULL COMMENT '日志类型 0是正常，1是异常',
  `request_ip` varchar(30) DEFAULT NULL COMMENT '请求的ip',
  `exception_code` varchar(50) DEFAULT NULL COMMENT '异常错误码',
  `exception_detail` varchar(255) DEFAULT NULL COMMENT '异常详情',
  `params` varchar(1000) DEFAULT NULL COMMENT '请求参数',
  `user_id` varchar(32) DEFAULT NULL COMMENT '请求的用户id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志表';

-- ----------------------------
-- Records of system_log
-- ----------------------------
INSERT INTO `system_log` VALUES ('18532d71a7994283803d6086051b00c2', '登录', 'class com.ymc.iotthings.controller.UserInfoController.login', '0', '58.247.251.226', null, null, '{\"this\":\"\\\"ymc\\\"\",\"userName\":\"\\\"123456\\\"\"}', '1', '2019-05-29 16:40:25');
INSERT INTO `system_log` VALUES ('37834ff0c55446bb86a66a1504b34eaa', '查询用户findAll', 'class com.ymc.iotthings.controller.UserInfoController.selectAll', '0', '58.247.251.226', null, null, '{\"size\":\"0\",\"page\":\"0\"}', '', '2019-05-29 17:50:12');
INSERT INTO `system_log` VALUES ('62c15a659db348c18db88e46e72d234a', '登录', 'class com.ymc.iotthings.controller.UserInfoController.login', '0', '58.247.251.226', null, null, '{\"this\":\"\\\"ymc\\\"\",\"userName\":\"\\\"123456\\\"\"}', '', '2019-05-29 16:39:19');
INSERT INTO `system_log` VALUES ('62e57a72122147d2919f4be50149b3f8', '登录', 'class com.ymc.iotthings.controller.UserInfoController.login', '0', '58.247.251.226', null, null, '{\"this\":\"\\\"ymc\\\"\",\"userName\":\"\\\"123456\\\"\"}', '', '2019-05-29 17:50:16');
INSERT INTO `system_log` VALUES ('b2e1aa3340264b7d8a1f52f8235866eb', '登录', 'class com.ymc.iotthings.controller.UserInfoController.login', '0', '58.247.251.226', null, null, '{\"this\":\"\\\"ymc\\\"\",\"userName\":\"\\\"123456\\\"\"}', '', '2019-05-29 17:43:10');

-- ----------------------------
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_perm`;
CREATE TABLE `sys_perm` (
  `id` varchar(36) NOT NULL,
  `perm_name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `perm_desc` varchar(255) DEFAULT NULL COMMENT '权限描述',
  `perm_value` varchar(255) DEFAULT NULL COMMENT '权限值',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_disable` int(1) DEFAULT NULL COMMENT '是否禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_perm
-- ----------------------------
INSERT INTO `sys_perm` VALUES ('1', '创建', '创建权限', 'create', '2018-05-26 00:39:16', null, '0');
INSERT INTO `sys_perm` VALUES ('2', '删除', '删除权限', 'delete', '2018-05-26 00:39:39', null, '0');
INSERT INTO `sys_perm` VALUES ('3', '修改', '修改权限', 'update', '2018-05-26 00:39:58', null, '0');
INSERT INTO `sys_perm` VALUES ('4', '查询', '查询权限', 'select', '2018-05-26 00:40:16', null, '0');

-- ----------------------------
-- Table structure for sys_permission_init
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_init`;
CREATE TABLE `sys_permission_init` (
  `id` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT '程序对应url地址',
  `permission_init` varchar(255) DEFAULT NULL COMMENT '对应shiro权限',
  `sort` int(100) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_permission_init
-- ----------------------------
INSERT INTO `sys_permission_init` VALUES ('1', '/userInfo/login', 'anon', '1');
INSERT INTO `sys_permission_init` VALUES ('2', '/userInfo/selectAll', 'anon', '2');
INSERT INTO `sys_permission_init` VALUES ('3', '/logout', 'anon', '3');
INSERT INTO `sys_permission_init` VALUES ('4', '/**', 'authc', '0');
INSERT INTO `sys_permission_init` VALUES ('5', '/userInfo/selectAlla', 'authc, roles[admin]', '6');
INSERT INTO `sys_permission_init` VALUES ('6', '/sysPermissionInit/aaa', 'anon', '5');
INSERT INTO `sys_permission_init` VALUES ('7', '/userInfo/list', 'anon', '4');
INSERT INTO `sys_permission_init` VALUES ('8', '/uploadFile/upload', 'anon', '7');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(36) NOT NULL COMMENT '角色名称',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名称，用于显示',
  `role_desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `role_value` varchar(255) DEFAULT NULL COMMENT '角色值，用于权限判断',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_disable` int(1) DEFAULT NULL COMMENT '是否禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '财务', '负责发工资', 'cw', '2018-05-26 00:37:52', null, '0');
INSERT INTO `sys_role` VALUES ('2', '人事', '负责员工', 'rs', '2018-05-26 00:38:18', null, '0');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` varchar(32) NOT NULL,
  `user_name` varchar(255) NOT NULL COMMENT '姓名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `salt` varchar(255) DEFAULT NULL COMMENT '加密盐',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('1', 'ymc', '607d1229e4cdac6a7a2628f22498ee3c', 'wxKYXuTPST5SG0jMQzVPsg==');
INSERT INTO `user_info` VALUES ('2', 'ymc11', '', 'wxKYXuTPST5SG0jMQzVPsg==');
INSERT INTO `user_info` VALUES ('3', 'ymc22', '', 'wxKYXuTPST5SG0jMQzVPsg==');
INSERT INTO `user_info` VALUES ('4', 'ymc33', '', 'wxKYXuTPST5SG0jMQzVPsg==');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL COMMENT '用户ID',
  `role_id` varchar(36) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1', '1');
INSERT INTO `user_role` VALUES ('2', '1', '2');
