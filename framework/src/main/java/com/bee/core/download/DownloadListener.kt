package com.bee.core.download

interface DownloadListener {
    /**
     *下载开始
     *
     * @param totalLength 需要下载的文件总大小
     */
    fun onStart(totalLength: Long)

    /**
     *下载进度
     *
     * @param progress 1,2,3...100
     */
    fun onProgress(progress: Int)

    /**
     *下载完成
     *
     * @param downloadLength 实际下载的文件的总大小
     */
    fun onFinish(downloadLength: Long, path: String)

    /**
     * 取消下载
     */
    fun onCancel()

    /**
     *下载异常
     *
     * @param message 异常信息
     * @param url
     */
    fun onError(message: String, url: String)
}