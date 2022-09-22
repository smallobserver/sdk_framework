package com.tinyblack.framework.clean.rx;

/**
 * 资源控制抽象
 *
 * @author yubiao
 * @date 2022/4/15
 */

public interface ResControl {

    boolean canDeal(ResObject t);

    void addResource(ResObject t);

    void releaseResource();

    /**
     * 释放指定的资源
     * @param resObject
     */
    void releaseResource(ResObject resObject);
}
