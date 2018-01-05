package com.tywho.dblibrary.helper;

import com.tywho.dblibrary.bean.AppStartImage;

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
 * Date: 2017/11/21/0021
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class AppStartImageHelper extends BaseDbHelper<AppStartImage, Long> {

    public AppStartImageHelper() {
        super(DbCore.getDaoSession().getAppStartImageDao());
    }

    public static AppStartImageHelper getInstance() {
        return SingleLoader.INSTANCE;
    }

    private static class SingleLoader {
        final static AppStartImageHelper INSTANCE = new AppStartImageHelper();
    }
}