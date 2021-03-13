package com.ysh.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.Gson;

public class httputil {
    //线程池
    private static ExecutorService executor;
    private static Handler mHandler;




    //设置线程池模式 最多有5个线程
    private httputil() {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(5);
            mHandler = new Handler();
        }
    }

    private static httputil instance;

    //单例模式
    public static httputil getInstance() {
        if (instance == null) {
            instance = new httputil();
        }
        return instance;
    }


    //网络请求 返回String类
    public void doHttpRequest(final String method, final String url,
                                     final Map<String, String> params, final StringCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    URL u = new URL(url);
                    connection = (HttpURLConnection) u.openConnection();
                    // 设置输入可用
                    connection.setDoInput(true);
                    // 设置输出可用
                    connection.setDoOutput(true);
                    // 设置请求方式
                    connection.setRequestMethod(method);
                    // 设置连接超时
                    connection.setConnectTimeout(5000);
                    // 设置读取超时
                    connection.setReadTimeout(5000);
                    // 设置缓存不可用
                    connection.setUseCaches(false);
                    // 开始连接
                    connection.connect();

                    // 当params有参数时为POST申请
                    if (params != null) {
                        // 获取输出流,connection.getOutputStream已经包含了connect方法的调用
                        outputStream = connection.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, String>> sets = params.entrySet();  //把params中的，每一个元素放入map中
                        // 将Hashmap转换为string放入StringBuilder中
                        for (Map.Entry<String, String> entry : sets) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);
                        // 使用输出流将string类型的参数写到服务器
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String result = inputStream2String(inputStream);  //使用inputStream2String得到String结果
                        if (result != null && callback != null) {
                            postSuccessString(callback, result);  //如果result不为空 则post成功
                        }
                    } else {
                        if (callback != null) {
                            postFailed(callback, responseCode, new Exception("请求数据失败：" + responseCode));
                        }
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        postFailed(callback, 0, e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    //返回图片
    public String doHttpRequest(final String method, final String url,
                                       final Map<String, String> params, final BitmapCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                InputStream inputStream = null;
                try {
                    URL u = new URL(url);
                    connection = (HttpURLConnection) u.openConnection();

                    connection.setDoInput(true);

                    connection.setDoOutput(true);

                    connection.setRequestMethod(method);

                    connection.setConnectTimeout(5000);

                    connection.setReadTimeout(5000);

                    connection.setUseCaches(false);

                    connection.connect();


                    if (params != null) {

                        outputStream = connection.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, String>> sets = params.entrySet();

                        for (Map.Entry<String, String> entry : sets) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);

                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap != null && callback != null) {
                            postSuccessBitmap(callback, bitmap);
                        }
                    } else {
                        if (callback != null) {
                            postFailed(callback, responseCode, new Exception("请求失败：" + responseCode));
                        }
                    }


                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        postFailed(callback, 0, e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return null;
    }

    //返回byte[]
    public void doHttpRequest(final String method, final String url,
                                       final Map<String, String> params, final ByteArrayCallback callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    URL u = new URL(url);
                    connection = (HttpURLConnection) u.openConnection();

                    connection.setDoInput(true);

                    connection.setDoOutput(true);

                    connection.setRequestMethod(method);

                    connection.setConnectTimeout(5000);

                    connection.setReadTimeout(5000);

                    connection.setUseCaches(false);

                    connection.connect();


                    if (params != null) {
                        outputStream = connection.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, String>> sets = params.entrySet();
                        for (Map.Entry<String, String> entry : sets) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        byte[] bytes = inputStream2ByteArray(inputStream);
                        if (bytes != null && callback != null) {
                            postSuccessByte(callback, bytes);
                        }
                    } else {
                        if (callback != null) {
                            postFailed(callback, responseCode, new Exception("请求失败：" + responseCode));
                        }
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        postFailed(callback, 0, e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    public <T> void doHttpRequest(final String method, final String url,
                                         final Map<String, String> params, final Class<T> cls, final ObjectCallback callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    URL u = new URL(url);
                    connection = (HttpURLConnection) u.openConnection();

                    connection.setDoInput(true);

                    connection.setDoOutput(true);

                    connection.setRequestMethod(method);

                    connection.setConnectTimeout(5000);

                    connection.setReadTimeout(5000);

                    connection.setUseCaches(false);

                    connection.connect();


                    if (params != null) {
                        outputStream = connection.getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, String>> sets = params.entrySet();
                        for (Map.Entry<String, String> entry : sets) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String result = inputStream2String(inputStream);
                        if (result != null && callback != null) {
                            postSuccessObject(callback, new Gson().fromJson(result, cls));
                        }
                    } else {
                        if (callback != null) {
                            postFailed(callback, responseCode, new Exception("请求失败：" + responseCode));
                        }
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        postFailed(callback, 0, e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    private static void postSuccessString(final StringCallback callback, final String result) {
        mHandler.post(new Runnable() {   //新线程来传输message
            @Override
            public void run() {
                callback.onSuccess(result);//回到onSuccess接口
            }
        });
    }

    private static void postSuccessBitmap(final Callback callback, final Bitmap bitmap) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                BitmapCallback bitmapCallback = (BitmapCallback) callback;
                bitmapCallback.onSuccess(bitmap);
            }
        });
    }

    private static void postSuccessByte(final Callback callback, final byte[] bytes) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ByteArrayCallback byteArrayCallback = (ByteArrayCallback) callback;
                byteArrayCallback.onSuccess(bytes);
            }
        });
    }

    private static <T> void postSuccessObject(final ObjectCallback callback, final T t) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ObjectCallback objectCallback = (ObjectCallback) callback;
                objectCallback.onSuccess(t);
            }
        });
    }

    private static void postFailed(final Callback callback, final int code, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(code, e);
            }
        });
    }


    private static String inputStream2String(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] inputStream2ByteArray(InputStream inputStream) {
        byte[] result = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 缓冲区
        byte[] bytes = new byte[1024];
        int len = -1;
        try {
            // 使用字节数据输出流来保存数据
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            result = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    
    public static boolean isNetWorkConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public interface Callback {
        void onFailure(int code, Exception e);
    }

    public interface StringCallback extends Callback {   //
        void onSuccess(String result);
    }

    public interface BitmapCallback extends Callback {
        void onSuccess(Bitmap bitmap);
    }

    public interface ByteArrayCallback extends Callback {
        void onSuccess(byte[] bytes);
    }

    public interface ObjectCallback<T> extends Callback {
        void onSuccess(T t);
    }
}
