package org.hyphen.ross.processors;

import org.hyphen.ross.model.PriceRecord;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.stream.Collectors;

/***
 * A basic price outlier detector using sliding window average.
 * The prices that are within the given range of days are neighbors. The average price of the neighbors is used as the baseline.
 * If the price of a data point is farther away from the baseline than the given threshold, then the datapoint is considered as outlier.
 */
@ApplicationScoped
@Alternative
public class DayRangeOutlierFilter extends FilterPredicate {

    /***
     * The main filter predicate function invoked by a Stream filter invocation.
     *
     * A price record is considered an outlier if and only if the price is farther away from the baseline beyond the given threshold.
     * The baseline is calculated as the average price of all price record for of the same Month.
     *
     * For Example: If the average price of the month is 100 and the threshold is 5, then all the price records of the same month
     * with a prices greater than 105 and less than 95 are considered outliers.
     *
     * @param priceRecord the record to be tested
     * @return Returns "true" if the given record is NOT an outlier. Otherwise, false for an outlier record.
     */
    @Override
    public boolean test(PriceRecord priceRecord) {
        var monthAverage = dataset.stream()
                //Only include records that are within the range
                .filter(item -> neighborFilter(priceRecord, item))
                //Exclude the record that is being tested
                .filter(item -> !item.equals(priceRecord))
                .collect(Collectors.averagingDouble(PriceRecord::getPrice));

        return Math.abs(monthAverage - priceRecord.getPrice()) > threshold;
    }

    private boolean neighborFilter(PriceRecord referenceRecord, PriceRecord testedRecord) {
        var referenceDate = referenceRecord.getDate();
        return testedRecord.getDate().isBefore(referenceDate.plusDays(neighborDaysRange)) &&
                testedRecord.getDate().isAfter(referenceDate.minusDays(neighborDaysRange));
    }
}
