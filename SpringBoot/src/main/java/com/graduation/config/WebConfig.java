package com.graduation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir:src/main/resources/static/image}")
    private String uploadDir;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置图片资源访问
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + uploadDir + "/")
                .setCachePeriod(3600); // 缓存时间1小时
    }

    /**
     * 配置跨域支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 实际部署时应当限制为特定域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

}