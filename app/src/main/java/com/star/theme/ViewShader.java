package com.star.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Detail：
 * Author：Stars
 * Create Time：2019/5/16 10:18
 */
public interface ViewShader {

    View shaderView(AppThemeHelper helper, Context context, String name, AttributeSet attrs);

}
