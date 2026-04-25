package com.ilmarscirulis.linearalgebra;

import com.ilmarscirulis.linearalgebra.pivotingrules.FirstNonzeroPivot;
import com.ilmarscirulis.linearalgebra.pivotingrules.RowPivotingRule;
import com.ilmarscirulis.linearalgebra.rowoperations.ElementaryRowOperation;
import com.ilmarscirulis.linearalgebra.rowoperations.RowMultiplied;
import com.ilmarscirulis.linearalgebra.rowoperations.RowPlusMultipliedRow;
import com.ilmarscirulis.linearalgebra.rowoperations.RowSwap;
import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ImmutableMatrix<T> extends Matrix<T> {

    private final ArrayList<ArrayList<T>> contents;

    private ImmutableMatrix(ArrayList<ArrayList<T>> contents, boolean copy) {
        super(contents, copy);
        this.contents = super.contents;
    }

    public ImmutableMatrix(ArrayList<ArrayList<T>> contents) {
        this(contents, true);
    }

    public ImmutableMatrix(T[][] contents) {
        super(contents);
        this.contents = super.contents;
    }

    public ImmutableMatrix<T> deepCopy() {
        return new ImmutableMatrix<>(this.contents);
    }

    public ImmutableMatrix<T> set(int i, int j, T value) {
        ImmutableMatrix<T> newMatrix = this.deepCopy();
        newMatrix.contents.get(i).set(j, value);
        return newMatrix;
    }

    ArrayList<T> getRow(int row) {
        if (row < 0) {
            throw new ArrayIndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row) {
            throw new ArrayIndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        return new ArrayList<>(this.contents.get(row));
    }

    ArrayList<T> getColumn(int col) {
        if (col < 0) {
            throw new ArrayIndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfColumns() <= col) {
            throw new ArrayIndexOutOfBoundsException("There are only " + this.getNumberOfColumns() + " columns in this matrix");
        }
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            result.add(this.get(i, col));
        }
        return result;
    }

    public ImmutableMatrix<T> map(Function<T, T> fun) {
        ArrayList<ArrayList<T>> newContents = new ArrayList<>();
        for (ArrayList<T> row : this.contents) {
            newContents.add(new ArrayList<>(row.stream().map(fun).toList()));
        }
        return (new ImmutableMatrix<>(newContents, false));
    }

    public ImmutableMatrix<T> map2(BiFunction<T, T, T> fun, Matrix<T> other) {
        if (this.getNumberOfRows() != other.getNumberOfRows() || this.getNumberOfColumns() != other.getNumberOfColumns()) {
            throw new IllegalArgumentException("Matrices has to have the same dimensions");
        }
        ArrayList<ArrayList<T>> newContents = new ArrayList<>();
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            ArrayList<T> row = new ArrayList<>();
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                row.add(fun.apply(this.get(i, j), other.get(i, j)));
            }
            newContents.add(row);
        }
        return new ImmutableMatrix<>(newContents, false);
    }


    ImmutableMatrix<T> multiply(Field<T> field, ImmutableMatrix<T> other) {
        if (this.getNumberOfColumns() != other.getNumberOfRows()) {
            throw new IllegalArgumentException("These matrices can't be multiplied because dimensions are wrong");
        }
        ArrayList<ArrayList<T>> newContents = new ArrayList<>();
        for (int row = 0; row < this.getNumberOfRows(); row++) {
            ArrayList<T> newRow = new ArrayList<>();
            for (int col = 0; col < other.getNumberOfColumns(); col++) {
                T temp = field.ZERO;
                for (int i = 0; i < this.getNumberOfColumns(); i++) {
                    T summand = field.mul.apply(this.get(row, i), other.get(i, col));
                    temp = field.add.apply(temp, summand);
                }
                newRow.add(temp);
            }
            newContents.add(newRow);
        }
        return new ImmutableMatrix<>(newContents, false);
    }


    public ImmutableMatrix<T> swapRows(int row1, int row2) {
        if (row1 < 0 || row2 < 0) {
            throw new IndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row1 || this.getNumberOfRows() <= row2) {
            throw new IndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (row1 != row2) {
            ArrayList<T> firstRow = this.contents.get(row1), secondRow = this.contents.get(row2);
            ArrayList<ArrayList<T>> newContents = new ArrayList<>(this.contents);
            newContents.set(row1, secondRow);
            newContents.set(row2, firstRow);
            return new ImmutableMatrix<>(newContents, false);
        } else {
            return this;
        }
    }

    public ImmutableMatrix<T> multiplyRow(Field<T> field, int row, T multiplier) {
        if (row < 0) {
            throw new IndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row) {
            throw new IndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (multiplier.equals(field.ZERO)) {
            throw new IllegalArgumentException("The multiplier must be nonzero");
        }
        ArrayList<ArrayList<T>> newContents = new ArrayList<>(this.contents);
        Function<T, T> mul = (x) -> field.mul.apply(x, multiplier);
        newContents.set(row, new ArrayList<>(newContents.get(row).stream().map(mul).toList()));
        return new ImmutableMatrix<>(newContents, false);
    }

    public ImmutableMatrix<T> addMultipliedRow(Field<T> field, int row1, T multiplier, int row2) {
        if (row1 < 0 || row2 < 0) {
            throw new IndexOutOfBoundsException("Negative indexes for rows aren't allowed");
        }
        if (this.getNumberOfRows() <= row1 || this.getNumberOfRows() <= row2) {
            throw new IndexOutOfBoundsException("There are only " + this.getNumberOfRows() + " rows in this matrix");
        }
        if (row1 == row2) {
            throw new IllegalArgumentException("Row indexes must be different");
        }
        if (multiplier.equals(field.ZERO)) {
            return this;
        }
        ArrayList<ArrayList<T>> newContents = new ArrayList<>(this.contents);
        ArrayList<T> modifiedRow = new ArrayList<>(this.getNumberOfColumns());
        for (int i = 0; i < this.getNumberOfColumns(); i++) {
            modifiedRow.add(field.add.apply(this.get(row1, i), field.mul.apply(this.get(row2, i), multiplier)));
        }
        newContents.set(row1, modifiedRow);
        return new ImmutableMatrix<>(newContents, false);
    }

    public ImmutableMatrix<T> applyElementaryRowOperation(Field<T> field, ElementaryRowOperation<T> op) {
        return switch (op) {
            case RowSwap(int i, int j) -> this.swapRows(i, j);
            case RowMultiplied(int i, T k) -> this.multiplyRow(field, i, k);
            case RowPlusMultipliedRow(int i, T k, int j) -> this.addMultipliedRow(field, i, k, j);
        };
    }

    public ArrayList<ElementaryRowOperation<T>> operationsForRowEchelonForm(Field<T> field, RowPivotingRule<T> rule, boolean toReduce) {
        MutableMatrix<T> mutableMatrix = new MutableMatrix<>(this.deepCopy().contents, false);
        return mutableMatrix.operationsForRowEchelonForm(field, rule, toReduce);
    }

    public ImmutableMatrix<T> toRowEchelonForm(Field<T> field, RowPivotingRule<T> rule, boolean toReduce) {
        ArrayList<ArrayList<T>> temp = this.deepCopy().contents;
        MutableMatrix<T> mutableMatrix = new MutableMatrix<>(temp, false);
        mutableMatrix.operationsForRowEchelonForm(field, new FirstNonzeroPivot<>(), false);
        return (new ImmutableMatrix<>(temp, true));
    }

    public String toString() {
        return this.contents.toString();
    }

}

