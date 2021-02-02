package com.star.theme.attr;

import android.view.View;

import com.star.theme.attr.AttrType;

public class Attr {

    int resId;
    String resName;
    AttrType attrType;

    public Attr(int resId, String resName, AttrType attrType) {
        this.resId = resId;
        this.resName = resName;
        this.attrType = attrType;
    }

    public void apply(View view, boolean changed) {
        if (resId != 0) {
            if (changed) {
                attrType.applyChanged(view, resId, resName);
            } else {
                attrType.apply(view, resId, resName);
            }
        }
    }
}
