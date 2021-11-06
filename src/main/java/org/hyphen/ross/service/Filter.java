package org.hyphen.ross.service;

import org.hyphen.ross.model.PriceRecord;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.function.Predicate;

/**
 * Abstract base class for all Filter implementations
 */
@ApplicationScoped
public abstract class Filter implements Predicate<PriceRecord> {
    protected Double threshold;
    protected List<PriceRecord> dataset;

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setDataset(List<PriceRecord> dataset) {
        this.dataset = dataset;
    }
}
