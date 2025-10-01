package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyAsterimTest {

    @Test
    public void testConstructor() {
        List<Star> nullList = null;
        List<Star> emptyList = new ArrayList<Star>();

        assertThrows(NullPointerException.class, () -> {
            new Asterism(nullList);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Asterism(emptyList);
        });
    }

    @Test
    public void testGetter() {
        List<Star> starList = new ArrayList<>();
        starList.add(new Star(2, "Etoile1", EquatorialCoordinates.of(0.5, 0.5), 3f, 0f));
        starList.add(new Star(3, "Etoile2", EquatorialCoordinates.of(1, 0.75), 2f, 3f));

        Asterism asterim = new Asterism(starList);
        Assertions.assertEquals(starList, asterim.stars());
    }
}
