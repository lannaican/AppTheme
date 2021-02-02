package com.star.theme.attr.impl;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.View;

import com.star.theme.AppTheme;
import com.star.theme.attr.AttrType;


public class AttrTypeBackgroundTint extends AttrType {

    public AttrTypeBackgroundTint() {
        super("backgroundTint");
    }

    @Override
    public void apply(View view, int resId, String resName) {
        view.setBackgroundTintList(ColorStateList.valueOf(AppTheme.getInstance().getColor(resId)));
    }

    @Override
    public void applyChanged(View view, int resId, String resName) {
        view.setBackgroundTintList(ColorStateList.valueOf(AppTheme.getInstance().getColor(resId)));
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
