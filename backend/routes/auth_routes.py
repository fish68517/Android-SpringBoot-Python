"""
认证相关的 API 路由
"""
from flask import Blueprint, request, jsonify
from database import get_db_connection, hash_password
import json

bp = Blueprint('auth', __name__, url_prefix='/api/auth')

@bp.route('/register', methods=['POST'])
def register():
    """用户注册"""
    try:
        data = request.get_json()
        
        # 验证必填字段
        required_fields = ['username', 'email', 'password', 'student_id']
        if not all(field in data for field in required_fields):
            return {'error': '缺少必填字段'}, 400
        
        username = data['username']
        email = data['email']
        password = data['password']
        student_id = data['student_id']
        
        # 验证密码长度
        if len(password) < 6:
            return {'error': '密码长度至少为 6 位'}, 400
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 检查用户名是否已存在
        cursor.execute('SELECT id FROM users WHERE username = ?', (username,))
        if cursor.fetchone():
            return {'error': '用户名已存在'}, 400
        
        # 检查邮箱是否已存在
        cursor.execute('SELECT id FROM users WHERE email = ?', (email,))
        if cursor.fetchone():
            return {'error': '邮箱已被注册'}, 400
        
        # 检查学号是否已存在
        cursor.execute('SELECT id FROM users WHERE student_id = ?', (student_id,))
        if cursor.fetchone():
            return {'error': '学号已被注册'}, 400
        
        # 创建新用户
        password_hash = hash_password(password)
        cursor.execute('''
            INSERT INTO users (username, email, password_hash, student_id)
            VALUES (?, ?, ?, ?)
        ''', (username, email, password_hash, student_id))
        
        conn.commit()
        user_id = cursor.lastrowid
        conn.close()
        
        return {
            'message': '注册成功',
            'user_id': user_id,
            'username': username,
            'email': email
        }, 201
    
    except Exception as e:
        return {'error': f'注册失败: {str(e)}'}, 500

@bp.route('/login', methods=['POST'])
def login():
    """用户登录"""
    try:
        data = request.get_json()
        
        # 验证必填字段
        if 'email' not in data or 'password' not in data:
            return {'error': '缺少邮箱或密码'}, 400
        
        email = data['email']
        password = data['password']
        password_hash = hash_password(password)
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 查询用户
        cursor.execute('''
            SELECT id, username, email, student_id FROM users
            WHERE email = ? AND password_hash = ?
        ''', (email, password_hash))
        
        user = cursor.fetchone()
        conn.close()
        
        if not user:
            return {'error': '邮箱或密码错误'}, 401
        
        # 返回用户信息和令牌（简化版，实际应使用 JWT）
        return {
            'message': '登录成功',
            'user_id': user['id'],
            'username': user['username'],
            'email': user['email'],
            'student_id': user['student_id'],
            'token': f'token_{user["id"]}'  # 简化的令牌
        }, 200
    
    except Exception as e:
        return {'error': f'登录失败: {str(e)}'}, 500

@bp.route('/verify', methods=['GET'])
def verify():
    """验证令牌有效性"""
    try:
        token = request.headers.get('Authorization', '').replace('Bearer ', '')
        
        if not token:
            return {'error': '缺少令牌'}, 401
        
        # 简化的令牌验证
        if token.startswith('token_'):
            user_id = int(token.split('_')[1])
            
            conn = get_db_connection()
            cursor = conn.cursor()
            cursor.execute('SELECT id, username, email FROM users WHERE id = ?', (user_id,))
            user = cursor.fetchone()
            conn.close()
            
            if user:
                return {
                    'valid': True,
                    'user_id': user['id'],
                    'username': user['username'],
                    'email': user['email']
                }, 200
        
        return {'error': '令牌无效'}, 401
    
    except Exception as e:
        return {'error': f'验证失败: {str(e)}'}, 500
