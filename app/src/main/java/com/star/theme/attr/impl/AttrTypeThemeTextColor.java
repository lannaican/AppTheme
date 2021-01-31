package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.star.theme.AppTheme;
import com.star.theme.attr.AttrType;

public class AttrTypeThemeTextColor extends AttrType {

    public AttrTypeThemeTextColor() {
        super("themeTextColor");
    }

    @Override
    public void apply(View view, String resName) {
        if (TextUtils.isEmpty(resName)) return;
        Resources mResources = view.getResources();
        int resId = mResources.getIdentifier(resName, COLOR, view.getContext().getPackageName());
        if (0 != resId) {
            int color = AppTheme.getInstance().getColor(resId);
            ((TextView) view).setTextColor(color);
        }
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
