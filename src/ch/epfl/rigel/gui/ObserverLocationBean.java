package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;

/**
 * a bean to view the location of the observer
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class ObserverLocationBean {
    private DoubleProperty lonDeg, latDeg;
    private ObservableObjectValue<GeographicCoordinates> coordinates;

    /**
     * the constructor of the observer location bean
     */
    public ObserverLocationBean() {
        lonDeg = new SimpleDoubleProperty();
        latDeg = new SimpleDoubleProperty();
        coordinates = Bindings.createObjectBinding(() ->
                GeographicCoordinates.ofDeg(lonDeg.get(), latDeg.get()), lonDeg, latDeg);
    }

    /**
     * return the longitude of the observer position (in degrees)
     *
     * @return : the longitude of the observer position (in degrees)
     */
    public double getLonDeg() {
        return lonDeg.get();
    }

    /**
     * Return the longitude property
     *
     * @retur : the longitude property
     */
    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     * Setter of the longitude of the observer value (in degrees)
     *
     * @param lonDeg : the new longitude value (in degrees)
     */
    public void setLonDeg(double lonDeg) {
        this.lonDeg.set(lonDeg);
    }

    /**
     * Return the latitude of the observer (in degrees)
     *
     * @return : the latitude of the observer (in degrees)
     */
    public double getLatDeg() {
        return latDeg.get();
    }

    /**
     * Return the latitude property
     *
     * @return : the latitude property
     */
    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     * Setter of the latitude of the observer value (in degrees)
     *
     * @param latDeg : the new latitude value (in degrees)
     */
    public void setLatDeg(double latDeg) {
        this.latDeg.set(latDeg);
    }

    /**
     * Return the geographic coordinates of the observer position
     *
     * @return : the geographic coordinates of the observer position
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.get();
    }

    /**
     * Return the property of the geographic coordinates
     *
     * @return : the property of the geographic coordinates
     */
    public ObservableObjectValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * Setter of the geographic coordinates of the observer location value
     *
     * @param coordinates : the new geographic coordinates
     */
    public void setCoordinates(GeographicCoordinates coordinates) {
        setLatDeg(coordinates.latDeg());
        setLonDeg(coordinates.lonDeg());
    }
}
