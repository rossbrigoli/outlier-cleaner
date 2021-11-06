package org.hyphen.ross.service;

import org.hyphen.ross.model.PriceRecord;

import java.util.List;

/**
 * Interface for Filter service implementations. Implement this interface to create a new filter service.
 */
public interface FilterService {
    List<PriceRecord> apply(List<PriceRecord> sourceCollection);
}
