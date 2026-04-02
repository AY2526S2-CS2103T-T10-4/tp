package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.PersonMatchesKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        PersonMatchesKeywordsPredicate expectedPredicate = new PersonMatchesKeywordsPredicate(
                Arrays.asList("Alice", "Bob"),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        FindCommand expectedFindCommand = new FindCommand(expectedPredicate);

        assertParseSuccess(parser, " " + PREFIX_NAME + "Alice " + PREFIX_NAME + "Bob", expectedFindCommand);

        assertParseSuccess(parser, " \n " + PREFIX_NAME
                + "Alice \n \t " + PREFIX_NAME + "Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_tutorialWithoutCourse_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_TUTORIAL + "T01", FindCommand.MESSAGE_TUTORIAL_REQUIRES_COURSE);
    }



}
