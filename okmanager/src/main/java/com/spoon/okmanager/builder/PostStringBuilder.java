package com.spoon.okmanager.builder;

import com.spoon.okmanager.base.OkHttpRequestBuilder;
import com.spoon.okmanager.base.RequestCall;
import com.spoon.okmanager.request.PostStringRequest;

import okhttp3.MediaType;

public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {
    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostStringRequest(url, tag, params, headers, content, mediaType, id).build();
    }

}
