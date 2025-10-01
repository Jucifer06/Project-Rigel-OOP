package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * a time accelerator
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
@FunctionalInterface
public interface TimeAccelerator {
    long NANO_IN_SEC = 1_000_000_000L;

    /**
     * Computed the simulated time
     *
     * @param initialSimulatedTime : the initial simulated time as a ZonedDateTime
     * @param realDeltaTime        : the time that passed since the beginning of the animation (in nanoseconds)
     * @return : simulated time as ZonedDateTime
     */
    ZonedDateTime adjust(ZonedDateTime initialSimulatedTime, long realDeltaTime);

    /**
     * Return a continuous accelerator depending on an acceleration factor
     *
     * @param accelerationFactor : the acceleration factor
     * @return :  a continuous accelerator depending on an acceleration factor
     */
    static TimeAccelerator continuous(int accelerationFactor) {
        return (initialSimulatedTime, realDeltaTime) ->
                initialSimulatedTime.plusNanos(accelerationFactor * realDeltaTime);
    }

    /**
     * Return a discrete accelerator depending on a frequency and a step
     *
     * @param frequency : the frequency
     * @param step      : the step
     * @return : a discrete accelerator depending on a frequency and a step
     */
    static TimeAccelerator discrete(long frequency, Duration step) {
        return (initialSimulatedTime, realDeltaTime) ->
                initialSimulatedTime.plus(step.multipliedBy(realDeltaTime * frequency / NANO_IN_SEC));
    }
}