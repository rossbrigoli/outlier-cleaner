package org.hyphen.ross;


import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.hyphen.ross.service.FileSerializer;
import org.hyphen.ross.service.FilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@QuarkusMain
public class Main implements QuarkusApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger("Main");

    @Inject
    FileSerializer serializer;

    @Inject
    FilterService filterService;

    /***
     * Removes outlier data points from a CSV file and writes the cleaned version of the CSV to another file
     * @param args The first argument is the path to the source CSV file.
     *             The optional second argument is the destination file path. If the second argument is not provided it will overwrite the source file.
     * @return
     * @throws Exception
     */
    @Override
    public int run(String... args) throws IOException {

        String inputPath = getInputFilePath(args);
        LOGGER.info("Using source file: {}", inputPath);
        String outputPath = getOutputFilePath(args);

        var inputFile = new File(inputPath);
        var outputFile = new File(outputPath);

        LOGGER.info("Reading input file...");
        var sourceCollection = serializer.deserialize(inputFile);
        LOGGER.info("Applying filter...");
        var filteredCollection = filterService.apply(sourceCollection);
        LOGGER.info("Writing output to destination file {} ...", outputPath);
        serializer.serialize(filteredCollection, outputFile);
        LOGGER.info("Done.");
        return 0;
    }

    private String getInputFilePath(String... args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Invalid arguments. Source file path expected but not provided.");
        }
        var inputPath = args[0];

        if (!Files.exists(Paths.get(inputPath))) {
            throw new IllegalArgumentException(String.format("Source file does not exist: {}", inputPath));
        }
        return inputPath;
    }

    private String getOutputFilePath(String... args) {
        if (args.length < 2) {
            return getInputFilePath(args);
        }
        var outputPath = args[1];

        var outputDirExist = Files.exists(Paths.get(outputPath).getParent());
        if (!outputDirExist) {
            throw new IllegalArgumentException(String.format("Destination directory does not exist: {}", outputPath));
        }
        return outputPath;
    }

}