package net.sqlcipher.database;

import android.content.Context;

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
 * Date: 2017/7/26/0026
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 * Description: 处理数据库加密不使用报错的问题
 */
public class SQLiteOpenHelper {
    public SQLiteOpenHelper(Context context, String name, String factory, int version) {
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onOpen(SQLiteDatabase db) {
    }

    public SQLiteDatabase getReadableDatabase(char[] password) {
        return null;
    }
    public SQLiteDatabase getReadableDatabase(String password) {
        return null;
    }
    public SQLiteDatabase getWritableDatabase(String password) {
        return null;
    }

    public SQLiteDatabase getWritableDatabase(char[] password) {
        return null;
    }
}
