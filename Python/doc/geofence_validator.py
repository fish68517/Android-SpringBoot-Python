from shapely.geometry import Polygon, Point
from pyproj import Transformer
import json

# 定义一个坐标转换器
# EPSG:4326 (WGS84) 是 GPS 使用的 (纬度, 经度) 坐标系
# EPSG:3857 (Web Mercator) 是一个常用的米制(x, y)平面坐标系
# 我们用它来把经纬度转换成米，以便计算距离
transformer = Transformer.from_crs("EPSG:4326", "EPSG:3857")

def check_user_presence(user_latlon, classroom_poly_latlon, threshold_meters):
    """
    检查用户是否在多边形教室的阈值范围内

    参数:
    user_latlon (tuple): 用户的 (纬度, 经度)
    classroom_poly_latlon (list of tuples): 教室多边形的 [(纬度, 经度), ...] 列表
    threshold_meters (float): 允许的阈值距离（米）

    返回:
    (bool, float): (是否在场, 实际距离)
    """

    # 1. 将教室的 (纬度, 经度) 列表 转换为 米制(x, y) 坐标列表
    # 注意：pyproj 的 transform 期望的输入是 (lat, lon)
    try:
        classroom_coords_metric = [transformer.transform(lat, lon) for lat, lon in classroom_poly_latlon]
    except Exception as e:
        print(f"坐标转换错误: {e}")
        return False, float('inf')

    # 2. 创建米制的教室多边形
    classroom_polygon = Polygon(classroom_coords_metric)

    # 3. 将用户的 (纬度, 经度) 转换为 米制(x, y) 坐标
    user_point_metric = Point(transformer.transform(user_latlon[0], user_latlon[1]))

    # 4. 计算用户点到教室多边形的距离 (单位：米)
    # 这是 Shapely 的核心功能：
    # - 如果点在多边形内部，距离为 0
    # - 如果点在多边形外部，返回点到多边形最近边界的距离
    distance = user_point_metric.distance(classroom_polygon)

    # 5. 判断是否在场
    if distance <= threshold_meters:
        is_present = True
    else:
        is_present = False

    return is_present, distance

# --- 主程序：测试功能 ---
if __name__ == "__main__":
    
    # 1. 定义一个灵活的多边形教室 (纬度, 经度)
    # (这是一个示例，你可以换成你教室的真实坐标)
    CLASSROOM_POLYGON = [
        (31.2299, 121.4988), # A点
        (31.2295, 121.4988), # B点
        (31.2295, 121.4995), # C点
        (31.2299, 121.4995), # D点
        (31.2299, 121.4988)  # 回到A点闭合
    ]

    # 2. 定义判定在场的阈值（例如：在5米范围内都算在场）
    PRESENCE_THRESHOLD = 5.0  # 单位：米

    # 3. 定义几个测试点
    test_locations = {
        "user_inside": (31.2297, 121.4991),    # 点1：明显在教室内
        "user_nearby": (31.2293, 121.4991),    # 点2：在教室外，但很近
        "user_far_away": (31.2305, 121.5000)   # 点3：在教室外，很远
    }

    print(f"教室多边形 (经纬度): {CLASSROOM_POLYGON}")
    print(f"判定在场阈值: {PRESENCE_THRESHOLD} 米")
    print("-" * 30)

    # 4. 运行测试
    for name, location in test_locations.items():
        is_present, distance = check_user_presence(location, CLASSROOM_POLYGON, PRESENCE_THRESHOLD)
        
        print(f"测试用户: '{name}'")
        print(f"  用户坐标: {location}")
        print(f"  计算距离: {distance:.2f} 米")
        print(f"  是否在场: {is_present}")
        print("-" * 30)