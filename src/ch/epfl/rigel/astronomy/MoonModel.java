package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;


/**
 * A moon model
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;

    private static final double MEAN_LON = Angle.ofDeg(91.929336);
    private static final double MEAN_LON_AT_PERIGEE = Angle.ofDeg(130.143076);
    private static final double ASCENDING_NODE_LON = Angle.ofDeg(291.682547);
    private static final double ORBIT_INCLINATION = Angle.ofDeg(5.145396);
    private static final double ORBIT_ECC = 0.0549;

    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        //Sun constants
        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double sunEclipticGeocentricLon = sun.eclipticPos().lon();
        double sunMeanAnomaly = sun.meanAnomaly();

        //Orbital longitude calculation

        double meanOrbitLon = Angle.ofDeg(13.1763966) * daysSinceJ2010 + MEAN_LON;
        double meanAnomaly = meanOrbitLon - Angle.ofDeg(0.1114041) * daysSinceJ2010 - MEAN_LON_AT_PERIGEE;

        double evection = Angle.ofDeg(1.2739) * Math.sin(2 * (meanOrbitLon - sunEclipticGeocentricLon) - meanAnomaly);
        double sunMeanAnomalySinus = Math.sin(sunMeanAnomaly);
        double annualEqCorrection = Angle.ofDeg(0.1858) * sunMeanAnomalySinus;
        double correction3 = Angle.ofDeg(0.37) * sunMeanAnomalySinus;

        double correctedAnomaly = meanAnomaly + evection - annualEqCorrection - correction3;

        double centerEqCorrection = Angle.ofDeg(6.2886) * Math.sin(correctedAnomaly);
        double correction4 = Angle.ofDeg(0.214) * Math.sin(2 * correctedAnomaly);

        double correctedOrbitLon = meanOrbitLon + evection + centerEqCorrection - annualEqCorrection + correction4;

        double variation = Angle.ofDeg(0.6583) * Math.sin(2 * (correctedOrbitLon - sunEclipticGeocentricLon));

        double trueOrbitLon = correctedOrbitLon + variation;


        //Ecliptic position calculations

        double ascendingNodeMeanLon = ASCENDING_NODE_LON - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double correctedAscendingNodeLon = ascendingNodeMeanLon - Angle.ofDeg(0.16) * sunMeanAnomalySinus;

        double difference2 = trueOrbitLon - correctedAscendingNodeLon;
        double difference2Sinus = Math.sin(difference2);
        double eclipticLon = Math.atan2(
                difference2Sinus * Math.cos(ORBIT_INCLINATION),
                Math.cos(difference2)) + correctedAscendingNodeLon;
        double eclipticLat = Math.asin(difference2Sinus * Math.sin(ORBIT_INCLINATION));


        //Moon phase
        double phase = (1 - Math.cos(trueOrbitLon - sunEclipticGeocentricLon)) / 2;

        //Moon angular size
        double distanceEarthMoon = (1 - ORBIT_ECC * ORBIT_ECC) /
                (1 + ORBIT_ECC * Math.cos(correctedAnomaly + centerEqCorrection));
        double angularSize = Angle.ofDeg(0.5181) / (distanceEarthMoon);

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(
                Angle.normalizePositive(eclipticLon),
                eclipticLat);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Moon(equatorialCoordinates, (float) angularSize, 0, (float) phase);
    }
}
