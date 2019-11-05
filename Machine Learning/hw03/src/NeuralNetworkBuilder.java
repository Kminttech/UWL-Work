import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NeuralNetworkBuilder {

    public static void main(String[] args){
        NeuralNetworkBuilder n = new NeuralNetworkBuilder();
        n.run();
    }

    public void run(){
        Scanner userIn = new Scanner(System.in);
        System.out.print("\nEnter L to load trained network, T to train a new one, Q to quit: ");
        String select = userIn.next();
        while (!select.equals("Q") && !select.equals("q")){
            System.out.println();
            if(select.equals("T") || select.equals("t")){
                int res = 0;
                while (res != 5 && res != 10 && res != 15 && res != 20){
                    System.out.print("Resolution of data (5/10/15/20): ");
                    res = userIn.nextInt();
                }
                int resInput = 0;
                String trainSetFile = "";
                String testSetFile = "";
                if(res == 5){
                    resInput = 65;
                    trainSetFile = "trainSet_05.dat";
                    testSetFile = "testSet_05.dat";
                }else if(res == 10){
                    resInput = 140;
                    trainSetFile = "trainSet_10.dat";
                    testSetFile = "testSet_10.dat";
                }else if(res == 10){
                    resInput = 265;
                    trainSetFile = "trainSet_15.dat";
                    testSetFile = "testSet_15.dat";
                }else{
                    resInput = 440;
                    trainSetFile = "trainSet_20.dat";
                    testSetFile = "testSet_20.dat";
                }
                int hidden = -1;
                while(hidden < 0 || hidden > 10){
                    System.out.print("Number of hidden layers: ");
                    hidden = userIn.nextInt();
                }
                NeuralNet n = buildNet(hidden, resInput);
                System.out.print("\nInitializing network...\n");
                System.out.printf("Training on %s...\n", trainSetFile);
                trainNet(n,"trainSet_data/" + trainSetFile, res);
                System.out.printf("Testing on %s...\n", testSetFile);
                double accuracy = testNet(n, "testSet_data/" + testSetFile, res);
                System.out.printf("Accuracy achieved: %.1f%%\n", accuracy*100);
                System.out.print("Save network (Y/N)? ");
                select = userIn.next();
                while (!select.equals("Y") && !select.equals("y") && !select.equals("N") && !select.equals("n")){
                    System.out.print("Save network (Y/N)? ");
                    select = userIn.next();
                }
                if(select.equals("Y") || select.equals("y")){
                    System.out.print("File-name: ");
                    String outName = userIn.next();
                    System.out.println("Saving network...");
                    saveNet(n,outName);
                    System.out.printf("Network saved to file: %s\n", outName);
                }
            }else if(select.equals("L") || select.equals("l")){
                System.out.print("Network file-name: ");
                String inFile = userIn.next();
                System.out.printf("\nLoading network from: %s\n", inFile);
                NeuralNet n = loadNet(inFile);
                System.out.printf("Input layer size: %d nodes\n", n.layers[0].length);
                if(n.layers.length > 2){
                    System.out.printf("Hidden layer sizes: %d", n.layers[1].length);
                    for(int i = 2; i < n.layers.length-1; i++){
                        System.out.printf(", %d", n.layers[i].length);
                    }
                }
                System.out.printf("\nOutput layer size: %d\n", n.layers[n.layers.length-1].length);
                int res = 0;
                String testSetFile = "";
                if(n.layers[0].length == 65){
                    res = 5;
                    testSetFile = "testSet_05.dat";
                }else if(n.layers[0].length == 140){
                    res = 10;
                    testSetFile = "testSet_10.dat";
                }else if(n.layers[0].length == 265){
                    res = 15;
                    testSetFile = "testSet_15.dat";
                }else if(n.layers[0].length == 440){
                    res = 20;
                    testSetFile = "testSet_20.dat";
                }
                System.out.printf("Testing on %s...\n", testSetFile);
                double accuracy = testNet(n, "testSet_data/" + testSetFile, res);
                System.out.printf("Accuracy achieved: %.1f%%\n", accuracy*100);

            }
            System.out.print("\nEnter L to load trained network, T to train a new one, Q to quit: ");
            select = userIn.next();
        }
        System.out.println("\nGoodbye.");
    }

    public NeuralNet buildNet(int hiddenLayer, int inNum){
        NeuralNet n = new NeuralNet(hiddenLayer);
        n.layers[0] = new NeuralNode[inNum];
        for (int j = 0; j < inNum; j++) {
            n.layers[0][j] = new NeuralNode();
        }
        int curSize = inNum;
        int prevSize;
        int i = 1;
        if(hiddenLayer > 0) {
            Scanner userIn = new Scanner(System.in);
            for (i = 1; i <= hiddenLayer; i++) {
                System.out.printf("Size of hidden layer %d: ", i);
                prevSize = curSize;
                curSize = userIn.nextInt();
                n.layers[i] = new NeuralNode[curSize];
                for (int j = 0; j < curSize; j++) {
                    n.layers[i][j] = new NeuralNode(prevSize);
                }
            }
        }
        n.layers[i] = new NeuralNode[14];
        for(int j = 0; j < 14; j++){
            n.layers[i][j] = new NeuralNode(curSize);
        }
        return n;
    }

    public void propagateForwardInput(NeuralNet n){
        for(int i = 1; i < n.layers.length; i++){
            for(int j = 0; j < n.layers[i].length; j++){
                NeuralNode cur = n.layers[i][j];
                double weightedSum = cur.dummyBias;
                for(int k = 0; k < n.layers[i-1].length; k++){
                    weightedSum += n.layers[i-1][k].value * cur.weights[k];
                }
                cur.value = (1/(1+Math.exp(-weightedSum)));
                cur.delta = 0;
            }
        }
    }

    public void backPropagate(NeuralNet n, double[] correct){
        for(int j = 0; j < n.layers[n.layers.length-1].length; j++){
            NeuralNode cur = n.layers[n.layers.length-1][j];
            cur.delta = (cur.value)*(1-cur.value) * (correct[j]-cur.value);
        }
        for(int i = n.layers.length-2; i > 0; i--){
            for(int j = 0; j < n.layers[i].length; j++){
                NeuralNode cur = n.layers[i][j];
                double weightedDeltaSum = 0;
                for(int k = 0; k < n.layers[i+1].length; k++){
                    weightedDeltaSum += n.layers[i+1][k].weights[j]*n.layers[i+1][k].delta;
                }
                cur.delta = weightedDeltaSum*cur.value;
            }
        }
        for(int i = 1; i < n.layers.length; i++){
            for(int j = 0; j < n.layers[i].length; j++){
                NeuralNode cur = n.layers[i][j];
                cur.dummyBias += cur.delta;
                for(int k = 0; k < cur.weights.length; k++){
                    cur.weights[k] += n.layers[i-1][k].value * cur.delta;
                }
            }
        }
    }

    public void trainNet(NeuralNet n, String filePath, int res){
        try{
            for(int j = 0; j < 1000; j++) {
                Scanner fileIn = new Scanner(new File(filePath)).useDelimiter("[()\\s]");
                for(int i = 0; i < 5; i++){
                    fileIn.nextLine();
                }
                double maxError = 0;
                while (fileIn.hasNextDouble()) {
                    for (int i = 0; i < n.layers[0].length-(res*res); i++) {
                        n.layers[0][i].value = fileIn.nextDouble();
                    }
                    for (int i = n.layers[0].length-(res*res); i < n.layers[0].length; i++) {
                        n.layers[0][i].value = fileIn.nextDouble()/255;
                    }
                    propagateForwardInput(n);
                    double correct[] = new double[14];
                    fileIn.next();
                    fileIn.next();
                    double curError = 0;
                    for (int i = 0; i < 14; i++) {
                        correct[i] = fileIn.nextDouble();
                        curError += Math.abs(correct[i]-n.layers[n.layers.length-1][i].value);
                    }
                    curError /= 14;
                    if(curError > maxError){
                        maxError = curError;
                    }
                    backPropagate(n, correct);
                    if(fileIn.hasNext()){
                        fileIn.next();
                    }
                    if(fileIn.hasNext()){
                        fileIn.next();
                    }
                }
                if(maxError < 0.01){
                    break;
                }
                fileIn.close();
            }
        }catch (IOException e){
            System.out.printf("Error Training network: %s\n", e.getMessage());
        }
    }

    public double testNet(NeuralNet n, String filePath, int res){
        int right = 0;
        int total = 0;
        try{
            Scanner fileIn = new Scanner(new File(filePath)).useDelimiter("[()\\s]");
            for(int i = 0; i < 5; i++){
                fileIn.nextLine();
            }
            while (fileIn.hasNextDouble()) {
                for (int i = 0; i < n.layers[0].length-(res*res); i++) {
                    n.layers[0][i].value = fileIn.nextDouble();
                }
                for (int i = n.layers[0].length-(res*res); i < n.layers[0].length; i++) {
                    n.layers[0][i].value = fileIn.nextDouble()/255;
                }
                propagateForwardInput(n);
                double correct[] = new double[14];
                fileIn.next();
                fileIn.next();
                for (int i = 0; i < 14; i++) {
                    correct[i] = (double) fileIn.nextInt();
                }
                double[] answer = n.getOutput();
                int givenOut= 0;
                int correctOut = 0;
                for (int i = 1; i < 14; i++) {
                    if(correct[i] == 1){
                        correctOut = i;
                    }
                    if(answer[i] > answer[givenOut]){
                        givenOut = i;
                    }
                }
                if (correctOut == givenOut) {
                    right++;
                }
                total++;
                if(fileIn.hasNext()){
                    fileIn.next();
                }
                if(fileIn.hasNext()){
                    fileIn.next();
                }
            }
            fileIn.close();
        }catch (IOException e){
            System.out.printf("Error Testing network: %s\n", e.getMessage());
        }
        return ((double)right)/((double)total);
    }

    public void saveNet(NeuralNet n, String fileName){
        try {
            FileWriter out = new FileWriter(new File(fileName));
            out.write(n.layers.length + " ");
            for(int i = 0; i < n.layers.length; i++){
                out.write(n.layers[i].length + " ");
            }
            for(int i = 1; i < n.layers.length; i++){
                out.write("\n");
                for(int j = 0; j < n.layers[i].length; j++){
                    NeuralNode cur = n.layers[i][j];
                    out.write(cur.dummyBias +" ");
                    for(int k = 0; k < cur.weights.length; k++){
                        out.write(cur.weights[k] + " ");
                    }
                    out.write("\n");
                }
            }
            out.write("\n");
            out.close();
        }catch (IOException e){
            System.out.printf("Error saving network: %s\n", e.getMessage());
        }
    }

    public NeuralNet loadNet(String fileName){
        try {
            Scanner in = new Scanner(new File(fileName));
            NeuralNet n = new NeuralNet(in.nextInt()-2);
            for(int i = 0; i < n.layers.length; i++){
                n.layers[i] = new NeuralNode[in.nextInt()];
            }
            for(int i = 0; i < n.layers[0].length; i++){
                n.layers[0][i] = new NeuralNode();
            }
            for(int i = 1; i < n.layers.length; i++){
                for(int j = 0; j < n.layers[i].length; j++){
                    double dummyTemp = in.nextDouble();
                    double[] weight = new double[n.layers[i-1].length];
                    for(int k = 0; k < weight.length; k++){
                        weight[k] = in.nextDouble();
                    }
                    n.layers[i][j] = new NeuralNode(dummyTemp, weight);
                }
            }

            return n;
        }catch (IOException e){
            System.out.printf("Error loading network: %s\n", e.getMessage());
        }
        return null;
    }

    public class NeuralNode{
        public double[] weights;
        public double dummyBias;
        public double value;
        public double delta;

        public NeuralNode(){}

        public NeuralNode(int numInputs){
            weights = new double[numInputs];
            dummyBias = Math.random()/100;
            for(int i = 0; i < numInputs; i++){
                weights[i] = Math.random()/100;
            }
        }

        public NeuralNode(double dummyBias, double[] weights){
            this.dummyBias = dummyBias;
            this.weights = weights;
        }
    }

    public class NeuralNet{
        NeuralNode[][] layers;

        public NeuralNet(int numHidden){
            layers = new NeuralNode[numHidden+2][];
        }

        public double[] getOutput(){
            double[] out = new double[14];
            for(int i = 0; i < 14; i++){
                out[i] = layers[layers.length-1][i].value;
            }
            return out;
        }
    }
}
