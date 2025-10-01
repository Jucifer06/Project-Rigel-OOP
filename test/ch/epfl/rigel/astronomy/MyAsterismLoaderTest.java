package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class MyAsterismLoaderTest {

    private static final String ASTERISMS_TXT = "/asterisms.txt";
    private static final String HYG_CATALOGUE_NAME = "/hygdata_v3.csv";


    @Test
    public void testFirstAsterism() throws IOException {
        try (InputStream asterismStream = getClass().getResourceAsStream(ASTERISMS_TXT)) {
            try (InputStream hygStream = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
                try (InputStream hygStream2 = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {

                    //catalogue
                    StarCatalogue.Builder builder = new StarCatalogue.Builder();
                    builder.loadFrom(hygStream2, HygDatabaseLoader.INSTANCE);
                    StarCatalogue catalogue = builder.loadFrom(asterismStream, AsterismLoader.INSTANCE).build();

                    //Creation Asterism
                    List<Star> starList = new ArrayList<>();
                    List<Integer> listNumberH = new ArrayList<>();
                    listNumberH.add(7607);
                    listNumberH.add(4436);
                    listNumberH.add(2912);
                    listNumberH.add(677);
                    listNumberH.add(3092);
                    listNumberH.add(5447);
                    listNumberH.add(9640);
                    for (int i = 0; i < listNumberH.size(); ++i) {
                        for (Star star : catalogue.stars()) {
                            if (star.hipparcosId() == listNumberH.get(i)) {
                                starList.add(star);
                            }
                        }
                    }
                    Asterism Xasterism = new Asterism(starList);


                    List<Star> starList2 = new ArrayList<>();
                    List<Integer> listNumberH2 = new ArrayList<>();
                    listNumberH2.add(97886);
                    listNumberH2.add(95771);
                    listNumberH2.add(94703);
                    for (int i = 0; i < listNumberH2.size(); ++i) {
                        for (Star star : catalogue.stars()) {
                            if (star.hipparcosId() == listNumberH2.get(i)) {
                                starList2.add(star);
                            }
                        }
                    }

                    List<Integer> hipStars = new ArrayList<Integer>();
                    for (Star s : Xasterism.stars()) {
                        hipStars.add(s.hipparcosId());

                    }
                    assertTrue((catalogue.asterismIndices(Xasterism)).equals(hipStars));


                    assertTrue(catalogue.asterisms().size() == 153);

                    Asterism Xasterism2 = new Asterism(starList2);

                    //First Asterism
                    int i = 0;
                    for (Asterism aCat : catalogue.asterisms()) {
                        for (Star sCat : aCat.stars()) {
                            if (aCat.stars().size() == Xasterism.stars().size() && (
                                    sCat.hipparcosId() == Xasterism.stars().get(0).hipparcosId()
                                            || sCat.hipparcosId() == Xasterism.stars().get(1).hipparcosId()
                                            || sCat.hipparcosId() == Xasterism.stars().get(2).hipparcosId()
                                            || sCat.hipparcosId() == Xasterism.stars().get(3).hipparcosId()
                                            || sCat.hipparcosId() == Xasterism.stars().get(4).hipparcosId()
                                            || sCat.hipparcosId() == Xasterism.stars().get(5).hipparcosId()
                                            || sCat.hipparcosId() == Xasterism.stars().get(6).hipparcosId())) {
                                ++i;
                            }
                        }
                    }

                    System.out.println(i);

                    //Last Asterism
                    int j = 0;
                    for (Asterism aCat : catalogue.asterisms()) {
                        for (Star sCat : aCat.stars()) {
                            if (aCat.stars().size() == Xasterism2.stars().size() && (
                                    sCat.hipparcosId() == Xasterism2.stars().get(0).hipparcosId()
                                            || sCat.hipparcosId() == Xasterism2.stars().get(1).hipparcosId()
                                            || sCat.hipparcosId() == Xasterism2.stars().get(2).hipparcosId())) {
                                ++j;
                            }
                        }
                    }

                    System.out.println(j);
                }
            }
        }
    }


    @Test
    public void testLoaders() throws IOException {
        try (InputStream asterismInput = getClass().getResourceAsStream(ASTERISMS_TXT)) {
            try (InputStream starInput = getClass().getResourceAsStream(HYG_CATALOGUE_NAME)) {
                StarCatalogue.Builder builder = new StarCatalogue.Builder();
                HygDatabaseLoader.INSTANCE.load(starInput, builder);
                AsterismLoader.INSTANCE.load(asterismInput, builder);
            }
        }
    }
}


