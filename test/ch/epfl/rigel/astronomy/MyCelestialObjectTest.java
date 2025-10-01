package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyCelestialObjectTest {
    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Planet("Mars", EquatorialCoordinates.of(1, 0.5), -0.1f, 0.4f);
        });
        assertThrows(NullPointerException.class, () -> {
            new Planet("Mars", null, 3f, 0.4f);
        });
        assertThrows(NullPointerException.class, () -> {
            new Planet(null, EquatorialCoordinates.of(1, 0.5), 3f, 0.4f);
        });
    }

    @Test
    public void testGetters() {
        CelestialObject o = new Planet("Mars", EquatorialCoordinates.of(1, 0.5), 0.1f, 0.4f);
        assertEquals("Mars", o.name());
        assertEquals(0.1f, o.angularSize());
        assertEquals(0.4f, o.magnitude());
        assertEquals(1, o.equatorialPos().ra());
        assertEquals(0.5, o.equatorialPos().dec());
    }

    @Test
    public void testInfoString() {
        CelestialObject o = new Sun(EclipticCoordinates.of(1, 0.5), EquatorialCoordinates.of(1, 0.5), 0.1f, 0.4f);
        String Newligne = System.getProperty("line.separator");
        assertEquals("Celestial object" + Newligne + "Name: Soleil" + Newligne + "Equatorial position: 1.0 right ascension, 0.5 declinaison" + Newligne + "Angular size: 0.1" + Newligne + "Magnitude: -26.7", o.info());
        assertEquals("Celestial object" + Newligne + "Name: Soleil" + Newligne + "Equatorial position: 1.0 right ascension, 0.5 declinaison" + Newligne + "Angular size: 0.1" + Newligne + "Magnitude: -26.7", o.toString());

        CelestialObject o2 = new Planet("Mars", EquatorialCoordinates.of(1.2, 0.3), 0.5f, 0.6f);
        assertEquals("Celestial object" + Newligne + "Name: Mars" + Newligne + "Equatorial position: 1.2 right ascension, 0.3 declinaison" + Newligne + "Angular size: 0.5" + Newligne + "Magnitude: 0.6", o2.info());
        assertEquals("Celestial object" + Newligne + "Name: Mars" + Newligne + "Equatorial position: 1.2 right ascension, 0.3 declinaison" + Newligne + "Angular size: 0.5" + Newligne + "Magnitude: 0.6", o2.toString());


    }
}
