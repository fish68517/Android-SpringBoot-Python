"""
交易相关的 API 路由
"""
from flask import Blueprint, request, jsonify
from database import get_db_connection

bp = Blueprint('transactions', __name__, url_prefix='/api/transactions')

def get_user_id_from_token(request):
    """从请求头中提取用户 ID"""
    token = request.headers.get('Authorization', '').replace('Bearer ', '')
    if token.startswith('token_'):
        try:
            return int(token.split('_')[1])
        except:
            return None
    return None

@bp.route('', methods=['POST'])
def create_transaction():
    """创建交易（结账）"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        data = request.get_json()
        
        if 'delivery_address' not in data:
            return {'error': '缺少收货地址'}, 400
        
        delivery_address = data['delivery_address']
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 获取用户的购物车
        cursor.execute('''
            SELECT c.id, c.book_id, c.quantity, b.seller_id, b.price
            FROM cart_items c
            JOIN books b ON c.book_id = b.id
            WHERE c.user_id = ?
        ''', (user_id,))
        
        cart_items = cursor.fetchall()
        
        if not cart_items:
            return {'error': '购物车为空'}, 400
        
        # 创建交易记录
        transaction_ids = []
        for item in cart_items:
            cursor.execute('''
                INSERT INTO transactions (buyer_id, seller_id, book_id, price, status, delivery_address)
                VALUES (?, ?, ?, ?, ?, ?)
            ''', (user_id, item['seller_id'], item['book_id'], item['price'], 'pending', delivery_address))
            
            transaction_ids.append(cursor.lastrowid)
            
            # 更新书籍状态为已售
            cursor.execute('''
                UPDATE books SET status = 'sold' WHERE id = ?
            ''', (item['book_id'],))
        
        # 清空购物车
        cursor.execute('DELETE FROM cart_items WHERE user_id = ?', (user_id,))
        
        conn.commit()
        conn.close()
        
        return {
            'message': '订单创建成功',
            'transaction_ids': transaction_ids,
            'count': len(transaction_ids)
        }, 201
    
    except Exception as e:
        return {'error': f'创建交易失败: {str(e)}'}, 500

@bp.route('/purchases', methods=['GET'])
def get_purchases():
    """获取用户的购买历史"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT t.id, t.buyer_id, t.seller_id, t.book_id, t.price, t.status, t.delivery_address, t.created_at,
                   b.title, b.author, u.username as seller_name
            FROM transactions t
            JOIN books b ON t.book_id = b.id
            JOIN users u ON t.seller_id = u.id
            WHERE t.buyer_id = ?
            ORDER BY t.created_at DESC
        ''', (user_id,))
        
        transactions = cursor.fetchall()
        conn.close()
        
        transactions_list = [dict(t) for t in transactions]
        
        return {'purchases': transactions_list}, 200
    
    except Exception as e:
        return {'error': f'获取购买历史失败: {str(e)}'}, 500

@bp.route('/sales', methods=['GET'])
def get_sales():
    """获取用户的销售历史"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT t.id, t.buyer_id, t.seller_id, t.book_id, t.price, t.status, t.delivery_address, t.created_at,
                   b.title, b.author, u.username as buyer_name
            FROM transactions t
            JOIN books b ON t.book_id = b.id
            JOIN users u ON t.buyer_id = u.id
            WHERE t.seller_id = ?
            ORDER BY t.created_at DESC
        ''', (user_id,))
        
        transactions = cursor.fetchall()
        conn.close()
        
        transactions_list = [dict(t) for t in transactions]
        
        return {'sales': transactions_list}, 200
    
    except Exception as e:
        return {'error': f'获取销售历史失败: {str(e)}'}, 500

@bp.route('/<int:transaction_id>', methods=['GET'])
def get_transaction_detail(transaction_id):
    """获取交易详情"""
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT t.*, b.title, b.author, b.condition,
                   buyer.username as buyer_name, buyer.email as buyer_email,
                   seller.username as seller_name, seller.email as seller_email
            FROM transactions t
            JOIN books b ON t.book_id = b.id
            JOIN users buyer ON t.buyer_id = buyer.id
            JOIN users seller ON t.seller_id = seller.id
            WHERE t.id = ?
        ''', (transaction_id,))
        
        transaction = cursor.fetchone()
        conn.close()
        
        if not transaction:
            return {'error': '交易不存在'}, 404
        
        return dict(transaction), 200
    
    except Exception as e:
        return {'error': f'获取交易详情失败: {str(e)}'}, 500

@bp.route('/<int:transaction_id>/status', methods=['PUT'])
def update_transaction_status(transaction_id):
    """更新交易状态"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        data = request.get_json()
        
        if 'status' not in data:
            return {'error': '缺少状态'}, 400
        
        status = data['status']
        valid_statuses = ['pending', 'completed', 'cancelled']
        
        if status not in valid_statuses:
            return {'error': f'无效的状态，必须是: {", ".join(valid_statuses)}'}, 400
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 检查交易是否存在且属于当前用户（买家或卖家）
        cursor.execute('''
            SELECT buyer_id, seller_id FROM transactions WHERE id = ?
        ''', (transaction_id,))
        
        transaction = cursor.fetchone()
        
        if not transaction:
            return {'error': '交易不存在'}, 404
        
        if transaction['buyer_id'] != user_id and transaction['seller_id'] != user_id:
            return {'error': '无权修改此交易'}, 403
        
        cursor.execute('''
            UPDATE transactions SET status = ? WHERE id = ?
        ''', (status, transaction_id))
        
        conn.commit()
        conn.close()
        
        return {'message': '交易状态已更新'}, 200
    
    except Exception as e:
        return {'error': f'更新交易状态失败: {str(e)}'}, 500
