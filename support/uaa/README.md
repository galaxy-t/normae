# 用户账号和认证

    项目标识: uaa
    默认端口: 8082

    提供根据用户名密码发放 token 的功能
    其它如组织架构等功能可以直接扩展本模块进行开发
    
## 概念
   
### 用户

    可以在系统进行登录的 , 拥有帐号密码的一个账号
    用户可以通过自行注册或后台管理员添加
    用户可以被禁用
    用户可以被逻辑删除 , 但不允许被物理删除
    
### 角色

    通常意义上的根业务有较为直接的联系的一个代号
    角色通常会绑定多个权限
    一个系统在开发完成之后角色可以直接被固定下来
    一个系统在开发完成之后角色可以被扩展(即可以动态的创建一个新的角色并为其关联权限)
    一个系统在开发完成之后默认可以没有角色 , 角色可以在实际使用的时候被创建
    
### 权限

    权限应该被定义为一个或者一件事情是否可以被操作称为权限
    软件(或系统)的每一次被使用者的操作可以理解为是一个权限
    从业务和开发的角度来看一个权限(即系统的使用者对软件的一次操作)可能会对应着多个后端接口的请求
    换言之一个接口可能会被多个权限标注也是可以的
    
### 用户和角色
    
    一个用户可以被授予(或默认)一个或多个角色
    默认分配的角色 : 若系统的用户可以自行进行注册 , 则系统需要为其默认分配角色
    
### 角色和权限

    一个角色是可以包含一个或多个权限的
    
### 用户和权限

    在某些情况下 , 需求方会要求除可以为用户分配角色外 , 还可以单独为用户分配某个或某些权限
    
## 使用

### 注册

    默认代码中仅提供"登录"方法 com.galaxyt.normae.uaa.service.LoginService.login(String username, String password)
    该方法返回的 LoginVo 中包含登录所需的一些基础信息 , 如: 用户 ID , 用户名 , 登录成功之后返回的令牌 , 令牌的到期时间(默认为两个小时) , 用户拥有的角色标识的集合 , 用户拥有的权限标识的集合等
    开发人员自行完善 Controller 层的代码可以依赖该方法实现登录
    若该登录功能不能满足实际开发需要 , 如返回值需要返回该用户所属部门 , 开发人员可自行修改该方法 , 或在登录 Controller 方法中进行单独调用(推荐)
    其它功能 , 如"注册" , 用户,角色,权限,组织架构等信息的增删改查操作等维护业务需要开发人员根据实际业务情况进行开发
    
## 数据库

    默认的数据库名为 normae_uaa , 实际开发人员可以自行定义该名称 , 并在配置文件中进行修改即可
    数据库中的表为作者根据已经解决过的实际业务整理出来的最精简表及字段 , 多数情况不足以支撑实际业务开发需要 , 开发人员需要自行扩展数据库及实体类
    
### 用户表 [t_user]
    
     CREATE TABLE `normae_uaa`.`t_user` (
     `id` bigint(20) NOT NULL,
     `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
     `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
     `disabled` int(1) NOT NULL COMMENT '是否禁用 1:是 0:否',
     `deleted` int(1) NOT NULL COMMENT '是否删除 1:是  0:否',
     `create_user_id` bigint(20) NOT NULL COMMENT '创建人 , 若为自行注册的用户创建人为其自己',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     `update_user_id` bigint(20) NOT NULL COMMENT ' 最后修改人',
     `update_time` datetime NOT NULL COMMENT '修改时间',
     PRIMARY KEY (`id`) ,
     UNIQUE INDEX `uk_username` (`username` ASC) USING BTREE COMMENT '用户名唯一索引' 
     )
     ENGINE = InnoDB
     AVG_ROW_LENGTH = 0
     DEFAULT CHARACTER SET = utf8mb4
     COLLATE = utf8mb4_0900_ai_ci
     COMMENT = '用户表'
     KEY_BLOCK_SIZE = 0
     MAX_ROWS = 0
     MIN_ROWS = 0
     ROW_FORMAT = Dynamic;
     
### 角色表 [t_role]

    CREATE TABLE `normae_uaa`.`t_role` (
    `id` bigint(20) NOT NULL,
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
    `mark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色标识',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
    `disabled` int(1) NOT NULL COMMENT '是否禁用   1:是  0:否',
    `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_user_id` bigint(20) NOT NULL COMMENT '修改人',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) ,
    UNIQUE INDEX `uk_mark` (`name` ASC) USING BTREE COMMENT '角色标识唯一索引' 
    )
    ENGINE = InnoDB
    AVG_ROW_LENGTH = 0
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '角色表'
    KEY_BLOCK_SIZE = 0
    MAX_ROWS = 0
    MIN_ROWS = 0
    ROW_FORMAT = Dynamic;

### 权限表 [t_authority]

    CREATE TABLE `normae_uaa`.`t_authority` (
    `id` bigint(20) NOT NULL,
    `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
    `mark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限标识',
    `disabled` int(1) NOT NULL COMMENT '是否禁用   1:是  0:否',
    `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_user_id` bigint(20) NOT NULL COMMENT '修改人',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) ,
    UNIQUE INDEX `uk_app_mark` (`mark` ASC) USING BTREE COMMENT '权限标识唯一索引' 
    )
    ENGINE = InnoDB
    AVG_ROW_LENGTH = 0
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '权限表'
    KEY_BLOCK_SIZE = 0
    MAX_ROWS = 0
    MIN_ROWS = 0
    ROW_FORMAT = Dynamic;
    
### 用户角色表 [t_user_role]

    CREATE TABLE `normae_uaa`.`t_user_role` (
    `id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL COMMENT '用户 id',
    `role_id` bigint(20) NOT NULL COMMENT '角色 id',
    PRIMARY KEY (`id`) ,
    UNIQUE INDEX `uk_userid_roleid` (`user_id` ASC, `role_id` ASC) USING BTREE COMMENT '用户id和角色id唯一索引' 
    )
    ENGINE = InnoDB
    AVG_ROW_LENGTH = 0
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '用户角色关联表'
    KEY_BLOCK_SIZE = 0
    MAX_ROWS = 0
    MIN_ROWS = 0
    ROW_FORMAT = Dynamic;

### 角色权限表 [t_role_authority]

    CREATE TABLE `normae_uaa`.`t_role_authority` (
    `id` bigint(20) NOT NULL,
    `role_id` bigint(20) NOT NULL COMMENT '角色 id',
    `authority_id` bigint(20) NOT NULL COMMENT '权限 id',
    PRIMARY KEY (`id`) ,
    UNIQUE INDEX `uk_roleid_authorityid` (`role_id` ASC, `authority_id` ASC) USING BTREE COMMENT '角色id和权限id唯一索引' 
    )
    ENGINE = InnoDB
    AVG_ROW_LENGTH = 0
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '角色权限关联表'
    KEY_BLOCK_SIZE = 0
    MAX_ROWS = 0
    MIN_ROWS = 0
    ROW_FORMAT = Dynamic;
    
### 用户权限表 [t_user_authority]

    CREATE TABLE `normae_uaa`.`t_user_authority` (
    `id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL COMMENT '用户主键 ID',
    `authority_id` bigint(20) NOT NULL COMMENT '权限主键 ID',
    PRIMARY KEY (`id`) 
    )
    ENGINE = InnoDB
    AVG_ROW_LENGTH = 0
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci
    KEY_BLOCK_SIZE = 0
    MAX_ROWS = 0
    MIN_ROWS = 0
    ROW_FORMAT = Dynamic;