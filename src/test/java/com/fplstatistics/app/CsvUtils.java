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

    public enum CSV_INDEX {
        ASSISTS(1, 1),
        BONUS(5, 2),
        CLEAN_SHEETS(7, 4),
        CREATIVITY(10, 5),
        GOALS(19, 9),
        ICT(20, 10),
        INFLUENCE(22, 11),
        KICK_OFF(24, 12),
        MINUTES(28, 13),
        POINTS(47, 25),
        ROUND(38, 19),
        THREAT(46, 24),
        WAS_HOME(-1, 30),
        NAME(0, 0);

        private final int before2019;
        private final int from2019;

        CSV_INDEX(int before2019, int from2019) {
            this.before2019 = before2019;
            this.from2019 = from2019;
        }

        public int getIndexBySeason(String seasonCode) {
            if (seasonCode.equals("2019-20")) {
                return from2019;
            } else {
                return before2019;
            }
        }
    }
}
