package com.ilmarscirulis.linearalgebra.rowoperations;

public record RowSwap<T>(int row1, int row2) implements ElementaryRowOperation<T> {
}
