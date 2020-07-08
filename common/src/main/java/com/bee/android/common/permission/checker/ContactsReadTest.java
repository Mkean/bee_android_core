package com.bee.android.common.permission.checker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * 测试读取联系人
 */
public class ContactsReadTest implements PermissionTest {

    @Override
    public boolean test(Context context) {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = new String[]{ContactsContract.Data._ID, ContactsContract.Data.DATA1};
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                null);
        if (cursor != null) {
            try {
                CursorTest.read(cursor);
            } finally {
                cursor.close();
            }
            return true;
        } else {
            return false;
        }
    }
}
