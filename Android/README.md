# 校园二手书交易平台 - Android 客户端

## 项目概述

这是校园二手书交易平台的 Android 客户端应用，采用 Java 开发，使用 Material Design 3 设计语言，通过 REST API 与 Python Flask 后端通信。

## 技术栈

- **语言**: Java
- **IDE**: Android Studio
- **最低 SDK**: 21
- **目标 SDK**: 34
- **UI 框架**: Material Design 3
- **网络库**: Retrofit 2
- **JSON 解析**: Gson
- **图片加载**: Glide

## 项目结构

```
android/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/campusbooktrading/
│   │   │   ├── activities/          # Activity 类
│   │   │   │   ├── SplashActivity.java
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── RegisterActivity.java
│   │   │   │   ├── HomeActivity.java
│   │   │   │   ├── BrowseActivity.java
│   │   │   │   ├── BookDetailActivity.java
│   │   │   │   ├── CreateListingActivity.java
│   │   │   │   ├── MyListingsActivity.java
│   │   │   │   ├── EditListingActivity.java
│   │   │   │   ├── CartActivity.java
│   │   │   │   ├── CheckoutActivity.java
│   │   │   │   ├── DashboardActivity.java
│   │   │   │   ├── PurchaseHistoryActivity.java
│   │   │   │   ├── SalesHistoryActivity.java
│   │   │   │   └── ProfileActivity.java
│   │   │   ├── api/                 # API 相关
│   │   │   │   ├── ApiService.java
│   │   │   │   └── ApiClient.java
│   │   │   ├── models/              # 数据模型
│   │   │   │   ├── User.java
│   │   │   │   ├── Book.java
│   │   │   │   ├── CartItem.java
│   │   │   │   ├── Transaction.java
│   │   │   │   ├── AuthRequest.java
│   │   │   │   └── AuthResponse.java
│   │   │   ├── adapters/            # RecyclerView 适配器
│   │   │   ├── utils/               # 工具类
│   │   │   │   └── SessionManager.java
│   │   │   └── fragments/           # Fragment 类
│   │   ├── res/
│   │   │   ├── layout/              # 布局文件
│   │   │   ├── values/              # 资源文件
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   ├── drawable/            # 图片资源
│   │   │   └── mipmap/              # 应用图标
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
└── settings.gradle
```

## 安装和运行

### 1. 前置条件

- 安装 Android Studio
- 安装 Android SDK (API 21+)
- 配置 Java JDK 11+

### 2. 打开项目

1. 打开 Android Studio
2. 选择 "Open an existing Android Studio project"
3. 选择 `android` 目录

### 3. 配置后端地址

在 `ApiClient.java` 中修改 `BASE_URL`：

```java
// Android 模拟器
private static final String BASE_URL = "http://10.0.2.2:5000/api/";

// 真实设备（改为你的电脑 IP）
// private static final String BASE_URL = "http://192.168.x.x:5000/api/";
```

### 4. 运行应用

1. 连接 Android 设备或启动模拟器
2. 点击 "Run" 或按 Shift + F10
3. 选择目标设备

## 主要功能

### 认证
- ✅ 用户注册
- ✅ 用户登录
- ✅ 会话管理
- ✅ 令牌存储

### 书籍浏览
- ✅ 浏览所有书籍
- ✅ 搜索书籍
- ✅ 按条件筛选
- ✅ 按价格排序
- ✅ 查看书籍详情

### 书籍管理
- ✅ 创建新列表
- ✅ 编辑列表
- ✅ 删除列表
- ✅ 查看我的列表

### 购物车
- ✅ 添加商品到购物车
- ✅ 修改数量
- ✅ 移除商品
- ✅ 清空购物车
- ✅ 计算总价

### 交易
- ✅ 结账
- ✅ 查看购买历史
- ✅ 查看销售历史
- ✅ 更新交易状态

### 用户管理
- ✅ 查看个人资料
- ✅ 编辑个人资料
- ✅ 修改密码
- ✅ 登出

## 屏幕列表

1. **Splash Screen** - 启动屏幕
2. **Login Screen** - 登录
3. **Register Screen** - 注册
4. **Home Screen** - 首页
5. **Browse Screen** - 浏览书籍
6. **Book Detail Screen** - 书籍详情
7. **Create Listing Screen** - 创建列表
8. **My Listings Screen** - 我的列表
9. **Edit Listing Screen** - 编辑列表
10. **Cart Screen** - 购物车
11. **Checkout Screen** - 结账
12. **Dashboard Screen** - 仪表板
13. **Purchase History Screen** - 购买历史
14. **Sales History Screen** - 销售历史
15. **Profile Screen** - 个人资料

## Material Design 3 特性

- 现代化的颜色方案（蓝色主色、青色次色、橙色三级色）
- Material Components（Button、CardView、RecyclerView 等）
- Material Icons
- 响应式布局
- 深色模式支持

## API 集成

所有 API 调用都通过 Retrofit 进行，支持：
- 自动 JSON 序列化/反序列化
- 请求/响应日志记录
- 错误处理
- 令牌认证

## 会话管理

使用 `SessionManager` 管理用户会话：
- 存储用户信息
- 存储认证令牌
- 检查登录状态
- 处理登出

## 测试用户

可以使用以下凭证登录：

| 用户名 | 邮箱 | 密码 | 学号 |
|--------|------|------|------|
| 张三 | zhangsan@university.edu | 123456 | 2021001 |
| 李四 | lisi@university.edu | 123456 | 2021002 |
| 王五 | wangwu@university.edu | 123456 | 2021003 |

## 开发注意事项

1. 确保 Python 后端服务正在运行
2. 检查网络连接
3. 使用 Android Studio 的 Logcat 查看日志
4. 在真实设备上测试时，修改 `ApiClient.java` 中的 BASE_URL

## 许可证

MIT
