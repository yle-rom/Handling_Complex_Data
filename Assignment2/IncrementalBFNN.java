//Romanos Kotsis 4714
import java.util.PriorityQueue;
import java.io.*;
import java.util.*;


public class IncrementalBFNN {
    private static PriorityQueue<double[]> minHeap;

    public static ArrayList<Node> readTreeFromFile(String filename) throws IOException{
        ArrayList<Node> treeFromFile = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                int nodeId = Integer.parseInt(parts[0]);
                int n = Integer.parseInt(parts[1]);
                int f = Integer.parseInt(parts[2]);

                Node node = new Node();
                node.setNodeId(nodeId);
                node.setN(n);
                node.setF(f);

                for (int i = 3; i < parts.length; i++) {
                    String[] recordParts = parts[i].split(" ");
                    int id = Integer.parseInt(recordParts[1]);
                    double x = Double.parseDouble(recordParts[2]);
                    double y = Double.parseDouble(recordParts[3]);

                    if (recordParts.length == 5) {
                        double[] record = { id, x, y };
                        node.addRecord(record);
                    } else if (recordParts.length == 7) {
                        double maxX = Double.parseDouble(recordParts[4]);
                        double maxY = Double.parseDouble(recordParts[5]);
                        double[] record = { id, x, y, maxX, maxY };
                        node.addRecord(record);
                    }
                }

                treeFromFile.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return treeFromFile;
    }

    private static double mindist(double qx, double qy, double minX, double minY, double maxX, double maxY) {
        double dx = Math.max(Math.max(minX - qx, 0), qx - maxX);
        double dy = Math.max(Math.max(minY - qy, 0), qy - maxY);
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static ArrayList<double[]> BNFF(ArrayList<Node> tree, double queryX, double queryY, int k){
        minHeap = new PriorityQueue<>((a, b) -> Double.compare(a[0], b[0]));        
        Node rootNode = tree.get(tree.size() - 1); 
        for (double[] entry : rootNode.getRecords()) {
            double distance = mindist(queryX, queryY, entry[1], entry[2], entry[3], entry[4]);
            minHeap.offer(new double[] { distance, entry[0], entry[1], entry[2], entry[3], entry[4] });
        }

        ArrayList<double[]> nearestNeighbors = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            double[] entry = minHeap.poll();

            if (entry.length == 4) { 
                nearestNeighbors.add(entry);
                System.out.println("Content of minHeap after adding neighbor " + nearestNeighbors.size() + " :");
                PriorityQueue<double[]> minHeapCopy = new PriorityQueue<>(minHeap);
                while (!minHeapCopy.isEmpty()) {
                    double[] element = minHeapCopy.poll();
                    System.out.println(Arrays.toString(element));
                }
                if (nearestNeighbors.size() == k) {
                    break; 
                }
            } else { 
                int nodeId = (int) entry[1];
                Node node = null;
                for (Node n : tree) {
                    if (n.getNodeId() == nodeId) {
                        node = n;
                        break;
                    }
                }
                for (double[] childEntry : node.getRecords()) {
                    double distance;
                    if (childEntry.length == 3) {
                        distance = Math.sqrt(Math.pow(queryX - childEntry[1], 2) + Math.pow(queryY - childEntry[2], 2));
                        minHeap.offer(new double[] { distance, childEntry[0], childEntry[1], childEntry[2] });
                    } else { 
                        distance = mindist(queryX, queryY, childEntry[1], childEntry[2], childEntry[3], childEntry[4]);
                        minHeap.offer(new double[] { distance, childEntry[0], childEntry[1], childEntry[2], childEntry[3], childEntry[4] });
                    }
                }
            }
        }

        return nearestNeighbors;
    }

    private static ArrayList<double[]> findKPlusOneNeighbor(ArrayList<Node> tree, double queryX, double queryY, int k) {
        ArrayList<double[]> nearestNeighbors = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            double[] entry = minHeap.poll();

            if (entry.length == 4) { 
                nearestNeighbors.add(entry);
                System.out.println("Content of minHeap after adding neighbor " + (k + 1) + " :");
                PriorityQueue<double[]> minHeapCopy = new PriorityQueue<>(minHeap);
                while (!minHeapCopy.isEmpty()) {
                    double[] element = minHeapCopy.poll();
                    System.out.println(Arrays.toString(element));
                }
                if (nearestNeighbors.size() == 1) { 
                    break; 
                }
            } else { 
                int nodeId = (int) entry[1];
                Node node = null;
                for (Node n : tree) {
                    if (n.getNodeId() == nodeId) {
                        node = n;
                        break;
                    }
                }
                for (double[] childEntry : node.getRecords()) {
                    double distance;
                    if (childEntry.length == 3) {
                        distance = Math.sqrt(Math.pow(queryX - childEntry[1], 2) + Math.pow(queryY - childEntry[2], 2));
                        minHeap.offer(new double[] { distance, childEntry[0], childEntry[1], childEntry[2] });
                    } else { 
                        distance = mindist(queryX, queryY, childEntry[1], childEntry[2], childEntry[3], childEntry[4]);
                        minHeap.offer(new double[] { distance, childEntry[0], childEntry[1], childEntry[2], childEntry[3], childEntry[4] });
                    }
                }
            }
        }

        return nearestNeighbors;
    }

    private static void printNearestNeighbors(ArrayList<double[]> nearestNeighbors) {
        System.out.println("Nearest Neighbors:");
        for (double[] neighbor : nearestNeighbors) {
            System.out.println("( ID : " + neighbor[1] + ", Distance : " + neighbor[0]  + ", x coordinate : " + neighbor[2] +  ", y coordinate : " + neighbor[3] + ")");
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        double queryX = Double.parseDouble(args[1]);
        double queryY = Double.parseDouble(args[2]);
        int k = Integer.parseInt(args[3]);

        try {   
            ArrayList<Node> tree = readTreeFromFile(filename);
            ArrayList<double[]> nearestNeighbors = BNFF(tree,queryX,queryY,k);
            ArrayList<double[]> kPlusOneNeighbor = findKPlusOneNeighbor(tree,queryX,queryY,k);
            ArrayList<double[]> kPlusTwoNeighbor = findKPlusOneNeighbor(tree,queryX,queryY,k+1);
            nearestNeighbors.addAll(kPlusOneNeighbor);
            nearestNeighbors.addAll(kPlusTwoNeighbor);
            printNearestNeighbors(nearestNeighbors);
        } catch (IOException e) { 
            e.printStackTrace();
        }
    }
}
