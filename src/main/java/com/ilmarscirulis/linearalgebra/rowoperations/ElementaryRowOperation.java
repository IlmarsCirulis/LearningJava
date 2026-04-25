package com.ilmarscirulis.linearalgebra.rowoperations;

public sealed interface ElementaryRowOperation<T> permits RowSwap, RowMultiplied, RowPlusMultipliedRow {
}
