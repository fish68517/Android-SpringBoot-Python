import sys
import json
import os
from deepface import DeepFace

# --- 配置 ---
# 指定我们将使用的模型
# ArcFace 是目前最顶尖的识别模型之一
MODEL_NAME = "ArcFace"
# MTCNN 是一个非常准确的人脸检测器
DETECTOR_BACKEND = "mtcnn"

# 禁用 deepface 的详细日志，只输出结果
os.environ['DEEPFACE_LOG_LEVEL'] = 'ERROR'


def get_face_embedding(image_path):
    """
    功能1: 注册人脸 - 提取单张人脸的特征向量 (Embedding)
    这是注册流程的核心。
    """
    try:
        # DeepFace.represent 会执行:
        # 1. 加载图片
        # 2. 使用 MTCNN 检测人脸
        # 3. 裁剪并对齐人脸
        # 4. 使用 ArcFace 计算特征向量
        embedding_objs = DeepFace.represent(
            img_path=image_path,
            model_name=MODEL_NAME,
            detector_backend=DETECTOR_BACKEND,
            enforce_detection=True # 强制必须检测到人脸，否则报错
        )
        
        # represent 返回一个列表，我们取第一个人脸的特征
        embedding = embedding_objs[0]["embedding"]
        
        # 将结果格式化为 JSON
        result = {
            "status": "success",
            "embedding": embedding,
            "image_path": image_path
        }
        return json.dumps(result, indent=2)

    except ValueError as e:
        # 捕获 DeepFace 找不到人脸时的错误
        result = {
            "status": "error",
            "message": "No face detected in the image.",
            "image_path": image_path
        }
        return json.dumps(result, indent=2)
    except Exception as e:
        # 捕获其他所有异常
        result = {
            "status": "error",
            "message": str(e),
            "image_path": image_path
        }
        return json.dumps(result, indent=2)


def verify_faces(image1_path, image2_path):
    """
    功能2: 人脸验证 (1:1) - 判断两张照片是否为同一个人
    """
    try:
        # DeepFace.verify 会执行:
        # 1. 分别计算两张图的特征向量
        # 2. 计算向量间的余弦距离
        # 3. 根据 ArcFace 的阈值判断是否为同一人
        verification_result = DeepFace.verify(
            img1_path=image1_path,
            img2_path=image2_path,
            model_name=MODEL_NAME,
            detector_backend=DETECTOR_BACKEND,
            enforce_detection=True
        )
        
        # 将结果格式化为 JSON
        # verification_result 是一个字典
        result = {
            "status": "success",
            "verified": verification_result["verified"],
            "distance": verification_result["distance"],
            "threshold": verification_result["threshold"],
            "model": MODEL_NAME
        }
        return json.dumps(result, indent=2)

    except ValueError as e:
        result = {
            "status": "error",
            "message": "Face not detected in one or both images."
        }
        return json.dumps(result, indent=2)
    except Exception as e:
        result = {
            "status": "error",
            "message": str(e)
        }
        return json.dumps(result, indent=2)


def find_face_in_db(image_path, db_path):
    """
    功能3: 人脸识别 (1:N) - 在数据库(文件夹)中搜索匹配的人脸
    """
    try:
        # DeepFace.find 会执行:
        # 1. 计算 image_path 的特征向量
        # 2. 遍历 db_path 文件夹中的所有图片，计算它们的特征向量
        # 3. 寻找与 image_path 向量最接近的结果
        # 
        # dfs (dataframes) 是一个包含匹配结果的列表
        # 我们只关心最匹配的那个
        dfs = DeepFace.find(
            img_path=image_path,
            db_path=db_path,
            model_name=MODEL_NAME,
            detector_backend=DETECTOR_BACKEND,
            enforce_detection=True,
            silent=True # 隐藏查找过程中的进度条
        )
        
        matched_identities = []
        if len(dfs) > 0 and not dfs[0].empty:
            # dfs[0] 是一个 pandas DataFrame，我们将其转换为字典列表
            matched_identities = dfs[0].to_dict('records')
            
        result = {
            "status": "success",
            # 返回所有匹配到的结果 (按相似度排序)
            "matches": matched_identities 
        }
        return json.dumps(result, indent=2)
        
    except ValueError as e:
        result = {
            "status": "error",
            "message": "No face detected in the query image."
        }
        return json.dumps(result, indent=2)
    except Exception as e:
        result = {
            "status": "error",
            "message": str(e)
        }
        return json.dumps(result, indent=2)


def main():
    """
    主函数：用于 SpringBoot 调用的入口
    
    用法:
    1. 注册: python face_recognition_service.py register <image_path>
    2. 验证: python face_recognition_service.py verify <image1_path> <image2_path>
    3. 识别: python face_recognition_service.py find <image_path> <db_path>
    """
    if len(sys.argv) < 3:
        print(json.dumps({"status": "error", "message": "Invalid arguments. Usage: python script.py [register|verify|find] <args...>"}))
        sys.exit(1)

    command = sys.argv[1]

    if command == "register" and len(sys.argv) == 3:
        image_path = sys.argv[2]
        print(get_face_embedding(image_path))
        
    elif command == "verify" and len(sys.argv) == 4:
        image1_path = sys.argv[2]
        image2_path = sys.argv[3]
        print(verify_faces(image1_path, image2_path))
        
    elif command == "find" and len(sys.argv) == 4:
        image_path = sys.argv[2]
        db_path = sys.argv[3]
        print(find_face_in_db(image_path, db_path))
        
    else:
        print(json.dumps({"status": "error", "message": "Invalid command or number of arguments."}))
        sys.exit(1)

if __name__ == "__main__":
    main()