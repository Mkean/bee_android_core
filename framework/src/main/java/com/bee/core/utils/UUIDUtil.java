package com.bee.core.utils;

import java.util.UUID;

/**
 * @Description:
 */
public class UUIDUtil {
    /**
     * 获取uuid
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }
}
