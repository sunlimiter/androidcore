package com.tywho.account;

import android.content.Context;
import android.text.TextUtils;

import com.tywho.common.utils.SharePreUtil;
import com.tywho.common.utils.StringUtils;

/**
 * ********************** Copyright (c) ***********************\
 * *
 * *              Copyright Reserved 2013-2016
 * *
 * *                 By(个人项目)
 * *                    www.tywho.com
 * *
 * *                       _oo0oo_
 * *                      o8888888o
 * *                      88" . "88
 * *                      (| -_- |)
 * *                      0\  =  /0
 * *                    ___/`---'\___
 * *                  .' \\|     |// '.
 * *                 / \\|||  :  |||// \
 * *                / _||||| -:- |||||- \
 * *               |   | \\\  -  /// |   |
 * *               | \_|  ''\---/''  |_/ |
 * *               \  .-\__  '-'  ___/-. /
 * *             ___'. .'  /--.--\  `. .'___
 * *          ."" '<  `.___\_<|>_/___.' >' "".
 * *         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * *         \  \ `_.   \_ __\ /__ _/   .-` /  /
 * *     =====`-.____`.___ \_____/___.-`___.-'=====
 * *                       `=---='
 * *
 * *
 * *     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * *
 * *               佛祖保佑         永无BUG
 * *
 * *
 * *                   南无本师释迦牟尼佛
 * *
 * ******************** Copyright (c) ***********************\
 * Created with IntelliJ IDEA.
 * User: limiter0417@foxmail.com
 * Date: 2017/9/26/0026
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class AuthPreferences {
    public static String PRENAME = "app_user_setting";//默认文件名
    public static String LOGINPRENAME = "app_login_setting";//默认文件名

    private static final String KEY_USER = "user";
    private static final String KEY_ACCESSTOKEN = "accessToken";
    private static final String KEY_ACCESSID = "accessId";
    private static final String KEY_USER_DATA = "user_data";
    private static final String GESTURE_PASSWORD = "GesturePassword";
    private static final String SHOW_ASSETS_FULL = "show_assets_full";


    private static final String KEY_LOGINACCOUNT = "loginAccount";
    private Context mContext;

    public AuthPreferences(Context context) {
        mContext = context;
    }

    /**
     * 以下设置跟用户相关信息，退出登录就清空
     *
     * @param user
     */
    public void setUser(String user) {
        SharePreUtil.putString(PRENAME, mContext, KEY_USER, user);
    }

    public void setAccessToken(String accessToken) {
        SharePreUtil.putString(PRENAME, mContext, KEY_ACCESSTOKEN, accessToken);
    }

    public void setAccessId(String accessId) {
        SharePreUtil.putString(PRENAME, mContext, KEY_ACCESSID, accessId);
    }

    public void setUserData(String userData) {
        SharePreUtil.putString(PRENAME, mContext, KEY_USER_DATA, userData);
    }

    /**
     * 手势密码 退出清空
     *
     * @param pwds
     */
    public void setGesturePwd(byte[] pwds) {
        SharePreUtil.putByteArr(PRENAME, mContext, GESTURE_PASSWORD, pwds);
    }

    /**
     * 是否显示个人资产 退出清空
     *
     * @param isShow
     */
    public void setShowAssetsFull(boolean isShow) {
        SharePreUtil.putBoolean(PRENAME, mContext, SHOW_ASSETS_FULL, isShow);
    }

    /**
     * 单独设置登录帐号，退出不清除信息
     *
     * @param account
     */
    public void setLoginAccount(String account) {
        SharePreUtil.putString(LOGINPRENAME, mContext, KEY_LOGINACCOUNT, account);
    }

    /**
     * 获取登录用户相关信息
     *
     * @return
     */
    public String getUser() {
        return SharePreUtil.getString(PRENAME, mContext, KEY_USER);
    }

    public String getAccessToken() {
        return SharePreUtil.getString(PRENAME, mContext, KEY_ACCESSTOKEN);
    }

    public String getAccessId() {
        return SharePreUtil.getString(PRENAME, mContext, KEY_ACCESSID);
    }

    public String getUserData() {
        return SharePreUtil.getString(PRENAME, mContext, KEY_USER_DATA);
    }

    public byte[] getGesturePwd() {
        return SharePreUtil.getByteArr(PRENAME, mContext, GESTURE_PASSWORD);
    }

    public boolean getShowAssetsFull() {
        return SharePreUtil.getBoolean(PRENAME, mContext, SHOW_ASSETS_FULL);
    }

    /**
     * 获取登录帐号
     *
     * @return
     */
    public String getLoginAccount() {
        return SharePreUtil.getString(LOGINPRENAME, mContext, KEY_LOGINACCOUNT);
    }

    public void clear() {
        SharePreUtil.clear(PRENAME, mContext);
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(getUserData()) && !TextUtils.isEmpty(getAccessToken()) && !TextUtils.isEmpty(getAccessId());
    }

    public boolean hasGesturePwd() {
        return !StringUtils.isEmpty(getGesturePwd());
    }
}
