package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Enumeration of epochs
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public enum Epoch {

    J2000(ZonedDateTime.of(
            LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.NOON,
            ZoneOffset.UTC)),
    J2010(ZonedDateTime.of(
            LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC));

    private final ZonedDateTime when;

    /**
     * Constructor of an epoch
     *
     * @param when : indicate the time of the epoch
     */
    Epoch(ZonedDateTime when) {
        this.when = when;
    }

    private static final double MILLIS_PER_DAY = 1000d * 60d * 60d * 24d;
    private static final double MILLIS_PER_JULIAN_CENTURIES = MILLIS_PER_DAY * 365.25 * 100;

    /**
     * Return the number of days until a specific date (and zone)
     *
     * @param when : the date and zone
     * @return : the number of days until a specific date (and zone)
     */
    public double daysUntil(ZonedDateTime when) {
        double t = this.when.until(when, ChronoUnit.MILLIS);
        return t / MILLIS_PER_DAY;
    }

    /**
     * Return the number of julian centuries until a specific date (and zone)
     *
     * @param when : the date and zone
     * @return : the number of julian centuries until a specific date (and zone)
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        double t = this.when.until(when, ChronoUnit.MILLIS);
        return t / MILLIS_PER_JULIAN_CENTURIES;
    }
}
