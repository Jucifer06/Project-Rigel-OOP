package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyMoonTest {
    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            Moon m = new Moon(EquatorialCoordinates.of(1, 0.5), 2, 2, 2);
        });
    }

    @Test
    public void testInfo() {
        Moon m = new Moon(EquatorialCoordinates.of(1, 0.5), 2, 2, 0.3333333f);
        assertEquals("Lune (33.3%)", m.info());
    }


    //testsLea

    @Test
    void myConstructorIllegalArgumentExceptionTest1 (){

        EquatorialCoordinates eq = EquatorialCoordinates.of(0.5, 0.4);
        assertThrows(IllegalArgumentException.class, () -> {
            new Moon(eq, 5f, 6f, 3f);
        });
    }

    @Test
    void myConstructorIllegalArgumentExceptionTest2 (){

        EquatorialCoordinates eq = EquatorialCoordinates.of(0.5, 0.4);
        assertThrows(IllegalArgumentException.class, () -> {
            new Moon(eq, 5f, 6f, -2f);
        });
    }

    @Test
    void myConstructorIllegalArgumentExceptionTest3 (){

        EquatorialCoordinates eq = EquatorialCoordinates.of(0.5, 0.4);
        assertThrows(IllegalArgumentException.class, () -> {
            new Moon(eq, -5f, 6f, 0.5f);
        });
    }




    @Test
    void myStringTest (){
        EquatorialCoordinates eq = EquatorialCoordinates.of(0.5, 0.4);
        Moon m = new Moon(eq, 4f, 5f, 0.5f);
        assertEquals("Lune (50.0%)", m.toString());
    }



}
