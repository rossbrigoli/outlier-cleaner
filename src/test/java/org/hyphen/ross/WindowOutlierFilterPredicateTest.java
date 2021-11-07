package org.hyphen.ross;

import io.quarkus.test.junit.QuarkusTest;
import org.hyphen.ross.model.PriceRecord;
import org.hyphen.ross.processors.WindowOutlierFilter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

@QuarkusTest
public class WindowOutlierFilterPredicateTest {

    WindowOutlierFilter filterPredicate = new WindowOutlierFilter();

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    private final static LinkedList<PriceRecord> sourceRecords = new LinkedList<>();

    @BeforeAll
    public static void setup() {
        sourceRecords.addLast(new PriceRecord(DateTime.parse("01/01/2020", dateTimeFormatter).toLocalDate(), 101.00));
        sourceRecords.addLast(new PriceRecord(DateTime.parse("02/01/2020", dateTimeFormatter).toLocalDate(), 102.00));
        sourceRecords.addLast(new PriceRecord(DateTime.parse("03/01/2020", dateTimeFormatter).toLocalDate(), 103.00));
    }

    @Test
    @DisplayName("When the price is far above the upper threshold, then it should be filtered out")
    public void testOutlierFarOutsideThreshold() {
        var priceRecord = new PriceRecord(DateTime.parse("04/01/2020", dateTimeFormatter).toLocalDate(), 200.00);
        filterPredicate.setDataset(sourceRecords);
        filterPredicate.setThreshold(50.0);
        filterPredicate.setRange(2);
        var actual = filterPredicate.test(priceRecord);
        //False is outlier, True is not
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("When the price is exactly at the upper threshold, then it should not be filtered out")
    public void testOutlierExactlyAtThreshold() {
        var priceRecord = new PriceRecord(DateTime.parse("04/01/2020", dateTimeFormatter).toLocalDate(), 151.00);
        filterPredicate.setDataset(sourceRecords);
        filterPredicate.setThreshold(50.0);
        filterPredicate.setRange(2);
        var actual = filterPredicate.test(priceRecord);

        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("When the price is a whole number above the upper threshold, then it should be filtered out")
    public void testOutlierJustOutsideTheThreshold() {
        var priceRecord = new PriceRecord(DateTime.parse("04/01/2020", dateTimeFormatter).toLocalDate(), 153.00);
        filterPredicate.setDataset(sourceRecords);
        filterPredicate.setThreshold(50.0);
        filterPredicate.setRange(2);
        var actual = filterPredicate.test(priceRecord);

        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("When the price is a few decimals below the upper threshold, then it should not be filtered out")
    public void testOutlierThresholdDecimalsAbove() {
        var priceRecord = new PriceRecord(DateTime.parse("04/01/2020", dateTimeFormatter).toLocalDate(), 151.999999999);
        filterPredicate.setDataset(sourceRecords);
        filterPredicate.setThreshold(50.0);
        filterPredicate.setRange(2);
        var actual = filterPredicate.test(priceRecord);

        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("When the price is far below the bottom threshold, then it should be filtered out")
    public void testOutlierFarBelowThreshold() {
        var priceRecord = new PriceRecord(DateTime.parse("04/01/2020", dateTimeFormatter).toLocalDate(), 10.1);
        filterPredicate.setDataset(sourceRecords);
        filterPredicate.setThreshold(50.0);
        filterPredicate.setRange(2);
        var actual = filterPredicate.test(priceRecord);
        //False is outlier, True is not
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("When the price is exactly at the bottom threshold, then it should be filtered out")
    public void testOutlierExactlyAtBottomThreshold() {
        var priceRecord = new PriceRecord(DateTime.parse("04/01/2020", dateTimeFormatter).toLocalDate(), 52.0);
        filterPredicate.setDataset(sourceRecords);
        filterPredicate.setThreshold(50.0);
        filterPredicate.setRange(2);
        var actual = filterPredicate.test(priceRecord);

        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("When the price is a whole number below the bottom threshold, then it should be filtered out")
    public void testOutlierJustBelowTheThreshold() {
        var priceRecord = new PriceRecord(DateTime.parse("04/01/2020", dateTimeFormatter).toLocalDate(), 51.00);
        filterPredicate.setDataset(sourceRecords);
        filterPredicate.setThreshold(50.0);
        filterPredicate.setRange(2);
        var actual = filterPredicate.test(priceRecord);

        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("When the price is a few decimals above the bottom threshold, then it should not be filtered out")
    public void testOutlierThresholdDecimalsBelow() {
        var priceRecord = new PriceRecord(DateTime.parse("04/01/2020", dateTimeFormatter).toLocalDate(), 151.000001);
        filterPredicate.setDataset(sourceRecords);
        filterPredicate.setThreshold(50.0);
        filterPredicate.setRange(2);
        var actual = filterPredicate.test(priceRecord);

        Assertions.assertTrue(actual);
    }
}
