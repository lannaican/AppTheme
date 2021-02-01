package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.star.theme.attr.AttrType;

public class AttrTypeTextColor extends AttrType {

    public AttrTypeTextColor() {
        super("textColor");
    }

    @Override
    public void apply(View view, String resName) {
        if (TextUtils.isEmpty(resName)) return;
        ((TextView) view).setTextColor(getColor(view.getContext(), resName));
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
