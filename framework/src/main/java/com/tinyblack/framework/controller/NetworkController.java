package com.tinyblack.framework.controller;

/**
 * 基础网络的数据 证书/baseUrl
 *
 * @author yubiao
 */
public class NetworkController {

    private static String baseUrl = null;

    /**
     * 证书信息
     */
    private static String caCrt = null;
    private static String clientCrt = null;
    private static String clientKey = null;


    public static void setBaseUrl(String baseUrl) {
        NetworkController.baseUrl = baseUrl;
    }

    /***
     * 设置证书
     * @param caCrt
     * @param clientCrt
     * @param clientKey
     */
    public static void setSSLCert(String caCrt, String clientCrt, String clientKey) {
        NetworkController.caCrt = caCrt;
        NetworkController.clientCrt = clientCrt;
        NetworkController.clientKey = clientKey;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getCaCrt() {
        return caCrt;
    }

    public static String getClientCrt() {
        return clientCrt;
    }

    public static String getClientKey() {
        return clientKey;
    }
}
