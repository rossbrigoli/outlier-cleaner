package org.hyphen.ross.processors;

import org.hyphen.ross.model.PriceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.function.Predicate;

/**
 * Abstract base class for all Filter implementations
 */
@ApplicationScoped
public abstract class FilterPredicate implements Predicate<PriceRecord> {
    final static Logger LOGGER = LoggerFactory.getLogger(FilterPredicate.class);

    /**
     * The allowable threshold before a record is considered as an outlier
     */
    protected Double threshold;

    /**
     * Holds the original collection to be used as reference for outlier detection
     */
    protected List<PriceRecord> dataset;

    /**
     * Depending on the filter implementation this is used to determine the size of the time window
     * or the range for looking back or looking ahead of time
     */
    protected Integer range;

    /**
     * The allowable threshold before a record is considered as an outlier
     */
    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    /**
     * Holds the original collection to be used as reference for outlier detection
     */
    public void setDataset(List<PriceRecord> dataset) {
        this.dataset = dataset;
    }

    /**
     * Depending on the filter implementation this is used to determine the size of the time window
     * or the range for looking back or looking ahead of time
     */
    public void setRange(Integer range) {
        this.range = range;
    }
}
