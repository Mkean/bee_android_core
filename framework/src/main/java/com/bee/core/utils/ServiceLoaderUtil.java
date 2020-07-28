package com.bee.core.utils;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Description: 用于组件收集
 */
public class ServiceLoaderUtil {

    public static <T> Iterator<T> loadAllServices(Class<T> clazz) {
        ServiceLoader<T> load = ServiceLoader.load(clazz);
        Iterator<T> iterator = load.iterator();
        return iterator;
    }
}
