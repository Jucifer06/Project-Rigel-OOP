package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MySkyCanvasPainterTest {
    private static final String ASTERISMS_TXT = "/asterisms.txt";
    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";


    @Test
    public void firstTest() throws IOException {
        ObservedSky observedSky = createObservedSky();
        Canvas canvas = new Canvas();
        SkyCanvasPainter painter = new SkyCanvasPainter(canvas);
        Transform transform = Transform.affine(1300, 0, 0, -420, 400, 300);
    }

    private ObservedSky createObservedSky() {
        LocalDate t = LocalDate.of(2009, Month.JULY, 6);
        ZonedDateTime when = ZonedDateTime.of(t, LocalTime.of(0, 0), ZoneOffset.UTC);
        GeographicCoordinates geographicCoordinates = GeographicCoordinates.ofDeg(50, 13);
        HorizontalCoordinates horizontalCoordinates = HorizontalCoordinates.ofDeg(20, 20);
        StereographicProjection projection = new StereographicProjection(horizontalCoordinates);
        try (InputStream asterismStream = getClass().getResourceAsStream(ASTERISMS_TXT)) {
            try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
                StarCatalogue.Builder builder = new StarCatalogue.Builder();
                builder.loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
                StarCatalogue catalogue = builder.loadFrom(asterismStream, AsterismLoader.INSTANCE).build();
                ObservedSky observedSky = new ObservedSky(when, geographicCoordinates, projection, catalogue);
                return observedSky;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

