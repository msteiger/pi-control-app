package msteiger.de.picontrolapp.dummy;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: describe
 */
public class TriggerTime {

    private Set<DayOfWeek> weekDays;
    private LocalTime time;

    @JsonCreator
    public TriggerTime(@JsonProperty("weekDays") Set<DayOfWeek> weekDays, @JsonProperty("time") LocalTime time) {
        this.weekDays = weekDays;
        this.time = time;
    }

    /**
     * @return the weekDays
     */
    public Set<DayOfWeek> getWeekDays() {
        return weekDays;
    }
    /**
     * @param weekDays the weekDays to set
     */
    public void setWeekDays(Set<DayOfWeek> weekDays) {
        this.weekDays = weekDays;
    }
    /**
     * @return the time
     */
    public LocalTime getTime() {
        return time;
    }
    /**
     * @param time the time to set
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekDays, time);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TriggerTime other = (TriggerTime) obj;
        return Objects.equals(weekDays, other.weekDays) && Objects.equals(time, other.time);
    }

    @Override
    public String toString() {
        return "TriggerTime [weekDays=" + weekDays + ", time=" + time + "]";
    }
}
