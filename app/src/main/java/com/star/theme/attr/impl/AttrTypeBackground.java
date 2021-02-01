package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;

import com.star.theme.AppTheme;
import com.star.theme.attr.AttrType;


public class AttrTypeBackground extends AttrType {

    public AttrTypeBackground() {
        super("background");
    }

    @Override
    public void apply(View view, int resId, String resName) {
        if (TextUtils.isEmpty(resName)) return;
        try {
            view.setBackgroundResource(0);
            view.setBackgroundResource(resId);
        } catch (Resources.NotFoundException e) {
            view.setBackgroundColor(AppTheme.getInstance().getColor(resId));
        }
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
