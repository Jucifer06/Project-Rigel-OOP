package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static java.lang.Math.*;

/**
 * Ecliptic to equatorial converter
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final static Polynomial poly = Polynomial.of(
            Angle.ofArcsec(0.00181),
            Angle.ofArcsec(-0.0006),
            Angle.ofArcsec(-46.815),
            Angle.ofDMS(23, 26, 21.45));

    private final double cosObliquity, sinObliquity;

    /**
     * Constructor of an ecliptic to equatorial converter for a specific time and zone
     *
     * @param when : a time and zone when the future conversions will take place
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double T = Epoch.J2000.julianCenturiesUntil(when);
        double obliquity = poly.at(T);
        cosObliquity = cos(obliquity);
        sinObliquity = sin(obliquity);
    }

    @Override
    public EquatorialCoordinates apply(EclipticCoordinates eclipticCoordinates) {
        double lon = eclipticCoordinates.lon();
        double lat = eclipticCoordinates.lat();
        double sinLon = sin(lon);
        double ra = Angle.normalizePositive(
                atan2(sinLon * cosObliquity - tan(lat) * sinObliquity, cos(lon)));
        double dec = asin(sin(lat) * cosObliquity + cos(lat) * sinObliquity * sinLon);
        return EquatorialCoordinates.of(ra, dec);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
