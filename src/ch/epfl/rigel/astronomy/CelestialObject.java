package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * A celestial object
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */

public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize, magnitude;

    /**
     * The constructor of the celestial object
     *
     * @param name          : name of the celestial object
     * @param equatorialPos : the equatorial position of the object
     * @param angularSize   : the angular size of the object
     * @param magnitude     : the magnitude of the object
     * @throws : IllegalArgumentException if the angular size is negative
     * @throws : NullPointerException if the name or the equatorial position are null
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        Preconditions.checkArgument(angularSize >= 0);
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * Return the name of the celestial object
     *
     * @return : the name of the celestial object
     */
    public String name() {
        return name;
    }

    /**
     * return the equatorial position of the celestial object
     *
     * @return : the equatorial position of the celestial object
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    /**
     * return the angular size of the celestial object
     *
     * @return : the angular size of the celestial object
     */
    public double angularSize() {
        return angularSize;
    }

    /**
     * return the magnitude of the celestial object
     *
     * @return : the magnitude of the celestial object
     */
    public double magnitude() {
        return magnitude;
    }

    /**
     * return a message containing information about the celestial object
     *
     * @return : a message containing information about the celestial object
     */
    public String info() {
        return name();
    }

    @Override
    public String toString() {
        return info();
    }
}
