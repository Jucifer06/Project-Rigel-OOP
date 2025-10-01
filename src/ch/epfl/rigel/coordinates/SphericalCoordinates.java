package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Spherical coordinates
 *
 * @author Paola Matta (296390)
 * @author Juliette Parchet (295888)
 */
abstract class SphericalCoordinates {

    private final double longitude, latitude;

    /**
     * Constructor of the spherical coordinates with the longitude and latitude given
     *
     * @param longitude : the longitude
     * @param latitude  : the latitude
     */
    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Return the longitude in radian
     *
     * @return the longitude in radian
     */
    protected double lon() {
        return longitude;
    }

    /**
     * Return the latitude in radian
     *
     * @return the latitude in radian
     */
    protected double lat() {
        return latitude;
    }

    /**
     * Return the longitude in degree
     *
     * @return the longitude in degree
     */
    protected double lonDeg() {
        return Angle.toDeg(longitude);
    }

    /**
     * Return the latitude in degree
     *
     * @return the latitude in degree
     */
    protected double latDeg() {
        return Angle.toDeg(latitude);
    }

    @Override
    final public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    final public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
