package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * Stereographic projection
 *
 * @author Paola Matta (296390)
 * @author Juliette Parchet (295888)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double altCenter, sinAltCenter, cosAltCenter, azCenter;

    /**
     * Constructor of a stereographic projection centered in center
     *
     * @param center : the center of the projection
     */
    public StereographicProjection(HorizontalCoordinates center) {
        altCenter = center.alt();
        sinAltCenter = Math.sin(altCenter);
        cosAltCenter = Math.cos(altCenter);
        azCenter = center.az();
    }

    /**
     * Return the horizontal coordinates of the point which projection is the cartesian coordinate point
     *
     * @param xy : the cartesian coordinates point
     * @return :the horizontal coordinates of the point which projection is the cartesian coordinate point
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {
        double az, alt, x, y, cosC, sinC, rho, rhoSqrt;
        x = xy.x();
        y = xy.y();
        rhoSqrt = x * x + y * y;
        rho = Math.sqrt(rhoSqrt);

        cosC = (1 - rhoSqrt) / (rhoSqrt + 1);
        sinC = (2 * rho) / (rhoSqrt + 1);

        if (x == 0 && y == 0) {
            az = azCenter;
            alt = altCenter;
        } else {
            az = Angle.normalizePositive(
                    Math.atan2(x * sinC, rho * cosAltCenter * cosC - y * sinAltCenter * sinC) + azCenter);
            alt = Math.asin(cosC * sinAltCenter + (y * sinC * cosAltCenter) / rho);
        }
        return HorizontalCoordinates.of(az, alt);
    }

    /**
     * Return the projected diameter of a sphere of angular size rad
     * centered in the projection center, assuming that it is on the horizon
     *
     * @param rad : the angular size
     * @return : the projected diameter of a sphere of angular size rad
     * centered in the projection center, assuming that it is on the horizon
     */
    public double applyToAngle(double rad) {
        return 2 * Math.tan(rad / 4.0);
    }

    /**
     * Return the circle radius corresponding to the projection of the parallel passing by the point of coordinates parallel
     *
     * @param parallel : the coordinates of the point
     * @return : the circle radius corresponding to the projection of the parallel passing by the point of coordinates parallel
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return Math.cos(parallel.lat()) / (Math.sin(parallel.lat()) + sinAltCenter);
    }

    /**
     * Return the coordinates of the center corresponding to the parallel projection passing by the point hor
     *
     * @param hor : the point
     * @return : the coordinates of the center corresponding to the parallel projection passing by the point hor
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        double cy = cosAltCenter / (Math.sin(hor.lat()) + sinAltCenter);
        return CartesianCoordinates.of(0, cy);
    }

    /**
     * Return the cartesian coordinates of the projection of the given horizontal coordinates points
     *
     * @param azAlt : the horizontal coordinates point
     * @return : the cartesian coordinates of the projection of the given horizontal coordinates points
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double x, y, d, alt, deltaLanda, sinAlt, cosAlt, cosDeltaLanda;
        alt = azAlt.alt();
        sinAlt = Math.sin(alt);
        cosAlt = Math.cos(alt);
        deltaLanda = azAlt.az() - azCenter;
        cosDeltaLanda = Math.cos(deltaLanda);
        d = 1 / (1 + sinAlt * sinAltCenter + cosAlt * cosAltCenter * cosDeltaLanda);
        x = d * cosAlt * Math.sin(deltaLanda);
        y = d * (sinAlt * cosAltCenter - cosAlt * sinAltCenter * cosDeltaLanda);
        return CartesianCoordinates.of(x, y);
    }

    @Override
    public String toString() {
        return String.format(
                Locale.ROOT,
                "StereographicProjection (longitude=%.4f[rad], latitude=%.4f[rad])", azCenter, altCenter);
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
