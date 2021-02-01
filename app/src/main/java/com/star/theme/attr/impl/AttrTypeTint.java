package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import com.star.theme.AppTheme;
import com.star.theme.attr.AttrType;

public class AttrTypeTint extends AttrType {

    public AttrTypeTint() {
        super("tint");
    }

    @Override
    public void apply(View view, int resId, String resName) {
        ((ImageView) view).setColorFilter(AppTheme.getInstance().getColor(resId));
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
