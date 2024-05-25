package com.payneteasy.odterm.metrics;

import com.payneteasy.http.server.api.handler.IHttpRequestHandler;
import com.payneteasy.http.server.api.request.HttpRequest;
import com.payneteasy.http.server.api.response.HttpResponse;
import com.payneteasy.http.server.api.response.HttpResponseStatusLine;
import com.payneteasy.http.server.impl.response.HttpResponseBuilder;
import com.payneteasy.odterm.metrics.fetch.Metric;
import com.payneteasy.odterm.metrics.fetch.MetricsStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MetricsHandler implements IHttpRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger( MetricsHandler.class );

    private final List<MetricsStore> stores;

    public MetricsHandler(List<MetricsStore> stores) {
        this.stores = stores;
    }

    @Override
    public HttpResponse handleRequest(HttpRequest aRequest) {
        StringBuilder sb = new StringBuilder();
        for (MetricsStore store : stores) {
            for (Metric metric : store.getMetrics()) {
                sb.append("odterm_");
                sb.append(metric.getName());
                sb.append(" ");
                sb.append(metric.getValue());
                sb.append('\n');
            }
        }
        return HttpResponseBuilder.status(HttpResponseStatusLine.OK)
                .addHeader("Content-Type", "text/plain; version=0.0.4; charset=utf-8")
                .body(sb.toString().getBytes(UTF_8))
                .build();
    }
}
