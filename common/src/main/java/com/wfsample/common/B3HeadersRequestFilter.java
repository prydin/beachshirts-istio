package com.wfsample.common;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A request filter to propagate the B3 http headers required to enable proper trace hierarchy in
 * Istio.
 *
 * @author Anil Kodali (akodali@vmware.com)
 */
public class B3HeadersRequestFilter implements ClientRequestFilter {

  private ThreadLocal<HttpHeaders> httpHeaders = new ThreadLocal<>();

  private static Logger log = Logger.getLogger(B3HeadersRequestFilter.class.getName());

  public B3HeadersRequestFilter() {
  }

  @Override
  public void filter(ClientRequestContext clientRequest) throws IOException {
    HttpHeaders headers = httpHeaders.get();
    log.info("Setting headers: " + headers.getRequestHeaders());
    if(headers == null) {
      return;
    }
    for (Map.Entry<String, List<String>> httpheader : headers.getRequestHeaders().entrySet()) {
      // Set B3-headers required for tracing in Istio as per
      // https://istio.io/docs/tasks/telemetry/distributed-tracing/#understanding-what-happened
      if(B3Headers.isB3Header(httpheader.getKey())) {
          log.info("Set header '" + httpheader.getKey() + "' to '" + httpheader.getValue() + "'");
          clientRequest.getHeaders().add(httpheader.getKey(), httpheader.getValue().get(0));
      }
    }
  }

  public void setB3Headers(HttpHeaders h) {
    log.info("Received headers: " + h.getRequestHeaders());
    this.httpHeaders.set(h);
  }
}
