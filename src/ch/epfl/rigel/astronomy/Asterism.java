package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.Collections;
import java.util.List;

/**
 * Represents an asterism
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */

public final class Asterism {

    private final List<Star> stars;

    /**
     * Constructor of the asterism
     *
     * @param stars : a non-null list of stars
     * @throws : IllegalArgumentException if the list is empty
     * @throws : NullPointerException if the list is null
     */
    public Asterism(List<Star> stars) {
        Preconditions.checkArgument(!stars.isEmpty());
        this.stars = Collections.unmodifiableList(List.copyOf(stars));
    }

    /**
     * Return the star list
     *
     * @return : the star list
     */
    public List<Star> stars() {
        return stars;
    }
}
