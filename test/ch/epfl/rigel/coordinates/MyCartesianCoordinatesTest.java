package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyCartesianCoordinatesTest {
    @Test
    public void testEquals_hashCode() {
        CartesianCoordinates c = CartesianCoordinates.of(20, 20);
        assertThrows(UnsupportedOperationException.class, () -> {
            c.equals(CartesianCoordinates.of(20, 20));
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            c.hashCode();
        });
    }

    @Test
    public void testToString() {
        CartesianCoordinates c = CartesianCoordinates.of(15.645328, 162.34258910373562);
        assertEquals("(abscissa=15.6453°, ordinate=162.3426°)", c.toString());
        CartesianCoordinates c2 = CartesianCoordinates.of(0, 123.123);
        assertEquals("(abscissa=0.0000°, ordinate=123.1230°)", c2.toString());
    }

    @Test
    public void testGetters() {
        CartesianCoordinates c = CartesianCoordinates.of(15.645328, 162.34258910373562);
        assertEquals(15.645328, c.x());
        assertEquals(162.34258910373562, c.y());
    }
}
