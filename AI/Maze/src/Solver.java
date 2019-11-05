import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Solver implements MazeSolver {

    int[][] maze;
    Point startPoint;
    ArrayList<Point> items;

    public static void main(String args[]){
        Solver solver = new Solver();
        MazeWindow window = new MazeWindow(solver);
        window.makeWindow();
    }

    public Solver(){
        items = new ArrayList<>();
    }

    @Override
    public int[][] getMaze(File mazeFile) {
        items.removeAll(items);
        try{
            Scanner input = new Scanner(mazeFile);
            int length = input.nextInt();
            int width = input.nextInt();
            maze = new int[length][width];
            for(int x = 0; x < length; x++){
                for(int y = 0; y < width; y++){
                    maze[x][y] = input.nextInt();
                    if(maze[x][y] == 1){
                        startPoint = new Point(x,y,0);
                    }else if(maze[x][y] > 1){
                        items.add(new Point(x,y,maze[x][y]));
                    }
                }
            }
            items.sort(Point::compareTo);
            return maze;
        }catch (IOException e){
            System.out.println("Error with File.");
        }
        return new int[0][];
    }

    @Override
    public ArrayList<String> getSolution(int capacity) {
        ArrayList<Point> getItems = new ArrayList<>();
        //Reward function is reducing size of getItems since every item in getItems must be found to complete solution
        //getItems is a subset of items determined by capacity after items has been sorted in reverse order of value
        for(int i = 0; i < capacity; i++){
            if(i >= items.size()){
                break;
            }
            getItems.add(items.get(i));
        }
        ArrayList<String> solution = getBestSolution(startPoint, getItems);
        return solution;
    }

    public ArrayList<String> getBestSolution(Point curPoint, ArrayList<Point> getItems) {
        //States are a combination of itemsRemaining in getItems and curPoint showing where you last picked up an item.
        int[][] pulseGrid = new int[maze.length][maze[0].length];
        ArrayList<Point> searchEdge = new ArrayList<>();
        pulseGrid[curPoint.getX()][curPoint.getY()] = 1;
        searchEdge.add(new Point(curPoint.getX(),curPoint.getY(),1));
        //Transitions are determined by finding the shortest route to the nearest item
        boolean done = false;
        int gridSpotsFound = 0;
        while (!done){
            ArrayList<Point> nextSearchEdge = new ArrayList<>();
            for(Point cur : searchEdge){
                int curX = cur.getX();
                int curY = cur.getY();
                if(curX - 1 >= 0 && maze[curX-1][curY] != -1 && pulseGrid[curX-1][curY] == 0){
                    Point temp = new Point(curX-1,curY,cur.getVal()+1);
                    Point answer = itemAtPoint(temp, getItems);
                    pulseGrid[curX-1][curY] = cur.getVal()+1;
                    if(answer != null){
                        gridSpotsFound++;
                        if(gridSpotsFound == getItems.size()){
                            done = true;
                        }
                    }
                    nextSearchEdge.add(temp);
                }
                if(curY - 1 >= 0 && maze[curX][curY-1] != -1 && pulseGrid[curX][curY-1] == 0){
                    Point temp = new Point(curX,curY-1,cur.getVal()+1);
                    Point answer = itemAtPoint(temp, getItems);
                    pulseGrid[curX][curY-1] = cur.getVal()+1;
                    if(answer != null){
                        gridSpotsFound++;
                        if(gridSpotsFound == getItems.size()){
                            done = true;
                        }
                    }
                    nextSearchEdge.add(temp);
                }
                if(curX + 1 < maze.length && maze[curX+1][curY] != -1 && pulseGrid[curX+1][curY] == 0){
                    Point temp = new Point(curX+1,curY,cur.getVal()+1);
                    Point answer = itemAtPoint(temp, getItems);
                    pulseGrid[curX+1][curY] = cur.getVal()+1;
                    if(answer != null){
                        gridSpotsFound++;
                        if(gridSpotsFound == getItems.size()){
                            done = true;
                        }
                    }
                    nextSearchEdge.add(temp);
                }
                if(curY + 1 < maze[curX].length && maze[curX][curY+1] != -1 && pulseGrid[curX][curY+1] == 0){
                    Point temp = new Point(curX,curY+1,cur.getVal()+1);
                    Point answer = itemAtPoint(temp, getItems);
                    pulseGrid[curX][curY+1] = cur.getVal()+1;
                    if(answer != null){
                        gridSpotsFound++;
                        if(gridSpotsFound == getItems.size()){
                            done = true;
                        }
                    }
                    nextSearchEdge.add(temp);
                }
            }
            searchEdge = nextSearchEdge;
        }

        //Get Path Based on endPoint and pulseGrid
        //Updates solution when transitioning to new state
        ArrayList<String> solution = null;
        for(Point curItem : getItems){
            ArrayList<String> possibleSolution = new ArrayList<>();
            int curX = curItem.getX();
            int curY = curItem.getY();
            int dist = pulseGrid[curX][curY];
            for(int i = 1; i < dist; i++){
                int nextVal = pulseGrid[curX][curY]-1;
                if(curX-1 >= 0 && pulseGrid[curX-1][curY] == nextVal){
                    curX = curX-1;
                    possibleSolution.add(0,MazeWindow.DOWN);
                }else if(curY-1 >= 0 && pulseGrid[curX][curY-1] == nextVal){
                    curY = curY-1;
                    possibleSolution.add(0,MazeWindow.RIGHT);
                }else if(curX+1 < pulseGrid.length && pulseGrid[curX+1][curY] == nextVal){
                    curX = curX+1;
                    possibleSolution.add(0,MazeWindow.UP);
                }else if(curY+1 < pulseGrid[curX].length && pulseGrid[curX][curY+1] == nextVal){
                    curY = curY+1;
                    possibleSolution.add(0,MazeWindow.LEFT);
                }
            }
            if(getItems.size() > 1){
                ArrayList<Point> nextSecItems = new ArrayList<>();
                nextSecItems.addAll(getItems);
                nextSecItems.remove(curItem);
                possibleSolution.addAll(getBestSolution(curItem, nextSecItems));
            }
            if(solution == null || solution.size() > possibleSolution.size()){
                solution = possibleSolution;
            }
        }
        return solution;
    }

    public Point itemAtPoint(Point check, ArrayList<Point> checkItems){
        for(Point cur : checkItems){
            if(check.getX() == cur.getX() && check.getY() == cur.getY()){
                return cur;
            }
        }
        return null;
    }

    private class Point implements Comparable<Point>{
        private int x;
        private int y;
        private int value;

        public Point(int x, int y, int value){
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public int getX(){
            return x;
        }

        public int getY(){
            return y;
        }

        public int getVal(){
            return value;
        }

        @Override
        public int compareTo(Point o) {
            if(value > o.getVal()){
                return -1;
            }else if(value < o.getVal()){
                return 1;
            }
            return 0;
        }
    }
}
