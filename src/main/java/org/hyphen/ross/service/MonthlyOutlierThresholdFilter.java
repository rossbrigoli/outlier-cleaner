package org.hyphen.ross.service;

import org.hyphen.ross.model.PriceRecord;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Collectors;

/***
 * A basic price outlier detector using fix time range of a Month and a threshold.
 * The prices that belong to the same month are considered as neighbors. The avergae price of the neighbors is used as the baseline.
 * If the price of a data point is farther away from the baseline than the given threshold, then the datapoint is considered as outlier.
 */
@ApplicationScoped
public class MonthlyOutlierThresholdFilter extends Filter {

    /***
     * The main filter predicate function invoked by a Stream filter invocation.
     *
     * A price record is considered an outlier if and only if the price is farther away from the baseline beyond the given threshold.
     * The baseline is calculated as the average price of all price record for of the same Month.
     *
     * For Example: If the average price of the month is 100 and the threshold is 5, then all the price records of the same month
     * with a prices greater than 105 and less than 95 are considered outliers.
     *
     * @param priceRecord
     * @return Returns "true" if the given record is NOT an outlier. Otherwise, false for an outlier record.
     */
    @Override
    public boolean test(PriceRecord priceRecord) {
        var monthAverage = dataset.stream()
                //Only include record from the same year and month
                .filter(c -> c.getDate().getYear() == priceRecord.getDate().getYear() && c.getDate().getMonthOfYear() == priceRecord.getDate().getMonthOfYear())
                //Exclude the record that is being tested
                .filter(c -> !c.equals(priceRecord))
                .collect(Collectors.averagingDouble(PriceRecord::getPrice));

        return Math.abs(monthAverage - priceRecord.getPrice()) > threshold;
    }
}
