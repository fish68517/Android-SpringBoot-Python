# 校园二手书交易平台 - Python Flask 后端

## 项目概述

这是校园二手书交易平台的 Python Flask 后端服务，提供 REST API 给 Android 客户端调用。

## 技术栈

- **框架**: Flask 2.3.0
- **数据库**: SQLite3
- **Python 版本**: 3.7+

## 项目结构

```
backend/
├── app.py                 # Flask 应用主文件
├── database.py            # 数据库初始化和连接管理
├── requirements.txt       # Python 依赖
├── .env                   # 环境变量配置
├── routes/                # API 路由
│   ├── auth_routes.py     # 认证相关路由
│   ├── book_routes.py     # 书籍相关路由
│   ├── cart_routes.py     # 购物车相关路由
│   ├── transaction_routes.py  # 交易相关路由
│   └── user_routes.py     # 用户相关路由
└── data/                  # 数据库文件目录
    └── books_trading.db   # SQLite3 数据库
```

## 安装和运行

### 1. 安装依赖

```bash
cd backend
pip install -r requirements.txt
```

### 2. 运行服务器

```bash
python app.py
```

服务器将在 `http://localhost:5000` 启动

### 3. 验证服务器

访问 `http://localhost:5000/api/health` 应该返回：

```json
{
  "status": "ok",
  "message": "校园二手书交易平台后端服务正常运行"
}
```

## API 端点

### 认证相关

- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/verify` - 验证令牌

### 书籍相关

- `GET /api/books` - 获取所有书籍（支持分页和过滤）
- `GET /api/books/search?q=<query>` - 搜索书籍
- `GET /api/books/<id>` - 获取书籍详情
- `POST /api/books` - 创建新书籍列表
- `PUT /api/books/<id>` - 更新书籍列表
- `DELETE /api/books/<id>` - 删除书籍列表
- `GET /api/books/user/<user_id>` - 获取用户的书籍列表

### 购物车相关

- `GET /api/cart` - 获取购物车
- `POST /api/cart` - 添加商品到购物车
- `PUT /api/cart/<item_id>` - 更新购物车商品数量
- `DELETE /api/cart/<item_id>` - 从购物车移除商品
- `DELETE /api/cart` - 清空购物车

### 交易相关

- `POST /api/transactions` - 创建交易（结账）
- `GET /api/transactions/purchases` - 获取购买历史
- `GET /api/transactions/sales` - 获取销售历史
- `GET /api/transactions/<id>` - 获取交易详情
- `PUT /api/transactions/<id>/status` - 更新交易状态

### 用户相关

- `GET /api/users/profile` - 获取用户个人资料
- `PUT /api/users/profile` - 更新用户个人资料
- `PUT /api/users/password` - 修改密码

## 模拟数据

数据库初始化时会自动插入以下模拟数据：

### 用户 (6 条)
- 张三 (2021001)
- 李四 (2021002)
- 王五 (2021003)
- 赵六 (2021004)
- 孙七 (2021005)
- 周八 (2021006)

### 书籍 (8 条)
- 高等数学 - 45.00 元
- 线性代数 - 38.00 元
- 大学物理 - 52.00 元
- 有机化学 - 48.00 元
- 数据结构 - 55.00 元
- C语言程序设计 - 42.00 元
- 英语词汇 - 35.00 元
- 马克思主义原理 - 28.00 元

### 购物车 (6 条)
### 交易 (7 条)

## 认证

所有需要认证的端点都需要在请求头中包含 `Authorization` 令牌：

```
Authorization: Bearer token_<user_id>
```

例如：`Authorization: Bearer token_1`

## 测试用户

可以使用以下凭证登录：

| 用户名 | 邮箱 | 密码 | 学号 |
|--------|------|------|------|
| 张三 | zhangsan@university.edu | 123456 | 2021001 |
| 李四 | lisi@university.edu | 123456 | 2021002 |
| 王五 | wangwu@university.edu | 123456 | 2021003 |

## 环境变量

在 `.env` 文件中配置：

```
SECRET_KEY=your-secret-key
DATABASE_PATH=./data/books_trading.db
DEBUG=True
```

## 错误处理

所有 API 错误都返回 JSON 格式的错误信息：

```json
{
  "error": "错误描述"
}
```

## 开发注意事项

1. 数据库文件会自动创建在 `data/` 目录
2. 首次运行时会自动初始化数据库和插入模拟数据
3. 所有密码都使用 SHA256 哈希存储
4. 令牌验证是简化版本，生产环境应使用 JWT

## 许可证

MIT
