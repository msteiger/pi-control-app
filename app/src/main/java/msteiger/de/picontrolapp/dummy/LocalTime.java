package msteiger.de.picontrolapp.dummy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A very simple and rudimentary implementation for 24h-based localtime.
 */
public class LocalTime {

    private static final Pattern FORMAT  = Pattern.compile("(\\d\\d):(\\d\\d).*");

    private final int hours;
    private final int mins;

    @JsonCreator
    public LocalTime(String timeStr) {
        Matcher m = FORMAT.matcher(timeStr);
        if (!m.matches()) {
            throw new IllegalArgumentException("'" + timeStr + "' does not match format 'hh:mm'");
        }
        hours = Integer.valueOf(m.group(1), 10);  // leading zeros will make it look like octals
        mins = Integer.valueOf(m.group(2), 10);  // leading zeros will make it look like octals
        validate();
    }

    public LocalTime(int selectedHour, int selectedMinute) {
        hours = selectedHour;
        mins = selectedMinute;
        validate();
    }

    public int getHours() {
        return hours;
    }

    public int getMins() {
        return mins;
    }

    private void validate() {
        if (hours < 0 || hours > 23) {
            throw new IllegalArgumentException("Hour invalid: " + hours);
        }

        if (mins < 0 || mins > 59) {
            throw new IllegalArgumentException("Minutes invalid: " + mins);
        }
    }

    @JsonValue
    @Override
    public String toString() {
        return String.format("%02d:%02d", hours, mins);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalTime localTime = (LocalTime) o;

        if (hours != localTime.hours) {
            return false;
        }
        return mins == localTime.mins;
    }

    @Override
    public int hashCode() {
        int result = hours;
        result = 31 * result + mins;
        return result;
    }
}
