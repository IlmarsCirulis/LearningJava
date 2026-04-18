package structures;

public abstract class Field<T> {
    public final T ZERO;
    public final T ONE;

    protected Field(T zero, T one) {
        ZERO = zero;
        ONE = one;
    }

    public abstract T add(T x, T y);
    public abstract T mul(T x, T y);
    public abstract T opp(T x);
    public abstract T inv(T x) throws IllegalArgumentException;
    public abstract T sub(T x, T y);
    public abstract T div(T x, T y) throws IllegalArgumentException;
}
