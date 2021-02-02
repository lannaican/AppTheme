package com.star.theme;

/**
 * 说明：
 * 时间：2021/1/31 19:25
 */
public interface Theme {

    boolean isDark();

    int getWindowBackgroundId();

    int getColor(int colorId);

}
