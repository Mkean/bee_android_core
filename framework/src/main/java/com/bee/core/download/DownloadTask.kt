package com.bee.core.download

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit

class DownloadTask {
    private var progress: Int = 0
    private var isCanceled: Boolean = false
    private var downloadListener: DownloadListener?
    private var retryNum: Int = 0
    private lateinit var downLoadUrl: String
    private lateinit var mFile: File

    private lateinit var observable: Observable<FileLoadingBean>

    private var emitter: ObservableEmitter<FileLoadingBean>? = null

    private lateinit var fileLoadingBean: FileLoadingBean

    private var listenTime: Long = 0
    private var mCount: Int = 0


    constructor(downloadListener: DownloadListener, retryNum: Int) {
        this.downloadListener = downloadListener
        this.retryNum = retryNum
        init()
    }

    constructor(downloadListener: DownloadListener) {
        this.downloadListener = downloadListener
        init()
    }

    @SuppressLint("CheckResult")
    fun init() {
        mCount = 0

        observable = Observable.create { e ->
            run {
                if (emitter == null) {
                    emitter = e
                }
            }
        }

        observable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (downloadListener == null) {
                return@subscribe
            }
            when (it.type) {
                FileLoadingBean.START_TYPE -> downloadListener!!.onStart(it.total)
                FileLoadingBean.PROGRESS_TYPE -> downloadListener!!.onProgress(it.progress)
                FileLoadingBean.FINISH_TYPE -> downloadListener!!.onFinish(it.total, it.path)
                FileLoadingBean.CANCEL_TYPE -> downloadListener!!.onCancel()
                FileLoadingBean.ERROR_TYPE -> {
                    mCount++;
                    if (mCount >= retryNum) {
                        downloadListener!!.onError(it.msg, it.url)
                    } else {
                        download(downLoadUrl, mFile)
                    }
                }
            }
        }
    }

    /**
     *开始一次下载任务，可以多次调用，但是一个DownloadHelper对象只允许一次下载一个文件，下载完成后再调用此方法开启新的下载
     * 如果需要并发下载文件，需要new 多个DownloadHelper对象，设置对应的多个listener，分别调用download方法
     *
     * @param url 网络文件的url
     * @param file 保存到本地目录，此目录合法即可，不需要创建出真正的文件，此方法帮助创建文件
     */
    fun download(url: String, file: File) {
        try {
            this.downLoadUrl = url
            this.mFile = file

            val request = Request.Builder()
                    .url(url)
                    .build()
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(60, TimeUnit.SECONDS)
            builder.readTimeout(60, TimeUnit.SECONDS)
            builder.writeTimeout(60, TimeUnit.SECONDS)
            builder.protocols(Collections.singletonList(Protocol.HTTP_1_1))
            val client = builder.build()
            // 异步请求
            client.newCall(request)
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            fileLoadingBean = FileLoadingBean()
                            fileLoadingBean.type = FileLoadingBean.ERROR_TYPE
                            fileLoadingBean.url = downLoadUrl
                            fileLoadingBean.msg = Log.getStackTraceString(e)
                            emitter!!.onNext(fileLoadingBean)

                        }

                        override fun onResponse(call: Call, response: Response) {
                            var ips: InputStream? = null
                            var fos: FileOutputStream? = null
                            val buf: ByteArray = ByteArray(1024)
                            var len: Int
                            try {
                                // 储存下载文件的目录
                                if (mFile.exists()) {
                                    mFile.delete()
                                }

                                val parentFile: File? = mFile.parentFile
                                if (parentFile != null && !parentFile.exists()) {
                                    makeDir(parentFile)
                                }
                                mFile.createNewFile()

                                ips = response.body()!!.byteStream()
                                val total: Long = response.body()!!.contentLength()
                                fos = FileOutputStream(mFile)
                                var sum: Long = 0

                                fileLoadingBean = FileLoadingBean()
                                fileLoadingBean.type = FileLoadingBean.START_TYPE
                                fileLoadingBean.total = total
                                emitter!!.onNext(fileLoadingBean)

                                listenTime = System.currentTimeMillis()

                                while (ips.read(buf).also { len = it } != -1) {
                                    if (isCanceled)
                                        break
                                    fos.write(buf, 0, len)
                                    sum += len

                                    var percent: Int = (100 * sum / total).toInt()
                                    var time: Long = System.currentTimeMillis()

                                    if (percent > progress && (percent == 1 || percent == 100 || time - listenTime > 100)) {
                                        progress = percent
                                        listenTime = time
                                        fileLoadingBean = FileLoadingBean()
                                        fileLoadingBean.type = FileLoadingBean.PROGRESS_TYPE
                                        fileLoadingBean.progress = percent
                                        emitter!!.onNext(fileLoadingBean)
                                    }
                                }
                                fos.flush()

                                if (!isCanceled) {
                                    // 下载完成
                                    fileLoadingBean = FileLoadingBean()
                                    fileLoadingBean.type = FileLoadingBean.FINISH_TYPE
                                    fileLoadingBean.total = total
                                    fileLoadingBean.path = mFile.path
                                    emitter!!.onNext(fileLoadingBean)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                                fileLoadingBean = FileLoadingBean()
                                fileLoadingBean.type = FileLoadingBean.ERROR_TYPE
                                fileLoadingBean.url = downLoadUrl
                                fileLoadingBean.msg = Log.getStackTraceString(e)
                                emitter!!.onNext(fileLoadingBean)
                            } finally {
                                try {
                                    ips?.close()
                                    fos?.close()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    })


        } catch (e: Exception) {
            e.printStackTrace()
            fileLoadingBean = FileLoadingBean()
            fileLoadingBean.type = FileLoadingBean.ERROR_TYPE
            fileLoadingBean.url = downLoadUrl
            fileLoadingBean.msg = Log.getStackTraceString(e)
            emitter!!.onNext(fileLoadingBean)
        }
    }

    fun makeDir(dir: File) {
        if (!dir.exists()) {
            makeDir(checkNotNull(dir.parentFile))
            dir.mkdir()
        }
    }

    /**
     *取消正在下载的任务，listener会收到onCancel回调
     */
    fun cancel() {
        isCanceled = true
        fileLoadingBean = FileLoadingBean()
        fileLoadingBean.type = FileLoadingBean.CANCEL_TYPE
        emitter!!.onNext(fileLoadingBean)
    }
}