package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.synth.SynthCheckBoxMenuItemUI;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyStarCatalogueTest {
    private Star star1 = new Star(10, "star1", EquatorialCoordinates.of(0.5, 0), 0.1f, 0.1f);
    private Star star2 = new Star(20, "star2", EquatorialCoordinates.of(0.6, 0.1), 0.2f, 0.2f);
    private Star star3 = new Star(30, "star3", EquatorialCoordinates.of(0.7, 0.2), 0.3f, 0.3f);

    @Test
    public void constructorInvalid() {
        List<Star> stars = new ArrayList<>();
        stars.add(star1);
        stars.add(star2);
        List<Star> stars2 = new ArrayList<>();
        stars2.add(star2);
        List<Asterism> asterisms = new ArrayList<>();
        asterisms.add(new Asterism(stars));


        assertThrows(IllegalArgumentException.class, () -> {
            new StarCatalogue(stars2, asterisms);
        });
    }

    @Test
    public void testGetters() {
        List<Star> stars = new ArrayList<>();
        stars.add(star1);
        stars.add(star2);
        List<Asterism> asterisms = new ArrayList<>();
        Asterism asterism = new Asterism(stars);
        asterisms.add(asterism);
        stars.add(star3);
        StarCatalogue starCatalogue = new StarCatalogue(stars, asterisms);

        //getter stars()
        List<Star> stars2 = starCatalogue.stars();
        assertEquals(stars, stars2);

        //getter asterisms()
        Set<Asterism> asterismSet = starCatalogue.asterisms();
        assertEquals(new HashSet(asterisms), asterismSet);

        //getter asterismsIndices()
        List<Integer> listIndices = starCatalogue.asterismIndices(asterism);
        List<Integer> expectedListIndices = new ArrayList<>();
        expectedListIndices.add(0);
        expectedListIndices.add(1);
        assertEquals(expectedListIndices, listIndices);
        Asterism asterism2 = new Asterism(stars2);
        assertThrows(IllegalArgumentException.class, () -> {
                    starCatalogue.asterismIndices(asterism2);
                }
        );
    }

    //Star Catalogue Builder

    @Test
    public void constructorBuilderTest() {
        StarCatalogue.Builder builder = new StarCatalogue.Builder();
        assertEquals(new ArrayList<Star>(), builder.stars());
        assertEquals(new ArrayList<Asterism>(), builder.asterisms());
    }

    @Test
    public void adderTest() {
        StarCatalogue.Builder builder = new StarCatalogue.Builder();
        builder.addStar(star1);
        assertTrue(builder.stars().contains(star1));
        List<Star> stars = new ArrayList<>();
        stars.add(star1);
        Asterism asterism = new Asterism(stars);
        builder.addAsterism(asterism);
        assertTrue(builder.asterisms().contains(asterism));
    }

    @Test
    public void gettersAndBuilderTest() {
        List<Star> stars = new ArrayList<>();
        stars.add(star1);
        stars.add(star2);
        Asterism asterism = new Asterism(stars);
        List<Asterism> asterisms = new ArrayList<>();
        asterisms.add(asterism);
        StarCatalogue.Builder builder = new StarCatalogue.Builder();
        builder.addStar(star1);
        builder.stars();
        builder.addStar(star2);
        builder.addStar(star3);
        stars.add(star3);
        builder.addAsterism(asterism);
        //getter stars
        assertEquals(stars, builder.stars());
        //getter asterisms
        assertEquals(asterisms, builder.asterisms());

        //builder
        StarCatalogue starCatalogue1 = new StarCatalogue(stars, asterisms);
        StarCatalogue starCatalogue2 = builder.build();

        assertEquals(starCatalogue1.stars(), starCatalogue2.stars());
        assertEquals(starCatalogue1.asterisms(), starCatalogue2.asterisms());
        assertEquals(starCatalogue1.asterismIndices(asterism), starCatalogue2.asterismIndices(asterism));
    }
}
