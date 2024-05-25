package com.payneteasy.odterm.metrics;

import com.payneteasy.http.server.HttpServer;
import com.payneteasy.http.server.api.handler.IHttpRequestHandler;
import com.payneteasy.odterm.metrics.fetch.ReadMetricsTask;
import com.payneteasy.odterm.metrics.fetch.MetricsStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class OdTermMetricsApplication {

    private static final Logger LOG = LoggerFactory.getLogger( OdTermMetricsApplication.class );

    public static void main(String[] args) throws IOException {

        MetricsStore        temperatureMetrics = new MetricsStore();
        IHttpRequestHandler handler            = new MetricsHandler(List.of(temperatureMetrics));
        ExecutorService     httpExecutor       = newFixedThreadPool(10);
        ExecutorService     metricsExecutor    = newFixedThreadPool(1);

        HttpServer server = new HttpServer(
                new InetSocketAddress(9094)
                , new HttpLoggerSlf4jImpl()
                , httpExecutor
                , handler
                , 10_000
        );

        metricsExecutor.execute(
                new ReadMetricsTask(
                        temperatureMetrics
                        , Duration.ofSeconds(5).toMillis()
                )
        );

        getRuntime().addShutdownHook(new Thread(() -> {
            LOG.warn("Got shutdown signal");

            LOG.warn("Stopping http server...");
            server.stop();

            LOG.warn("Stopping http executor...");
            httpExecutor.shutdown();

            LOG.warn("Stopping metrics executor...");
            metricsExecutor.shutdown();

            LOG.warn("Waiting all threads to exit");
        }));

        server.acceptSocketAndWait();
    }
}
