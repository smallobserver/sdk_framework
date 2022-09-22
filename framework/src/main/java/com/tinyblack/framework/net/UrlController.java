package com.tinyblack.framework.net;

/**
 * 项目的根域名管理
 *
 * @author yubiao
 */
public class UrlController {
    public static final int TYPE_DEVELOP = 1;
    public static final int TYPE_PRODUCTION = 2;
    public static final int TYPE_PRE_PRODUCTION = 3;
    public static final int TYPE_PERF = 4;

    private static final String DEVELOP_URL = "https://mivigw-sit.dfiov.com.cn/";
//    private static final String DEVELOP_URL = "http://10.10.170.232:8080";


    private static final String PRE_PRODUCTION_URL = "https://mivigw-uat.dfiov.com.cn/";

    private static final String PRODUCTION_URL = "https://mivigw-pro.dfiov.com.cn/";

    private static final String PERF_URL = "https://mivigw-perf.dfiov.com.cn/";


    private static String currentUrl = DEVELOP_URL;


    /**
     * 获取根域名
     *
     * @return
     */
    public static String getBaseUrl() {
        return currentUrl;
    }

    /**
     * 切换所选的根域名
     *
     * @param type
     */
    public static void changeCurrentUrl(int type) {
        switch (type) {
            case TYPE_DEVELOP:
                currentUrl = DEVELOP_URL;
                break;
            case TYPE_PRODUCTION:
                currentUrl = PRODUCTION_URL;
                break;
            case TYPE_PRE_PRODUCTION:
                currentUrl = PRE_PRODUCTION_URL;
                break;
            case TYPE_PERF:
                currentUrl = PERF_URL;
                break;
            default:
        }
    }
}
