package org.hyphen.ross.service;

import org.hyphen.ross.model.PriceRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * A basic bespoke CSV file serializer/deserializer
 */
@ApplicationScoped
public class CsvSerializerService implements FileSerializerService {
    private final static String[] HEADERS = new String[]{"Date", "Price"};
    private final static String DATE_STRING_PATTERN = "dd/MM/yyyy";

    /**
     * Deserializes a given CSV file into a List of PriceRecords
     * @param sourceFile Path of the CSV file to deserialize
     * @return List of PriceRecords
     * @throws IOException
     */
    @Override
    public List<PriceRecord> deserialize(File sourceFile) throws IOException {
        var fileReader = new FileReader(sourceFile);
        var dataset = new LinkedList<PriceRecord>();

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .parse(fileReader);

        records.forEach(record -> {
            var dateTimeFormatter = DateTimeFormat.forPattern(DATE_STRING_PATTERN);
            LocalDate recordDate = dateTimeFormatter.parseLocalDate(record.get("Date"));
            Double recordPrice = Double.parseDouble(record.get("Price"));
            dataset.addLast(new PriceRecord(recordDate, recordPrice));
        });

        return dataset;
    }

    /**
     * Serializes a given List of PriceRecords into a CSV file and writes it out to the file system
     * @param priceRecords The List of PriceRecords to serialize
     * @param destinationFile The path of the CSV file to be created
     * @throws IOException
     */
    @Override
    public void serialize(List<PriceRecord> priceRecords, File destinationFile) throws IOException {
        var fileWriter = new FileWriter(destinationFile);
        try(var csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader(HEADERS))) {
            for (PriceRecord record : priceRecords) {
                csvPrinter.printRecord(record.getDate().toString(DATE_STRING_PATTERN), record.getPrice());
            }
        }
    }
}
