//Romanos Kotsis 4714
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CompositeQuery {


    public static String[][] compositeQuery(String fileS, String fileR) throws IOException {
        String[][] data = new String[1000][2];
        int index = 0;
        int sum ;
        int flag;
        String[] temp = new String[2];
        BufferedReader readerS = new BufferedReader(new FileReader(fileS));
        BufferedReader readerR = new BufferedReader(new FileReader(fileR));
        String lineS = readerS.readLine();
        while (lineS != null) {
            String[] rowS = lineS.split(",");
            String lineR = readerR.readLine();
            while (lineR != null) {
                String[] rowR = lineR.split(",");
                sum = 0;
                flag = 0;
                temp = new String[2];
                while (rowR[0].equals(rowS[1]) && !rowR[2].equals("7")) {
                    lineS = readerS.readLine();
                    if (lineS == null){
                        break;
                    }
                    rowS = lineS.split(",");
                }

                while (rowR[0].equals(rowS[1]) && rowR[2].equals("7")) {
                    sum += Integer.parseInt(rowS[2]);
                    data[index] = new String[]{rowR[0], String.valueOf(sum)};
                    flag = 1;
                    lineS = readerS.readLine();
                    if (lineS == null){
                        break;
                    }
                    rowS = lineS.split(",");
                }

                if (flag == 1) index = index + 1;
                
                lineR = readerR.readLine();
            }
            readerR.close();
            readerS.close();
        }
    return data;
    }

    public static void saveCSV(String[][] data, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String[] row : data) {
                if (row[0] != null) {
                    writer.write(row[0] + "," + row[1]);
                    writer.newLine();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String[][] mergedData = new String[1000][2]; 
        mergedData = compositeQuery("S.csv", "R.csv");
        saveCSV(mergedData, "O3.csv");
    }
}
