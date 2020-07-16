package com.bee.launch.persistence;

import android.text.TextUtils;

import com.bee.android.common.persistence.MMKVManager;
import com.bee.android.common.persistence.Persistence;
import com.tencent.mmkv.MMKV;

import java.util.Set;

public class MMKVUtils {

    /**
     * 保存字符串
     *
     * @param id    区分不同业务组件的Persistence实例，相当于不同名字的SharedPreferences
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(String id, String key, String value) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.putString(key, value).commit();
    }

    public static String getString(String id, String key, String defaultValue) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.getString(key, defaultValue);
    }

    public static boolean putBoolean(String id, String key, boolean value) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String id, String key, boolean defaultValue) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.getBoolean(key, defaultValue);
    }

    public static boolean putInt(String id, String key, int value) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.putInt(key, value).commit();
    }

    public static int getInt(String id, String key, int defaultValue) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.getInt(key, defaultValue);
    }

    public static boolean putFloat(String id, String key, float value) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.putFloat(key, value).commit();
    }

    public static float getFloat(String id, String key, float defaultValue) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.getFloat(key, defaultValue);
    }

    public static boolean putLong(String id, String key, long value) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.putLong(key, value).commit();
    }

    public static long getLong(String id, String key, long defaultLong) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.getLong(key, defaultLong);
    }

    /**
     * 删除
     *
     * @param id
     * @param key
     * @return
     */
    public static boolean remove(String id, String key) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.remove(key).commit();
    }

    /**
     * 清除
     *
     * @param id
     * @return
     */
    public static boolean clear(String id) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.clear().commit();
    }

    /**
     * 异步提交
     *
     * @param id
     */
    public static void apply(String id) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        persistence.apply();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param id
     * @param key
     * @return
     */
    public static boolean contains(String id, String key) {
        Persistence persistence = MMKVInstance.getPersistence(id);
        return persistence.contains(key);
    }

    /**
     * 所有的key
     *
     * @param id
     * @return
     */
    public static String[] allKeys(String id) {
        MMKV mmkv = MMKVManager.INSTANCE.mmkvWithId(id);
        return mmkv.allKeys();
    }

    public static Object getObjectValue(String id, String key) {
        MMKV mmkv = MMKVManager.INSTANCE.mmkvWithId(id);
        // 因为其他基础类型value会读成空字符串,所以不是空字符串即为string or string-set类型
        String value = mmkv.decodeString(key);
        if (!TextUtils.isEmpty(value)) {
            // 判断 string or string-set
            if (value.charAt(0) == 0x01) {
                return mmkv.decodeStringSet(key);
            } else {
                return value;
            }
        }
        // float double类型可通过string-set配合判断
        // 通过数据分析可以看到类型为float或double时string类型为空字符串且string-set类型读出空数组
        // 最后判断float为0或NAN的时候可以直接读成double类型,否则读float类型
        // 该判断方法对于非常小的double类型数据 (0d < value <= 1.0569021313E-314) 不生效
        Set<String> set = mmkv.decodeStringSet(key);
        if (set != null && set.size() == 0) {
            Float valueFloat = mmkv.decodeFloat(key);
            Double valueDouble = mmkv.decodeDouble(key);
            if (Float.compare(valueFloat, 0f) == 0 || Float.compare(valueFloat, Float.NaN) == 0) {
                return valueDouble;
            } else {
                return valueFloat;
            }
        }
        // int long bool 类型的处理放在一起, int类型1和0等价于bool类型true和false
        // 判断long或int类型时, 如果数据长度超出int的最大长度, 则long与int读出的数据不等, 可确定为long类型
        int valueInt = mmkv.decodeInt(key);
        long valueLong = mmkv.decodeLong(key);
        if (valueInt != valueLong) {
            return valueLong;
        } else {
            return valueInt;
        }
    }
}
