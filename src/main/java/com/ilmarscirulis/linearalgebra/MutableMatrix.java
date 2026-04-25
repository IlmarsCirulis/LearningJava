package com.ilmarscirulis.linearalgebra;

import com.ilmarscirulis.linearalgebra.pivotingrules.RowPivotingRule;
import com.ilmarscirulis.linearalgebra.rowoperations.*;
import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MutableMatrix<T> {

    private final ArrayList<ArrayList<T>> contents;

    public MutableMatrix(ArrayList<ArrayList<T>> contents) {
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
        this.contents = contents;
    }

    public MutableMatrix(T[][] contents) {
        ArrayList<ArrayList<T>> temp = new ArrayList<>();
        for (T[] row : contents) {
            temp.add(new ArrayList<>(Arrays.asList(row)));
        }
        this(temp);
    }

    int getNumberOfRows() {
        return this.contents.size();
    }

    int getNumberOfColumns() {
        return this.contents.getFirst().size();
    }

    T get(int i, int j) {
        return this.contents.get(i).get(j);
    }

    void set(int i, int j, T value) {
        this.contents.get(i).set(j, value);
    }

    private void map(Function<T, T> fun) {
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.set(i, j, fun.apply(this.get(i, j)));
            }
        }
    }

    private void map2(MutableMatrix<T> other, BiFunction<T, T, T> fun) {
        if (this.getNumberOfRows() != other.getNumberOfRows() || this.getNumberOfColumns() != other.getNumberOfColumns()) {
            throw new IllegalArgumentException("Matrices has to have the same dimensions");
        }
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                this.set(i, j, fun.apply(this.get(i, j), other.get(i, j)));
            }
        }
    }

    @Override
    public String toString() {
        return this.contents.toString();
    }

    public void swapRows(int row1, int row2) {
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
        ArrayList<T> newRow = new ArrayList<>(this.getNumberOfColumns());
        for (T x : this.contents.get(row)) {
            newRow.add(field.mul.apply(x, multiplier));
        }
        this.contents.set(row, newRow);
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
                T multipliedValue = field.mul.apply(this.get(row2, i), multiplier);
                this.set(row1, i, field.add.apply(this.get(row1, i), multipliedValue));
            }
        }
    }

    void applyElementaryRowOperation(Field<T> field, ElementaryRowOperation<T> op) {
        switch (op) {
            case RowSwap(int i, int j) -> this.swapRows(i, j);
            case RowMultiplied(int i, T k) -> this.multiplyRow(field, i, k);
            case RowPlusMultipliedRow(int i, T k, int j) -> this.addMultipliedRow(field, i, k, j);
        }
    }

    ArrayList<ElementaryRowOperation<T>> toRowEchelonForm(Field<T> field, RowPivotingRule<T> rule, boolean toReduce) {
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

}
