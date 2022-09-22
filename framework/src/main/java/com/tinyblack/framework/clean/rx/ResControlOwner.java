package com.tinyblack.framework.clean.rx;

import androidx.annotation.NonNull;

/**
 * 资源控制器的所有者，一般是需要操作ResControl的类来实现
 *
 * @author yubiao
 */
public interface ResControlOwner<T> {

    /**
     * 添加一个资源控制器
     *
     * @param resControl
     */
    void addResControl(@NonNull ResControl resControl);

    /**
     * 添加一个具体的资源对象
     *
     * @param t
     */
    void addResource(@NonNull T t);

    /**
     * 释放所有的资源对象
     */
    void releaseResource();
}
