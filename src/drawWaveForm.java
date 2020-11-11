import javax.swing.*;
import java.awt.*;

public class drawWaveForm extends JPanel {

    public int[] sampleData;
    public int maximumValue;
    public int totalSamples;

    public drawWaveForm(int[] sampleData, int maximumValue, int totalSamples) {
        this.sampleData = sampleData;
        this.maximumValue = maximumValue;
        this.totalSamples = totalSamples;
    }

    @Override
    protected void paintComponent(Graphics waveForm) {
        super.paintComponent(waveForm);

        int width = getWidth();
        int height = getHeight();

        int sampleDistance = totalSamples / width;
        int previousX = 0;
        int previousY = 0;
        int y = 0;

        waveForm.setColor(Color.BLACK);
        for (int x = 0; x < width; x++) {
            //Normalize to 1 based on unsigned int
            y = (height / 2) + (int)((sampleData[x * sampleDistance] / 32767.0) * (height/2));
            //Fade Out
            if(x >= (width / 2)) {
                Color color = new Color(0, 0, 0, 0.5f);
                waveForm.setColor(color);
            }
            waveForm.drawLine(previousX, previousY, x, y);
            previousX = x;
            previousY = y;
        }
        waveForm.setColor(Color.BLACK);
        waveForm.drawString("Maximum Value: " + maximumValue, width - 150,height - 85);
        waveForm.drawString("Total Samples: " + totalSamples, width - 150,height - 100);
    }
}
