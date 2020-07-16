package com.bee.update.base;

import android.content.Context;

import com.bee.update.model.Update;

/**
 * 提供一个安装策略。便于针对不同的应用场景，定制不同的安装策略实现
 */
public abstract class InstallStrategy {
    /**
     * 在此定制你自己的安装策略。如
     * 1.普通环境下，调起系统安装页面
     * 2.插件化环境下，在此进行插件安装
     * 3.热修复环境下，在此进行热修复dex合并及更多兼容操作
     *
     * @param context  application context
     * @param fileName the apk fileName
     * @param update   更新数据实体类
     */
    public abstract void install(Context context, String fileName, Update update);
}
