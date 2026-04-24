package com.ilmarscirulis.linearalgebra;

import java.util.ArrayList;

public record OperationsOnAugmentedMatrix<T>(ArrayList<ElementaryRowOperation<T>> ops, Matrix<T> result, Matrix<T> augmentation) {
}
