package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * A closed interval
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */

public final class ClosedInterval extends Interval {

    /**
     * Check if the closed interval can be constructed and construct it
     *
     * @param low  lower bound of the interval (must be lower than the upper bound)
     * @param high upper bound of the interval
     * @return a closed interval
     * @throws IllegalArgumentException if the lower bound is bigger or equal to the upper bound of the interval
     */
    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new ClosedInterval(low, high);
    }

    /**
     * Creates a symmetric closed interval of a given size
     *
     * @param size the size of the interval
     * @return a closed interval
     */
    public static ClosedInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        double halfSize = size / 2d;
        return new ClosedInterval(-halfSize, halfSize);
    }

    /**
     * Constructor of a closed interval with the bounds given
     *
     * @param lowerBound : the lower bound
     * @param upperBound : the upper bound
     */
    private ClosedInterval(double lowerBound, double upperBound) {
        super(lowerBound, upperBound);
    }

    /**
     * Return the clipped the given parameter to the interval
     *
     * @param v : the given parameter
     * @return : the clipped the given parameter to the interval
     */
    public double clip(double v) {
        if (v <= this.low()) return this.low();
        else if (v >= this.high()) return this.high();
        return v;
    }

    @Override
    public boolean contains(double v) {
        return v >= this.low() && v <= this.high();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%f,%f]", this.low(), this.high());
    }
}