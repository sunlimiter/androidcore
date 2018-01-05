package com.tywho.common.base.api;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.tywho.common.http.NormalResponseTransformer;
import com.tywho.common.http.interceptor.MashapeKeyInterceptor;
import com.tywho.common.utils.JsonUtils;
import com.tywho.common.utils.ReflectionUtil;
import com.tywho.common.utils.StringUtils;
import com.tywho.config.CommonConfig;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by limit on 2017/5/27/0027.
 */

public abstract class BaseApi<T> {
    //rx生命周期管理
    private SoftReference<RxAppCompatActivity> rxAppCompatActivity;

    private MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    /**
     * 是否能取消加载框
     */
    private boolean cancel = false;
    /**
     * 是否显示加载框
     */
    private boolean showProgress = true;
    /**
     * 是否需要缓存处理
     */
    private boolean cache = false;
    /**
     * 超时时间-默认30秒
     */
    private int connectionTime = 10;
    /**
     * 读取超时
     */
    private int readTimeout = 25;
    /**
     * 写入超时
     */
    private int writeTimeout = 25;
    /**
     * 有网情况下的本地缓存时间默认60秒
     */
    private int cookieNetWorkTime = 60;
    /**
     * 无网络的情况下本地缓存时间默认30天
     */
    private int cookieNoNetWorkTime = 24 * 60 * 60 * 30;
    /**
     * 基础url
     */
    private String baseUrl = CommonConfig.API_URL;
    /**
     * 方法(eg:     @GET("data/Android/2/1")
     * Observable<String> getAndroidList();
     * ,  我们的 mMethod 为 data/Android/2/1 )
     * 这个参数是缓存必须的,如果不要缓存则可以不管
     */
    private String mMethod;
    /**
     * 缓存解析类，使用缓存必填
     */
    private Class<T> classType;
    /**
     * 是否强制刷新，默认false
     */
    private boolean forceRefresh = false;
    /**
     * 加载进度提示文字
     */
    private String loadMessage = "加载数据...";

    /**
     * 分页
     */
    private int page;
    private int pageSize = CommonConfig.PAGE_SIZE;

    public BaseApi(RxAppCompatActivity rxAppCompatActivity) {
        this();
        this.rxAppCompatActivity = new SoftReference<RxAppCompatActivity>(rxAppCompatActivity);
    }

    public BaseApi() {
        Type[] parameterizedTypes = ReflectionUtil.getParameterizedTypes(this);
        try {
            classType = (Class<T>) ReflectionUtil.getClass(parameterizedTypes[0]);
        } catch (Exception e) {
        }
    }

    /**
     * rx绑定生命周期，加载进度条
     *
     * @return
     */
    public SoftReference<RxAppCompatActivity> getRxAppCompatActivity() {
        return rxAppCompatActivity;
    }

    /**
     * 设置参数
     *
     * @return
     */
    public abstract Observable getObservable(Retrofit retrofit);

    /**
     * 数据解析
     *
     * @return
     */
    public Observable.Transformer getCustomTransformer() {
        return new NormalResponseTransformer<>();
    }

    /**
     * 封装访问数据，最终处理为json 交RequestBody发送
     *
     * @return
     */
    public Map<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        Class clazz = getClass();
        Field[] field = clazz.getDeclaredFields();
        try {
            for (Field f : field) {
                f.setAccessible(true);
                if (f.get(this) != null) {
                    /**忽略编译产生的属性**/
                    if (f.isSynthetic()) {
                        continue;
                    }
                    /**忽略serialVersionUID**/
                    if (f.getName().equals("serialVersionUID")) {
                        continue;
                    }
                    params.put(f.getName(), f.get(this));
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (page > 0) {
            params.put("currentPage", page);
            params.put("pageSize", pageSize);
        }
//        HashMap<String, Object> paramsNew = new HashMap<>();
//        paramsNew.put("jsonString", JsonUtils.toJson(params));
//        paramsNew.put("commenParams", JsonUtils.toJson(getCommenParams()));
        return params;
    }

    //公共参数
    public HashMap<String, Object> getCommenParams() {
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

    public String getJson() {
        return JsonUtils.toJson(getParams());
    }

    public RequestBody getRequestBody() {
        return RequestBody.create(getMediaType(), JsonUtils.toJson(getParams()));
    }

    /**
     * 获取根路径
     *
     * @return
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 拦截器
     *
     * @return
     */
    public Interceptor getInterceptor() {
        return new MashapeKeyInterceptor(JsonUtils.toJson(getCommenParams()));
    }

    public String getUrl() {
        if (isCache()) {
            if (StringUtils.isEmpty(mMethod)) {
                throw new NullPointerException("如果需要缓存数据,则必须设置setMethod(String method)");
            }
        }
        return baseUrl + mMethod;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public String getLoadMessage() {
        return loadMessage;
    }

    public void setLoadMessage(String loadMessage) {
        this.loadMessage = loadMessage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCookieNetWorkTime() {
        return cookieNetWorkTime;
    }

    public void setCookieNetWorkTime(int cookieNetWorkTime) {
        this.cookieNetWorkTime = cookieNetWorkTime;
    }

    public int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    public void setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
    }

    public String getmMethod() {
        return mMethod;
    }

    public void setmMethod(String mMethod) {
        this.mMethod = mMethod;
    }

    public Class<T> getClassType() {
        return classType;
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }

    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }
}
