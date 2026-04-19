package com.ilmarscirulis.linearalgebra;

public record RowMultiplied<T>(int row, T multiplier) implements ElementaryRowOperation {
}
