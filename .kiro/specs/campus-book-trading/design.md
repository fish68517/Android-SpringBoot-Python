# Design Document: Campus Second-Hand Book Trading Platform

## Overview

The Campus Second-Hand Book Trading Platform is a mobile application built with Java for Android and Python for the backend API server. The system provides a user-friendly interface for students to buy and sell used textbooks. The architecture emphasizes simplicity, maintainability, and sufficient screens for a capstone project while ensuring all screens are clickable and navigable. The Python backend runs as a local server providing REST APIs to the Android client.

## Architecture

### Technology Stack
- **Frontend**: Java (Android Studio, Android SDK) with Google Material Design 3
- **Backend**: Python (Flask) with REST API
- **Database**: SQLite3 (local on Python server)
- **Communication**: HTTP/REST API (JSON)
- **Session Management**: JWT tokens or session-based authentication
- **UI Framework**: Material Components for Android (MDC)
- **Deployment**: Python server runs locally on development machine or network

### Design System
- **Material Design 3**: Modern, clean UI with Material Components
- **Color Scheme**: Primary color (blue), Secondary color (teal), Tertiary color (orange)
- **Typography**: Roboto font family for consistency
- **Spacing**: 8dp base unit for consistent spacing
- **Elevation**: Material elevation system for depth
- **Icons**: Material Icons for all UI elements

### High-Level Architecture Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                  Android Client (Java)                       │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              Activities & Fragments                    │  │
│  │  - LoginActivity                                       │  │
│  │  - RegisterActivity                                    │  │
│  │  - BrowseActivity                                      │  │
│  │  - BookDetailActivity                                  │  │
│  │  - CreateListingActivity                               │  │
│  │  - CartActivity                                        │  │
│  │  - DashboardActivity                                   │  │
│  │  - ProfileActivity                                     │  │
│  └────────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │         API Client Layer (Retrofit/OkHttp)            │  │
│  │  - UserApiClient                                       │  │
│  │  - BookApiClient                                       │  │
│  │  - CartApiClient                                       │  │
│  │  - TransactionApiClient                                │  │
│  └────────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │         Local Data Layer (SharedPreferences)          │  │
│  │  - Session tokens                                      │  │
│  │  - User preferences                                    │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────┬───────────────────────────────────────┘
                       │ HTTP/REST API (JSON)
                       │ localhost:5000
                       ▼
┌──────────────────────────────────────────────────────────────┐
│              Python Flask API Server                         │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              API Endpoints (Routes)                    │  │
│  │  - POST /api/auth/register                             │  │
│  │  - POST /api/auth/login                                │  │
│  │  - GET /api/books                                      │  │
│  │  - POST /api/books                                     │  │
│  │  - GET /api/books/<id>                                 │  │
│  │  - PUT /api/books/<id>                                 │  │
│  │  - DELETE /api/books/<id>                              │  │
│  │  - POST /api/cart                                      │  │
│  │  - GET /api/cart                                       │  │
│  │  - POST /api/transactions                              │  │
│  │  - GET /api/transactions                               │  │
│  └────────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │           Business Logic Layer (Services)             │  │
│  │  - UserService                                         │  │
│  │  - BookService                                         │  │
│  │  - CartService                                         │  │
│  │  - TransactionService                                  │  │
│  └────────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │         Data Access Layer (Repositories)              │  │
│  │  - UserRepository                                      │  │
│  │  - BookRepository                                      │  │
│  │  - CartRepository                                      │  │
│  │  - TransactionRepository                               │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────┬───────────────────────────────────────┘
                       │ SQL Queries
                       ▼
┌──────────────────────────────────────────────────────────────┐
│                    SQLite3 Database                          │
│  ┌────────────────────────────────────────────────────────┐  │
│  │  Tables:                                               │  │
│  │  - users                                               │  │
│  │  - books                                               │  │
│  │  - cart_items                                          │  │
│  │  - transactions                                        │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

## Components and Interfaces

### 1. Android Frontend Screens (Java)

#### Screen Structure (12+ Screens for Capstone Coverage)

1. **Splash Screen** (`SplashActivity`)
   - App logo and branding
   - Auto-navigate to login or home after 2 seconds
   - Check if user is already logged in

2. **Login Screen** (`LoginActivity`)
   - Email/username input field
   - Password input field
   - "Remember me" checkbox
   - Login button
   - Link to registration screen
   - Password recovery link

3. **Registration Screen** (`RegisterActivity`)
   - Username input field
   - Email input field
   - Password input field
   - Student ID input field
   - Register button
   - Link to login screen
   - Form validation feedback

4. **Home Screen** (`HomeActivity`)
   - Featured books carousel
   - Quick search bar
   - Navigation menu (Browse, Sell, Cart, Profile)
   - Recent books section
   - Category quick links

5. **Browse Books Screen** (`BrowseActivity`)
   - RecyclerView with book listings
   - Search bar
   - Filter options (category, condition, price range)
   - Sort options (newest, price, rating)
   - Pagination/infinite scroll
   - Book card with image, title, price, seller rating

6. **Book Detail Screen** (`BookDetailActivity`)
   - Full book information
   - Book images
   - Seller profile card with rating
   - Price and condition display
   - "Add to Cart" button
   - Related books section
   - Reviews/ratings section

7. **Create Listing Screen** (`CreateListingActivity`)
   - Book title input
   - Author input
   - ISBN input
   - Price input
   - Condition spinner (New, Like New, Good, Fair, Poor)
   - Description text area
   - Image picker
   - Submit button
   - Cancel button

8. **My Listings Screen** (`MyListingsActivity`)
   - RecyclerView of user's listings
   - Edit button for each listing
   - Delete button for each listing
   - Create new listing button
   - Status indicator (active, sold)
   - Listing card with image, title, price

9. **Edit Listing Screen** (`EditListingActivity`)
   - Pre-filled form with current listing data
   - All fields editable
   - Update button
   - Delete button
   - Cancel button

10. **Shopping Cart Screen** (`CartActivity`)
    - RecyclerView of cart items
    - Quantity adjustment controls
    - Remove item buttons
    - Subtotal, tax, total calculations
    - Checkout button
    - Continue shopping button
    - Empty cart message

11. **Checkout Screen** (`CheckoutActivity`)
    - Order summary
    - Delivery address input
    - Payment method selection
    - Order confirmation
    - Place order button
    - Order success confirmation

12. **Dashboard Screen** (`DashboardActivity`)
    - User profile summary
    - Quick stats (listings, purchases, sales)
    - Recent transactions
    - Navigation buttons to history screens
    - Logout button

13. **Purchase History Screen** (`PurchaseHistoryActivity`)
    - RecyclerView of purchased books
    - Transaction details
    - Seller information
    - Delivery status
    - Click to view details

14. **Sales History Screen** (`SalesHistoryActivity`)
    - RecyclerView of sold books
    - Transaction details
    - Buyer information
    - Delivery status
    - Click to view details

15. **User Profile Screen** (`ProfileActivity`)
    - User information display
    - Edit profile button
    - Change password option
    - Account settings
    - Logout button

### 2. Python Flask API Endpoints

#### Authentication Endpoints
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/verify` - Verify token validity

#### Book Endpoints
- `GET /api/books` - Get all books with filters
- `GET /api/books/search?q=<query>` - Search books
- `GET /api/books/<id>` - Get book details
- `POST /api/books` - Create new listing
- `PUT /api/books/<id>` - Update listing
- `DELETE /api/books/<id>` - Delete listing
- `GET /api/books/user/<user_id>` - Get user's listings

#### Cart Endpoints
- `GET /api/cart` - Get user's cart
- `POST /api/cart` - Add item to cart
- `PUT /api/cart/<item_id>` - Update cart item quantity
- `DELETE /api/cart/<item_id>` - Remove item from cart
- `DELETE /api/cart` - Clear cart

#### Transaction Endpoints
- `POST /api/transactions` - Create transaction (checkout)
- `GET /api/transactions/purchases` - Get user's purchases
- `GET /api/transactions/sales` - Get user's sales
- `GET /api/transactions/<id>` - Get transaction details
- `PUT /api/transactions/<id>/status` - Update transaction status

#### User Endpoints
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile
- `PUT /api/users/password` - Change password

### 3. Android API Client Layer (Retrofit)

#### UserApiClient
```java
interface UserApiClient {
    @POST("/api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);
    
    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
    
    @GET("/api/users/profile")
    Call<User> getProfile(@Header("Authorization") String token);
    
    @PUT("/api/users/profile")
    Call<User> updateProfile(@Header("Authorization") String token, @Body User user);
}
```

#### BookApiClient
```java
interface BookApiClient {
    @GET("/api/books")
    Call<List<Book>> getBooks(@Query("page") int page, @Query("limit") int limit);
    
    @GET("/api/books/search")
    Call<List<Book>> searchBooks(@Query("q") String query);
    
    @GET("/api/books/{id}")
    Call<Book> getBookDetail(@Path("id") int id);
    
    @POST("/api/books")
    Call<Book> createListing(@Header("Authorization") String token, @Body Book book);
    
    @PUT("/api/books/{id}")
    Call<Book> updateListing(@Header("Authorization") String token, @Path("id") int id, @Body Book book);
    
    @DELETE("/api/books/{id}")
    Call<Void> deleteListing(@Header("Authorization") String token, @Path("id") int id);
}
```

#### CartApiClient
```java
interface CartApiClient {
    @GET("/api/cart")
    Call<List<CartItem>> getCart(@Header("Authorization") String token);
    
    @POST("/api/cart")
    Call<CartItem> addToCart(@Header("Authorization") String token, @Body CartItem item);
    
    @PUT("/api/cart/{id}")
    Call<CartItem> updateCartItem(@Header("Authorization") String token, @Path("id") int id, @Body CartItem item);
    
    @DELETE("/api/cart/{id}")
    Call<Void> removeFromCart(@Header("Authorization") String token, @Path("id") int id);
}
```

#### TransactionApiClient
```java
interface TransactionApiClient {
    @POST("/api/transactions")
    Call<Transaction> checkout(@Header("Authorization") String token, @Body CheckoutRequest request);
    
    @GET("/api/transactions/purchases")
    Call<List<Transaction>> getPurchases(@Header("Authorization") String token);
    
    @GET("/api/transactions/sales")
    Call<List<Transaction>> getSales(@Header("Authorization") String token);
}
```

#### User Table
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    student_id TEXT UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 模拟数据 (Mock Data)
INSERT INTO users (username, email, password_hash, student_id) VALUES
('张三', 'zhangsan@university.edu', 'hashed_password_1', '2021001'),
('李四', 'lisi@university.edu', 'hashed_password_2', '2021002'),
('王五', 'wangwu@university.edu', 'hashed_password_3', '2021003'),
('赵六', 'zhaoliu@university.edu', 'hashed_password_4', '2021004'),
('孙七', 'sunqi@university.edu', 'hashed_password_5', '2021005'),
('周八', 'zhouba@university.edu', 'hashed_password_6', '2021006');
```

#### Book Table
```sql
CREATE TABLE books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    seller_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    isbn TEXT,
    price REAL NOT NULL,
    condition TEXT NOT NULL,
    description TEXT,
    status TEXT DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

-- 模拟数据 (Mock Data)
INSERT INTO books (seller_id, title, author, isbn, price, condition, description, status) VALUES
(1, '高等数学', '同济大学', '978-7-04-033951-0', 45.00, '良好', '第七版，有笔记，无损坏', 'active'),
(1, '线性代数', '同济大学', '978-7-04-033952-7', 38.00, '如新', '全新未使用', 'active'),
(2, '大学物理', '张三丰', '978-7-04-033953-4', 52.00, '一般', '有折角，内容完整', 'active'),
(2, '有机化学', '李明', '978-7-04-033954-1', 48.00, '良好', '笔记清晰，适合复习', 'active'),
(3, '数据结构', '严蔚敏', '978-7-04-033955-8', 55.00, '如新', '未翻阅过', 'active'),
(3, 'C语言程序设计', '谭浩强', '978-7-04-033956-5', 42.00, '良好', '有标记，无损坏', 'sold'),
(4, '英语词汇', '王长喜', '978-7-04-033957-2', 35.00, '一般', '有使用痕迹', 'active'),
(5, '马克思主义原理', '教育部', '978-7-04-033958-9', 28.00, '良好', '笔记完整', 'active');
```

#### CartItem Table
```sql
CREATE TABLE cart_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    quantity INTEGER DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    UNIQUE(user_id, book_id)
);

-- 模拟数据 (Mock Data)
INSERT INTO cart_items (user_id, book_id, quantity) VALUES
(2, 1, 1),
(2, 3, 2),
(3, 5, 1),
(4, 7, 1),
(5, 2, 1),
(6, 4, 1);
```

#### Transaction Table
```sql
CREATE TABLE transactions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    buyer_id INTEGER NOT NULL,
    seller_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    price REAL NOT NULL,
    status TEXT DEFAULT 'pending',
    delivery_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyer_id) REFERENCES users(id),
    FOREIGN KEY (seller_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- 模拟数据 (Mock Data)
INSERT INTO transactions (buyer_id, seller_id, book_id, price, status, delivery_address) VALUES
(2, 1, 1, 45.00, 'completed', '宿舍楼A-201'),
(3, 1, 2, 38.00, 'completed', '宿舍楼B-305'),
(4, 2, 3, 52.00, 'pending', '宿舍楼C-102'),
(5, 2, 4, 48.00, 'completed', '宿舍楼D-410'),
(6, 3, 5, 55.00, 'pending', '宿舍楼E-215'),
(2, 4, 7, 35.00, 'completed', '宿舍楼A-308'),
(3, 5, 8, 28.00, 'pending', '宿舍楼B-401');
```

## Error Handling

### User-Facing Error Messages
- Invalid login credentials → "Email or password is incorrect"
- Duplicate email/username → "This email/username is already registered"
- Book not found → "The book you're looking for doesn't exist"
- Insufficient permissions → "You don't have permission to perform this action"
- Database errors → "An error occurred. Please try again later"

### Logging Strategy
- Log all authentication attempts
- Log all database operations
- Log all transaction completions
- Store logs in a `logs/` directory with daily rotation

### Exception Handling
- Wrap database operations in try-catch blocks
- Validate all user inputs before processing
- Return appropriate HTTP status codes (400, 401, 403, 404, 500)
- Display generic error messages to users while logging detailed errors

## Testing Strategy

### Unit Tests
- Test UserService methods (registration, authentication, profile updates)
- Test BookService methods (CRUD operations, search, filtering)
- Test CartService methods (add, remove, update, calculate totals)
- Test TransactionService methods (creation, status updates)

### Integration Tests
- Test complete user registration and login flow
- Test book listing creation and retrieval
- Test adding books to cart and checkout process
- Test transaction creation and history retrieval

### Manual Testing Checklist
- Verify all page navigation links work correctly
- Test form submissions with valid and invalid data
- Verify session persistence across page transitions
- Test search and filter functionality
- Verify database persistence after application restart
- Test responsive layout on different screen sizes

## Page Navigation Flow

```
Splash Screen
├── Auto-navigate to Login (if not logged in)
└── Auto-navigate to Home (if logged in)

Login Screen
├── Register link → Registration Screen → Login Screen
├── Login button → Home Screen
└── Password recovery link → Password Recovery

Home Screen
├── Browse button → Browse Books Screen
│   ├── Book card click → Book Detail Screen
│   │   └── Add to Cart → Cart Screen
│   └── Search/Filter → Browse Books Screen
├── Sell button → Create Listing Screen
│   └── Submit → My Listings Screen
├── Cart button → Cart Screen
│   └── Checkout → Checkout Screen → Dashboard
├── Profile button → Dashboard Screen
│   ├── Purchase History → Purchase History Screen
│   ├── Sales History → Sales History Screen
│   ├── Edit Profile → Profile Screen
│   └── Logout → Login Screen
└── My Listings → My Listings Screen
    ├── Edit → Edit Listing Screen
    └── Delete → My Listings Screen
```

## Design Decisions and Rationales

1. **Android + Python Backend**: Provides native mobile experience with flexible backend
2. **SQLite3 on Python Server**: Lightweight, no external database needed, perfect for capstone
3. **REST API Architecture**: Clean separation between frontend and backend, easy to test
4. **Retrofit for HTTP Calls**: Industry-standard library for Android API communication
5. **JWT/Token Authentication**: Stateless authentication suitable for REST APIs
6. **Multiple Screens**: Ensures sufficient screen count for capstone requirements
7. **RecyclerView for Lists**: Efficient list rendering in Android
8. **Local Server**: Python server runs on development machine, accessible via localhost:5000

