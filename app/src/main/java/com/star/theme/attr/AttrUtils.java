package com.star.theme.attr;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.star.theme.attr.impl.AttrTypeBackground;
import com.star.theme.attr.impl.AttrTypeBackgroundTint;
import com.star.theme.attr.impl.AttrTypeImageSrc;
import com.star.theme.attr.impl.AttrTypeProgressDrawable;
import com.star.theme.attr.impl.AttrTypeTextColor;
import com.star.theme.attr.impl.AttrTypeTint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttrUtils {

    private static final int[][] SUPPORT_STYLE = new int[][] {
            new int[]{android.R.attr.background},
            new int[]{android.R.attr.textColor},
    };

    private static final String[] SUPPORT_STYLE_NAME = new String[] {
            "background",
            "textColor",
    };

    private static HashMap<String, AttrType> attrTypeHashMap = new HashMap<>();

    static {
        AttrType background = new AttrTypeBackground();
        attrTypeHashMap.put(background.getAttrType(), background);

        AttrType imageSrc = new AttrTypeImageSrc();
        attrTypeHashMap.put(imageSrc.getAttrType(), imageSrc);

        AttrType progressDrawable = new AttrTypeProgressDrawable();
        attrTypeHashMap.put(progressDrawable.getAttrType(), progressDrawable);

        AttrType textColor = new AttrTypeTextColor();
        attrTypeHashMap.put(textColor.getAttrType(), textColor);

        AttrType tint = new AttrTypeTint();
        attrTypeHashMap.put(tint.getAttrType(), tint);

        AttrType backgroundTint = new AttrTypeBackgroundTint();
        attrTypeHashMap.put(backgroundTint.getAttrType(), backgroundTint);
    }

    public static void addType(AttrType... attrTypes) {
        if (attrTypes == null || attrTypes.length == 0) return;
        for (AttrType attrType: attrTypes) {
            attrTypeHashMap.put(attrType.getAttrType(), attrType);
        }
    }

    private static AttrType getSupportAttrType(String attrName) {
        return attrTypeHashMap.get(attrName);
    }

    public static List<Attr> getAttrs(Object[] args, Resources resources) {
        List<Attr> attrs = new ArrayList<>();
        if (args != null && args.length > 0) {
            Context context = null;
            for (Object obj : args) {
                if (context == null && obj instanceof View) {
                    context = ((View) obj).getContext();
                }
                if (obj instanceof AttributeSet) {
                    int styleIndex = -1;
                    AttributeSet set = (AttributeSet) obj;
                    for (int i = 0; i < set.getAttributeCount(); i++) {
                        int attrId = set.getAttributeResourceValue(i, 0);
                        String attrName = set.getAttributeName(i);
                        if ("style".equals(attrName)) {
                            styleIndex = i;
                            continue;
                        }
                        String attrValue = set.getAttributeValue(i);
                        AttrType attrType = getSupportAttrType(attrName);
                        if (attrType == null) continue;
                        if (attrValue.startsWith("@")) {
                            String resourceName = attrType.getResourceName(attrValue, resources);
                            Attr attr = new Attr(attrId, resourceName, attrType);
                            attrs.add(attr);
                        }
                    }
                    if (styleIndex != -1) {
                        for (int t=0; t<SUPPORT_STYLE.length; t++) {
                            TypedArray ta = context.obtainStyledAttributes(set, SUPPORT_STYLE[t]);
                            int valueId = ta.getResourceId(0, View.NO_ID);
                            if (valueId != -1) {
                                AttrType attrType = getSupportAttrType(SUPPORT_STYLE_NAME[t]);
                                if (attrType != null) {
                                    String resourceName = resources.getResourceEntryName(valueId);
                                    Attr attr = new Attr(valueId, resourceName, attrType);
                                    attrs.add(attr);
                                }
                            }
                            ta.recycle();
                        }
                    }
                }
            }
        }
        return attrs;
    }
}
