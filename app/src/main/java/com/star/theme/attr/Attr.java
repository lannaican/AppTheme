package com.star.theme.attr;

import android.view.View;

public class Attr {

    String resName;
    AttrType attrType;

    public Attr(String resName, AttrType attrType) {
        this.resName = resName;
        this.attrType = attrType;
    }

    public void apply(View view) {
        attrType.apply(view, resName);
    }
}
