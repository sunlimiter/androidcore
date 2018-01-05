package com.tywho.dblibrary.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Entity
public class AppStartImage {
    @Id(autoincrement = true)
    private Long id;

    /*图片url*/
    private String url;
    /*本地存储路径*/
    private String file;
    /*图片点击跳转路径*/
    private String webUrl;
    /*开始时间*/
    private long startTime;
    /*结束时间*/
    private long endTime;

    @Generated(hash = 1520678996)
    public AppStartImage(Long id, String url, String file, String webUrl,
            long startTime, long endTime) {
        this.id = id;
        this.url = url;
        this.file = file;
        this.webUrl = webUrl;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    @Generated(hash = 1054371345)
    public AppStartImage() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getFile() {
        return this.file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getWebUrl() {
        return this.webUrl;
    }
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
    public long getStartTime() {
        return this.startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getEndTime() {
        return this.endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
