import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {
    private double[][] weightsInputHidden;
    private double[][] weightsHiddenOutput;
    private int hiddenNodes;
    private int outputNodes;
    private Random random;

    public NeuralNetwork(int inputSize, int hiddenSize, int outputSize) {
        random = new Random(1); // for deterministic results
        weightsInputHidden = initializeWeights(inputSize, hiddenSize);
        weightsHiddenOutput = initializeWeights(hiddenSize, outputSize);
        hiddenNodes = hiddenSize;
        outputNodes = outputSize;
    }

    private double[][] initializeWeights(int rows, int cols) {
        double[][] weights = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                weights[i][j] = random.nextDouble() * 2 - 1; // values between -1 and 1
            }
        }
        return weights;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double sigmoidDerivative(double x) {
        return x * (1 - x);
    }

    public double[] feedForward(int[] inputs) {
        double[] hidden = new double[hiddenNodes];
        double[] output = new double[outputNodes];

        // Calculate hidden layer activation
        for (int j = 0; j < hiddenNodes; j++) {
            hidden[j] = 0;
            for (int i = 0; i < inputs.length; i++) {
                hidden[j] += inputs[i] * weightsInputHidden[i][j];
            }
            hidden[j] = sigmoid(hidden[j]);
        }

        // Calculate output layer activation
        for (int j = 0; j < outputNodes; j++) {
            output[j] = 0;
            for (int i = 0; i < hiddenNodes; i++) {
                output[j] += hidden[i] * weightsHiddenOutput[i][j];
            }
            output[j] = sigmoid(output[j]);
        }

        return output;
    }

    public void backpropagation(double[][] inputs, double[][] actualOutput, double learningRate) {
        double[][] predictedOutput = new double[inputs.length][outputNodes];

        // Forward pass
        for (int i = 0; i < inputs.length; i++) {
            predictedOutput[i] = feedForward(toIntArray(inputs[i]));
        }

        double[][] errorOutputLayer = new double[inputs.length][outputNodes];
        double[][] errorHiddenLayer = new double[inputs.length][hiddenNodes];

        // Calculate error at output layer
        for (int i = 0; i < predictedOutput.length; i++) {
            for (int j = 0; j < outputNodes; j++) {
                errorOutputLayer[i][j] = actualOutput[i][j] - predictedOutput[i][j];
            }
        }

        // Update weights for output layer
        for (int i = 0; i < errorOutputLayer.length; i++) {
            for (int j = 0; j < outputNodes; j++) {
                double dOutput = errorOutputLayer[i][j] * sigmoidDerivative(predictedOutput[i][j]);
                for (int k = 0; k < hiddenNodes; k++) {
                    weightsHiddenOutput[k][j] += dOutput * predictedOutput[i][j] * learningRate;
                }
            }
        }

        // Update weights for hidden layer
        for (int i = 0; i < errorOutputLayer.length; i++) {
            for (int j = 0; j < hiddenNodes; j++) {
                errorHiddenLayer[i][j] = 0;
                for (int k = 0; k < outputNodes; k++) {
                    errorHiddenLayer[i][j] += errorOutputLayer[i][k] * weightsHiddenOutput[j][k];
                }
                double dHiddenLayer = errorHiddenLayer[i][j] * sigmoidDerivative(predictedOutput[i][j]);
                for (int k = 0; k < inputs[i].length; k++) {
                    weightsInputHidden[k][j] += inputs[i][k] * dHiddenLayer * learningRate;
                }
            }
        }
    }

    public void train(double[][] inputs, double[][] actualOutput, double learningRate, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            backpropagation(inputs, actualOutput, learningRate);
        }
    }

    private int[] toIntArray(double[] input) {
        int[] intArray = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            intArray[i] = (int) input[i];
        }
        return intArray;
    }
}
