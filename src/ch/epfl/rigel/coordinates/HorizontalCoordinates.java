package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkInInterval;

/**
 * Horizontal Coordinates
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */

public final class HorizontalCoordinates extends SphericalCoordinates {

    private static final RightOpenInterval RIGHT_OPEN_INTERVAL_0_TO_TAU = RightOpenInterval.of(0, Angle.TAU);
    private static final ClosedInterval CLOSED_INTERVAL_AROUND_0_SIZE_PI = ClosedInterval.symmetric(Angle.TAU / 2);

    /**
     * Creates Horizontal Coordinates with radian
     *
     * @param az  azimut in radian (must be in [0, +2π[ )
     * @param alt altitude in radian (must be in [-π/2, π/2[ )
     * @return Horizontal Coordinates
     * @throws : IllegalArgumentException if one the the
     *           *           argument is invalid (not included in [0, +2π[ and [-π/2, π/2[ )
     */
    public static HorizontalCoordinates of(double az, double alt) {
        return new HorizontalCoordinates(checkInInterval(RIGHT_OPEN_INTERVAL_0_TO_TAU, az),
                checkInInterval(CLOSED_INTERVAL_AROUND_0_SIZE_PI, alt));
    }

    /**
     * Creates Horizontal Coordinates with degrees
     *
     * @param azDeg  azimut in degrees (must be in [0°, +360°[ )
     * @param altDeg altitude in degrees (must be in [–90°, +90°] )
     * @return Horizontal Coordinates
     * @throws : IllegalArgumentException if one the the
     *           *           argument is invalid (not included in [0°, +360°[ and [–90°, +90°])
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        double az = Angle.ofDeg(azDeg);
        double alt = Angle.ofDeg(altDeg);
        return new HorizontalCoordinates(checkInInterval(RIGHT_OPEN_INTERVAL_0_TO_TAU, az),
                checkInInterval(CLOSED_INTERVAL_AROUND_0_SIZE_PI, alt));
    }

    /**
     * Construct horizontal coordinates coordinates with the longitude and latitude given
     *
     * @param longitude : the longitude
     * @param latitude  : the latitude
     */
    private HorizontalCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Getter of the azimut in radian
     *
     * @return azimut in radian
     */
    public double az() {
        return super.lon();
    }

    /**
     * Getter of the azimut in degrees
     *
     * @return azimut in degrees
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * Getter of the altitude in radian
     *
     * @return altitude in radian
     */
    public double alt() {
        return super.lat();
    }

    /**
     * Getter of the altitude in degrees
     *
     * @return altitude in degrees
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     * returns the name of the octan to which the position belongs
     *
     * @param n : String for North
     * @param e : String for East
     * @param s : String for South
     * @param w : String for West
     * @return : the name of the octan to which the position belongs
     */
    public String azOctantName(String n, String e, String s, String w) {
        StringBuilder builder = new StringBuilder();

        double angleDeg = azDeg();
        // the angle is shifted by 22.5° over the compass dial
        double shiftedAngleDeg = (angleDeg + 22.5) % 360;
        // we then determine in which of the 8 dials the angle is
        int dial = (int) (shiftedAngleDeg / 45);

        switch (dial) {
            case 0:
                builder.append(n);
                break;
            case 1:
                builder.append(n).append(e);
                break;
            case 2:
                builder.append(e);
                break;
            case 3:
                builder.append(s).append(e);
                break;
            case 4:
                builder.append(s);
                break;
            case 5:
                builder.append(s).append(w);
                break;
            case 6:
                builder.append(w);
                break;
            case 7:
                builder.append(n).append(w);
                break;
        }
        return builder.toString();
    }

    /**
     * Return the angular distance between the receptor and the point given
     *
     * @param that : the point given
     * @return : the angular distance between the receptor and the point given
     */

    public double angularDistanceTo(HorizontalCoordinates that) {
        double dist = Math.acos(
                Math.sin(this.alt()) * Math.sin(that.alt()) +
                        Math.cos(this.alt()) * Math.cos(that.alt()) * Math.cos(this.az() - that.az())
        );
        return dist;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg());
    }
}


