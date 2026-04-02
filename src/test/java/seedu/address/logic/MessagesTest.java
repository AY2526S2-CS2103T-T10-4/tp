package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.person.TutInfo;
import seedu.address.testutil.PersonBuilder;

public class MessagesTest {

    @Test
    public void format_personWithNameOnly_success() {
        Person person = new PersonBuilder()
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutTelegram()
                .build();

        String expectedString = person.getName()
                + "; Phone: -"
                + "; Email: -"
                + "; Telegram: -"
                + "; Address: -"
                + "; Courses: "
                + "; Tags: ";

        assertEquals(expectedString, Messages.format(person));
    }

    @Test
    public void format_personWithAllFieldsSingleTag_success() {
        Person person = new PersonBuilder()
                .withTags("friend")
                .withTutInfos(List.of(new TutInfo("CS2101", "t10"), new TutInfo("CS2103T", "t01")))
                .build();

        String expectedString = person.getName()
            + "; Phone: " + person.getDisplayPhone()
            + "; Email: " + person.getDisplayEmail()
            + "; Telegram: " + person.getDisplayTelegram()
            + "; Address: " + person.getDisplayAddress()
            + "; Courses: [" + person.getTutInfos().get(0).toDisplayString() + "]"
            + "[" + person.getTutInfos().get(1).toDisplayString() + "]"
            + "; Tags: " + person.getTags().toArray()[0];

        assertEquals(expectedString.toString(), Messages.format(person));
    }

    @Test
    public void format_personWithAllFieldsMultipleTags_success() {
        Person person = new PersonBuilder()
                .withTags("friend", "brother")
                .withTutInfos(List.of(new TutInfo("CS2101", "t10"), new TutInfo("CS2103T", "t01")))
                .build();

        String expectedTagString = person.getTags().stream()
                .map(tag -> tag.tagName)
                .sorted()
                .map(tagName -> "[" + tagName + "]")
                .collect(Collectors.joining());

        String expectedString = person.getName()
            + "; Phone: " + person.getDisplayPhone()
            + "; Email: " + person.getDisplayEmail()
            + "; Telegram: " + person.getDisplayTelegram()
            + "; Address: " + person.getDisplayAddress()
            + "; Courses: [" + person.getTutInfos().get(0).toDisplayString() + "]"
            + "[" + person.getTutInfos().get(1).toDisplayString() + "]"
            + "; Tags: " + expectedTagString;

        assertEquals(expectedString.toString(), Messages.format(person));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_singleDuplicatePrefix_success() {
        String expectedMessage = Messages.MESSAGE_DUPLICATE_FIELDS + "n/";
        assertEquals(expectedMessage, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_mulitpleDuplicatePrefix_success() {
        // .collect(Collectors.toSet()) in testing method does not ensure fixed ordering
        String expectedMessageOptionOne = Messages.MESSAGE_DUPLICATE_FIELDS + "n/ " + "e/";
        String expectedMessageOptionTwo = Messages.MESSAGE_DUPLICATE_FIELDS + "e/ " + "n/";
        String errorMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_EMAIL);

        assertTrue(errorMessage.equals(expectedMessageOptionOne)
                || errorMessage.equals(expectedMessageOptionTwo));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_repeatedDuplicateDuplicatePrefix_success() {
        String expectedMessage = Messages.MESSAGE_DUPLICATE_FIELDS + "n/";
        assertEquals(expectedMessage,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_NAME));
    }
}
