package com.tinyblack.framework.mvp;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.tinyblack.framework.mvp.base.MvpView;


/**
 * @author 余彪
 * @date 2022/4/15
 */

public interface MvpPresenter<V extends MvpView> {

    /**
     * 绑定 view
     *
     * @param view
     */
    @UiThread
    void attachView(@NonNull V view);

    /**
     * 接触绑定
     */
    @UiThread
    void detachView();

    /**
     * presenter是否还和界面绑定
     *
     * @return
     */
    boolean isViewAttached();

}
