package com.tywho.common.http.download;

import com.tywho.common.exception.RetryWhenNetworkException;
import com.tywho.common.http.download.bean.DownInfoBean;
import com.tywho.common.http.download.bean.DownState;
import com.tywho.common.http.download.interceptor.DownloadInterceptor;
import com.tywho.common.http.download.service.HttpDownService;
import com.tywho.common.utils.FileUtils;
import com.tywho.common.utils.StringUtils;
import com.tywho.common.utils.show.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by limit on 2017/3/30.
 */

public class HttpDownManager {
    /*记录下载数据*/
    private Set<DownInfoBean> downInfos;
    /*回调sub队列*/
    private HashMap<String, ProgressDownSubscriber> subMap;

    private volatile static HttpDownManager INSTANCE;

    //构造方法私有
    private HttpDownManager() {
        downInfos = new HashSet<>();
        subMap = new HashMap<>();
//        db= DbDwonUtil.getInstance();
    }

    //获取单例
    public static HttpDownManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpDownManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDownManager();
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void startDown(final DownInfoBean downInfoBean) {
        //正在下载不处理
        if (downInfoBean == null || subMap.get(downInfoBean.getUrl()) != null) {
            subMap.get(downInfoBean.getUrl()).setDownInfo(downInfoBean);
            LogUtils.d("this");
            return;
        }
        //添加回调处理类
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(downInfoBean);
         /*记录回调sub*/
        subMap.put(downInfoBean.getUrl(), subscriber);
        /*获取service，多次请求公用一个sercie*/
        HttpDownService httpService;
        if (downInfos.contains(downInfoBean)) {
            httpService = downInfoBean.getService();
        } else {
            DownloadInterceptor interceptor = new DownloadInterceptor(subscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //手动创建一个OkHttpClient并设置超时时间
            builder.connectTimeout(downInfoBean.getConnectonTime(), TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(getBasUrl(downInfoBean.getUrl()))
                    .build();
            httpService = retrofit.create(HttpDownService.class);
            downInfoBean.setService(httpService);
            downInfos.add(downInfoBean);
        }


         /*得到rx对象-上一次下載的位置開始下載*/
        httpService.download("bytes=" + downInfoBean.getReadLength() + "-", downInfoBean.getUrl())
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
                .map(new Func1<ResponseBody, DownInfoBean>() {
                    @Override
                    public DownInfoBean call(ResponseBody responseBody) {
                        try {
                            String filename;
                            if(StringUtils.isEmpty(downInfoBean.getSaveName())){
                                filename = downInfoBean.getUrl().substring(downInfoBean.getUrl().lastIndexOf('/') + 1)+"_temp";
                            }else{
                                filename = downInfoBean.getSaveName()+"_temp";
                            }
                            LogUtils.d(filename);
                            FileUtils.writeCache(responseBody, new File(downInfoBean.getSavePath(),filename), downInfoBean);
                        } catch (IOException e) {
                            //Exceptions.propagate()只是简单地做了这样一件事：如果异常是Checked异常，那就把它包装成Unchecked异常。
                            throw Exceptions.propagate(e);
                            /*失败抛出异常*/
//                            throw new RuntimeException("文件写入错误");
                        }
                        return downInfoBean;
                    }
                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(subscriber);
    }

    /**
     * 读取baseurl
     *
     * @param url
     * @return
     */
    public static String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

    /**
     * 停止下载
     */
    public void stopDown(DownInfoBean info) {
        if (info == null) return;
        info.setState(DownState.STOP);
        info.getListener().onStop();
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }
        /*保存数据库信息和本地文件*/
//        db.save(info);
    }


    /**
     * 暂停下载
     *
     * @param info
     */
    public void pause(DownInfoBean info) {
        if (info == null) return;
        info.setState(DownState.PAUSE);
        info.getListener().onPuase();
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
//        db.update(info);
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown() {
        for (DownInfoBean downInfo : downInfos) {
            stopDown(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }

    /**
     * 暂停全部下载
     */
    public void pauseAll() {
        for (DownInfoBean downInfo : downInfos) {
            pause(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }


    /**
     * 返回全部正在下载的数据
     *
     * @return
     */
    public Set<DownInfoBean> getDownInfos() {
        return downInfos;
    }

    /**
     * 移除下载数据
     *
     * @param info
     */
    public void remove(DownInfoBean info) {
        subMap.remove(info.getUrl());
        downInfos.remove(info);
    }
}
