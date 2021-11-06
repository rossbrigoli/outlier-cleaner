package org.hyphen.ross;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.hyphen.ross.model.PriceRecord;
import org.hyphen.ross.processors.FilterPredicate;
import org.hyphen.ross.processors.MonthlyOutlierFilter;
import org.hyphen.ross.service.SimpleFilterService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import javax.inject.Inject;
import java.util.LinkedList;

@QuarkusTest
public class SimpleOutlierFilterServiceTest {

    @Inject
    SimpleFilterService filterService;

    MonthlyOutlierFilter mockFilterPredicate = Mockito.mock(MonthlyOutlierFilter.class);
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    private final static LinkedList<PriceRecord> sourceRecords = new LinkedList<>();

    @BeforeAll
    public static void setup() {
        sourceRecords.addLast(new PriceRecord(DateTime.parse("01/01/2020", dateTimeFormatter).toLocalDate(), 100.00));
        sourceRecords.addLast(new PriceRecord(DateTime.parse("01/01/2020", dateTimeFormatter).toLocalDate(), 102.00));
        sourceRecords.addLast(new PriceRecord(DateTime.parse("01/01/2020", dateTimeFormatter).toLocalDate(), 103.00));
    }

    @Test
    @DisplayName("When the filter predicate always returns TRUE, then nothing should be filtered out")
    public void testFilterServiceAlwaysTruePredicate() {
        Mockito.when(mockFilterPredicate.test(Mockito.any())).thenReturn(true);
        QuarkusMock.installMockForType(mockFilterPredicate, FilterPredicate.class);
        var actual = filterService.apply(sourceRecords);
        //Because the mock filter always returns true all records should be returned
        Assertions.assertEquals(sourceRecords, actual);
    }

    @Test
    @DisplayName("When the filter predicate always returns FALSE, then all records should be filtered out")
    public void testFilterServiceAlwaysFalsePredicate() {
        Mockito.when(mockFilterPredicate.test(Mockito.any())).thenReturn(false);
        QuarkusMock.installMockForType(mockFilterPredicate, FilterPredicate.class);
        var actual = filterService.apply(sourceRecords);
        //Because the mock filter always returns false all records should filtered out and should return an empty list
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("When the filter predicate conditionally returns false, then some records should be filtered out")
    public void testFilterServiceSelectivePredicate() {

        //Mock the filter predicate to filter our record with price == 103.00
        Mockito.when(mockFilterPredicate.test(Mockito.any(PriceRecord.class))).thenAnswer((Answer<Boolean>) invocationOnMock -> {
            return !invocationOnMock.getArgument(0,PriceRecord.class).getPrice().equals(103.00);
        });

        QuarkusMock.installMockForType(mockFilterPredicate, FilterPredicate.class);
        var actual = filterService.apply(sourceRecords);

        //Because the mock filter revomed records with price 103.00, there should only be 2 records left
        Assertions.assertEquals(2, actual.size());
    }
}