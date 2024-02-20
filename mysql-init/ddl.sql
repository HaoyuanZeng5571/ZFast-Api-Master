-- 创建库
create database if not exists api;

-- 切换库
use api;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    `accessKey` varchar(512) not null comment 'accessKey',
    `secretKey` varchar(512) not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户';

-- 接口信息
create table if not exists api.`interface_info`
(
    `id`                bigint not null auto_increment comment '主键' primary key,
    `name`              varchar(256) not null comment '名称',
    `description`       varchar(256) null comment '描述',
    `url`               varchar(512) not null comment '接口地址',
     requestParams      text null comment '请求参数',
     responseParams     text null comment '接口响应参数',
    `requestHeader`     text null comment '请求头',
    `responseHeader`    text null comment '响应头',
     returnFormat       varchar(512) default 'JSON' null comment '返回格式(JSON等等)',
    `status`            int default 0 not null comment '接口状态（0-默认下线，1-上线）',
    `method`            varchar(256) not null comment '请求方法',
    `userId`            bigint not null comment '创建人',
    totalInvokes        bigint default 0 not null comment '接口总调用次数',
    `createTime`        datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`          tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息';


-- 用户调用接口关系表
create table if not exists api.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalInvokes` int default 0 not null comment '总调用次数',
    `status` int default 0 not null comment '0-正常，1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';
