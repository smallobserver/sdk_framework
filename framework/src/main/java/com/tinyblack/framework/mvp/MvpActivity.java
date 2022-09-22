package com.tinyblack.framework.mvp;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tinyblack.framework.base.BaseActivity;
import com.tinyblack.framework.log.TLog;
import com.tinyblack.framework.mvp.base.MvpView;
import com.tinyblack.framework.util.eventBus.LoadingEvent;
import com.tinyblack.framework.util.eventBus.RxBus;
import com.tinyblack.framework.widget.Loading;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * mvpActivity
 *
 * @author yubiao
 */
public abstract class MvpActivity extends BaseActivity implements MvpView {
    private Disposable subscribe;
    private Loading loading;
    private boolean isTopResumedActivity;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showError() {
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribe = RxBus.getRxBus().toEventOnMain(LoadingEvent.class).subscribe(loadingEvent -> {
            if (loadingEvent.isShow) {
                if (isTopResumedActivity) {
                    showLoading(loadingEvent.message);
                }
            } else {
                hideLoading();
            }
        }, throwable -> TLog.e(throwable.getMessage()));
    }

    @Override
    public void onTopResumedActivityChanged(boolean isTopResumedActivity) {
        super.onTopResumedActivityChanged(isTopResumedActivity);
        this.isTopResumedActivity = isTopResumedActivity;
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
        super.onDestroy();
    }

    protected void showLoading() {
        showLoading(null);
    }

    protected void showLoading(String msg) {
        if (loading == null) {
            Loading.Builder builder = new Loading.Builder(this)
                    .setMessage(msg)
                    .setCancelable(true)
                    .setCancelOutside(true);
            loading = builder.create();
        } else {
            loading.setLoadingMessage(msg);
        }
        if (!loading.isShowing()) {
            loading.showDialog();
        }
    }

    protected void hideLoading() {
        if (loading != null) {
            loading.dismissDialog();
        }
    }
}
