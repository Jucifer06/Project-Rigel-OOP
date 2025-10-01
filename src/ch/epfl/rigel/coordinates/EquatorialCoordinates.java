package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.Interval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkInInterval;

/**
 * equatorial coordinates
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class EquatorialCoordinates extends SphericalCoordinates {

    private static final Interval RIGHT_OPEN_INTERVAL_0_TO_TAU = RightOpenInterval.of(0, Angle.TAU);
    private static final Interval CLOSED_INTERVAL_AROUND_0_SIZE_PI = ClosedInterval.symmetric(Angle.TAU / 2);

    /**
     * Check if the equatorial coordinates can be constructed and construct them
     *
     * @param ra  : the right ascension (must be in [0,  2π])
     * @param dec : the declination (must be in [-π/2,  π/2] )
     * @return : the constructed equatorial coordinates
     * @throws : IllegalArgumentException if one the the
     *           argument is invalid (not included in [0, 24h[ and [-π/2,  π/2])
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        return new EquatorialCoordinates(checkInInterval(RIGHT_OPEN_INTERVAL_0_TO_TAU, ra),
                checkInInterval(CLOSED_INTERVAL_AROUND_0_SIZE_PI, dec));
    }

    /**
     * Construct equatorial coordinates
     *
     * @param ra  : the right ascension
     * @param dec : the declination
     */
    private EquatorialCoordinates(double ra, double dec) {
        super(ra, dec);
    }

    /**
     * Getter of the right ascension in radian
     *
     * @return : right ascension in radian
     */
    public double ra() {
        return super.lon();
    }

    /**
     * Getter of the right ascension in degrees
     *
     * @return : right ascension in degrees
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     * Getter of the right ascension in hours
     *
     * @return : right ascension in hours
     */
    public double raHr() {
        return Angle.toHr(ra());
    }

    /**
     * Getter of the declination in radian
     *
     * @return : right declination in radian
     */
    public double dec() {
        return super.lat();
    }

    /**
     * Getter of the declination in degrees
     *
     * @return : right declination in degress
     */
    public double decDeg() {
        return super.latDeg();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
    }
}
