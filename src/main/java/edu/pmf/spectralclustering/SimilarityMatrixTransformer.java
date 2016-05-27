package edu.pmf.spectralclustering;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public interface SimilarityMatrixTransformer {
    DenseDoubleMatrix2D transform(DenseDoubleMatrix2D similarityMatrix);
}
