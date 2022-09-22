package com.tinyblack.framework.clean.base;

import androidx.annotation.NonNull;

/**
 *
 */

public interface CommonCallback<T> extends BaseCallback {

    void onLoadSuccess(@NonNull T t);

    void onLoadFail(Throwable throwable);
}
