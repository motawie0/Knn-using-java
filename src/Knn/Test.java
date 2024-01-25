package Knn;

import data.DataReader;
import data.Image;
import data.MatrixUtility;

import me.tongfei.progressbar.*;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
public class Test {
    List<Image> imagesTest;
    List<Image> imagesTrain;
    int K;
    double split = 1;
    public Test(int neighbors){
        this.imagesTest = (new DataReader().readData("data/mnist_test.csv"));
        this.imagesTrain = new DataReader().readData("data/mnist_train.csv");
        this.K=neighbors;
     }
    private static class Neighbour {
        int label;
        double distance;

        public Neighbour(int label, double distance) {
            this.label = label;
            this.distance = distance;
        }
        public double getDistance() {
            return distance;
        }

        public int getLabel() {
            return label;
        }
    }
    /*
        Returns a list of the k nearest neighbours for each testImage using the Manhattan Distance Algorithm
    */
    public List<List<Neighbour>> getNeighboursL1() {
        List<List<Neighbour>> neighboursList = new ArrayList<>(); /* For each index from 0 to tests -> list of k-Neighbours array */
        /* For each test image i */
        ProgressBar pb = new ProgressBar("Manhattan Distance", (long) (imagesTest.size()/split));
        for (int i = 0; i < imagesTest.size()/split; i++) {
            /* For each Train image j */
//            System.out.write(i);
            List<Neighbour> neighbours = new ArrayList<>(); /* List of all neighbors for image i */
            for (int j = 0; j < imagesTrain.size()/split; j++) {
                double[][] difference = MatrixUtility.MatrixDifference(imagesTest.get(i).getData(), imagesTrain.get(j).getData());
                double distanceManhattan = MatrixUtility.ManhattanDistance(difference);
                int labelTrain = imagesTrain.get(j).getLabel();
                neighbours.add(new Neighbour(labelTrain, distanceManhattan));
            }
            neighbours.sort((n1, n2) -> Double.compare(n1.distance, n2.distance));

            neighboursList.add(neighbours.subList(0, this.K));
            pb.step();
        }
        return neighboursList;
    }
    /*
        Returns a list of the k nearest neighbours for each testImage using the Euclidean Distance Algorithm
    */
    public List<List<Neighbour>> getNeighboursL2() {
        List<List<Neighbour>> neighboursList = new ArrayList<>(); /* For each index from 0 to tests -> list of k-Neighbours array */
        /* For each test image i */
        ProgressBar pb = new ProgressBar("Euclidean Distance", (long) (imagesTest.size()/split));
        for (int i = 0; i < imagesTest.size()/split; i++) {
            /* For each Train image j */
//            System.out.println(String.format("L1 - i: %d", i));
            List<Neighbour> neighbours = new ArrayList<>(); /* List of all neighbors for image i */
            for (int j = 0; j < imagesTrain.size()/split; j++) {
                double[][] difference = MatrixUtility.MatrixDifference(imagesTest.get(i).getData(), imagesTrain.get(j).getData());
                double distanceEuclidean = MatrixUtility.EuclideanDistance(difference);
                int labelTrain = imagesTrain.get(j).getLabel();
                neighbours.add(new Neighbour(labelTrain, distanceEuclidean));
            }
            neighbours.sort((n1, n2) -> Double.compare(n1.distance, n2.distance));

            neighboursList.add(neighbours.subList(0, this.K));
            pb.step();
        }
        return neighboursList;
    }
    public int recognizeImages(Image digit) {
        List<Neighbour> neighbours = new ArrayList<>();
        for (int j = 0; j < imagesTrain.size()/split; j++) {
            double[][] difference = MatrixUtility.MatrixDifference(digit.getData(), imagesTrain.get(j).getData());
            double distanceEuclidean = MatrixUtility.EuclideanDistance(difference);
            int labelTrain = imagesTrain.get(j).getLabel();
            neighbours.add(new Neighbour(labelTrain, distanceEuclidean));
        }
        neighbours.sort((n1, n2) -> Double.compare(n1.distance, n2.distance));
        int predictedLabel = mostRepeatedLabel(neighbours.subList(0, this.K));
        return predictedLabel;
    }
    /*
        Returns the label for most repeated neighbour
        @param KNearestNeighbours => List of k neighbours of a specific test image
    */
    public int mostRepeatedLabel(List<Neighbour> KNearestNeighbours) {
        // Frequency Array for the 10 digits of the mnist (0-9)
        int[] frequencyArray = new int[10];
        Arrays.fill(frequencyArray,0);
        for(Neighbour neighbour : KNearestNeighbours){
            frequencyArray[neighbour.label]++;
        }
        int maxIndex = 0, maxElement=-1;
        for (int i = 0; i < 10; i++) {
            if (maxElement < frequencyArray[i]){
                maxElement = frequencyArray[i];
                maxIndex=i;
            }
        }
        return maxIndex;
    }
    /*
        Gets the accuracy of the Predicts Neighbours;
        @param TestNeighbours => List of k neighbours for the whole test dataset
    */
    public double getAccuracy(List<List<Neighbour>> TestNeighbours) {
        int testImagesCount = TestNeighbours.size();
        int wrongPredictions = 0;
        for (int i = 0; i < testImagesCount; i++) {
            int predictedLabel = mostRepeatedLabel(TestNeighbours.get(i));
//            System.out.println(String.format("%d %d", predictedLabel, imagesTest.get(i).getLabel()));
            if (predictedLabel != imagesTest.get(i).getLabel()) {
                wrongPredictions++;
            }
        }
        double accuracy = (testImagesCount - wrongPredictions) / (double)testImagesCount;
        return accuracy;
    }
}