import java.io.*;
import java.nio.ByteBuffer;

public class Medianize {

    public static void main(String args[]){
        File input = new File(args[0]);
        File output = new File(args[1]);
        int numThreads = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        int n = Integer.parseInt(args[4]);
        int width = Integer.parseInt(args[5]);
        int height = Integer.parseInt(args[6]);
        try{
            InputStream reader = new FileInputStream(input);
            byte[] bif = new byte[14];
            reader.read(bif);
            int w, h;
            byte[] inBytes = new byte[2];
            ByteBuffer buff = ByteBuffer.wrap(inBytes);
            reader.read(inBytes);
            w = buff.getShort();
            reader.read(inBytes);
            buff.rewind();
            h = buff.getShort();
            TwoDArray inArr = new TwoDArray(h, w);
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    reader.read(inBytes);
                    buff.rewind();
                    inArr.setArr(i, j, buff.getShort());
                }
            }
            TwoDArray outArr = new TwoDArray(h, w);
            Tiler tiler = new Tiler(inArr, width, height);
            Median[] threads = new Median[numThreads];
            for(int i = 0; i < numThreads; i++){
                threads[i] = new Median(inArr, outArr, m, n, tiler);
                threads[i].start();
            }
            for(int i = 0; i < numThreads; i++){
                threads[i].join();
            }
            OutputStream writer = new FileOutputStream(output);
            writer.write(bif);
            byte[] outBytes = new byte[6+(w*h*2)];
            buff = ByteBuffer.wrap(outBytes);
            buff.putShort((short) w);
            buff.putShort((short) h);
            for(int i = 0; i < h; i++){
                for(int j = 0; j < w; j++){
                    buff.putShort((short) outArr.getElement(i, j));
                }
            }
            writer.write(outBytes);
        }catch (IOException e){
            System.out.println("Error with input file");
            return;
        }catch (InterruptedException e){
            System.out.println("Thread Error");
            return;
        }
    }


}
