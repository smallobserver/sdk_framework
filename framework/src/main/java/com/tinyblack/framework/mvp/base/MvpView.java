package com.tinyblack.framework.mvp.base;

import android.content.Context;

/**
 * @author 余彪
 * @date 2022/4/15
 */

public interface MvpView {

    /**
     * 上下文
     *
     * @return
     */
    Context getContext();

    void showError();
}
