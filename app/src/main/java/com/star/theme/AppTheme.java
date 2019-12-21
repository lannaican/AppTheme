package com.star.theme;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.LayoutInflaterCompat;

/**
 * Detail：
 * Author：Stars
 * Create Time：2019/3/18 14:25
 */
public class AppTheme {

    private static AppTheme instance;

    public static AppTheme getInstance() {
        if (instance == null) {
            instance = new AppTheme();
        }
        return instance;
    }

    //缓存色
    private int color;
    private int colorOn;
    private int colorFont;
    private int dark;

    //默认主题色
    private DefaultTheme defaultTheme;

    //布局构造器
    private AppThemeFactory factory;

    //持久存储
    private AppThemeStorage storage;

    private OnAppThemeListener onAppThemeListener;

    private AppTheme() {}

    /**
     * 初始化
     */
    public void init(@NonNull ViewCreator creator, @NonNull AppThemeStorage storage,
                     @NonNull DefaultTheme defaultTheme) {
        this.factory = new AppThemeFactory(creator);
        this.storage = storage;
        this.defaultTheme = defaultTheme;
    }

    public void setListener(OnAppThemeListener callBack) {
        this.onAppThemeListener = callBack;
    }

    /**
     * 对每个Activity进行注册
     */
    public void register(Context context) {
        LayoutInflaterCompat.setFactory2(LayoutInflater.from(context), factory);
    }

    /**
     * 是否是夜间模式
     */
    public boolean isNight() {
        return storage.getBoolean(AppThemeKey.Night);
    }

    /**
     * 设置夜间模式
     */
    public void setNight(boolean night) {
        storage.set(AppThemeKey.Night, night);
        AppCompatDelegate.setDefaultNightMode(night ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        clearCache();
        callListener();
    }

    /**
     * 设置主题
     */
    public void setColors(int color, int colorOn, boolean isDark) {
        this.color = color;
        this.colorOn = colorOn;
        this.dark = isDark ? 1 : 2;
        this.colorFont = 0;
        storage.set(AppThemeKey.Color, color);
        storage.set(AppThemeKey.Color_On, colorOn);
        storage.set(AppThemeKey.Color_Dark, isDark);
        callListener();
    }

    /**
     * 设置主题色
     */
    public void setColor(int color) {
        storage.set(AppThemeKey.Color, color);
        this.color = color;
        this.colorFont = 0;
        callListener();
    }

    /**
     * 设置强调色
     */
    public void setColorOn(int color) {
        storage.set(AppThemeKey.Color_On, color);
        this.colorOn = color;
        callListener();
    }

    /**
     * 设置是否暗色
     */
    public void setColorDark(boolean dark) {
        storage.set(AppThemeKey.Color_Dark, dark);
        this.dark = dark ? 1 : 2;
        colorFont = 0;
        callListener();
    }

    /**
     * 获取主题色
     */
    public int getColor() {
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
    public int getColorOn() {
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
    public int getColorFont() {
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
    public int getColorFontReverse() {
        return isDark() ? defaultTheme.getColorFont() : defaultTheme.getColorFontReverse();
    }

    /**
     * 暗色主题
     */
    public boolean isDark() {
        if (dark == 0) {
            boolean isDark = storage.getBoolean(AppThemeKey.Color_Dark, defaultTheme.isDark());
            dark = isDark ? 1 : 2;
        }
        return dark == 1;
    }

    /**
     * 获取默认主题
     */
    public DefaultTheme getDefaultTheme() {
        return defaultTheme;
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        color = 0;
        colorOn = 0;
        colorFont = 0;
        defaultTheme.clear();
    }

    private void callListener() {
        if (onAppThemeListener != null) {
            onAppThemeListener.onChanged();
        }
    }

}
