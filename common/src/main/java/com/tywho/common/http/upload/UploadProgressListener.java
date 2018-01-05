package com.tywho.common.http.upload;

/**
 * Created by limit on 2017/3/31.
 */

public interface UploadProgressListener {
    /**
     * 上传进度
     * @param currentBytesCount
     * @param totalBytesCount
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}
