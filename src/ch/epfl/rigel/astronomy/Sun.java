package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * the Sun (at a given time)
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class Sun extends CelestialObject {

    private static final String NAME = "Soleil";
    private static final float MAGNITUDE = -26.7f;

    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * the Sun constructor
     *
     * @param eclipticPos   : the ecliptic position of the sun
     * @param equatorialPos : the equatorial position of the sun
     * @param angularSize   : the angular size of the sun
     * @param meanAnomaly   : the mean anomaly of the sun
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super(NAME, equatorialPos, angularSize, MAGNITUDE);
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * Return the ecliptic position
     *
     * @return : the ecliptic position
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     * Return the mean anomaly
     *
     * @return : the mean anomaly
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }
}
