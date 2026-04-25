package com.ilmarscirulis.linearalgebra;

import com.ilmarscirulis.linearalgebra.pivotingrules.FirstNonzeroPivot;
import com.ilmarscirulis.linearalgebra.rowoperations.*;
import com.schuerger.math.rationalj.Rational;
import org.junit.jupiter.api.Test;
import com.ilmarscirulis.structures.Field;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestImmutableMatrix {

    private final Field<Rational> fieldOfRationalNumbers = new Field<>(Rational.ZERO, Rational.ONE, Rational::add, Rational::multiply, Rational::negate, Rational::reciprocal);

    // test constructor that uses ArrayList<ArrayList<T>>
    @Test
    public void testMatrixConstructorNoRows() {
        ArrayList<ArrayList<Rational>> empty = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(empty));
    }

    @Test
    public void testMatrixConstructorNoEmptyRows() {
        ArrayList<ArrayList<Rational>> oneEmptyRow = new ArrayList<>();
        oneEmptyRow.add(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(oneEmptyRow));

        ArrayList<ArrayList<Rational>> twoEmptyRows = new ArrayList<>();
        twoEmptyRows.add(new ArrayList<>());
        twoEmptyRows.add(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(twoEmptyRows));

        ArrayList<Rational> firstRow = new ArrayList<>(List.of(Rational.of(2, 3)));
        ArrayList<ArrayList<Rational>> secondRowEmpty = new ArrayList<>(List.of(firstRow, new ArrayList<>()));
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(secondRowEmpty));
    }

    @Test
    public void testMatrixConstructorJaggedRows() {
        ArrayList<Rational> firstRow = new ArrayList<>(List.of(Rational.ONE, Rational.TWO));
        ArrayList<Rational> secondRow = new ArrayList<>(List.of(Rational.ZERO));
        ArrayList<ArrayList<Rational>> arr = new ArrayList<>(List.of(firstRow, secondRow));
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(arr));
    }

    @Test
    public void testMatrixConstructorRectangularContent() {
        ArrayList<ArrayList<Rational>> arr1, arr2, arr3, arr4;

        arr1 = new ArrayList<>();
        arr1.add(new ArrayList<>(List.of(Rational.of(3, 7))));
        assertEquals("[[3/7]]", (new ImmutableMatrix<>(arr1)).toString());

        arr2 = new ArrayList<>();
        arr2.add(new ArrayList<>(List.of(Rational.of(3, 4), Rational.of(-2, 3))));
        assertEquals("[[3/4, -2/3]]", (new ImmutableMatrix<>(arr2)).toString());

        arr3 = new ArrayList<>();
        arr3.add(new ArrayList<>(List.of(Rational.of(-3, 2))));
        arr3.add(new ArrayList<>(List.of(Rational.MINUS_ONE)));
        assertEquals("[[-3/2], [-1]]", (new ImmutableMatrix<>(arr3)).toString());

        arr4 = new ArrayList<>();
        arr4.add(new ArrayList<>(List.of(Rational.of(-1, 7), Rational.ONE_HALF)));
        arr4.add(new ArrayList<>(List.of(Rational.ZERO, Rational.TWO)));
        assertEquals("[[-1/7, 1/2], [0, 2]]", (new ImmutableMatrix<>(arr4)).toString());
    }

    // test constructor that uses T[][]
    @Test
    public void testMatrixConstructorNoRowsArray() {
        Rational[][] emptyArray = {};
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(emptyArray));
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(new Rational[0][0]));
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(new Rational[0][3]));
    }

    @Test
    public void testMatrixConstructorNoEmptyRowsArray() {
        Rational[][] oneEmptyRow = new Rational[1][0];
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(oneEmptyRow));

        Rational[][] twoEmptyRows = new Rational[2][0];
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(twoEmptyRows));

        Rational[][] secondRowEmpty = {{Rational.of(2, 3)}, {}};
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(secondRowEmpty));
    }

    @Test
    public void testMatrixConstructorJaggedRowsArray() {
        Rational[][] arr = {{Rational.ONE, Rational.TWO}, {Rational.ZERO}};
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix<>(arr));
    }

    @Test
    public void testMatrixConstructorRectangularContentArray() {
        Rational[][] arr1 = {{Rational.of(3, 7)}};
        assertEquals("[[3/7]]", (new ImmutableMatrix<>(arr1)).toString());

        Rational[][] arr2 = {{Rational.of(3, 4), Rational.of(-2, 3)}};
        assertEquals("[[3/4, -2/3]]", (new ImmutableMatrix<>(arr2)).toString());

        Rational[][] arr3 = {{Rational.of(-3, 2)}, {Rational.MINUS_ONE}};
        assertEquals("[[-3/2], [-1]]", (new ImmutableMatrix<>(arr3)).toString());

        Rational[][] arr4 = {{Rational.of(-1, 7), Rational.of(1, 2)}, {Rational.ZERO, Rational.TWO}};
        assertEquals("[[-1/7, 1/2], [0, 2]]", (new ImmutableMatrix<>(arr4)).toString());
    }

    @Test
    public void testMatrixConstructorsBothAreSame() {
        Rational[][] arr1 = {{Rational.of(1, 3), Rational.of(1, 2)}, {Rational.of(3, 5), Rational.of(2, 3)}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr1);

        ArrayList<ArrayList<Rational>> arr2 = new ArrayList<>();
        arr2.add(new ArrayList<>(List.of(Rational.of(1, 3), Rational.of(1, 2))));
        arr2.add(new ArrayList<>(List.of(Rational.of(3, 5), Rational.of(2, 3))));
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr2);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(m1.get(i, j), m2.get(i, j));
            }
        }
    }

    @Test
    public void testMatrixGet() {
        Rational[][] arr = {{Rational.of(1, 1), Rational.of(1, 2), Rational.of(1, 3)}, {Rational.of(2, 12), Rational.of(213, 2), Rational.of(2, 3)}, {Rational.of(3, 11), Rational.of(3, 2), Rational.of(1)}};
        ImmutableMatrix<Rational> m = new ImmutableMatrix<>(arr);

        assertThrows(IndexOutOfBoundsException.class, () -> m.get(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> m.get(3, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> m.get(1, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> m.get(2, 3));
        assertEquals(Rational.of(1, 1), m.get(0, 0));
        assertEquals(Rational.of(1, 3), m.get(0, 2));
        assertEquals(Rational.of(3, 11), m.get(2, 0));
        assertEquals(Rational.of(1), m.get(2, 2));
        assertEquals(Rational.of(213, 2), m.get(1, 1));
    }

    @Test
    public void testMatrixDeepCopyAndSet() {
        Rational[][] arr = {{Rational.of(10, 9), Rational.of(8, 7)}, {Rational.of(6, 5), Rational.of(4, 3)}};
        ImmutableMatrix<Rational> m = new ImmutableMatrix<>(arr);
        ImmutableMatrix<Rational> copy = m.deepCopy();

        assertEquals("[[10/9, 8/7], [6/5, 4/3]]", m.toString());
        assertEquals("[[10/9, 8/7], [6/5, 4/3]]", copy.toString());

        for (int i = 0; i < copy.getNumberOfRows(); i++) {
            for (int j = 0; j < copy.getNumberOfColumns(); j++) {
                copy = copy.set(i, j, copy.get(i, j).reciprocal());
            }
        }
        assertEquals("[[10/9, 8/7], [6/5, 4/3]]", m.toString());
        assertEquals("[[9/10, 7/8], [5/6, 3/4]]", copy.toString());
    }

    @Test
    public void testMatrixGetNumberOfRows() {
        Rational[][] arr1 = {{Rational.of(-1, 7), Rational.of(1, 11)}};
        assertEquals(1, (new ImmutableMatrix<>(arr1)).getNumberOfRows());

        Rational[][] arr2 = {{Rational.of(2, 17)}, {Rational.of(3, 14)}};
        assertEquals(2, (new ImmutableMatrix<>(arr2)).getNumberOfRows());
    }

    @Test
    public void testMatrixGetNumberOfColumns() {
        Rational[][] arr1 = {{Rational.of(13, 5)}, {Rational.of(13, 7)}};
        assertEquals(1, (new ImmutableMatrix<>(arr1)).getNumberOfColumns());

        Rational[][] arr2 = {{Rational.of(13, 2), Rational.of(13, 4)}};
        assertEquals(2, (new ImmutableMatrix<>(arr2)).getNumberOfColumns());
    }

    @Test
    public void testMatrixGetRow() {
        Rational[][] arr1 = {{Rational.of(7, 13), Rational.MINUS_ONE}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr1);
        assertThrows(IndexOutOfBoundsException.class, () -> m1.getRow(-1));
        assertEquals("[7/13, -1]", m1.getRow(0).toString());
        assertThrows(IndexOutOfBoundsException.class, () -> m1.getRow(1));

        Rational[][] arr2 = {{Rational.of(1), Rational.of(2)}, {Rational.of(1, 3), Rational.of(2, 3)}};
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr2);
        assertThrows(IndexOutOfBoundsException.class, () -> m2.getRow(-2));
        assertEquals("[1, 2]", m2.getRow(0).toString());
        assertEquals("[1/3, 2/3]", m2.getRow(1).toString());
        assertThrows(IndexOutOfBoundsException.class, () -> m2.getRow(2));
    }

    @Test
    public void testMatrixGetColumn() {
        Rational[][] arr1 = {{Rational.of(-1, 2)}, {Rational.TEN}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr1);
        assertThrows(IndexOutOfBoundsException.class, () -> m1.getColumn(-1));
        assertEquals("[-1/2, 10]", m1.getColumn(0).toString());
        assertThrows(IndexOutOfBoundsException.class, () -> m1.getColumn(1));

        Rational[][] arr2 = {{Rational.of(10, 9), Rational.of(8, 7)}, {Rational.of(6, 5), Rational.of(4, 3)}};
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr2);
        assertThrows(IndexOutOfBoundsException.class, () -> m2.getColumn(-2));
        assertEquals("[10/9, 6/5]", m2.getColumn(0).toString());
        assertEquals("[8/7, 4/3]", m2.getColumn(1).toString());
        assertThrows(IndexOutOfBoundsException.class, () -> m2.getColumn(2));
    }

    @Test
    public void testMatrixSwapRows() {
        Rational[][] arr = {{Rational.of(7, 5), Rational.of(3, 2)}, {Rational.of(5, 3), Rational.of(2)}};
        ImmutableMatrix<Rational> matrix = new ImmutableMatrix<>(arr);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.swapRows(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.swapRows(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.swapRows(2, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.swapRows(1, 2));

        assertEquals("[[7/5, 3/2], [5/3, 2]]", matrix.toString());

        ImmutableMatrix<Rational> sameMatrix = matrix.swapRows(0, 0);
        assertEquals("[[7/5, 3/2], [5/3, 2]]", matrix.toString());
        assertEquals("[[7/5, 3/2], [5/3, 2]]", sameMatrix.toString());

        ImmutableMatrix<Rational> changedMatrix = matrix.swapRows(0, 1);
        assertEquals("[[7/5, 3/2], [5/3, 2]]", matrix.toString());
        assertEquals("[[5/3, 2], [7/5, 3/2]]", changedMatrix.toString());
    }


    @Test
    public void testMatrixMultiplyByNumber() {
        Rational[][] arr1 = {{Rational.of(1, 17)}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr1);
        assertEquals("[[3/85]]", m1.multiply(fieldOfRationalNumbers, Rational.of(3, 5)).toString());

        Rational[][] arr2 = {{Rational.of(3), Rational.of(4, 5)}, {Rational.ZERO, Rational.of(1, 11)}};
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr2);
        assertEquals("[[2, 8/15], [0, 2/33]]", m2.multiply(fieldOfRationalNumbers, Rational.of(2, 3)).toString());
    }

    @Test
    public void testMatrixOpp() {
        Rational[][] arr1 = {{Rational.of(2, 7), Rational.of(-1, 12)}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr1);
        assertEquals("[[-2/7, 1/12]]", m1.opp(fieldOfRationalNumbers).toString());

        Rational[][] arr2 = {{Rational.of(1, 2)}, {Rational.of(3)}};
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr2);
        assertEquals("[[-1/2], [-3]]", m2.opp(fieldOfRationalNumbers).toString());
    }

    @Test
    public void testMatrixAdd() {
        Rational[][] arr1 = {{Rational.of(1), Rational.of(-1, 12)}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr1);
        Rational[][] arr2 = {{Rational.of(1, 3), Rational.of(3, 2)}, {Rational.of(0), Rational.of(-3)}};
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr2);
        Rational[][] arr3 = {{Rational.ONE}, {Rational.TWO}};
        ImmutableMatrix<Rational> m3 = new ImmutableMatrix<>(arr3);
        Rational[][] arr4 = {{Rational.of(2, 3), Rational.of(-1, 2)}, {Rational.ONE, Rational.TWO}};
        ImmutableMatrix<Rational> m4 = new ImmutableMatrix<>(arr4);

        assertThrows(IllegalArgumentException.class, () -> m1.add(fieldOfRationalNumbers, m2));
        assertThrows(IllegalArgumentException.class, () -> m1.add(fieldOfRationalNumbers, m3));
        assertThrows(IllegalArgumentException.class, () -> m3.add(fieldOfRationalNumbers, m2));

        assertEquals("[[1, 1], [1, -1]]", m2.add(fieldOfRationalNumbers, m4).toString());
    }

    @Test
    public void testMatrixSub() {
        Rational[][] arr1 = {{Rational.of(2), Rational.of(-1, 6)}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr1);
        Rational[][] arr2 = {{Rational.of(1, 3), Rational.of(3, 2)}, {Rational.of(0), Rational.of(-3)}};
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr2);
        Rational[][] arr3 = {{Rational.TWO}, {Rational.TEN}};
        ImmutableMatrix<Rational> m3 = new ImmutableMatrix<>(arr3);
        Rational[][] arr4 = {{Rational.of(2, 3), Rational.of(-1, 2)}, {Rational.ONE, Rational.TWO}};
        ImmutableMatrix<Rational> m4 = new ImmutableMatrix<>(arr4);

        assertThrows(IllegalArgumentException.class, () -> m1.sub(fieldOfRationalNumbers, m2));
        assertThrows(IllegalArgumentException.class, () -> m1.sub(fieldOfRationalNumbers, m3));
        assertThrows(IllegalArgumentException.class, () -> m3.sub(fieldOfRationalNumbers, m2));

        assertEquals("[[-1/3, 2], [-1, -5]]", m2.sub(fieldOfRationalNumbers, m4).toString());
    }

    @Test
    public void testMatrixMultiplyByMatrix() {
        Rational[][] arr1 = {{Rational.of(12), Rational.of(-11, 6)}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr1);
        Rational[][] arr2 = {{Rational.of(11, 3), Rational.of(13, 2)}, {Rational.of(10), Rational.of(-3)}};
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr2);
        Rational[][] arr3 = {{Rational.TWO}, {Rational.TEN}};
        ImmutableMatrix<Rational> m3 = new ImmutableMatrix<>(arr3);
        Rational[][] arr4 = {{Rational.of(2, 13), Rational.of(-11, 2)}, {Rational.ONE, Rational.TWO}};
        ImmutableMatrix<Rational> m4 = new ImmutableMatrix<>(arr4);

        assertThrows(IllegalArgumentException.class, () -> m3.multiply(fieldOfRationalNumbers, m2));
        assertEquals("[[217/3], [-10]]", m2.multiply(fieldOfRationalNumbers, m3).toString());
        assertEquals("[[17/3]]", m1.multiply(fieldOfRationalNumbers, m3).toString());
        assertEquals("[[551/78, -43/6], [-19/13, -61]]", m2.multiply(fieldOfRationalNumbers, m4).toString());
    }

    @Test
    public void testMatrixMultiplyRow() {
        Rational[][] arr = {{Rational.of(1, 3), Rational.of(2, 3)}, {Rational.of(1), Rational.of(4, 3)}};
        ImmutableMatrix<Rational> m = new ImmutableMatrix<>(arr);
        Rational multiplier = Rational.of(3, 2);

        assertThrows(IllegalArgumentException.class, () -> m.multiplyRow(fieldOfRationalNumbers, 1, Rational.ZERO));
        assertThrows(IndexOutOfBoundsException.class, () -> m.multiplyRow(fieldOfRationalNumbers, -1, multiplier));
        assertThrows(IndexOutOfBoundsException.class, () -> m.multiplyRow(fieldOfRationalNumbers, 2, multiplier));

        assertEquals("[[1/3, 2/3], [1, 4/3]]", m.toString());
        ImmutableMatrix<Rational> mNew = m.multiplyRow(fieldOfRationalNumbers, 0, multiplier);
        assertEquals("[[1/2, 1], [1, 4/3]]", mNew.toString());
        assertEquals("[[1/3, 2/3], [1, 4/3]]", m.toString());
    }

    @Test
    public void testMatrixAddMultipliedRow() {
        Rational[][] arr = {{Rational.of(1, 4), Rational.of(1)}, {Rational.of(-1, 2), Rational.of(2, 3)}};
        ImmutableMatrix<Rational> matrix = new ImmutableMatrix<>(arr);
        Rational multiplier = Rational.of(3, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.addMultipliedRow(fieldOfRationalNumbers, 1, multiplier, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.addMultipliedRow(fieldOfRationalNumbers, -1, multiplier, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.addMultipliedRow(fieldOfRationalNumbers, 0, multiplier, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.addMultipliedRow(fieldOfRationalNumbers, 2, multiplier, 1));
        assertThrows(IllegalArgumentException.class, () -> matrix.addMultipliedRow(fieldOfRationalNumbers, 1, multiplier, 1));

        assertEquals("[[1/4, 1], [-1/2, 2/3]]", matrix.toString());
        ImmutableMatrix<Rational> modifiedMatrix = matrix.addMultipliedRow(fieldOfRationalNumbers, 0, multiplier, 1);
        assertEquals("[[-1/2, 2], [-1/2, 2/3]]", modifiedMatrix.toString());
        assertEquals("[[1/4, 1], [-1/2, 2/3]]", matrix.toString());

        modifiedMatrix = matrix.addMultipliedRow(fieldOfRationalNumbers, 1, multiplier, 0);
        assertEquals("[[1/4, 1], [-1/8, 13/6]]", modifiedMatrix.toString());
        assertEquals("[[1/4, 1], [-1/2, 2/3]]", matrix.toString());

        modifiedMatrix = matrix.addMultipliedRow(fieldOfRationalNumbers, 1, Rational.ZERO, 0);
        assertEquals("[[1/4, 1], [-1/2, 2/3]]", modifiedMatrix.toString());
        assertEquals("[[1/4, 1], [-1/2, 2/3]]", matrix.toString());
    }

    @Test
    public void testMatrixApplyElementaryRowOperation() {
        ElementaryRowOperation<Rational> op1, op2, op3;
        op1 = new RowSwap<>(0, 1);
        op2 = new RowMultiplied<>(0, Rational.TWO);
        op3 = new RowPlusMultipliedRow<>(0, Rational.ONE_HALF, 1);

        Rational[][] arr = {{Rational.of(1, 4), Rational.of(1)}, {Rational.of(-1, 2), Rational.of(2, 3)}};
        ImmutableMatrix<Rational> matrix = new ImmutableMatrix<>(arr), m1, m2, m3;

        m1 = matrix.applyElementaryRowOperation(fieldOfRationalNumbers, op1);
        m2 = matrix.applyElementaryRowOperation(fieldOfRationalNumbers, op2);
        m3 = matrix.applyElementaryRowOperation(fieldOfRationalNumbers, op3);

        assertEquals("[[-1/2, 2/3], [1/4, 1]]", m1.toString());
        assertEquals("[[1/2, 2], [-1/2, 2/3]]", m2.toString());
        assertEquals("[[0, 4/3], [-1/2, 2/3]]", m3.toString());

        m2 = m1.applyElementaryRowOperation(fieldOfRationalNumbers, op3);
        assertEquals("[[-3/8, 7/6], [1/4, 1]]", m2.toString());
    }

    @Test
    public void testMatrixOperationsForRowEchelonForm() {
        Rational[][] arr = {{Rational.of(-4), Rational.of(-1, 3)}, {Rational.of(10, 7), Rational.of(-8, 9)}};
        ImmutableMatrix<Rational> m1 = new ImmutableMatrix<>(arr);
        ImmutableMatrix<Rational> m2 = new ImmutableMatrix<>(arr);

        ArrayList<ElementaryRowOperation<Rational>> ops1 =
                m1.operationsForRowEchelonForm(fieldOfRationalNumbers, new FirstNonzeroPivot<>(), true);
        ArrayList<ElementaryRowOperation<Rational>> ops2 =
                m2.operationsForRowEchelonForm(fieldOfRationalNumbers, new FirstNonzeroPivot<>(), false);

        ArrayList<ElementaryRowOperation<Rational>> correctOps1 = new ArrayList<>();
        correctOps1.add(new RowMultiplied<>(0, Rational.of(-1, 4)));
        correctOps1.add(new RowPlusMultipliedRow<>(1, Rational.of(-10, 7), 0));
        correctOps1.add(new RowMultiplied<>(1, Rational.of(-126, 127)));
        ArrayList<ElementaryRowOperation<Rational>> correctOps2 = new ArrayList<>();
        correctOps2.add(new RowPlusMultipliedRow<>(1, Rational.of(5, 14), 0));

        RowOperationEquals<Rational> cmp = new RowOperationEquals<>(Rational::equals);

        assertTrue(cmp.run(ops1, correctOps1));
        assertTrue(cmp.run(ops2, correctOps2));
    }

    @Test
    public void testMatrixToRowEchelonForm() {
        Rational[][] array = {
                {Rational.of(1), Rational.of(5, 3), Rational.of(10, 7)},
                {Rational.of(2), Rational.of(-9, 4), Rational.of(-1)},
                {Rational.of(3, 7), Rational.of(8, 3), Rational.of(2)}
        };
        ImmutableMatrix<Rational> startMatrix = new ImmutableMatrix<>(array);
        ImmutableMatrix<Rational> endMatrixWithoutReduce = startMatrix.toRowEchelonForm(fieldOfRationalNumbers, new FirstNonzeroPivot<>(), false);
        ImmutableMatrix<Rational> endMatrixWithReduce = startMatrix.toRowEchelonForm(fieldOfRationalNumbers, new FirstNonzeroPivot<>(), true);

        assertEquals("[[1, 5/3, 10/7], [0, -67/12, -27/7], [0, 0, 128/3283]]", endMatrixWithoutReduce.toString());
        assertEquals("[[1, 5/3, 10/7], [0, 1, 324/469], [0, 0, 1]]", endMatrixWithReduce.toString());
    }


}
