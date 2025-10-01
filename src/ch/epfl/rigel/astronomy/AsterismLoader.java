package ch.epfl.rigel.astronomy;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * An asterism loader
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public enum AsterismLoader implements StarCatalogue.Loader {

    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        //Mapping hipparcos number and star (from catalogue)
        Map<Integer, Star> starAndHipparcosMap = new HashMap<>();
        List<Star> starsOfCatalogue = builder.stars();
        for (Star star : starsOfCatalogue) {
            starAndHipparcosMap.put(star.hipparcosId(), star);
        }

        try (BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            String stringLine = input.readLine();
            while (stringLine != null) {

                String[] stringTable = stringLine.split(",");
                List<Star> starListOfAsterism = new ArrayList<>();

                for (String hipparcosNumber : stringTable) {
                    starListOfAsterism.add(starAndHipparcosMap.get(Integer.parseInt(hipparcosNumber)));
                }

                builder.addAsterism(new Asterism(starListOfAsterism));
                stringLine = input.readLine();
            }
        }
    }
}
