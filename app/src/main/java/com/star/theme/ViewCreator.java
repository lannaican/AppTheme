package com.star.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

/**
 * Detail：
 * Author：Stars
 * Create Time：2019/4/29 9:01
 */
public abstract class ViewCreator {

    private AppThemeHelper helper;

    public ViewCreator(Context context, AppThemeAttribute attribute) {
        this.helper = new AppThemeHelper(context, attribute);
    }

    public abstract Map<String, ViewShader> getShaderMap();

    View createView(Context context, String name, AttributeSet attrs) {
        ViewShader shader = getShaderMap().get(name);
        if (shader != null) {
            return shader.shaderView(helper, context, name, attrs);
        }
        return null;
    }

}
