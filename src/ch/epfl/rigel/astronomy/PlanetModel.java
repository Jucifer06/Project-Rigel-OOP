package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.List;

/**
 * the model of the 8 planets of our solar system
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public enum PlanetModel implements CelestialObjectModel<Planet> {

    MERCURY("Mercure", 0.24085, 75.5671,
            77.612, 0.205627, 0.387098,
            7.0051, 48.449, 6.74, -0.42),

    VENUS("Vénus", 0.615207, 272.30044,
            131.54, 0.006812, 0.723329,
            3.3947, 76.769, 16.92, -4.40),

    EARTH("Terre", 0.999996, 99.556772,
            103.2055, 0.016671, 0.999985,
            0, 0, 0, 0),

    MARS("Mars", 1.880765, 109.09646,
            336.217, 0.093348, 1.523689,
            1.8497, 49.632, 9.36, -1.52),

    JUPITER("Jupiter", 11.857911, 337.917132,
            14.6633, 0.048907, 5.20278,
            1.3035, 100.595, 196.74, -9.40),

    SATURN("Saturne", 29.310579, 172.398316,
            89.567, 0.053853, 9.51134,
            2.4873, 113.752, 165.60, -8.88),

    URANUS("Uranus", 84.039492, 356.135400,
            172.884833, 0.046321, 19.21814,
            0.773059, 73.926961, 65.80, -7.19),

    NEPTUNE("Neptune", 165.84539, 326.895127,
            23.07, 0.010483, 30.1985,
            1.7673, 131.879, 62.20, -6.87);

    private final String frenchName;
    private final double revolutionPeriod, lonAtJ2010, lonAtPerigee, orbitEcc, semiMajorAxis,
            inclinAtEcliptic, lonOfAscendingNode, angularSecSizeAt1UA, magnitudeAt1UA;

    /**
     * Constructor of the enum type
     *
     * @param frenchName            : the french name of the planet
     * @param revolutionPeriod      : the period of revolution of the planet
     * @param lonDegAtJ2010         : the longitude (in degree) at J2010 of the planet
     * @param lonDegAtPerigee       : the longitude (in degree) at the perigee of the planet
     * @param orbitEcc              : the eccentricity of the orbit
     * @param semiMajorAxis         : the Half major axis of the orbit of the planet
     * @param inclinDegAtEcliptic   . the inclination of the orbit at the ecliptic
     * @param lonDegOfAscendingNode : longitude (in degree) of the ascending node
     * @param angularSecSizeAt1UA   : the angular size (in seconds) at 1UA of the planet
     * @param magnitudeAt1UA        : the magnitude at 1UA of the planet
     */
    PlanetModel(String frenchName, double revolutionPeriod, double lonDegAtJ2010, double lonDegAtPerigee, double orbitEcc,
                double semiMajorAxis, double inclinDegAtEcliptic, double lonDegOfAscendingNode, double angularSecSizeAt1UA, double magnitudeAt1UA) {

        this.frenchName = frenchName;
        this.revolutionPeriod = revolutionPeriod;
        this.lonAtJ2010 = Angle.ofDeg(lonDegAtJ2010);
        this.lonAtPerigee = Angle.ofDeg(lonDegAtPerigee);
        this.orbitEcc = orbitEcc;
        this.semiMajorAxis = semiMajorAxis;
        this.inclinAtEcliptic = Angle.ofDeg(inclinDegAtEcliptic);
        this.lonOfAscendingNode = Angle.ofDeg(lonDegOfAscendingNode);
        this.angularSecSizeAt1UA = Angle.ofArcsec(angularSecSizeAt1UA);
        this.magnitudeAt1UA = magnitudeAt1UA;
    }

    public static final List<PlanetModel> ALL = List.of(values());

    private static final double MEAN_ANGULAR_SPEED_EARTH_AROUND_SUN = (Angle.TAU / 365.242191);

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        //Earth
        double earthMeanAnomaly = MEAN_ANGULAR_SPEED_EARTH_AROUND_SUN * (daysSinceJ2010 / EARTH.revolutionPeriod)
                + EARTH.lonAtJ2010 - EARTH.lonAtPerigee;
        double earthTrueAnomaly = earthMeanAnomaly + 2 * EARTH.orbitEcc * Math.sin(earthMeanAnomaly);
        double earthRadius = (EARTH.semiMajorAxis * (1 - EARTH.orbitEcc * EARTH.orbitEcc))
                / (1 + EARTH.orbitEcc * Math.cos(earthTrueAnomaly));
        double earthLon = earthTrueAnomaly + EARTH.lonAtPerigee;

        //Planet
        double meanAnomaly = MEAN_ANGULAR_SPEED_EARTH_AROUND_SUN * (daysSinceJ2010 / revolutionPeriod)
                + lonAtJ2010 - lonAtPerigee;
        double trueAnomaly = meanAnomaly + 2 * orbitEcc * Math.sin(meanAnomaly);
        double radius = (semiMajorAxis * (1 - Math.pow(orbitEcc, 2))) / (1 + orbitEcc * Math.cos(trueAnomaly));
        double heliocentricLon = trueAnomaly + lonAtPerigee;
        double var1 = Math.sin(heliocentricLon - lonOfAscendingNode);
        double latEclipticHeliocentric = Math.asin(var1 * Math.sin(inclinAtEcliptic));
        double projectionRadius = radius * Math.cos(latEclipticHeliocentric);
        double projectionLonOrbit = lonOfAscendingNode + Math.atan2(var1 * Math.cos(inclinAtEcliptic),
                Math.cos(heliocentricLon - lonOfAscendingNode));

        double eclipticGeocentricLon;
        double var2 = earthRadius * Math.sin(projectionLonOrbit - earthLon);

        if (semiMajorAxis < EARTH.semiMajorAxis) {
            eclipticGeocentricLon = Math.PI + earthLon +
                    Math.atan2(
                            (projectionRadius * Math.sin(earthLon - projectionLonOrbit))
                            , (earthRadius - projectionRadius * Math.cos(earthLon - projectionLonOrbit)));
        } else {
            eclipticGeocentricLon = projectionLonOrbit +
                    Math.atan(var2 / (projectionRadius - earthRadius * Math.cos(projectionLonOrbit - earthLon)));
        }
        double eclipticGeocentricLat = Math.atan((projectionRadius * Math.tan(latEclipticHeliocentric)
                * Math.sin(eclipticGeocentricLon - projectionLonOrbit)) / var2);

        double rho = Math.sqrt(
                earthRadius * earthRadius + radius * radius
                        - 2 * earthRadius * radius * Math.cos(heliocentricLon - earthLon) * Math.cos(latEclipticHeliocentric));
        double planetAngularSize = angularSecSizeAt1UA / rho;

        //Magnitude
        double phase = (1 + Math.cos(eclipticGeocentricLon - heliocentricLon)) / 2;
        double magnitude = magnitudeAt1UA + 5 * Math.log10((radius * rho) / Math.sqrt(phase));

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(
                Angle.normalizePositive(eclipticGeocentricLon),
                eclipticGeocentricLat);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Planet(frenchName, equatorialCoordinates, (float) planetAngularSize, (float) magnitude);
    }

}
