//Romanos Kotsis 4714
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MergeJoin {

    public static List<String[]> mergeJoin(String fileS, String fileR) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader readerS = new BufferedReader(new FileReader(fileS))) {
            String lineS;
            while ((lineS = readerS.readLine()) != null) {
                String[] rowS = lineS.split(",");
                try (BufferedReader readerR = new BufferedReader(new FileReader(fileR))) {
                    String lineR;
                    while ((lineR = readerR.readLine()) != null) {
                        String[] rowR = lineR.split(",");
                        if (rowR[0].equals(rowS[1])) {
                            String[] mergedRow = new String[5];
                            System.arraycopy(rowR, 0, mergedRow, 0, rowR.length);
                            mergedRow[3] = rowS[0];
                            mergedRow[4] = rowS[2];
                            data.add(mergedRow);
                        }
                    }
                }
            }
        }
        return data;
    }

    public static void saveCsv(List<String[]> data, String csvFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (String[] row : data) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    sb.append(row[i]);
                    if (i < row.length - 1) {
                        sb.append(",");
                    }
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        List<String[]> mergedData = mergeJoin("S.csv", "R.csv");
        saveCsv(mergedData, "O2.csv");
    }
}
