package com.wfsample.common;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

import javax.ws.rs.core.HttpHeaders;
import java.util.logging.Logger;


public class B3ClientHeaderInjector implements ClientInterceptor {

    private static final Logger logger = Logger.getLogger(B3ClientHeaderInjector.class.getName());

    private final ThreadLocal<Metadata> metadata = new ThreadLocal<>();

    private final ThreadLocal<HttpHeaders> httpHeaders = new ThreadLocal<>();

    public void setHttpHeaders(HttpHeaders h) {
        httpHeaders.set(h);
    }

    public void setGRPCHeaders(Metadata m) {
        metadata.set(m);
    }


    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                if(metadata.get() != null) {
                    for(String key : metadata.get().keys()) {
                        Metadata.Key mk = Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER);
                        if(B3Headers.isB3Header(key)) {
                            headers.put(mk, metadata.get().get(mk));
                        }
                    }
                    headers.merge(metadata.get());
                }
                HttpHeaders h = httpHeaders.get();
                if(h != null) {
                    for(String key : h.getRequestHeaders().keySet()) {
                        if(B3Headers.isB3Header(key)) {
                            logger.info("Copied b3 header: " + key);
                            headers.put(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER), h.getHeaderString(key));
                        }
                    }
                }
                super.start(responseListener, headers);
            }
        };
    }
}



