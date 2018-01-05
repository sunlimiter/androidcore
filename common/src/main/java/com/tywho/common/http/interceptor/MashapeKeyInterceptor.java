package com.tywho.common.http.interceptor;

import com.tywho.common.utils.MD5Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by limit on 2017/3/30.
 * 默认拦截器，处理请求加密，可自定义
 */

public class MashapeKeyInterceptor implements Interceptor {
    private String commenParams = "";

    public MashapeKeyInterceptor(String commenParams) {
        this.commenParams = commenParams;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        //添加请求头
        request = request.newBuilder().addHeader("mobileinfo", encodeHeadInfo(commenParams)).build();
        if (request.method().equals("GET")) {
            request = addGetParams(request);
        } else if (request.method().equals("POST")) {
            request = addPostParams(request);
        }
        return chain.proceed(request);
    }

    //get请求 添加公共参数 签名
    private Request addGetParams(Request request) {
        //添加公共参数
        HttpUrl httpUrl = request.url()
                .newBuilder()
                .build();

        //添加签名
        Set<String> nameSet = httpUrl.queryParameterNames();
        ArrayList<String> nameList = new ArrayList<>();
        nameList.addAll(nameSet);
        Collections.sort(nameList);

        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (int i = 0; i < nameList.size(); i++) {
            treeMap.put(nameList.get(i), httpUrl.queryParameterValues(nameList.get(i)));
            httpUrl.newBuilder()
                    .addQueryParameter(nameList.get(i), httpUrl.queryParameterValues(nameList.get(i)).toString());
        }

        httpUrl = httpUrl.newBuilder()
                .addQueryParameter("signature", MD5Util.MD5(MD5Util.MD5(mapToQueryString(treeMap)) + "654321"))
                .build();
        request = request.newBuilder().url(httpUrl).build();
        return request;
    }

    //post 添加签名和公共参数
    private Request addPostParams(Request request) throws UnsupportedEncodingException {
        if (request.body() instanceof FormBody) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            FormBody formBody = (FormBody) request.body();

            //把原来的参数添加到新的构造器
            TreeMap<String, Object> treeMap = new TreeMap<>();
            for (int i = 0; i < formBody.size(); i++) {
                treeMap.put(formBody.encodedName(i), URLDecoder.decode(formBody.encodedValue(i), "UTF-8"));
                bodyBuilder
                        .addEncoded(formBody.encodedName(i),URLDecoder.decode(formBody.encodedValue(i), "UTF-8"));
            }
            formBody = bodyBuilder
                    .addEncoded("signature", MD5Util.MD5(MD5Util.MD5(mapToQueryString(treeMap)) + "654321"))
                    .build();
            request = request.newBuilder().post(formBody).build();
        }
        return request;
    }

    private static String encodeHeadInfo(String headInfo) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0, length = headInfo.length(); i < length; i++) {
            char c = headInfo.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", (int) c));
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }


    private String mapToQueryString(Map<String, Object> params) {
        String formData;
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            encodedParams.append(entry.getKey());
            encodedParams.append('=');
            encodedParams.append(entry.getValue());
            encodedParams.append('&');
        }
        formData = encodedParams.toString();
        if (formData.endsWith("&")) {
            formData = formData.substring(0, formData.lastIndexOf("&"));
        }
        return formData;
    }
}

