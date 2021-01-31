package com.star.theme;

/**
 * 持久存储
 */
public interface Storage<T extends Theme> {

    void set(String key, int value);

    void set(String key, boolean value);

    int getInt(String key);

    int getInt(String key, int defaultValue);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);

    void set(String key, T theme);

    T getTheme(String key);

}
