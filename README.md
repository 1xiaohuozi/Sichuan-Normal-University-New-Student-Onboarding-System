# Sichuan Normal University New Student Onboarding System

[English](README.md) | [日本語](README.ja.md) | [中文](README.zh-CN.md)

A web-based new student onboarding and administration system designed around the registration workflow of Sichuan Normal University. The project covers student-facing onboarding tasks, administrative configuration, workflow processing, notices, dormitory selection, payments, green-channel handling, and progress statistics.

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

- Publish onboarding information and notices for new students.
- Configure different onboarding workflows and step relationships.
- Support cross-department processing of onboarding tasks.
- Record on-site payment procedures.
- Provide green-channel handling for students with financial difficulties.
- Manage dormitory information and student dormitory selection.
- Track onboarding progress and generate institute-level statistics.
- Export selected onboarding data to Excel.
- Monitor access logs.
- Manage menus, users, roles, and permissions.
- Support CAPTCHA, JWT authentication, password recovery, and email verification.

## Technology Stack

### Frontend

- Vue 2.6
- Vue CLI
- Vue Router
- Vuex
- Element UI
- Axios
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
├── frontend/vueadmin-vue/          # Vue frontend source code
├── backend/sys_newwelcome/         # Spring Boot backend
└── database-sql/adsad2.sql         # Database initialization script
```

The repository uses English directory names for easier review. Generated folders such as `dist` and `target`, dependency folders such as `node_modules`, editor settings, and nested Git metadata are intentionally excluded.

## Getting Started

### Prerequisites

- JDK 8
- Maven
- MySQL
- Redis
- Node.js and npm

### 1. Initialize the database

Create a MySQL database and import:

```text
database-sql/adsad2.sql
```

The included backend configuration expects a database named `adsad2`. Review local database, Redis, and email settings before starting the application.

### 2. Run the backend

```bash
cd backend/sys_newwelcome
mvn spring-boot:run
```

The configured backend port is `8081`.

### 3. Run the frontend

```bash
cd frontend/vueadmin-vue
npm install
npm run serve
```

Build the frontend for production:

```bash
npm run build
```

## Notes

- Configuration files are intended for local development. Review and replace environment-specific values before deployment.
- Screenshots under `assets/` document the current user interface.
