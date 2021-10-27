package com.star.theme;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.ArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.LayoutInflaterFactory;

import com.star.theme.attr.Attr;
import com.star.theme.attr.AttrType;
import com.star.theme.attr.AttrUtils;
import com.star.theme.attr.AttrView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppTheme implements Application.ActivityLifecycleCallbacks {

    private static AppTheme instance;

    private final SparseArrayCompat<List<AttrView>> attrViewMaps = new SparseArrayCompat<>();
    private final List<Activity> activities = new ArrayList<>();

    private static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap<>();
    private final Object[] mConstructorArgs = new Object[2];
    private static final Class<?>[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};

    public static AppTheme getInstance() {
        if (instance == null) {
            instance = new AppTheme();
        }
        return instance;
    }

    private Application application;

    //持久存储
    private Storage storage;

    //当前主题
    private Theme currentTheme;

    //深色模式
    private Boolean isDarkMode;

    private List<OnThemeListener> onThemeListeners = new ArrayList<>();

    private AppTheme() {}

    /**
     * 初始化
     */
    public void init(Application application, @NonNull Storage storage, @NonNull Theme defaultTheme) {
        application.registerActivityLifecycleCallbacks(this);
        this.application = application;
        this.storage = storage;
        this.currentTheme = storage.getTheme(com.star.theme.StorageKey.Theme);
        if (this.currentTheme == null) {
            this.currentTheme = defaultTheme;
        }
        int mode = storage.getInt(StorageKey.Mode, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public void setTheme(AppCompatActivity activity, @NonNull Theme theme) {
        isDarkMode = null;
        callListener(0);
        this.currentTheme = theme;
        storage.set(com.star.theme.StorageKey.Theme, theme);
        callListener(1);
        invokeResources(activity);
        update();
        callListener(2);
    }

    public Theme getTheme() {
        return currentTheme;
    }

    public void addAttrType(AttrType type) {
        AttrUtils.addType(type);
    }

    public void addOnThemeListener(OnThemeListener listener) {
        onThemeListeners.add(listener);
    }

    public void removeThemeListener(OnThemeListener listener) {
        onThemeListeners.remove(listener);
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
     * 是否是深色模式
     */
    public boolean isDarkMode() {
        if (isDarkMode == null) {
            int mode = AppCompatDelegate.getDefaultNightMode();
            if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
                return isDarkMode = true;
            }
            if (mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                if ((application.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                    return isDarkMode = true;
                }
            }
            return isDarkMode = false;
        }
        return isDarkMode;
    }

    /**
     * 获取深色模式设置
     *
     * @return
     */
    public int getDarkMode() {
        return storage.getInt(StorageKey.Mode, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    private void updateDarkModeValue() {
        isDarkMode = null;
        isDarkMode();
    }

    /**
     * 设置深色模式
     */
    public void setMode(AppCompatActivity activity, int mode) {
        isDarkMode = null;
        callListener(0);
        invokeResources(activity);
        storage.set(StorageKey.Mode, mode);
        AppCompatDelegate.setDefaultNightMode(mode);
        callListener(1);
        update();
        callListener(2);
    }

    /**
     * 获取主题色
     */
    public int getColor(int colorId) {
        return currentTheme.getColor(colorId);
    }

    /**
     * 是否深色主题
     */
    public boolean isDarkTheme() {
        return currentTheme.isDark();
    }

    /**
     * 更新StateListDrawable
     */
    private void invokeResources(AppCompatActivity activity) {
        try {
            Field resources = AppCompatActivity.class.getDeclaredField("mResources");
            resources.setAccessible(true);
            resources.set(activity, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                attrView.apply(true);
            }
        }
        for (Activity act : activities) {
            act.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(currentTheme.getWindowBackgroundId())));
            View rootView = act.findViewById(android.R.id.content);
            updateThemeView(rootView);
        }
    }

    private void updateThemeView(View view) {
        if (view instanceof ThemeView) {
            ((ThemeView) view).onThemeApply();
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i=0; i<group.getChildCount(); i++) {
                updateThemeView(group.getChildAt(i));
            }
        }
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        updateDarkModeValue();
    }

    private void callListener(int state) {
        switch (state) {
            case 0:
                for (OnThemeListener listener : onThemeListeners) {
                    listener.onThemeChangeBefore();
                }
                break;
            case 1:
                for (OnThemeListener listener : onThemeListeners) {
                    listener.onThemeViewApplyBefore();
                }
                break;
            case 2:
                for (OnThemeListener listener : onThemeListeners) {
                    listener.onThemeChanged();
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        activities.add(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        activities.remove(activity);
        attrViewMaps.remove(activity.hashCode());
    }


    /*****************************************************************************/

    class InflateFactoryHandler<T> implements InvocationHandler {

        private final T inflaterFactory;
        private final AppCompatActivity activity;

        public InflateFactoryHandler(T inflaterFactory, AppCompatActivity activity) {
            this.inflaterFactory = inflaterFactory;
            this.activity = activity;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            try {
                result = method.invoke(inflaterFactory, args);
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
                        attrView.apply(false);
                    }
                }
            } catch (Exception e) { }
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
            if (name.equals("View")) {
                name = "android.view.View";
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
