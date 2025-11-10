package ums.util;

// [IMPORT] Standard
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Project Files
import static ums.util.Logger.errorMessage;

public class CSVReader {
    // [UTILITY] Read data from a CSV file
    public static List<String[]> readCSV(String filePath, String delimiter) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Split line into columns using the delimiter
                String[] columns = line.split(delimiter);
                rows.add(columns);
            }

        } catch (IOException e) {
            errorMessage("Failed to read CSV: " + e.getMessage());
        }

        return rows;
    }

    // [UTILITY] Read data from a CSV file with comma(,) as delimiter
    public static List<String[]> readCSV(String filePath) {
        return readCSV(filePath, ",");
    }
}
