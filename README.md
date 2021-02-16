# AppTheme
Android主题色框架

参考项目：[NightModel](https://github.com/achenglike/NightModel)

## 特性
- 支持色值替换，不支持资源替换
- 低入侵性，老项目改造方便
- 支持夜间模式，无需重启界面

## [参考APP](http://tapsss.com)

![示例图](https://github.com/lannaican/AppTheme/blob/master/sample.gif)


## 导入
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
```
dependencies {
	implementation 'com.github.lannaican:AppTheme:1.1.4'
}
```

## 使用
1.添加支持的attr类型
```
//默认已支持textColor、background、backgroundTint、src、tint、progressDrawable
AppTheme.getInstance().addAttrType();

//示例：替换CardView的cardBackgroundColor属性
public class AttrTypeCardBackgroundColor extends AttrType {

    public AttrTypeCardBackgroundColor() {
        super("cardBackgroundColor");
    }

	//View创建时执行
    @Override
    public void apply(View view, int resId, String resName) {
        if (view instanceof CardView) {
            ((CardView)view).setCardBackgroundColor(AppTheme.getInstance().getColor(resId));
        }
    }

	//主题切换后执行
    @Override
    public void applyChanged(View view, int resId, String resName) {
        if (view instanceof CardView) {
            ((CardView)view).setCardBackgroundColor(AppTheme.getInstance().getColor(resId));
        }
    }

    @Override
    public String getResourceName(String attrValue, Resources resources) {
        return getIntResourceName(attrValue, resources);
    }
}

```

2.定义主题类，实现Theme类
```
public class CustomTheme implements Theme {

	//自定义需要变化的色值
    int color;		//主题色
    int colorOn;	//强调色
    boolean dark;	//是否暗色

    @Override
    public int getColor(@ColorRes int colorId) {
		//实现颜色替换逻辑，比如参数为R.color.theme做为主题色，这里就将所有色值引用为R.color.theme替换成该主题color色值
		switch(colorId) {
			case R.color.theme:
				return color;
			...
		}
	}
}
```

2.初始化
```
AppTheme.getInstance().init(); //建议在Application中执行

AppTheme.getInstant.attach(); //在BaseActivity setContentView之前注册

AppTheme.getInstant.detach(); //在BaseActivity onDestory取消注册
```

3.切换主题/夜间模式
```
//主题与夜间模式不冲突，处理好Theme中的getColor逻辑可以共存
AppTheme.getInstant().setTheme(theme);
AppTheme.getInstant().setNight(isNight);
//切换夜间模式为保证界面不重启，请在activity中添加android:configChanges="uiMode"
```

4.添加监听
```
AppTheme.getInstant().addOnThemeListener(listener);
AppTheme.getInstant().removeThemeListener(listener);
```

5.对于通过代码动态设置色值的View，可能需要特殊处理，View实现ThemeView
```
public CustomView implements ThemeView {
	void onThemeApply();	//在主题切换后执行
}
```

6.获取色值等其他参考AppTheme方法；
