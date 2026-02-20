"""
数据库初始化和连接管理
"""
import sqlite3
import os
from datetime import datetime
import hashlib

DATABASE_PATH = os.getenv('DATABASE_PATH', './data/books_trading.db')

def get_db_connection():
    """获取数据库连接"""
    os.makedirs(os.path.dirname(DATABASE_PATH), exist_ok=True)
    conn = sqlite3.connect(DATABASE_PATH)
    conn.row_factory = sqlite3.Row
    return conn

def hash_password(password):
    """密码哈希"""
    return hashlib.sha256(password.encode()).hexdigest()

def init_db():
    """初始化数据库，创建表和插入模拟数据"""
    conn = get_db_connection()
    cursor = conn.cursor()
    
    # 创建用户表
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE NOT NULL,
            email TEXT UNIQUE NOT NULL,
            password_hash TEXT NOT NULL,
            student_id TEXT UNIQUE NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
    ''')
    
    # 创建书籍表 (新增 image_data 和 image_mime_type 字段)
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS books (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            seller_id INTEGER NOT NULL,
            title TEXT NOT NULL,
            author TEXT NOT NULL,
            isbn TEXT,
            price REAL NOT NULL,
            condition TEXT NOT NULL,
            description TEXT,
            image_data BLOB,
            image_mime_type TEXT,
            status TEXT DEFAULT 'active',
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (seller_id) REFERENCES users(id)
        )
    ''')
    
    # 创建购物车表
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS cart_items (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            book_id INTEGER NOT NULL,
            quantity INTEGER DEFAULT 1,
            added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(id),
            FOREIGN KEY (book_id) REFERENCES books(id),
            UNIQUE(user_id, book_id)
        )
    ''')
    
    # 创建交易表
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS transactions (
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
        )
    ''')
    
    # 检查是否已有数据
    cursor.execute('SELECT COUNT(*) FROM users')
    if cursor.fetchone()[0] == 0:
        # 插入模拟用户数据
        users_data = [
            ('张三', 'zhangsan@university.edu', '123456', '2021001'),
            ('李四', 'lisi@university.edu', '123456', '2021002'),
            ('王五', 'wangwu@university.edu', '123456', '2021003'),
            ('赵六', 'zhaoliu@university.edu', '123456', '2021004'),
            ('孙七', 'sunqi@university.edu', '123456', '2021005'),
            ('周八', 'zhouba@university.edu', '123456', '2021006'),
        ]
        
        for username, email, password, student_id in users_data:
            cursor.execute('''
                INSERT INTO users (username, email, password_hash, student_id)
                VALUES (?, ?, ?, ?)
            ''', (username, email, hash_password(password), student_id))
        
        # 插入模拟书籍数据 (追加了 None, None 代表默认没有图片)
        books_data = [
            (1, '高等数学', '同济大学', '978-7-04-033951-0', 45.00, '良好', '第七版，有笔记，无损坏', None, None, 'active'),
            (1, '线性代数', '同济大学', '978-7-04-033952-7', 38.00, '如新', '全新未使用', None, None, 'active'),
            (2, '大学物理', '张三丰', '978-7-04-033953-4', 52.00, '一般', '有折角，内容完整', None, None, 'active'),
            (2, '有机化学', '李明', '978-7-04-033954-1', 48.00, '良好', '笔记清晰，适合复习', None, None, 'active'),
            (3, '数据结构', '严蔚敏', '978-7-04-033955-8', 55.00, '如新', '未翻阅过', None, None, 'active'),
            (3, 'C语言程序设计', '谭浩强', '978-7-04-033956-5', 42.00, '良好', '有标记，无损坏', None, None, 'sold'),
            (4, '英语词汇', '王长喜', '978-7-04-033957-2', 35.00, '一般', '有使用痕迹', None, None, 'active'),
            (5, '马克思主义原理', '教育部', '978-7-04-033958-9', 28.00, '良好', '笔记完整', None, None, 'active'),
        ]
        
        for seller_id, title, author, isbn, price, condition, description, img_data, img_mime, status in books_data:
            cursor.execute('''
                INSERT INTO books (seller_id, title, author, isbn, price, condition, description, image_data, image_mime_type, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ''', (seller_id, title, author, isbn, price, condition, description, img_data, img_mime, status))
        
        # 插入模拟购物车数据
        cart_data = [(2, 1, 1), (2, 3, 2), (3, 5, 1), (4, 7, 1), (5, 2, 1), (6, 4, 1)]
        for user_id, book_id, quantity in cart_data:
            cursor.execute('''
                INSERT INTO cart_items (user_id, book_id, quantity)
                VALUES (?, ?, ?)
            ''', (user_id, book_id, quantity))
        
        # 插入模拟交易数据
        transactions_data = [
            (2, 1, 1, 45.00, 'completed', '宿舍楼A-201'),
            (3, 1, 2, 38.00, 'completed', '宿舍楼B-305'),
            (4, 2, 3, 52.00, 'pending', '宿舍楼C-102'),
            (5, 2, 4, 48.00, 'completed', '宿舍楼D-410'),
            (6, 3, 5, 55.00, 'pending', '宿舍楼E-215'),
            (2, 4, 7, 35.00, 'completed', '宿舍楼A-308'),
            (3, 5, 8, 28.00, 'pending', '宿舍楼B-401'),
        ]
        for buyer_id, seller_id, book_id, price, status, delivery_address in transactions_data:
            cursor.execute('''
                INSERT INTO transactions (buyer_id, seller_id, book_id, price, status, delivery_address)
                VALUES (?, ?, ?, ?, ?, ?)
            ''', (buyer_id, seller_id, book_id, price, status, delivery_address))
    
    conn.commit()
    conn.close()
    print(f"✓ 数据库初始化完成: {DATABASE_PATH}")