package com.tywho.common.base.bean;

import com.google.gson.annotations.SerializedName;
import com.tywho.common.exception.ApiException;

import java.util.List;

/**
 * Created by limit on 2017/3/29.
 */

public class BaseBean<T> {
    public int code;
    @SerializedName("msg")
    public String message;
    @SerializedName("content")
    public T data;

    public boolean success;
    public List<ResponseBean> errordata;

    public BaseBean() {
    }

    public BaseBean(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public BaseBean(int code) {
        this.code = code;
        this.message = ApiException.getApiExceptionMessage(code);
    }
    /**
     * 根据 code 码判断业务状态
     * code == 10000，表示业务成功。其他表示业务失败，message 进一步描述业务状态。
     */
    public boolean isSuccess() {
        return this.code == 100000;
    }
    public boolean isLogout() {
        return this.code == 100105 || this.code ==100101;
    }

}
