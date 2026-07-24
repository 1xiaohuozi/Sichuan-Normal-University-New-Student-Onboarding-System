# 四川师范大学数字迎新服务系统

[English](README.md) | [日本語](README.ja.md) | [简体中文](README.zh-CN.md)

这是一个根据四川师范大学新生报到流程开发的 Web 迎新服务与后台管理系统，覆盖学生在线办理、管理人员配置、业务处理和迎新进度统计等场景。

## 项目截图

| 登录页面 | 学生迎新流程 |
| --- | --- |
| <img src="assets/登录页面.png" alt="登录页面" width="100%"> | <img src="assets/学生流程页面.png" alt="学生迎新流程页面" width="100%"> |

| 宿舍选择 | 公告信息 |
| --- | --- |
| <img src="assets/宿舍选择页面.png" alt="宿舍选择页面" width="100%"> | <img src="assets/公告页面.png" alt="公告页面" width="100%"> |

| 管理后台 | 数据统计 |
| --- | --- |
| <img src="assets/管理员页面.png" alt="管理员页面" width="100%"> | <img src="assets/统计页面.png" alt="统计页面" width="100%"> |

## 主要功能

- 发布迎新相关信息，供学生查询公告。
- 配置不同类别学生的报到流程及流程项目之间的关系。
- 支持院系和业务部门协同办理迎新事务。
- 登记新生现场缴费手续。
- 为经济困难学生提供绿色通道。
- 管理宿舍信息并支持学生选择宿舍。
- 查询迎新进度并按学院进行数据统计。
- 将部分迎新数据导出为 Excel。
- 监控系统访问日志。
- 管理栏目、用户、角色和权限。
- 支持验证码、JWT 身份认证、密码找回和邮件验证。

## 技术栈

### 前端

- Vue 3
- npm

### 后端

- Java 8
- Spring Boot 2.7.0
- Spring Security 与 JWT
- MyBatis-Plus 3.4.1
- MySQL
- Redis
- Swagger 2.9.2
- EasyExcel
- Maven

## 仓库结构

```text
.
├── assets/                         # 项目截图
├── 前端/vueadmin-vue/              # 当前以 Git 链接形式记录的前端入口
├── 后端/sys_newwelcome/            # Spring Boot 后端
└── 数据库SQL脚本/adsad2.sql         # 数据库初始化脚本
```

> 当前 `main` 分支中的 `前端/vueadmin-vue` 是一个没有可用子模块地址的 Git 链接。如果克隆后该目录为空，需要先取得前端源码，再执行下面的 npm 命令。

## 快速开始

### 环境要求

- JDK 8
- Maven
- MySQL
- Redis
- 用于前端的 Node.js 和 npm

### 1. 初始化数据库

创建 MySQL 数据库并导入：

```text
数据库SQL脚本/adsad2.sql
```

仓库中的后端配置默认使用名为 `adsad2` 的数据库。启动前请根据本地环境检查数据库、Redis 和邮件配置。

### 2. 启动后端

```bash
cd 后端/sys_newwelcome
mvn spring-boot:run
```

后端配置端口为 `8081`。

### 3. 启动前端

取得前端源码后执行：

```bash
cd 前端/vueadmin-vue
npm install
npm run serve
```

生产环境打包：

```bash
npm run build
```

## 注意事项

- 仓库中的配置文件面向本地开发环境，部署前应检查并替换与具体环境相关的配置值。
- `assets/` 中的图片来自现有项目界面。
