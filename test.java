package com.ysh.httputil;

import android.util.Log;

public class test {
    public static void main(String[] args) {
        String url = "http://qt.qq.com/php_cgi/news/php/varcache_getnews.php?id=12&page=0&plat=android&version=9724";
        httputil.doHttpReqeust("GET", url, null, new httputil.StringCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFaileure(int code, Exception e) {
                Log.e("xyh", "onFaileure: " + e.getMessage());
            }
        });
    }
}
