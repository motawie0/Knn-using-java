import data.DataReader;
import data.Image;
import java.util.*;
import Knn.*;

public class Main {
    public static void main(String[] args) {

        Test test = new Test(3);
        List<Image> imagesTest = new DataReader().readData("data/mnist_test.csv");

        Image digit = imagesTest.get(56);
        long start1 = System.currentTimeMillis();
        System.out.println("Prediction for " + digit.getLabel() + " is " +  test.recognizeImages(digit));
        long end = System.currentTimeMillis();
        System.out.println((end-start1)*Math.pow(10, -3));
    }

}