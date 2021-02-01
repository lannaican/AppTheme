package com.star.theme.attr.impl;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.star.theme.attr.AttrType;

public class AttrTypeTextColor extends AttrType {

    public AttrTypeTextColor() {
        super("textColor");
    }

    @Override
    public void apply(View view, int resId, String resName) {
        Resources resources = view.getResources();
        try {
            ColorStateList colorList = resources.getColorStateList(resId);
            ((TextView) view).setTextColor(colorList);
        } catch (Resources.NotFoundException e) {
            ((TextView) view).setTextColor(getColor(view.getContext(), resName));
        }
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
