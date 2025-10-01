package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyStarTest {

    @Test
    void StarConstructorTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Star(-1,"etoile", EquatorialCoordinates.of(0.5,0.5),2.7f,1f);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Star(10,"etoile", EquatorialCoordinates.of(0.5,0.5),2.7f,5.55f);
        });
    }

    @Test
    void GetterHipparcosTest() {
        Star star = new Star(2,"star1", EquatorialCoordinates.of(0.5,0.5),2.5f, 1f) ;
       assertEquals(2, star.hipparcosId());
    }

    @Test
    void colorTemperatureTest() {
        Star star = new Star(2,"star1", EquatorialCoordinates.of(0.5,0.5),2.5f, 2.75f) ;
        Assertions.assertEquals(2547, star.colorTemperature());
    }


}
