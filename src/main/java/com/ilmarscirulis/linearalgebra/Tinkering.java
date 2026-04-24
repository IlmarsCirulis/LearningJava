package com.ilmarscirulis.linearalgebra;

import com.schuerger.math.rationalj.Rational;


public class Tinkering {

    static String toString(ElementaryRowOperation<Rational> op) {
        return switch (op) {
            case RowSwap(int i, int j) -> "Rows " + i + " and " + j + " swapped";
            case RowMultiplied(int i, var k) -> "Row " + i + " multiplied by " + k;
            case RowPlusMultipliedRow(int i, var k, int j) -> "Row " + i + " plus row " + j + " multiplied by " + k;
        };
    }

    static void main() {

        System.out.println(toString(new RowSwap<>(1, 3)));
        System.out.println(toString(new RowMultiplied<>(1, Rational.of(-1, 2))));
        System.out.println(toString(new RowPlusMultipliedRow<>(1, Rational.of(3, 14), 0)));

        Rational[][] array = {{Rational.of(1, 2), Rational.of(2, 3)}, {Rational.of(3, 4), Rational.of(4, 5)}};
        MutableMatrix<Rational> matrix = new MutableMatrix<>(array);

        System.out.println(matrix);
    }
}
