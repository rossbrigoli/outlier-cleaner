package org.hyphen.ross.processors;

import org.hyphen.ross.model.PriceRecord;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Collectors;

/***
 * A basic price outlier detector using sliding window average.
 * The prices that are within the given range belong to the same window. The average price of the window is used as the baseline.
 * If the price of a data point is above or below the given threshold from the baseline, then the datapoint is considered as outlier.
 *
 * Complexity is O(n^2)
 */
@ApplicationScoped
public class WindowOutlierFilter extends FilterPredicate {
    /***
     * The main filter predicate function invoked by a Stream filter invocation.
     *
     * A price record is considered an outlier if and only if the price is above or below the threshold from the calculated baseline.
     * The baseline is calculated as the average price of all price record in the window.
     *
     * For Example: If the average price of the window is 100 and the threshold is 5, then all the price records of the same month
     * with a prices greater than 105 and less than 95 are considered outliers.
     *
     * @param priceRecord the record to be tested
     * @return Returns "true" if the given record is NOT an outlier. Otherwise, false for an outlier record.
     */
    @Override
    public boolean test(PriceRecord priceRecord) {
        var monthAverage = dataset.stream()
                //Only include records that are within the range
                .filter(item -> windowFilter(priceRecord, item))
                //Exclude the record that is being tested
                .filter(item -> !item.equals(priceRecord))
                .collect(Collectors.averagingDouble(PriceRecord::getPrice));

        var isTypicalPrice = Math.abs(monthAverage - priceRecord.getPrice()) < threshold;

        if (!isTypicalPrice) {
            LOGGER.info("Outlier detected: {}, {}", priceRecord.getDate().toString("dd/MM/yyyy"), priceRecord.getPrice());
        }

        return isTypicalPrice;
    }

    private boolean windowFilter(PriceRecord referenceRecord, PriceRecord testedRecord) {
        //if range is zero, consider all records as neighbors
        if (range.equals(0)) {
            return true;
        }

        var referenceDate = referenceRecord.getDate();
        return testedRecord.getDate().isBefore(referenceDate.plusDays(range)) &&
                testedRecord.getDate().isAfter(referenceDate.minusDays(range));
    }
}
