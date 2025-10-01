package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * the named time accelerator
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public enum NamedTimeAccelerator {

    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60L, Duration.ofDays(1L))),
    SIDEREAL_DAY("jour sidéral",
            TimeAccelerator.discrete(60L, Duration.from(Duration.ofHours(23L).plusMinutes(56L).plusSeconds(4L))));

    private String name;
    private TimeAccelerator accelerator;

    /**
     * The constructor of the enumerated named timed accelerator, which is a pair of name and accelerator
     *
     * @param name        : name to be displayed
     * @param accelerator : time accelerator, it can be continuous or discrete
     */
    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * Returns the name to be displayed of the enumerated pair : timer accelerator and name
     *
     * @return : the name to be displayed
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the accelerator of the enumerated pair : timer accelerator and name
     *
     * @return : the accelerator
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    @Override
    public String toString() {
        return name;
    }
}
