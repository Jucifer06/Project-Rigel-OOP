package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

/**
 * The moon
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class Moon extends CelestialObject {

    private static final String NAME = "Lune";
    private static final ClosedInterval PHASE_INTERVAL = ClosedInterval.of(0, 1);

    private final float phase;

    /**
     * Constructor of the moon
     *
     * @param equatorialPos : the equatorial position of the moon
     * @param angularSize   : the angular size of the moon
     * @param magnitude     : the magnitude of the moon
     * @param phase         : the phase of the moon
     * @throws : IllegalArgumentException if the phase is not contained in [0, 1]
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super(NAME, equatorialPos, angularSize, magnitude);

        Preconditions.checkArgument(PHASE_INTERVAL.contains(phase));
        this.phase = phase;
    }

    @Override
    public String info() {
        return String.format(Locale.ROOT, "%s (%.1f%s)", name(), phase * 100, "%");
    }
}
