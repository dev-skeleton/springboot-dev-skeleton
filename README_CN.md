# springboot-dev-skeleton

## 开发环境

+ IDEA 2021.2
+ gradle

## 开始使用

### 准备数据库
+ 安装 MySQL 数据库
+ 创建 database，名称为 skeleton: `create database skeleton default charset=utf8mb4 collate utf8mb4_general_ci;`
+ 创建用户: `grant all privileges on skeleton.* to skeleton@'%' identified by 'password4skeleton';`;

### 启动应用
+ IDE 右键 `com.example.skeleton.SkeletonApplication` 启动应用
+ 访问 [Swagger UI](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

## TIPS

### maven
+ 当前版本的 gradle 需要 maven 仓库或者代理服务器使用 https，[参考](https://support.sonatype.com/hc/en-us/articles/360041287334)