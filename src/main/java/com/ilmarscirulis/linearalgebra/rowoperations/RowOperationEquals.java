package com.ilmarscirulis.linearalgebra.rowoperations;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class RowOperationEquals<T> {
    private final BiFunction<T, T, Boolean> cmp;

    public RowOperationEquals(BiFunction<T, T, Boolean> cmp) {
        this.cmp = cmp;
    }

    public boolean run(ElementaryRowOperation<T> op1, ElementaryRowOperation<T> op2) {
        if (Objects.requireNonNull(op1) instanceof RowSwap<T>(int i, int j) &&
                Objects.requireNonNull(op2) instanceof RowSwap<T>(int i0, int j0)) {
            return (i == i0 && j == j0) || (i == j0 && j == i0);
        }
        if (Objects.requireNonNull(op1) instanceof RowMultiplied<T>(int row, T k) &&
                Objects.requireNonNull(op2) instanceof RowMultiplied<T>(int row0, T k0)) {
            return (row == row0 && this.cmp.apply(k, k0));
        }
        if (Objects.requireNonNull(op1) instanceof RowPlusMultipliedRow<T>(int i, T k, int j) &&
                Objects.requireNonNull(op2) instanceof RowPlusMultipliedRow<T>(int i0, T k0, int j0)) {
            return (i == i0 && this.cmp.apply(k, k0) && j == j0);
        }
        return false;
    }

    public boolean run(List<ElementaryRowOperation<T>> ops1, List<ElementaryRowOperation<T>> ops2) {
        if (ops1.size() != ops2.size()) {
            return false;
        }
        for (int i = 0; i < ops1.size(); i++) {
            if (!this.run(ops1.get(i), ops2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
