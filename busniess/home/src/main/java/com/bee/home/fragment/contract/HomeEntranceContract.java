package com.bee.home.fragment.contract;

import com.bee.android.common.base.IPresenter;
import com.bee.android.common.base.IView;
import com.bee.home.fragment.bean.HomeNativeBean;

import java.util.List;

/**
 * @Description:
 */
public interface HomeEntranceContract {

    interface View extends IView {
        void setServerData(List<HomeNativeBean> data);

        void finishRefreshAndLoadMore();

        void showErrorView(boolean isShow, String code);

        boolean isHasData();
    }

    interface Presenter extends IPresenter<View> {

    }
}
