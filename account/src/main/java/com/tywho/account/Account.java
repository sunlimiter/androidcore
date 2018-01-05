package com.tywho.account;

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
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public interface Account {

    String name();//名字

    String mobile();//登录帐号（手机号码）

    String accessToken();//登录token

    String accessId();//登录tokenid

    String toJson();//用户信息json

    String getUserId();//用户Id
}