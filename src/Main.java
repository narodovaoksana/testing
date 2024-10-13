import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static NeuralNetwork nn;
    private static DrawingPanel drawingPanel;
    private static int numberOfLetters = 26; // A-Z
    private static double[][] trainingData; // Training data
    private static double[][] outputData; // Expected output

    public static void main(String[] args) {
        JFrame frame = new JFrame("Letter Recognition");
        drawingPanel = new DrawingPanel(5, 5);
        nn = new NeuralNetwork(25, 10, numberOfLetters); // 5x5 pixels -> 25 input nodes, 10 hidden nodes, 26 output nodes

        // Setup buttons
        JButton trainButton = new JButton("Train");
        JButton testButton = new JButton("Test");
        JButton clearButton = new JButton("Clear");
        JComboBox<String> letterSelector = new JComboBox<>(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"});
        letterSelector.setSelectedItem("A");

        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] drawnPixels = drawingPanel.getPixels();
                int letterIndex = letterSelector.getSelectedIndex();
                if (trainingData == null) {
                    trainingData = new double[1][25];
                    outputData = new double[1][numberOfLetters];
                } else {
                    trainingData = addData(trainingData, drawnPixels);
                    outputData = addOutput(outputData, letterIndex);
                }
                nn.train(trainingData, outputData, 0.1, 1000);
                drawingPanel.clear();
            }
        });

        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] drawnPixels = drawingPanel.getPixels();
                double[] output = nn.feedForward(drawnPixels);
                int predictedLetter = getPredictedLetter(output);
                JOptionPane.showMessageDialog(frame, "Predicted Letter: " + (char) (predictedLetter + 'A'));
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.clear();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(letterSelector);
        buttonPanel.add(trainButton);
        buttonPanel.add(testButton);
        buttonPanel.add(clearButton);

        frame.setLayout(new BorderLayout());
        frame.add(drawingPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static double[][] addData(double[][] existingData, int[] newData) {
        double[][] updatedData = new double[existingData.length + 1][newData.length];
        for (int i = 0; i < existingData.length; i++) {
            updatedData[i] = existingData[i];
        }
        updatedData[existingData.length] = new double[newData.length];
        for (int i = 0; i < newData.length; i++) {
            updatedData[existingData.length][i] = newData[i];
        }
        return updatedData;
    }

    private static double[][] addOutput(double[][] existingOutput, int letterIndex) {
        double[][] updatedOutput = new double[existingOutput.length + 1][numberOfLetters];
        for (int i = 0; i < existingOutput.length; i++) {
            updatedOutput[i] = existingOutput[i];
        }
        updatedOutput[existingOutput.length] = new double[numberOfLetters];
        updatedOutput[existingOutput.length][letterIndex] = 1.0; // Set the correct output to 1
        return updatedOutput;
    }

    private static int getPredictedLetter(double[] output) {
        int maxIndex = 0;
        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
