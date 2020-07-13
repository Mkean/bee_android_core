package com.bee.android.common.logger;


/**
 *  作用描述
 */
public interface SendLogListener {

    /**
     * 上传进度
     * @param currentSize
     * @param totalSize
     */
    public void onProgress(long currentSize, long totalSize);

    /**
     *
     */
    void onComplete(boolean isSuccess);

    /**
     * 单个文件上传成功
     * @param url
     */
    void onSuccess(String url, String path);

    /**
     * 上传失败
     * @param errorMessage
     */
    void onFailure(String errorMessage, String path);
}
