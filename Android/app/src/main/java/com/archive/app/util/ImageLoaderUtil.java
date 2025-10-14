package com.archive.app.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.archive.app.R;

public class ImageLoaderUtil {
    /**
     * 根据 imageUrl 规则加载图片：
     * 1. 如果 imageUrl 以 https://via.placeholder.com 开头，则取最后一个等号后的内容作为图片名加载本地 drawable。
     * 2. 如果 imageUrl 不以 http/https 开头，则直接作为 drawable 名称加载本地图片。
     * 3. 否则按网络图片加载。
     */
    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        System.out.println("imageUrl: " + imageUrl);
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.circle_background);
            return;
        }
        if (imageUrl.startsWith("http") && imageUrl.contains("via.placeholder.com")) {
            // 取最后一个 = 后的内容作为图片名
            int idx = imageUrl.lastIndexOf('=');
            String name = idx != -1 ? imageUrl.substring(idx + 1) : null;
            if (name != null && !name.isEmpty()) {
                int resId = context.getResources().getIdentifier(name.toLowerCase(), "drawable", context.getPackageName());
                if (resId != 0) {
                    imageView.setImageResource(resId);
                    return;
                }
            }
        }
        // "https://cdn.example.com/images/promo_subway.png",需要兼容
        if (imageUrl.startsWith("https://cdn.example.com/images/")) {
            // 截取图片名作为图片名
            int idx = imageUrl.lastIndexOf('/');
            String name = idx != -1 ? imageUrl.substring(idx + 1) : null;
            System.out.println("name: " + name);
            // name: promo_new_user.png 去掉 .png 后缀
            if (name != null && name.endsWith(".png")) {
                name = name.substring(0, name.length() - 4);
            }
            if (name != null && !name.isEmpty()) {
                int resId = context.getResources().getIdentifier(name.toLowerCase(), "drawable",
                        context.getPackageName());
                // 如果找到了对应的 drawable 资源
                System.out.println("resId: " + resId);
                if (resId != 0) {
                    imageView.setImageResource(resId);
                    return;
                }
            }
        } else if (!imageUrl.startsWith("http")) {
            // 直接用 imageUrl 作为 drawable 名称
            int resId = context.getResources().getIdentifier(imageUrl.toLowerCase(), "drawable", context.getPackageName());
            if (resId != 0) {
                imageView.setImageResource(resId);
                return;
            }
        }
        // 默认用 Glide 加载网络图片
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions().placeholder(R.drawable.promo_new_user).error(R.drawable.promo_new_user))
                .into(imageView);
    }
} 