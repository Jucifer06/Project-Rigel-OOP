package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * a star and asterism catalogue
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class StarCatalogue {

    private final List<Star> stars;
    private final Map<Asterism, List<Integer>> asterismMap;

    /**
     * The constructor of the catalogue
     *
     * @param stars     : a list of stars
     * @param asterisms : a list of asterisms
     * @throws : IllegalArgumentException if an asterism contains a star which is not in the star list
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {
        asterismMap = new HashMap<>();
        for (Asterism asterism : asterisms) {
            List<Integer> index = new ArrayList<>();
            List<Star> asterismList = asterism.stars();
            for (Star starOfAsterism : asterismList) {
                if (!stars.contains(starOfAsterism)) {
                    throw new IllegalArgumentException();
                }
                index.add(stars.indexOf(starOfAsterism));
            }
            asterismMap.put(asterism, index);
        }
        this.stars = Collections.unmodifiableList(List.copyOf(stars));
    }

    /**
     * Return the star list of the catalogue
     *
     * @return : the star list of the catalogue
     */
    public List<Star> stars() {
        return stars;
    }

    /**
     * Return the asterism set of the catalogue
     *
     * @return : the asterism set of the catalogue
     */
    public Set<Asterism> asterisms() {
        return Set.copyOf(asterismMap.keySet());
    }

    /**
     * Return the index list (in the star catalogue) of the stars constituting the asterism
     *
     * @param asterism : the asterism
     * @return : the index list (in the star catalogue) of the stars constituting the asterism
     * @throws : IllegalArgumentException if the given asterism is not in the asterism list of the catalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        if (asterismMap.get(asterism) == null) {
            throw new IllegalArgumentException();
        }
        return List.copyOf(asterismMap.get(asterism));
    }

    /**
     * the star catalogue builder
     */
    public static class Builder {

        private List<Star> stars;
        private List<Asterism> asterisms;

        /**
         * the default constructor (initialise the builder for it to be empty)
         */
        public Builder() {
            stars = new ArrayList<>();
            asterisms = new ArrayList<>();
        }

        /**
         * Return a unmodifiable view of the star list of the builder under construction
         *
         * @return : a unmodifiable view of the star list of the builder under construction
         */
        public List<Star> stars() {
            return List.copyOf(stars);
        }

        /**
         * Add the given star to the builder under construction
         *
         * @param star : the star
         * @return : the builder
         */
        public Builder addStar(Star star) {
            stars.add(star);
            return this;
        }

        /**
         * Return a unmodifiable view of the asterism list of the builder under construction
         *
         * @return : a unmodifiable view of the asterism list of the builder under construction
         */
        public List<Asterism> asterisms() {
            return List.copyOf(asterisms);
        }

        /**
         * Add the given asterism to the asterism list of the catalogue under construction
         *
         * @param asterism : the asterism
         * @return : the builder
         */
        public Builder addAsterism(Asterism asterism) {
            asterisms.add(asterism);
            return this;
        }

        /**
         * Request to the loader to add the stars and/or the asterisms obtained by the input stream
         *
         * @param inputStream . the input stream
         * @param loader      : the loader
         * @return : the builder
         * @throws : IOException in case of an input or output error
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * Build and return the star catalogue corresponding to the builder
         *
         * @return : the star catalogue corresponding to the builder
         */
        public StarCatalogue build() {
            return new StarCatalogue(stars, asterisms);
        }

    }

    /**
     * A star and asterism catalogue loader
     */
    public interface Loader {
        /**
         * load the stars and/or asterisms of the input stream and add them to the catalogue under construction
         *
         * @param inputStream : the input stream
         * @param builder     : the catalogue under construction
         * @throws : IOException in case of an input or output error
         */
        void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
