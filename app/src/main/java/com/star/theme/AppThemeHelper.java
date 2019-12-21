package com.star.theme;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Detail：
 * Author：Stars
 * Create Time：2019/5/16 10:11
 */
public class AppThemeHelper {

    private Context context;
    private AppThemeAttribute attribute;


    public AppThemeHelper(Context context, AppThemeAttribute attribute) {
        this.context = context;
        this.attribute = attribute;
    }

    /**
     * 获取Attr值
     */
    public int getAttributeValue(AttributeSet attrs, String name) {
        return attrs.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", name, 0);
    }

    /**
     * 替换背景色
     */
    public void replaceBackgroundColor(View view, AttributeSet attrs) {
        int colorId = getAttributeValue(attrs, attribute.backgroundColor());
        if (colorId != 0) {
            view.setBackgroundColor(getColor(colorId));
        }
    }

    /**
     * 替换字体色
     */
    public void replaceTextColor(TextView view, AttributeSet attrs) {
        int colorId = getAttributeValue(attrs, attribute.textColor());
        if (colorId != 0) {
            view.setTextColor(getColor(colorId));
        }
    }

    /**
     * 替换提示字体色
     */
    public void replaceHintTextColor(EditText view, AttributeSet attrs) {
        int colorId = getAttributeValue(attrs, attribute.hinTextColor());
        if (colorId != 0) {
            view.setHintTextColor(alpha(getColor(colorId), 0.6F));
        }
    }

    /**
     * 替换图片着色
     */
    public void replaceColorFilter(ImageView view, AttributeSet attrs) {
        int colorId = getAttributeValue(attrs, attribute.colorFilter());
        if (colorId != 0) {
            view.setColorFilter(getColor(colorId));
        }
    }

    /**
     * 背景着色
     */
    public void replaceBackgroundTint(View view, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int colorId = getAttributeValue(attrs, attribute.themeBackgroundTint());
            if (colorId != 0) {
                view.setBackgroundTintList(ColorStateList.valueOf(getColor(colorId)));
            }
        }
    }

    /**
     * 根据颜色ID获取颜色
     */
    public int getColor(int colorId) {
        AppTheme appTheme = AppTheme.getInstance();
        DefaultTheme theme = appTheme.getDefaultTheme();
        if (colorId == theme.getColorId()) {
            return appTheme.getColor();
        } else if (colorId == theme.getColorOnId()) {
            return appTheme.getColorOn();
        } else if (colorId == theme.getColorFontId()) {
            return appTheme.getColorFont();
        } else if (colorId == theme.getColorFontReverseId()) {
            return appTheme.getColorFontReverse();
        }
        return context.getResources().getColor(colorId);
    }

    private int alpha(int color, float alpha) {
        int r = (color >> 16) & 0xff;
        int g = (color >>  8) & 0xff;
        int b = (color      ) & 0xff;
        return Color.argb((int)(255 * alpha), r, g, b);
    }

}
