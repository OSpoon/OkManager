package com.spoon.okmanager.builder;

import com.spoon.okmanager.base.OkHttpRequestBuilder;
import com.spoon.okmanager.base.RequestCall;
import com.spoon.okmanager.request.PostFileRequest;

import java.io.File;

import okhttp3.MediaType;

public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder> {
    private File file;
    private MediaType mediaType;


    public OkHttpRequestBuilder file(File file) {
        this.file = file;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostFileRequest(url, tag, params, headers, file, mediaType, id).build();
    }

}
