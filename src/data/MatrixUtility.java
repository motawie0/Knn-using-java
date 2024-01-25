package data;
public class MatrixUtility {
    public static double[][] MatrixDifference(double[][] a, double[][] b){
        double[][] out = new double[a.length][a[0].length];
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a[0].length; j++){
                out[i][j] = (a[i][j] - b[i][j]);
            }
        }
        return out;
    }

    /* L1 Distance */
    public static double ManhattanDistance(double[][] a){
        double total = 0;
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a[0].length; j++){
                total += Math.abs(a[i][j]);
            }
        }
        return total;
    }

    /* L2 Distance */
    public static double EuclideanDistance(double[][] a){
        double total = 0;
        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a[0].length; j++){
                total += Math.pow(a[i][j],2);
            }
        }
        return Math.sqrt(total);
    }
}
