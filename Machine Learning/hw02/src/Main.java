import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    int dimension;

    public static void main(String[] args) {
	    Main m = new Main();
	    m.run(args[0], Integer.parseInt(args[1]));
    }

    public void run(String filePath, int size){
        ArrayList<Element> data = new ArrayList<Element>();
        try{
            Scanner fileIn = new Scanner(new File(filePath));
            dimension = fileIn.nextInt();
            fileIn.nextLine();
            while (fileIn.hasNextLine()){
                double[] arr = new double[dimension];
                for(int i = 0; i < dimension; i++){
                    arr[i] = fileIn.nextDouble();
                }
                data.add(new Element(arr));
                fileIn.nextLine();
            }
        }catch (IOException e){
            System.out.println("File input not found");
            return;
        }
        KDNode root = buildTree(data, 0, size);
        //Output Settings
        Scanner userIn = new Scanner(System.in);
        System.out.print("Print tree leaves? (Enter Y for yes, anything else for no): ");
        String responce = userIn.next();
        if(responce.equals("Y") || responce.equals("y")){
            System.out.print("\n------------------------\n\n");
            printTree(root, 0, "");
            System.out.print("\n------------------------\n\n");
        }
        System.out.print("Test data? (Enter Y for yes, anything else to quit): ");
        responce = userIn.next();
        if(responce.equals("Y") || responce.equals("y")){
            System.out.print("Name of data-file: ");
            String fileTest = userIn.next();
            System.out.print("\n------------------------\n\n");
            try{
                Scanner testIn = new Scanner(new File(fileTest));
                if(testIn.nextInt() != dimension){
                    System.out.println("Error Test File wrong dimension");
                    return;
                }
                testIn.nextLine();
                ArrayList<Element> tests = new ArrayList<Element>();
                while (testIn.hasNextLine()){
                    double[] arr = new double[dimension];
                    for(int i = 0; i < dimension; i++){
                        arr[i] = testIn.nextDouble();
                    }
                    tests.add(new Element(arr));
                    testIn.nextLine();
                }
                //Run Tests
                for(int i = 0; i < tests.size(); i++){
                    KDNode leaf = findLeaf(tests.get(i), root, 0);
                    System.out.printf("(%f", tests.get(i).array[0]);
                    for(int j = 1; j < dimension; j++){
                        System.out.printf(" %f", tests.get(i).array[j]);
                    }
                    System.out.printf(") is in the set: [");
                    System.out.printf("(%f",leaf.containedElements.get(0).array[0]);
                    for(int j = 1; j < dimension; j++){
                        System.out.printf(" %f", leaf.containedElements.get(0).array[j]);
                    }
                    System.out.print(")");
                    for(int k = 1; k < leaf.containedElements.size(); k++){
                        System.out.printf(", (%f",leaf.containedElements.get(k).array[0]);
                        for(int j = 1; j < dimension; j++){
                            System.out.printf(" %f", leaf.containedElements.get(k).array[j]);
                        }
                        System.out.print(")");
                    }
                    System.out.println("]");
                    //Nearest Neighbor
                    nearestNeighbor(tests.get(i), leaf.containedElements);
                }
            }catch (IOException e){
                System.out.println("Test file input not found");
            }
            System.out.print("\n------------------------\n\n");
        }
        System.out.print("Goodbye.");
    }

    public KDNode buildTree(ArrayList<Element> data, int depth, int size) {
        if(data.size() > size){
            int split = (depth % dimension);
            double sum = 0;
            for(Element e : data){
                sum += e.array[split];
            }
            double median = sum / data.size();
            ArrayList<Element> lesserData = new ArrayList<Element>();
            ArrayList<Element> greaterData = new ArrayList<Element>();
            for(Element e : data){
                if(e.array[split] <= median){
                    lesserData.add(e);
                }else {
                    greaterData.add(e);
                }
            }
            return new KDNode(median, buildTree(lesserData, depth+1, size), buildTree(greaterData, depth+1, size));
        }
        KDNode node = new KDNode();
        node.containedElements.addAll(data);
        return node;
    }

    public int printTree(KDNode tree, int leafNum, String direc){
        if(tree.leaf == true){
            double[] minBounds = new double[dimension];
            double[] maxBounds = new double[dimension];
            for(Element e : tree.containedElements){
                for(int i = 0; i < dimension; i++){
                    if(e.array[i] < minBounds[i]){
                        minBounds[i] = e.array[i];
                    }
                    if(e.array[i] > maxBounds[i]){
                        maxBounds[i] = e.array[i];
                    }
                }
            }
            System.out.printf("%d. %s: Bounding Box: [(%f", leafNum, direc, minBounds[0]);
            for(int i = 1; i < dimension; i++){
                System.out.printf(" %f", minBounds[i]);
            }
            System.out.printf("), (%f", maxBounds[0]);
            for(int i = 1; i < dimension; i++){
                System.out.printf(" %f", maxBounds[i]);
            }
            System.out.printf(")]\nData in leaf: [");
            System.out.printf("(%f", tree.containedElements.get(0).array[0]);
            for(int i = 1; i < dimension; i++){
                System.out.printf(" %f", tree.containedElements.get(0).array[i]);
            }
            System.out.printf(")");
            for(int j = 1; j < tree.containedElements.size(); j++){
                System.out.printf(", (%f", tree.containedElements.get(j).array[0]);
                for(int i = 1; i < dimension; i++){
                    System.out.printf(" %f", tree.containedElements.get(j).array[i]);
                }
                System.out.printf(")");
            }
            System.out.printf("]\n\n");
            return leafNum+1;
        }
        return printTree(tree.right, printTree(tree.left, leafNum, direc+"L"), direc+"R");
    }

    public KDNode findLeaf(Element e, KDNode tree, int depth){
        if(tree.leaf){
            return tree;
        }
        int split = (depth % dimension);
        if(e.array[split] <= tree.median){
            return findLeaf(e, tree.left,depth+1);
        }else{
            return findLeaf(e, tree.right, depth+1);
        }
    }

    public void nearestNeighbor(Element e, ArrayList<Element> neighbors){
        double temp = 0;
        for(int i = 0; i < dimension; i++){
            temp += Math.pow((e.array[i] - neighbors.get(0).array[i]), 2);
        }
        int closest = 0;
        double bestDist = Math.sqrt(temp);
        for(int j = 1; j < neighbors.size(); j++){
            temp = 0;
            for(int i = 0; i < dimension; i++){
                temp += Math.pow((e.array[i] - neighbors.get(j).array[i]), 2);
            }
            double dist = Math.sqrt(temp);
            if(dist < bestDist){
                closest = j;
                bestDist = dist;
            }
        }
        System.out.printf("Nearest neighbor: (%f", neighbors.get(closest).array[0]);
        for(int i = 1; i < dimension; i++){
            System.out.printf(" %f", neighbors.get(closest).array[i]);
        }
        System.out.printf(") (distance = %f)\n\n",bestDist);
    }

    public class KDNode {
        public boolean leaf;
        public ArrayList<Element> containedElements;
        public double median;
        public KDNode left;
        public KDNode right;

        public KDNode(){
            leaf = true;
            containedElements = new ArrayList<Element>();
        }

        public KDNode(double median, KDNode left, KDNode right){
            leaf = false;
            this.median = median;
            this.left = left;
            this.right = right;
        }
    }

    public class Element {
        public double[] array;

        public Element(double[] arr){
            array = arr;
        }
    }
}
