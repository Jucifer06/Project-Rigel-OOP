package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EclipticCoordinatesTest {

    private double PI = Math.PI;

    @Test
    void checksConstructorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            HorizontalCoordinates.ofDeg(-120, 0);
        });

    }

    @Test
    void checksGettersInRadian() {
        EclipticCoordinates coord = EclipticCoordinates.of(Math.PI / 5, Math.PI / 3);
        assertEquals(Math.PI / 5, coord.lon());
        assertEquals(Math.PI / 3, coord.lat());
    }

    @Test
    void checksGettersInDegrees() {
        EclipticCoordinates coord = EclipticCoordinates.of(Math.PI / 4, Math.PI / 10);
        assertEquals(Angle.toDeg(Math.PI / 4), coord.lonDeg());
        assertEquals(Angle.toDeg(Math.PI / 10), coord.latDeg());
    }

    @Test
    void checksToString() {
        EclipticCoordinates coord = EclipticCoordinates.of(Angle.ofDeg(22.5), Angle.ofDeg(18));
        assertEquals("(λ=22.5000°, β=18.0000°)", coord.toString());
    }


    //Tests epfl
    @Test
    void eclOfWorksWithValidCoordinates() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lon = rng.nextDouble(0, 2d * PI);
            var lat = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EclipticCoordinates.of(lon, lat);
            assertEquals(lon, c.lon(), 1e-8);
            assertEquals(lat, c.lat(), 1e-8);
        }
    }

    @Test
    void eclOfFailsWithInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(2d * PI + 1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(-1e-8, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(0, PI + 1e-8);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EclipticCoordinates.of(0, -(PI + 1e-8));
        });
    }

    @Test
    void lonDegAndLatDegReturnCoordinatesInDegrees() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var lon = rng.nextDouble(0, 2d * PI);
            var lat = rng.nextDouble(-PI / 2d, PI / 2d);
            var c = EclipticCoordinates.of(lon, lat);
            assertEquals(Math.toDegrees(lon), c.lonDeg(), 1e-8);
            assertEquals(Math.toDegrees(lat), c.latDeg(), 1e-8);
        }
    }

    @Test
    void ecEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = EclipticCoordinates.of(0, 0);
            c.equals(c);
        });
    }

    @Test
    void ecHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EclipticCoordinates.of(0, 0).hashCode();
        });
    }


}
