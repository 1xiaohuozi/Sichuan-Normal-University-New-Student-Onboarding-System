# Sichuan Normal University New Student Onboarding System

[English](README.md) | [日本語](README.ja.md) | [简体中文](README.zh-CN.md)

A web-based onboarding service and administration system designed around the new-student registration process at Sichuan Normal University. It supports student-facing onboarding tasks as well as administrative configuration, processing, and progress monitoring.

## Screenshots

| Login | Student onboarding process |
| --- | --- |
| <img src="assets/登录页面.png" alt="Login page" width="100%"> | <img src="assets/学生流程页面.png" alt="Student onboarding process" width="100%"> |

| Dormitory selection | Notices |
| --- | --- |
| <img src="assets/宿舍选择页面.png" alt="Dormitory selection page" width="100%"> | <img src="assets/公告页面.png" alt="Notice page" width="100%"> |

| Administration | Statistics |
| --- | --- |
| <img src="assets/管理员页面.png" alt="Administration page" width="100%"> | <img src="assets/统计页面.png" alt="Statistics page" width="100%"> |

## Features

- Publish onboarding information and allow students to view notices.
- Configure registration processes and the relationships between onboarding steps.
- Support cross-department processing of onboarding tasks.
- Record on-site payment procedures.
- Provide a green channel for students with financial difficulties.
- Manage dormitory information and student dormitory selection.
- Track onboarding progress and generate institute-level statistics.
- Export selected onboarding data to Excel.
- Monitor system access logs.
- Manage menus, users, roles, and permissions.
- Support CAPTCHA, JWT-based authentication, password recovery, and email verification.

## Technology Stack

### Frontend

- Vue 3
- npm

### Backend

- Java 8
- Spring Boot 2.7.0
- Spring Security and JWT
- MyBatis-Plus 3.4.1
- MySQL
- Redis
- Swagger 2.9.2
- EasyExcel
- Maven

## Repository Structure

```text
.
├── assets/                         # Project screenshots
├── 前端/vueadmin-vue/              # Frontend entry recorded as a Git link
├── 后端/sys_newwelcome/            # Spring Boot backend
└── 数据库SQL脚本/adsad2.sql         # Database initialization script
```

> The current `main` branch records `前端/vueadmin-vue` as a Git link without an available submodule URL. If the directory is empty after cloning, obtain the frontend source before running the npm commands below.

## Getting Started

### Prerequisites

- JDK 8
- Maven
- MySQL
- Redis
- Node.js and npm for the frontend

### 1. Initialize the database

Create a MySQL database and import:

```text
数据库SQL脚本/adsad2.sql
```

The included backend configuration expects a database named `adsad2`. Review the local database, Redis, and email settings before starting the application.

### 2. Run the backend

```bash
cd 后端/sys_newwelcome
mvn spring-boot:run
```

The configured backend port is `8081`.

### 3. Run the frontend

After obtaining the frontend source:

```bash
cd 前端/vueadmin-vue
npm install
npm run serve
```

Build the frontend for production with:

```bash
npm run build
```

## Notes

- This repository contains configuration files intended for local development. Review and replace environment-specific values before deployment.
- The screenshots under `assets/` document the existing interface.
