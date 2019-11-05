import java.awt.Rectangle;

public class Tiler {
    private TwoDArray arr;
    private int width;
    private int height;
    private int nextX;
    private int nextY;

    public Tiler(TwoDArray arr, int width, int height) {
        this.arr = arr;
        this.width = height;
        this.height = width;
        nextX = 0;
        nextY = 0;
    }

    public synchronized Rectangle nextTile() {
        if(nextY > (arr.getWidth()/ height)){
            return null;
        }
        Rectangle rec = null;
        if((nextX+1)* width > arr.getHeight() && (nextY+1)* height > arr.getWidth()){
            rec = new Rectangle(nextX* width, nextY* height, arr.getHeight()-(nextX* width), arr.getWidth()-(nextY* height));
        }else if((nextX+1)* width > arr.getHeight()){
            rec = new Rectangle(nextX* width, nextY* height, arr.getHeight()-(nextX* width), height);
        }else if((nextY+1)* height > arr.getWidth()){
            rec = new Rectangle(nextX* width, nextY* height, width, arr.getWidth()-(nextY* height));
        }else {
            rec = new Rectangle(nextX* width, nextY* height, width, height);
        }
        nextX++;
        if(nextX > (arr.getHeight()/ width)){
            nextX = 0;
            nextY++;
        }
        return rec;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
