package com.bee.update;

import com.bee.update.base.CheckCallback;
import com.bee.update.base.CheckNotifier;
import com.bee.update.base.CheckWorker;
import com.bee.update.base.DownloadNotifier;
import com.bee.update.base.DownloadWorker;
import com.bee.update.base.FileChecker;
import com.bee.update.base.FileCreator;
import com.bee.update.base.InstallNotifier;
import com.bee.update.base.InstallStrategy;
import com.bee.update.base.RestartHandler;
import com.bee.update.base.UpdateChecker;
import com.bee.update.base.UpdateParser;
import com.bee.update.base.UpdateStrategy;
import com.bee.update.flow.CallbackDelegate;
import com.bee.update.flow.Launcher;
import com.bee.update.model.CheckEntity;

/**
 * 此类用于建立真正的更新任务，每个更新任务对应一个{@link UpdateBuilder} 实例。
 *
 * <p> 创建更新任务有两种当时<br>
 * 1. 通过{@link #create()}进行创建，代表将使用默认提供的全局更新配置。此默认更新配置通过{@link UpdateConfig#getConfig()}进行获取。<br>
 * 2. 通过{@lin #create(UpdateConfig)}指定使用某个特殊的更新配置。<br>
 *
 * <p> 此Builder中的所有配置项，均在{@link UpdateConfig} 中有对应的相同方法名的配置函数，此两者的关系为：
 * 在更新流程中：当Builder中未设置对应的配置，将会使用在{@link UpdateConfig}更新配置中所提供的默认配置。
 *
 * <p> 正常启动：调用{@link #check()}
 *
 * <p> 后台启动：调用{@link #checkWithDaemon(Long)}
 */
public class UpdateBuilder {

    private boolean isDaemon;
    private Class<? extends CheckWorker> checkWorker;
    private Class<? extends DownloadWorker> downloadWorker;
    private CheckEntity entity;
    private UpdateStrategy updateStrategy;
    private CheckNotifier checkNotifier;
    private InstallNotifier installNotifier;
    private DownloadNotifier downloadNotifier;
    private DownloadNotifier downloadErrorNotifier;

    public DownloadNotifier getDownloadErrorNotifier() {
        return downloadErrorNotifier;
    }

    public void setDownloadErrorNotifier(DownloadNotifier downloadErrorNotifier) {
        this.downloadErrorNotifier = downloadErrorNotifier;
    }

    private UpdateParser updateParser;
    private FileCreator fileCreator;
    private UpdateChecker updateChecker;
    private FileChecker fileChecker;
    private InstallStrategy installStrategy;
    private UpdateConfig config;
    private RestartHandler restartHandler;

    private CallbackDelegate callbackDelegate;

    private UpdateBuilder(UpdateConfig config) {
        this.config = config;
        callbackDelegate = new CallbackDelegate();
        callbackDelegate.setCheckDelegate(config.getCheckCallback());
        callbackDelegate.setDownloadDelegate(config.getDownloadCallback());

    }

    /**
     * 使用默认全局配置进行更新任务创建，默认全局配置可通过{@link UpdateConfig#getConfig()}进行获取
     *
     * @return
     */
    public static UpdateBuilder create() {
        return create(UpdateConfig.getConfig());
    }

    /**
     * 指定该更新任务所使用的更新配置，可通过{@link UpdateConfig#createConfig()} ()}进行新的跟新配置创建。
     *
     * @param config 指定使用的更新配置
     * @return
     */
    public static UpdateBuilder create(UpdateConfig config) {
        return new UpdateBuilder(config);
    }

    /**
     * 启动更新任务，可在任意线程进行启动。
     */
    public void check() {
        Launcher.getInstance().launchCheck(this);
    }

    /**
     * 启动后台更新任务，特性：当检查更新失败，当前无更新或者用户取消更新时，等待指定时间后，自动重启更新任务
     *
     * @param retryTime 重启时间间隔，单位为妙
     */
    public void checkWithDaemon(long retryTime) {

    }

    public CallbackDelegate getCheckCallback() {
        return callbackDelegate;
    }

}
