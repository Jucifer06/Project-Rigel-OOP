package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyObservedSkyTest {


    private static final String ASTERISMS_TXT = "/asterisms.txt";
    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";

    ObservedSky sky;
    StereographicProjection stereo;
    GeographicCoordinates geoCoords;
    ZonedDateTime time;
    EquatorialToHorizontalConversion convEquToHor;
    EclipticToEquatorialConversion convEcltoEqu;
    StarCatalogue.Builder builder;
    StarCatalogue catalogue;

    @Test
    public void ConstrustorTest() throws IOException {
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
            }
        }

    }

    @Test
    void constructorTest2() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            builder = new StarCatalogue.Builder().loadFrom(hygStream, HygDatabaseLoader.INSTANCE);
        }

        try (InputStream astStream = getClass().getResourceAsStream(ASTERISMS_TXT)) {
            catalogue = builder.loadFrom(astStream, AsterismLoader.INSTANCE).build();
        }

        time = ZonedDateTime.of(LocalDate.of(2020, Month.APRIL, 4), LocalTime.of(0, 0), ZoneOffset.UTC);
        geoCoords = GeographicCoordinates.ofDeg(30, 45);
        stereo = new StereographicProjection(HorizontalCoordinates.ofDeg(20, 22));
        convEquToHor = new EquatorialToHorizontalConversion(time, geoCoords);
        convEcltoEqu = new EclipticToEquatorialConversion(time);
        sky = new ObservedSky(time, geoCoords, stereo, catalogue);

    }


    @Test
    void closestToObjectTestException() throws IOException {

        constructorTest2();

        assertEquals(Optional.empty(),
                sky.objectClosestTo(stereo.apply(new EquatorialToHorizontalConversion(time, geoCoords)
                        .apply(EquatorialCoordinates.of(0.004696959812148989, -0.8618930353430763))), 0.001));
    }

    @Test
    void closestToObjectTest() {
        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(2, 0.5f);
        EquatorialCoordinates equatorialCoordinates = EquatorialCoordinates.of(1, 1.2f);
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(time,geoCoords);
        CelestialObject celestialObject = new Sun(eclipticCoordinates, equatorialCoordinates, 2f, 0.5f);
        StereographicProjection stereographicProjection = new StereographicProjection(equatorialToHorizontalConversion.apply(equatorialCoordinates));
        Optional<CelestialObject> celestialObject1 = sky.objectClosestTo(stereographicProjection.apply(equatorialToHorizontalConversion.apply(equatorialCoordinates)),30);
        System.out.println(celestialObject.toString());
        //   ra 3,794851105208
        //    dec -1,0821041362
    }


    @Test
    void starsTest() throws IOException {
        constructorTest2();

        int i = 0;
        for (Star star : sky.stars()) {
            assertEquals(stereo.apply(convEquToHor.apply(star.equatorialPos())).x(),
                    sky.starsPosition()[i]);
            i += 2;
        }
        assertEquals(catalogue.stars().size(), sky.stars().size());
        //Si fail: Cloner/Copier le tableau
        double memory = sky.starsPosition()[0];
        sky.starsPosition()[0] = Double.MAX_VALUE;
        assertEquals(memory, sky.starsPosition()[0]);
    }
}



