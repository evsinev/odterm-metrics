package com.payneteasy.odterm.metrics.fetch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

import static java.lang.Thread.currentThread;

public class ReadMetricsTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ReadMetricsTask.class);

    private final MetricsStore metricsStore;
    private final long         sleepMs;

    public ReadMetricsTask(MetricsStore metricsStore, long sleepMs) {
        this.metricsStore = metricsStore;
        this.sleepMs      = sleepMs;
    }

    @Override
    public void run() {
        while (!currentThread().isInterrupted()) {
            try {
                readMetrics();
            } catch (Exception e) {
                LOG.error("Error while fetching metrics", e);
            }

            try {
                LOG.debug("Sleeping {}ms ...\n", sleepMs);
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
                LOG.error("Interrupted sleep. Exited.", e);
                return;
            }
        }
    }

    private void readMetrics() throws IOException {
        LOG.info("Opening file ...");
        try (LineNumberReader in = new LineNumberReader(new FileReader(new File("/dev/ttyACM0")))) {
            String line;
            LOG.info("Reading first line ...");
            while ((line = in.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                LOG.info("Line is '{}'", line);
                String tempText = line.replace("~G", "");
                double temp = Double.parseDouble(tempText);
                metricsStore.updateMetrics(List.of(new Metric("temp", temp)));
            }
        }
        LOG.info("End of file");
    }

}
