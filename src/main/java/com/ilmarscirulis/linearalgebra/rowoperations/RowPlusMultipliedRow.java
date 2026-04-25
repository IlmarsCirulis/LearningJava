package com.ilmarscirulis.linearalgebra.rowoperations;

public record RowPlusMultipliedRow<T>(int rowToChange, T multiplier,
                                      int rowToMultiply) implements ElementaryRowOperation<T> {
}
