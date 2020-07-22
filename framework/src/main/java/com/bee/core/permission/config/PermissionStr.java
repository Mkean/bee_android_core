package com.bee.core.permission.config;

import android.Manifest;

import java.util.HashMap;

/**
 * 权限常量类
 */
public class PermissionStr {
    public static final HashMap<String, Item> PERMISSION_MAP = new HashMap<>();
    public static final String CAMERA = build(Manifest.permission.CAMERA, "相机");
    public static final String RECORD_AUDIO = build(Manifest.permission.RECORD_AUDIO, "麦克风");
    public static final String READ_EXTERNAL_STORAGE = build(Manifest.permission.READ_EXTERNAL_STORAGE, "存储");
    public static final String WRITE_EXTERNAL_STORAGE = build(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储");
    public static final String READ_CONTACTS = build(Manifest.permission.READ_CONTACTS, "读取联系人");
    public static final String WRITE_CONTACTS = build(Manifest.permission.WRITE_CONTACTS, "修改联系人");
    public static final String ACCESS_FINE_LOCATION = build(Manifest.permission.ACCESS_FINE_LOCATION, "定位");
    public static final String ACCESS_COARSE_LOCATION = build(Manifest.permission.ACCESS_COARSE_LOCATION, "定位");

    private static final String PREFIX = "android.permission.";


    public static String build(String permission, String chName) {
        PERMISSION_MAP.put(permission, new Item(permission, chName));
        return permission;
    }

    public static String query(String permission) {
        Item item = PERMISSION_MAP.get(permission);
        if (item == null) {
            return permission.replace(PREFIX, "");
        }
        return item.chName;
    }

    public static class Item {
        private String permission;
        private String chName;

        public Item(String permission, String chName) {
            this.permission = permission;
            this.chName = chName;
        }
    }
}
