package com.tywho.common.http.download.interceptor;

/**
 * Created by limit on 2017/3/30.
 */
public interface DownloadProgressListener {
    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}