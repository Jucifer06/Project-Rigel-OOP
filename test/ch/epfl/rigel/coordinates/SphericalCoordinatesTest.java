package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class SphericalCoordinatesTest {
    @Test
    public void EqualsGetWrong() {
        Object object = new Object();
        HorizontalCoordinates hor = HorizontalCoordinates.of(1, 1);

        assertThrows(UnsupportedOperationException.class, () -> {
            hor.equals(object);
        });
    }

    @Test
    public void HashCodeGetWrong() {
        HorizontalCoordinates hor = HorizontalCoordinates.of(1, 1);
        assertThrows(UnsupportedOperationException.class, () -> {
            int i = hor.hashCode();
        });
    }
}
