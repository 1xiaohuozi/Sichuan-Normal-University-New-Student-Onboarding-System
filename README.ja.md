# 四川師範大学 新生オンボーディングシステム

[English](README.md) | [日本語](README.ja.md) | [中文](README.zh-CN.md)

四川師範大学の新入生受付業務を想定した、Web ベースの新入生オンボーディング・管理システムです。学生側の手続き、管理者側の設定、業務処理、通知、寮選択、支払い、グリーンチャネル、進捗統計などを扱います。

## スクリーンショット

| ログイン | 学生手続きフロー |
| --- | --- |
| <img src="assets/登录页面.png" alt="ログイン画面" width="100%"> | <img src="assets/学生流程页面.png" alt="学生手続きフロー" width="100%"> |

| 寮選択 | お知らせ |
| --- | --- |
| <img src="assets/宿舍选择页面.png" alt="寮選択画面" width="100%"> | <img src="assets/公告页面.png" alt="お知らせ画面" width="100%"> |

| 管理画面 | 統計 |
| --- | --- |
| <img src="assets/管理员页面.png" alt="管理画面" width="100%"> | <img src="assets/统计页面.png" alt="統計画面" width="100%"> |

## 主な機能

- 新生オンボーディング情報とお知らせの公開
- 学生区分ごとの手続きフローとステップ関係の設定
- 学院・業務部門による共同処理
- 現地支払い手続きの記録
- 経済的に困難な学生向けのグリーンチャネル処理
- 寮情報管理と学生の寮選択
- オンボーディング進捗確認と学院別統計
- 一部データの Excel 出力
- システムアクセスログの監視
- メニュー、ユーザー、ロール、権限管理
- CAPTCHA、JWT 認証、パスワード再設定、メール認証

## 技術スタック

### フロントエンド

- Vue 2.6
- Vue CLI
- Vue Router
- Vuex
- Element UI
- Axios
- npm

### バックエンド

- Java 8
- Spring Boot 2.7.0
- Spring Security / JWT
- MyBatis-Plus 3.4.1
- MySQL
- Redis
- Swagger 2.9.2
- EasyExcel
- Maven

## リポジトリ構成

```text
.
├── assets/                         # 画面スクリーンショット
├── frontend/vueadmin-vue/          # Vue フロントエンドのソースコード
├── backend/sys_newwelcome/         # Spring Boot バックエンド
└── database-sql/adsad2.sql         # データベース初期化 SQL
```

レビューしやすいように、トップレベルのディレクトリ名は英語に統一しています。`dist`、`target`、`node_modules`、エディタ設定、ネストされた Git メタデータは意図的に除外しています。

## セットアップ

### 必要環境

- JDK 8
- Maven
- MySQL
- Redis
- Node.js と npm

### 1. データベースの初期化

MySQL データベースを作成し、次の SQL をインポートします。

```text
database-sql/adsad2.sql
```

バックエンド設定では `adsad2` というデータベース名が使われています。起動前にローカル環境に合わせてデータベース、Redis、メール設定を確認してください。

### 2. バックエンドの起動

```bash
cd backend/sys_newwelcome
mvn spring-boot:run
```

設定されているバックエンドポートは `8081` です。

### 3. フロントエンドの起動

```bash
cd frontend/vueadmin-vue
npm install
npm run serve
```

本番用ビルド:

```bash
npm run build
```

## 注意事項

- 設定ファイルはローカル開発向けです。デプロイ前に環境固有の値を確認し、必要に応じて変更してください。
- `assets/` 内の画像は既存システム画面のスクリーンショットです。
