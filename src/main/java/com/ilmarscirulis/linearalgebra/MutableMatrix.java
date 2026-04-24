package com.ilmarscirulis.linearalgebra;

import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MutableMatrix<T> {

    private final T[][] contents;

    public MutableMatrix(T[][] contents) {
        if (contents.length == 0) {
            throw new IllegalArgumentException("No empty matrices are allowed");
        }
        for (T[] row : contents) {
            if (row.length == 0) {
                throw new IllegalArgumentException("No empty rows in matrix are allowwed");
            }
        }
        int sizeOfFirstRow = contents[0].length;
        for (T[] row : contents) {
            if (row.length != sizeOfFirstRow) {
                throw new IllegalArgumentException(("Rows of matrix must have the same length"));
            }
        }
        this.contents = contents;
    }

    int getNumberOfRows() {
        return this.contents.length;
    }

    int getNumberOfColumns() {
        return this.contents[0].length;
    }

    private void map(Function<T, T> fun) {
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.contents[i][j] = fun.apply(this.contents[i][j]);
            }
        }
    }

    private void map2(MutableMatrix<T> other, BiFunction<T, T, T> fun) {
        if (this.getNumberOfRows() != other.getNumberOfRows() || this.getNumberOfColumns() != other.getNumberOfColumns()) {
            throw new IllegalArgumentException("Matrices has to have the same dimensions");
        }
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.contents[i][j] = fun.apply(this.contents[i][j], other.contents[i][j]);
            }
        }
    }

    @Override
    public String toString() {
        return Arrays.deepToString(this.contents);
    }

    public void swapRows(int row1, int row2) {
        if (row1 < 0 || row2 < 0) {
            throw new IndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row1 || this.getNumberOfRows() <= row2) {
            throw new IndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (row1 != row2) {
            T[] temp = this.contents[row1];
            this.contents[row1] = this.contents[row2];
            this.contents[row2] = temp;
        }
    }


    public void multiply(Field<T> field, T multiplier) {
        this.map(value -> field.mul.apply(value, multiplier));
    }

    public void opp(Field<T> field) {
        this.map(field.opp);
    }

    public void add(Field<T> field, MutableMatrix<T> other) {
        this.map2(other, field.add);
    }

    public void sub(Field<T> field, MutableMatrix<T> other) {
        this.map2(other, field.sub);
    }

    void multiply(Field<T> field, MutableMatrix<T> other) {
        if (this.getNumberOfColumns() != other.getNumberOfRows()) {
            throw new IllegalArgumentException("These matrices can't be multiplied because dimensions are wrong");
        }
        ArrayList<ArrayList<T>> temp = new ArrayList<>(this.getNumberOfRows());
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            temp.add(new ArrayList<>(Arrays.stream(this.contents[i]).toList()));
        }

        for (int row = 0; row < this.getNumberOfRows(); row++) {
            for (int col = 0; col < this.getNumberOfColumns(); col++) {
                T value = field.ZERO;
                for (int i = 0; i < this.getNumberOfColumns(); i++) {
                    T summand = field.mul.apply(temp.get(row).get(i), other.contents[i][col]);
                    value = field.add.apply(value, summand);
                }
                this.contents[row][col] = value;
            }
        }
    }


    void multiplyRow(Field<T> field, int row, T multiplier) {
        if (row < 0) {
            throw new IndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row) {
            throw new IndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (multiplier.equals(field.ZERO)) {
            throw new IllegalArgumentException("The multiplier must be nonzero");
        }
        for (int i = 0; i < this.getNumberOfColumns(); i++) {
            this.contents[row][i] = field.mul.apply(this.contents[row][i], multiplier);
        }
    }

    void addMultipliedRow(Field<T> field, int row1, T multiplier, int row2) {
        if (row1 < 0 || row2 < 0) {
            throw new IndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row1 || this.getNumberOfRows() <= row2) {
            throw new IndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (row1 == row2) {
            throw new IllegalArgumentException("Row indexes must be different");
        }
        if (!multiplier.equals(field.ZERO)) {
            for (int i = 0; i < this.getNumberOfColumns(); i++) {
                T multipliedValue = field.mul.apply(this.contents[row2][i], multiplier);
                this.contents[row1][i] = field.add.apply(this.contents[row1][i], multipliedValue);
            }
        }
    }

    void applyElementaryRowOperation(Field<T> field, ElementaryRowOperation<T> op) {
        switch (op) {
            case RowSwap(int i, int j) -> this.swapRows(i, j);
            case RowMultiplied(int i, T k) -> this.multiplyRow(field, i, k);
            case RowPlusMultipliedRow(int i, T k, int j) -> this.addMultipliedRow(field, i, k, j);
        };
    }

}
