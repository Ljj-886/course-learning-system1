# 课程学习系统后端

基于 **Spring Boot + Spring Data JPA + MySQL** 的课程学习系统后端，为课程学习小程序提供 RESTful 接口，覆盖用户登录注册、课程管理、章节学习、班级管理与成绩计算等模块。

## ✨ 功能接口

**用户**
- 登录 / 注册 / 检查登录态
- 修改昵称 / 修改密码

**课程**
- 课程列表 / 全部课程
- 新增课程 / 更新课程

**章节学习**
- 章节列表 / 章节内容 / 章节学习记录

**班级与成绩**
- 用户班级绑定
- 用户成绩计算

**微信支持**
- 用户表含 `wx_openid` 字段，支持微信小程序端登录接入

## 🛠 技术栈

- Java 8 · Spring Boot · Spring Data JPA（Hibernate）
- MySQL（`ddl-auto=update` 自动建表/更新）
- fastjson

## 🚀 快速开始

1. 用 IDEA 打开项目（Maven 工程，自动拉取依赖）
2. 修改 `src/main/resources/application.properties` 中的数据库连接（默认 `localhost:3306/develop`）
3. 运行 `com.restservice.DemoApplication`，服务端口 `5000`

## 📁 目录结构

```
src/main/java/com/
├── example/
│   ├── entities/     # 实体：User / Clazz 班级 / Chapter 章节 / Knowlege 知识点 / UserClass
│   ├── repos/        # Spring Data JPA Repository
│   ├── service/      # 业务逻辑（IUserService / UserService）
│   └── tool/         # 工具类：Course / CourseRepository
└── restservice/      # 启动类 + GreetingController（全部接口入口）
```

## ⚠️ 说明

- 本仓库为学校课程项目**后端部分**，前端为微信小程序
- 数据库表结构由 Hibernate 根据实体自动维护，无需手动建表
- 仓库中数据库配置已改为本地占位值，请按需修改
