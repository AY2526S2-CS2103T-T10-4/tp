package seedu.address.model.person;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a student's enrollment and attendance in a tutorial.
 */
public class TutInfo {
    public static final int NUMBER_OF_WEEKS_PER_SEM = 13;

    public static final String MESSAGE_CONSTRAINTS = "Course code and Tutorial code should be alphanumeric";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    private final String courseCode;
    private final String tutorialCode;
    private final boolean[] attendance;

    /**
     * Constructs a {@code TutInfo} with given attendance array.
     *
     * @param courseCode A valid course code.
     * @param tutorialCode A valid tutorial code.
     * @param attendance Attendance array.
     */
    public TutInfo(String courseCode, String tutorialCode, boolean[] attendance) {
        requireAllNonNull(courseCode, tutorialCode, attendance);
        checkArgument(isValidCode(courseCode), MESSAGE_CONSTRAINTS);
        checkArgument(isValidCode(tutorialCode), MESSAGE_CONSTRAINTS);

        this.courseCode = courseCode.toUpperCase();
        this.tutorialCode = tutorialCode.toUpperCase();
        // Defensive programming, to prevent the client from providing an array with wrong length
        this.attendance = Arrays.copyOf(attendance, NUMBER_OF_WEEKS_PER_SEM);
    }

    /**
     * Constructs a {@code TutInfo} with all tutorial slots unattended.
     *
     * @param courseCode A valid course code. (E.g. CS2109S)
     * @param tutorialCode A valid tutorial code. (E.g. Tut01)
     */
    public TutInfo(String courseCode, String tutorialCode) {
        this(courseCode.toUpperCase(), tutorialCode.toUpperCase(), new boolean[NUMBER_OF_WEEKS_PER_SEM]);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTutorialCode() {
        return tutorialCode;
    }

    public boolean[] getAttendance() {
        return Arrays.copyOf(attendance, NUMBER_OF_WEEKS_PER_SEM);
    }

    /**
     * Returns true if a given string is a valid course/tutorial code.
     */
    public static boolean isValidCode(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns a new TutInfo with updated attendance for the specific week.
     *
     * @param week Week number (1-13).
     * @param attended Attendance status.
     * @return New TutInfo instance.
     */
    public TutInfo setAttendance(int week, boolean attended) {
        if (week < 1 || week > NUMBER_OF_WEEKS_PER_SEM) {
            throw new IllegalArgumentException("Week must be between 1 and " + NUMBER_OF_WEEKS_PER_SEM);
        }
        boolean[] newAttendance = Arrays.copyOf(attendance, NUMBER_OF_WEEKS_PER_SEM);
        newAttendance[week - 1] = attended;
        return new TutInfo(courseCode, tutorialCode, newAttendance);
    }

    /**
     * Formats and returns the course code and tutorial code separated by a space.
     *
     * @return A formatted string representation of the course and tutorial codes for display.
     */
    public String toDisplayString() {
        return courseCode + " " + tutorialCode;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TutInfo)) {
            return false;
        }

        TutInfo otherTutInfo = (TutInfo) other;
        return courseCode.equals(otherTutInfo.courseCode)
                && tutorialCode.equals(otherTutInfo.tutorialCode)
                && Arrays.equals(attendance, otherTutInfo.attendance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, tutorialCode, Arrays.hashCode(attendance));
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return courseCode + " " + tutorialCode + " " + Arrays.toString(attendance);
    }
}
