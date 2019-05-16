package com.star.theme;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Detail：
 * Author：Stars
 * Create Time：2019/5/16 10:11
 */
public class AppThemeHelper {

    private final String namespace = "http://schemas.android.com/apk/res-auto";

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
        return attrs.getAttributeResourceValue(namespace, name, 0);
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
    public void replaceTextColor(AppCompatTextView view, AttributeSet attrs) {
        int colorId = getAttributeValue(attrs, attribute.textColor());
        if (colorId != 0) {
            view.setTextColor(getColor(colorId));
        }
    }

    /**
     * 替换图片着色
     */
    public void replaceColorFilter(AppCompatImageView view, AttributeSet attrs) {
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
        DefaultTheme theme = AppTheme.getDefaultTheme();
        if (colorId == theme.getColorId()) {
            return AppTheme.getColor();
        } else if (colorId == theme.getColorOnId()) {
            return AppTheme.getColorOn();
        } else if (colorId == theme.getColorFontId()) {
            return AppTheme.getColorFont();
        } else if (colorId == theme.getColorFontReverseId()) {
            return AppTheme.getColorFontReverse();
        }
        return context.getResources().getColor(colorId);
    }

}
