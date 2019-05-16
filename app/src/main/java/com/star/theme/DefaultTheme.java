package com.star.theme;

import androidx.annotation.ColorRes;

/**
 * Detail：默认主题
 * Author：Stars
 * Create Time：2019/4/29 9:28
 */
public interface DefaultTheme {

    @ColorRes
    int getColorId();

    @ColorRes
    int getColorOnId();

    @ColorRes
    int getColorFontId();

    @ColorRes
    int getColorFontReverseId();

    int getColor();

    int getColorOn();

    int getColorFont();

    int getColorFontReverse();

}
