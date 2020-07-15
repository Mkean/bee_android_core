package com.bee.android.common.persistence

import com.tencent.mmkv.MMKVHandler
import com.tencent.mmkv.MMKVLogLevel
import com.tencent.mmkv.MMKVRecoverStrategic

class MMKVLog : MMKVHandler {


    override fun onMMKVCRCCheckFail(p0: String?): MMKVRecoverStrategic? {
        return null
    }

    override fun onMMKVFileLengthError(p0: String?): MMKVRecoverStrategic? {
        return null
    }

    override fun wantLogRedirecting(): Boolean {
        return true
    }

    override fun mmkvLog(p0: MMKVLogLevel?, p1: String?, p2: Int, p3: String?, p4: String?) {
    }
}