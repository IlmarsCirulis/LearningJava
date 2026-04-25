package com.ilmarscirulis.linearalgebra;

import com.ilmarscirulis.linearalgebra.pivotingrules.RowPivotingRule;
import com.ilmarscirulis.linearalgebra.rowoperations.ElementaryRowOperation;
import com.ilmarscirulis.linearalgebra.rowoperations.RowMultiplied;
import com.ilmarscirulis.linearalgebra.rowoperations.RowPlusMultipliedRow;
import com.ilmarscirulis.linearalgebra.rowoperations.RowSwap;
import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Matrix<T> {

    public ArrayList<ArrayList<T>> contents;

    public Matrix(ArrayList<ArrayList<T>> contents, boolean copy) {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("No empty matrices are allowed");
        }
        for (ArrayList<T> row : contents) {
            if (row.isEmpty()) {
                throw new IllegalArgumentException("No empty rows in matrix are allowed");
            }
        }
        int sizeOfFirstRow = contents.getFirst().size();
        for (ArrayList<T> row : contents) {
            if (row.size() != sizeOfFirstRow) {
                throw new IllegalArgumentException(("Rows of matrix must have the same length"));
            }
        }
        if (copy) {
            this.contents = new ArrayList<>();
            for (ArrayList<T> row : contents) {
                this.contents.add(new ArrayList<>(row));
            }
        } else {
            this.contents = contents;
        }
    }

    public Matrix(T[][] array) {
        ArrayList<ArrayList<T>> temp = new ArrayList<>();
        for (T[] row : array) {
            temp.add(new ArrayList<>(Arrays.asList(row)));
        }
        this(temp, false);
    }

    int getNumberOfRows() {
        return this.contents.size();
    }

    int getNumberOfColumns() {
        return this.contents.getFirst().size();
    }

    T get(int row, int col) {
        if (row < 0) {
            throw new ArrayIndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row) {
            throw new ArrayIndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (col < 0) {
            throw new ArrayIndexOutOfBoundsException("Negative indexes for columns aren't allowed");
        }
        if (this.getNumberOfColumns() <= col) {
            throw new ArrayIndexOutOfBoundsException("There are only " + this.getNumberOfColumns() + " columns in this matrix");
        }
        return this.contents.get(row).get(col);
    }

    abstract Matrix<T> set(int i, int j, T value);

    abstract Matrix<T> map(Function<T, T> fun);

    abstract Matrix<T> map2(BiFunction<T, T, T> fun, Matrix<T> other);

    public Matrix<T> multiply(Field<T> field, T multiplier) {
        return this.map(value -> field.mul.apply(value, multiplier));
    }

    ;

    public Matrix<T> opp(Field<T> field) {
        return this.map(field.opp);
    }

    ;

    public Matrix<T> add(Field<T> field, Matrix<T> other) {
        return this.map2(field.add, other);
    }

    ;

    public Matrix<T> sub(Field<T> field, Matrix<T> other) {
        return this.map2(field.sub, other);
    }

    ;

    abstract Matrix<T> swapRows(int row1, int row2);

    abstract Matrix<T> multiplyRow(Field<T> field, int row, T multiplier);

    abstract Matrix<T> addMultipliedRow(Field<T> field, int row1, T multiplier, int row2);

    Matrix<T> applyElementaryRowOperation(Field<T> field, ElementaryRowOperation<T> op) {
        return switch (op) {
            case RowSwap(int i, int j) -> this.swapRows(i, j);
            case RowMultiplied(int i, T k) -> this.multiplyRow(field, i, k);
            case RowPlusMultipliedRow(int i, T k, int j) -> this.addMultipliedRow(field, i, k, j);
        };
    }

    abstract ArrayList<ElementaryRowOperation<T>> operationsForRowEchelonForm(Field<T> field, RowPivotingRule<T> rule, boolean toReduce);

    abstract Matrix<T> toRowEchelonForm(Field<T> field, RowPivotingRule<T> rule, boolean toReduce);
}
