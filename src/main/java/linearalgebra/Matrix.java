package linearalgebra;

import structures.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Matrix<T> {
    private final ArrayList<ArrayList<T>> contents;

    private Matrix(ArrayList<ArrayList<T>> contents, boolean copy) {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("No empty matrices are allowed");
        }
        for (ArrayList<T> row : contents) {
            if (row.isEmpty()) {
                throw new IllegalArgumentException("No empty rows in matrix are allowed");
            }
        }
        int sizeOfFirstRow = contents.getFirst().size();
        if (contents.stream().anyMatch(r -> r.size() != sizeOfFirstRow)) {
            throw new IllegalArgumentException("Rows of matrix must have the same length");
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

    public Matrix(ArrayList<ArrayList<T>> contents) {
        this(contents, true);
    }

    public Matrix(T[][] contents) {
        ArrayList<ArrayList<T>> arrayList = new ArrayList<>();
        for (T[] row : contents) {
            arrayList.add(new ArrayList<>(List.of(row)));
        }
        this(arrayList, false);
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
            result.add(this.contents.get(i).get(col));
        }
        return result;
    }

    private Matrix<T> map(Function<T, T> fun) {
        ArrayList<ArrayList<T>> newContents = new ArrayList<>();
        for (ArrayList<T> row : this.contents) {
            ArrayList<T> newRow = new ArrayList<>();
            for (T value : row) {
                newRow.add(fun.apply(value));
            }
            newContents.add(newRow);
        }
        return new Matrix<>(newContents, false);
    }

    private Matrix<T> map2(Matrix<T> other, BiFunction<T, T, T> fun) {
        if (this.getNumberOfRows() != other.getNumberOfRows() || this.getNumberOfColumns() != other.getNumberOfColumns()) {
            throw new IllegalArgumentException("Matrices has to have same dimensions");
        }
        ArrayList<ArrayList<T>> newContents = new ArrayList<>();
        for (int row = 0; row < this.getNumberOfRows(); row++) {
            ArrayList<T> temp = new ArrayList<>();
            for (int col = 0; col < this.getNumberOfColumns(); col++) {
                temp.add(fun.apply(this.contents.get(row).get(col), other.contents.get(row).get(col)));
            }
            newContents.add(temp);
        }
        return new Matrix<>(newContents, false);
    }

    @Override
    public String toString() {
        StringJoiner s = new StringJoiner(", ", "[", "]");
        for (ArrayList<T> row : this.contents) {
            StringJoiner t = new StringJoiner(", ", "[", "]");
            for (T value : row) {
                t.add(value.toString());
            }
            s.add(t.toString());
        }
        return s.toString();
    }

    // Arithmetic operations with matrices

    public Matrix<T> multiply(Field<T> field, T multiplier) {
        return this.map(value -> field.mul.apply(value, multiplier));
    }

    public Matrix<T> opp(Field<T> field) {
        return this.map(field.opp);
    }

    public Matrix<T> add(Field<T> field, Matrix<T> other) {
        return this.map2(other, field.add);
    }

    public Matrix<T> sub(Field<T> field, Matrix<T> other) {
        return this.map2(other, field.sub);
    }

    Matrix<T> multiply(Field<T> field, Matrix<T> other) {
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
        return new Matrix<>(newContents, false);
    }

}
