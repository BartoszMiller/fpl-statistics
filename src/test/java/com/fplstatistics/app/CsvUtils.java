package com.fplstatistics.app;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.List;

public class CsvUtils {

    public static List<String[]> readCsvWithoutHeaders(File file) {
        List<String[]> lines;
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            lines = reader.readAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        lines.remove(0);
        return lines;
    }
}
