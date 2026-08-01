//Romanos Kotsis 4714
import java.util.ArrayList;

public class Node {

    private int nodeId;
    private int n;
    private int f;
    private ArrayList<double[]> records;

    public Node(){
        records = new ArrayList<>();
    }

    public void addRecord(double[] record) {
        records.add(record);
    }

    public ArrayList<double[]> getRecords() {
        return records;
    }

    public void setNodeId(int nodeId){
        this.nodeId = nodeId;
    }

    public void setN(int n){
        this.n = n;
    }

    public void setF(int f){
        this.f = f;
    }

    public int getN(){
        return this.n;
    }

    public int getNodeId(){
        return this.nodeId;
    }

    public int getF(){
        return this.f;
    }
}