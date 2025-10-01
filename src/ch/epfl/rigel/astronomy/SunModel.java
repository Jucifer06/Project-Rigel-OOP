package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * a sun model
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public enum SunModel implements CelestialObjectModel<Sun> {

    SUN;

    private static final double SUN_LON_AT_J_2010 = Angle.ofDeg(279.557208);
    private static final double SUN_LON_AT_PERIGEE = Angle.ofDeg(283.112438);
    private static final double ORBIT_ECC = 0.016705;
    private static final double TROPICAL_YEAR = 365.242191;
    private static final double TETA_ZERO = Angle.ofDeg(0.533128);

    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAnomaly = SUN_LON_AT_J_2010 - SUN_LON_AT_PERIGEE + (Angle.TAU * daysSinceJ2010 / TROPICAL_YEAR);
        double trueAnomaly = meanAnomaly + 2 * ORBIT_ECC * Math.sin(meanAnomaly);
        double sunAngularSize = TETA_ZERO * (1 + ORBIT_ECC * Math.cos(trueAnomaly)) / (1 - ORBIT_ECC * ORBIT_ECC);

        double sunEclipticLon = trueAnomaly + SUN_LON_AT_PERIGEE;
        double sunEclipticLat = 0;

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(
                Angle.normalizePositive(sunEclipticLon),
                sunEclipticLat);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Sun(eclipticCoordinates, equatorialCoordinates, (float) sunAngularSize, (float) meanAnomaly);
    }
}
