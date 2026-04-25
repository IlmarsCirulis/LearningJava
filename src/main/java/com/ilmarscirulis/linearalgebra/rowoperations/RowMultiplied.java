package com.ilmarscirulis.linearalgebra.rowoperations;

public record RowMultiplied<T>(int row, T multiplier) implements ElementaryRowOperation<T> {
}
