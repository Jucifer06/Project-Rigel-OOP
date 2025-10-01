package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;

/**
 * a time animator
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class TimeAnimator extends AnimationTimer {
    private DateTimeBean dateTimeBean;
    private ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private SimpleBooleanProperty running = new SimpleBooleanProperty();

    private long firstHandle;
    private ZonedDateTime whenFirstHandle = null;

    /**
     * Constructs a time animator
     *
     * @param dateTimeBean : DateTimeBean wanted for the animation
     */
    public TimeAnimator(DateTimeBean dateTimeBean) {
        this.dateTimeBean = dateTimeBean;
        setRunning(false);
    }

    /**
     * Getter for the  content of the object property of accelerator
     *
     * @return : TimeAccelerator contained in the Object Property assigned to accelerator
     */
    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    /**
     * Getter for the  content of the object property of accelerator
     *
     * @return : Boolean contained in the Object Property assigned to running; true when the animation is being executed
     */
    public Boolean getRunning() {
        return running.get();
    }

    /**
     * Getter of the object property of accelerator
     *
     * @return : object property of TimeAccelerator
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     * Getter of the boolean property of running as a read only
     *
     * @return : ReadOnlyBooleanProperty of running
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return ReadOnlyBooleanProperty.readOnlyBooleanProperty(running);
    }

    /**
     * Sets the content of the accelerator object property
     *
     * @param accelerator : TimeAccelerator to be set as the time
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }


    /**
     * Sets the content of the running simple boolean property
     *
     * @param running : Boolean running
     */
    private void setRunning(Boolean running) {
        this.running.set(running);
    }

    /**
     * Start the animation
     */
    @Override
    public void start() {
        super.start();
        setRunning(true);
    }

    /**
     * Stop the animation
     */
    @Override
    public void stop() {
        super.stop();
        setRunning(false);
        whenFirstHandle = null;
    }

    /**
     * update the animation
     *
     * @param l : the number of nanoseconds since a starting time
     */
    @Override
    public void handle(long l) {
        if (whenFirstHandle == null) {
            firstHandle = l;
            whenFirstHandle = dateTimeBean.getZonedDateTime();
        } else {
            long deltaTime = l - firstHandle;
            dateTimeBean.setZonedDateTime(accelerator.getValue().adjust(whenFirstHandle, deltaTime));
        }
    }
}
