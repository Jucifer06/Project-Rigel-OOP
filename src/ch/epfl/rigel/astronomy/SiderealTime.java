package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Sidereal time
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class SiderealTime {

    private static final double MILLIS_PER_HOUR = 1000d * 60d * 60d;
    private static final Polynomial calculationS0 = Polynomial.of(
            0.000025862, 2400.051336, 6.697374558);

    /**
     * Return the sidereal time of Greenwich (in radians) included in [0,2π[ for a specific time
     *
     * @param when : the time when the calculation takes place
     * @return : the sidereal time of Greenwich for a specific time
     */
    public static double greenwich(ZonedDateTime when) {
        when = when.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime whenMidnight = when.truncatedTo(ChronoUnit.DAYS);
        double T = Epoch.J2000.julianCenturiesUntil(whenMidnight);
        double t = whenMidnight.until(when, ChronoUnit.MILLIS) / MILLIS_PER_HOUR;
        double s0 = calculationS0.at(T);
        double s1 = 1.002737909 * t;
        return Angle.normalizePositive(Angle.ofHr(s0 + s1));
    }

    /**
     * Return the local sidereal time (in radians) included in [0,2π[ for a specific time and place
     *
     * @param when  : the time when the calculation takes place
     * @param where : the place where the calculation takes place
     * @return : the local sidereal time  for a specific time and place
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        double sl = greenwich(when) + where.lon();
        return Angle.normalizePositive(sl);
    }

    /**
     * Private constructor of SiderealTime (for it to be non-instantiable)
     */
    private SiderealTime() {
    }
}
