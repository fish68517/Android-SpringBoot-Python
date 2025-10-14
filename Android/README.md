# Android_Mysql_校园订餐程序

#### 介绍
毕业设计，校园订餐程序，android 和 Mysql 
微信联系： fish_yhw
微信联系： fish_yhw
微信联系： fish_yhw

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)


模拟订单数据：
INSERT INTO orders (order_time, student_id, merchant_id, dish_list, total_price, order_status, dish_id, dining_option, order_quantity) VALUES ('2024-12-31 11:39:13', 4, 13, '14:2,30:3', 53.0, 'completed', 14, 'pickup', 2)
INSERT INTO orders (order_time, student_id, merchant_id, dish_list, total_price, order_status, dish_id, dining_option, order_quantity) VALUES ('2024-12-31 17:46:13', 4, 13, '30:2,14:2', 44.0, 'completed', 30, 'pickup', 2)
INSERT INTO orders (order_time, student_id, merchant_id, dish_list, total_price, order_status, dish_id, dining_option, order_quantity) VALUES ('2024-12-31 18:12:13', 3, 13, '14:1,30:3', 40.0, 'completed', 14, 'pickup', 1)



现在需要你帮我实现商家菜单产品销售逻辑
1：需要从dish_list 数据：如：14:8,30:3 （表示 dish_id 14 ,数量8 ，dish_id 30,数量3 ）获取到 dish_id 和 对应数量，并将dish_id 转换成 菜单名称
2：横轴表示：比如根据order_time 获取一行数据，得到 dish_lis, 解析 所有销售菜单如：dish_id 14  dish_id 30, 作为一天销售数据，纵轴 ：dish_lis 解析出表示对应菜单销售数量，不同菜单dish_id 用不同颜色表示
3：请帮我写出DBMysqlHelper 接口 并修改  MerchantStatsFragment
    
