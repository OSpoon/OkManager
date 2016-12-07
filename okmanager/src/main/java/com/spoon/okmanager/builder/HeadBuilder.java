package com.spoon.okmanager.builder;

import com.spoon.okmanager.OkManager;
import com.spoon.okmanager.base.RequestCall;
import com.spoon.okmanager.request.OtherRequest;

public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null, OkManager.METHOD.HEAD, url, tag, params, headers, id).build();
    }
}
