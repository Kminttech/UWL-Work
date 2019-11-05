import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BayesNet {

    ArrayList<BayesNode> nodes;

    public static void main(String[] args) {
        System.out.printf("\nLoading file \"%s\".\n\n", args[0]);
	    BayesNet mainNet = new BayesNet(args[0]);
        Scanner cmdIn = new Scanner(System.in);

        String curCmd = "";
        while(!curCmd.equals("quit")){
            curCmd = cmdIn.nextLine();
            Scanner cmdParse = new Scanner(curCmd);
            BayesNode variable = mainNet.findByName(cmdParse.next());
            if(cmdParse.hasNext()){
                cmdParse.next();
            }
            ArrayList<Evidence> e = new ArrayList<>();
            while(cmdParse.hasNext()){
                BayesNode curEvidence = mainNet.findByName(cmdParse.next());
                cmdParse.next();
                String curEvidenceState = cmdParse.next();
                if(curEvidenceState.charAt(curEvidenceState.length()-1) == ','){
                    curEvidenceState = curEvidenceState.substring(0,curEvidenceState.length()-1);
                }
                e.add(mainNet.new Evidence(curEvidence, curEvidenceState));
            }
            Distribution response = mainNet.enumerationAsk(variable,e);
            for(int i = 0; i < response.states.length; i++){
                System.out.printf("P(%s) = %f,", response.states[i], response.probabilities[i]);
            }
        }
    }

    public BayesNet(String constructionFile){
        nodes = new ArrayList<>();
        try {
            File inFile = new File(constructionFile);
            Scanner input = new Scanner(inFile);

            String nodeName = input.next();

            while(!nodeName.equals("#")){
                Scanner valScanner = new Scanner(input.nextLine());
                ArrayList<String> vals = new ArrayList<>();
                while(valScanner.hasNext()){
                    vals.add(valScanner.next());
                }
                nodes.add(new BayesNode(nodeName, vals));
                nodeName = input.next();
            }
            input.nextLine();

            nodeName = input.next();

            while(!nodeName.equals("#")){
                BayesNode cur = findByName(nodeName);
                Scanner parentScanner = new Scanner(input.nextLine());
                while(parentScanner.hasNext()){
                    cur.addParent(findByName(parentScanner.next()));
                }
                nodeName = input.next();
            }
            input.nextLine();

            while(input.hasNextLine()){
                BayesNode cur = findByName(input.next());
                cur.initializeProbs();
                for(int i = 0; i < cur.probabilities.length/(cur.possibleValues.size()-1); i++){
                    int index = 0;
                    int prevSizeMultiply = 1;
                    for(int j = 0; j < cur.parents.size(); j++){
                        index += (cur.parents.get(j).possibleValues.indexOf(input.next())*prevSizeMultiply);
                        prevSizeMultiply *= cur.possibleValues.size();
                    }
                    for(int j = 0; j < cur.possibleValues.size()-1; j++){
                        cur.setProb(index, input.nextDouble());
                        index++;
                    }
                }
            }
        }catch(IOException e){
            System.out.println("File Error Occurred");
        }
    }

    public BayesNode findByName(String name){
        for(BayesNode cur : nodes){
            if(cur.name.equals(name)){
                return cur;
            }
        }
        return null;
    }

    public Distribution enumerationAsk(BayesNode x, ArrayList<Evidence> e){
        Distribution answer = new Distribution(x);
        double lastProb = 1;
        for(int i = 0; i < answer.states.length-1; i++){
            answer.probabilities[i] = enumerateAll(x, answer.states[i], e, 0);
            lastProb -= answer.probabilities[i];
        }
        answer.probabilities[answer.probabilities.length-1] = lastProb;
        return answer;
    }

    public double enumerateAll(BayesNode x,String state,ArrayList<Evidence> e,int index){
        if(index >= nodes.size()){ return 1; }
        BayesNode cur = nodes.get(index);
        String value = null;
        for(Evidence check : e){
            if(check.var.equals(cur)){
                value = check.val;
                break;
            }
        }
        if(value != null){
            return cur.probabilities[cur.possibleValues.indexOf(value)] * enumerateAll(x,state,e,index+1);  //P(y | parents(Y)) * ENUMERATE-ALL(REST(vars),e)
        }else{
            double sum = 0;
            for(String checkVal : cur.possibleValues){
                Evidence tempEv = new Evidence(cur,checkVal);
                e.add(tempEv); // extend e with Y = y
                if(cur.possibleValues.indexOf(checkVal) == cur.probabilities.length){
                    double temp = 1;
                    for(double minus : cur.probabilities){
                        temp -= minus;
                    }
                    sum += temp * enumerateAll(x,state,e,index+1);
                }else{
                    sum += cur.probabilities[cur.possibleValues.indexOf(checkVal)] * enumerateAll(x,state,e,index+1);
                }
                e.remove(tempEv);
            }
            return sum;  // given(Y) P(y|parents(Y)) * ENUMERATE-ALL(REST(vars),ey) where ey is e extended with Y = y
        }
    }

    public class BayesNode {
        String name;
        ArrayList<String> possibleValues;
        ArrayList<BayesNode> parents;
        double[] probabilities;

        public BayesNode(String name, ArrayList<String> vals){
            this.name = name;
            possibleValues = vals;
            parents = new ArrayList<>();
        }

        public void addParent(BayesNode newParent){
            parents.add(newParent);
        }

        public void initializeProbs(){
            int probSize = this.possibleValues.size()-1;
            for(BayesNode cur : parents){
                probSize *= cur.possibleValues.size();
            }
            probabilities = new double[probSize];
        }

        public void setProb(int index, double val){
            probabilities[index] = val;
        }
    }

    public class Evidence {
        BayesNode var;
        String val;

        public Evidence(BayesNode variable, String value){
            var = variable;
            val = value;
        }
    }

    public class Distribution {
        String[] states;
        double[] probabilities;

        public Distribution(BayesNode node){
            states = node.possibleValues.toArray(new String[node.possibleValues.size()]);
            probabilities = new double[node.possibleValues.size()];
        }
    }
}
