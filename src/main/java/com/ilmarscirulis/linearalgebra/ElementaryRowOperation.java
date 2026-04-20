package com.ilmarscirulis.linearalgebra;

public sealed interface ElementaryRowOperation<T> permits RowSwap, RowMultiplied, RowPlusMultipliedRow {
}
