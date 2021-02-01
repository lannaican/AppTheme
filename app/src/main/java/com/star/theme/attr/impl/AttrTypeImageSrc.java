package com.star.theme.attr.impl;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.star.theme.attr.AttrType;

/**
 * Created by like on 2017/7/25.
 */

public class AttrTypeImageSrc extends AttrType {

    public AttrTypeImageSrc() {
        super("src");
    }

    @Override
    public void apply(View view, String resName) {
        if (TextUtils.isEmpty(resName)) return;
        if (view instanceof ImageView) {
            int drawableId = getDrawableId(view.getContext(), resName);
            if (drawableId == 0) return;
            ((ImageView) view).setImageResource(drawableId);
        }
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}
