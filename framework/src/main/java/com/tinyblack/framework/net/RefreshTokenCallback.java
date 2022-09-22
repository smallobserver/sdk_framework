package com.tinyblack.framework.net;

/**
 * 提供给宿主实现,用于刷新 token, 子线程同步调用
 *
 * @author yubiao
 */
public interface RefreshTokenCallback {

    /**
     * 子线程调用,获取新 token
     *
     * @return 刷新后的 token
     */
    String onRefreshToken();

}
