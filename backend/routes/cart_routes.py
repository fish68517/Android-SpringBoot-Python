"""
购物车相关的 API 路由
"""
from flask import Blueprint, request, jsonify
from database import get_db_connection

bp = Blueprint('cart', __name__, url_prefix='/api/cart')

def get_user_id_from_token(request):
    """从请求头中提取用户 ID"""
    token = request.headers.get('Authorization', '').replace('Bearer ', '')
    if token.startswith('token_'):
        try:
            return int(token.split('_')[1])
        except:
            return None
    return None

@bp.route('', methods=['GET'])
def get_cart():
    """获取用户的购物车"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT c.id, c.user_id, c.book_id, c.quantity, c.added_at,
                   b.title, b.author, b.price, b.condition
            FROM cart_items c
            JOIN books b ON c.book_id = b.id
            WHERE c.user_id = ?
            ORDER BY c.added_at DESC
        ''', (user_id,))
        
        items = cursor.fetchall()
        conn.close()
        
        cart_items = [dict(item) for item in items]
        
        # 计算总价
        total = sum(item['price'] * item['quantity'] for item in cart_items)
        
        return {
            'items': cart_items,
            'total': total,
            'count': len(cart_items)
        }, 200
    
    except Exception as e:
        return {'error': f'获取购物车失败: {str(e)}'}, 500

@bp.route('', methods=['POST'])
def add_to_cart():
    """添加商品到购物车"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        data = request.get_json()
        
        if 'book_id' not in data:
            return {'error': '缺少 book_id'}, 400
        
        book_id = data['book_id']
        quantity = data.get('quantity', 1)
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 检查书籍是否存在
        cursor.execute('SELECT id FROM books WHERE id = ?', (book_id,))
        if not cursor.fetchone():
            return {'error': '书籍不存在'}, 404
        
        # 检查是否已在购物车中
        cursor.execute('''
            SELECT id, quantity FROM cart_items
            WHERE user_id = ? AND book_id = ?
        ''', (user_id, book_id))
        
        existing_item = cursor.fetchone()
        
        if existing_item:
            # 更新数量
            new_quantity = existing_item['quantity'] + quantity
            cursor.execute('''
                UPDATE cart_items SET quantity = ?
                WHERE user_id = ? AND book_id = ?
            ''', (new_quantity, user_id, book_id))
        else:
            # 添加新项
            cursor.execute('''
                INSERT INTO cart_items (user_id, book_id, quantity)
                VALUES (?, ?, ?)
            ''', (user_id, book_id, quantity))
        
        conn.commit()
        conn.close()
        
        return {'message': '商品已添加到购物车'}, 201
    
    except Exception as e:
        return {'error': f'添加到购物车失败: {str(e)}'}, 500

@bp.route('/<int:item_id>', methods=['PUT'])
def update_cart_item(item_id):
    """更新购物车商品数量"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        data = request.get_json()
        
        if 'quantity' not in data:
            return {'error': '缺少 quantity'}, 400
        
        quantity = data['quantity']
        
        if quantity <= 0:
            return {'error': '数量必须大于 0'}, 400
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 检查购物车项是否存在且属于当前用户
        cursor.execute('''
            SELECT user_id FROM cart_items WHERE id = ?
        ''', (item_id,))
        
        item = cursor.fetchone()
        
        if not item:
            return {'error': '购物车项不存在'}, 404
        
        if item['user_id'] != user_id:
            return {'error': '无权修改此项'}, 403
        
        cursor.execute('''
            UPDATE cart_items SET quantity = ? WHERE id = ?
        ''', (quantity, item_id))
        
        conn.commit()
        conn.close()
        
        return {'message': '购物车项已更新'}, 200
    
    except Exception as e:
        return {'error': f'更新购物车失败: {str(e)}'}, 500

@bp.route('/<int:item_id>', methods=['DELETE'])
def remove_from_cart(item_id):
    """从购物车移除商品"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 检查购物车项是否存在且属于当前用户
        cursor.execute('''
            SELECT user_id FROM cart_items WHERE id = ?
        ''', (item_id,))
        
        item = cursor.fetchone()
        
        if not item:
            return {'error': '购物车项不存在'}, 404
        
        if item['user_id'] != user_id:
            return {'error': '无权删除此项'}, 403
        
        cursor.execute('DELETE FROM cart_items WHERE id = ?', (item_id,))
        conn.commit()
        conn.close()
        
        return {'message': '商品已从购物车移除'}, 200
    
    except Exception as e:
        return {'error': f'移除购物车商品失败: {str(e)}'}, 500

@bp.route('', methods=['DELETE'])
def clear_cart():
    """清空购物车"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('DELETE FROM cart_items WHERE user_id = ?', (user_id,))
        conn.commit()
        conn.close()
        
        return {'message': '购物车已清空'}, 200
    
    except Exception as e:
        return {'error': f'清空购物车失败: {str(e)}'}, 500
