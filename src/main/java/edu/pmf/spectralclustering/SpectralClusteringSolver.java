package edu.pmf.spectralclustering;

import java.util.Arrays;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;

public class SpectralClusteringSolver {
    public static DenseDoubleMatrix2D getLaplacian(DenseDoubleMatrix2D weightedMatrix) {
        DenseDoubleMatrix2D laplacian = new DenseDoubleMatrix2D(weightedMatrix.rows(), weightedMatrix.columns());
        for (int r = 0; r < laplacian.rows(); r++) {
            double sumRow = 0.0;
            for (int c = 0; c < laplacian.columns(); c++) {
                if (r != c) {
                    double wrc = weightedMatrix.getQuick(r, c);
                    sumRow += wrc;
                    laplacian.setQuick(r, c, -wrc);
                }
            }
            laplacian.setQuick(r, r, sumRow);
        }
        return laplacian;
    }

    /**
     * Finds the clusters using algorithm version 1.
     * 
     * @param similarityMatrix
     *            the similarity matrix
     * @param numCluster
     *            the number of clusters to construct
     */
    public static void SolveSpectralClustering(DenseDoubleMatrix2D similarityMatrix, int numCluster,
            SimilarityMatrixTransformer transformer) {
        for (int r = 0; r < similarityMatrix.rows(); r++) {
            for (int c = 0; c < similarityMatrix.columns(); c++) {
                System.out.print(similarityMatrix.getQuick(r, c) + " ");
            }
            System.out.println();
        }

        DenseDoubleMatrix2D weightedMatrix = transformer.transform(similarityMatrix);
        DenseDoubleMatrix2D laplacianMatrix = getLaplacian(weightedMatrix);
        EigenvalueDecomposition eigenDecomposition = new EigenvalueDecomposition(laplacianMatrix);
        DoubleMatrix2D eigenVectors = eigenDecomposition.getV();
        DoubleMatrix1D eigenValues = eigenDecomposition.getRealEigenvalues();
        System.out.println("EigenValues:");
        EigenValueSortHelper[] eigenvaluesToSort = new EigenValueSortHelper[eigenValues.size()];
        for (int iEigen = 0; iEigen < eigenValues.size(); iEigen++) {
            eigenvaluesToSort[iEigen] = new EigenValueSortHelper(eigenValues.getQuick(iEigen), iEigen);
            System.out.print(eigenValues.getQuick(iEigen) + " ");
        }
        System.out.println();
        Arrays.sort(eigenvaluesToSort);

        for (int iEigen = 0; iEigen < eigenValues.size(); iEigen++) {
            System.out.print(eigenvaluesToSort[iEigen].getIndex() + ":"
                    + eigenValues.getQuick(eigenvaluesToSort[iEigen].getIndex()) + " ");
        }
        System.out.println();
        System.out.println("EigenVectors:");
        for (int iEigen = 0; iEigen < eigenVectors.columns(); iEigen++) {
            System.out.print("[");
            for (int r = 0; r < eigenVectors.rows(); r++) {
                System.out.print(eigenVectors.getQuick(r, eigenvaluesToSort[iEigen].getIndex()) + " ");
            }
            System.out.println("]");
        }
    }

    public static void SolveSpectralClustering(DenseDoubleMatrix2D similarityMatrix, int numCluster) {
        SolveSpectralClustering(similarityMatrix, numCluster, new FullyConnectedSimilarityMatrixTransformer());
    }

    private static class EigenValueSortHelper implements Comparable<EigenValueSortHelper> {
        private double eigenvalue;
        private int    index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public EigenValueSortHelper(double eigenvalue, int index) {
            this.eigenvalue = eigenvalue;
            this.index = index;
        }

        @Override
        public int compareTo(EigenValueSortHelper o) {
            if (o instanceof EigenValueSortHelper) {
                EigenValueSortHelper other = (EigenValueSortHelper) o;

                if (this.eigenvalue < other.eigenvalue)
                    return -1;
                if (this.eigenvalue > other.eigenvalue)
                    return 1;
                return 0;
            }
            return -1;
        }
    }
}
