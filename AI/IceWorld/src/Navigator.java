import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Navigator {

    private char[][] world;
    private char[][] policy;
    private double[][][] policyValues;
    private int startX;
    private int startY;
    public static final int ITERATIONS_RANDOM = 1000;
    public static final int ITERATIONS_GREEDY = 1000;
    public static final char UP = 'U';
    public static final char LEFT = 'L';
    public static final char DOWN = 'D';
    public static final char RIGHT = 'R';


    public static void main(String[] args) {
        Navigator nav = new Navigator();
        nav.loadWorld(args[0]);
        try{
            nav.iterateSARSA();
            nav.resetPolicy();
            nav.iterateQLearn();
        }catch (IOException e){
            System.out.println("Error with output");
        }
    }

    public Navigator(){
    }

    public void loadWorld(String file){
        try{
            File inFile = new File(file);
            Scanner input = new Scanner(inFile);
            int lines = 0;
            while(input.hasNextLine()){
                input.nextLine();
                lines++;
            }
            world = new char[lines][];
            policy = new char[lines][];
            policyValues = new double[lines][][];
            input = new Scanner(inFile);
            for(int i = 0; i < lines; i++){
                world[i] = input.nextLine().toCharArray();
                policy[i] = new char[world[i].length];
                policyValues[i] = new double[world[i].length][];
                for(int j = 0; j < world[i].length; j++){
                    if(world[i][j] == 'S'){
                        policy[i][j] = 'S';
                        startX = i;
                        startY = j;
                    }else if(world[i][j] == 'H'){
                        policy[i][j] = 'H';
                    }else{
                        policy[i][j] = 'R';
                    }
                    policyValues[i][j] = new double[4];
                }
            }
        }catch(IOException e){
            System.out.println("Error with input file");
        }
    }

    public void iterateSARSA() throws IOException{
        int numMovesPer = 10 * world.length * world[0].length;
        double discountFactor = .9;
        double randomness = .9;
        BufferedWriter rewardWriter = new BufferedWriter(new FileWriter("sarsaRewards.txt"));
        BufferedWriter policyWriter = new BufferedWriter(new FileWriter("sarsaPolicys.txt"));
        //Create File to output rewards
        for(int i = 1; i <= ITERATIONS_RANDOM + ITERATIONS_GREEDY; i++) {
            double finalVal = 0;
            int x = startX;
            int y = startY;
            int prevX = startX;
            int prevY = startY;
            char dir;
            char nextDir;
            int dirValue = 0;
            int nextDirValue = 0;
            boolean slip = false;
            double randomChance = Math.random();

            if (i < ITERATIONS_RANDOM && randomChance < randomness || world[x][y] == 'S') {
                //Random Move
                dir = randomDir();
            }else {
                if (policy[x][y] == 'U') {
                    dir = UP;
                } else if (policy[x][y] == 'L') {
                    dir = LEFT;
                } else if (policy[x][y] == 'D') {
                    dir = DOWN;
                } else {
                    dir = RIGHT;
                }
            }

            if (world[x][y] == 'I') {
                int slipChance = (int)(Math.random() * 5);
                if (slipChance == 0) {
                    slip = true;
                }
            }
            switch(dir){
                case UP:
                    dirValue = 0;
                    x--;
                    if (slip) {
                        int slipDir = (int)(Math.random()*2);
                        if (slipDir == 0) {
                            y--;
                        } else {
                            y++;
                        }
                    }
                    break;
                case LEFT:
                    dirValue = 1;
                    y--;
                    if (slip) {
                        int slipDir = (int)(Math.random()*2);
                        if (slipDir == 0) {
                            x--;
                        } else {
                            x++;
                        }
                    }
                    break;
                case DOWN:
                    dirValue = 2;
                    x++;
                    if (slip) {
                        int slipDir = (int)(Math.random()*2);
                        if (slipDir == 0) {
                            y--;
                        } else {
                            y++;
                        }
                    }
                    break;
                case RIGHT:
                    dirValue = 3;
                    y++;
                    if (slip) {
                        int slipDir = (int)(Math.random()*2);
                        if (slipDir == 0) {
                            x--;
                        } else {
                            x++;
                        }
                    }
                    break;
            }

            if (x < 0 || x >= world.length) {
                x = prevX;
            }
            if (y < 0 || y >= world[x].length) {
                y = prevY;
            }

            int reward = getReward(x,y);
            if(world[x][y] == 'H'){
                x = prevX;
                y = prevY;
            }

            for (int j = 1; j < numMovesPer; j++) {
                randomChance = Math.random();
                if (i < ITERATIONS_RANDOM && randomChance < randomness) {
                    //Random Move
                    nextDir = randomDir();
                }else {
                    nextDir = policy[x][y];
                }

                if (world[x][y] == 'I') {
                    int slipChance = (int)(Math.random()*5);
                    if (slipChance == 0) {
                        slip = true;
                    }
                }

                int nextX = x;
                int nextY = y;
                switch (nextDir){
                    case UP:
                        nextDirValue = 0;
                        nextX--;
                        if (slip) {
                            int slipDir = (int)(Math.random()*2);
                            if (slipDir == 0) {
                                nextY--;
                            } else {
                                nextY++;
                            }
                        }
                        break;
                    case LEFT:
                        nextDirValue = 1;
                        nextY--;
                        if (slip) {
                            int slipDir = (int)(Math.random()*2);
                            if (slipDir == 0) {
                                nextX--;
                            } else {
                                nextX++;
                            }
                        }
                        break;
                    case DOWN:
                        nextDirValue = 2;
                        nextX++;
                        if (slip) {
                            int slipDir = (int)(Math.random()*2);
                            if (slipDir == 0) {
                                nextY--;
                            } else {
                                nextY++;
                            }
                        }
                        break;
                    case RIGHT:
                        nextDirValue = 3;
                        nextY++;
                        if (slip) {
                            int slipDir = (int)(Math.random()*2);
                            if (slipDir == 0) {
                                nextX--;
                            } else {
                                nextX++;
                            }
                        }
                        break;
                }

                if (nextX < 0 || nextX >= world.length) {
                    nextX = x;
                }
                if (nextY < 0 || nextY >= world[nextX].length) {
                    nextY = y;
                }

                int nextReward = getReward(nextX,nextY);
                if(world[nextX][nextY] == 'H'){
                    nextX = x;
                    nextY = y;
                }

                policyValues[prevX][prevY][dirValue] += reward + (discountFactor*nextReward);
                finalVal+=policyValues[prevX][prevY][dirValue];
                if(world[prevX][prevY] != 'S'){
                    int bestChoice = 0;
                    for(int k = 1; k < 4; k++){
                        if(policyValues[prevX][prevY][k] > policyValues[prevX][prevY][bestChoice]){
                            bestChoice = k;
                        }
                    }
                    if(bestChoice == 0){
                        policy[prevX][prevY] = UP;
                    }else if(bestChoice == 1){
                        policy[prevX][prevY] = LEFT;
                    }else if(bestChoice == 2){
                        policy[prevX][prevY] = DOWN;
                    }else{
                        policy[prevX][prevY] = RIGHT;
                    }
                }

                prevX = x;
                prevY = y;
                x = nextX;
                y = nextY;
                dir = nextDir;
                dirValue = nextDirValue;
                reward = nextReward;
                if(world[nextX][nextY] == 'G'){
                    break;
                }
            }
            if (i % 10 == 0) {
                randomness = 0.9 / (i / 10);
            }
            rewardWriter.write(finalVal + "\n");
            if(i % 100 == 0){
                policyWriter.write("Episode: " + i + "\n");
                for(int j = 0; j < world.length; j++){
                    for(int k = 0; k < world[j].length; k++){
                        policyWriter.write(policy[j][k]);
                    }
                    policyWriter.write("\n");
                }
                policyWriter.write("\n");
            }
        }
        rewardWriter.close();
        policyWriter.close();
    }

    public void iterateQLearn() throws IOException{
        int numMovesPer = 10 * world.length * world[0].length;
        double discountFactor = .9;
        double randomness = .9;
        BufferedWriter rewardWriter = new BufferedWriter(new FileWriter("qRewards.txt"));
        BufferedWriter policyWriter = new BufferedWriter(new FileWriter("qPolicys.txt"));
        //Create File to output rewards
        for(int i = 1; i <= ITERATIONS_RANDOM + ITERATIONS_GREEDY; i++) {
            double finalVal = 0;
            int x = startX;
            int y = startY;
            int prevX = startX;
            int prevY = startY;
            char dir;
            int dirValue = 0;
            boolean slip = false;
            for (int j = 0; j < numMovesPer; j++) {
                double randomChance = Math.random();
                if (i < ITERATIONS_RANDOM && randomChance < randomness || world[x][y] == 'S') {
                    //Random Move
                    dir = randomDir();
                }else {
                    dir = policy[x][y];
                }

                if (world[x][y] == 'I') {
                    int slipChance = (int)(Math.random()*5);
                    if (slipChance == 0) {
                        slip = true;
                    }
                }

                switch (dir){
                    case UP:
                        dirValue = 0;
                        x--;
                        if (slip) {
                            int slipDir = (int)(Math.random()*2);
                            if (slipDir == 0) {
                                y--;
                            } else {
                                y++;
                            }
                        }
                        break;
                    case LEFT:
                        dirValue = 1;
                        y--;
                        if (slip) {
                            int slipDir = (int)(Math.random()*2);
                            if (slipDir == 0) {
                                x--;
                            } else {
                                x++;
                            }
                        }
                        break;
                    case DOWN:
                        dirValue = 2;
                        x++;
                        if (slip) {
                            int slipDir = (int)(Math.random()*2);
                            if (slipDir == 0) {
                                y--;
                            } else {
                                y++;
                            }
                        }
                        break;
                    case RIGHT:
                        dirValue = 3;
                        y++;
                        if (slip) {
                            int slipDir = (int)(Math.random()*2);
                            if (slipDir == 0) {
                                x--;
                            } else {
                                x++;
                            }
                        }
                        break;
                }

                if (x < 0 || x >= world.length) {
                    x = prevX;
                }
                if (y < 0 || y >= world[x].length) {
                    y = prevY;
                }

                int reward = getReward(x,y);
                if(world[x][y] == 'H'){
                    x = prevX;
                    y = prevY;
                }

                double optNextReward = Double.MIN_VALUE;
                for(int k = 0; k < 4; k++){
                    if(policyValues[x][y][k] > optNextReward){
                        optNextReward = policyValues[x][y][k];
                    }
                }


                policyValues[prevX][prevY][dirValue] += reward+(discountFactor*optNextReward);
                finalVal += policyValues[prevX][prevY][dirValue];
                if(world[prevX][prevY] != 'S'){
                    int bestChoice = 0;
                    for(int k = 1; k < 4; k++){
                        if(policyValues[prevX][prevY][k] > policyValues[prevX][prevY][bestChoice]){
                            bestChoice = k;
                        }
                    }
                    if(bestChoice == 0){
                        policy[prevX][prevY] = UP;
                    }else if(bestChoice == 1){
                        policy[prevX][prevY] = LEFT;
                    }else if(bestChoice == 2){
                        policy[prevX][prevY] = DOWN;
                    }else{
                        policy[prevX][prevY] = RIGHT;
                    }
                }

                prevX = x;
                prevY = y;
                if(world[x][y] == 'G'){
                    break;
                }
            }
            if (i % 10 == 0) {
                randomness = 0.9 / (i / 10);
            }
            rewardWriter.write(finalVal + "\n");
            if(i % 100 == 0){
                policyWriter.write("Episode: " + i + "\n");
                for(int j = 0; j < world.length; j++){
                    for(int k = 0; k < world[j].length; k++){
                        policyWriter.write(policy[j][k]);
                    }
                    policyWriter.write("\n");
                }
                policyWriter.write("\n");
            }
        }
        policyWriter.close();
        rewardWriter.close();
    }

    public int getReward(int x, int y){
        if (world[x][y] == 'H') {
            return -50;
        } else if (world[x][y] == 'G') {
            return 100;
        } else {
            return -1;
        }
    }

    public char randomDir(){
        int dir = (int)(Math.random()*4);
        if(dir == 0){
            return UP;
        }else if(dir == 1){
            return LEFT;
        }else if(dir == 2){
            return DOWN;
        }else{
            return RIGHT;
        }
    }

    public void resetPolicy(){
        for(int i = 0; i < policy.length; i++){
            for(int j = 0; j < policy[i].length; j++){
                if(world[i][j] == 'H'){
                    policy[i][j] = 'H';
                }else if(world[i][j] == 'S'){
                    policy[i][j] = 'S';
                }else{
                    policy[i][j] = 'R';
                }
                policyValues[i][j] = new double[4];
            }
        }
    }
}
