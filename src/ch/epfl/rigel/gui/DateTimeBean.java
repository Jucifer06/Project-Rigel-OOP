package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * a bean to view the date and time
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public final class DateTimeBean {
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();
    private ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>();

    /**
     * Getter for the  content of the object property of date
     *
     * @return : the LocalDate contained in the Object Property assigned to date
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * Getter for the  content of the object property of time
     *
     * @return : the LocalTime contained in the Object Property assigned to time
     */
    public LocalTime getTime() {
        return time.get();
    }

    /**
     * Getter for the  content of the object property of zone
     *
     * @return : the ZoneId contained in the Object Property assigned to zone
     */
    public ZoneId getZone() {
        return zone.get();
    }

    /**
     * Getter of the moment of observation as a ZonedDateTime
     *
     * @return : ZonedDateTime with the date, time and zone
     */
    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.of(this.getDate(), this.getTime(), this.getZone());
    }

    /**
     * Getter of the object property of date
     *
     * @return : object property of LocalDate
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Getter of the object property of time
     *
     * @return : object property of LocalTime
     */

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    /**
     * Getter of the object property of zone
     *
     * @return : object property of ZoneId
     */
    public ObjectProperty<ZoneId> zoneProperty() {
        return zone;
    }

    /**
     * Sets the content of the date object property
     *
     * @param date : LocalDate to be set as the date
     */
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    /**
     * Sets the content of the time object property
     *
     * @param time : LocalTime to be set as the time
     */

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    /**
     * Sets the content of the zone object property
     *
     * @param zone : ZoneId to be set as the zone
     */
    public void setZone(ZoneId zone) {
        this.zone.set(zone);
    }

    /**
     * Setter of the moment of observation from a ZonedDateTime
     *
     * @param when : ZonedDateTime of the moment of observation
     */
    public void setZonedDateTime(ZonedDateTime when) {
        this.setDate(when.toLocalDate());
        this.setTime(when.toLocalTime());
        this.setZone(when.getZone());
    }
}