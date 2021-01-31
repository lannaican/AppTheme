package com.star.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.ArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.LayoutInflaterFactory;

import com.star.theme.attr.Attr;
import com.star.theme.attr.AttrUtils;
import com.star.theme.attr.AttrView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppTheme {

    private static AppTheme instance;

    private SparseArrayCompat<List<AttrView>> attrViewMaps = new SparseArrayCompat<>();

    private static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap<>();
    private final Object[] mConstructorArgs = new Object[2];
    private static final Class<?>[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};

    public static AppTheme getInstance() {
        if (instance == null) {
            instance = new AppTheme();
        }
        return instance;
    }

    //持久存储
    private Storage storage;

    //缓存色值
    private SparseIntArray colorCache = new SparseIntArray();

    //是否暗色
    private Boolean isDark;

    //默认主题
    private Theme defaultTheme;
    //当前主题
    private Theme currentTheme;

    private OnAppThemeListener onAppThemeListener;

    private AppTheme() {}

    /**
     * 初始化
     */
    public void init(@NonNull Storage storage, @NonNull Theme defaultTheme) {
        this.storage = storage;
        this.defaultTheme = defaultTheme;
        this.currentTheme = storage.getTheme(StorageKey.Theme);
        if (this.currentTheme == null) {
            this.currentTheme = defaultTheme;
        }
    }

    public void setTheme(@NonNull Theme theme) {
        this.currentTheme = theme;
        update();
        callListener();
    }

    public void setListener(OnAppThemeListener callBack) {
        this.onAppThemeListener = callBack;
    }

    /**
     * 注册
     */
    public void attach(AppCompatActivity activity) {
        if (activity.getDelegate() instanceof LayoutInflaterFactory) {
            LayoutInflaterFactory originInflaterFactory = (LayoutInflaterFactory) activity.getDelegate();
            LayoutInflaterFactory proxyInflaterFactory = (LayoutInflaterFactory) Proxy.newProxyInstance(
                    originInflaterFactory.getClass().getClassLoader(),
                    new Class[]{LayoutInflaterFactory.class},
                    new InflateFactoryHandler<>(originInflaterFactory, activity));
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            LayoutInflaterCompat.setFactory(layoutInflater, proxyInflaterFactory);
        } else if (activity.getDelegate() instanceof LayoutInflater.Factory2) {
            LayoutInflater.Factory2 originInflaterFactory = (LayoutInflater.Factory2) activity.getDelegate();
            LayoutInflater.Factory2 proxyInflaterFactory = (LayoutInflater.Factory2) Proxy.newProxyInstance(
                    originInflaterFactory.getClass().getClassLoader(),
                    new Class[]{LayoutInflater.Factory2.class},
                    new InflateFactoryHandler<>(originInflaterFactory, activity));
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            LayoutInflaterCompat.setFactory2(layoutInflater, proxyInflaterFactory);
        }
    }

    /**
     * 取消注册
     */
    public void detach(AppCompatActivity activity) {
        attrViewMaps.remove(activity.hashCode());
    }

    /**
     * 是否是夜间模式
     */
    public boolean isNight() {
        return storage.getBoolean(StorageKey.Night);
    }

    /**
     * 设置夜间模式
     */
    public void setNight(boolean night) {
        storage.set(StorageKey.Night, night);
        AppCompatDelegate.setDefaultNightMode(night ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        update();
        callListener();
    }

    /**
     * 设置是否暗色
     */
    public void setColorDark(boolean dark) {
        storage.set(StorageKey.Color_Dark, dark);
        isDark = dark;
        update();
        callListener();
    }

    /**
     * 获取主题色
     */
    public int getColor(int colorId) {
        int color = colorCache.get(colorId, -1);
        if (color == -1) {
            if (isNight()) {
                color = defaultTheme.getColor(colorId);
            } else {
                color = currentTheme.getColor(colorId);
            }
            colorCache.put(colorId, color);
        }
        return color;
    }

    /**
     * 暗色主题
     */
    public boolean isDark() {
        if (isDark == null) {
            isDark = storage.getBoolean(StorageKey.Color_Dark, defaultTheme.isDark());
        }
        return isDark;
    }

    /**
     * 实时更新UI
     */
    private void update() {
        clearCache();
        int count = attrViewMaps.size();
        for (int i=0; i<count; i++) {
            List<AttrView> attrViews = attrViewMaps.valueAt(i);
            for (AttrView attrView : attrViews) {
                attrView.apply();
            }
        }
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        colorCache.clear();
        isDark = null;
    }

    private void callListener() {
        if (onAppThemeListener != null) {
            onAppThemeListener.onChanged();
        }
    }


    /*****************************************************************************/

    class InflateFactoryHandler<T> implements InvocationHandler {

        private T inflaterFactory;
        private AppCompatActivity activity;

        public InflateFactoryHandler(T inflaterFactory, AppCompatActivity activity) {
            this.inflaterFactory = inflaterFactory;
            this.activity = activity;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            try {
                result = method.invoke(inflaterFactory, args);
            } catch (Exception e) { }

            List<Attr> attrs = AttrUtils.getAttrs(args, activity.getResources());
            if (attrs.isEmpty()) {
                return result;
            }
            if (result == null) {
                result = createViewFromTag((Context)args[2], (String)args[1], (AttributeSet)args[3]);
            }
            if (attrs.size() > 0) {
                AttrView attrView = new AttrView((View) result, attrs);
                putAttrView(attrView, activity.hashCode());
                if (result != null) {
                    attrView.apply();
                }
            }
            return result;
        }

        private void putAttrView(AttrView attrView, int hashCode) {
            List<AttrView> attrViews;
            if (attrViewMaps.indexOfKey(hashCode) > -1) {
                attrViews = attrViewMaps.get(hashCode);
            } else {
                attrViews = new ArrayList<>();
            }
            attrViews.add(attrView);
            attrViewMaps.put(hashCode, attrViews);
        }

        private View createViewFromTag(Context context, String name, AttributeSet attrs) {
            if (name.equals("view")) {
                name = attrs.getAttributeValue(null, "class");
            }

            try {
                mConstructorArgs[0] = context;
                mConstructorArgs[1] = attrs;

                if (-1 == name.indexOf('.')) {
                    return createView(context, name, "android.widget.");
                } else {
                    return createView(context, name, null);
                }
            } catch (Exception e) {
                // We do not want to catch these, lets return null and let the actual LayoutInflater
                // try
                return null;
            } finally {
                // Don't retain references on context.
                mConstructorArgs[0] = null;
                mConstructorArgs[1] = null;
            }
        }

        private View createView(Context context, String name, String prefix)
                throws ClassNotFoundException, InflateException {

            Constructor<? extends View> constructor = sConstructorMap.get(name);

            try {
                if (constructor == null) {
                    Class<? extends View> clazz = context.getClassLoader().loadClass(
                            prefix != null ? (prefix + name) : name).asSubclass(View.class);

                    constructor = clazz.getConstructor(sConstructorSignature);
                    sConstructorMap.put(name, constructor);
                }
                constructor.setAccessible(true);
                return constructor.newInstance(mConstructorArgs);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
