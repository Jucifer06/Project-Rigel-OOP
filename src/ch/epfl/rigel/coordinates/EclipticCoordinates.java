package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkInInterval;

/**
 * ecliptic coordinates
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class EclipticCoordinates extends SphericalCoordinates {

    private static final Interval RIGHT_OPEN_INTERVAL_0_TO_TAU = RightOpenInterval.of(0, Angle.TAU);
    private static final Interval CLOSED_INTERVAL_AROUND_0_SIZE_PI = ClosedInterval.symmetric(Angle.TAU / 2);

    /**
     * Check if the ecliptic coordinates can be constructed and construct them
     *
     * @param lon : the longitude
     * @param lat : the latitude
     * @return : the constructed ecliptic coordinates
     * @throws : IllegalArgumentException if the arguments are invalid (the longitude
     *           not included in [0,2π[ and the latitude not included in [-π/2,π/2]
     */
    public static EclipticCoordinates of(double lon, double lat) {
        return new EclipticCoordinates(checkInInterval(RIGHT_OPEN_INTERVAL_0_TO_TAU, lon),
                checkInInterval(CLOSED_INTERVAL_AROUND_0_SIZE_PI, lat));
    }

    /**
     * construct ecliptic coordinates using the super
     *
     * @param longitude : the longitude
     * @param latitude  : the latitude
     */
    private EclipticCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Return the longitude
     *
     * @return : the longitude
     */
    public double lon() {
        return super.lon();
    }

    /**
     * Return the longitude in degree
     *
     * @return : the longitude in degree
     */
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * Return the latitude
     *
     * @return : the latitude
     */
    public double lat() {
        return super.lat();
    }

    /**
     * Return the latitude in degree
     *
     * @return : the latitude in degree
     */
    public double latDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
    }

}
