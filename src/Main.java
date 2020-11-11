import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Main{

    //Reference: http://soundfile.sapp.org/doc/WaveFormat/
    public static void main(String[] args) throws IOException {
        //Dialog Box to open and select file
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        BufferedInputStream wavFile = new BufferedInputStream(new FileInputStream(fc.getSelectedFile()));

        //First Part of Header is RIFF so its Skipped, Assume always correct
        wavFile.skip(4);

        //Get 4 Bytes of ChunkSize
        long chunkSize = readBytesToNumber(4, wavFile);
        System.out.println("Chunk Size");
        System.out.println(chunkSize);

        //Skip the WAVE in bytes ASCII
        wavFile.skip(4);

        //SKIP FMT Sub chunk
        wavFile.skip(4);

        //Get 4 Bytes of SubChunk1Size
        long SubChunk1Size = readBytesToNumber(4, wavFile);
        System.out.println("SubChunk1Size");
        System.out.println(SubChunk1Size);

        //Read 2 Bytes of Audio Format
        int audioFormat = (int)readBytesToNumber(2, wavFile);
        System.out.println("Audio Format");
        System.out.println(audioFormat);

        //Read 2 Bytes of Number of Channels
        int numberOfChannels = (int)readBytesToNumber(2, wavFile);
        System.out.println("Number of Channels");
        System.out.println(numberOfChannels);

        //Read 4 Bytes of Sample Rate
        long sampleRate = readBytesToNumber(4, wavFile);
        System.out.println("Sample Rate");
        System.out.println(sampleRate);

        //Read 4 Bytes of Byte Rate
        long byteRate = readBytesToNumber(4, wavFile);
        System.out.println("Byte Rate");
        System.out.println(byteRate);

        //Read 2 Bytes of Block Align
        int blockAlign = (int)readBytesToNumber(2, wavFile);
        System.out.println("Block Align");
        System.out.println(blockAlign);

        //Read 2 Bytes of Bits Per Sample
        int bitsPerSample= (int)readBytesToNumber(2, wavFile);
        System.out.println("Bits Per Sample");
        System.out.println(bitsPerSample);

        //Skip Data sub chunk
        wavFile.skip(4);

        //Read 4 Bytes of sub chunk 2 size
        long subChunk2Size = readBytesToNumber(4, wavFile);
        System.out.println("SubChunk2Size");
        System.out.println(subChunk2Size);

        //Total Samples Calculated Here
        final int totalSamples = (int) (subChunk2Size / (bitsPerSample / 8));
        System.out.println("Total Samples:" + totalSamples);

        //Now we get to read the data
        int sampleData[] = new int[totalSamples];

        //Find Maximum Value
        int maximumValue = Integer.MIN_VALUE;

        //Iterate through all Samples
        for (int i = 0; i < totalSamples; i++) {
            if (bitsPerSample == 16) {
                sampleData[i] = (int) readBytesToNumber(2, wavFile);
            } else {
                sampleData[i] = wavFile.read();
            }
            if(sampleData[i] > maximumValue) {
                maximumValue = sampleData[i];
            }
        }

        System.out.println("Maximum Value: " + maximumValue);

        wavFile.close();

        final int finalMaximumValue = maximumValue;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new drawWaveForm(sampleData, finalMaximumValue, totalSamples));
                frame.setSize(1250,750);
                frame.setVisible(true);
            }
        });
    }

    //Reference: https://stackoverflow.com/questions/362384/does-java-read-integers-in-little-endian-or-big-endian
    public static long readBytesToNumber(final int size, BufferedInputStream wavFile) throws IOException {
        if(size == 4) {
            long[] temp = new long[size];
            for (int i = 0; i < size; i++) {
                temp[i] = wavFile.read();
            }

            //Shift little endian
            return (temp[0] | (temp[1] << 8) | (temp[2] << 16) | (temp[3] << 24));
        } else {
            byte[] buffer = new byte[2];
            wavFile.read(buffer);

            //Shift little endian
            return (buffer[0]) | ((buffer[1]) << 8);
        }
    }
}
