package com.graduation;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;
import java.util.Collections;

public class GenerateCode {

    public static void main(String[] args) {
        // 1. 设置数据库连接
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/time_management_db?serverTimezone=Asia/Shanghai",
                        "root", "root")

                // 2. 全局配置
                .globalConfig(builder -> builder
                        .author("张三") // <--- 修改为你的名字
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java") // 输出目录
                        .commentDate("yyyy-MM-dd") // 注释日期格式
                        .disableOpenDir() // 禁止生成后打开输出目录
                )

                // 3. 包名配置
                .packageConfig(builder -> builder
                        .parent("com.graduation") // <--- 修改为你的项目主包名
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .controller("controller") // 添加 controller 包配置
                        .pathInfo(Collections.singletonMap(OutputFile.xml, Paths.get(System.getProperty("user.dir")) + "/src/main/resources/mapper")) // XML输出目录
                )

                // 4. 策略配置
                .strategyConfig(builder -> builder
//                        .addInclude("user") // <--- 修改为你需要生成的表名，多个用逗号隔开
                        // Controller 策略配置
                        .controllerBuilder()
                        .enableRestStyle() // <--- 【关键】生成 @RestController
                        .formatFileName("%sController") // Controller 文件名格式
                                .superClass("com.graduation.common.BaseController") // <--- ADD THIS LINE
                                .enableFileOverride() // <--- 新增：开启文件覆盖


                        // Service 策略配置
                        .serviceBuilder()
                        .formatServiceFileName("%sService") // Service 接口文件名格式
                        .formatServiceImplFileName("%sServiceImpl") // Service 实现类文件名格式
                                .enableFileOverride() // <--- 新增：开启文件覆盖

                        // Entity 策略配置
                        .entityBuilder()
                        .enableLombok() // 开启 Lombok
                        .naming(NamingStrategy.underline_to_camel) // 数据库表名下划线转驼峰
                        .columnNaming(NamingStrategy.underline_to_camel) // 数据库字段名下划线转驼峰
                                .enableFileOverride() // <--- 新增：开启文件覆盖


                                // Mapper 策略配置
                        .mapperBuilder()
                        .enableBaseResultMap()  // 生成 BaseResultMap
                        .enableBaseColumnList() // 生成 BaseColumnList
                        .formatMapperFileName("%sMapper") // Mapper 接口文件名格式
                        .formatXmlFileName("%sMapper") // XML 文件名格式
                                .enableFileOverride() // <--- 新增：开启文件覆盖
                )

                // 5. 模板引擎配置
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}