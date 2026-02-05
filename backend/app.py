"""
Campus Second-Hand Book Trading Platform - Flask Backend
主程序入口
"""
from flask import Flask
from flask_cors import CORS
import os
from dotenv import load_dotenv

# 加载环境变量
load_dotenv()

# 创建 Flask 应用
app = Flask(__name__)
app.config['SECRET_KEY'] = os.getenv('SECRET_KEY', 'dev-secret-key-change-in-production')

# 启用 CORS
CORS(app)

# 导入数据库初始化
from database import init_db

# 初始化数据库
with app.app_context():
    init_db()

# 导入路由
from routes import auth_routes, book_routes, cart_routes, transaction_routes, user_routes

# 注册蓝图
app.register_blueprint(auth_routes.bp)
app.register_blueprint(book_routes.bp)
app.register_blueprint(cart_routes.bp)
app.register_blueprint(transaction_routes.bp)
app.register_blueprint(user_routes.bp)

@app.route('/api/health', methods=['GET'])
def health_check():
    """健康检查端点"""
    return {'status': 'ok', 'message': '校园二手书交易平台后端服务正常运行'}, 200

@app.errorhandler(404)
def not_found(error):
    """404 错误处理"""
    return {'error': '请求的资源不存在'}, 404

@app.errorhandler(500)
def internal_error(error):
    """500 错误处理"""
    return {'error': '服务器内部错误'}, 500

if __name__ == '__main__':
    # 开发环境运行
    app.run(
        host='0.0.0.0',
        port=5000,
        debug=True
    )
