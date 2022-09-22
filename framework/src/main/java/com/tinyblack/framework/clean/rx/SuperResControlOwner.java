package com.tinyblack.framework.clean.rx;

/**
 * 新增的资源管理器,支持局部释放
 *
 * @param <T>
 * @author yubiao
 */
public interface SuperResControlOwner<T> extends ResControlOwner<T> {

    /**
     * 添加统一的支持资源处理
     *
     * @param resObject
     */
    void addResource(ResObject resObject);

    /**
     * 释放指定的资源
     *
     * @param resObject
     */
    void releaseResource(ResObject resObject);
}
