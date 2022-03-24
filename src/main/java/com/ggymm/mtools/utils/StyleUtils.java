package com.ggymm.mtools.utils;

import javafx.scene.Scene;

/**
 * @author gongym
 * @version 创建时间: 2022-03-01 10:24
 */
public class StyleUtils {

    public static void initStyle(Scene scene) {
        // 设置自定义样式
        final String[] styles = new String[]{
                "assets/css/main.css",
                "assets/css/jfoenix.css",

                "assets/css/button.css",

                "assets/css/checkbox.css",
                "assets/css/input.css",
                "assets/css/select.css",
                "assets/css/option.css",

                "assets/css/list-view.css",

                "assets/css/console.css",
                "assets/css/cover.css",
        };
        scene.getStylesheets().addAll(styles);
    }
}
