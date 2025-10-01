package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * a star
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */

public final class Star extends CelestialObject {

    private static final ClosedInterval COLOR_INDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    private final int hipparcosId;
    private final float colorIndex;
    private int colorTemperature;

    /**
     * Constructor of the Star
     *
     * @param hipparcosId   : the Hipparcos number
     * @param name          : the name of the star
     * @param equatorialPos : the equatorial position of the star
     * @param magnitude     : the magnitude of the star
     * @param colorIndex    : the color index
     * @throws : IllegalArgumentException if the Hipparcos number is negative or if the color index is not contained is in [-0.5, 5.5]
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name, equatorialPos, 0, magnitude);
        Preconditions.checkArgument(!(hipparcosId < 0 || !(COLOR_INDEX_INTERVAL.contains(colorIndex))));
        this.hipparcosId = hipparcosId;
        this.colorIndex = colorIndex;
        colorTemperature = (int) (4600 * (1 / (0.92 * colorIndex + 1.7) + 1 / (0.92 * colorIndex + 0.62)));
    }

    /**
     * return the Hipparcos number
     *
     * @return : the Hipparcos number
     */
    public int hipparcosId() {
        return hipparcosId;
    }

    /**
     * Return the color temperature of the star in Kelvin (rounded down)
     *
     * @return : the color temperature of the star in Kelvin (rounded down)
     */
    public int colorTemperature() {
        return colorTemperature;
    }
}
