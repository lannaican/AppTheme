package com.star.theme;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.ColorRes;

/**
 * Detail：默认主题
 * Author：Stars
 * Create Time：2019/4/29 9:28
 */
public abstract class DefaultTheme {

    private int color;
    private int colorOn;
    private int colorFont;
    private int colorFontReverse;

    @ColorRes
    public abstract int getColorId();

    @ColorRes
    public abstract int getColorOnId();

    @ColorRes
    public abstract int getColorFontId();

    @ColorRes
    public abstract int getColorFontReverseId();

    public abstract boolean isDark();

    public abstract int getColor(int colorId);

    public int getColor() {
        if (color == 0) {
            color = getColor(getColorId());
        }
        return color;
    }

    public int getColorOn() {
        if (colorOn == 0) {
            colorOn = getColor(getColorOnId());
        }
        return colorOn;
    }

    public int getColorFont() {
        if (colorFont == 0) {
            colorFont = getColor(getColorFontId());
        }
        return colorFont;
    }

    public int getColorFontReverse() {
        if (colorFontReverse == 0) {
            colorFontReverse = getColor(getColorFontReverseId());
        }
        return colorFontReverse;
    }


    /**
     * 重新获取颜色
     */
    void clear() {
        color = 0;
        colorOn = 0;
        colorFont = 0;
        colorFontReverse = 0;
    }

}
