package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyMoonModelTest {
    ZonedDateTime when = ZonedDateTime.of(
            LocalDate.of(2003, Month.SEPTEMBER, 1),
            LocalTime.MIDNIGHT,
            ZoneOffset.UTC);
    double daysSince = Epoch.J2010.daysUntil(when);
    EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(when);
    Moon moon = MoonModel.MOON.at(daysSince, eclipticToEquatorialConversion);
    EquatorialCoordinates expectedEqCoor = eclipticToEquatorialConversion.apply(
            EclipticCoordinates.of(Angle.ofDeg(214.862515), Angle.ofDeg(1.716257)));

    @Test
    public void testAtRa() {
        //Précision 9 décimales
        assertEquals(expectedEqCoor.ra(), moon.equatorialPos().ra(), 1e-9);
    }

    @Test
    public void testAtDec() {
        //Précision 7 décimales
        assertEquals(expectedEqCoor.dec(), moon.equatorialPos().dec(), 1e-7);
    }


    //Tests internet
    Moon m1 = MoonModel.MOON.at(-2313, new EclipticToEquatorialConversion(ZonedDateTime.of(
            LocalDate.of(2003, Month.SEPTEMBER, 1), LocalTime.of(0, 0), ZoneOffset.UTC)));

    @Test
    //précision 4 décimales
    public void MoonModelAtInternetRA() {
        assertEquals(14.211456460, m1.equatorialPos().raHr(), 1e-7);
    }

    @Test
    //précision 1 décimale
    public void MoonModelAtInternetDEC() {
        assertEquals(-0.20114171387374752, m1.equatorialPos().dec() , 1e-9);
    }

}
