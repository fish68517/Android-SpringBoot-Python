"""
用户相关的 API 路由
"""
from flask import Blueprint, request, jsonify
from database import get_db_connection, hash_password

bp = Blueprint('users', __name__, url_prefix='/api/users')

def get_user_id_from_token(request):
    """从请求头中提取用户 ID"""
    token = request.headers.get('Authorization', '').replace('Bearer ', '')
    if token.startswith('token_'):
        try:
            return int(token.split('_')[1])
        except:
            return None
    return None

@bp.route('/profile', methods=['GET'])
def get_profile():
    """获取用户个人资料"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT id, username, email, student_id, created_at, updated_at
            FROM users WHERE id = ?
        ''', (user_id,))
        
        user = cursor.fetchone()
        
        if not user:
            return {'error': '用户不存在'}, 404
        
        # 获取用户的统计信息
        cursor.execute('SELECT COUNT(*) FROM books WHERE seller_id = ? AND status = "active"', (user_id,))
        active_listings = cursor.fetchone()[0]
        
        cursor.execute('SELECT COUNT(*) FROM books WHERE seller_id = ? AND status = "sold"', (user_id,))
        sold_listings = cursor.fetchone()[0]
        
        cursor.execute('SELECT COUNT(*) FROM transactions WHERE buyer_id = ?', (user_id,))
        purchases = cursor.fetchone()[0]
        
        cursor.execute('SELECT COUNT(*) FROM transactions WHERE seller_id = ?', (user_id,))
        sales = cursor.fetchone()[0]
        
        conn.close()
        
        user_dict = dict(user)
        user_dict['active_listings'] = active_listings
        user_dict['sold_listings'] = sold_listings
        user_dict['purchases'] = purchases
        user_dict['sales'] = sales
        
        return user_dict, 200
    
    except Exception as e:
        return {'error': f'获取个人资料失败: {str(e)}'}, 500

@bp.route('/profile', methods=['PUT'])
def update_profile():
    """更新用户个人资料"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        data = request.get_json()
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 检查用户是否存在
        cursor.execute('SELECT id FROM users WHERE id = ?', (user_id,))
        if not cursor.fetchone():
            return {'error': '用户不存在'}, 404
        
        # 更新允许的字段
        update_fields = []
        update_values = []
        
        if 'username' in data:
            # 检查用户名是否已被使用
            cursor.execute('SELECT id FROM users WHERE username = ? AND id != ?', (data['username'], user_id))
            if cursor.fetchone():
                return {'error': '用户名已被使用'}, 400
            update_fields.append('username = ?')
            update_values.append(data['username'])
        
        if 'email' in data:
            # 检查邮箱是否已被使用
            cursor.execute('SELECT id FROM users WHERE email = ? AND id != ?', (data['email'], user_id))
            if cursor.fetchone():
                return {'error': '邮箱已被使用'}, 400
            update_fields.append('email = ?')
            update_values.append(data['email'])
        
        if not update_fields:
            return {'error': '没有要更新的字段'}, 400
        
        update_fields.append('updated_at = CURRENT_TIMESTAMP')
        update_values.append(user_id)
        
        query = f'UPDATE users SET {", ".join(update_fields)} WHERE id = ?'
        cursor.execute(query, update_values)
        
        conn.commit()
        conn.close()
        
        return {'message': '个人资料已更新'}, 200
    
    except Exception as e:
        return {'error': f'更新个人资料失败: {str(e)}'}, 500

@bp.route('/password', methods=['PUT'])
def change_password():
    """修改密码"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        data = request.get_json()
        
        if 'old_password' not in data or 'new_password' not in data:
            return {'error': '缺少旧密码或新密码'}, 400
        
        old_password = data['old_password']
        new_password = data['new_password']
        
        if len(new_password) < 6:
            return {'error': '新密码长度至少为 6 位'}, 400
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 验证旧密码
        cursor.execute('''
            SELECT password_hash FROM users WHERE id = ?
        ''', (user_id,))
        
        user = cursor.fetchone()
        
        if not user:
            return {'error': '用户不存在'}, 404
        
        if user['password_hash'] != hash_password(old_password):
            return {'error': '旧密码错误'}, 401
        
        # 更新密码
        new_password_hash = hash_password(new_password)
        cursor.execute('''
            UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
        ''', (new_password_hash, user_id))
        
        conn.commit()
        conn.close()
        
        return {'message': '密码已修改'}, 200
    
    except Exception as e:
        return {'error': f'修改密码失败: {str(e)}'}, 500
