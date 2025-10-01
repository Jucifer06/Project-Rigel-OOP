package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MySunTest {
    @Test
    public void testConstructor() {
        assertThrows(NullPointerException.class, () -> {
            new Sun(null, EquatorialCoordinates.of(1, 0.5), 3, 0.5f);
        });
    }

    @Test
    public void testGetters() {
        Sun s = new Sun(EclipticCoordinates.of(1, 0.5), EquatorialCoordinates.of(1, 0.5), 3, 0.5f);
        assertEquals("Soleil", s.name());
        assertEquals(0.5, s.meanAnomaly());
        assertEquals(1, s.equatorialPos().ra());
        assertEquals(0.5, s.equatorialPos().dec());
        assertEquals(-26.7, s.magnitude(),1e-5);
    }


    //Tests lea

    EquatorialCoordinates eq = EquatorialCoordinates.of(0.4, 0.78);
    float ang = 0.3f;
    float mean = 0.7f;

    EclipticCoordinates e = EclipticCoordinates.of(0.0, 0.0);

    //public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly);


    @Test
    void myConstructorEcliPosNull(){

    }

    @Test
    void myConstructorAngleNeg(){
        ang = -0.3f;
        assertThrows(IllegalArgumentException.class, () -> {
            Sun s = new Sun(e, eq, ang,  mean);
        });
    }

    @Test
    void myConstructorEqNeg(){

    }

    @Test
    void myConstructorNameNull(){

    }
}
