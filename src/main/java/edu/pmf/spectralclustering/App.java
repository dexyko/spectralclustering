package edu.pmf.spectralclustering;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        double[][] vals = { { 0, 4, 10 }, { 3, 0, 3 }, { 8, 15, 0 } };
        DenseDoubleMatrix2D mat = new DenseDoubleMatrix2D(vals);
        SpectralClusteringSolver.SolveSpectralClustering(mat, 3);

    }
}
