package ch.epfl.rigel.astronomy;


import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyPlanetModelTest {

    @Test
    public void atPlanetJupiterTest() {

        ZonedDateTime zone = ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC);
        EclipticToEquatorialConversion conv = new EclipticToEquatorialConversion(zone);
        Planet jupiter = PlanetModel.JUPITER.at(Epoch.J2010.daysUntil(zone), conv);
        Assertions.assertEquals(11.1871549347096, jupiter.equatorialPos().raHr(),1e-10);
        Assertions.assertEquals(6.3566355066857, jupiter.equatorialPos().decDeg(),1e-10);


        //*3600 car la valeur dans le livre est en arcsec, et chez nous en degres
        assertEquals(Angle.ofDeg(35.11141185362771), jupiter.angularSize() * 3600);
        assertEquals(-1.9885659217834473, jupiter.magnitude());

    }

    @Test

    public void atPlanetMercuryTest() {

        ZonedDateTime zone = ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC);
        Planet mercury = PlanetModel.MERCURY.at(Epoch.J2010.daysUntil(zone),
                new EclipticToEquatorialConversion( zone));

        assertEquals(16.820074565897194, mercury.equatorialPos().raHr(), 1e-10);
        assertEquals(-24.500872462861224, mercury.equatorialPos().decDeg(), 1e-10);
    }


    //tests Lea
    @Test
    void at(){
        assertEquals(11.187154934709678,PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .equatorialPos().raHr(), 1e-10);
        assertEquals(6.356635506685756,PlanetModel.JUPITER.at(-2231.0,
                new EclipticToEquatorialConversion(
                        ZonedDateTime.of(LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .equatorialPos().decDeg(), 1e-10);
        assertEquals(35.11141185362771, Angle.toDeg(PlanetModel.JUPITER.at(-2231.0,new EclipticToEquatorialConversion(ZonedDateTime.of(
                LocalDate.of(2003, Month.NOVEMBER, 22), LocalTime.of(0, 0, 0, 0),
                ZoneOffset.UTC))).angularSize())*3600 );

    }
}
