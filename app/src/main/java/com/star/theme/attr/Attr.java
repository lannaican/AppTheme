package com.star.theme.attr;

import android.view.View;

public class Attr {

    int resId;
    String resName;
    AttrType attrType;

    public Attr(int resId, String resName, AttrType attrType) {
        this.resId = resId;
        this.resName = resName;
        this.attrType = attrType;
    }

    public void apply(View view) {
        if (resId != 0) {
            attrType.apply(view, resId, resName);
        }
    }
}
