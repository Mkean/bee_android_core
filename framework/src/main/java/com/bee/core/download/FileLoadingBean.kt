package com.bee.core.download

data class FileLoadingBean(
        /**
         * 1.onStart
         * 2.onProgress
         * 3.onFinish
         * 4.onCancel
         * 5.onError
         */
        var type: Int = 0,
        /**
         * 文件大小
         */
        var total: Long = 0,
        /**
         * 下载进度
         */
        var progress: Int = 0,
        /**
         * 异常信息
         */
        var msg: String = "",
        var path: String = "",
        var url: String = ""
) {
    companion object {
        const val START_TYPE = 1
        const val PROGRESS_TYPE = 2
        const val FINISH_TYPE = 3
        const val CANCEL_TYPE = 4
        const val ERROR_TYPE = 5
    }


}