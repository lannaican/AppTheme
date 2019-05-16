package com.star.theme;

/**
 * Detail：持久存储
 * Author：Stars
 * Create Time：2019/5/16 11:24
 */
public interface AppThemeStorage {

    void set(String key, int value);

    void set(String key, boolean value);

    int getInt(String key);

    int getInt(String key, int defaultValue);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);

}
