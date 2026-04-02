package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AttendCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EnrollCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UnattendCommand;
import seedu.address.logic.commands.UnenrollCommand;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;
import seedu.address.model.person.TutInfo;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    public static final String LOWERCASE_LIST_COMMAND = "list";
    public static final String UPPERCASE_LIST_COMMAND = "LIST";
    public static final String ALTERNATING_CASE_LIST_COMMAND = "LisT";

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().withTelegram("tg_1234").build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName("Amy Bee")
                .withPhone("85355255")
                .withEmail("amy@gmail.com")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .build();

        String commandString = EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                + PersonUtil.getEditPersonDescriptorDetails(descriptor);

        EditCommand command = (EditCommand) parser.parseCommand(commandString);
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        String input = FindCommand.COMMAND_WORD + " " + keywords.stream()
                .map(word -> PREFIX_NAME + word)
                .collect(Collectors.joining(" "));

        FindCommand command = (FindCommand) parser.parseCommand(input);

        PersonMatchesKeywordsPredicate expectedPredicate = new PersonMatchesKeywordsPredicate(
                keywords,
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        assertEquals(new FindCommand(expectedPredicate), command);
    }

    @Test
    public void parseCommand_enroll() throws Exception {
        EnrollCommand command = (EnrollCommand) parser.parseCommand(
                EnrollCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                        + PREFIX_COURSE + "CS2103T " + PREFIX_TUTORIAL + "T03");
        assertEquals(new EnrollCommand(INDEX_FIRST_PERSON, new TutInfo("CS2103T", "T03")), command);
    }

    @Test
    public void parseCommand_unenroll() throws Exception {
        UnenrollCommand command = (UnenrollCommand) parser.parseCommand(
                UnenrollCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                        + PREFIX_COURSE + "CS2103T");
        assertEquals(new UnenrollCommand(INDEX_FIRST_PERSON, "CS2103T"), command);
    }

    @Test
    public void parseCommand_view() throws Exception {
        ViewCommand command = (ViewCommand) parser.parseCommand(
                ViewCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());

        assertEquals(new ViewCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_attend() throws Exception {
        AttendCommand command = (AttendCommand) parser.parseCommand(
                AttendCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                        + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK + "1");
        assertEquals(new AttendCommand(INDEX_FIRST_PERSON, "CS2103T", 1), command);
    }

    @Test
    public void parseCommand_unattend() throws Exception {
        UnattendCommand command = (UnattendCommand) parser.parseCommand(
                UnattendCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " "
                        + PREFIX_COURSE + "CS2103T " + PREFIX_WEEK + "1");
        assertEquals(new UnattendCommand(INDEX_FIRST_PERSON, "CS2103T", 1), command);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_caseInsensitive() throws Exception {
        assertTrue(parser.parseCommand(UPPERCASE_LIST_COMMAND) instanceof ListCommand);
        assertTrue(parser.parseCommand(LOWERCASE_LIST_COMMAND) instanceof ListCommand);
        assertTrue(parser.parseCommand(ALTERNATING_CASE_LIST_COMMAND) instanceof ListCommand);
    }

    @Test
    public void parseCommand_whitespaceHandling() throws Exception {
        // Leading whitespaces
        assertTrue(parser.parseCommand("   " + ListCommand.COMMAND_WORD) instanceof ListCommand);
        // Trailing whitespaces
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + "   ") instanceof ListCommand);
        // Both leading and trailing whitespaces
        assertTrue(parser.parseCommand("   " + ListCommand.COMMAND_WORD + "   ") instanceof ListCommand);
    }
}
