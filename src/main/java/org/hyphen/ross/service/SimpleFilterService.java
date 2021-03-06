package org.hyphen.ross.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hyphen.ross.model.PriceRecord;
import org.hyphen.ross.processors.FilterPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple filter service that utilizes a single filter predicate.
 *
 */
@ApplicationScoped
public class SimpleFilterService implements FilterService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleFilterService.class);

    /**
     * The predicate function to use in filtering records.
     * To change the filter logic, replace this with another implementation of the Filter abstract class.
     * You can do this by marking the unwanted implementation with @Alternative annotation
     */
    @Inject
    FilterPredicate filterPredicate;

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
     * Applies a filter to the given collection of PriceRecords using a filter predicate.
     * If the predicate function returns false for a given record, that record will be removed from the output collection.
     * @param sourceCollection the List of Price Record to be filtered
     * @return the filtered out version of the original List of PriceRecord
     */
    @Override
    public List<PriceRecord> apply(List<PriceRecord> sourceCollection) {
        LOGGER.info("Applying filter to {} records...", sourceCollection.size());

        filterPredicate.setThreshold(threshold);
        filterPredicate.setDataset(sourceCollection);
        filterPredicate.setRange(range);

        return sourceCollection.stream().filter(filterPredicate).collect(Collectors.toList());
    }

    @Override
    public List<PriceRecord> apply(List<PriceRecord> sourceCollection, int range, double threshold) {
        this.threshold = threshold;
        this.range = range;

        return apply(sourceCollection);
    }
}
