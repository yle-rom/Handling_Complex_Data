import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.*;


public class algorithmB{
	
	public ArrayList<String[]> readMales() throws IOException {
        ArrayList<String[]> validLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("males_sorted"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (isValid(values)) {
                    String[] selectedValues = new String[5];
                    selectedValues[0] = values[0]; // id
                    selectedValues[1] = values[1]; // age
                    selectedValues[2] = values[13]; // sex
                    selectedValues[3] = values[8]; // marriage status
                    selectedValues[4] = values[25]; // instance weight
                    validLines.add(selectedValues);
                }
            }
        }
        return validLines;
    }

    private boolean isValid(String[] values) {
        if (Integer.parseInt(values[1].trim()) < 18 || values[8].equals(" Married-civilian spouse present") || values[8].equals(" Married-spouse absent")) { 
            return false;
        }
        return true;
    }

    public HashMap<String, ArrayList<String[]>> createHashMale() throws IOException {
        HashMap<String, ArrayList<String[]>> hashMale = new HashMap<>();
        ArrayList<String[]> validLines = readMales(); 

        for (String[] line : validLines) {
            String age = line[1]; 
            if (hashMale.containsKey(age)) {
                hashMale.get(age).add(line);
            } else { 
                ArrayList<String[]> newEntry = new ArrayList<>();
                newEntry.add(line);
                hashMale.put(age, newEntry);
            }
        }
        return hashMale;
    }

    public PriorityQueue<String[]> findTopK(HashMap<String, ArrayList<String[]>> hashMale) throws IOException{
        PriorityQueue<String[]> maxHeap = new PriorityQueue<>(new JoinResultComparator());
        BufferedReader reader = new BufferedReader(new FileReader("females_sorted"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            if (isValid(values)) {
                String[] selectedValues = new String[5];
                selectedValues[0] = values[0]; // id
                selectedValues[1] = values[1]; // age
                selectedValues[2] = values[13]; // sex
                selectedValues[3] = values[8]; // marriage status
                selectedValues[4] = values[25]; // instance weight
                if(hashMale.containsKey(selectedValues[1])){
                    ArrayList<String[]> males = hashMale.get(selectedValues[1]);
                    for(String[] male : males){
                        String[] join = new String[11];
                        System.arraycopy(selectedValues,0,join,1,selectedValues.length);
                        System.arraycopy(male,0,join,6,male.length);
                        join[0] = String.valueOf(Double.parseDouble(selectedValues[4]) + Double.parseDouble(male[4]));
                        maxHeap.offer(join);
                    }
                }
            }
        }
        return maxHeap;
    }

    public void printK(int k) throws IOException{
        HashMap<String, ArrayList<String[]>> hashMale = createHashMale();
        PriorityQueue<String[]> maxHeap = findTopK(hashMale);

        for(int i=0; i < k; i++){
            String[] couple = maxHeap.poll();
            System.out.println("Top "+ (i+1) + " couple is : "+ couple[6]+" , "+couple[1]+" score : "+couple[0]);
        }
    }

	public static void main(String[] args) throws IOException{
        long startTime = System.nanoTime();

        int k = Integer.parseInt(args[0]);
		algorithmB b = new algorithmB();

        b.printK(k);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        System.out.println("Execution time: " + duration + " ms");
	}
}