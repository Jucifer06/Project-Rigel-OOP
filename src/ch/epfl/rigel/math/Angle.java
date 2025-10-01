package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * An angle
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class Angle {

    public static final double TAU = 2d * Math.PI;
    private static final double RAD_PER_HOUR = TAU / 24;
    private static final double HOUR_PER_RAD = 24 / TAU;
    private static final double RAD_PER_SECOND = TAU / (60 * 60 * 360);
    private static final double SECONDS_PER_HOUR = 3600.0;
    private static final double MINUTES_PER_HOUR = 60.0;
    private static final double SECONDS_PER_MINUTE = 60.0;
    private static final RightOpenInterval RIGHT_OPEN_INTERVAL_0_TO_TAU = RightOpenInterval.of(0, TAU);

    /**
     * normalizes the angle rad by reducing it to the interval [0,2π[
     *
     * @param rad : the angle in radians
     * @return : the normalized angle (in radians)
     */
    public static double normalizePositive(double rad) {
        return RIGHT_OPEN_INTERVAL_0_TO_TAU.reduce(rad);
    }

    /**
     * Return the angle corresponding to the number of arc seconds given
     *
     * @param sec : the number of arc seconds (can be negative)
     * @return : the angle corresponding to the number of arc seconds given
     */
    public static double ofArcsec(double sec) {
        return sec * RAD_PER_SECOND;
    }

    /**
     * Return the angle in radians corresponding to the angle deg ° min​′ sec ″
     *
     * @param deg : the degrees
     * @param min : the minutes of degree
     * @param sec : the seconds of degree
     * @return : the angle in radians corresponding to the angle deg ° min​′ sec ″
     * @throws : IllegalArgumentException if the degrees are negative or the minutes are not included between 0 (included) and 60 (excluded),
     *           or if the seconds are not included between 0 (included) and 60 (excluded)
     */
    public static double ofDMS(int deg, int min, double sec) {
        Preconditions.checkArgument(deg >= 0);
        Preconditions.checkArgument(!(!(min >= 0 && min < MINUTES_PER_HOUR) || !(sec >= 0 && sec < SECONDS_PER_MINUTE) || deg < 0));
        double newDeg = deg + sec / SECONDS_PER_HOUR + min / MINUTES_PER_HOUR;
        return Math.toRadians(newDeg);
    }

    /**
     * Return the angle (radians) in degrees
     *
     * @param rad : the angle (in radians)
     * @return the angle (radians) in degrees
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Return the angle (degree) in radians
     *
     * @param deg : the angle (in degree)
     * @return : the angle (degree) in radians
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Return the angle (hour) in radians
     *
     * @param hr : the angle (in hour)
     * @return : the angle (hour) in radians
     */
    public static double ofHr(double hr) {
        return hr * RAD_PER_HOUR;
    }

    /**
     * Return the angle (radian) in hour
     *
     * @param rad : the angle in radians
     * @return : the angle (radian) in hour
     */
    public static double toHr(double rad) {
        return rad * HOUR_PER_RAD;
    }
}
