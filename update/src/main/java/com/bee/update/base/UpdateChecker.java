package com.bee.update.base;

import com.bee.update.model.Update;

/**
 * 此类用于对通过{@link UpdateParser}所解析返回的更新数据进行检查，判断是否此新版本数据需要被更新
 */
public abstract class UpdateChecker {

    /**
     * 对提供的更新实体类进行检查，判断是否需要进行更新
     *
     * @param update
     * @return {@code true} 需要被更新
     * @throws Exception
     */
    public abstract boolean check(Update update) throws Exception;

}
