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

    public DefaultTheme(Context context) {
        Resources resources = context.getResources();
        color = resources.getColor(getColorId());
        colorOn = resources.getColor(getColorOnId());
        colorFont = resources.getColor(getColorFontId());
        colorFontReverse = resources.getColor(getColorFontReverseId());
    }

    @ColorRes
    public abstract int getColorId();

    @ColorRes
    public abstract int getColorOnId();

    @ColorRes
    public abstract int getColorFontId();

    @ColorRes
    public abstract int getColorFontReverseId();

    public int getColor() {
        return color;
    }

    public int getColorOn() {
        return colorOn;
    }

    public int getColorFont() {
        return colorFont;
    }

    public int getColorFontReverse() {
        return colorFontReverse;
    }

}
