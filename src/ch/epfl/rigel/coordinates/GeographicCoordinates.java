package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkInInterval;

/**
 * Geographic Coordinates
 *
 * @author Paola Matta (296390)
 * @author Juliette Parchet (295888)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    private static final Interval RIGHT_OPEN_INTERVAL_AROUND_0_SIZE_TAU = RightOpenInterval.symmetric(Angle.TAU);
    private static final Interval CLOSED_INTERVAL_AROUND_0_SIZE_PI = ClosedInterval.symmetric(Angle.TAU / 2);
    private static final Interval RIGHT_OPEN_INTERVAL_AROUND_0_SIZE_360 = RightOpenInterval.symmetric(180 * 2);
    private static final Interval CLOSED_INTERVAL_AROUND_0_SIZE_180 = ClosedInterval.symmetric(90 * 2);

    /**
     * Check if the geographic coordinates can be constructed and construct them
     *
     * @param lonDeg : the longitude in degree (must be in [–180°, +180°[ )
     * @param latDeg : the latitude in degree ( must be in [–90°, +90°])
     * @return : the constructed geographic coordinates
     * @throws : IllegalArgumentException if one the the argument is invalid
     *           (not included in [–180°, +180°[ for lonDeg and [–90°, +90°] for latDeg)
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        return new GeographicCoordinates(checkInInterval(RIGHT_OPEN_INTERVAL_AROUND_0_SIZE_TAU, Angle.ofDeg(lonDeg)),
                checkInInterval(CLOSED_INTERVAL_AROUND_0_SIZE_PI, Angle.ofDeg(latDeg)));
    }

    /**
     * Return true if and only if the angle lonDeg (degree) represent valid longitude (in degree)
     *
     * @param lonDeg : the angle in degree
     * @return : true if and only if the angle lonDeg represent a valid longitude (in degree)
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return RIGHT_OPEN_INTERVAL_AROUND_0_SIZE_360.contains(lonDeg);
    }

    /**
     * Return true if and only if the angle latDeg (degree) represent valid latitude (in degree)
     *
     * @param latDeg : the angle in degree
     * @return : true if and only if the angle latDeg represent a valid latitude (in degree)
     */
    public static boolean isValidLatDeg(double latDeg) {
        return CLOSED_INTERVAL_AROUND_0_SIZE_180.contains(latDeg);
    }

    /**
     * Construct geographic coordinates
     *
     * @param longitude : the longitude
     * @param latitude  : the latitude
     */
    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Return the longitude
     *
     * @return : the longitude
     */
    @Override
    public double lon() {
        return super.lon();
    }

    /**
     * Return the longitude in degree
     *
     * @return : the longitude in degree
     */
    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * Return the latitude
     *
     * @return : the latitude
     */
    @Override
    public double lat() {
        return super.lat();
    }

    /**
     * Return the latitude in degree
     *
     * @return : the latitude in degree
     */
    @Override
    public double latDeg() {
        return Angle.toDeg(lat());
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }
}
