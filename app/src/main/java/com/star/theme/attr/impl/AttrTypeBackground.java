package com.star.theme.attr.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.star.theme.AppTheme;
import com.star.theme.attr.AttrType;


public class AttrTypeBackground extends AttrType {

    public AttrTypeBackground() {
        super("background");
    }

    @Override
    public void apply(View view, int resId, String resName) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            view.setBackgroundColor(AppTheme.getInstance().getColor(resId));
        }
    }

    @Override
    public void applyChanged(View view, int resId, String resName) {
        Context context = view.getContext();
        try {
            int findResId = context.getResources().getIdentifier(resName, DRAWABLE, context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(findResId);
            view.setBackground(drawable);
        } catch (Resources.NotFoundException e) {
            try {
                int findResId = context.getResources().getIdentifier(resName, MIPMAP, context.getPackageName());
                Drawable drawable = context.getResources().getDrawable(findResId);
                view.setBackground(drawable);
            } catch (Resources.NotFoundException e1) {
                try {
                    view.setBackground(new ColorDrawable(AppTheme.getInstance().getColor(resId)));
                } catch (Resources.NotFoundException e2) {

                }
            }
        }
        view.jumpDrawablesToCurrentState();
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
