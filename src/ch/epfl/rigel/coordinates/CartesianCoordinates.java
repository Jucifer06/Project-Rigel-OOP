package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * cartesian coordinates
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class CartesianCoordinates {

    /**
     * Construct and return the cartesian coordinates
     *
     * @param x : the abscissa
     * @param y : the ordinate
     * @return : the constructed cartesian coordinates
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    private final double abscissa;
    private final double ordinate;

    /**
     * Private constructor of the cartesian coordinates
     *
     * @param x : the abscissa
     * @param y : the ordinate
     */
    private CartesianCoordinates(double x, double y) {
        abscissa = x;
        ordinate = y;
    }

    /**
     * Return the abscissa
     *
     * @return : the abscissa
     */
    public double x() {
        return abscissa;
    }

    /**
     * Return the ordinate
     *
     * @return : the ordinate
     */
    public double y() {
        return ordinate;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(abscissa=%.4f°, ordinate=%.4f°)", abscissa, ordinate);
    }

    @Override
    final public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
