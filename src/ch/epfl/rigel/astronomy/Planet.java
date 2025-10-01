package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * a planet (at a given place)
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class Planet extends CelestialObject {
    /**
     * Constructor of the planet
     *
     * @param name          : the name of the planet
     * @param equatorialPos : the equatorial position of the planet
     * @param angularSize   : the angular size of the planet
     * @param magnitude     : the magnitude of the planet
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}
