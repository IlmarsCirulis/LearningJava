package com.ilmarscirulis.linearalgebra;

import com.ilmarscirulis.linearalgebra.rowoperations.ElementaryRowOperation;

import java.util.ArrayList;

public record OperationsOnAugmentedMatrix<T>(ArrayList<ElementaryRowOperation<T>> ops, Matrix<T> result, Matrix<T> augmentation) {
}
