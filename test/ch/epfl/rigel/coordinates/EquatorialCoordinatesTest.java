package ch.epfl.rigel.coordinates;

import
        ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class EquatorialCoordinatesTest {

    private double PI = Math.PI;

    @Test
    void checkOfDegConstructorException() {
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(Math.PI, 40);
        });
    }

    @Test
    void checksGetterRaInRadian() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(Math.PI, Angle.ofDeg(67));
        assertEquals(Math.PI, coord.ra());
    }

    @Test
    void checksGetterDecInRadian() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(Angle.ofDeg(67), Angle.ofDeg(67));
        assertEquals(Angle.ofDeg(67), coord.dec());
    }

    @Test
    void checksGetterRaInDegrees() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(Math.PI / 10, Angle.ofDeg(67));
        assertEquals(Angle.toDeg(Math.PI / 10), coord.raDeg());
    }

    @Test
    void checksGetterDecInDegrees() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(Math.PI / 8, Angle.ofDeg(67));
        assertEquals(67, coord.decDeg());
    }

    @Test
    void checksToString() {
        EquatorialCoordinates coord = EquatorialCoordinates.of(Math.PI, Angle.ofDeg(45));
        assertEquals("(ra=12.0000h, dec=45.0000°)", coord.toString());
    }

    @Test
    void ofFailsOnInvalidValues() {
        double pi = Math.PI;
        assertThrows(IllegalArgumentException.class, () -> EquatorialCoordinates.of(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> EquatorialCoordinates.of(-0.1, pi / 2));
        assertThrows(IllegalArgumentException.class, () -> EquatorialCoordinates.of(0, (-pi - 0.1) / 2));
        assertThrows(IllegalArgumentException.class, () -> EquatorialCoordinates.of(3 * pi, 0));
        assertThrows(IllegalArgumentException.class, () -> EquatorialCoordinates.of(2 * pi, 0));
        assertThrows(IllegalArgumentException.class, () -> EquatorialCoordinates.of(0, (pi + 0.1) / 2));
    }


    @Test
    void ra() {
        RightOpenInterval interval = RightOpenInterval.of(0, Math.PI * 2);
        var rng = TestRandomizer.newRandom();
        double value;
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; ++i) {
            value = rng.nextDouble(interval.low(), interval.high());
            EquatorialCoordinates e = EquatorialCoordinates.of(value, 0);
            assertEquals(value, e.ra(), 1e-6);
        }
    }

    @Test
    void raDeg() {
        RightOpenInterval interval = RightOpenInterval.of(0, 360);
        var rng = TestRandomizer.newRandom();
        double value;
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; ++i) {
            value = rng.nextDouble(interval.low(), interval.high());
            EquatorialCoordinates e = EquatorialCoordinates.of(Angle.ofDeg(value), 0);
            assertEquals(value, e.raDeg(), 1e-6);
        }
    }

    @Test
    void raHr() {
        RightOpenInterval interval = RightOpenInterval.of(0, Angle.toHr(Math.PI * 2));
        var rng = TestRandomizer.newRandom();
        double value;
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; ++i) {
            value = rng.nextDouble(interval.low(), interval.high());
            EquatorialCoordinates e = EquatorialCoordinates.of(Angle.ofHr(value), 0);
            assertEquals(value, e.raHr(), 1e-6);
        }
    }

    @Test
    void dec() {
        RightOpenInterval interval = RightOpenInterval.of(-Math.PI / 2, Math.PI / 2);
        var rng = TestRandomizer.newRandom();
        double value;
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; ++i) {
            value = rng.nextDouble(interval.low(), interval.high());
            EquatorialCoordinates e = EquatorialCoordinates.of(0, value);
            assertEquals(value, e.dec(), 1e-6);
        }
    }

    @Test
    void decDeg() {
        RightOpenInterval interval = RightOpenInterval.symmetric(180);
        var rng = TestRandomizer.newRandom();
        double value;
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; ++i) {
            value = rng.nextDouble(interval.low(), interval.high());
            EquatorialCoordinates e = EquatorialCoordinates.of(0, Angle.ofDeg(value));
            assertEquals(value, e.decDeg(), 1e-6);
        }
    }

    @Test
    void testToString() {
        double ra = 0.0025;
        double dec = 1.0024;
        String s = String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", Angle.toHr(ra), Angle.toDeg(dec));

        EquatorialCoordinates e = EquatorialCoordinates.of(ra, dec);
        assertEquals(s, e.toString());
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var equaCoord = EquatorialCoordinates.of(1, 1);
            equaCoord.equals(equaCoord);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EquatorialCoordinates.of(1, 1).hashCode();
        });
    }

    //Tests EPFL
    @Test
    void equOfWorksWithValidCoordinates() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * PI);
            var dec = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EquatorialCoordinates.of(ra, dec);
            assertEquals(ra, c.ra(), 1e-8);
            assertEquals(dec, c.dec(), 1e-8);
        }
    }

    @Test
    void equOfFailsWithInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(2d * PI + 1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(-1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(0, PI + 1e-8);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(0, -(PI + 1e-8));
        });
    }

    @Test
    void raDegAndDecDegReturnCoordinatesInDegrees() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * PI);
            var dec = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EquatorialCoordinates.of(ra, dec);
            assertEquals(Math.toDegrees(ra), c.raDeg(), 1e-8);
            assertEquals(Math.toDegrees(dec), c.decDeg(), 1e-8);
        }
    }

    @Test
    void raHrReturnsRightAscensionInHours() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var ra = rng.nextDouble(0, 2d * PI);
            var dec = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EquatorialCoordinates.of(ra, dec);
            assertEquals(Math.toDegrees(ra) / 15d, c.raHr(), 1e-8);
        }
    }

    @Test
    void equEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = EquatorialCoordinates.of(0, 0);
            c.equals(c);
        });
    }

    @Test
    void equHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EquatorialCoordinates.of(0, 0).hashCode();
        });
    }

}
