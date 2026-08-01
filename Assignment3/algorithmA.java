import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.*;


public class algorithmA{

	private int gender = 1;
	private double hashMaleMax = -1;
	private double hashFemaleMax = -1;
	private double hashMaleCur;
	private double hashFemaleCur;
	private double T;
	private int validLinesMale = 0;
	private int validLinesFemale = 0;



	public String[] lineReader(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		if (line == null) {
			return null;
		}
		String[] values = line.split(",");
		while ( Integer.parseInt(values[1].trim()) < 18 || (values[8].equals(" Married-civilian spouse present") || values[8].equals(" Married-spouse absent")) ){
			if((line = reader.readLine()) == null)
				return null;
			values = line.split(",");
		}
        String[] selectedValues = new String[5];
        selectedValues[0] = values[0]; //id
        selectedValues[1] = values[1]; //age
        selectedValues[2] = values[13]; //sex
        selectedValues[3] = values[8]; //marriage status
        selectedValues[4] = values[25]; //instance weight
		return selectedValues; 
	}

	public void addPerson(String[] info, HashMap<String, ArrayList<String[]>> hashMap) {
		if(info == null)
			System.exit(0);
        String age = info[1];
        if (hashMap.containsKey(age)) {
            ArrayList<String[]> values = hashMap.get(age);
        	values.add(info);
            hashMap.put(age, values); 
        } 
        else {
        	ArrayList<String[]> values = new ArrayList<>();
        	values.add(info);
        	hashMap.put(age, values);
        }


        if ( hashMap.get(age).get(0)[2].equals(" Male")) {
        	hashMaleCur = Double.parseDouble(hashMap.get(age).get(hashMap.get(age).size()-1)[4]);
        	if( hashMaleCur > hashMaleMax ){
        		hashMaleMax = hashMaleCur;
        	}
        }
        else if ( hashMap.get(age).get(0)[2].equals(" Female")) {
        	hashFemaleCur = Double.parseDouble(hashMap.get(age).get(hashMap.get(age).size()-1)[4]);
        	if( hashFemaleCur > hashFemaleMax ) {
        		hashFemaleMax = hashFemaleCur;
        	}
        }
    }

    public double calculateF(double p1, double p2){
    	double f = 0.5*p1 + 0.5*p2;
    	return f;
    }

    public void updateT(){
    	double t1 = calculateF(hashMaleMax, hashFemaleCur);
        double t2 = calculateF(hashMaleCur, hashFemaleMax);
        T = Math.max(t1, t2);
    }

    public void topCouple(BufferedReader readerMale, BufferedReader readerFemale, HashMap<String, ArrayList<String[]>> hashMale, HashMap<String, ArrayList<String[]>> hashFemale, PriorityQueue<String[]> maxHeap) throws IOException{
    	String[] info = new String[5];
    	ArrayList<String[]> joinResults = new ArrayList<>();
    	if(gender == 1){
    		info = lineReader(readerMale);
    		validLinesMale += 1;
    		addPerson(info,hashMale);
			updateT();
    		if(hashFemale.containsKey(info[1])){
    			ArrayList<String[]> infoFemale = hashFemale.get(info[1]);
    			for(String[] female : infoFemale){
    				String[] joinResult = new String[11];
    				System.arraycopy(info,0,joinResult,1,info.length);
    				System.arraycopy(female,0,joinResult,6,female.length);
    				joinResult[0] = String.valueOf(Double.parseDouble(info[4]) + Double.parseDouble(female[4]));
    				joinResults.add(joinResult);
    			}
    		}
    		gender = 0;
    	}
    	else if(gender == 0){
    		info = lineReader(readerFemale);
    		validLinesFemale += 1;
    		addPerson(info,hashFemale);
			updateT();
    		if(hashMale.containsKey(info[1])){
    			ArrayList<String[]> infoMale = hashMale.get(info[1]);
    			for(String[] male : infoMale){
    				String[] joinResult = new String[11];
    				System.arraycopy(info,0,joinResult,1,info.length);
    				System.arraycopy(male,0,joinResult,6,male.length);
    				joinResult[0] = String.valueOf(Double.parseDouble(info[4]) + Double.parseDouble(male[4]));
    				joinResults.add(joinResult);
    			}
    		}
    		gender = 1;
    	}
    	for(String[] joinResult : joinResults){
    		maxHeap.offer(joinResult);
    	}
    }

    public String[] topK(BufferedReader readerMale, BufferedReader readerFemale, HashMap<String, ArrayList<String[]>> hashMale, HashMap<String, ArrayList<String[]>> hashFemale, PriorityQueue<String[]> maxHeap) throws IOException{
    	while(true){
    		topCouple(readerMale,readerFemale,hashMale,hashFemale,maxHeap);
    		if(maxHeap.size() > 0){
    			double f = calculateF(Double.parseDouble(maxHeap.peek()[5]),Double.parseDouble(maxHeap.peek()[10]));
    			if(f >= T){
    				break;
    			}
    		}
    		
    	}

    	return maxHeap.poll();
    }

    public void printK(int k, BufferedReader readerMale, BufferedReader readerFemale, HashMap<String, ArrayList<String[]>> hashMale, HashMap<String, ArrayList<String[]>> hashFemale, PriorityQueue<String[]> maxHeap) throws IOException{
    	for(int i = 0; i < k; i++){
			String[] result = topK(readerMale,readerFemale,hashMale,hashFemale,maxHeap);
			System.out.println("Top "+ (i+1) + " couple is : "+ result[6]+" , "+result[1]+" score : "+result[0]);
		}
		System.out.println("Valid Lines for Males : "+ validLinesMale);
		System.out.println("Valid lines for Females : "+ validLinesFemale);
    }

	public static void main(String args[]) throws IOException{
		long startTime = System.nanoTime();

		int k = Integer.parseInt(args[0]);

	    BufferedReader readerMale = new BufferedReader(new FileReader("males_sorted"));
	    BufferedReader readerFemale = new BufferedReader(new FileReader("females_sorted"));
		algorithmA a = new algorithmA();
		
		HashMap<String, ArrayList<String[]>> hashMale = new HashMap<>();
		HashMap<String, ArrayList<String[]>> hashFemale = new HashMap<>();
		PriorityQueue<String[]> maxHeap = new PriorityQueue<>(new JoinResultComparator());

		a.printK(k,readerMale,readerFemale,hashMale,hashFemale,maxHeap);

		long endTime = System.nanoTime();
    	long duration = (endTime - startTime) / 1000000;

    	System.out.println("Execution time: " + duration + " ms");
	}
}