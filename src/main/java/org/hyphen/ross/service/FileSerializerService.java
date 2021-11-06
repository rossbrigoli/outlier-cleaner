package org.hyphen.ross.service;

import org.hyphen.ross.model.PriceRecord;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interface for serialization/deserialization of PriceRecord to and from a CSV file
 */
public interface FileSerializerService {
    List<PriceRecord> deserialize(File sourceFile) throws IOException;
    void serialize(List<PriceRecord> priceRecords, File destinationFile) throws IOException;
}
