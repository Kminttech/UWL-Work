import java.awt.*;
import java.util.Arrays;

public class Median extends Thread {
    public TwoDArray src;
    public TwoDArray dest;
    int n, m;
    Tiler tiler;
    private int numberOfElementsProcessed;

    public Median(TwoDArray src, TwoDArray dest, int m, int n, Tiler tiler){
        this.src = src;
        this.dest = dest;
        this.m = m;
        this.n = n;
        this.tiler = tiler;
        numberOfElementsProcessed = 0;
    }

    @Override
    public void run() {
        Rectangle curTile = tiler.nextTile();
        while (curTile != null){
            for(int i = curTile.x; i < (int)curTile.getMaxX(); i++){
                for(int j = curTile.y; j < (int)curTile.getMaxY(); j++){
                    int[] arr = new int[n*m];
                    for(int x = 0; x < m; x++){
                        for(int y = 0; y < n; y++){
                            arr[(x*n)+y] = src.getElement(i-(x-(m/2)), j-(y-(n/2)));
                        }
                    }
                    Arrays.sort(arr);
                    dest.setArr(i,j,arr[m*n/2]);
                    numberOfElementsProcessed++;
                }
            }
            curTile = tiler.nextTile();
        }
    }

    public int getNumberOfElementsProcessed(){
        return numberOfElementsProcessed;
    }
}
