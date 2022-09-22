package com.tinyblack.framework.permission;

public interface PermissionListener {
    /**
     * 允许权限
     */
    void onAllowPermission();

    /**
     * 拒绝权限
     */
    void onRefusePermission();

    /**
     * 抛异常
     *
     * @param throwable
     */
    void onError(Throwable throwable);
}
