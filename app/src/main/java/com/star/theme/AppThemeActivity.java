package com.star.theme;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Detail：
 * Author：Stars
 * Create Time：2019/5/16 10:41
 */
public class AppThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppTheme.register(this);
        super.onCreate(savedInstanceState);
    }
}
