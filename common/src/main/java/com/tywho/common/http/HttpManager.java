package com.tywho.common.http;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.tywho.common.base.api.BaseApi;
import com.tywho.common.exception.RetryWhenNetworkException;
import com.tywho.common.http.jsonfactory.CustomGsonConverterFactory;
import com.tywho.common.http.listener.CallBackListener;
import com.tywho.config.CommonConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscription;

/**
 * Created by limit on 2017/3/29.
 */

public class HttpManager {
    private volatile static HttpManager INSTANCE;
    private HttpLoggingInterceptor httpLoggingInterceptor;

    //构造方法私有
    private HttpManager() {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(CommonConfig.isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    //获取单例
    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 绑定生命周期，传递上下文，显示加载进度
     *
     * @param api
     * @param callBackListener
     * @return
     */
    @SuppressWarnings("unchecked")
    public void bindRequest(BaseApi api, CallBackListener callBackListener) {
        if (api == null) return;
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(api, callBackListener);
        api.getObservable(baseRequest(api))
                .retryWhen(new RetryWhenNetworkException())
                .compose(api.getCustomTransformer())//数据解析，可以自定义
                .compose(((RxAppCompatActivity) api.getRxAppCompatActivity().get()).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(progressSubscriber);
    }

    /**
     * 不绑定生命周期
     * 返回Subscription 自行处理
     *
     * @param api
     * @param callBackListener
     * @return
     */
    @SuppressWarnings("unchecked")
    public Subscription request(BaseApi api, CallBackListener callBackListener) {
        if (api == null) return null;
        ProgressSubscriber progressSubscriber = new ProgressSubscriber(api, callBackListener);

        return api.getObservable(baseRequest(api))
                .retryWhen(new RetryWhenNetworkException())
                .compose(api.getCustomTransformer())//数据解析，可以自定义
                .subscribe(progressSubscriber);
    }

    private Retrofit baseRequest(BaseApi api) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(api.getConnectionTime(), TimeUnit.SECONDS);
        builder.readTimeout(api.getReadTimeout(), TimeUnit.SECONDS);
        builder.writeTimeout(api.getWriteTimeout(), TimeUnit.SECONDS);
        builder.addInterceptor(httpLoggingInterceptor);
        Interceptor interceptor = api.getInterceptor();
        if (interceptor != null) builder.addInterceptor(interceptor);

        /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(api.getBaseUrl())
                .build();
        return retrofit;
    }


}
