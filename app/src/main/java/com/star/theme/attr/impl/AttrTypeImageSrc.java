package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;

import com.star.theme.attr.AttrType;

public class AttrTypeImageSrc extends AttrType {

    public AttrTypeImageSrc() {
        super("src");
    }

    @Override
    public void apply(View view, int resId, String resName) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resId);
        }
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
