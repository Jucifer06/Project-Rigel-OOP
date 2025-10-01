package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class EquatorialToHorizontalConversionTest {
    /*
        @Test
        public void newTestAz() {
            LocalDate t = LocalDate.of(2001, Month.APRIL, 16);
            ZonedDateTime when = ZonedDateTime.of(t, LocalTime.of(14, 26, 33), ZoneOffset.UTC);
            GeographicCoordinates where = GeographicCoordinates.ofDeg(20, 52);
            EquatorialToHorizontalConversion converter = new EquatorialToHorizontalConversion(when, where);
            EquatorialCoordinates eqCoord = EquatorialCoordinates.of(0.25, 0.8);
            double actualAz = converter.apply(eqCoord).az();
            //from internet
            //assertEquals(Angle.ofDeg(4.61), actualAz);
            //my calculations
            assertEquals(Angle.normalizePositive(-2.788405659), actualAz);
        }

        @Test
        public void newTestAlt() {
            LocalDate t = LocalDate.of(2001, Month.APRIL, 16);
            ZonedDateTime when = ZonedDateTime.of(t, LocalTime.of(14, 26, 33), ZoneOffset.UTC);
            GeographicCoordinates where = GeographicCoordinates.ofDeg(20, 52);
            EquatorialToHorizontalConversion converter = new EquatorialToHorizontalConversion(when, where);
            EquatorialCoordinates eqCoord = EquatorialCoordinates.of(0.25, 0.8);
            double actualh = converter.apply(eqCoord).alt();
            assertEquals(Angle.ofHr(5.42892), SiderealTime.local(when, where));
            //from internet
            //assertEquals(Angle.ofDeg(7.99), actualh);
            //my calculations
            assertEquals(0.8184919671, actualh);
        }
    */
    @Test
    public void testsFormInternet2Alt() {
        LocalDate t = LocalDate.of(2001, Month.APRIL, 16);
        ZonedDateTime when = ZonedDateTime.of(t, LocalTime.of(14, 26, 33), ZoneOffset.UTC);
        GeographicCoordinates where = GeographicCoordinates.ofDeg(20, 52);
        EquatorialToHorizontalConversion converter = new EquatorialToHorizontalConversion(when, where);
        EquatorialCoordinates eqCoord = EquatorialCoordinates.of(Angle.ofHr(14.5), Angle.ofDMS(45, 50, 11));
        double actualh = converter.apply(eqCoord).alt();
        assertEquals(Angle.ofHr(5.42892), SiderealTime.local(when, where), 0.001);
        //from internet
        assertEquals(Angle.ofDeg(14.85), actualh, 0.001);
    }

    @Test
    public void testsFormInternet2Az() {
        LocalDate t = LocalDate.of(2001, Month.APRIL, 16);
        ZonedDateTime when = ZonedDateTime.of(t, LocalTime.of(14, 26, 33), ZoneOffset.UTC);
        GeographicCoordinates where = GeographicCoordinates.ofDeg(20, 52);
        EquatorialToHorizontalConversion converter = new EquatorialToHorizontalConversion(when, where);
        EquatorialCoordinates eqCoord = EquatorialCoordinates.of(Angle.ofHr(14.5), Angle.ofDMS(45, 50, 11));
        double actualAz = converter.apply(eqCoord).az();
        assertEquals(Angle.ofHr(5.42892), SiderealTime.local(when, where), 0.001);
        //from internet
        assertEquals(Angle.ofDeg(30), actualAz, 0.001);
    }


    //Test correction

    private static final GeographicCoordinates EPFL =
            GeographicCoordinates.ofDeg(6.57, 46.52);

    private static final ZonedDateTime ZDT_SEMESTER_START = ZonedDateTime.of(
            LocalDate.of(2020, Month.FEBRUARY, 17),
            LocalTime.of(13, 15),
            ZoneOffset.ofHours(1));

    @Test
    void e2hApplyWorksOnBookExample() {
        // PACS4, §25, p. 47
        var where = GeographicCoordinates.ofDeg(0, 52);
        var when = ZonedDateTime.of(
                LocalDate.of(2020, Month.MARCH, 6),
                LocalTime.of(17, 0),
                ZoneOffset.UTC);
        var equToHor = new EquatorialToHorizontalConversion(when, where);
        var starEquPos = EquatorialCoordinates.of(
                5.7936855654392385,
                Angle.ofDMS(23, 13, 10));
        var starHorPos = equToHor.apply(starEquPos);
        assertEquals(
                Angle.ofDMS(283, 16, 15.70),
                starHorPos.az(),
                Angle.ofDMS(0, 0, 0.01 / 2d));
        assertEquals(
                Angle.ofDMS(19, 20, 3.64),
                starHorPos.alt(),
                Angle.ofDMS(0, 0, 0.01 / 2d));
    }


    @Test
    void e2hApplyWorksOnKnownValues() {
        var conversion = new EquatorialToHorizontalConversion(ZDT_SEMESTER_START, EPFL);

        var ecl1 = conversion.apply(EquatorialCoordinates.of(4.9541, -1.4153));
        var ecl2 = conversion.apply(EquatorialCoordinates.of(3.1282, +1.0420));
        var ecl3 = conversion.apply(EquatorialCoordinates.of(5.8611, -1.1461));
        var ecl4 = conversion.apply(EquatorialCoordinates.of(4.6253, +0.7497));
        var ecl5 = conversion.apply(EquatorialCoordinates.of(3.9206, -0.6974));

        assertEquals(3.3066186595315328, ecl1.az(), 1e-9);
        assertEquals(-0.712006491500507, ecl1.alt(), 1e-9);
        assertEquals(6.0837767698593845, ecl2.az(), 1e-9);
        assertEquals(0.3094716108610624, ecl2.alt(), 1e-9);
        assertEquals(3.1528850649053615, ecl3.az(), 1e-9);
        assertEquals(-0.3873294393853973, ecl3.alt(), 1e-9);
        assertEquals(5.127316217769322, ecl4.az(), 1e-9);
        assertEquals(0.7048208908932408, ecl4.alt(), 1e-9);
        assertEquals(4.400817599512725, ecl5.az(), 1e-9);
        assertEquals(-0.7328787267995615, ecl5.alt(), 1e-9);
    }

    @Test
    void e2hEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var c = new EquatorialToHorizontalConversion(ZDT_SEMESTER_START, EPFL);
            c.equals(c);
        });
    }

    @Test
    void e2hHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new EquatorialToHorizontalConversion(ZDT_SEMESTER_START, EPFL).hashCode();
        });
    }

}


//OUR TESTS
/**
 * @Test public void applyTestAz() {
 * <p>
 * LocalDate t = LocalDate.of(2001, Month.APRIL, 16);
 * ZonedDateTime when = ZonedDateTime.of(t, LocalTime.of(14, 26, 33), ZoneOffset.UTC);
 * GeographicCoordinates where = GeographicCoordinates.ofDeg(20, 52);
 * EquatorialCoordinates eq = EquatorialCoordinates.of(1.25, 0.1);
 * EquatorialToHorizontalConversion converter = new EquatorialToHorizontalConversion(when, where);
 * <p>
 * double actualAz = converter.apply(eq).az();
 * double H = SiderealTime.local(when, where) - eq.ra();
 * double sinHigh = Math.sin(Angle.normalizeSemiPositive(Angle.ofDeg(89.17444855)));
 * double azimut = Angle.normalizePositive(Math.atan2(-Math.cos(eq.dec()) * Math.cos(where.lat()) * Math.sin(H), Math.sin(eq.dec()) - Math.sin(where.lat()) * sinHigh));
 * //double azimut = Angle.normalizePositive(-60.64224229);
 * assertEquals(azimut, actualAz);
 * }
 * @Test public void applyTestAlt() {
 * //Expected creation
 * LocalDate t = LocalDate.of(2001, Month.APRIL, 16);
 * ZonedDateTime when = ZonedDateTime.of(t, LocalTime.of(14, 26, 33), ZoneOffset.UTC);
 * GeographicCoordinates where = GeographicCoordinates.ofDeg(20, 52);
 * EquatorialCoordinates eq = EquatorialCoordinates.of(1.25, 0.1);
 * <p>
 * double H = SiderealTime.local(when, where) - eq.ra();
 * double expectedAlt = Math.asin(Math.sin(eq.dec()) * Math.sin(where.lat()) + Math.cos(eq.dec()) * Math.cos(eq.lat()) * Math.cos(H));
 * double expectedAz = Math.atan2(-Math.cos(eq.dec()) * Math.cos(where.lat()) * Math.sin(H), Math.sin(eq.dec()) - Math.sin(where.lat()) * Math.sin(expectedAlt));
 * <p>
 * // Actual creation
 * EquatorialToHorizontalConversion converter = new EquatorialToHorizontalConversion(when, where);
 * double actualAlt = converter.apply(eq).alt();
 * //Second Expected creation
 * //  double H0 = 0.17136713296983652;
 * // double latitude = 0.9075712110370514;
 * double alt = Angle.normalizeSemiPositive(Angle.ofDeg(89.17444855));
 * assertEquals(alt, actualAlt);
 * <p>
 * /**
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double a = toHoriConver.azDeg();
 * <p>
 * assertEquals(283.2710273,a);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 * <p>
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 */


/**
 * GeographicCoordinates geoCoord = GeographicCoordinates.ofDeg(0,52) ;
 * ZonedDateTime zone1 = ZonedDateTime.of(LocalDate.of(2018, Month.APRIL, 22), LocalTime.of(14, 0), ZoneOffset.UTC);
 * EquatorialToHorizontalConversion conv = new EquatorialToHorizontalConversion(zone1, geoCoord) ;
 * EquatorialCoordinates equaCoor = EquatorialCoordinates.of(1.534726189,0.405255756);
 * HorizontalCoordinates horiCoord = HorizontalCoordinates.ofDeg(283.2710273, 19) ;
 * HorizontalCoordinates toHoriConver = conv.apply(equaCoor);
 * double b = toHoriConver.altDeg();
 * <p>
 * assertEquals(19,b);
 */

