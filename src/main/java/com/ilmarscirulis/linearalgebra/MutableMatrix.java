package com.ilmarscirulis.linearalgebra;

import com.ilmarscirulis.linearalgebra.pivotingrules.RowPivotingRule;
import com.ilmarscirulis.linearalgebra.rowoperations.*;
import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MutableMatrix<T> extends Matrix<T> {

    public ArrayList<ArrayList<T>> contents;

    public MutableMatrix(ArrayList<ArrayList<T>> contents, boolean copy) {
        super(contents, copy);
        this.contents = super.contents;
    }

    public MutableMatrix(T[][] contents) {
        super(contents);
        this.contents = super.contents;
    }

    public MutableMatrix<T> set(int i, int j, T value) {
        this.contents.get(i).set(j, value);
        return this;
    }

    public MutableMatrix<T> map(Function<T, T> fun) {
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.set(i, j, fun.apply(this.get(i, j)));
            }
        }
        return this;
    }

    public MutableMatrix<T> map2(BiFunction<T, T, T> fun, Matrix<T> other) {
        if (this.getNumberOfRows() != other.getNumberOfRows() || this.getNumberOfColumns() != other.getNumberOfColumns()) {
            throw new IllegalArgumentException("Matrices has to have the same dimensions");
        }
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.set(i, j, fun.apply(this.get(i, j), other.get(i, j)));
            }
        }
        return this;
    }


    public MutableMatrix<T> swapRows(int row1, int row2) {
        if (row1 < 0 || row2 < 0) {
            throw new IndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row1 || this.getNumberOfRows() <= row2) {
            throw new IndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (row1 != row2) {
            ArrayList<T> firstRow = this.contents.get(row1), secondRow = this.contents.get(row2);
            this.contents.set(row1, secondRow);
            this.contents.set(row2, firstRow);
        }
        return this;
    }


    MutableMatrix<T> multiply(Field<T> field, Matrix<T> other) {
        if (this.getNumberOfColumns() != other.getNumberOfRows()) {
            throw new IllegalArgumentException("These matrices can't be multiplied because dimensions are wrong");
        }
        ArrayList<ArrayList<T>> temp = this.contents;
        new ArrayList<>(this.getNumberOfRows());
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            temp.add(new ArrayList<>(this.contents.get(i)));
        }

        for (int row = 0; row < this.getNumberOfRows(); row++) {
            for (int col = 0; col < this.getNumberOfColumns(); col++) {
                T value = field.ZERO;
                for (int i = 0; i < this.getNumberOfColumns(); i++) {
                    T summand = field.mul.apply(temp.get(row).get(i), other.get(i, col));
                    value = field.add.apply(value, summand);
                }
                this.set(row, col, value);
            }
        }
        return this;
    }

    MutableMatrix<T> multiplyRow(Field<T> field, int row, T multiplier) {
        if (row < 0) {
            throw new IndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row) {
            throw new IndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (multiplier.equals(field.ZERO)) {
            throw new IllegalArgumentException("The multiplier must be nonzero");
        }
        ArrayList<T> newRow = new ArrayList<>(this.getNumberOfColumns());
        for (T x : this.contents.get(row)) {
            newRow.add(field.mul.apply(x, multiplier));
        }
        this.contents.set(row, newRow);
        return this;
    }

    MutableMatrix<T> addMultipliedRow(Field<T> field, int row1, T multiplier, int row2) {
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
                T multipliedValue = field.mul.apply(this.get(row2, i), multiplier);
                this.set(row1, i, field.add.apply(this.get(row1, i), multipliedValue));
            }
        }
        return this;
    }

    public ArrayList<ElementaryRowOperation<T>> operationsForRowEchelonForm(Field<T> field, RowPivotingRule<T> rule, boolean toReduce) {
        ArrayList<ElementaryRowOperation<T>> ops = new ArrayList<>();
        int pivotRow = 0, pivotColumn = 0;
        while (pivotRow < this.getNumberOfRows() && pivotColumn < this.getNumberOfColumns()) {
            int chosenRow = rule.chooseRow(field, this.contents, pivotRow, pivotColumn);
            if (!this.get(chosenRow, pivotColumn).equals(field.ZERO)) {
                if (chosenRow != pivotRow) {
                    this.swapRows(pivotRow, chosenRow);
                    ops.add(new RowSwap<>(pivotRow, chosenRow));
                }
                if (toReduce) {
                    T pivotValue = this.get(pivotRow, pivotColumn);
                    if (!pivotValue.equals(field.ZERO) && !pivotValue.equals(field.ONE)) {
                        T multiplier = field.inv.apply(pivotValue);
                        this.multiplyRow(field, pivotRow, multiplier);
                        ops.add(new RowMultiplied<>(pivotRow, multiplier));
                    }
                }
                for (int row = pivotRow + 1; row < this.getNumberOfRows(); row++) {
                    if (!this.get(row, pivotColumn).equals(field.ZERO)) {
                        T multiplier = field.opp.apply(field.div.apply(this.get(row, pivotColumn), this.get(pivotRow, pivotColumn)));
                        this.addMultipliedRow(field, row, multiplier, pivotRow);
                        ops.add(new RowPlusMultipliedRow<>(row, multiplier, pivotRow));
                    }
                }
                pivotRow += 1;
            }
            pivotColumn += 1;
        }
        return ops;
    }

    Matrix<T> toRowEchelonForm(Field<T> field, RowPivotingRule<T> rule, boolean toReduce) {
        this.operationsForRowEchelonForm(field, rule, toReduce);
        return this;
    }

    @Override
    public String toString() {
        return "MutableMatrix{" +
                "contents=" + this.contents +
                '}';
    }
}
