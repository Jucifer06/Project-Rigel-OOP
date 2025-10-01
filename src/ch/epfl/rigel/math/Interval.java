package ch.epfl.rigel.math;

/**
 * An interval
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public abstract class Interval {

    private final double lowerBound, upperBound;

    /**
     * Construct an interval given the lower and upper bound
     *
     * @param lowerBound : the lower bound
     * @param upperBound : the upper bound
     */
    protected Interval(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Return the lower bound of the interval
     *
     * @return the lower bound of the interval
     */
    public double low() {
        return lowerBound;
    }

    /**
     * Return the upper bound of the interval
     *
     * @return the upper bound of the interval
     */
    public double high() {
        return upperBound;
    }

    /**
     * Return the size of the interval
     *
     * @return the size of the interval
     */
    public double size() {
        return upperBound - lowerBound;
    }

    /**
     * Check if the argument is contained in the interval
     *
     * @param v : the argument
     * @return : true if the argument is contained in the interval
     */
    abstract public boolean contains(double v);

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }
}