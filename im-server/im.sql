/*
SQLyog Professional v12.08 (64 bit)
MySQL - 10.2.31-MariaDB : Database - im
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`im` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `im`;

/*Table structure for table `t_friend_req` */

DROP TABLE IF EXISTS `t_friend_req`;

CREATE TABLE `t_friend_req` (
  `req_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请加好友逻辑id',
  `user_id` bigint(20) NOT NULL COMMENT '申请人id',
  `friend_id` bigint(20) NOT NULL COMMENT '被申请人id',
  `status` int(2) NOT NULL COMMENT '申请状态 1申请中2 同意 3拒绝',
  `req_time` varchar(50) NOT NULL COMMENT '申请时间',
  `handle_time` varchar(50) DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`req_id`),
  KEY `idx_friend_req_userid` (`friend_id`),
  KEY `idx_friend_req_friendid` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_friend_ship` */

DROP TABLE IF EXISTS `t_friend_ship`;

CREATE TABLE `t_friend_ship` (
  `friend_ship_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id，逻辑主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `friend_id` bigint(20) NOT NULL COMMENT '朋友id',
  `remark_name` varchar(50) NOT NULL COMMENT '备注名称',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '朋友的原始名字，即昵称',
  `add_time_stamp` varchar(30) DEFAULT NULL COMMENT '加好友的时候的时间戳',
  `status` int(2) DEFAULT NULL COMMENT '状态1 有效好友 2 黑名单 3删除',
  `level` int(10) DEFAULT NULL COMMENT '友好等级，聊天越多越高',
  PRIMARY KEY (`friend_ship_id`),
  UNIQUE KEY `idx_firnedship_unique` (`user_id`,`friend_id`),
  KEY `idx_friendship_friendid` (`friend_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_login_log` */

DROP TABLE IF EXISTS `t_login_log`;

CREATE TABLE `t_login_log` (
  `login_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `token` varchar(100) NOT NULL COMMENT '登录token',
  `status` int(2) NOT NULL COMMENT '状态1 有效 2失效，只能有一个为1，登录成功之后先把老的置为2',
  `login_time_stamp` varchar(30) NOT NULL COMMENT '登录时间戳',
  PRIMARY KEY (`login_log_id`),
  UNIQUE KEY `idx_login_log` (`login_log_id`,`login_time_stamp`),
  KEY `idx_login_token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_msg` */

DROP TABLE IF EXISTS `t_msg`;

CREATE TABLE `t_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `msg_id` varchar(80) NOT NULL COMMENT '消息唯一id，这个是客户端生成的',
  `user_id` bigint(20) NOT NULL COMMENT '用户id，消息是属于谁的',
  `type` int(2) NOT NULL COMMENT '类型，1 单聊 2 群聊',
  `content` text NOT NULL COMMENT '消息内容，单聊或者群聊的base64编码',
  `add_time` varchar(30) NOT NULL COMMENT '入库时间',
  `srl_no` bigint(20) NOT NULL COMMENT '消息序号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_msg_id` (`msg_id`),
  KEY `idx_msg_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表，注意这个需要分库分表';

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `password` varchar(100) NOT NULL COMMENT '用户密码',
  `salt` varchar(20) NOT NULL COMMENT '密码盐值',
  `mobile_phone` varchar(20) NOT NULL COMMENT '用户手机号',
  `srl_no` bigint(20) NOT NULL COMMENT '用户的消息序号',
  `read_srl_no` bigint(20) DEFAULT NULL COMMENT '用户已读消息序号',
  `update_time_stamp` varchar(30) DEFAULT NULL COMMENT '更新时间戳',
  `add_time_stamp` varchar(30) DEFAULT NULL COMMENT '更新时间戳',
  `invite_code` varchar(10) DEFAULT NULL COMMENT '用户邀请码',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_user_phone` (`mobile_phone`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `t_user_attribute` */

DROP TABLE IF EXISTS `t_user_attribute`;

CREATE TABLE `t_user_attribute` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `sex` int(2) DEFAULT NULL COMMENT '用户性别1男 2 女',
  `birthday` varchar(20) DEFAULT NULL COMMENT '用户生日',
  `id_card` varchar(30) DEFAULT NULL COMMENT '用户身份证号',
  `real_name` varchar(30) DEFAULT NULL COMMENT '用户真实姓名',
  `head_icon` varchar(200) DEFAULT NULL COMMENT '用户头像地址',
  `nick_name` varchar(100) NOT NULL COMMENT '用户昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '用户邮箱，貌似没啥用',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
