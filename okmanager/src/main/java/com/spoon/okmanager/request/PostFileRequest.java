package com.spoon.okmanager.request;

import com.spoon.okmanager.OkManager;
import com.spoon.okmanager.base.Callback;
import com.spoon.okmanager.base.OkHttpRequest;
import com.spoon.okmanager.utils.Exceptions;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostFileRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    private File file;
    private MediaType mediaType;

    public PostFileRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, File file, MediaType mediaType, int id) {
        super(url, tag, params, headers, id);
        this.file = file;
        this.mediaType = mediaType;

        if (this.file == null) {
            Exceptions.illegalArgument("the file can not be null !");
        }
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_STREAM;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, file);
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        if (callback == null) return requestBody;
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {

                OkManager.getInstance().getDelivery().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(bytesWritten * 1.0f / contentLength, contentLength, id);
                    }
                });

            }
        });
        return countingRequestBody;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }


}
