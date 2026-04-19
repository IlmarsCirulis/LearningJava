package com.ilmarscirulis.linearalgebra;

import com.schuerger.math.rationalj.Rational;

import java.util.StringJoiner;
import java.util.function.BiFunction;

public class Vector {

    private final Rational[] contents;

    private Vector(Rational[] contents, boolean copy) {
        if (contents.length == 0) {
            throw new IllegalArgumentException("No empty vectors are allowed");
        }
        if (copy) {
            this.contents = new Rational[contents.length];
            System.arraycopy(contents, 0, this.contents, 0, contents.length);
        } else {
            this.contents = contents;
        }
    }

    Vector(Rational... values) {
        this(values, true);
    }

    public Vector multiply(Rational k) {
        Rational[] newContents = new Rational[this.contents.length];
        for (int i = 0; i < this.contents.length; i++) {
            newContents[i] = this.contents[i].multiply(k);
        }
        return new Vector(newContents, false);
    }

    private Vector map2(Vector other, BiFunction<Rational, Rational, Rational> fun) {
        if (this.contents.length != other.contents.length) {
            throw new IllegalArgumentException("These vectors have different lengths");
        }
        int length = this.contents.length;
        Rational[] newContents = new Rational[length];
        for (int i = 0; i < length; i++) {
            newContents[i] = fun.apply(this.contents[i], other.contents[i]);
        }
        return new Vector(newContents, false);
    }

    public Vector add(Vector other) {
        return this.map2(other, Rational::add);
    }

    public Vector subtract(Vector other) {
        return this.map2(other, Rational::subtract);
    }

    public Vector addMultiplied(Vector other, Rational k) {
        return this.map2(other, (x, y) -> x.add(y.multiply(k)));
    }

    public Vector subtractMultiplied(Vector other, Rational k) {
        return this.map2(other, (x, y) -> x.subtract(y.multiply(k)));
    }

    public Rational scalarProduct(Vector other) {
        Rational sum = Rational.ZERO;
        Vector elementwiseProduct = this.map2(other, Rational::multiply);
        for (Rational a : elementwiseProduct.contents) {
            sum = sum.add(a);
        }
        return sum;
    }

    @Override
    public String toString() {
        StringJoiner s = new StringJoiner(", ", "[", "]");
        for (Rational x : this.contents) {
            s.add(x.toString());
        }
        return s.toString();
    }

    public int size() {
        return this.contents.length;
    }

    public Rational get(int i) {
        if (i < 0 || this.contents.length <= i) {
            throw new IndexOutOfBoundsException("Index = " + i + " while size of the vector = " + this.contents.length);
        }
        return this.contents[i];
    }

    public Rational[] getContents() {
        Rational[] copy = new Rational[this.contents.length];
        System.arraycopy(this.contents, 0, copy, 0, this.contents.length);
        return copy;
    }
}
