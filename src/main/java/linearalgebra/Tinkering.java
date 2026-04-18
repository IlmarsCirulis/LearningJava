package linearalgebra;

import com.schuerger.math.rationalj.Rational;

public class Tinkering {

    static void main() {

        Rational x = Rational.of(3, 5);
        Rational y = Rational.of(4, 5);
        Rational z = x.pow(2).add(y.pow(2));

        System.out.println("x = " + x);
        System.out.println("y = " + y);
        System.out.println("z = x ^ 2 + y ^ 2");
        System.out.println("z = " + z);

        Rational[] arr1 = {Rational.of("1/2"), Rational.of("2/3")};
        Rational[] arr2 = {Rational.of("1/3"), Rational.of("3/4")};
        Vector v1 = new Vector(arr1), v2 = new Vector(arr2);

        System.out.println(v1.scalarProduct(v2));
        System.out.println(v1);
        System.out.println(v2);

        Rational[][] arr = {
                {Rational.of("1.2"), Rational.of("1.3")},
                {Rational.of("3/4"), Rational.of("-1")}
        };
        Matrix m = new Matrix(arr);
        System.out.println(m);
    }
}
