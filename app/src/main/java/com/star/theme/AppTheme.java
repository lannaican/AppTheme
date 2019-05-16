package com.star.theme;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.LayoutInflater;

/**
 * Detail：
 * Author：Stars
 * Create Time：2019/3/18 14:25
 */
public class AppTheme {

    //默认主题色
    private static DefaultTheme defaultTheme;

    //色值用于缓存，不需要每次读取
    private static int color;
    private static int colorOn;
    private static int colorFont;
    private static int dark;

    //布局构造器
    private static AppThemeFactory factory;

    //持久存储
    private static AppThemeStorage storage;

    private AppTheme() {}

    /**
     * 初始化
     */
    public static void init(@NonNull ViewCreator creator, @NonNull AppThemeStorage storage,
                            @NonNull DefaultTheme defaultTheme) {
        AppTheme.factory = new AppThemeFactory(creator);
        AppTheme.storage = storage;
        AppTheme.defaultTheme = defaultTheme;
    }

    /**
     * 对每个Activity进行注册
     */
    public static void register(Context context) {
        LayoutInflaterCompat.setFactory2(LayoutInflater.from(context), factory);
    }

    /**
     * 是否是夜间模式
     */
    public static boolean isNight() {
        return storage.getBoolean(AppThemeKey.Night);
    }

    /**
     * 设置夜间模式
     */
    public static void setNight(boolean night) {
        storage.set(AppThemeKey.Night, night);
        AppCompatDelegate.setDefaultNightMode(night ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        clearCache();
    }

    /**
     * 设置主题色
     */
    public static void setColor(int color) {
        storage.set(AppThemeKey.Color, color);
        AppTheme.color = color;
        colorFont = 0;
    }

    /**
     * 设置强调色
     */
    public static void setColorOn(int color) {
        storage.set(AppThemeKey.Color_On, color);
        AppTheme.colorOn = color;
    }

    /**
     * 设置是否暗色
     */
    public static void setColorDark(boolean dark) {
        storage.set(AppThemeKey.Color_Dark, dark);
        AppTheme.dark = dark ? 1 : 2;
        colorFont = 0;
    }

    /**
     * 获取主题色
     */
    public static int getColor() {
        if (color == 0) {
            if (isNight()) {
                color = defaultTheme.getColor();
            } else {
                color = storage.getInt(AppThemeKey.Color, defaultTheme.getColor());
            }
        }
        return color;
    }

    /**
     * 获取主题强调色
     */
    public static int getColorOn() {
        if (colorOn == 0) {
            if (isNight()) {
                colorOn = defaultTheme.getColorOn();
            } else {
                colorOn = storage.getInt(AppThemeKey.Color_On, defaultTheme.getColorOn());
            }
        }
        return colorOn;
    }

    /**
     * 获取主题字体色
     */
    public static int getColorFont() {
        if (colorFont == 0) {
            if (isNight()) {
                colorFont = defaultTheme.getColorFont();
            } else {
                colorFont = isDark() ? defaultTheme.getColorFontReverse() : defaultTheme.getColorFont();
            }
        }
        return colorFont;
    }

    /**
     * 获取主题字体色反色
     */
    public static int getColorFontReverse() {
        return isDark() ? defaultTheme.getColorFont() : defaultTheme.getColorFontReverse();
    }

    /**
     * 暗色主题
     */
    public static boolean isDark() {
        if (dark == 0) {
            boolean isDark = storage.getBoolean(AppThemeKey.Color_Dark, false);
            dark = isDark ? 1 : 2;
        }
        return dark == 1;
    }

    /**
     * 获取默认主题
     */
    public static DefaultTheme getDefaultTheme() {
        return defaultTheme;
    }

    private static void clearCache() {
        color = 0;
        colorOn = 0;
        colorFont = 0;
        defaultTheme.update();
    }

}
