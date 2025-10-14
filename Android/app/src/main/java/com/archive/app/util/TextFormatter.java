package com.archive.app.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TextFormatter {

    private static final String TAG = "TextFormatter";

    public static String formatText(String text) {
        // 1. 分割字符串
        List<String> parts = Arrays.asList(text.split(","));

        // 2. 过滤掉 null 值
        List<String> filteredParts = parts.stream()
                .map(String::trim) // 去除空格
                .filter(part -> !("null".equals(part) || part.isEmpty())) // 过滤 null 和空字符串
                .collect(Collectors.toList());

        // 3. 转换日期格式
        for (int i = 0; i < filteredParts.size(); i++) {
            String part = filteredParts.get(i);
            try {
                // 尝试解析日期
                SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                Date date = originalFormat.parse(part);

                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = targetFormat.format(date);
                filteredParts.set(i, formattedDate); // 替换为格式化后的日期
            } catch (ParseException e) {
                // 如果解析失败，说明不是日期，则忽略
                Log.e(TAG, "日期解析失败: " + part, e);
            }
        }

        // 4. 构建带提示词的格式化文本
        StringBuilder result = new StringBuilder("你的日程详情是：");

        // 根据位置添加相应的提示词
        for (int i = 0; i < filteredParts.size(); i++) {
            String part = filteredParts.get(i);
            switch (i) {
                case 0:
                    result.append("\n日程标题：").append(part);
                    break;
                case 1:
                    result.append("\n日程时间：").append(part);
                    break;
                case 2:
                    result.append("\n日程地点：").append(part);
                    break;
                default:
                    // 对于其他字段，简单追加
                    result.append("\n").append(part);
                    break;
            }
        }

        return result.toString();
    }

    public static String formatText00(String text) {
        // 1. 分割字符串
        List<String> parts = Arrays.asList(text.split(","));

        // 2. 过滤掉 null 值
        List<String> filteredParts = parts.stream()
                .map(String::trim) // 去除空格
                .filter(part -> !("null".equals(part) || part.isEmpty())) // 过滤 null 和空字符串
                .collect(Collectors.toList());

        // 3. 转换日期格式
        String formattedDate = null;
        for (int i = 0; i < filteredParts.size(); i++) {
            String part = filteredParts.get(i);
            try {
                // 尝试解析日期
                SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                Date date = originalFormat.parse(part);

                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formattedDate = targetFormat.format(date);
                filteredParts.set(i, formattedDate); // 替换为格式化后的日期
            } catch (ParseException e) {
                // 如果解析失败，说明不是日期，则忽略
                Log.e(TAG, "日期解析失败: " + part, e);
            }
        }

        // 4. 将过滤和格式化后的部分重新组合成字符串
        return String.join(",", filteredParts);
    }

}