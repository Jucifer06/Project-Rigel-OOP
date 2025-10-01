package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * A right open interval
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */

public final class RightOpenInterval extends Interval {

    /**
     * Check if the right open interval, given an upper ad lower bound, can be constructed and construct it
     *
     * @param low  : lower bound of the interval (must be lower than the upper bound)
     * @param high :  upper bound of the interval
     * @return : a right open interval
     * @throws : IllegalArgumentException if the lower bound is bigger or equal to the upper bound
     */
    public static RightOpenInterval of(double low, double high) {
        Preconditions.checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }

    /**
     * Check if the right open interval of a give size can be constructed and construct it
     *
     * @param : size the size of the interval (must be positive)
     * @return : a symmetric right open interval
     * @throws : IllegalArgumentException if the size is negative or zero
     */
    public static RightOpenInterval symmetric(double size) {
        Preconditions.checkArgument(size > 0);
        double halfSize = size / 2;
        return new RightOpenInterval(-halfSize, halfSize);
    }

    /**
     * Constructor of a right open interval
     *
     * @param lowerBound : the lower bound (smaller than the upper bound)
     * @param upperBound :the upper bound
     */
    private RightOpenInterval(double lowerBound, double upperBound) {
        super(lowerBound, upperBound);
    }

    /**
     * Return the reduction of a parameter, according to the interval
     *
     * @param v : parameter
     * @return : the reduction of a parameter, according to the interval
     */
    public double reduce(double v) {
        if (this.contains(v)) return v;
        return this.low() + FloorMode(v - this.low(), this.size());
    }

    /**
     * Return the floorMode of two doubles
     *
     * @param x : first parameter
     * @param y : second parameter
     * @return : the floorMode of two doubles
     */
    private double FloorMode(double x, double y) {
        return x - y * Math.floor(x / y);
    }

    @Override
    public boolean contains(double v) {
        return v >= this.low() && v < this.high();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%f,%f[", this.low(), this.high());
    }
}
