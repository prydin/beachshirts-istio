package com.wfsample.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.wavefront.sdk.jaxrs.client.WavefrontJaxrsClientFilter;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import java.io.File;
import java.io.IOException;

/**
 * Utilities for use by the various beachshirts application related services.
 *
 * @author Srujan Narkedamalli (snarkedamall@wavefront.com).
 */
public final class BeachShirtsUtils {

  private BeachShirtsUtils() {
  }

  public static <T> T createProxyClient(String url, Class<T> clazz,
                                        WavefrontJaxrsClientFilter filter, B3HeadersRequestFilter b3Filter) {
    HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(2000).
        setMaxConnPerRoute(1000).build();
    ApacheHttpClient4Engine apacheHttpClient4Engine = new ApacheHttpClient4Engine(httpClient, true);
    ResteasyProviderFactory factory = ResteasyProviderFactory.getInstance();
    factory.registerProvider(ResteasyJackson2Provider.class);
    ResteasyClient resteasyClient = new ResteasyClientBuilder().
        httpEngine(apacheHttpClient4Engine).providerFactory(factory).register(filter).register(b3Filter).build();
    ResteasyWebTarget target = resteasyClient.target(url);
    return target.proxy(clazz);
  }

  public static GrpcServiceConfig scenarioFromFile(String file) throws IOException {
    File configFile = new File(file);
    GrpcServiceConfig config;
    if (configFile.exists()) {
      YAMLFactory factory = new YAMLFactory(new ObjectMapper());
      YAMLParser parser = factory.createParser(configFile);
      config = parser.readValueAs(GrpcServiceConfig.class);
    } else {
      config = new GrpcServiceConfig();
    }
    return config;
  }
}
