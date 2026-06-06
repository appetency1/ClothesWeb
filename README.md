# ClothesWeb - 网上衣橱管理系统

## 一、项目简介

网上衣橱管理系统是一个基于 Java Servlet + Vue 3 的全栈 Web 应用，提供服装展示、购物车、订单管理、用户管理等功能。分为前台用户端和后台管理端。

### 功能模块

| 模块 | 功能 |
|------|------|
| 用户模块 | 注册、登录、个人信息管理 |
| 服装模块 | 服装分类浏览、详情查看、搜索筛选 |
| 购物车 | 添加商品、修改数量、删除商品 |
| 订单模块 | 下单、查看订单、订单状态跟踪 |
| 后台管理 | 商品管理、订单管理、用户管理 |

---

## 二、技术架构

### 2.1 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 17 | 开发语言 |
| Tomcat | 10 | Web 服务器 |
| Jakarta Servlet | 6.1.0 | Web 容器接口 |
| MySQL | 9.1.0 | 关系型数据库 |
| HikariCP | 5.1.0 | 数据库连接池 |
| Gson | 2.10.1 | JSON 序列化 |
| BCrypt | 0.4 | 密码加密 |
| SLF4J | 2.0.16 | 日志框架 |

### 2.2 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5.34 | 前端框架 |
| Vue Router | 4.6.4 | 路由管理 |
| Element Plus | 2.14.1 | UI 组件库 |
| Axios | 1.17.0 | HTTP 请求 |
| Vite | 8.0.12 | 构建工具 |

### 2.3 架构图

```
┌─────────────────────────────────────────────────────────┐
│                        前端层                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  用户端 (Vue) │  │ 管理端 (Vue)  │  │ Element Plus │  │
│  └──────┬───────┘  └──────┬───────┘  └──────────────┘  │
└─────────┼─────────────────┼───────────────────────────┘
          │                 │
          └────────┬────────┘
                   │ HTTP/JSON
┌──────────────────┼──────────────────────────────────────┐
│                  ▼                                      │
│  ┌─────────────────────────────────────────────────┐   │
│  │              控制层 (Servlet)                     │   │
│  │  UserServlet / ClothesServlet / CartServlet      │   │
│  │  OrderServlet / AdminServlet / UploadServlet     │   │
│  └────────────────────┬────────────────────────────┘   │
│                       │                                 │
│  ┌────────────────────┼────────────────────────────┐   │
│  │              业务层 (Service)                     │   │
│  │  UserService / ClothesService / CartService      │   │
│  │  OrderService / SizeService / TypeService        │   │
│  └────────────────────┬────────────────────────────┘   │
│                       │                                 │
│  ┌────────────────────┼────────────────────────────┐   │
│  │              数据层 (DAO)                         │   │
│  │  UserDAO / ClothesDAO / CartDAO / OrderDAO       │   │
│  └────────────────────┬────────────────────────────┘   │
│                       │                                 │
│  ┌────────────────────┼────────────────────────────┐   │
│  │           数据库 (MySQL)                          │   │
│  │  t_user / t_clothes / t_cart / t_order           │   │
│  │  t_type / t_size                                  │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

---

## 三、项目结构

```
ClothesWebProject/
├── ClothesWeb/                          # 后端项目
│   ├── src/main/java/org/example/clothesweb/
│   │   ├── servlet/                     # 控制器层
│   │   │   ├── UserServlet.java         # 用户接口 (/api/user/*)
│   │   │   ├── ClothesServlet.java      # 服装接口 (/api/clothes/*)
│   │   │   ├── CartServlet.java         # 购物车接口 (/api/cart/*)
│   │   │   ├── OrderServlet.java        # 订单接口 (/api/order/*)
│   │   │   ├── AdminServlet.java        # 管理员接口 (/api/admin/*)
│   │   │   ├── TypeServlet.java         # 分类接口 (/api/type/*)
│   │   │   ├── SizeServlet.java         # 尺码接口 (/api/size/*)
│   │   │   └── UploadServlet.java       # 文件上传接口 (/api/upload)
│   │   ├── service/                     # 业务逻辑层
│   │   │   ├── UserService.java         # 用户业务
│   │   │   ├── ClothesService.java      # 服装业务
│   │   │   ├── CartService.java         # 购物车业务
│   │   │   ├── OrderService.java        # 订单业务
│   │   │   ├── TypeService.java         # 分类业务
│   │   │   └── SizeService.java         # 尺码业务
│   │   ├── dao/                         # 数据访问层
│   │   │   ├── UserDAO.java             # 用户数据访问
│   │   │   ├── ClothesDAO.java          # 服装数据访问
│   │   │   ├── CartDAO.java             # 购物车数据访问
│   │   │   ├── OrderDAO.java            # 订单数据访问
│   │   │   ├── TypeDAO.java             # 分类数据访问
│   │   │   └── SizeDAO.java             # 尺码数据访问
│   │   ├── entity/                      # 实体类
│   │   │   ├── User.java                # 用户实体
│   │   │   ├── Clothes.java             # 服装实体
│   │   │   ├── Cart.java                # 购物车实体
│   │   │   ├── Order.java               # 订单实体
│   │   │   ├── Type.java                # 分类实体
│   │   │   └── Size.java                # 尺码实体
│   │   ├── filter/                      # 过滤器
│   │   │   ├── AuthFilter.java          # 认证过滤器
│   │   │   └── CorsFilter.java          # 跨域过滤器
│   │   └── util/                        # 工具类
│   │       ├── DBUtil.java              # 数据库连接工具
│   │       ├── JsonResult.java          # 统一响应封装
│   │       ├── PasswordUtil.java        # 密码加密工具
│   │       └── TokenUtil.java           # JWT Token工具
│   ├── src/main/resources/
│   │   └── db.properties                # 数据库配置
│   ├── src/main/webapp/
│   │   └── WEB-INF/web.xml              # Web配置
│   ├── database.sql                     # 数据库初始化脚本
│   └── pom.xml                          # Maven配置
│
├── clothes-frontend/                    # 前端项目
│   ├── src/
│   │   ├── views/                       # 页面视图
│   │   │   ├── Home.vue                 # 首页
│   │   │   ├── ProductDetail.vue        # 商品详情
│   │   │   ├── Cart.vue                 # 购物车
│   │   │   ├── Login.vue                # 登录/注册
│   │   │   ├── Orders.vue               # 订单列表
│   │   │   ├── Profile.vue              # 个人中心
│   │   │   └── admin/                   # 后台管理页面
│   │   │       ├── AdminLogin.vue       # 管理员登录
│   │   │       ├── AdminLayout.vue      # 管理后台布局
│   │   │       ├── ProductManage.vue    # 商品管理
│   │   │       ├── OrderManage.vue      # 订单管理
│   │   │       └── UserManage.vue       # 用户管理
│   │   ├── components/                  # 公共组件
│   │   │   ├── NavBar.vue               # 导航栏
│   │   │   ├── FooterBar.vue            # 底部栏
│   │   │   └── HelloWorld.vue           # 示例组件
│   │   ├── api/                         # API接口封装
│   │   │   ├── request.js               # 请求基础封装
│   │   │   ├── user.js                  # 用户接口
│   │   │   ├── clothes.js               # 服装接口
│   │   │   ├── cart.js                  # 购物车接口
│   │   │   ├── order.js                 # 订单接口
│   │   │   └── type.js                  # 分类接口
│   │   ├── router/
│   │   │   └── index.js                 # 路由配置
│   │   ├── data/
│   │   │   └── mockProducts.js          # 模拟数据
│   │   ├── styles/                      # 样式文件
│   │   │   ├── global.css               # 全局样式
│   │   │   └── variables.css            # CSS变量
│   │   ├── main.js                      # 入口文件
│   │   └── App.vue                      # 根组件
│   ├── index.html                       # HTML模板
│   ├── package.json                     # 依赖配置
│   ├── vite.config.js                   # Vite配置(用户端)
│   └── vite.admin.config.js             # Vite配置(管理端)
│
├── 设计文档.md                           # 系统设计文档
├── 项目需求.md                           # 需求文档
├── 验收标准.md                           # 验收标准
└── README.md                            # 本文件
```

---

## 四、环境要求

| 环境 | 版本要求 |
|------|----------|
| JDK | 17+ |
| MySQL | 8.0+ |
| Maven | 3.8+ |
| Node.js | 18+ |
| npm | 9+ |

---

## 五、快速开始

### 5.1 数据库初始化

1. 安装 MySQL 并启动服务
2. 使用 root 用户登录（密码：000000）
3. 执行初始化脚本：

```bash
mysql -u root -p < ClothesWeb/database.sql
```

数据库配置在 `ClothesWeb/src/main/resources/db.properties`：

```properties
db.url=jdbc:mysql://localhost:3306/wardrobe
db.username=root
db.password=000000
```

如需修改数据库密码，请同时修改此配置文件。

### 5.2 启动后端服务

```bash
cd ClothesWeb

# 方式一：使用 Maven Tomcat 插件（推荐开发）
mvn tomcat7:run

# 方式二：打包后部署到 Tomcat 10
mvn clean package
# 将 target/ClothesWeb-1.0-SNAPSHOT.war 复制到 Tomcat 10 的 webapps 目录
```

后端服务默认运行在 `http://localhost:8080/ClothesWeb`

### 5.3 启动前端服务

```bash
cd clothes-frontend

# 安装依赖
npm install

# 启动用户端（默认）
npm run dev

# 启动管理端
npm run dev:admin
```

前端服务默认运行在 `http://localhost:7070`（用户端）和 `http://localhost:7071`（管理端）

### 5.4 访问应用

| 端 | 地址 | 说明 |
|----|------|------|
| 后端 API | http://localhost:8080/ClothesWeb | RESTful API |
| 用户端 | http://localhost:7070 | 普通用户前台 |
| 管理端 | http://localhost:7071 | 管理员后台 |

---

## 六、默认账号

### 管理员账号

| 字段 | 值 |
|------|-----|
| 用户名 | admin |
| 密码 | admin123 |
| 手机号 | 13800138000 |
| 角色 | 管理员 |

### 测试用户

可通过注册页面自行注册普通用户账号。

---

## 七、API 接口文档

### 7.1 用户接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/user/register | 用户注册 | 否 |
| POST | /api/user/login | 用户登录 | 否 |
| GET | /api/user/info | 获取用户信息 | 是 |
| POST | /api/user/update | 更新用户信息 | 是 |

### 7.2 服装接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/clothes/list | 获取服装列表 | 否 |
| GET | /api/clothes/detail | 获取服装详情 | 否 |
| GET | /api/clothes/search | 搜索服装 | 否 |

### 7.3 购物车接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/cart/list | 获取购物车 | 是 |
| POST | /api/cart/add | 添加商品 | 是 |
| POST | /api/cart/update | 更新数量 | 是 |
| DELETE | /api/cart/delete | 删除商品 | 是 |

### 7.4 订单接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/order/create | 创建订单 | 是 |
| GET | /api/order/list | 获取订单列表 | 是 |
| GET | /api/order/detail | 获取订单详情 | 是 |

### 7.5 管理员接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/admin/login | 管理员登录 | 否 |
| GET | /api/admin/users | 获取用户列表 | 管理员 |
| GET | /api/admin/orders | 获取所有订单 | 管理员 |
| GET | /api/admin/clothes | 获取商品列表 | 管理员 |

---

## 八、数据库设计

### 8.1 表结构

| 表名 | 说明 |
|------|------|
| t_user | 用户表（含管理员） |
| t_type | 服装类型表 |
| t_size | 尺码表 |
| t_clothes | 服装表 |
| t_cart | 购物车表 |
| t_order | 订单表 |

### 8.2 ER 图

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│  t_user  │────<│  t_cart  │>────│ t_clothes│
└────┬─────┘     └──────────┘     └────┬─────┘
     │                                  │
     │         ┌──────────┐            │
     └────────<│ t_order  │>───────────┘
               └──────────┘

┌──────────┐     ┌──────────┐
│  t_type  │────<│  t_size  │
└──────────┘     └──────────┘
```

---

## 九、开发规范

### 9.1 Git 分支管理

- `main`：主分支，稳定版本
- `feature/*`：功能分支
- `fix/*`：修复分支

### 9.2 提交规范

```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式
test: 测试相关
```

---

## 十、常见问题

### Q1: 前端无法连接后端？

检查 `clothes-frontend/src/api/request.js` 中的 `BASE_URL` 配置，确保与后端地址一致。

### Q2: 数据库连接失败？

检查 `ClothesWeb/src/main/resources/db.properties` 中的数据库配置是否正确，MySQL 服务是否启动。

### Q3: 管理员无法登录？

数据库中默认管理员密码使用 BCrypt 加密。如果无法登录，可通过注册接口注册一个普通用户，然后在数据库中将其 `role` 字段改为 `1`。

---

## 十一、需求完成度对比

### 已完成的功能

**前台用户端：**
- [x] 用户注册（含格式校验、唯一性校验）
- [x] 用户登录（用户名/手机号+密码，返回Token）
- [x] 服装查询（首页展示、按类别筛选、模糊搜索）
- [x] 服装详情（查看信息、动态尺码、加入购物车）
- [x] 购物车（增删改查、批量结算、自动合并同款同尺码）
- [x] 我的订单（提交、支付、确认收货、删除、按状态查看、状态机控制）
- [x] 个人中心（查看信息、修改信息、校验唯一性）

**后台管理端：**
- [x] 服装管理（增删改查、多条件查询、图片上传、防重名）
- [x] 订单管理（查询全部、按用户/状态筛选、发货）

### 未完成的功能

- [ ] **后台用户管理后端接口** - AdminServlet 缺少用户管理的 CRUD 接口
- [ ] **注册时地址字段** - 前端注册表单缺少地址输入框
- [ ] **个人中心修改密码** - Profile.vue 缺少密码修改功能
- [ ] **购物车清空接口** - CartServlet 未暴露 /clear 接口
- [ ] **管理员登录对接后端** - AdminLogin.vue 使用前端硬编码，未调用后端接口
- [ ] **首页风格筛选 UI** - Home.vue 缺少按风格筛选的控件

### 与需求文档的差异

| 项目 | 需求要求 | 实际情况 |
|------|---------|---------|
| 后端部署路径 | `/wardrobe_back` | `/ClothesWeb` |
| 前端代理地址 | `localhost:8080/wardrobe_back` | `localhost:8080/ClothesWeb` |

### 多出来的功能

- 收藏按钮（纯UI，无实际逻辑）
- 订单详情弹窗（后台）
- 前端 mock 数据
- HelloServlet（模板遗留文件）

---

## 十二、相关文档

- [设计文档](./设计文档.md)
- [项目需求](./项目需求.md)
- [验收标准](./验收标准.md)
