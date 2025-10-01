package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * A model of a celestial object
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public interface CelestialObjectModel<O> {
    /**
     * Return the modeled object for the number of days after J2010
     *
     * @param daysSinceJ2010                 : the number of days after J2010 (can be negative)
     * @param eclipticToEquatorialConversion : a conversion from ecliptic to equatorial conversion
     * @return : the modeled object for the number of days after J2010
     */
    O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
