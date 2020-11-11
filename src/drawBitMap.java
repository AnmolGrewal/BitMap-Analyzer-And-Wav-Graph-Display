import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class drawBitMap extends JPanel {

    BufferedImage bitMapImageDrawn;

    public drawBitMap(BufferedImage bitMapImageDrawn) {
        this.bitMapImageDrawn = bitMapImageDrawn;
    }

    @Override
    protected void paintComponent(Graphics bitMapImage) {
        super.paintComponent(bitMapImage);
        bitMapImage.drawImage(bitMapImageDrawn, 0, 0, null);
    }
}
