package com.tywho.config;

import android.app.Application;

import java.util.HashMap;

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
 * Date: 2017/10/26/0026
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class CommonConfig {
    public static String API_URL;//核心url，域名/ip
    public static String WEB_URL;//网页url，域名/ip
    public static int FIRST_PAGE = 1;//分页，每页的数量
    public static int PAGE_SIZE = 20;//分页，每页的数量
    public static String HTTP_LOG = "HTTP_LOG";//日志tag
    public static String APP_LOG = "APP_LOG";//日志tag
    public static boolean isDebug = false;//是否debug模式


    public static Object VERSIONCODE;//APP版本,内部
    public static Object VERSION;//APP版本
    public static String APPTYPE;//APP类型
    public static String DEVICEID;//获取设备的唯一标识
    public static Object CHANNEL;//APP渠道
    public static String MOBILEVERSION;//获取手机Android 版本
    public static String MOBILEMODEL;// 获取手机型号
    public static String MOBILEBRAND;//获取手机品牌
    public static String SCREENWIDTH;//获取手机屏幕宽度
    public static String SCREENHEIGHT;//获取手机屏幕宽度

    public static Application APPLICATION;//application


    public static int EXITAPP = 10110;//exit app
    public static int WEBTHEME = 10119;//web theme
    //    public static int FINANCEADDVIEW = 10120;//finance add view
//    public static int GESTUREPWDCHANGEVIEW = 10121;//gesture pwd change view
    public static int INDEXSTARTFINANCEVIEW = 10122;//index start finance view
    public static int LOGINSTATE = 10123;//login state 登录状态true 登录，false 登出
    public static int COUPONSELECT = 10124;//coupon select
    //    public static int LOGINOUT = 10125;//login out
    public static int CHANGETOOLBARMENU = 10126;//change toolbar menu


    public static HashMap<String, Object> getCommenParams() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("version", CommonConfig.VERSION);
        map.put("versionCode", CommonConfig.VERSIONCODE);
        map.put("appType", CommonConfig.APPTYPE);
        map.put("deviceId", CommonConfig.DEVICEID);
        map.put("channel", CommonConfig.CHANNEL);
        map.put("mobileVersion", CommonConfig.MOBILEVERSION);
        map.put("sourceType", CommonConfig.MOBILEBRAND + CommonConfig.MOBILEMODEL);
        map.put("screenWidth", CommonConfig.SCREENWIDTH);
        map.put("screenHeight", CommonConfig.SCREENHEIGHT);
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return map;
    }
}
