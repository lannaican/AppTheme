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
        int drawableId = getDrawableId(view.getContext(), resName);
        if (drawableId == 0) return;
        view.setBackgroundResource(drawableId);
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
