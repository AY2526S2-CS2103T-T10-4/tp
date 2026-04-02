package seedu.address.logic;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutInfo;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INDEX_OUT_OF_BOUNDS = "Index exceeds the number of contacts displayed";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getDisplayPhone())
                .append("; Email: ")
                .append(person.getDisplayEmail())
                .append("; Telegram: ")
                .append(person.getDisplayTelegram())
                .append("; Address: ")
                .append(person.getDisplayAddress())
                .append("; Courses: ");

        // Sort tutorials first by course code, then by tutorial code (case-sensitive)
        person.getTutInfos().stream()
                .sorted(Comparator.comparing(TutInfo::getCourseCode).thenComparing(TutInfo::getTutorialCode))
                .forEach(tut -> builder.append("[").append(tut.toDisplayString()).append("]"));

        builder.append("; Tags: ");

        // Sorting by tagName alphabetically (case-sensitive)
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(builder::append);

        return builder.toString();
    }

}
