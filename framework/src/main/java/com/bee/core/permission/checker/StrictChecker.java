package com.bee.core.permission.checker;

import android.Manifest;
import android.content.Context;
import android.os.Build;

/**
 * 权限检查工具
 */
public class StrictChecker {
    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        try {
            switch (permission) {
                case Manifest.permission.CAMERA:
                    return checkCamera(context);
                case Manifest.permission.READ_CONTACTS:
                    return checkReadContacts(context);
                case Manifest.permission.WRITE_CONTACTS:
                    return checkWriteContacts(context);
                case Manifest.permission.RECORD_AUDIO:
                    return checkRecordAudio(context);
                case Manifest.permission.READ_SMS:
                    return checkReadSms(context);
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    private static boolean checkCamera(Context context) {
        return new CameraTest().test(context);
    }

    private static boolean checkReadContacts(Context context) {
        return new ContactsReadTest().test(context);
    }

    private static boolean checkWriteContacts(Context context) {
        return new ContactsWriteTest().test(context);

    }

    private static boolean checkRecordAudio(Context context) {
        return new RecordAudioTest().test(context);
    }

    private static boolean checkReadSms(Context context) {
        return new SmsReadTest().test(context);
    }
}
