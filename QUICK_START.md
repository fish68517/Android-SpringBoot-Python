# 快速启动指南

## 项目概述

校园二手书交易平台 - 一个完整的毕业设计项目
- **Android 客户端**: Java + Material Design 3
- **Python 后端**: Flask + SQLite3
- **15+ 屏幕**: 完整的用户交互流程
- **中文模拟数据**: 27 条测试数据

## 5 分钟快速启动

### 第一步：启动 Python 后端

```bash
# 进入后端目录
cd backend

# 安装依赖
pip install -r requirements.txt

# 运行服务器
python app.py
```

✅ 服务器启动成功，访问 http://localhost:5000/api/health

### 第二步：配置 Android 项目

1. 打开 Android Studio
2. 打开 `android` 文件夹
3. 等待 Gradle 同步完成
4. 连接 Android 设备或启动模拟器

### 第三步：运行 Android 应用

1. 点击 "Run" 或按 Shift + F10
2. 选择目标设备
3. 应用启动后，会自动跳转到登录页面

### 第四步：登录测试

使用以下凭证登录：

```
邮箱: zhangsan@university.edu
密码: 123456
```

## 项目文件说明

### 规格文档
- `.kiro/specs/campus-book-trading/requirements.md` - 需求文档
- `.kiro/specs/campus-book-trading/design.md` - 设计文档
- `.kiro/specs/campus-book-trading/tasks.md` - 任务列表

### 后端文件
- `backend/app.py` - Flask 应用主文件
- `backend/database.py` - 数据库初始化
- `backend/routes/` - API 路由
- `backend/requirements.txt` - Python 依赖

### 前端文件
- `android/app/src/main/java/` - Java 源代码
- `android/app/src/main/res/` - 资源文件
- `android/app/build.gradle` - Gradle 配置

## 主要功能

### 已实现 ✅
- 用户注册和登录
- 书籍浏览和搜索
- 购物车管理
- 交易处理
- 用户个人资料

### 待实现 ⏳
- 完整的 Android UI 屏幕
- 图片上传功能
- 评价和评分系统
- 消息通知功能

## 数据库

### 自动初始化
首次运行时，数据库会自动创建并插入模拟数据：

- **用户表**: 6 条记录
- **书籍表**: 8 条记录
- **购物车表**: 6 条记录
- **交易表**: 7 条记录

### 数据库位置
```
backend/data/books_trading.db
```

## API 测试

### 使用 curl 测试

```bash
# 健康检查
curl http://localhost:5000/api/health

# 用户登录
curl -X POST http://localhost:5000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"zhangsan@university.edu","password":"123456"}'

# 获取书籍列表
curl http://localhost:5000/api/books?page=1&limit=10

# 搜索书籍
curl "http://localhost:5000/api/books/search?q=数学"
```

## 常见问题

### Q: Android 应用无法连接到后端？
A: 检查以下几点：
1. Python 后端是否正在运行
2. 防火墙是否允许 5000 端口
3. 在 `ApiClient.java` 中检查 BASE_URL 是否正确
   - 模拟器: `http://10.0.2.2:5000/api/`
   - 真实设备: `http://192.168.x.x:5000/api/`

### Q: 数据库文件在哪里？
A: 在 `backend/data/books_trading.db`

### Q: 如何重置数据库？
A: 删除 `backend/data/books_trading.db` 文件，重新运行 `python app.py`

### Q: 如何修改模拟数据？
A: 编辑 `backend/database.py` 中的 `init_db()` 函数

## 项目结构

```
campus-book-trading/
├── .kiro/specs/                 # 规格文档
├── backend/                     # Python Flask 后端
│   ├── app.py
│   ├── database.py
│   ├── routes/
│   ├── requirements.txt
│   └── data/
├── android/                     # Android 客户端
│   ├── app/
│   ├── build.gradle
│   └── settings.gradle
├── PROJECT_SUMMARY.md           # 项目总结
└── QUICK_START.md              # 本文件
```

## 下一步

1. **完成 Android UI 屏幕** - 实现剩余的 13 个屏幕
2. **添加图片上传** - 支持书籍图片上传
3. **实现评价系统** - 用户可以评价卖家
4. **添加消息功能** - 买卖双方可以沟通
5. **部署到云服务** - 将应用部署到真实服务器

## 技术支持

- 查看 `backend/README.md` 了解后端详情
- 查看 `android/README.md` 了解前端详情
- 查看 `.kiro/specs/` 了解完整的设计文档

## 许可证

MIT

---

**祝你毕业设计顺利！** 🎓
