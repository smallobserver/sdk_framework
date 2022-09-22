package com.tinyblack.framework.controller;

/**
 * 用户数据控制器
 *
 * @author yubiao
 */
public class UserInfoController {

    private static String userId;
    private static String phone;

    public static void setUserInfo(String userId, String phone) {
        UserInfoController.userId = userId;
        UserInfoController.phone = phone;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getPhone() {
        return phone;
    }
}
