package linearalgebra;

import com.schuerger.math.rationalj.Rational;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestMatrix {

    @Test
    public void testMatrixConstructorNoRows() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix(new Rational[0][6]));
        Rational[][] arr = {};
        assertThrows(IllegalArgumentException.class, () -> new Matrix(arr));
    }

    @Test
    public void testMatrixConstructorNoEmptyRows() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix(new Rational[6][0]));
        Rational[][] arr = {{Rational.ONE, Rational.TWO}, {}};
        assertThrows(IllegalArgumentException.class, () -> new Matrix(arr));
    }

    @Test
    public void testMatrixConstructorJaggedRows() {
        Rational[][] arr = {{Rational.ONE, Rational.TWO}, {Rational.ZERO}};
        assertThrows(IllegalArgumentException.class, () -> new Matrix(arr));
    }

    @Test
    public void testMatrixConstructorCorrectArray() {
        Rational[][] arr1 = {{Rational.of(3, 7)}};
        Rational[][] arr2 = {{Rational.of(2, 5), Rational.of(-2, 3)}};
        Rational[][] arr3 = {{Rational.of(-3, 2)}, {Rational.MINUS_ONE}};
        Rational[][] arr4 = {{Rational.of(-1, 7), Rational.ONE_HALF}, {Rational.ZERO, Rational.TWO}};
        assertEquals("[[3/7]]", (new Matrix(arr1)).toString());
        assertEquals("[[2/5, -2/3]]", (new Matrix(arr2)).toString());
        assertEquals("[[-3/2], [-1]]", (new Matrix(arr3)).toString());
        assertEquals("[[-1/7, 1/2], [0, 2]]", (new Matrix(arr4)).toString());
    }

    @Test
    public void testMatrixGetRow() {
        Rational[][] arr = {{Rational.ONE, Rational.ZERO}, {Rational.ZERO, Rational.ONE}};
        Matrix m = new Matrix(arr);
        assertThrows(IndexOutOfBoundsException.class, () -> m.getRow(-2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getRow(-1));
        assertEquals("[1, 0]", (new Vector(m.getRow(0))).toString());
        assertEquals("[0, 1]", (new Vector(m.getRow(1))).toString());
        assertThrows(IndexOutOfBoundsException.class, () -> m.getRow(2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getRow(3));
    }

    @Test
    public void testMatrixGetCol() {
        Rational[][] arr = {{Rational.of(1), Rational.of(2)}, {Rational.of(3, 2), Rational.of(-17, 19)}};
        Matrix m = new Matrix(arr);
        assertThrows(IndexOutOfBoundsException.class, () -> m.getCol(-2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getCol(-1));
        assertEquals("[1, 3/2]", (new Vector(m.getCol(0))).toString());
        assertEquals("[2, -17/19]", (new Vector(m.getCol(1))).toString());
        assertThrows(IndexOutOfBoundsException.class, () -> m.getCol(2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.getCol(3));
    }

    @Test
    public void testMatrixAddDifferentDimensions() {
        Matrix m1 = new Matrix(new Rational[2][2]), m2 = new Matrix(new Rational[2][1]);
        assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    public void testMatrixAddSameDimensions() {
        Rational[][] arr1 = {{Rational.of(1, 2), Rational.of(1, 3)}, {Rational.of(1, 4), Rational.of(1, 5)}};
        Rational[][] arr2 = {{Rational.of(1, 6), Rational.of(1, 8)}, {Rational.of(1, 7), Rational.of(1, 9)}};
        Matrix m1 = new Matrix(arr1), m2 = new Matrix(arr2);
        assertEquals("[[2/3, 11/24], [11/28, 14/45]]", m1.add(m2).toString());
    }

    @Test
    public void testMatrixSubtractDifferentDimensions() {
        Matrix m1 = new Matrix(new Rational[1][1]), m2 = new Matrix(new Rational[2][2]);
        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    public void testMatrixSubtractSameDimensions() {
        Rational[][] arr1 = {{Rational.of(27, 5), Rational.of(31, 7)}};
        Rational[][] arr2 = {{Rational.of(2, 5), Rational.of(3, 7)}};
        Matrix m1 = new Matrix(arr1), m2 = new Matrix(arr2);
        assertEquals("[[5, 4]]", m1.subtract(m2).toString());
    }

    @Test
    public void testMatrixMultiplyByNumber() {
        Rational[][] arr = {{Rational.of(2, 9), Rational.of(31, 3)}, {Rational.of(1, 3), Rational.of(-2, 3)}};
        assertEquals("[[2/3, 31], [1, -2]]", (new Matrix(arr)).multiply(Rational.of(3)).toString());
    }

    @Test
    public void testMatrixMultiplyWrongDimensions() {
        Matrix m1 = new Matrix(new Rational[2][2]), m2 = new Matrix(new Rational[1][2]);
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2));
    }

    @Test
    public void testMatrixMultiply() {
        Rational[][] arr1 = {{Rational.of(2, 17)}}, arr2 = {{Rational.of(17, 4)}};
        Matrix m1 = new Matrix(arr1), m2 = new Matrix(arr2);
        assertEquals("[[1/2]]", m1.multiply(m2).toString());

        Rational[][] arr3 = {{Rational.of(2, 17), Rational.of(-3, 13)}}, arr4 = {{Rational.of(17, 2)}, {Rational.of(-13, 3)}};
        Matrix m3 = new Matrix(arr3), m4 = new Matrix(arr4);
        assertEquals("[[2]]", m3.multiply(m4).toString());
        assertEquals("[[1, -51/26], [-26/51, 1]]", m4.multiply(m3).toString());

        Rational[][] arr5 = {{Rational.of(1, 2), Rational.of(2, 3), Rational.of(3, 4)}};
        Rational[][] arr6 = {
                {Rational.of(4, 5), Rational.of(5, 6)},
                {Rational.of(-5, 4), Rational.of(4, 3)},
                {Rational.of(6, 5), Rational.of(-3, 2)}
        };
        Matrix m5 = new Matrix(arr5), m6 = new Matrix(arr6);
        assertEquals("[[7/15, 13/72]]", m5.multiply(m6).toString());
    }


}
