package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.InvalidIndexMessages;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.person.TutInfo;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_COURSE = "CS2103!";
    private static final String INVALID_TUTORIAL = "T 18";
    private static final String INVALID_WEEK = "W4";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_COURSE = "CS2101";
    private static final String VALID_TUTORIAL = "T30";
    private static final int VALID_WEEK = 2;

    private static final String WHITESPACE = " \t\r\n";

    private static final String VALID_NAME_WITH_EXTRA_INTERNAL_WHITESPACES = "Rachel        Walker";
    private static final String VALID_NAME_WITH_INTERNAL_TAB = "Rachel\tWalker";

    @Test
    public void parseIndex_missingIndex_throwsParseException() {
        String expectedMessage = InvalidIndexMessages.MESSAGE_MISSING_INDEX;

        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex(""));
        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex("   "));
    }

    @Test
    public void parseIndex_multipleIndices_throwsParseException() {
        String expectedMessage = InvalidIndexMessages.MESSAGE_MULTIPLE_INDICES;

        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex("1 3"));
    }

    @Test
    public void parseIndex_nonIntegerIndex_throwsParseException() {
        String expectedMessage = InvalidIndexMessages.MESSAGE_INDEX_NON_INTEGER;

        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex("2.5"));
    }

    @Test
    public void parseIndex_nonNumericIndex_throwsParseException() {
        String expectedMessage = InvalidIndexMessages.MESSAGE_INDEX_NON_NUMERIC;

        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex("abc"));
        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex("1a"));
    }

    @Test
    public void parseIndex_overflowIndex_throwsParseException() {
        String expectedMessage = InvalidIndexMessages.MESSAGE_INDEX_OVERFLOW;

        String overflowInput = Long.toString((long) Integer.MAX_VALUE + 1);
        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex(overflowInput));
    }

    @Test
    public void parseIndex_zeroIndex_throwsParseException() {
        String expectedMessage = InvalidIndexMessages.MESSAGE_INDEX_ZERO;

        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex("0"));
    }

    @Test
    public void parseIndex_negativeIndex_throwsParseException() {
        String expectedMessage = InvalidIndexMessages.MESSAGE_INDEX_NEGATIVE;

        assertThrows(ParseException.class, expectedMessage, () -> ParserUtil.parseIndex("-2"));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));

        // Leading zero
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("01"));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_placeholderValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone("-"));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_placeholderValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail("-"));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_emptyTag_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(""));
        assertThrows(ParseException.class, () -> ParserUtil.parseTag("  "));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseAddress_extraWhitespaces_returnsSanitizedAddress() throws Exception {
        String addressWithSpaces = "  123,  Jurong   West  ";
        Address expectedAddress = new Address("123, Jurong West");

        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithSpaces));
    }

    @Test
    public void parseName_validValueWithExtraInternalWhitespace_returnsSanitizedName() throws Exception {
        Name expectedName = new Name(VALID_NAME);

        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME_WITH_EXTRA_INTERNAL_WHITESPACES));
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME_WITH_INTERNAL_TAB));
        assertEquals(expectedName.hashCode(),
                ParserUtil.parseName(VALID_NAME_WITH_EXTRA_INTERNAL_WHITESPACES).hashCode());
        assertEquals(expectedName.hashCode(), ParserUtil.parseName(VALID_NAME_WITH_INTERNAL_TAB).hashCode());
    }

    @Test
    public void parseTelegram_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTelegram(null));
    }

    @Test
    public void parseTelegram_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTelegram("!!!invalid"));
    }

    @Test
    public void parseTelegram_invalidEmptyPlaceholder_throwsParseException() throws Exception {
        assertThrows(ParseException.class, () -> ParserUtil.parseTelegram("-"));
    }

    @Test
    public void parseTelegram_validValueWithoutWhitespace_returnsTelegram() throws Exception {
        Telegram expectedTelegram = new Telegram("@johndoe");
        assertEquals(expectedTelegram, ParserUtil.parseTelegram("@johndoe"));
    }

    @Test
    public void parseTelegram_validValueWithWhitespace_returnsTrimmedTelegram() throws Exception {
        String telegramWithWhitespace = "  @johndoe  ";
        Telegram expectedTelegram = new Telegram("@johndoe");
        assertEquals(expectedTelegram, ParserUtil.parseTelegram(telegramWithWhitespace));
    }

    @Test
    public void parseCourse_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseCourse(null));
    }

    @Test
    public void parseCourse_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCourse(INVALID_COURSE));
    }

    @Test
    public void parseCourse_placeholderValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseCourse("-"));
    }

    @Test
    public void parseCourse_validValueWithoutWhitespace_returnsCourse() throws Exception {
        assertEquals(VALID_COURSE, ParserUtil.parseCourse(VALID_COURSE));
    }

    @Test
    public void parseCourse_validValueWithWhitespace_returnsTrimmedCourse() throws Exception {
        String validCourseWithWhitespace = WHITESPACE + VALID_COURSE + WHITESPACE;
        assertEquals(VALID_COURSE, ParserUtil.parseCourse(validCourseWithWhitespace));
    }

    @Test
    public void parseTutorial_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTutorial(null));
    }

    @Test
    public void parseTutorial_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTutorial(INVALID_TUTORIAL));
    }

    @Test
    public void parseTutorial_placeholderValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTutorial("-"));
    }

    @Test
    public void parseTutorial_validValueWithoutWhitespace_returnsTutorial() throws Exception {
        assertEquals(VALID_TUTORIAL, ParserUtil.parseTutorial(VALID_TUTORIAL));
    }

    @Test
    public void parseTutorial_validValueWithWhitespace_returnsTrimmedTutorial() throws Exception {
        String validTutorialWithWhitespace = WHITESPACE + VALID_TUTORIAL + WHITESPACE;
        assertEquals(VALID_TUTORIAL, ParserUtil.parseTutorial(validTutorialWithWhitespace));
    }

    @Test
    public void parseWeek_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseWeek((String) null));
    }

    @Test
    public void parseWeek_invalidValue_throwsParseException() {
        // Empty string
        assertThrows(ParseException.class, () -> ParserUtil.parseWeek(""));

        // Contains non-numeric characters
        assertThrows(ParseException.class, () -> ParserUtil.parseWeek(INVALID_WEEK));

        // Contains negative values
        assertThrows(ParseException.class, () -> ParserUtil.parseWeek("-4"));

        // Contains non-integer values
        assertThrows(ParseException.class, () -> ParserUtil.parseWeek("3.5"));
    }

    @Test
    public void parseWeek_valueOutOfBounds_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseWeek("0"));

        assertThrows(ParseException.class, ()
                -> ParserUtil.parseWeek(String.valueOf(TutInfo.NUMBER_OF_WEEKS_PER_SEM + 1)));
    }

    @Test
    public void parseWeek_validBoundaryValues_returnsWeek() throws Exception {
        assertEquals(1, ParserUtil.parseWeek(String.valueOf(1)));
        assertEquals(TutInfo.NUMBER_OF_WEEKS_PER_SEM,
                ParserUtil.parseWeek(String.valueOf(TutInfo.NUMBER_OF_WEEKS_PER_SEM)));
    }

    @Test
    public void parseWeek_validValueWithoutWhitespace_returnsWeek() throws Exception {
        assertEquals(VALID_WEEK, ParserUtil.parseWeek(String.valueOf(VALID_WEEK)));
    }

    @Test
    public void parseWeek_validValueWithWhitespace_returnsTrimmedWeek() throws Exception {
        String weekWithWhitespace = WHITESPACE + String.valueOf(VALID_WEEK) + WHITESPACE;
        assertEquals(VALID_WEEK, ParserUtil.parseWeek(weekWithWhitespace));
    }
}
