//Romanos Kotsis 4714
import java.io.*;
import java.util.*;

public class STR{

	private static ArrayList<Node> tree = new ArrayList<>();
	private static int treeLevels = 0;
	private static ArrayList<Integer> nodesPerLevel = new ArrayList<>();
	private static ArrayList<Double> MBRareaPerLevel = new ArrayList<>();

	public static ArrayList<Point> readFile(String filename) throws IOException {
        ArrayList<Point> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
        	line = br.readLine();
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                String[] coordinates = line.split(" ");
                double x = Double.parseDouble(coordinates[0]);
                double y = Double.parseDouble(coordinates[1]);
                Point point = new Point(lineNumber, x, y);
                points.add(point);
                lineNumber++;
            }
        }

        return points;
    }

	public static void sortByX(ArrayList<Point> points) {
        Collections.sort(points, (p1, p2) -> Double.compare(p1.getX(), p2.getX())); 
    }

    public static void sortByY(ArrayList<Point> points) {
        Collections.sort(points, (p1, p2) -> Double.compare(p1.getY(), p2.getY()));
    }

    public static ArrayList<ArrayList<Point>> dividePoints(ArrayList<Point> sortedPoints, int r) {
	    ArrayList<ArrayList<Point>> dividedPoints = new ArrayList<>();
	    
	    int totalPoints = sortedPoints.size();
	    int numDivisions;
		if (totalPoints < r) {
		    numDivisions = 1; 
		} else {
		    numDivisions = (int) Math.ceil(Math.sqrt((double) totalPoints / r));
		}
	    int divisionSize = r * numDivisions;

	    int startIndex = 0;
	    int endIndex = 0;

	    for (int i = 0; i < numDivisions; i++) {
	        endIndex = Math.min(startIndex + divisionSize, totalPoints);
	        ArrayList<Point> division = new ArrayList<>(sortedPoints.subList(startIndex, endIndex));
	        sortByY(division);
	        dividedPoints.add(division);
	        startIndex = endIndex;
	    }

	    return dividedPoints;
	}

	public static ArrayList<Node> createLeafNodes(ArrayList<ArrayList<Point>> dividedPoints) {
        ArrayList<Node> leafNodes = new ArrayList<>();
        int nodeIdCount = 0;
        for (ArrayList<Point> stripe : dividedPoints) {
            Node node = new Node();
            int count = 0;

            for (Point point : stripe) {
                double[] record = {(double) point.getLineNumber(), point.getX(), point.getY()};
                node.addRecord(record);
                count++;

                if (count == 51) { 
                	nodeIdCount++;
                	node.setNodeId(nodeIdCount);
                	node.setN(count);
                	node.setF(0);
                    leafNodes.add(node);
                    tree.add(node);
                    node = new Node();
                    count = 0;
                }
            }

            if (count > 0) {
            	nodeIdCount++;
                node.setNodeId(nodeIdCount);
                node.setN(count);
                node.setF(0);
                leafNodes.add(node);
                tree.add(node);
            }
        }

        treeLevels++;
        nodesPerLevel.add(leafNodes.size());
        MBRareaPerLevel.add(0.0);
        return leafNodes;
    }

    public static ArrayList<Node> createFirstInternalNodes(ArrayList<Node> leafNodes) {
	    ArrayList<Node> internalNodes = new ArrayList<>();
	    int nodeIdCount = leafNodes.size() + 1;
	    int chunkSize = 28;
	    int leafNodeIndex = 0;
	    
	    double totalArea = 0;
	    int numberOfMBRs = 0;

	    while (leafNodeIndex < leafNodes.size()) {
	        Node internalNode = new Node();
	        internalNode.setNodeId(nodeIdCount);
	        internalNode.setF(1);

	        int remainingLeafNodes = leafNodes.size() - leafNodeIndex;
	        int nodesToAdd = Math.min(chunkSize, remainingLeafNodes);

	        

	        for (int i = 0; i < nodesToAdd; i++) {
	            Node leafNode = leafNodes.get(leafNodeIndex++);
	            internalNode.setN(internalNode.getN() + 1);

	            double minX = Double.POSITIVE_INFINITY;
				double minY = Double.POSITIVE_INFINITY;
				double maxX = Double.NEGATIVE_INFINITY;
				double maxY = Double.NEGATIVE_INFINITY;

				for (double[] record : leafNode.getRecords()) {
				    double x = record[1];
				    double y = record[2];
				    
				    minX = Math.min(minX, x);
				    minY = Math.min(minY, y);
				    maxX = Math.max(maxX, x);
				    maxY = Math.max(maxY, y);
				}
				double[] record = {leafNode.getNodeId(), minX, minY, maxX, maxY};
				numberOfMBRs++;
				totalArea += (maxX - minX) * (maxY - minY);

	            internalNode.addRecord(record);
	        }

	        

	        internalNodes.add(internalNode);
	        tree.add(internalNode);
	        
	        nodeIdCount++;
	    }
	    
	    double averageArea = totalArea / numberOfMBRs;
	    MBRareaPerLevel.add(averageArea);
	    treeLevels++;
	    nodesPerLevel.add(internalNodes.size());
	    return internalNodes;
	}

	public static Node constructTree(ArrayList<Node> internalNodes) {
	    if (internalNodes.isEmpty()) {
	        return null;
	    }

	    while (internalNodes.size() > 1) {
	        ArrayList<Node> newInternalNodes = new ArrayList<>();
	        int nodeIdCount = tree.size() + 1;

	        double levelTotalArea = 0;
	        int levelNumberOfMBRs = 0;

	        for (int i = 0; i < internalNodes.size(); i += 28) {
	            Node parentNode = new Node();
	            parentNode.setNodeId(nodeIdCount++);
	            parentNode.setF(1);

	            int remainingNodes = internalNodes.size() - i;
	            int nodesToAdd = Math.min(28, remainingNodes);

	            

	            for (int j = i; j < i + nodesToAdd; j++) {
	                Node childNode = internalNodes.get(j);
	                parentNode.setN(parentNode.getN() + 1);

	                double minX = Double.POSITIVE_INFINITY;
	                double minY = Double.POSITIVE_INFINITY;
	                double maxX = Double.NEGATIVE_INFINITY;
	                double maxY = Double.NEGATIVE_INFINITY;

	                for (double[] record : childNode.getRecords()) {
	                    double childMinX = record[1];
	                    double childMinY = record[2];
	                    double childMaxX = record[3];
	                    double childMaxY = record[4];

	                    minX = Math.min(minX, childMinX);
	                    minY = Math.min(minY, childMinY);
	                    maxX = Math.max(maxX, childMaxX);
	                    maxY = Math.max(maxY, childMaxY);
	                }

	                double area = (maxX - minX) * (maxY - minY);
	                levelTotalArea += area;
	                levelNumberOfMBRs++;

	                double[] record = { childNode.getNodeId(), minX, minY, maxX, maxY };
	                parentNode.addRecord(record);
	            }

	            
	            newInternalNodes.add(parentNode);
	            tree.add(parentNode);
	        }

			double levelAverageArea = levelTotalArea / levelNumberOfMBRs;
	        MBRareaPerLevel.add(levelAverageArea);
	        internalNodes = newInternalNodes;
	        treeLevels++;
	        nodesPerLevel.add(internalNodes.size());
	    }

	    return internalNodes.get(0);
	}

	public static void printTreeInfo(){
		System.out.println("Tree height :" + treeLevels);
        for(int i = 0; i < treeLevels; i++){
        	System.out.println("------------------------------");
        	System.out.println("Level : " + i);
        	System.out.println("Number of nodes in level "+i+" :" +nodesPerLevel.get(i));
        	System.out.println("Average MBR area in level " + i+ " :" +MBRareaPerLevel.get(i));
        }
	}

	public static void printTreeToFile(String filename, ArrayList<Node> tree) {
	    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
	    	writer.println(tree.get(tree.size()-1).getNodeId());
	        for (Node node : tree) {
	            writer.print(node.getNodeId() + ", " + node.getN() + ", " + node.getF());

	            for (double[] record : node.getRecords()) {
	                if (record.length == 3) { 
	                    writer.print(", ( " + (int)record[0] + " " + record[1] + " " + record[2] + " )");
	                } else { 
	                    writer.print(", ( " + (int)record[0] + " " + record[1] + " " + record[2] + " " + record[3] + " " + record[4] + " )");
	                }
	            }
	            writer.println();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

    public static void main(String[] args) {
 		String filename = args[0];
        try {
            ArrayList<Point> points = readFile(filename);
            sortByX(points);
            ArrayList<ArrayList<Point>> dividedPoints = dividePoints(points,51);
            ArrayList<Node> leafNodes = createLeafNodes(dividedPoints);
            ArrayList<Node> internalNodes = createFirstInternalNodes(leafNodes);
            Node root = constructTree(internalNodes);
            printTreeInfo();
            printTreeToFile("tree.txt",tree);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}