package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.star.theme.AppTheme;
import com.star.theme.attr.AttrType;

public class AttrTypeTextColor extends AttrType {

    public AttrTypeTextColor() {
        super("textColor");
    }

    @Override
    public void apply(View view, int resId, String resName) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(AppTheme.getInstance().getColor(resId));
        }
    }

    @Override
    public void applyChanged(View view, int resId, String resName) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(AppTheme.getInstance().getColor(resId));
        }
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
