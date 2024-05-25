package com.payneteasy.odterm.metrics.fetch;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Collections.emptyList;

public class MetricsStore {

    private AtomicReference<List<Metric>> metricsRef = new AtomicReference<>(emptyList());

    public void updateMetrics(List<Metric> aMetrics) {
        metricsRef.set(aMetrics);
    }

    public List<Metric> getMetrics() {
        return metricsRef.get();
    }



}
