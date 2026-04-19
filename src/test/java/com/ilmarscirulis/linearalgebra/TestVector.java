package com.ilmarscirulis.linearalgebra;

import com.schuerger.math.rationalj.Rational;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestVector {

    @Test
    public void testVectorConstructorEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> new Vector(new Rational[0]));
    }

    @Test
    public void testVectorConstructorOneElementArray() {
        Rational[] arr = {Rational.of(13, 6)};
        assertEquals("[13/6]", (new Vector(arr)).toString());
    }

    @Test
    public void testVectorConstructorTwoElementArray() {
        Rational[] arr = {Rational.of(11, 13), Rational.of(-1, 2)};
        assertEquals("[11/13, -1/2]", (new Vector(arr)).toString());
    }

    @Test
    public void testVectorConstructorThreeElementArray() {
        Rational[] arr = {Rational.of(0, 11), Rational.ONE, Rational.TWO};
        assertEquals("[0, 1, 2]", (new Vector(arr)).toString());
    }

    @Test
    public void testVectorConstructorNoValues() {
        assertThrows(IllegalArgumentException.class, Vector::new);
    }

    @Test
    public void testVectorConstructorOneElement() {
        assertEquals("[5]", new Vector(Rational.of(15, 3)).toString());
    }

    @Test
    public void testVectorConstructorTwoElements() {
        assertEquals("[-1/2, -3]", new Vector(Rational.of(-1, 2), Rational.of(-3)).toString());
    }

    @Test
    public void testVectorConstructorThreeElements() {
        assertEquals("[1/2, 2/3, 3/4]", new Vector(Rational.of(1, 2), Rational.of(2, 3), Rational.of(3, 4)).toString());
    }


    @Test
    public void testVectorLengthOneMultiply() {
        Vector v = new Vector(Rational.of(17, 22));
        assertEquals("[17/2]", v.multiply(Rational.of(11)).toString());
    }

    @Test
    public void testVectorLengthTwoMultiply() {
        Vector v = new Vector(Rational.of(47), Rational.of(-3, 5));
        assertEquals("[-1, 3/235]", v.multiply(Rational.of(-1, 47)).toString());
    }

    @Test
    public void testVectorLengthFourMultiply() {
        Vector v = new Vector(Rational.of(1, 5), Rational.of(1, 4), Rational.of(-1, 3), Rational.ZERO);
        assertEquals("[-1/5, -1/4, 1/3, 0]", v.multiply(Rational.MINUS_ONE).toString());
    }

    @Test
    public void testVectorAddDifferentLengths() {
        Vector v1 = new Vector(Rational.ONE_HALF);
        Vector v2 = new Vector(Rational.of(1, 2), Rational.of(3, 14));
        assertThrows(IllegalArgumentException.class, () -> v1.add(v2));
    }

    @Test
    public void testVectorAddLengthOne() {
        Vector v1 = new Vector(Rational.of(2, 7));
        Vector v2 = new Vector(Rational.of(5, 7));
        assertEquals("[1]", v1.add(v2).toString());
    }

    @Test
    public void testVectorAddLengthTwo() {
        Vector v1 = new Vector(Rational.of(-7), Rational.of(7, 2));
        Vector v2 = new Vector(Rational.of(7, 2), Rational.of(7, 2));
        assertEquals("[-7/2, 7]", v1.add(v2).toString());
    }

    @Test
    public void testVectorAddLengthFour() {
        Vector v1 = new Vector(Rational.of(3, 4), Rational.of(4, 3), Rational.of(22, 7), Rational.of(6, 7));
        Vector v2 = new Vector(Rational.ZERO, Rational.of(4, 3), Rational.of(22, 7), Rational.of(-6, 7));
        assertEquals("[3/4, 8/3, 44/7, 0]", v1.add(v2).toString());
    }

    @Test
    public void testVectorSubtractDifferentLengths() {
        Vector v1 = new Vector(Rational.of(2, 3), Rational.of(3, 4));
        Vector v2 = new Vector(Rational.of(2, 1), Rational.ZERO, Rational.MINUS_ONE, Rational.of(17, 4));
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v2));
    }

    @Test
    public void testVectorSubtractLengthOne() {
        Vector v1 = new Vector(Rational.of(2, 7));
        Vector v2 = new Vector(Rational.of(5, 7));
        assertEquals("[-3/7]", v1.subtract(v2).toString());
    }

    @Test
    public void testVectorSubtractLengthTwo() {
        Vector v1 = new Vector(Rational.of(16, 9), Rational.of(9, 16));
        Vector v2 = new Vector(Rational.of(1), Rational.of(1));
        assertEquals("[7/9, -7/16]", v1.subtract(v2).toString());
    }

    @Test
    public void testVectorSubtractLengthFour() {
        Vector v1 = new Vector(Rational.of(3, 4), Rational.of(4, 3), Rational.of(22, 7), Rational.of(6, 7));
        Vector v2 = new Vector(Rational.ZERO, Rational.of(4, 3), Rational.of(22, 7), Rational.of(-6, 7));
        assertEquals("[3/4, 0, 0, 12/7]", v1.subtract(v2).toString());
    }

    @Test
    public void testVectorAddMultipliedDifferentLengths() {
        Vector v1 = new Vector(Rational.MINUS_ONE_HALF);
        Vector v2 = new Vector(Rational.ZERO, Rational.ONE_HALF);
        assertThrows(IllegalArgumentException.class, () -> v1.addMultiplied(v2, Rational.of(4, 3)));
    }

    @Test
    public void testVectorAddMultipliedLengthOne() {
        Vector v1 = new Vector(Rational.of(2, 7));
        Vector v2 = new Vector(Rational.of(5, 7));
        assertEquals("[12/7]", v1.addMultiplied(v2, Rational.TWO).toString());
    }

    @Test
    public void testVectorAddMultipliedLengthTwo() {
        Vector v1 = new Vector(Rational.of(1), Rational.of(14));
        Vector v2 = new Vector(Rational.of(4), Rational.of(-16));
        assertEquals("[-1, 22]", v1.addMultiplied(v2, Rational.MINUS_ONE_HALF).toString());
    }

    @Test
    public void testVectorAddMultipliedLengthFour() {
        Vector v1 = new Vector(Rational.of(3, 4), Rational.of(4, 3), Rational.of(22, 7), Rational.of(6, 7));
        Vector v2 = new Vector(Rational.ZERO, Rational.of(4, 3), Rational.of(22, 7), Rational.of(-6, 7));
        assertEquals("[3/4, 2/3, 11/7, 9/7]", v1.addMultiplied(v2, Rational.MINUS_ONE_HALF).toString());
    }

    @Test
    public void testVectorSubtractMultipliedDifferentLengths() {
        Vector v1 = new Vector(Rational.of(1, 2), Rational.of(-3, 17));
        Vector v2 = new Vector(Rational.of(3, 14));
        assertThrows(IllegalArgumentException.class, () -> v1.subtractMultiplied(v2, Rational.of(1, 7)));
    }

    @Test
    public void testVectorSubtractMultipliedLengthTwo() {
        Vector v1 = new Vector(Rational.of(2, 11), Rational.of(33));
        Vector v2 = new Vector(Rational.of(3, 11), Rational.of(-10));
        assertEquals("[1, 3]", v1.subtractMultiplied(v2, Rational.of(-3)).toString());
    }

    @Test
    public void testVectorSubtractMultipliedLengthFour() {
        Vector v1 = new Vector(Rational.of(3, 4), Rational.of(4, 3), Rational.of(22, 7), Rational.of(6, 7));
        Vector v2 = new Vector(Rational.ZERO, Rational.of(4, 3), Rational.of(22, 7), Rational.of(-6, 7));
        assertEquals("[3/4, 2, 33/7, 3/7]", v1.subtractMultiplied(v2, Rational.MINUS_ONE_HALF).toString());
    }

    @Test
    public void testVectorScalarProductDifferentLengths() {
        Vector v1 = new Vector(Rational.of(3, 4));
        Vector v2 = new Vector(Rational.of(3, 4), Rational.of(4, 5));
        assertThrows(IllegalArgumentException.class, () -> v1.scalarProduct(v2));
    }

    @Test
    public void testVectorScalarProductLengthOne() {
        Vector v1 = new Vector(Rational.of(1, 3));
        Vector v2 = new Vector(Rational.MINUS_ONE_HALF);
        assertEquals(Rational.of(-1, 6), v1.scalarProduct(v2));
    }

    @Test
    public void testVectorScalarProductLengthTwo() {
        Vector v1 = new Vector(Rational.ONE_HALF, Rational.of(22, 7));
        Vector v2 = new Vector(Rational.of(1, 3), Rational.ONE_HALF);
        assertEquals(Rational.of(73, 42), v1.scalarProduct(v2));
    }

    @Test
    public void testVectorSizeOne() {
        assertEquals(1, (new Vector(Rational.of(7, -6))).size());
    }

    @Test
    public void testVectorSizeFour() {
        Vector v = new Vector(Rational.MINUS_ONE, Rational.ZERO, Rational.ONE, Rational.TWO);
        assertEquals(4, v.size());
    }

    @Test
    public void testVectorGetIndexNegative() {
        Vector v = new Vector(Rational.of(2, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> v.get(-1));
    }

    @Test
    public void testVectorGetIndexTooBig() {
        Vector v = new Vector(Rational.MINUS_ONE, Rational.ZERO, Rational.ONE, Rational.ONE);
        assertThrows(IndexOutOfBoundsException.class, () -> v.get(4));
    }

    @Test
    public void testVectorGetIndexCorrect() {
        Vector v = new Vector(Rational.MINUS_ONE_HALF, Rational.of(-1, 7), Rational.of(99, 2));
        assertEquals(Rational.of(-1, 2), v.get(0));
        assertEquals(Rational.of(-1, 7), v.get(1));
        assertEquals(Rational.of(99, 2), v.get(2));
    }

    @Test
    public void testVectorGetContents() {
        Vector v = new Vector(Rational.of(1, 2), Rational.of(2, 3), Rational.of(3, 4), Rational.of(4, 5));
        Rational[] arr = v.getContents();
        assertEquals(4, arr.length);
        for (int i = 0; i < 4; i++) {
            assertEquals(v.get(i), arr[i]);
        }

        arr[0] = Rational.ZERO;
        assertEquals(Rational.of(1, 2), v.get(0));
    }
}
