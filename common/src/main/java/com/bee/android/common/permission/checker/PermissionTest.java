package com.bee.android.common.permission.checker;

import android.content.Context;
import android.database.Cursor;

/**
 * 权限软件测试工具
 */
public interface PermissionTest {
    boolean test(Context context);

    class CursorTest {
        static void read(Cursor cursor) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                int type = cursor.getType(0);
                switch (type) {
                    case Cursor.FIELD_TYPE_BLOB:
                    case Cursor.FIELD_TYPE_NULL: {
                        break;
                    }
                    case Cursor.FIELD_TYPE_INTEGER:
                    case Cursor.FIELD_TYPE_FLOAT:
                    case Cursor.FIELD_TYPE_STRING:
                    default: {
                        cursor.getString(0);
                        break;
                    }
                }
            }
        }
    }
}
