package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * a bean to view parameters
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class ViewingParametersBean {
    private DoubleProperty fieldOfViewDeg;
    private ObjectProperty<HorizontalCoordinates> center;

    /**
     * the constructor of the viewing parameter bean
     */
    public ViewingParametersBean() {
        fieldOfViewDeg = new SimpleDoubleProperty();
        center = new SimpleObjectProperty<>();
    }

    /**
     * Return the filed of view (in degrees)
     *
     * @return : the filed of view (in degrees)
     */
    public double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    /**
     * Return the horizontal coordinates of the center of the projection
     *
     * @return : the horizontal coordinates of the center of the projection
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * Return the field of view property
     *
     * @return : the field of view property
     */
    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * Return the center of the projection property
     *
     * @return : the center of the projection property
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * Setter of the field of view value (in degrees)
     *
     * @param fieldOfViewDeg : the new field of view value (in degrees)
     */
    public void setFieldOfViewDeg(double fieldOfViewDeg) {
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    /**
     * Set the value of the center of the projection
     *
     * @param center : the new value of the center of the projection
     */
    public void setCenter(HorizontalCoordinates center) {
        this.center.set(center);
    }
}
