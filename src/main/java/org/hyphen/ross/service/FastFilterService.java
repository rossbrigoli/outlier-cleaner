package org.hyphen.ross.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hyphen.ross.model.PriceRecord;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.List;

/**
 * A faster version of SimpleFilterService with O(n) complexity. Use this service for bigger datasets.
 */
@ApplicationScoped
@Alternative
public class FastFilterService implements FilterService{

    /**
     * Used as the allowable distance from the baseline price before a record is considered as outlier.
     */
    @ConfigProperty(name = "filter.threshold")
    Double threshold = 5.0;

    /**
     * Used to determine the size of the time window to lookup for neighbors
     */
    @ConfigProperty(name = "filter.range")
    Integer range = 15;

    /**
     * Applies a filter to the given collection of PriceRecords using a sliding window algorithm.
     * If the predicate function returns false for a given record, that record will be removed from the output collection.
     * @param sourceCollection the List of Price Record to be filtered
     * @return the filtered out version of the original List of PriceRecord
     */
    @Override
    public List<PriceRecord> apply(List<PriceRecord> sourceCollection) {
        //TODO: 1. On first call, create a map of date range window and their corresponding price records
        //TODO: 2. Store this in a static variable so it can be used in the succeeding calls
        //TODO: 3. Use this map to get the min and max price for that window excluding the current records being tested
        //TODO: 4. If the price of the current record is above or below the threshold from the max price and min price of the window, then return false.
        return sourceCollection;
    }

    @Override
    public List<PriceRecord> apply(List<PriceRecord> sourceCollection, int range, double threshold) {
        this.threshold = threshold;
        this.range = range;

        return apply(sourceCollection);
    }
}
