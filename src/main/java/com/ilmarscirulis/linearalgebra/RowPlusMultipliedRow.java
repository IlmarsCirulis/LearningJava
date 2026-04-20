package com.ilmarscirulis.linearalgebra;

public record RowPlusMultipliedRow<T>(int rowToChange, T multiplier,
                                      int rowToMultiply) implements ElementaryRowOperation {
}
