package com.bee.update.impl;

import android.text.TextUtils;

import com.bee.android.common.base.CommonApplication;
import com.bee.core.logger.CommonLogger;
import com.bee.core.manager.CommonFileManager;
import com.bee.update.UpdateBuilder;
import com.bee.update.UpdateConfig;
import com.bee.update.base.DownloadWorker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 默认的apk下载任务，支持断点下载功能。
 * <p>
 * 若需定制，则可通过{@link UpdateConfig#setDownloadWorker(Class)}或者{@link UpdateBuilder#setDownloadWorker(Class)}
 */
public class DefaultDownloadWorker extends DownloadWorker {
    private final String TAG = "Update";

    private HttpURLConnection urlConn;
    private File original;
    private File bak;
    private long contentLength;
    public static volatile boolean downloadFlag;
    IOException e;

    @Override
    protected void download(String url, File target) throws Exception {
        original = target;
        URL httpUrl = new URL(url);
        urlConn = (HttpURLConnection) httpUrl.openConnection();
        setDefaultProperties();
        urlConn.connect();

        int responseCode = urlConn.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            CommonLogger.d(TAG, "APK包下载网络请求失败");
            urlConn.disconnect();
            throw new HttpException(responseCode, urlConn.getResponseMessage());
        }

        contentLength = urlConn.getContentLength();
        File totalLengthFile = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"),"totalLengthFile");
        if (!totalLengthFile.exists()) {
            if (!totalLengthFile.getParentFile().exists()) {
                totalLengthFile.getParentFile().createNewFile();
            }
            totalLengthFile.createNewFile();
            CommonLogger.d(TAG, "apk总长度文件是否 创建成功" + totalLengthFile.exists());
            if (totalLengthFile.length() == 0) {
                CommonLogger.d(TAG, "下载文件前第一次保存apk包的总大小");
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(totalLengthFile));
                bufferedWriter.write(String.valueOf(contentLength));
                bufferedWriter.flush();
                bufferedWriter.close();
            }
        }
        CommonLogger.d(TAG, "保存到本地的apk文件的总大小" + contentLength);

        // 使用此bak文件进行下载。辅助进行断点下载。
        if (checkIsDownAll()) {
            CommonLogger.d("文件下载路径Target: ", original.getAbsolutePath());
            CommonLogger.d("APK包contentLength : ", contentLength + "");
            urlConn.disconnect();
            urlConn = null;
            // notify download completed
            sendDownloadComplete(original);
            return;
        }

        createBakFile();
        FileOutputStream writer = supportBreakpointDownload(httpUrl);

        long offset = bak.length();
        CommonLogger.d(TAG, "断点续传当前文件的大小: " + offset);
        InputStream inputStream = urlConn.getInputStream();
        byte[] buffer = new byte[8 * 1024];
        int length;
        long start = System.currentTimeMillis();
        try {
            while ((length = inputStream.read(buffer)) != -1 && offset <= contentLength && downloadFlag) {
                writer.write(buffer, 0, length);
                offset += length;
                long end = System.currentTimeMillis();
                if (end - start > 1000) {
                    sendDownloadProgress(offset, contentLength);
                    start = System.currentTimeMillis();
                }
            }

            if (!downloadFlag) {
                CommonLogger.d(TAG, "双击退出移除网络下载");
                sendDownloadError(new IOException());
                return;
            }

            downloadFlag = false;
        } catch (IOException e) {
            this.e = e;
            CommonLogger.d(TAG, "已下载到：" + offset);
            e.printStackTrace();
            CommonLogger.d(TAG, "网络中断catch ");
            sendDownloadError(e);
            return;
        } finally {
            CommonLogger.d(TAG, "关闭流");

            urlConn.disconnect();
            writer.close();
            urlConn = null;

        }


        // notify download completed
        renameAndNotifyCompleted();
    }

    private boolean checkIsDownAll() {
        return original.length() == contentLength
                && contentLength > 0;
    }

    volatile long length;

    private FileOutputStream supportBreakpointDownload(URL httpUrl) throws IOException {

        String range = urlConn.getHeaderField("Accept-Ranges");
        if (TextUtils.isEmpty(range) || !range.startsWith("bytes")) {
            bak.delete();
            return new FileOutputStream(bak, false);
        }

        length = bak.length();

        urlConn.disconnect();
        urlConn = (HttpURLConnection) httpUrl.openConnection();
        CommonLogger.d(TAG, "断点续传 RANGE ：" + length);
        CommonLogger.d(TAG, "断点续传 contentLength ：" + contentLength);
        urlConn.setRequestProperty("RANGE", "bytes=" + length + "-" + (contentLength - 1));
        urlConn.setDoInput(true);
        setDefaultProperties();
        urlConn.connect();

        int responseCode = urlConn.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            CommonLogger.d(TAG, "APK包下载网络请求失败");
            throw new HttpException(responseCode, urlConn.getResponseMessage());
        }

        return new FileOutputStream(bak, true);
    }

    private void setDefaultProperties() throws IOException {
        urlConn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
        urlConn.setRequestMethod("GET");
        urlConn.setConnectTimeout(60000);
        urlConn.setReadTimeout(60000);
    }

    // 创建bak文件。
    private void createBakFile() {
        bak = new File(String.format("%s_%s", original.getAbsolutePath(), contentLength));
    }

    private void renameAndNotifyCompleted() {
        original.delete();
        bak.renameTo(original);
        sendDownloadComplete(original);
    }
}
