package org.hyphen.ross;

import org.hyphen.ross.model.PriceRecord;
import org.hyphen.ross.processors.Filter;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
@Priority(1)
public class MockAllFilterPredicate extends Filter {
    @Override
    public boolean test(PriceRecord priceRecord) {
        return true;
    }
}
