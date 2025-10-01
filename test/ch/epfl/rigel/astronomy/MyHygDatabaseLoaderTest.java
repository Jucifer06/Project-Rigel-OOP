package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyHygDatabaseLoaderTest {

    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";

    @Test
    public void hyDatabaseFirstStar() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            Star expectedStar = new Star(88, "Tau Phe", EquatorialCoordinates.of(
                    0.004696959812148889, -0.8518930353430763), 5.710f, 0.911f);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).build();
            Star actualStar = catalogue.stars().get(0);
            assertEquals(expectedStar.hipparcosId(), actualStar.hipparcosId());
            assertEquals(expectedStar.colorTemperature(), actualStar.colorTemperature());
            assertEquals(expectedStar.equatorialPos().ra(), actualStar.equatorialPos().ra());
            assertEquals(expectedStar.equatorialPos().dec(), actualStar.equatorialPos().dec());
            assertEquals(expectedStar.name(), actualStar.name());
            assertEquals(expectedStar.magnitude(), actualStar.magnitude());
        }
    }

    @Test
    public void hyDatabaseLastStar() throws IOException {
        try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
            Star starExpected = new Star(0, "? Aqr", EquatorialCoordinates.of(
                    6.064662769813043, -0.3919549465551), 5.900f, 0);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).build();
            List<Star> starList = catalogue.stars();
            Star starActual = starList.get(starList.size() - 1);
            assertEquals(starExpected.hipparcosId(), starActual.hipparcosId());
            assertEquals(starExpected.colorTemperature(), starActual.colorTemperature());
            assertEquals(starExpected.equatorialPos().ra(), starActual.equatorialPos().ra());
            assertEquals(starExpected.equatorialPos().dec(), starActual.equatorialPos().dec());
            assertEquals(starExpected.name(), starActual.name());
            assertEquals(starExpected.magnitude(), starActual.magnitude());
        }
    }

    //Tests de l'énoncé
    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel"))
                    rigel = s;
            }
            assertNotNull(rigel);
        }
    }


    //tests lea


    @Test
    void myLoad() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();
            int i = 0;
            for (Star star : catalogue.stars()) {
                if (star.name().charAt(0) == '?') {
                    i = 1;
                    assertEquals(' ', star.name().charAt(1));
                }
            }
            assertEquals(1, i);

        }

    }
    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";

    @Test
    void myTest() throws IOException{
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            List<Star> allStar = new ArrayList<Star>();
            allStar.addAll(catalogue.stars());

            System.out.println("LIST OF STARS :");
            for(Star s : allStar){
                System.out.print(s.hipparcosId() + " ");
            } //should print out the same star IDS as in the fichier (check visually)
            System.out.println();


            System.out.println(); System.out.println("ASTERISMS : ");
            int i;

            //vérifier visuellement en utilisant CTRL-F que les astérismes contenu dans ASTERISMS sont bien les memes
            //flemme de coder une méthode qui vérifie automatiquement
            for(Asterism asterism : catalogue.asterisms()){
                List<Integer> cAstInd = catalogue.asterismIndices(asterism);
                i = 0;
                for(Star star : asterism.stars()){
                    System.out.print("Hip : ");
                    System.out.print(star.hipparcosId());
                    System.out.print("  foundHipparcos : ");
                    System.out.print(allStar.get(cAstInd.get(i)).hipparcosId());

                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                l'index stoqué dans l'astérisme voulu : */
                    assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
                    System.out.print(" ||| ");
                    i++;
                }
                System.out.println();
            }
        }
    }

}
