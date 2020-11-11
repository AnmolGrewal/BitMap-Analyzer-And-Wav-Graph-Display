import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class AnalyzeBitMap{

    //Reference: https://itnext.io/bits-to-bitmaps-a-simple-walkthrough-of-bmp-image-format-765dc6857393
    public static void main(String[] args) throws IOException {
        //Dialog Box to open and select file
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        BufferedInputStream bitMap = new BufferedInputStream(new FileInputStream(fc.getSelectedFile()));

        //Skip Unimportant Stuff
        bitMap.skip(18);

        //Get 4 Bytes of Image Width
        int imageWidth = (int)readBytesToNumber(4, bitMap);
        System.out.println("Image Width");
        System.out.println(imageWidth);

        //Get 4 Bytes of Image Height
        int imageHeight = (int)readBytesToNumber(4, bitMap);
        System.out.println("Image Height");
        System.out.println(imageHeight);

        //Skip Unimportant Stuff
        bitMap.skip(28);

        BufferedImage readImage = ImageIO.read(fc.getSelectedFile());

        //Reference: https://en.wikipedia.org/wiki/BMP_file_format#File_structure
        int rowSize = (int)(((24 * imageWidth) + 31)/ 32) * 4;
        System.out.println("Row Size:" + rowSize);

        //Now we get to read the data
        int totalPixels = (int)(Math.abs(imageHeight) * rowSize);
        int pixelData[] = new int[totalPixels];

        //Iterate through all Samples
        Boolean reachedEnd = false;
        for (int i = 0; i < totalPixels; i++) {
//            readBytesToPixel(3, bitMap);
        }

        bitMap.close();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new drawBitMap(readImage));
                frame.setSize(imageWidth,imageHeight);
                frame.setVisible(true);
            }
        });
    }

    //Reference: https://stackoverflow.com/questions/362384/does-java-read-integers-in-little-endian-or-big-endian
    public static long readBytesToNumber(final int size, BufferedInputStream bitMap) throws IOException {
        if(size == 4) {
            long[] temp = new long[size];
            for (int i = 0; i < size; i++) {
                temp[i] = bitMap.read();
            }

            //Shift little endian
            return (temp[0] | (temp[1] << 8) | (temp[2] << 16) | (temp[3] << 24));
        } else {
            byte[] buffer = new byte[2];
            bitMap.read(buffer);

            //Shift little endian
            return (buffer[0]) | ((buffer[1]) << 8);
        }
    }

    public static long readBytesToPixel(final int size, BufferedInputStream bitMap) throws IOException {
        long[] temp = new long[3];
        for (int i = 0; i < size; i++) {
            temp[i] = bitMap.read();
        }

        System.out.println(temp[0]);
        System.out.println(temp[1]);
        System.out.println(temp[2]);

        //Shift little endian
        return (temp[0] | (temp[1] << 8) | (temp[2] << 16));
    }
}
