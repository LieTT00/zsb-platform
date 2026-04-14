# 专升本一站式学习平台

> 基于 **SpringBoot 3.x + Vue 3 + Vite + Element Plus** 构建的专升本备考交流平台

---

## 📁 项目结构

```
zsb-platform/
├── frontend/                   # 前端项目（Vue3 + Vite）
│   ├── index.html
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── main.js             # 入口文件
│       ├── App.vue
│       ├── router/             # Vue Router 路由配置
│       ├── store/              # Pinia 状态管理
│       ├── api/                # Axios 封装 + 接口定义
│       ├── assets/             # 全局样式
│       ├── components/
│       │   └── Layout.vue      # 主布局（导航栏 + 页脚）
│       └── views/
│           ├── Login.vue       # 登录页
│           ├── Register.vue    # 注册页
│           ├── Home.vue        # 首页（帖子广场）
│           ├── CreatePost.vue  # 发帖页
│           ├── PostDetail.vue  # 帖子详情页
│           ├── Profile.vue     # 个人中心
│           └── Stats.vue       # 数据统计仪表盘
│
├── backend/                    # 后端项目（SpringBoot 3）
│   ├── pom.xml
│   └── src/main/
│       ├── resources/
│       │   └── application.yml # 应用配置
│       └── java/com/zsb/community/
│           ├── ZsbCommunityApplication.java  # 启动类
│           ├── controller/     # 接口层
│           │   ├── UserController.java
│           │   ├── PostController.java
│           │   └── StatsController.java
│           ├── service/        # 服务层接口
│           ├── service/impl/   # 服务层实现
│           ├── mapper/         # MyBatis-Plus Mapper
│           ├── entity/         # 数据库实体
│           ├── dto/            # 请求/响应 DTO
│           ├── config/         # 配置类（CORS、MVC等）
│           ├── interceptor/    # JWT认证拦截器
│           └── util/           # 工具类（JwtUtil）
│
└── database/
    └── init.sql               # 数据库初始化脚本
```

---

## 🚀 快速启动

### 前置条件
- **Node.js** 18+ 
- **Java** 17+
- **Maven** 3.6+
- **MySQL** 8.x
- **Redis** 6+ 

### 一、初始化数据库

```sql
-- 在 MySQL 中执行
source /path/to/zsb-platform/database/init.sql
```

### 二、配置后端

修改 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/zsb_community?...
    username: root
    password: 你的MySQL密码
  redis:
    host: localhost
    port: 6379
```

### 三、启动后端

```bash
cd backend
mvn spring-boot:run
# 或在 IntelliJ IDEA 中直接运行 ZsbCommunityApplication.java
```

后端服务默认运行在 `http://localhost:8080`

### 四、启动前端

```bash
cd frontend
npm install
npm run dev
# 前端运行在 http://localhost:5173
```

---

## 🎯 功能模块

| 模块 | 功能点 |
|------|--------|
| **用户模块** | 注册、登录（JWT认证）、密码MD5加密、个人信息管理、修改密码 |
| **帖子模块** | 发帖（支持Markdown）、编辑、删除、分页查询、院校筛选、多维排序 |
| **互动模块** | 帖子浏览量统计、点赞/取消点赞（Redis防重）、评论发布 |
| **统计模块** | 总用户数、月活用户、活跃率、帖子趋势图（ECharts可视化） |
| **权限控制** | 未登录无法访问主页和发帖，用户只能操作自身数据 |

---

## 🔑 演示账号

| 用户名 | 密码 | 说明 |
|--------|------|------|
| admin | 123456 | 管理员账号 |
| test_user | 123456 | 普通用户 |

---

## 🛠 技术栈

### 前端
- **Vue 3** + `<script setup>` Composition API
- **Vite** 构建工具
- **Vue Router 4** 路由管理
- **Pinia** 状态管理
- **Element Plus** UI组件库
- **ECharts** 数据可视化
- **Axios** HTTP请求

### 后端
- **Spring Boot 3.2**
- **MyBatis-Plus 3.5** ORM
- **JWT（jjwt 0.12）** 身份认证
- **Redis** 缓存热点数据（浏览量、点赞去重）
- **Hutool** 工具库（MD5加密等）
- **MySQL 8** 业务数据存储

---

## 📡 API 接口文档

### 用户接口

| Method | Path | 描述 | 认证 |
|--------|------|------|------|
| POST | `/api/user/register` | 用户注册 | 否 |
| POST | `/api/user/login` | 用户登录 | 否 |
| GET | `/api/user/info` | 获取当前用户信息 | ✅ |
| PUT | `/api/user/info` | 更新用户信息 | ✅ |
| PUT | `/api/user/password` | 修改密码 | ✅ |

### 帖子接口

| Method | Path | 描述 | 认证 |
|--------|------|------|------|
| GET | `/api/post/list` | 分页查询帖子列表 | 否 |
| GET | `/api/post/{id}` | 获取帖子详情 | 否 |
| POST | `/api/post` | 发布帖子 | ✅ |
| PUT | `/api/post/{id}` | 修改帖子 | ✅ |
| DELETE | `/api/post/{id}` | 删除帖子 | ✅ |
| POST | `/api/post/{id}/like` | 点赞/取消 | ✅ |
| GET | `/api/post/my` | 我的帖子 | ✅ |

### 统计接口

| Method | Path | 描述 | 认证 |
|--------|------|------|------|
| GET | `/api/stats/overview` | 总览数据 | ✅ |
| GET | `/api/stats/monthly` | 月度趋势 | ✅ |
| GET | `/api/stats/post-trend` | 帖子趋势 | ✅ |

---

## 📌 注意事项

1. 请在 `application.yml` 中修改 `jwt.secret` 为更复杂的随机字符串（生产环境）
2. Redis 默认不需要密码，若有密码请在配置中设置
3. 前端演示模式下（无后端），可直接用 `admin/123456` 登录体验
4. 生产部署时请开启 HTTPS，并配置 Nginx 反向代理
