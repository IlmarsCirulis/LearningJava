package com.ilmarscirulis.structures;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Field<T> {
    public final T ZERO, ONE;
    public final BiFunction<T, T, T> add, mul;
    public final Function<T, T> opp, inv;
    public final BiFunction<T, T, T> sub, div;

    public Field(T zero, T one, BiFunction<T, T, T> add, BiFunction<T, T, T> mul, Function<T, T> opp, Function<T, T> inv) {
        this.ZERO = zero;
        this.ONE = one;
        this.add = add;
        this.mul = mul;
        this.opp = opp;
        this.inv = inv;
        this.sub = (x, y) -> this.add.apply(x, this.opp.apply(y));
        this.div = (x, y) -> this.mul.apply(x, this.inv.apply(y));
    }
}
