# 校园二手书交易平台 - 毕业设计项目

## 📱 项目概述

这是一个完整的校园二手书交易平台毕业设计项目，包含 Android 客户端和 Python Flask 后端。

**项目完成度**: 37.5% (12/32 任务完成)

## 🏗️ 项目架构

```
Android 客户端 (Java)          Python 后端 (Flask)         SQLite3 数据库
    ↓                              ↓                            ↓
15+ 屏幕                    20+ REST API 端点            4 个数据表
Material Design 3           完整的业务逻辑                27 条模拟数据
Retrofit HTTP 通信          错误处理和日志                自动初始化
```

## ✨ 主要特性

### 已实现 ✅

#### Python 后端 (8/8 完成)
- ✅ Flask 项目设置和依赖管理
- ✅ SQLite3 数据库初始化 (4 个表，27 条模拟数据)
- ✅ 认证 API (注册、登录、验证)
- ✅ 书籍 API (CRUD、搜索、过滤、排序)
- ✅ 购物车 API (添加、更新、移除、清空)
- ✅ 交易 API (创建、历史、状态更新)
- ✅ 用户 API (个人资料、密码修改)
- ✅ 错误处理和日志

#### Android 客户端 (4/24 完成)
- ✅ Android 项目设置 (Material Design 3 主题)
- ✅ 数据模型和 API 客户端 (Retrofit)
- ✅ 认证屏幕 (Splash、Login、Register)
- ✅ 首页和导航
- ✅ 会话管理 (SharedPreferences)

### 待实现 ⏳

#### Android 屏幕 (13 个)
- [ ] 浏览书籍屏幕 (搜索、过滤、排序)
- [ ] 书籍详情屏幕
- [ ] 购物车屏幕
- [ ] 结账屏幕
- [ ] 创建/编辑列表屏幕
- [ ] 我的列表屏幕
- [ ] 仪表板屏幕
- [ ] 购买历史屏幕
- [ ] 销售历史屏幕
- [ ] 个人资料屏幕
- [ ] 错误处理和用户反馈
- [ ] 响应式布局
- [ ] 会话管理优化

#### 测试和文档 (7 个)
- [ ] Python 后端单元测试
- [ ] Python 后端集成测试
- [ ] Android API 客户端测试
- [ ] 端到端测试
- [ ] 响应式布局测试
- [ ] API 文档
- [ ] 部署准备

## 🚀 快速启动

### 启动 Python 后端

```bash
cd backend
pip install -r requirements.txt
python app.py
```

服务器将在 `http://localhost:5000` 启动

### 运行 Android 应用

1. 打开 Android Studio
2. 打开 `android` 项目
3. 连接设备或启动模拟器
4. 点击 "Run"

### 测试登录

```
邮箱: zhangsan@university.edu
密码: 123456
```

## 📁 项目结构

```
campus-book-trading/
├── .kiro/specs/                          # 规格文档
│   └── campus-book-trading/
│       ├── requirements.md               # 需求文档 (8 个需求)
│       ├── design.md                     # 设计文档 (完整架构)
│       └── tasks.md                      # 任务列表 (32 个任务)
│
├── backend/                              # Python Flask 后端
│   ├── app.py                            # Flask 应用主文件
│   ├── database.py                       # 数据库初始化
│   ├── requirements.txt                  # Python 依赖
│   ├── .env                              # 环境变量
│   ├── routes/                           # API 路由
│   │   ├── auth_routes.py
│   │   ├── book_routes.py
│   │   ├── cart_routes.py
│   │   ├── transaction_routes.py
│   │   └── user_routes.py
│   ├── data/                             # 数据库文件
│   │   └── books_trading.db
│   └── README.md
│
├── android/                              # Android 客户端
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/example/campusbooktrading/
│   │   │   │   ├── activities/           # 15+ Activity 类
│   │   │   │   ├── api/                  # Retrofit API 客户端
│   │   │   │   ├── models/               # 6 个数据模型
│   │   │   │   ├── adapters/             # RecyclerView 适配器
│   │   │   │   ├── utils/                # 工具类
│   │   │   │   └── fragments/            # Fragment 类
│   │   │   ├── res/                      # 资源文件
│   │   │   └── AndroidManifest.xml
│   │   ├── build.gradle
│   │   └── proguard-rules.pro
│   ├── build.gradle
│   ├── settings.gradle
│   └── README.md
│
├── PROJECT_SUMMARY.md                    # 项目总结
├── QUICK_START.md                        # 快速启动指南
├── IMPLEMENTATION_STATUS.md              # 实现状态报告
└── README.md                             # 本文件
```

## 📊 数据库

### 自动初始化

首次运行时，数据库会自动创建并插入模拟数据：

| 表名 | 记录数 | 说明 |
|------|--------|------|
| users | 6 | 用户账户 |
| books | 8 | 书籍列表 |
| cart_items | 6 | 购物车项目 |
| transactions | 7 | 交易记录 |

### 模拟数据

**用户** (6 条)
- 张三 (zhangsan@university.edu)
- 李四 (lisi@university.edu)
- 王五 (wangwu@university.edu)
- 赵六 (zhaoliu@university.edu)
- 孙七 (sunqi@university.edu)
- 周八 (zhouba@university.edu)

**书籍** (8 条)
- 高等数学 - 45.00 元
- 线性代数 - 38.00 元
- 大学物理 - 52.00 元
- 有机化学 - 48.00 元
- 数据结构 - 55.00 元
- C语言程序设计 - 42.00 元
- 英语词汇 - 35.00 元
- 马克思主义原理 - 28.00 元

## 🔌 API 端点

### 认证
- `POST /api/auth/register` - 注册
- `POST /api/auth/login` - 登录
- `GET /api/auth/verify` - 验证令牌

### 书籍
- `GET /api/books` - 获取书籍
- `GET /api/books/search?q=<query>` - 搜索
- `GET /api/books/<id>` - 详情
- `POST /api/books` - 创建
- `PUT /api/books/<id>` - 更新
- `DELETE /api/books/<id>` - 删除

### 购物车
- `GET /api/cart` - 获取购物车
- `POST /api/cart` - 添加商品
- `PUT /api/cart/<id>` - 更新数量
- `DELETE /api/cart/<id>` - 移除商品
- `DELETE /api/cart` - 清空

### 交易
- `POST /api/transactions` - 创建交易
- `GET /api/transactions/purchases` - 购买历史
- `GET /api/transactions/sales` - 销售历史
- `GET /api/transactions/<id>` - 详情
- `PUT /api/transactions/<id>/status` - 更新状态

### 用户
- `GET /api/users/profile` - 个人资料
- `PUT /api/users/profile` - 更新资料
- `PUT /api/users/password` - 修改密码

## 🛠️ 技术栈

### 后端
- **框架**: Flask 2.3.0
- **数据库**: SQLite3
- **Python**: 3.7+
- **依赖**: Flask-CORS, python-dotenv

### 前端
- **语言**: Java
- **IDE**: Android Studio
- **SDK**: API 21-34
- **UI**: Material Design 3
- **网络**: Retrofit 2, OkHttp 3
- **JSON**: Gson
- **图片**: Glide

## 📖 文档

- **[快速启动指南](QUICK_START.md)** - 5 分钟快速开始
- **[项目总结](PROJECT_SUMMARY.md)** - 完整的项目概述
- **[实现状态报告](IMPLEMENTATION_STATUS.md)** - 详细的完成情况
- **[后端 README](backend/README.md)** - Python 后端文档
- **[前端 README](android/README.md)** - Android 客户端文档

## 🎯 下一步

1. **完成 Android UI 屏幕** (13 个任务)
   - 浏览、详情、购物车、结账等屏幕
   - RecyclerView 适配器
   - 布局文件

2. **添加测试** (7 个任务)
   - 单元测试
   - 集成测试
   - 端到端测试

3. **优化和部署**
   - 性能优化
   - 安全加固
   - 部署到云服务

## ❓ 常见问题

### Q: 如何连接到后端？
A: 在 `ApiClient.java` 中修改 BASE_URL：
- 模拟器: `http://10.0.2.2:5000/api/`
- 真实设备: `http://192.168.x.x:5000/api/`

### Q: 如何重置数据库？
A: 删除 `backend/data/books_trading.db` 文件，重新运行后端

### Q: 默认登录凭证是什么？
A: 邮箱: `zhangsan@university.edu`, 密码: `123456`

## 📝 许可证

MIT

## 👨‍💻 开发者

毕业设计项目

## 🙏 致谢

感谢所有使用本项目的开发者！

---

**项目完成度**: 37.5% (12/32 任务)
**最后更新**: 2024 年
**祝你毕业设计顺利！** 🎓
