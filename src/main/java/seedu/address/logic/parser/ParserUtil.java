package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.InvalidIndexMessages;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.person.TutInfo;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes.
 */
public class ParserUtil {

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the input is invalid, (empty, contains multiple
     *                        indices, non-numeric, non-integer, zero, or negative)
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndexString = oneBasedIndex.trim();

        checkMissingIndex(trimmedIndexString);
        checkMultipleIndices(trimmedIndexString);
        checkNonInteger(trimmedIndexString);
        checkNonNumeric(trimmedIndexString);

        int indexValue;

        try {
            indexValue = Integer.parseInt(trimmedIndexString);
        } catch (NumberFormatException e) {
            throw new ParseException(InvalidIndexMessages.MESSAGE_INDEX_OVERFLOW);
        }

        checkZeroIndex(indexValue);
        checkNegativeIndex(indexValue);

        return Index.fromOneBased(indexValue);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String sanitizedName = name.trim().replaceAll("\\s+", " ");
        if (!Name.isValidName(sanitizedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(sanitizedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     * Dash ("-") and empty strings are invalid for user input.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     * Dash ("-") and empty strings are invalid for user input.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String sanitizedAddress = address.trim().replaceAll("\\s+", " ");
        if (!Address.isValidAddress(sanitizedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(sanitizedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     * Dash ("-") and empty strings are invalid for user input.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid or blank.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();

        if (trimmedTag.isEmpty()) {
            throw new ParseException(Tag.MESSAGE_EMPTY_TAG);
        }

        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String telegram} into a {@code Telegram}.
     * Leading and trailing whitespaces will be trimmed.
     * Dash ("-") and empty strings are invalid for user input.
     *
     * @throws ParseException if the given {@code telegram} is invalid.
     */
    public static Telegram parseTelegram(String telegram) throws ParseException {
        requireNonNull(telegram);
        String trimmedTelegram = telegram.trim();

        if (!Telegram.isValidTelegramHandle(trimmedTelegram)) {
            throw new ParseException(Telegram.MESSAGE_CONSTRAINTS);
        }
        return new Telegram(trimmedTelegram);
    }

    /**
     * Parses a {@code String course} into a course code.
     * Leading and trailing whitespaces will be trimmed.
     * Dash ("-") and empty strings are invalid for user input.
     *
     * @throws ParseException if the given {@code course} is invalid.
     */
    public static String parseCourse(String course) throws ParseException {
        requireNonNull(course);
        String trimmedCourse = course.trim();
        if (!TutInfo.isValidCode(trimmedCourse)) {
            throw new ParseException(TutInfo.MESSAGE_CONSTRAINTS);
        }
        return trimmedCourse.toUpperCase();
    }

    /**
     * Parses a {@code String tutorial} into a tutorial code.
     * Leading and trailing whitespaces will be trimmed.
     * Dash ("-") and empty strings are invalid for user input.
     *
     * @throws ParseException if the given {@code tutorial} is invalid.
     */
    public static String parseTutorial(String tutorial) throws ParseException {
        requireNonNull(tutorial);
        String trimmedTutorial = tutorial.trim();
        if (!TutInfo.isValidCode(trimmedTutorial)) {
            throw new ParseException(TutInfo.MESSAGE_CONSTRAINTS);
        }
        return trimmedTutorial.toUpperCase();
    }

    /**
     * Parses a {@code String week} into an {@code int}.
     * @throws ParseException if the given {@code week} is invalid.
     */
    public static int parseWeek(String week) throws ParseException {
        requireNonNull(week);
        String trimmedWeek = week.trim();
        int weekInt;
        try {
            weekInt = Integer.parseInt(trimmedWeek);
        } catch (NumberFormatException e) {
            throw new ParseException("Week must be a number.");
        }
        if (weekInt < 1 || weekInt > TutInfo.NUMBER_OF_WEEKS_PER_SEM) {
            throw new ParseException("Week must be between 1 and " + TutInfo.NUMBER_OF_WEEKS_PER_SEM);
        }
        return weekInt;
    }

    /**
    * Checks if the index string is empty.
    *
    * @param input the trimmed index string
    * @throws ParseException if index is missing
    */
    private static void checkMissingIndex(String input) throws ParseException {
        if (input.isEmpty()) {
            throw new ParseException(InvalidIndexMessages.MESSAGE_MISSING_INDEX);
        }
    }

    /**
    * Checks if the index string contains more than one index separated by whitespace.
    *
    * @param input the trimmed index string
    * @throws ParseException if multiple indices are present
    */
    private static void checkMultipleIndices(String input) throws ParseException {
        String[] indices = input.split("\\s+");
        if (indices.length > 1) {
            throw new ParseException(InvalidIndexMessages.MESSAGE_MULTIPLE_INDICES);
        }
    }

    /**
    * Checks if the index string represents a non-integer.
    *
    * @param input the trimmed index string
    * @throws ParseException if the index string contains a decimal point
    */
    private static void checkNonInteger(String input) throws ParseException {
        if (input.contains(".")) {
            throw new ParseException(InvalidIndexMessages.MESSAGE_INDEX_NON_INTEGER);
        }
    }

    /**
    * Checks if the index string represents a numeric integer value.
    *
    * @param input the trimmed index string
    * @throws ParseException if the index string contains non-numeric characters
    */
    private static void checkNonNumeric(String input) throws ParseException {
        if (!input.matches("-?\\d+")) {
            throw new ParseException(InvalidIndexMessages.MESSAGE_INDEX_NON_NUMERIC);
        }
    }

    /**
    * Checks if the value of integer index is zero.
    *
    * @param indexValue the value of the integer index
    * @throws ParseException if the index is zero
    */
    private static void checkZeroIndex(int indexValue) throws ParseException {
        if (indexValue == 0) {
            throw new ParseException(InvalidIndexMessages.MESSAGE_INDEX_ZERO);
        }
    }

    /**
    * Checks if the value of index is negative.
    *
    * @param indexValue the value of the integer index
    * @throws ParseException if the index is negative
    */
    private static void checkNegativeIndex(int indexValue) throws ParseException {
        if (indexValue < 0) {
            throw new ParseException(InvalidIndexMessages.MESSAGE_INDEX_NEGATIVE);
        }
    }

}
