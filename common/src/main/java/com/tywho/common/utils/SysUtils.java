package com.tywho.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.tywho.common.utils.show.LogUtils;
import com.tywho.config.CommonConfig;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by limit on 2017/3/30.
 */

public class SysUtils {
    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     *
     * @param context   Used to find the correct file
     * @param fieldName The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    public static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            int resId = context.getResources().getIdentifier("build_config_package", "string", context.getPackageName());
            // try/catch blah blah
            Class<?> clazz = Class.forName(context.getString(resId) + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Android studio 多个module时，非当前运行的module对家获取BuildConfig.DEBUG都是false
     * 这里通过获取当前应用的context下的BuildConfig来判断才正确
     *
     * @param context
     * @return
     */
    public static boolean isDebug(Context context) {
        Object result = getBuildConfigValue(context, "DEBUG");
        return result != null && ((boolean) result);
    }

    /**
     * 获取当前app的服务器地址，build.gradle里面配置
     *
     * @param context
     * @return
     */
    public static String getConfigStr(Context context, String configStr) {
        Object result = getBuildConfigValue(context, configStr);
        return (String) result;

    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
    /**
     * try get host activity from view.
     * views hosted on floating window like dialog and toast will sure return null.
     *
     * @return host activity; or null if not available
     * 如果一个 View 绘制于某个 Activity 的 ContentView 上,
     * 那它的 Context 一定是和这个 Activity 相关联的.
     * 因此我们想在 View 中直接用 Activity 方法时 (最常用的应该就是 Activity.startActivity() 方法了),
     * 不必再向 View 中传递 Activity 对象.
     * 但在 View 继承自 AppCompat 系的 View 时 (比如 AppCompatTextView, AppCompatImageView),
     * Activity activity = (Activity) getContext();上面方法可能会得到下面异常
     * **java.lang.ClassCastException: android.support.v7.widget.TintContextWrapper cannot be cast to ...Activity**
     * 解决办法：从 ContextWrapper 中把 Activity 剥出来
     */
    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            if (TextUtils.isEmpty(deviceId)) {
                return null;
            } else {
                return deviceId;
            }
        } catch (Exception ex) {
            LogUtils.e(CommonConfig.APP_LOG, ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     *@param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }
}
