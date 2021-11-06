package org.hyphen.ross.processors;

import org.hyphen.ross.model.PriceRecord;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.function.Predicate;

/**
 * Abstract base class for all Filter implementations
 */
@ApplicationScoped
public abstract class FilterPredicate implements Predicate<PriceRecord> {
    protected Double threshold;
    protected List<PriceRecord> dataset;
    protected Integer neighborDaysRange;

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setDataset(List<PriceRecord> dataset) {
        this.dataset = dataset;
    }

    public void setNeighborDaysRange(Integer neighborDaysRange) {
        this.neighborDaysRange = neighborDaysRange;
    }
}
