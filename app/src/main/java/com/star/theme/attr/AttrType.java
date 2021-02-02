package com.star.theme.attr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;


public abstract class AttrType {

    protected static final String DRAWABLE = "drawable";
    protected static final String MIPMAP = "mipmap";
    protected static final String COLOR = "color";
    protected static final String STYLE = "style";

    String attrType;

    public AttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getAttrType()
    {
        return attrType;
    }

    /**
     * 构造布局
     */
    public abstract void apply(View view, int resId, String resName);

    /**
     * 主题改变
     */
    public abstract void applyChanged(View view, int resId, String resName);

    public abstract String getResourceName(String attrValue, Resources resources);

    protected String getIntResourceName(String attrValue, Resources resources) {
        int id = Integer.parseInt(attrValue.substring(1));
        if (id==0) return null;
        return resources.getResourceEntryName(id);
    }

    protected Drawable getDrawable(Context context, String resName) {
        Drawable drawable = null;
        Resources resources = context.getResources();
        try {
            drawable = resources.getDrawable(resources.getIdentifier(resName, DRAWABLE,  context.getPackageName()));
        } catch (Resources.NotFoundException e) {
            try {
                drawable = resources.getDrawable(resources.getIdentifier(resName, MIPMAP,  context.getPackageName()));
            } catch (Resources.NotFoundException e2) {
                try {
                    drawable = resources.getDrawable(resources.getIdentifier(resName, COLOR,  context.getPackageName()));
                } catch (Resources.NotFoundException e3) {
                }
            }
        }
        return drawable;
    }

    protected @DrawableRes
    int getDrawableId(Context context, String resName) {
        Resources resources = context.getResources();
        return resources.getIdentifier(resName, DRAWABLE, context.getPackageName());
    }

}
