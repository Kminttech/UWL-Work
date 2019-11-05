import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Main m = new Main();
        m.run();
    }

    public void run(){
        Scanner userIn = new Scanner(System.in);
        System.out.print("Please enter a training set size (a positive multiple of 250 that is <= 1000):");
        int size = userIn.nextInt();
        while(size != 250 && size != 500 && size != 750 && size != 1000){
            System.out.println("Invalid Input: Valid inputs include: 250, 500, 750, 1000");
            System.out.print("Please Enter a Size: ");
            size = userIn.nextInt();
        }
        System.out.print("Please enter a training increment (either 10, 25, or 50):");
        int increment = userIn.nextInt();
        while(increment != 10 && increment != 25 && increment != 50){
            System.out.println("Invalid Input: Valid inputs include: 10, 25, 50");
            System.out.print("Please Enter an Increment: ");
            increment = userIn.nextInt();
        }
        try{
            Scanner propFileIn = new Scanner(new File("input_files/properties.txt"));
            System.out.println("\nLoading Property Information from file.");
            ArrayList<Property> propertyData = new ArrayList<Property>();
            int propIndex = 0;
            while (propFileIn.hasNextLine()){
                propertyData.add(new Property(propFileIn.nextLine(), propIndex++));
            }
            Scanner dataFileIn = new Scanner(new File("input_files/mushroom_data.txt"));
            System.out.println("Loading Data from database.");
            ArrayList<Mushroom> mushroomData = new ArrayList<Mushroom>();
            while (dataFileIn.hasNextLine()){
                mushroomData.add((int)(Math.random()*mushroomData.size()),new Mushroom(dataFileIn.nextLine()));
            }
            System.out.printf("\nCollecting set of %d training examples.\n\n", size);
            ArrayList<Mushroom> buildData = new ArrayList<Mushroom>(mushroomData.subList(0,size));
            ArrayList<Mushroom> testData = new ArrayList<Mushroom> (mushroomData.subList(size,mushroomData.size()));
            double[] stats = new double[size/increment];
            for(int i = increment; i <= buildData.size(); i += increment){
                System.out.printf("Running with %d examples in training set.\n\n", i);
                TreeNode tree = learning(new ArrayList<Mushroom>(buildData.subList(0, i)), propertyData, new ArrayList<Mushroom>(buildData.subList(0, i)));
                int trueCount = 0;
                for(Mushroom m : testData){
                    if(test(m, tree)){
                        trueCount++;
                    }
                }
                stats[(i-1)/increment] = (double) trueCount / (double) testData.size();
                System.out.printf("Given current tree, there are %d correct classifications\nout of %d possible (a success rate of %f percent).\n\n", trueCount, testData.size(), stats[(i-1)/increment]*100);
            }
            System.out.printf("\t----------\n\tStatistics\n\t----------\n\n");
            int curIndex = 0;
            for(int i = increment; i <= buildData.size(); i += increment){
                System.out.printf("Training set size: %d.  Success:  %f percent.\n",i,stats[curIndex++]*100);
            }
        }catch (IOException e){
            System.out.println("File Missing");
        }
    }

    public TreeNode learning(ArrayList<Mushroom> buildData, ArrayList<Property> propData, ArrayList<Mushroom> parentBuildData){
        int numPoison = 0;
        for(Mushroom m : buildData){
            if(m.poison == true){
                numPoison++;
            }
        }
        if(buildData.isEmpty()){
            //TODO change to plurality
            return new TreeNode(true);
        }else if(numPoison == 0){
            return new TreeNode(false);
        }else if(numPoison == buildData.size()){
            return new TreeNode(true);
        }else if(propData.isEmpty()){
            //TODO change to parent plurality
            return new TreeNode(true);
        }else{
            Property bestProperty = propData.get(0);
            double bestGain = 0;
            for(int i = 0; i < propData.size(); i++){
                int[] labelCounts = new int[propData.get(i).vars.size()];
                int[] labelPoison = new int[propData.get(i).vars.size()];
                for(Mushroom m : buildData){
                    int var = propData.get(i).vars.indexOf(m.props[i]);
                    labelCounts[var]++;
                    if(m.poison){
                        labelPoison[var]++;
                    }
                }
                double temp = (double)numPoison/(double)buildData.size();
                double curGain = -((temp * (Math.log10(temp)/Math.log10(2))) + ((1-temp) * (Math.log10(1-temp)/Math.log10(2))));
                for(int j = 0; j < labelCounts.length; j++){
                    if(labelCounts[j] != 0 && labelPoison[j] != 0 && labelCounts[j] != labelPoison[j]) {
                        double curLabelPoison = (double) labelPoison[j];
                        double curLabelCounts = (double) labelCounts[j];
                        temp = curLabelPoison / curLabelCounts;
                        double temp2 = (curLabelCounts / (double) buildData.size()) * -(temp * (Math.log10(temp) / Math.log10(2)) + ((1 - temp) * (Math.log10(1 - temp) / Math.log10(2))));
                        curGain -= temp2;
                    }
                }
                if(curGain > bestGain){
                    bestProperty = propData.get(i);
                    bestGain = curGain;
                }
            }
            HashMap<String, TreeNode> propNodes = new HashMap<String, TreeNode>();
            for(int j = 0; j < bestProperty.vars.size(); j++){
                ArrayList<Mushroom> nextNodeData = new ArrayList<Mushroom>();
                for(int k = 0; k < buildData.size(); k++){
                    if(buildData.get(k).props[bestProperty.index].equals(bestProperty.vars.get(j))){
                        nextNodeData.add(buildData.get(k));
                    }
                }
                propNodes.put(bestProperty.vars.get(j), learning(nextNodeData,propData,buildData));
            }
            return new TreeNode(bestProperty, propNodes);
        }
    }

    public boolean test(Mushroom m, TreeNode tree){
        if(tree.leafNode){
            return m.poison == tree.poisonous;
        }else{
            return test(m, tree.propNodes.get(m.props[tree.prop.index]));
        }
    }

    public class TreeNode {
        boolean leafNode;
        boolean poisonous;
        Property prop;
        HashMap<String, TreeNode> propNodes;

        public TreeNode(Property prop, HashMap<String, TreeNode> propNodes){
            leafNode = false;
            this.prop = prop;
            this.propNodes = propNodes;
        }

        public TreeNode(boolean poisonous){
            leafNode = true;
            this.poisonous = poisonous;
        }
    }

    public class Mushroom {
        String[] props;
        boolean poison;

        public Mushroom(String lineIn){
            props = new String[22];
            Scanner mushScan = new Scanner(lineIn);
            for(int i = 0; i < props.length; i++){
                props[i] = mushScan.next();
            }
            if(mushScan.next().charAt(0) == 'p'){
                poison = true;
            }else{
                poison = false;
            }
        }
    }

    public class Property {
        String name;
        int index;
        ArrayList<String> vars;

        public Property(String name, int index, ArrayList<String> vars){
            this.name = name;
            this.index = index;
            this.vars = vars;
        }

        public Property(String s, int index){
            name = s.substring(0,s.indexOf(':'));
            this.index = index;
            vars = new ArrayList<String>();
            String propVars = s.substring(s.indexOf(':')+1);
            Scanner propScan = new Scanner(propVars);
            while (propScan.hasNext()){
                vars.add(propScan.next());
            }
        }
    }
}
