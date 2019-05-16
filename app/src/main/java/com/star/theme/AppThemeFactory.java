package com.star.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Detail：视图构造器
 * Author：Stars
 * Create Time：2019/3/18 14:26
 */
public class AppThemeFactory implements LayoutInflater.Factory2 {

    private ViewCreator viewCreator;

    AppThemeFactory(ViewCreator creator) {
        this.viewCreator = creator;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (viewCreator != null) {
            View view = viewCreator.createView(context, name, attrs);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

}
