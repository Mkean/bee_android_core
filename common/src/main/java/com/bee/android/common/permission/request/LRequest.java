package com.bee.android.common.permission.request;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.bee.android.common.permission.checker.StrictChecker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * [5.0 - 6.0) 权限处理
 */
public class LRequest extends BaseRequest {

    private static final String TAG = "LRequest";

    public LRequest(DealPermissionListener dealListener) {
        super(dealListener);
    }

    @Override
    public void requestPermission(FragmentActivity context, String[] permissions) {
        List<String> deny = new ArrayList<>();
        List<String> denyWithNoAsk = new ArrayList<>();
        Observable.fromArray(permissions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (context != null && !StrictChecker.hasPermission(context, s)) {
                            denyWithNoAsk.add(s);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "requestPermission error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (mDealListener != null) {
                            mDealListener.dealFinish(deny, denyWithNoAsk);
                        }
                    }
                });
    }
}
