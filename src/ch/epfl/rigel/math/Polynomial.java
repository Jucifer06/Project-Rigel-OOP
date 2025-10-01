package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * A polynomial
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class Polynomial {

    /**
     * Check if the polynomial can be constructed and construct it
     *
     * @param coefficientN : the coefficient of the highest degree (not null)
     * @param coefficients : the other coefficients (in decreasing order of degrees)
     * @return : the polynomial
     * @throws : IllegalArgumentException if coefficientN is null
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    private double[] polFunction;

    /**
     * Construct a new polynomial with the coefficients given (coefficientN and coefficients)
     *
     * @param coefficientN : the coefficient of the highest degree (not null)
     * @param coefficients : the other coefficients (in decreasing order of degrees)
     */
    private Polynomial(double coefficientN, double... coefficients) {
        polFunction = new double[coefficients.length + 1];
        polFunction[0] = coefficientN;
        System.arraycopy(coefficients, 0, polFunction, 1, coefficients.length);
    }

    /**
     * Return the value of the polynomial function tested with an argument
     *
     * @param x : the argument
     * @return : the value of the polynomial function tested with an argument
     */
    public double at(double x) {
        double result = 0.0;
        for (int i = 0; i < polFunction.length; ++i) {
            result = result * x + polFunction[i];
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder polMessage = new StringBuilder();
        int length = polFunction.length;

        for (int i = 0; i < length; ++i) {
            double coeff = polFunction[i];
            if (coeff != 0) {
                if (polMessage.length() != 0 && coeff > 0) {
                    polMessage.append("+");
                }
                if (coeff != 1 && coeff != -1) {
                    polMessage.append(coeff);
                } else if (coeff == -1) {
                    polMessage.append("-");
                }

                if (i < length - 1) {
                    polMessage.append("x");
                    if (i < length - 2) {
                        polMessage.append("^")
                                .append(length - 1 - i);
                    }
                }
            }
        }
        return polMessage.toString();
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
