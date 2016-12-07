package com.spoon.okmanager;

import com.spoon.okmanager.base.Callback;
import com.spoon.okmanager.base.RequestCall;
import com.spoon.okmanager.builder.GetBuilder;
import com.spoon.okmanager.builder.HeadBuilder;
import com.spoon.okmanager.builder.OtherRequestBuilder;
import com.spoon.okmanager.builder.PostFileBuilder;
import com.spoon.okmanager.builder.PostFormBuilder;
import com.spoon.okmanager.builder.PostStringBuilder;
import com.spoon.okmanager.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class OkManager {

    public static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkManager(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
        mPlatform = Platform.get();
    }


    public static OkManager initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkManager.class) {
                if (mInstance == null) {
                    mInstance = new OkManager(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkManager getInstance() {
        return initClient(null);
    }

    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 构建GET请求
     */
    public static GetBuilder get() {
        return new GetBuilder();
    }

    /**
     * 构建POST字符串请求
     */
    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    /**
     * 构建POST文件请求
     */
    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    /**
     * 构建POST表单请求
     */
    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    /**
     * 执行请求
     */
    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;

        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //请求失败
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    //已取消
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }
                    //认证失败
                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }
                    //获取数据成功回调
                    Object o = finalCallback.parseNetworkResponse(response, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                    //异常导致失败
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }

    /**
     * 处理失败回调
     */
    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 处理成功回调
     */
    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 取消响应请求
     */
    public void cancelTag(Object tag) {
        //请求队列中的所有请求
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        //正在请求中的所有请求
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public static class METHOD {
        //HEAD： 只请求页面的首部。
        public static final String HEAD = "HEAD";
        //DELETE： 请求服务器删除指定的页面。
        public static final String DELETE = "DELETE";
        //PUT： 从客户端向服务器传送的数据取代指定的文档的内容。
        public static final String PUT = "PUT";
        //实体中包含一个表，表中说明与该URI所表示的原内容的区别。
        public static final String PATCH = "PATCH";
    }
}

