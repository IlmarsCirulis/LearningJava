package rational.linearalgebra;

import com.schuerger.math.rationalj.Rational;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestVector {

    @Test
    public void testConstructor() {

        Vector v0 = new Vector(new Rational[0]);
        assertEquals("[]", v0.toString());

        Rational[] arr1 = {Rational.of(7, 22)};
        Vector v1 = new Vector(arr1);
        assertEquals("[7/22]", v1.toString());

        Rational[] arr2 = {
                Rational.MINUS_ONE_HALF,
                Rational.of(7, 2)
        };
        Vector v2 = new Vector(arr2);
        assertEquals("[-1/2, 7/2]", v2.toString());

        Rational[] arr4 = {
                Rational.of(1, 2),
                Rational.of(2, 3),
                Rational.of(3, 4),
                Rational.of(3, -4)
        };
        Vector v4 = new Vector(arr4);
        assertEquals("[1/2, 2/3, 3/4, -3/4]", v4.toString());
    }



}
