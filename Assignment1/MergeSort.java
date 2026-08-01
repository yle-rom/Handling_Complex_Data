//Romanos Kotsis 4714
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MergeSort {

    public static List<String[]> mergeSort(List<String[]> data, String aggregationFunction) {
        if (data.size() <= 1) {
            return data;
        }

        int mid = data.size() / 2;
        List<String[]> leftArray = new ArrayList<>(data.subList(0, mid));
        List<String[]> rightArray = new ArrayList<>(data.subList(mid, data.size()));

        leftArray = mergeSort(leftArray, aggregationFunction);
        rightArray = mergeSort(rightArray, aggregationFunction);
        return merge(leftArray, rightArray, aggregationFunction);
    }

    public static List<String[]> merge(List<String[]> left, List<String[]> right, String aggregationFunction) {
        List<String[]> merged = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            int x = Integer.parseInt(left.get(i)[0]);
            int y = Integer.parseInt(right.get(j)[0]);

            if (x < y) {
                merged.add(left.get(i));
                i++;
            } else if (x > y) {
                merged.add(right.get(j));
                j++;
            } else {
                int aggValue = 0;
                if (aggregationFunction.equals("sum")) {
                    aggValue = Integer.parseInt(left.get(i)[1]) + Integer.parseInt(right.get(j)[1]);
                } else if (aggregationFunction.equals("min")) {
                    aggValue = Math.min(Integer.parseInt(left.get(i)[1]), Integer.parseInt(right.get(j)[1]));
                } else if (aggregationFunction.equals("max")) {
                    aggValue = Math.max(Integer.parseInt(left.get(i)[1]), Integer.parseInt(right.get(j)[1])); 
                }

                merged.add(new String[]{left.get(i)[0], String.valueOf(aggValue)});
                i++;
                j++;
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }

    public static List<String[]> removeColumns(List<String[]> data, int groupAttribute, int functionAttribute) {
        List<String[]> reducedData = new ArrayList<>();
        for (String[] row : data) {
            String[] reducedRow = new String[2];
            reducedRow[0] = row[groupAttribute];
            reducedRow[1] = row[functionAttribute];
            reducedData.add(reducedRow);
        }
        return reducedData;
    }

    public static List<String[]> readCSV(String file) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        }
        return data;
    }

    public static void saveToCSV(List<String[]> data, String csvFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        String csvFile = args[0];
        int groupAttribute = Integer.parseInt(args[1]);
        int functionAttribute = Integer.parseInt(args[2]);
        String aggregationFunction = args[3];

        List<String[]> csvData = readCSV(csvFile);
        List<String[]> csvDataReduced = removeColumns(csvData, groupAttribute, functionAttribute);
        List<String[]> sortedData = mergeSort(csvDataReduced, aggregationFunction);
        saveToCSV(sortedData, "O1.csv");
    }
}
