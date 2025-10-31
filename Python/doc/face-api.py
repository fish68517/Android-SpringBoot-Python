import os
import uuid
import json
from flask import Flask, request, jsonify
from deepface import DeepFace

# --- 配置 ---
app = Flask(__name__)

# 人脸数据库 和 临时上传文件夹
DB_PATH = "db"
UPLOAD_FOLDER = "uploads"
os.makedirs(DB_PATH, exist_ok=True)
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

# DeepFace 模型配置
MODEL_NAME = "ArcFace"
DETECTOR_BACKEND = "mtcnn"

# 禁用 deepface 的详细日志
os.environ['DEEPFACE_LOG_LEVEL'] = 'ERROR'

# --- API 路由 ---

@app.route("/register", methods=["POST"])
def register():
    """
    人脸注册接口
    需要 'user_id' 和 'image' 文件作为输入
    """
    # 1. 检查必要的数据
    if 'image' not in request.files:
        return jsonify({"status": "error", "message": "图片文件未提供"}), 400
    if 'user_id' not in request.form:
        return jsonify({"status": "error", "message": "用户ID (user_id) 未提供"}), 400

    image_file = request.files['image']
    user_id = request.form['user_id']
    
    # 2. 保存图片到数据库
    # 使用 user_id 作为文件名，方便管理
    image_path = os.path.join(DB_PATH, f"{user_id}.jpg")
    image_file.save(image_path)

    try:
        # 3. 验证图片中是否包含人脸 (重要！)
        # 这一步可以确保数据库里的照片都是有效的
        DeepFace.represent(
            img_path=image_path,
            model_name=MODEL_NAME,
            detector_backend=DETECTOR_BACKEND,
            enforce_detection=True
        )
        
        print(f"用户 '{user_id}' 注册成功，图片已保存至: {image_path}")
        return jsonify({"status": "success", "message": f"用户 '{user_id}' 注册成功"})

    except ValueError as e:
        # 如果 represent 找不到人脸，会抛出 ValueError
        os.remove(image_path) # 删除无效的图片
        print(f"注册失败: 图片中未检测到人脸。用户: {user_id}")
        return jsonify({"status": "error", "message": "注册失败: 图片中未检测到人脸"}), 400
    except Exception as e:
        os.remove(image_path) # 出错时删除图片
        print(f"注册时发生未知错误: {str(e)}")
        return jsonify({"status": "error", "message": f"服务器内部错误: {str(e)}"}), 500


@app.route("/login", methods=["POST"])
def login():
    """
    人脸登录接口
    需要 'image' 文件作为输入
    """
    # 1. 检查图片文件
    if 'image' not in request.files:
        return jsonify({"status": "error", "message": "图片文件未提供"}), 400

    image_file = request.files['image']
    
    # 2. 临时保存上传的图片以便处理
    # 使用UUID确保文件名唯一，防止并发请求冲突
    temp_image_path = os.path.join(UPLOAD_FOLDER, f"{uuid.uuid4()}.jpg")
    image_file.save(temp_image_path)

    try:
        # 3. 在数据库中进行1:N人脸识别
        dfs = DeepFace.find(
            img_path=temp_image_path,
            db_path=DB_PATH,
            model_name=MODEL_NAME,
            detector_backend=DETECTOR_BACKEND,
            enforce_detection=True, # 强制要求上传的图片必须有人脸
            silent=True
        )

        # 4. 分析结果
        if len(dfs) > 0 and not dfs[0].empty:
            # dfs[0] 是一个 DataFrame，我们只关心最匹配的那个
            # 结果已按 distance 升序排序
            best_match = dfs[0].iloc[0]
            identity_path = best_match['identity']
            
            # 从文件路径中提取 user_id (文件名)
            user_id = os.path.splitext(os.path.basename(identity_path))[0]
            
            print(f"登录成功: 识别到用户 '{user_id}'")
            # 登录成功，返回识别到的用户ID
            return jsonify({"status": "success", "user_id": user_id})
        else:
            # 数据库中没有找到匹配的人脸
            print("登录失败: 未在数据库中找到匹配的人脸")
            return jsonify({"status": "failure", "message": "人脸识别失败，用户未注册"})

    except ValueError:
        # 上传的登录图片中未检测到人脸
        print("登录失败: 上传的图片中未检测到人脸")
        return jsonify({"status": "error", "message": "登录失败: 图片中未检测到人脸"}), 400
    except Exception as e:
        print(f"登录时发生未知错误: {str(e)}")
        return jsonify({"status": "error", "message": f"服务器内部错误: {str(e)}"}), 500
    finally:
        # 5. 清理临时文件
        if os.path.exists(temp_image_path):
            os.remove(temp_image_path)


if __name__ == '__main__':
    # 运行 Flask app
    # host='0.0.0.0' 使服务在你的局域网内可访问
    # Android App 需要通过你电脑的局域网IP来访问这个服务
    app.run(host='0.0.0.0', port=5000, debug=True)