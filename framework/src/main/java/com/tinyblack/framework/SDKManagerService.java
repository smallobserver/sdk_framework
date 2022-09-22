package com.tinyblack.framework;

import com.tinyblack.framework.controller.NetworkController;
import com.tinyblack.framework.controller.TokenController;
import com.tinyblack.framework.controller.UserInfoController;
import com.tinyblack.framework.net.RefreshTokenCallback;

/**
 * 全局多个 sdk 通用的服务
 *
 * @author yubiao
 */
public class SDKManagerService {

    /**
     * sdk 通用注册方法
     *
     * @param baseUrl
     * @param caCrt
     * @param clientCrt
     * @param clientKey
     * @param token
     * @param callback
     */
    public static void init(String baseUrl, String caCrt, String clientCrt, String clientKey, String token, RefreshTokenCallback callback) {
        //网络控制器数据初始化
        NetworkController.setBaseUrl(baseUrl);
        NetworkController.setSSLCert(caCrt, clientCrt, clientKey);
        //token 控制器数据初始化
        TokenController.token = token;
        TokenController.refreshTokenCallback = callback;
    }

    /**
     * 初始化用户数据
     *
     * @param userId
     * @param phone
     */
    public static void setUserInfo(String userId, String phone) {
        UserInfoController.setUserInfo(userId, phone);
    }

    /**
     * 更新 token
     *
     * @param token
     */
    public static void updateToken(String token) {
        TokenController.token = token;
    }

    /**
     * 更新证书
     *
     * @param caCrt
     * @param clientCrt
     * @param clientKey
     */
    public static void updateCert(String caCrt, String clientCrt, String clientKey) {
        NetworkController.setSSLCert(caCrt, clientCrt, clientKey);
    }
}
