package com.ysh.myapplication;

import android.util.Log;

public class test {
    public static void main(String[] args) {
        String url = "http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724";

        httputil.getInstance().doHttpRequest("GET", url, null, new httputil.StringCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("ysh", "onSuccess: " + result);
            }

            @Override
            public void onFailure(int code, Exception e) {
                Log.e("ysh", "onFailure: " + e.getMessage());
            }
        });
    }
}
