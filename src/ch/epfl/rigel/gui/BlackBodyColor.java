package ch.epfl.rigel.gui;

import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * the color of the black body
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */

public final class BlackBodyColor {
    public static Map<Integer, Color> MAP = createMap();
    private static final Interval CLOSED_INTERVAL_1000_TO_40000 = ClosedInterval.of(1_000, 40_000);

    /**
     * The private constructor of the black body color
     */
    private BlackBodyColor() {
    }

    /**
     * Return the closest color corresponding to temperature (in kelvin)
     *
     * @param temperatureKelvin : the temperature (in kelvin)
     * @return : the color corresponding to the temperature (in kelvin)
     * @throws IllegalArgumentException : if the temperature in kelvin is not in the interval [1'000,40'000]
     */
    public static Color colorForTemperature(double temperatureKelvin) throws IllegalArgumentException {
        if (!CLOSED_INTERVAL_1000_TO_40000.contains(temperatureKelvin)) throw new IllegalArgumentException();
        int roundedTemperatureKelvin = ((((int) temperatureKelvin + 99) / 100) * 100);
        return MAP.get(roundedTemperatureKelvin);
    }

    /**
     * Return the map mapping a temperature (in kelvin) to a color, using the resource bbr_color.txt
     *
     * @return : the map mapping a temperature (in kelvin) to a color, using the resource bbr_color.txt
     */
    private static Map<Integer, Color> createMap() {
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(BlackBodyColor.class.getResourceAsStream("/bbr_color.txt"), US_ASCII))) {
            Map<Integer, Color> map = new HashMap<>();
            String tenDegrees = "10deg";
            char sharp = '#';
            while (input.ready()) {
                String line = input.readLine();
                if (!line.isBlank() && line.charAt(0) != sharp && line.substring(10, 15).equals(tenDegrees)) {
                    int shifting = 0;
                    if (line.substring(1, 2).isBlank()) {
                        ++shifting;
                    }
                    int kelvinTemp = Integer.parseInt(line.substring(1 + shifting, 6));
                    Color color = Color.web(line.substring(80, 87));
                    map.put(kelvinTemp, color);
                }
            }
            return map;
        } catch (IOException e) {
            throw new UncheckedIOException((e));
        }
    }
}
