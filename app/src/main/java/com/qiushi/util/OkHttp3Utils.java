package com.qiushi.util;


import android.content.Context;

import okhttp3.OkHttpClient;


/**
 * Created by Steven
 * on 16-1-7.
 */
public class OkHttp3Utils {
    private static OkHttpClient okHttpClient = null;
    private static OkHttp3Utils okHttpUtils = null;

    private OkHttp3Utils(Context context) {
        okHttpClient = getOkHttpSingletonInstance();
    }

    public static OkHttp3Utils getOkHttpClientUtils(Context context) {
        if (okHttpUtils == null) {
            synchronized (OkHttp3Utils.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new OkHttp3Utils(context);
                }
            }
        }
        return okHttpUtils;
    }

    public static OkHttpClient getOkHttpSingletonInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }
}