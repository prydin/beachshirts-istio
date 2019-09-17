package com.wfsample.common;

import com.google.common.annotations.VisibleForTesting;
import io.grpc.*;

import java.util.logging.Logger;

public class B3ServerHeaderInterceptor implements ServerInterceptor {
    private static final Logger logger = Logger.getLogger(B3ServerHeaderInterceptor.class.getName());

    private ThreadLocal<Metadata> metadata = new ThreadLocal<>();

    @VisibleForTesting
    static final Metadata.Key<String> CUSTOM_HEADER_KEY =
            Metadata.Key.of("custom_server_header_key", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            final Metadata requestHeaders,
            ServerCallHandler<ReqT, RespT> next) {
        metadata.set(requestHeaders);
        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {}, requestHeaders);
    }

    public Metadata getRequestHeaders() {
        return metadata.get();
    }
}