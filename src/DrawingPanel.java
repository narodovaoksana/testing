import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;

public class DrawingPanel extends JPanel {
    private boolean[][] pixels;
    private int pixelSize;

    public DrawingPanel(int rows, int cols) {
        pixels = new boolean[rows][cols];
        pixelSize = 30; // Adjust pixel size as needed

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawPixel(e);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                drawPixel(e);
            }
        });
    }

    private void drawPixel(MouseEvent e) {
        int row = e.getY() / pixelSize;
        int col = e.getX() / pixelSize;
        if (row >= 0 && row < pixels.length && col >= 0 && col < pixels[0].length) {
            pixels[row][col] = true;
            repaint();
        }
    }

    public int[] getPixels() {
        int[] pixelArray = new int[pixels.length * pixels[0].length];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixelArray[i * pixels[0].length + j] = pixels[i][j] ? 1 : 0;
            }
        }
        return pixelArray;
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            Arrays.fill(pixels[i], false);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j]) {
                    g.fillRect(j * pixelSize, i * pixelSize, pixelSize, pixelSize);
                }
                g.drawRect(j * pixelSize, i * pixelSize, pixelSize, pixelSize);
            }
        }
    }
}
