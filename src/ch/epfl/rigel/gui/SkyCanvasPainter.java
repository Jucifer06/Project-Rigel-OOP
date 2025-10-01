package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * a painter of the sky on a canvas
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class SkyCanvasPainter {

    private static final ClosedInterval MAGNITUDE_CLIP_INTERVAL = ClosedInterval.of(-2, 5);
    private static final double THETA = Angle.ofDeg(0.5d);
    private static final Color FADED_YELLOW_COLOR = Color.YELLOW.deriveColor(0, 1, 1, 0.25);
    private static final Color ULTRA_FADED_YELLOW_COLOR = Color.YELLOW.deriveColor(0, 1, 1, 0.10);
    private static final Color FADED_GRAY_COLOR = Color.LIGHTGRAY.deriveColor(0, 1, 1, 0.25);
    private static final Color FADED_WHITE_COLOR = Color.WHITE.deriveColor(0, 1, 1, 0.40);
    private static final Color ULTRA_FADED_WHITE_COLOR = Color.WHITE.deriveColor(0, 1, 1, 0.20);

    private Canvas canvas;
    private GraphicsContext ctx;

    /**
     * Constructor of the sky painter and initialisation of the canvas
     *
     * @param canvas : the canvas
     */
    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        ctx = canvas.getGraphicsContext2D();
    }

    /**
     * Clear the sky to the given color
     *
     * @param color : the base color of the sky
     */
    public void clear(Color color) {
        ctx.setFill(color);
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        ctx.clearRect(0, 0, canvasWidth, canvasHeight);
        ctx.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    /**
     * Draws the asterisms and stars' representation of a given observed sky
     *
     * @param observedSky    : the observed sky
     * @param projection     : the stereographic projection
     * @param transformation : the affine transformation from stereographic projection to canvas system
     * @param colorAsterism  : the color of the asterisms
     */
    public void drawStars(ObservedSky observedSky, StereographicProjection projection, Transform transformation, Color colorAsterism) {
        List<Star> stars = observedSky.stars();
        double[] starPositions = observedSky.starsPosition();
        Set<Asterism> asterisms = observedSky.asterisms();
        StarCatalogue starCatalogue = new StarCatalogue(stars, new ArrayList<>(asterisms));

        if (Main.asterismsAreOn.get()) {
            //draw the asterisms
            ctx.setStroke(colorAsterism);
            ctx.setLineWidth(1);
            for (Asterism asterism : asterisms) {
                Point2D previousStar = null;
                ctx.beginPath();
                List<Integer> indices = starCatalogue.asterismIndices(asterism);
                for (Integer index : indices) {
                    Point2D currentStar = transformation.transform(starPositions[index * 2], starPositions[index * 2 + 1]);
                    Bounds bounds = canvas.getBoundsInLocal();
                    if (bounds.contains(previousStar) || bounds.contains(currentStar)) {
                        ctx.lineTo(currentStar.getX(), currentStar.getY());
                    } else {
                        ctx.stroke();
                        ctx.closePath();
                        ctx.beginPath();
                    }
                    previousStar = currentStar;
                }
                ctx.stroke();
                ctx.closePath();
            }
        }

        //draw the stars
        int i = 0;
        for (Star star : stars) {
            double diameter = diameterOfPlanetsAndStars(star.magnitude(), projection);
            Color color = BlackBodyColor.colorForTemperature(star.colorTemperature());
            Color fadedColor = color.deriveColor(0, 1, 1, 0.25);
            drawCircle(fadedColor, starPositions[i], starPositions[i + 1], diameter * 1.5, transformation, null);
            drawCircle(color, starPositions[i], starPositions[i + 1], diameter, transformation, null);
            i += 2;
        }
    }

    /**
     * Draws the planets' representation of a given observed sky
     *
     * @param observedSky    : the observed sky
     * @param projection     : the stereographic projection
     * @param transformation : the affine transformation from stereographic projection to canvas system
     */
    public void drawPlanet(ObservedSky observedSky, StereographicProjection projection, Transform transformation) {
        List<Planet> planets = observedSky.planets();
        double[] planetPositions = observedSky.planetPosition();
        int i = 0;
        for (Planet planet : planets) {
            double diameter = diameterOfPlanetsAndStars(planet.magnitude(), projection);
            drawCircle(FADED_GRAY_COLOR, planetPositions[i], planetPositions[i + 1], diameter * 1.5, transformation, planet.info());
            drawCircle(Color.LIGHTGRAY, planetPositions[i], planetPositions[i + 1], diameter, transformation, null);
            i += 2;
        }
    }

    /**
     * Draws the sun's representation of a given observed sky
     *
     * @param observedSky    : the observed sky
     * @param projection     : the stereographic projection
     * @param transformation : the affine transformation from stereographic projection to canvas system
     */
    public void drawSun(ObservedSky observedSky, StereographicProjection projection, Transform transformation) {
        Sun sun = observedSky.sun();
        CartesianCoordinates sunPosition = observedSky.sunPosition();
        double x = sunPosition.x();
        double y = sunPosition.y();
        double diameter = projection.applyToAngle(sun.angularSize());
        drawCircle(ULTRA_FADED_YELLOW_COLOR, x, y, diameter * 2.8, transformation, sun.info());
        drawCircle(FADED_YELLOW_COLOR, x, y, diameter * 2.2, transformation, null);
        drawCircle(Color.YELLOW, x, y, diameter * 2, transformation, null);
        drawCircle(Color.WHITE, x, y, diameter, transformation, null);
    }

    /**
     * Draws the moon's representation of a given observed sky
     *
     * @param observedSky    : the observed sky
     * @param projection     : the stereographic projection
     * @param transformation : the affine transformation from stereographic projection to canvas system
     */
    public void drawMoon(ObservedSky observedSky, StereographicProjection projection, Transform transformation) {
        Moon moon = observedSky.moon();
        CartesianCoordinates moonPosition = observedSky.moonPosition();
        double diameter = projection.applyToAngle(moon.angularSize());
        drawCircle(ULTRA_FADED_WHITE_COLOR, moonPosition.x(), moonPosition.y(), diameter * 3, transformation, moon.info());
        drawCircle(FADED_WHITE_COLOR, moonPosition.x(), moonPosition.y(), diameter * 2, transformation, null);
        drawCircle(Color.WHITE, moonPosition.x(), moonPosition.y(), diameter, transformation, null);
    }

    /**
     * Draws the cardinals and horizon's representation of a given observed sky
     *
     * @param projection     : the stereographic projection
     * @param transformation : the affine transformation from stereographic projection to canvas system
     */
    public void drawHorizon(StereographicProjection projection, Transform transformation) {
        //horizon diameter
        double diameter = projection.circleRadiusForParallel(HorizontalCoordinates.of(0, 0)) * 2;
        Point2D diameterPoint2D = transformation.deltaTransform(0, diameter);
        double newDiameter = Math.hypot(diameterPoint2D.getX(), diameterPoint2D.getY());

        // points (x,y) center
        CartesianCoordinates cc = projection.circleCenterForParallel(HorizontalCoordinates.ofDeg(0, 0));
        Point2D point2D = transformation.transform(cc.x(), cc.y());
        double x = point2D.getX();
        double y = point2D.getY();
        double xTransformed = x - newDiameter / 2d;
        double yTransformed = y - newDiameter / 2d;

        //drawing horizon
        ctx.setLineWidth(2);
        ctx.setStroke(Color.RED);
        ctx.strokeOval(xTransformed, yTransformed, newDiameter, newDiameter);

        //Cardinals
        ctx.setFill(Color.RED);
        for (int i = 0; i < 360; i += 45) {
            drawCardinal(i, projection, transformation);
        }
    }

    /**
     * Draws a transfomed circle of the given color, diameter and coordinates
     *
     * @param color          : color of the circle
     * @param x              : abscissa of the circle's position (corner left)
     * @param y              : ordinate of the circle's position (corner left)
     * @param diameter       : diameter of the circle
     * @param transformation : the affine transformation from stereographic projection to canvas system
     * @param name           : the name (can be null) that must be bind with the circle
     */
    private void drawCircle(Color color, double x, double y, double diameter, Transform transformation, String name) {
        ctx.setFill(color);
        Point2D cordPoint2D = transformation.transform(x, y);
        Point2D diameterPoint2D = transformation.deltaTransform(0, diameter);
        double newDiameter = Math.abs(diameterPoint2D.getX() - diameterPoint2D.getY());
        ctx.fillOval(cordPoint2D.getX() - newDiameter / 2d,
                cordPoint2D.getY() - newDiameter / 2d,
                newDiameter,
                newDiameter);
        if (name != null) {
            ctx.setFill(Color.YELLOW);
            ctx.fillText(name, cordPoint2D.getX() + newDiameter / 2, cordPoint2D.getY() + newDiameter / 2);
        }
    }

    /**
     * Computes the scaled diameter of the planets or stars
     *
     * @param magnitude  : magnitude of the planet or star
     * @param projection : stereographic projection
     * @return : the scaled diameter of the planets or stars
     */
    private double diameterOfPlanetsAndStars(double magnitude, StereographicProjection projection) {
        double clippedMagnitude = MAGNITUDE_CLIP_INTERVAL.clip(magnitude);
        double sizeFactor = (99 - 17 * clippedMagnitude) / 140d;
        return sizeFactor * projection.applyToAngle(THETA);
    }

    /**
     * Draw a transformed cardinal given some coordinates (in degrees)
     *
     * @param coordinatesDeg : the coordinates of the cardinal (in degrees)
     * @param projection     : the stereographic projection
     * @param transformation : the affine transformation from stereographic projection to canvas system
     */
    private void drawCardinal(double coordinatesDeg, StereographicProjection projection, Transform transformation) {
        double textSize = ctx.getTextBaseline().ordinal();
        HorizontalCoordinates horizontal = HorizontalCoordinates.ofDeg(coordinatesDeg, -0.5 - textSize);
        String name = horizontal.azOctantName("N", "E", "S", "O");
        CartesianCoordinates cartesian = projection.apply(horizontal);
        Point2D cordPoint2D = transformation.transform(cartesian.x(), cartesian.y());
        ctx.fillText(name, cordPoint2D.getX(), cordPoint2D.getY());
    }
}

