package com.star.theme.attr;

import android.view.View;

import com.star.theme.attr.Attr;

import java.lang.ref.SoftReference;
import java.util.List;


public class AttrView {

    SoftReference<View> view;
    List<Attr> attrs;

    public AttrView(View view, List<Attr> attrs) {
        this.view = new SoftReference<>(view);
        this.attrs = attrs;
    }

    public void apply(boolean changed) {
        if (null == view || view.get() == null) return;
        for (Attr attr : attrs) {
            attr.apply(view.get(), changed);
        }
    }
}
