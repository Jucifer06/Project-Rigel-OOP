package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

/**
 * Preconditions
 *
 * @author Paola Matta (296390)
 * @author Juliette Parchet (295888)
 */
public class Preconditions {

    /**
     * Default constructor
     */
    private Preconditions() {
    }

    /**
     * throws an exception if the argument is false, and else do nothing
     *
     * @param isTrue : the argument that will be tested
     * @throws : IllegalArgumentException if isTrue is false
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            String e = "argument not checked";
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * throws an exception if the value is not included in the interval and else return the value
     *
     * @param interval : an interval
     * @param value    : a value
     * @return : the value
     * @throws : IllegalArgumentException if the value is not included in the interval
     */
    public static double checkInInterval(Interval interval, double value) {
        if (!(interval.contains(value))) {
            String e = "not in interval";
            throw new IllegalArgumentException(e);
        }
        return value;
    }
}
