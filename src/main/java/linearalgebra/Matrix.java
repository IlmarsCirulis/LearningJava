package linearalgebra;

import com.schuerger.math.rationalj.Rational;

import java.util.StringJoiner;
import java.util.function.BiFunction;

public class Matrix {

    private final Rational[][] contents;

    private Matrix(Rational[][] contents, boolean copy) {
        if (contents.length == 0) {
            throw new IllegalArgumentException("No empty matrices are allowed");
        }
        for (Rational[] row : contents) {
            if (row.length == 0) {
                throw new IllegalArgumentException("No empty rows in matrix are allowed");
            }
        }
        int lengthOfFirstRow = contents[0].length;
        for (Rational[] row : contents) {
            if (row.length != lengthOfFirstRow) {
                throw new IllegalArgumentException("Rows of matrix must have the same length");
            }
        }
        if (copy) {
            this.contents = new Rational[contents.length][];
            for (int i = 0; i < contents.length; i++) {
                this.contents[i] = new Rational[contents[i].length];
                System.arraycopy(contents[i], 0, this.contents[i], 0, contents[i].length);
            }
        } else {
            this.contents = contents;
        }
    }

    Matrix(Rational[][] contents) {
        this(contents, true);
    }

    Rational[] getRow(int row) {
        if (row < 0 || this.contents.length <= row) {
            throw new ArrayIndexOutOfBoundsException("There isn't such row in this matrix");
        }
        Rational[] rowArray = new Rational[this.contents[row].length];
        System.arraycopy(this.contents[row], 0, rowArray, 0, this.contents[row].length);
        return rowArray;
    }

    Rational[] getCol(int col) {
        if (col < 0 || this.contents[0].length <= col) {
            throw new ArrayIndexOutOfBoundsException("There isn't such column in this matrix");
        }
        Rational[] columnArray = new Rational[this.contents.length];
        for (int row = 0; row < this.contents.length; row++) {
            columnArray[row] = this.contents[row][col];
        }
        return columnArray;
    }

    private Matrix map2(Matrix other, BiFunction<Rational, Rational, Rational> fun) {
        if (this.contents.length != other.contents.length || this.contents[0].length != other.contents[0].length) {
            throw new IllegalArgumentException("Matrices has to have same dimensions");
        }
        Rational[][] newContents = new Rational[this.contents.length][this.contents[0].length];
        for (int row = 0; row < newContents.length; row++) {
            for (int col = 0; col < newContents[0].length; col++) {
                newContents[row][col] = fun.apply(this.contents[row][col], other.contents[row][col]);
            }
        }
        return new Matrix(newContents, false);
    }

    Matrix add(Matrix other) {
        return this.map2(other, Rational::add);
    }

    Matrix subtract(Matrix other) {
        return this.map2(other, Rational::subtract);
    }

    Matrix multiply(Rational k) {
        Rational[][] newContents = new Rational[this.contents.length][this.contents[0].length];
        for (int row = 0; row < this.contents.length; row++) {
            for (int col = 0; col < this.contents[0].length; col++) {
                newContents[row][col] = this.contents[row][col].multiply(k);
            }
        }
        return new Matrix(newContents, false);
    }

    Matrix multiply(Matrix other) {
        if (this.contents[0].length != other.contents.length) {
            throw new IllegalArgumentException("These matrices can't be multiplied");
        }
        Rational[][] newContents = new Rational[this.contents.length][other.contents[0].length];
        for (int row = 0; row < this.contents.length; row++) {
            for (int col = 0; col < other.contents[0].length; col++) {
                Rational temp = Rational.ZERO;
                for (int i = 0; i < this.contents[0].length; i++) {
                    temp = temp.add(this.contents[row][i].multiply(other.contents[i][col]));
                }
                newContents[row][col] = temp;
            }
        }
        return new Matrix(newContents, false);
    }

    @Override
    public String toString() {
        StringJoiner s = new StringJoiner(", ", "[", "]");
        for (Rational[] row : this.contents) {
            StringJoiner t = new StringJoiner(", ", "[", "]");
            for (Rational value : row) {
                t.add(value.toString());
            }
            s.add(t.toString());
        }
        return s.toString();
    }
}
