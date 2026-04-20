package com.ilmarscirulis.linearalgebra;

public record RowSwap<T>(int row1, int row2) implements ElementaryRowOperation<T> {
}
