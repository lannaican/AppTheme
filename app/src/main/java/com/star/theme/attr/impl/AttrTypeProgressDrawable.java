package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.star.theme.attr.AttrType;

public class AttrTypeProgressDrawable extends AttrType {

    public AttrTypeProgressDrawable() {
        super("progressDrawable");
    }

    @Override
    public void apply(View view, int resId, String resName) {

    }

    @Override
    public void applyChanged(View view, int resId, String resName) {
        if (TextUtils.isEmpty(resName)) return;
        Drawable drawable = getDrawable(view.getContext(), resName);
        if (drawable == null) return;
        ((ProgressBar)view).setProgressDrawable(drawable);
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
