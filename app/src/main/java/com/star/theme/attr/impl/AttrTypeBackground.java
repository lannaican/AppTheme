package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.star.theme.attr.AttrType;


public class AttrTypeBackground extends AttrType {

    public AttrTypeBackground() {
        super("background");
    }

    @Override
    public void apply(View view, String resName) {
        if (TextUtils.isEmpty(resName)) return;
        Drawable drawable = getDrawable(view.getContext(), resName);
        if (drawable == null) return;
        view.setBackground(drawable);
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
