package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * An observed sky at a given time
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class ObservedSky {

    private static final List<PlanetModel> PLANET_MODELS = PlanetModel.ALL;

    private final CartesianCoordinates sunCart, moonCart;
    private final List<CartesianCoordinates> planetsCart, starsCart;
    private final Map<CelestialObject, CartesianCoordinates> observedSky = new HashMap<>();
    private final Sun sun;
    private final Moon moon;
    private final List<Planet> planets;
    private final StarCatalogue catalogue;

    /**
     * the constructor of the observed sky
     *
     * @param when       : the observation time
     * @param where      : the observation position
     * @param projection : the stereographic projection to use
     * @param catalogue  : the catalogue containing the stars and asterisms
     */
    public ObservedSky(ZonedDateTime when, GeographicCoordinates where, StereographicProjection projection, StarCatalogue catalogue) {
        this.catalogue = catalogue;

        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(when);
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(when, where);

        double daysSinceJ2010 = Epoch.J2010.daysUntil(when);

        //Sun
        sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        EquatorialCoordinates sunEquatorial = sun.equatorialPos();
        sunCart = projection.apply(equatorialToHorizontalConversion.apply(sunEquatorial));
        observedSky.put(sun, sunCart);

        //Moon
        moon = MoonModel.MOON.at(daysSinceJ2010, eclipticToEquatorialConversion);
        EquatorialCoordinates moonEquatorial = moon.equatorialPos();
        moonCart = projection.apply(equatorialToHorizontalConversion.apply(moonEquatorial));
        observedSky.put(moon, moonCart);

        //Planets
        planetsCart = new ArrayList<>();
        planets = new ArrayList<>();
        for (PlanetModel p : PLANET_MODELS) {
            if (!(p == PlanetModel.EARTH)) {
                Planet planet = p.at(daysSinceJ2010, eclipticToEquatorialConversion);
                planets.add(planet);
                EquatorialCoordinates planetEquatorial = planet.equatorialPos();
                CartesianCoordinates planetCart = projection.apply(equatorialToHorizontalConversion.apply(planetEquatorial));
                planetsCart.add(planetCart);
                observedSky.put(planet, planetCart);
            }
        }

        //Stars
        starsCart = new ArrayList<>();
        if (catalogue != null) {
            for (Star star : catalogue.stars()) {
                EquatorialCoordinates starEquatorial = star.equatorialPos();
                CartesianCoordinates starCart = projection.apply(equatorialToHorizontalConversion.apply(starEquatorial));
                starsCart.add(starCart);
                observedSky.put(star, starCart);
            }
        }
    }

    /**
     * Return the sun
     *
     * @return : the sun
     */
    public Sun sun() {
        return sun;
    }

    /**
     * Return the moon
     *
     * @return : the moon
     */
    public Moon moon() {
        return moon;
    }

    /**
     * return the list of  seven planets
     *
     * @return : the list of  seven planets
     */
    public List<Planet> planets() {
        return List.copyOf(planets);
    }

    /**
     * Return the set of the asterisms
     *
     * @return : the set of the asterisms
     */
    public Set<Asterism> asterisms() {
        return catalogue.asterisms();
    }

    /**
     * return a list of the stars
     *
     * @return : a list of the stars
     */
    public List<Star> stars() {
        return catalogue.stars();
    }

    /**
     * Return the sun position in cartesian coordinates
     *
     * @return : the sun position in cartesian coordinates
     */
    public CartesianCoordinates sunPosition() {
        return sunCart;
    }

    /**
     * return the moon position in cartesian coordinates
     *
     * @return : the moon position in cartesian coordinates
     */
    public CartesianCoordinates moonPosition() {
        return moonCart;
    }

    /**
     * Return a table containing the planet positions (the x in the even indices and the y in the odd indices)
     *
     * @return : a table containing the planet positions
     */
    public double[] planetPosition() {
        double[] planetPositions = new double[14];
        for (int i = 0; i < planetPositions.length; i = i + 2) {
            planetPositions[i] = planetsCart.get(i / 2).x();
            planetPositions[i + 1] = planetsCart.get(i / 2).y();
        }
        return planetPositions;
    }

    /**
     * Return a table containing the star positions (the x in the even indices and the y in the odd indices)
     *
     * @return :a table containing the star positions
     */
    public double[] starsPosition() {
        double[] starPositions = new double[starsCart.size() * 2];
        for (int i = 0; i < starPositions.length; i = i + 2) {
            starPositions[i] = starsCart.get(i / 2).x();
            starPositions[i + 1] = starsCart.get(i / 2).y();
        }
        return starPositions;
    }

    /**
     * Return the object closest to a point of the plan (mousePosition)
     * if this object is closer than the maxDistance, otherwise it returns null
     *
     * @param mousePosition : the point of the plan
     * @param maxDistance   : the maximum distance
     * @return : null if there is not any object in the radius maxDistance around the mousePosition point,
     * and else the object closest to the mousPosition point
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates mousePosition, double maxDistance) {
        CelestialObject closestCO = null;
        double minRadiusFromMouse = maxDistance;

        for (Map.Entry<CelestialObject, CartesianCoordinates> entry : observedSky.entrySet()) {
            CelestialObject key = entry.getKey();
            CartesianCoordinates value = entry.getValue();
            double radiusFromMouse = Math.hypot(mousePosition.x() - value.x(), mousePosition.y() - value.y());
            if (radiusFromMouse <= minRadiusFromMouse) {
                closestCO = key;
                minRadiusFromMouse = radiusFromMouse;
            }
        }
        return Optional.ofNullable(closestCO);
    }

}
