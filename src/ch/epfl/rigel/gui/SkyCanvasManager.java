package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;


/**
 * a manager of the sky
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class SkyCanvasManager {

    private static final ClosedInterval CLOSED_INTERVAL_5_TO_90 = ClosedInterval.of(5, 90);
    private static final ClosedInterval CLOSED_INTERVAL_30_TO_150 = ClosedInterval.of(30, 150);
    private static final double MAX_UNIT_DISTANCE_CELESTIAL_OBJECT = 10;
    private static final double POP_UP_IMAGE_SIZE = 300;

    public ObservableDoubleValue mouseAzDeg, mouseAltDeg;
    public ObservableObjectValue<Optional<CelestialObject>> objectUnderMouse;
    public ObjectProperty<CartesianCoordinates> mousePosition = new SimpleObjectProperty<>(CartesianCoordinates.of(0, 0));

    private DateTimeBean dateTimeBean;
    private ObserverLocationBean observerLocationBean;
    private ViewingParametersBean viewingParametersBean;
    private ObservableObjectValue<StereographicProjection> projection;
    private ObservableObjectValue<ObservedSky> observedSky;

    private Canvas canvas;
    private SkyCanvasPainter painter;


    /**
     * The constructor of the sky canvas manager
     *
     * @param catalogue             : the catalogue of stars and asterisms
     * @param dateTimeBean          : a bean to view the date and time
     * @param observerLocationBean  : a bean to view the observer location
     * @param viewingParametersBean : a bean to view the viewing parameters
     */
    public SkyCanvasManager(StarCatalogue catalogue, DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {

        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;

        canvas = new Canvas();
        painter = new SkyCanvasPainter(canvas);

        //projection's link
        projection = Bindings.createObjectBinding(() ->
                        new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty());

        // transformation's link
        ObservableObjectValue<Transform> planeToCanvas = Bindings.createObjectBinding(() -> {
            double dilatationFactor = canvas.getWidth() / projection.get().applyToAngle(Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg()));
            return Transform.affine(dilatationFactor, 0, 0, -dilatationFactor, canvas.getWidth() / 2d, canvas.getHeight() / 2d);
        }, canvas.widthProperty(), canvas.heightProperty(), projection, viewingParametersBean.fieldOfViewDegProperty());

        //observed sky's link
        observedSky = Bindings.createObjectBinding(() ->
                        new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.get(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneProperty(), observerLocationBean.coordinatesProperty(), projection);

        //change in the sky listeners
        observedSky.addListener(o -> drawSky(painter, observedSky.get(), planeToCanvas.get(), projection.get()));
        planeToCanvas.addListener(o -> drawSky(painter, observedSky.get(), planeToCanvas.get(), projection.get()));
        Main.asterismsAreOn.addListener(o -> drawSky(painter,observedSky.get(),planeToCanvas.get(),projection.get()));

        //mouse click event
        canvas.setOnMousePressed((e) -> {
            if (e.isPrimaryButtonDown()) {
                canvas.requestFocus();
            }
        });

        //position mouse event
        canvas.setOnMouseMoved((e) -> mousePosition.setValue(CartesianCoordinates.of(e.getX(), e.getY())));

        //Images
        canvas.setOnMouseClicked(e -> {
            if (objectUnderMouse.get() != null && objectUnderMouse.get().isPresent()) {
                CelestialObject celestialObject = objectUnderMouse.get().get();
                if (celestialObject.equals(observedSky.get().sun())) {
                    popUpWindowsImage("Sun Photograph", "/sun.jpg");
                } else if (celestialObject.equals(observedSky.get().moon())) {
                    popUpWindowsImage("Moon Photograph", "/moon.jpg");
                } else if (celestialObject.equals(observedSky.get().planets().get(0))) {
                    popUpWindowsImage("Mercure Photograph", "/mercury.jpg");
                } else if (celestialObject.equals(observedSky.get().planets().get(1))) {
                    popUpWindowsImage("Vénus Photograph", "/venus.jpg");
                } else if (celestialObject.equals(observedSky.get().planets().get(2))) {
                    popUpWindowsImage("Mars Photograph", "/mars.jpg");
                } else if (celestialObject.equals(observedSky.get().planets().get(3))) {
                    popUpWindowsImage("Jupiter Photograph", "/Jupiter.jpg");
                } else if (celestialObject.equals(observedSky.get().planets().get(4))) {
                    popUpWindowsImage("Saturne Photograph", "/saturn.jpg");
                } else if (celestialObject.equals(observedSky.get().planets().get(5))) {
                    popUpWindowsImage("Uranus Photograph", "/uranus.png");
                } else if (celestialObject.equals(observedSky.get().planets().get(6))) {
                    popUpWindowsImage("Neptune Photograph", "/neptune.png");
                }
            }
        });


        // mouse coordinates horizontal
        ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
            try {
                CartesianCoordinates cartesianCoordinates = mousePosition.get();
                Point2D point2D = planeToCanvas.get().inverseTransform(
                        cartesianCoordinates.x(), cartesianCoordinates.y());
                CartesianCoordinates transformedCartesianCoordinates = CartesianCoordinates.of(
                        point2D.getX(), point2D.getY());
                return projection.get().inverseApply(transformedCartesianCoordinates);
            } catch (NonInvertibleTransformException e) {
                return null;
            }
        }, projection, planeToCanvas, mousePosition);

        //mouse altitude and azimuth
        mouseAltDeg = Bindings.createDoubleBinding(() -> {
                    if (mouseHorizontalPosition.getValue() != null) {
                        return mouseHorizontalPosition.get().altDeg();
                    } else {
                        return Double.NaN;
                    }
                },
                mouseHorizontalPosition);
        mouseAzDeg = Bindings.createDoubleBinding(() -> {
                    if (mouseHorizontalPosition.getValue() != null) {
                        return mouseHorizontalPosition.get().azDeg();
                    } else {
                        return Double.NaN;
                    }
                },
                mouseHorizontalPosition);

        // Object under mouse
        objectUnderMouse = Bindings.createObjectBinding(() -> {
            try {
                Point2D mousePositionPoint2D = planeToCanvas.get().inverseTransform(
                        mousePosition.get().x(), mousePosition.get().y());
                CartesianCoordinates inverseMousePosition = CartesianCoordinates.of(
                        mousePositionPoint2D.getX(), mousePositionPoint2D.getY());

                Point2D point2D = planeToCanvas.get().inverseDeltaTransform(0, MAX_UNIT_DISTANCE_CELESTIAL_OBJECT);
                double maxDistance = Math.hypot(point2D.getX(), point2D.getY());
                return observedSky.get().objectClosestTo(inverseMousePosition, maxDistance);
            } catch (NonInvertibleTransformException e) {
                return Optional.empty();
            }
        }, planeToCanvas, mousePosition, observedSky);

        //trackPad
        canvas.setOnScroll((e) -> {
            double deltaX = e.getDeltaX();
            double deltaY = e.getDeltaY();
            double biggestValue = (Math.abs(deltaX) >= Math.abs(deltaY)) ? (deltaX) : (deltaY);
            double newValue = CLOSED_INTERVAL_30_TO_150.clip(viewingParametersBean.getFieldOfViewDeg() - biggestValue);
            viewingParametersBean.setFieldOfViewDeg(newValue);
        });

        //keyboard changes
        canvas.setOnKeyPressed((e) -> {
            KeyCode keyCode = e.getCode();
            if (keyCode.equals(KeyCode.LEFT)) {
                setCenterAz(viewingParametersBean, viewingParametersBean.getCenter().azDeg(), -10);
                e.consume();
            } else if (keyCode.equals(KeyCode.RIGHT)) {
                setCenterAz(viewingParametersBean, viewingParametersBean.getCenter().azDeg(), 10);
                e.consume();
            } else if (keyCode.equals(KeyCode.UP)) {
                setCenterAlt(viewingParametersBean, viewingParametersBean.getCenter().altDeg(), 5);
                e.consume();
            } else if (keyCode.equals(KeyCode.DOWN)) {
                setCenterAlt(viewingParametersBean, viewingParametersBean.getCenter().altDeg(), -5);
                e.consume();
            }
        });
    }

    /**
     * Return the canvas
     *
     * @return : the canvas
     */
    public Canvas canvas() {
        return canvas;
    }

    /**
     * Return the date time bean
     *
     * @return : the date time bean
     */
    public DateTimeBean getDateTimeBean() {
        return dateTimeBean;
    }

    /**
     * Return the observer location bean
     *
     * @return : the observer location bean
     */
    public ObserverLocationBean getObserverLocationBean() {
        return observerLocationBean;
    }

    /**
     * Return the viewing parameter bean
     *
     * @return : the viewing parameter bean
     */
    public ViewingParametersBean getViewingParametersBean() {
        return viewingParametersBean;
    }

    /**
     * Return the mouse azimuth (in degrees)
     *
     * @return :the mouse azimuth (in degrees)
     */
    public double getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    /**
     * Return the mouse altitude (in degrees)
     *
     * @return : the mouse altitude (in degrees)
     */
    public double getMouseAltDeg() {
        return mouseAltDeg.get();
    }

    /**
     * Return the object under mouse (can be null)
     *
     * @return : the object under mouse (can be null)
     */
    public Optional<CelestialObject> getObjectUnderMouse() {
        return objectUnderMouse.get();
    }

    /**
     * Return the cartesian coordinates of the mouse position
     *
     * @return : the cartesian coordinates of the mouse position
     */
    public CartesianCoordinates getMousePosition() {
        return mousePosition.get();
    }

    /**
     * Return the stereographic projection
     *
     * @return : the stereographic projection
     */
    public StereographicProjection getProjection() {
        return projection.get();
    }

    /**
     * Return the observed sky
     *
     * @return : the observed sky
     */
    public ObservedSky getObservedSky() {
        return observedSky.get();
    }

    /**
     * Return the mouse azimuth property
     *
     * @return : the mouse azimuth property
     */
    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    /**
     * Return the mouse altitude property
     *
     * @return : the mouse altitude property
     */
    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }

    /**
     * Return the object under mouse property
     *
     * @return : the object under mouse property
     */
    public ObservableObjectValue<Optional<CelestialObject>> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    /**
     * Return the mouse position property
     *
     * @return : the mouse position property
     */
    public ObjectProperty<CartesianCoordinates> mousePositionProperty() {
        return mousePosition;
    }

    /**
     * Return the stereographic projection property
     *
     * @return : the stereographic projection property
     */
    public ObservableObjectValue<StereographicProjection> projectionProperty() {
        return projection;
    }

    /**
     * Return the observed sky property
     *
     * @return : the observed sky property
     */
    public ObservableObjectValue<ObservedSky> observedSkyProperty() {
        return observedSky;
    }

    /**
     * Setter of the cartesian coordinates of the mouse position
     *
     * @param mousePosition : the new mouse position
     */
    public void setMousePosition(CartesianCoordinates mousePosition) {
        this.mousePosition.set(mousePosition);
    }

    /**
     * Setter of the date time bean
     *
     * @param dateTimeBean : the new date time bean
     */
    public void setDateTimeBean(DateTimeBean dateTimeBean) {
        this.dateTimeBean = dateTimeBean;
    }

    /**
     * Setter of the observer location bean
     *
     * @param observerLocationBean : the new observer location bean
     */
    public void setObserverLocationBean(ObserverLocationBean observerLocationBean) {
        this.observerLocationBean = observerLocationBean;
    }

    /**
     * Setter of the viewing parameter bean
     *
     * @param viewingParametersBean : the new viewing parameter bean
     */
    public void setViewingParametersBean(ViewingParametersBean viewingParametersBean) {
        this.viewingParametersBean = viewingParametersBean;
    }

    /**
     * shift the azimuth center
     *
     * @param viewingParametersBean : a viewing parameter bean
     * @param oldAz                 : the old azimuth
     * @param delta                 : the shifting
     */
    private void setCenterAz(ViewingParametersBean viewingParametersBean, double oldAz, double delta) {
        double newAz = Angle.toDeg(Angle.normalizePositive(Angle.ofDeg(oldAz + delta)));
        viewingParametersBean.setCenter(
                HorizontalCoordinates.ofDeg(newAz, viewingParametersBean.getCenter().altDeg()));
    }

    /**
     * shift the altitude center
     *
     * @param viewingParametersBean : a viewing parameter bean
     * @param oldAlt                : the old altitude
     * @param delta                 : the shifting
     */
    private void setCenterAlt(ViewingParametersBean viewingParametersBean, double oldAlt, double delta) {
        double newAlt = CLOSED_INTERVAL_5_TO_90.clip(oldAlt + delta);
        viewingParametersBean.setCenter(
                HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg(), newAlt));
    }

    /**
     * Draw the sky in respect to the order
     *
     * @param painter    : the sky canvas painter
     * @param sky        : the sky
     * @param t          : the transformation
     * @param projection : the stereographic projection
     */
    private void drawSky(SkyCanvasPainter painter, ObservedSky sky, Transform t, StereographicProjection projection) {
        if (projection.inverseApply(sky.sunPosition()).alt() > 0) {
            painter.clear(Color.MIDNIGHTBLUE);
            painter.drawStars(sky, projection, t, Color.YELLOWGREEN);
        } else {
            painter.clear(Color.BLACK);
            painter.drawStars(sky, projection, t, Color.BLUE);
        }
        painter.drawPlanet(sky, projection, t);
        painter.drawSun(sky, projection, t);
        painter.drawMoon(sky, projection, t);
        painter.drawHorizon(projection, t);
    }

    /**
     * Brings up a window with a celestial object picture
     *
     * @param celestialObjectTitle : the celestial object name
     * @param resourceName         : the resource (image) name
     */
    private void popUpWindowsImage(String celestialObjectTitle, String resourceName) {
        final Stage popUpStage = new Stage();
        popUpStage.setTitle(celestialObjectTitle);
        popUpStage.initModality(Modality.WINDOW_MODAL);
        VBox popUpVBox = new VBox();
        ImageView imageView = new ImageView(resourceName);
        imageView.setFitHeight(POP_UP_IMAGE_SIZE);
        imageView.setFitWidth(POP_UP_IMAGE_SIZE);
        popUpVBox.getChildren().add(imageView);
        Scene popUpScene = new Scene(popUpVBox, POP_UP_IMAGE_SIZE, POP_UP_IMAGE_SIZE);
        popUpStage.setScene(popUpScene);
        popUpStage.setResizable(false);
        popUpStage.show();
    }
}