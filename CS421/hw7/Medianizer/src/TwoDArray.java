
public class TwoDArray {
    private int[] arr;
    private int height;
    private int width;

    public TwoDArray(int height, int width){
        arr = new int[height*width];
        this.height = height;
        this.width = width;
    }

    public int getElement(int x, int y) {
        if(x < 0 || y < 0 || x >= height || y >= width){
            return 0;
        }
        return arr[(x*width)+y];
    }

    public void setArr(int x, int y, int elm) {
        arr[(x*width)+y] = elm;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
