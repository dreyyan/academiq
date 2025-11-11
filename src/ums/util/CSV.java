package ums.util;

// [IMPORT] Standard
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Project Files
import static ums.util.Logger.errorMessage;

public class CSV {
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

    // [UTILITY] Write data to a CSV file
    public static void writeCSV(String filePath, List<String[]> rows, String delimiter) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : rows) {
                // Join each row using the delimiter
                String line = String.join(delimiter, row);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            errorMessage("Failed to write CSV: " + e.getMessage());
        }
    }

    // [UTILITY] Write data to a CSV file with comma(,) as delimiter
    public static void writeCSV(String filePath, List<String[]> rows) {
        writeCSV(filePath, rows, ",");
    }

    // [UTILITY] Get a specific column value
    public static String getColumnValue(String filePath, int keyColumnIndex, String keyValue, int targetColumnIndex) {
        List<String[]> rows = CSV.readCSV(filePath);
        for (String[] row : rows) {
            if (row.length > Math.max(keyColumnIndex, targetColumnIndex) &&
                row[keyColumnIndex].equalsIgnoreCase(keyValue)) {
                return row[targetColumnIndex];
            }
        }
        return null;
    }
}
