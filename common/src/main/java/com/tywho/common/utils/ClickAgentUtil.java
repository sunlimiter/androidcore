package com.tywho.common.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * 创建时间：2017/6/9/0009
 * 创建人：limit
 * 功能描述：统计封装类
 */

public class ClickAgentUtil {
    public static void onEvent(Context context, String str) {
        if (StringUtils.isEmpty(context) || StringUtils.isEmpty(str)) return;
        MobclickAgent.onEvent(context, str);
    }
}
