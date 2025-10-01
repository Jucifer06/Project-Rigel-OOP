package ch.epfl.rigel.gui;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyBlackBodyColorTest {

    @Test
    public void test() {
        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3798.1409));
        assertEquals(Color.web("#c8d9ff"), BlackBodyColor.colorForTemperature(10500d));
        assertThrows(IllegalArgumentException.class, () -> BlackBodyColor.colorForTemperature(40_000.00001d));
    }


}
