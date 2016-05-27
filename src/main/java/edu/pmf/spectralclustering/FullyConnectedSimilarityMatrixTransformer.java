package edu.pmf.spectralclustering;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class FullyConnectedSimilarityMatrixTransformer implements SimilarityMatrixTransformer {

    @Override
    public DenseDoubleMatrix2D transform(DenseDoubleMatrix2D similarityMatrix) {
        return (DenseDoubleMatrix2D) similarityMatrix.copy();
    }

}
