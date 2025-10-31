import os
import uuid
import traceback
from flask import Flask, request, jsonify
from deepface import DeepFace

# --- 1. 全局配置 ---
app = Flask(__name__)

# 用于存储注册人脸的 "数据库" 文件夹
DB_PATH = "face_database"
# 用于临时存放上传文件的文件夹
UPLOAD_FOLDER = "temp_uploads"
os.makedirs(DB_PATH, exist_ok=True)
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

# DeepFace 模型配置
MODEL_NAME = "ArcFace"
DETECTOR_BACKEND = "mtcnn"
DISTANCE_METRIC = "cosine" # 距离度量方式

# 禁用 deepface 的详细日志，保持控制台干净
os.environ['DEEPFACE_LOG_LEVEL'] = 'ERROR'

# --- 2. 预加载模型 ---
print("正在加载人脸识别模型...")
try:
    DeepFace.build_model(MODEL_NAME)
    print(f"模型 '{MODEL_NAME}' 加载成功，服务已准备就绪！")
except Exception as e:
    print(f"模型加载失败，错误: {e}")

# --- 3. API 接口定义 ---

@app.route("/register", methods=["POST"])
def register():
    """
    人脸注册接口
    接收表单数据: 'user_id' 和 'image' 文件
    """
    if 'image' not in request.files:
        return jsonify({"status": "error", "message": "请求中未找到 'image' 文件"}), 400
    if 'user_id' not in request.form:
        return jsonify({"status": "error", "message": "请求中未找到 'user_id' 参数"}), 400

    image_file = request.files['image']
    user_id = request.form['user_id']
    
    if not user_id.strip():
        return jsonify({"status": "error", "message": "'user_id' 不能为空"}), 400

    image_filename = f"{user_id}.jpg"
    image_path = os.path.join(DB_PATH, image_filename)
    image_file.save(image_path)

    try:
        DeepFace.represent(
            img_path=image_path,
            model_name=MODEL_NAME,
            detector_backend=DETECTOR_BACKEND,
            enforce_detection=True
        )
        print(f"SUCCESS: 用户 '{user_id}' 注册成功, 图片保存至 '{image_path}'")
        return jsonify({"status": "success", "message": f"用户 '{user_id}' 注册成功"})
    except ValueError:
        os.remove(image_path)
        print(f"FAIL: 用户 '{user_id}' 注册失败，图片中未检测到人脸。")
        return jsonify({"status": "error", "message": "注册失败: 提供的图片中无法检测到清晰的人脸"}), 400
    except Exception as e:
        if os.path.exists(image_path):
            os.remove(image_path)
        traceback.print_exc()
        return jsonify({"status": "error", "message": f"服务器内部错误: {str(e)}"}), 500


@app.route("/login", methods=["POST"])
def login():
    """
    人脸登录接口
    接收 'image' 文件
    """
    if 'image' not in request.files:
        return jsonify({"status": "error", "message": "请求中未找到 'image' 文件"}), 400

    image_file = request.files['image']
    
    temp_image_path = os.path.join(UPLOAD_FOLDER, f"{uuid.uuid4()}.jpg")
    image_file.save(temp_image_path)

    try:
        dfs = DeepFace.find(
            img_path=temp_image_path,
            db_path=DB_PATH,
            model_name=MODEL_NAME,
            detector_backend=DETECTOR_BACKEND,
            distance_metric=DISTANCE_METRIC,
            enforce_detection=True,
            silent=True
        )

        if len(dfs) > 0 and not dfs[0].empty:
            best_match = dfs[0].iloc[0].to_dict()
            
            identity_path = best_match.get('identity', '')
            user_id = os.path.splitext(os.path.basename(identity_path))[0]
            
            # 【核心验证逻辑 - 已修正】
            # 直接从匹配结果中获取 'distance' 和 'threshold'
            distance = best_match.get('distance', 1.0)
            threshold = best_match.get('threshold', 0.0) # 注意：这里的默认值设为0，确保在异常时不会错误匹配
            
            if distance <= threshold:
                print(f"SUCCESS: 登录成功! 识别为用户 '{user_id}', Distance: {distance:.4f}, Threshold: {threshold}")
                return jsonify({
                    "status": "success", 
                    "user_id": user_id,
                    "distance": distance
                })
            else:
                print(f"FAIL: 登录失败. 最佳匹配 '{user_id}' 的相似度不足. Distance: {distance:.4f}, Threshold: {threshold}")
                return jsonify({"status": "failure", "message": "人脸不匹配，请重试"})
        else:
            print("FAIL: 登录失败. 数据库中未找到任何匹配的人脸。")
            return jsonify({"status": "failure", "message": "用户未注册或人脸不匹配"})

    except ValueError:
        print("FAIL: 登录失败，上传的图片中未检测到人脸。")
        return jsonify({"status": "error", "message": "登录失败: 无法检测到清晰的人脸"}), 400
    except Exception as e:
        traceback.print_exc()
        return jsonify({"status": "error", "message": f"服务器内部错误: {str(e)}"}), 500
    finally:
        if os.path.exists(temp_image_path):
            os.remove(temp_image_path)

# --- 4. 启动服务 ---
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)