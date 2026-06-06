# ClothesWeb - 网上衣橱管理系统

## 项目简介

基于 Java Servlet + Vue 3 的全栈Web应用，提供衣物管理、购物车、订单处理等功能。

## 技术栈

### 后端 (ClothesWeb/)
- Java Servlet
- JDBC + MySQL
- Maven

### 前端 (clothes-frontend/)
- Vue 3
- Vite
- Vue Router

## 项目结构

```
ClothesWeb/
├── src/main/java/org/example/clothesweb/  # Java后端源码
│   ├── servlet/    # Servlet控制器
│   ├── service/    # 业务逻辑层
│   ├── dao/        # 数据访问层
│   ├── entity/     # 实体类
│   ├── filter/     # 过滤器
│   └── util/       # 工具类
├── src/main/webapp/  # Web应用
└── database.sql      # 数据库初始化脚本

clothes-frontend/
├── src/
│   ├── views/      # 页面视图
│   ├── components/ # 组件
│   ├── api/        # API接口
│   └── router/     # 路由配置
└── ...
```

## 待办事项 (TODO)

- [ ] 修复前后端接口连接问题
- [ ] 美化前端 Vue 界面
- [ ] 修正后端逻辑错误

## 快速开始

### 后端启动
```bash
cd ClothesWeb
mvn tomcat7:run
```

### 前端启动
```bash
cd clothes-frontend
npm install
npm run dev
```

## 数据库

执行 `ClothesWeb/database.sql` 初始化数据库。

## 文档

- [设计文档](./设计文档.md)
- [项目需求](./项目需求.md)
- [验收标准](./验收标准.md)
