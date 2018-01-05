package com.tywho.common.http.download.bean;

import com.tywho.common.http.download.listener.HttpDownCallBackListener;
import com.tywho.common.http.download.service.HttpDownService;
import com.tywho.common.utils.StringUtils;

/**
 * Created by limit on 2017/3/30.
 */

public class DownInfoBean {
    private long id;
    /*存储位置*/
    private String savePath;
    private String saveName;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*下载唯一的HttpService*/
    private HttpDownService service;
    /*回调监听*/
    private HttpDownCallBackListener listener;
    /*超时设置*/
    private int connectonTime = 6;
    /*state状态数据库保存*/
    private int stateInte;
    /*url*/
    private String url;

    public DownInfoBean(String url, HttpDownCallBackListener listener) {
        setUrl(url);
        setListener(listener);
    }

    public DownInfoBean(String url) {
        setUrl(url);
    }

    public DownInfoBean(long id, String savePath, long countLength, long readLength, HttpDownService service, HttpDownCallBackListener listener, int connectonTime, int stateInte, String url) {
        this.id = id;
        this.savePath = savePath;
        this.countLength = countLength;
        this.readLength = readLength;
        this.service = service;
        this.listener = listener;
        this.connectonTime = connectonTime;
        this.stateInte = stateInte;
        this.url = url;
    }

    public DownState getState() {
        switch (getStateInte()) {
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
            default:
                return DownState.FINISH;
        }
    }

    public void setState(DownState state) {
        setStateInte(state.getState());
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public HttpDownService getService() {
        return service;
    }

    public void setService(HttpDownService service) {
        this.service = service;
    }

    public int getConnectonTime() {
        return connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }

    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpDownCallBackListener getListener() {
        return listener;
    }

    public void setListener(HttpDownCallBackListener listener) {
        this.listener = listener;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }
}
