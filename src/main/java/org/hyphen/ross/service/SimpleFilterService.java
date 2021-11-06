package org.hyphen.ross.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hyphen.ross.model.PriceRecord;
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
     */
    @Inject
    Filter filterPredicate;

    /**
     * Used as the allowable distance from the baseline price before a record is considered as outlier.
     */
    @ConfigProperty(name = "filter.threshold")
    Double filterThreshold;

    /**
     * Applies a filter to the given collection of PriceRecords using a filter predicate.
     * If the predicate function returns false for a given record, that record will be removed from the output collection.
     * @param sourceCollection the List of Price Record to be filtered
     * @return the filtered out version of the original List of PriceRecord
     */
    public List<PriceRecord> apply(List<PriceRecord> sourceCollection) {
        LOGGER.info("Applying filter to {} records...", sourceCollection.size());

        filterPredicate.setThreshold(filterThreshold);
        filterPredicate.setDataset(sourceCollection);
        return sourceCollection.stream().filter(filterPredicate).collect(Collectors.toList());
    }
}
