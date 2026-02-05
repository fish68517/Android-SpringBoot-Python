"""
书籍相关的 API 路由
"""
from flask import Blueprint, request, jsonify
from database import get_db_connection

bp = Blueprint('books', __name__, url_prefix='/api/books')

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
def get_books():
    """获取所有书籍（支持分页和过滤）"""
    try:
        page = request.args.get('page', 1, type=int)
        limit = request.args.get('limit', 10, type=int)
        condition = request.args.get('condition', None)
        min_price = request.args.get('min_price', 0, type=float)
        max_price = request.args.get('max_price', 10000, type=float)
        
        offset = (page - 1) * limit
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 构建查询条件
        query = 'SELECT * FROM books WHERE status = "active" AND price >= ? AND price <= ?'
        params = [min_price, max_price]
        
        if condition:
            query += ' AND condition = ?'
            params.append(condition)
        
        query += ' ORDER BY created_at DESC LIMIT ? OFFSET ?'
        params.extend([limit, offset])
        
        cursor.execute(query, params)
        books = cursor.fetchall()
        
        # 获取总数
        count_query = 'SELECT COUNT(*) FROM books WHERE status = "active" AND price >= ? AND price <= ?'
        count_params = [min_price, max_price]
        if condition:
            count_query += ' AND condition = ?'
            count_params.append(condition)
        
        cursor.execute(count_query, count_params)
        total = cursor.fetchone()[0]
        conn.close()
        
        books_list = [dict(book) for book in books]
        
        return {
            'books': books_list,
            'total': total,
            'page': page,
            'limit': limit,
            'pages': (total + limit - 1) // limit
        }, 200
    
    except Exception as e:
        return {'error': f'获取书籍失败: {str(e)}'}, 500

@bp.route('/search', methods=['GET'])
def search_books():
    """搜索书籍"""
    try:
        query = request.args.get('q', '')
        
        if not query:
            return {'error': '搜索关键词不能为空'}, 400
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        search_pattern = f'%{query}%'
        cursor.execute('''
            SELECT * FROM books
            WHERE status = "active" AND (title LIKE ? OR author LIKE ?)
            ORDER BY created_at DESC
        ''', (search_pattern, search_pattern))
        
        books = cursor.fetchall()
        conn.close()
        
        books_list = [dict(book) for book in books]
        
        return {'books': books_list}, 200
    
    except Exception as e:
        return {'error': f'搜索失败: {str(e)}'}, 500

@bp.route('/<int:book_id>', methods=['GET'])
def get_book_detail(book_id):
    """获取书籍详情"""
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT b.*, u.username, u.email FROM books b
            JOIN users u ON b.seller_id = u.id
            WHERE b.id = ?
        ''', (book_id,))
        
        book = cursor.fetchone()
        conn.close()
        
        if not book:
            return {'error': '书籍不存在'}, 404
        
        return dict(book), 200
    
    except Exception as e:
        return {'error': f'获取书籍详情失败: {str(e)}'}, 500

@bp.route('', methods=['POST'])
def create_book():
    """创建新的书籍列表"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        data = request.get_json()
        
        # 验证必填字段
        required_fields = ['title', 'author', 'price', 'condition']
        if not all(field in data for field in required_fields):
            return {'error': '缺少必填字段'}, 400
        
        title = data['title']
        author = data['author']
        isbn = data.get('isbn', '')
        price = data['price']
        condition = data['condition']
        description = data.get('description', '')
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('''
            INSERT INTO books (seller_id, title, author, isbn, price, condition, description)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        ''', (user_id, title, author, isbn, price, condition, description))
        
        conn.commit()
        book_id = cursor.lastrowid
        conn.close()
        
        return {
            'message': '书籍列表创建成功',
            'book_id': book_id,
            'title': title
        }, 201
    
    except Exception as e:
        return {'error': f'创建书籍列表失败: {str(e)}'}, 500

@bp.route('/<int:book_id>', methods=['PUT'])
def update_book(book_id):
    """更新书籍列表"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 检查书籍是否存在且属于当前用户
        cursor.execute('SELECT seller_id FROM books WHERE id = ?', (book_id,))
        book = cursor.fetchone()
        
        if not book:
            return {'error': '书籍不存在'}, 404
        
        if book['seller_id'] != user_id:
            return {'error': '无权修改此书籍'}, 403
        
        data = request.get_json()
        
        # 更新字段
        update_fields = []
        update_values = []
        
        for field in ['title', 'author', 'isbn', 'price', 'condition', 'description', 'status']:
            if field in data:
                update_fields.append(f'{field} = ?')
                update_values.append(data[field])
        
        if not update_fields:
            return {'error': '没有要更新的字段'}, 400
        
        update_values.append(book_id)
        
        query = f'UPDATE books SET {", ".join(update_fields)} WHERE id = ?'
        cursor.execute(query, update_values)
        
        conn.commit()
        conn.close()
        
        return {'message': '书籍列表更新成功'}, 200
    
    except Exception as e:
        return {'error': f'更新书籍列表失败: {str(e)}'}, 500

@bp.route('/<int:book_id>', methods=['DELETE'])
def delete_book(book_id):
    """删除书籍列表"""
    try:
        user_id = get_user_id_from_token(request)
        if not user_id:
            return {'error': '未授权'}, 401
        
        conn = get_db_connection()
        cursor = conn.cursor()
        
        # 检查书籍是否存在且属于当前用户
        cursor.execute('SELECT seller_id FROM books WHERE id = ?', (book_id,))
        book = cursor.fetchone()
        
        if not book:
            return {'error': '书籍不存在'}, 404
        
        if book['seller_id'] != user_id:
            return {'error': '无权删除此书籍'}, 403
        
        cursor.execute('DELETE FROM books WHERE id = ?', (book_id,))
        conn.commit()
        conn.close()
        
        return {'message': '书籍列表删除成功'}, 200
    
    except Exception as e:
        return {'error': f'删除书籍列表失败: {str(e)}'}, 500

@bp.route('/user/<int:user_id>', methods=['GET'])
def get_user_books(user_id):
    """获取用户的所有书籍列表"""
    try:
        conn = get_db_connection()
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT * FROM books WHERE seller_id = ?
            ORDER BY created_at DESC
        ''', (user_id,))
        
        books = cursor.fetchall()
        conn.close()
        
        books_list = [dict(book) for book in books]
        
        return {'books': books_list}, 200
    
    except Exception as e:
        return {'error': f'获取用户书籍失败: {str(e)}'}, 500
