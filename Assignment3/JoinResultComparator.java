import java.util.Comparator;

public class JoinResultComparator implements Comparator<String[]> {
        
        @Override
        public int compare(String[] a, String[] b) {
            double sumA = Double.parseDouble(a[0]);
            double sumB = Double.parseDouble(b[0]);
            return Double.compare(sumB, sumA); 
        }
}