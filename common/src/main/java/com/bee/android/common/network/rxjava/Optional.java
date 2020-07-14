package com.bee.android.common.network.rxjava;

import androidx.annotation.Nullable;

import java.util.NoSuchElementException;


/**
 * Optional 这是个 Java8 新出的特性，是为了解决null安全问题出的API
 *
 * @param <M>
 */
public class Optional<M> {
    private final M optional; // 接收到的返回结果

    public Optional(@Nullable M optional) {
        this.optional = optional;
    }

    /**
     * 判断返回结果是否为 null
     *
     * @return
     */
    public boolean isEmpty() {
        return this.optional == null;
    }

    /**
     * 获取不能为 null 的返回结果，如果为 null，直接抛出异常，经过二次封装之后，这个异常最终可以在走向 RxJava 的 onError()
     *
     * @return
     */
    public M get() {
        if (optional == null) {
            throw new NoSuchElementException("No value present");
        }
        return optional;
    }

    /**
     * 获取可以为null的返回结果
     *
     * @return
     */
    public M getIncludeNull() {
        return optional;
    }
}
