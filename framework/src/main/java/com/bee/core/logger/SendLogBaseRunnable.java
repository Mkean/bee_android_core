package com.bee.core.logger;

import com.dianping.logan.SendLogRunnable;

/**
 * 作用描述
 */
public abstract class SendLogBaseRunnable extends SendLogRunnable {
    private long totalSize;
    private SendLogListener sendLogListener;
    private int fileCount;

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public SendLogListener getSendLogListener() {
        return sendLogListener;
    }

    public void setSendLogListener(SendLogListener sendLogListener) {
        this.sendLogListener = sendLogListener;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }
}
