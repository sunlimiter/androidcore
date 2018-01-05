package com.tywho.common.exception;

import com.tywho.common.base.bean.BaseBean;

/**
 * Created by limit on 2017/3/30.
 */

public class ApiException extends Exception {
    /*网络错误*/
    public static final int NETWORD_ERROR = 0x1;
    /*http_错误*/
    public static final int HTTP_ERROR = 0x2;
    /*json错误*/
    public static final int JSON_ERROR = 0x3;
    /*未知错误*/
    public static final int UNKNOWN_ERROR = 0x4;
    /*运行时异常-包含自定义异常*/
    public static final int RUNTIME_ERROR = 0x5;
    /*无法解析该域名*/
    public static final int UNKOWNHOST_ERROR = 0x6;
    /*本地无缓存错误*/
    public static final int NO_CHACHE_ERROR = 0x1003;
    /*缓存过时错误*/
    public static final int CHACHE_TIMEOUT_ERROR = 0x1004;

    private BaseBean baseBean;

    public ApiException(BaseBean baseBean) {
        super(baseBean.message);
        this.baseBean = baseBean;
    }

    @Override
    public String getMessage() {
        return baseBean.message;
    }

    public BaseBean getBaseBean() {
        return baseBean;
    }

    /**
     * 转换错误数据
     *
     * @param code
     * @return
     */
    public static String getApiExceptionMessage(int code) {
        switch (code) {
            case NETWORD_ERROR:
                return "错误：网络出现问题，请检查您的网络";
            case HTTP_ERROR:
            case UNKOWNHOST_ERROR:
                return "错误：访问出现错误，请稍后重试";
            case JSON_ERROR:
                return "错误：数据解析错误，请稍后重试";
            case RUNTIME_ERROR:
                return "错误：运行出错啦，请稍后重试";
            case NO_CHACHE_ERROR:
                return "错误：无缓存数据";
            case CHACHE_TIMEOUT_ERROR:
                return "错误：缓存数据过期";
            case UNKNOWN_ERROR:
            default:
                return "错误：未知错误，请稍后重试";
        }
    }
}
