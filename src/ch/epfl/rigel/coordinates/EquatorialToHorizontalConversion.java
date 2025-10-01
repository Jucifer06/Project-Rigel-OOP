package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import static java.lang.Math.*;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Equatorial to horizontal converter
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double sinLat, cosLat, sl;

    /**
     * Constructor of an equatorial to horizontal converter for a specific time, zone and place
     *
     * @param when  : a time and zone when the future conversions will take place
     * @param where : the place (geographic coordinates)
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        sl = SiderealTime.local(when, where);
        double lat = where.lat();
        sinLat = sin(lat);
        cosLat = cos(lat);
    }

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equatorialCoordinates) {
        double ra = equatorialCoordinates.ra();
        double H = sl - ra;
        double dec = equatorialCoordinates.dec();
        double sinDec = sin(dec);
        double cosDec = cos(dec);
        double sinHigh = sinDec * sinLat + cosDec * cosLat * cos(H);
        double az = Angle.normalizePositive(atan2(-cosDec * cosLat * sin(H), sinDec - sinLat * sinHigh));

        return HorizontalCoordinates.of(az, asin(sinHigh));
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
