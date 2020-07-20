package com.bee.update.base;

import com.bee.update.UpdateBuilder;
import com.bee.update.UpdateConfig;
import com.bee.update.model.Update;

/**
 * 此类用于解析通过{@link CheckWorker}访问的地址返回的数据。解析出框架所需的{@link Update}实体类数据并提供给框架内部各处使用
 *
 * <p>配置方式：通过{@link UpdateConfig#setUpdateParser(UpdateParser)}或者{@link UpdateBuilder#setUpdateParser(UpdateParser)}
 */
public abstract class UpdateParser {

    /**
     * 当更新api网络任务请求成功时，将会触发到此，在此根据网络数据解析创建出对应的更新数据实体类并返回给框架层使用
     *
     * @param response 更新api返回的数据
     * @return 被创建的更新数据实体类，不能为null
     * @throws Exception 捕获异常，防止crash并统一处理
     */
    public abstract Update parse(String response) throws Exception;
}
