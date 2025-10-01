package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyStereographicProjectionTest {
    @Test
    public void testCircleCenterForParallel() {
        double latCenter = 0.5;
        HorizontalCoordinates h = HorizontalCoordinates.of(1.2, 0.5);
        HorizontalCoordinates s = HorizontalCoordinates.of(1, 0.8);
        StereographicProjection p = new StereographicProjection(h);
        assertEquals(0.733285455137111, p.circleCenterForParallel(s).y());
        assertEquals(0, p.circleCenterForParallel(s).x());
    }

    @Test
    public void testCircleRadiusForParallel() {
        double latCenter = 0.5;
        HorizontalCoordinates h = HorizontalCoordinates.of(1.2, latCenter);
        HorizontalCoordinates s = HorizontalCoordinates.of(1, 0.8);
        StereographicProjection p = new StereographicProjection(h);
        assertEquals(0.582150237078815778, p.circleRadiusForParallel(s));
        double latCenter2 = 0;
        HorizontalCoordinates h2 = HorizontalCoordinates.of(1.2, latCenter2);
        HorizontalCoordinates s2 = HorizontalCoordinates.of(1, 0);
        StereographicProjection p2 = new StereographicProjection(h2);
        System.out.println(p2.circleRadiusForParallel(s2));
    }

    @Test
    public void testApplyToAngle() {
        HorizontalCoordinates h = HorizontalCoordinates.of(0, 0);
        StereographicProjection p = new StereographicProjection(h);
        double rad = 2.765;
        double result = p.applyToAngle(rad);
        assertEquals(1.6548795024834346, result);
    }

    @Test
    public void testApply() {
        HorizontalCoordinates center = HorizontalCoordinates.of(1.2, 0.6);
        HorizontalCoordinates proj = HorizontalCoordinates.of(0.4, 0.9);
        StereographicProjection p = new StereographicProjection(center);
        double d = 0.555637093428647560583206838532;
        double expectedY = d * (Math.sin(0.9) * Math.cos(0.6) - Math.cos(0.9) * Math.sin(0.6) * Math.cos(-0.8));
        assertEquals(-0.2477673017412939004205648004, p.apply(proj).x(), 0.00000000001);
        assertEquals(expectedY, p.apply(proj).y());
    }

    @Test
    public void TestInverseApply() {
        HorizontalCoordinates center = HorizontalCoordinates.of(1.2, 0.6);
        HorizontalCoordinates proj = HorizontalCoordinates.of(0.4, 0.9);
        StereographicProjection p = new StereographicProjection(center);
        CartesianCoordinates cart = p.apply(proj);
        assertEquals(0.4, p.inverseApply(cart).az(), 1e-9);
        assertEquals(0.9, p.inverseApply(cart).alt(), 1e-9);
        /*
        CartesianCoordinates cart = CartesianCoordinates.of(18.453, 2.351);
        HorizontalCoordinates center = HorizontalCoordinates.of(2.5, 1.3);
        StereographicProjection p = new StereographicProjection(center);
        double rho = 18.602161433553897636705161406285;
        double sinC = 2 * rho / (rho * rho + 1);
        double cosC = (1 - rho * rho) / (rho * rho + 1);
        sinC = 0.10720458423590437;
        cosC = 0.9942369823733207;
        System.out.println("Sin c " + sinC);
        System.out.println("Cos C " + cosC);
        assertEquals(0.3980516758, p.inverseApply(cart).az());
        */
    }

    //Tests Leaa


    @Test
    void myCircleCenterForParallelOrdonneeTest1() {
        HorizontalCoordinates c = HorizontalCoordinates.of(0.2, 0.3);
        StereographicProjection s = new StereographicProjection(c);

        HorizontalCoordinates h = HorizontalCoordinates.of(0.2, 0.3);

        CartesianCoordinates coord = s.circleCenterForParallel(h);

        assertEquals(1.61636407, coord.y(), 1e-6);
        assertEquals(0.00000000, coord.x(), 1e-6);
    }

    @Test
    void myCircleCenterForParallelTest2() {
        HorizontalCoordinates c = HorizontalCoordinates.of(0.2, 1.5);
        StereographicProjection s = new StereographicProjection(c);

        HorizontalCoordinates h = HorizontalCoordinates.of(0.2, 0.3);

        CartesianCoordinates coord = s.circleCenterForParallel(h);

        assertEquals(0.054707169, coord.y(), 1e-6);
    }

    @Test
    void myCircleRadiusForParallelTest() {
        HorizontalCoordinates h = HorizontalCoordinates.of(0.2, 0.3);

        HorizontalCoordinates c = HorizontalCoordinates.of(0.2, 0.3);
        StereographicProjection s = new StereographicProjection(c);
        s.circleRadiusForParallel(h);

        assertEquals(1.61636407, s.circleRadiusForParallel(h), 1e-6);
    }

    @Test
    void myApplyToAngleTest() {

//        StereographicProjection proj =
//       assertEquals(0.10008341, StereographicProjection.applyToAngle(0.2), 1e-6);
    }

    @Test
    void myApplyAbscisseTest1() {
        HorizontalCoordinates c = HorizontalCoordinates.of(0.2, 0.3);
        StereographicProjection s = new StereographicProjection(c);

        HorizontalCoordinates h = HorizontalCoordinates.of(0.2, 0.3);
        CartesianCoordinates coord = s.apply(h);

        assertEquals(0.0000000, coord.x(), 1e-6);
    }

    @Test
    void myApplyAbscisseTest2() {
        HorizontalCoordinates c = HorizontalCoordinates.of(0.2, 0.3);
        StereographicProjection s = new StereographicProjection(c);

        HorizontalCoordinates h = HorizontalCoordinates.of(0.1, 0.2);
        CartesianCoordinates coord = s.apply(h);

        assertEquals(-0.04915946, coord.x(), 1e-6);
    }

    @Test
    void myApplyAbscisseTest3() {
        HorizontalCoordinates c = HorizontalCoordinates.of(0.2, 0.3);
        StereographicProjection s = new StereographicProjection(c);

        HorizontalCoordinates h = HorizontalCoordinates.of(0.2, 0.2);
        CartesianCoordinates coord = s.apply(h);

        assertEquals(-0.0, coord.x(), 1e-6);
    }

    @Test
    void myApplyOrdonneeTest1() {
        HorizontalCoordinates c = HorizontalCoordinates.of(0.2, 0.3);
        StereographicProjection s = new StereographicProjection(c);

        HorizontalCoordinates h = HorizontalCoordinates.of(0.1, 0.2);
        CartesianCoordinates coord = s.apply(h);

        assertEquals(-0.0494323, coord.y(), 1e-6);
    }

    @Test
    void myInverseApplyTest() { //PASSE
        HorizontalCoordinates center = HorizontalCoordinates.of(0.2, 0.3);
        StereographicProjection s = new StereographicProjection(center);

        CartesianCoordinates c = CartesianCoordinates.of(2, 6);

        HorizontalCoordinates coord = s.inverseApply(c);
        CartesianCoordinates cart = s.apply(coord);

        assertEquals(2, cart.x(), 1e-9);
        assertEquals(6, cart.y(), 1e-9);

        HorizontalCoordinates hor = HorizontalCoordinates.of(2.4, 1.1);
        CartesianCoordinates cart2 = s.apply(hor);
        HorizontalCoordinates hor2 = s.inverseApply(cart2);

        assertEquals(2.4, hor2.az(), 1e-9);
        assertEquals(1.1, hor2.alt(), 1e-9);

    }


    StereographicProjection projection = new StereographicProjection(HorizontalCoordinates.ofDeg(26,52));
    HorizontalCoordinates point = HorizontalCoordinates.ofDeg(2,25);
    StereographicProjection projection2 = new StereographicProjection(HorizontalCoordinates.ofDeg(15,0));
    HorizontalCoordinates point2 = HorizontalCoordinates.ofDeg(2,0);

    @Test
    void circleCenterForParallelWorks() {
        assertEquals(CartesianCoordinates.of(0, 0.5085467699).x(), projection.circleCenterForParallel(point).x());
        assertEquals(CartesianCoordinates.of(0, 0.5085467699).y(), projection.circleCenterForParallel(point).y(), 1e-9);
        assertEquals(0, projection2.circleCenterForParallel(point2).x());
        assertEquals((1.0/0.0), projection2.circleCenterForParallel(point2).y());
    }

    @Test
    void circleRadiusForParallelWorks() {
        assertEquals(0.74862552899, projection.circleRadiusForParallel(point), 1e-9);
    }

    @Test
    void applyToAngleWorks() {
        assertEquals(0.0873218859, projection.applyToAngle(Angle.ofDeg(10)), 1e-9);
    }

    @Test
    void applyWorks() {
        assertEquals(-0.20004083112004686, projection.apply(HorizontalCoordinates.ofDeg(2, 25)).x(), 1e-9);
        assertEquals(-0.21285731140715247, projection.apply(HorizontalCoordinates.ofDeg(2, 25)).y(), 1e-9);
    }

    @Test
    void inverseApplyWorks() {
        CartesianCoordinates coord = CartesianCoordinates.of(-0.20004083112004686, -0.21285731140715247);
        assertEquals(point.azDeg(), projection.inverseApply(CartesianCoordinates.of(-0.20004083112004686, -0.21285731140715247)).azDeg(), 1e-9);
        assertEquals(point.altDeg(), projection.inverseApply(CartesianCoordinates.of(-0.20004083112004686, -0.21285731140715247)).altDeg(), 1e-9);
    }

    @Test
    void StereographicProjectionHashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, ()-> projection.hashCode());
    }

    @Test
    void StereographicProjectionEqualsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, ()-> projection.equals(this));
    }

    @Test
    void toStringWorks() {
        assertEquals("Center of the StereographicProjection (0.4538; 0.9076)", projection.toString());
    }

   /* @Test
    void myInverseApplyLatitudeTest() { //PASSE
        HorizontalCoordinates h = HorizontalCoordinates.of(0.2, 0.3);
        StereographicProjection s = new StereographicProjection(h);

        CartesianCoordinates c = CartesianCoordinates.of(2, 6);

        HorizontalCoordinates coord = s.inverseApply(c);
// moi j'ai trouvé 0.099165763
        assertEquals(-0.31517328, coord.lat(), 1e-6);
    }


    */

}
