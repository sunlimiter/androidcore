package com.tywho.account;

import android.content.Context;
import android.text.TextUtils;

import com.tywho.config.CommonConfig;

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
 * 判断登录：boolean isLogin = AccountManager.INSTANCE.isLogin();
 * 退出登录：AccountManager.INSTANCE.logout();
 * UserBean bean  = new UserBean();
 * bean.setName("测试");
 * bean.setId(1);
 * AccountManager.INSTANCE.refreshAccount(bean);
 * boolean isLogin = AccountManager.INSTANCE.isLogin();
 * LogUtils.d(AccountManager.INSTANCE.getCurrentAccount().name()+"=="+isLogin);
 */
public enum AccountManager {

    INSTANCE;

    private final AuthPreferences authPreferences;
    private Account mCurrentAccount;
    private Context mContext;

    AccountManager() {
        mContext = CommonConfig.APPLICATION;
        authPreferences = new AuthPreferences(CommonConfig.APPLICATION);
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return authPreferences.isLogin();
    }

    /**
     * 是否存在手势密码
     *
     * @return
     */
    public boolean hasGesturePwd() {
        return authPreferences.hasGesturePwd();
    }

    /**
     * 是否显示用户资产信息 默认不显示
     *
     * @return
     */
    public boolean isShowAssetsFull() {
        return authPreferences.getShowAssetsFull();
    }

    public void logout() {
        mCurrentAccount = null;
        authPreferences.clear();
    }

    /**
     * 更新用户数据
     *
     * @param account
     */
    public void refreshAccount(Account account) {
        mCurrentAccount = account;
        authPreferences.setAccessToken(account.accessToken());
        authPreferences.setAccessId(account.accessId());
        authPreferences.setUser(account.name());
        authPreferences.setUserData(account.toJson());
        authPreferences.setLoginAccount(account.mobile());
    }

    /**
     * 登录成功获取存入数据
     *
     * @param account
     */
    public void storeAccount(Account account) {
        mCurrentAccount = account;
        authPreferences.setAccessToken(account.accessToken());
        authPreferences.setAccessId(account.accessId());
        authPreferences.setUser(account.name());
        authPreferences.setUserData(account.toJson());
        authPreferences.setLoginAccount(account.mobile());
    }

    /**
     * 存入手势密码
     *
     * @param gesturePwd
     */
    public void storeGesturePwd(byte[] gesturePwd) {
        authPreferences.setGesturePwd(gesturePwd);
    }

    /**
     * 获取手势密码
     *
     * @return
     */
    public byte[] getGesturePwd() {
        return authPreferences.getGesturePwd();
    }

    /**
     * 存入"是否显示我的资产数据"状态
     *
     * @param isShow
     */
    public void storeShowAssetsFull(boolean isShow) {
        authPreferences.setShowAssetsFull(isShow);
    }

    public String getUserId() {
        return getCurrentAccount() != null ? getCurrentAccount().getUserId() : "";
    }

    public String getLoginAccount() {
        return authPreferences.getLoginAccount();
    }


    public String getAccessToken() {
        return authPreferences.getAccessToken();
    }

    /**
     * 刷新AccessToken
     *
     * @param accessToken
     */
    public void refreshAccessToken(String accessToken) {
        authPreferences.setAccessToken(accessToken);
    }

    /**
     * 刷新refreshToken
     *
     * @param accessId
     */
    public void refreshAccessId(String accessId) {
        authPreferences.setAccessId(accessId);
    }

    public String getAccessId() {
        return authPreferences.getAccessId();
    }

    public String user() {
        return authPreferences.getUser();
    }

    @SuppressWarnings("unchecked")
    public <T extends Account> T getCurrentAccount() {
        if (mCurrentAccount == null) {
            String accountJson = authPreferences.getUserData();
            if (!TextUtils.isEmpty(accountJson)
                    && mContext instanceof AccountProvider) {
                mCurrentAccount = ((AccountProvider) mContext).provideAccount(accountJson);
            }
        }
        return (T) mCurrentAccount;
    }
}
