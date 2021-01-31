package com.star.theme.attr;

import android.content.res.Resources;
import android.util.AttributeSet;

import com.star.theme.attr.impl.AttrTypeBackground;
import com.star.theme.attr.impl.AttrTypeImageSrc;
import com.star.theme.attr.impl.AttrTypeProgressDrawable;
import com.star.theme.attr.impl.AttrTypeTextColor;
import com.star.theme.attr.impl.AttrTypeTextStyle;
import com.star.theme.attr.impl.AttrTypeTint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttrUtils {

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

        AttrType textStyle = new AttrTypeTextStyle();
        attrTypeHashMap.put(textStyle.getAttrType(), textStyle);

        AttrType tint = new AttrTypeTint();
        attrTypeHashMap.put(tint.getAttrType(), tint);
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
            for (Object obj: args) {
                if (obj instanceof AttributeSet) {
                    AttributeSet set = (AttributeSet) obj;
                    for (int i = 0; i < set.getAttributeCount(); i++) {
                        String attrName = set.getAttributeName(i);
                        String attrValue = set.getAttributeValue(i);
                        AttrType attrType = getSupportAttrType(attrName);
                        if (attrType == null) continue;
                        if (attrValue.startsWith("@")) {
                            String resourceName = attrType.getResourceName(attrValue, resources);
                            Attr attr = new Attr(resourceName, attrType);
                            attrs.add(attr);
                        }
                    }
                }
            }
        }
        return attrs;
    }
}
