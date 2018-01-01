package msteiger.de.picontrolapp.dummy;

/**
 * Adapted from Java8 java.time.DayOfWeek. The int constants have been changed to match
 * the values from Calendar (e.g. Calendar.SUNDAY).
 */
public enum DayOfWeek {

    /**
     * The singleton instance for the day-of-week of Sunday.
     * This has the numeric value of {@code 1}.
     */
    SUNDAY,
    /**
     * The singleton instance for the day-of-week of Monday.
     * This has the numeric value of {@code 2}.
     */
    MONDAY,
    /**
     * The singleton instance for the day-of-week of Tuesday.
     * This has the numeric value of {@code 3}.
     */
    TUESDAY,
    /**
     * The singleton instance for the day-of-week of Wednesday.
     * This has the numeric value of {@code 4}.
     */
    WEDNESDAY,
    /**
     * The singleton instance for the day-of-week of Thursday.
     * This has the numeric value of {@code 5}.
     */
    THURSDAY,
    /**
     * The singleton instance for the day-of-week of Friday.
     * This has the numeric value of {@code 6}.
     */
    FRIDAY,
    /**
     * The singleton instance for the day-of-week of Saturday.
     * This has the numeric value of {@code 7}.
     */
    SATURDAY;
    /**
     * Private cache of all the constants.
     */
    private static final DayOfWeek[] ENUMS = DayOfWeek.values();

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of {@code DayOfWeek} from an {@code int} value.
     * <p>
     * {@code DayOfWeek} is an enum representing the 7 days of the week.
     * This factory allows the enum to be obtained from the {@code int} value.
     * The {@code int} value follows the Java calendar implementation, from 1 (Sunday)
     * to 7 (Saturday).
     *
     * @param dayOfWeek  the day-of-week to represent, from 1 (Sunday) to 7 (Saturday)
     * @return the day-of-week singleton, not null
     * @throws IllegalArgumentException if the day-of-week is invalid
     */
    public static DayOfWeek of(int dayOfWeek) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("Invalid value for DayOfWeek: " + dayOfWeek);
        }
        return ENUMS[dayOfWeek - 1];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the day-of-week {@code int} value.
     * <p>
     * The values are numbered following the Java calendar implementation, from 1 (Sunday) to 7 (Saturday).
     *
     * @return the day-of-week, from 1 (Sunday) to 7 (Saturday)
     */
    public int getValue() {
        return ordinal() + 1;
    }
}
