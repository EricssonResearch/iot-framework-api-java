package com.ericsson.research;

import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import org.apache.log4j.Logger;

public class AsyncHandler implements com.ning.http.client.AsyncHandler<String> {

    private static final Logger logger = Logger.getLogger(AsyncHandler.class);

    @Override
    public void onThrowable(Throwable throwable) {
        logger.error(throwable);
    }

    @Override
    public STATE onBodyPartReceived(HttpResponseBodyPart httpResponseBodyPart) throws Exception {
        return STATE.CONTINUE;
    }

    @Override
    public STATE onStatusReceived(HttpResponseStatus httpResponseStatus) throws Exception {
        if (httpResponseStatus.getStatusCode() != 200) {
            logger.error("Http status was " + httpResponseStatus.getStatusCode());
        }

        return STATE.CONTINUE;
    }

    @Override
    public STATE onHeadersReceived(HttpResponseHeaders httpResponseHeaders) throws Exception {
        return STATE.CONTINUE;
    }

    @Override
    public String onCompleted() throws Exception {
        return null;
    }
}