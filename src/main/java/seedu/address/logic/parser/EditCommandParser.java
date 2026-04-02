package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Functional interface to represent a function that converts a string into object of type T.
     */
    @FunctionalInterface
    private interface StringParser<T> {
        // Solution below inspired by https://stackoverflow.com/a/73036423.
        T parse(String input) throws ParseException;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG, PREFIX_TELEGRAM);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_TELEGRAM);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        setFieldIfPresent(argMultimap.getValue(PREFIX_NAME),
                editPersonDescriptor::setName, ParserUtil::parseName);

        setFieldIfPresent(argMultimap.getValue(PREFIX_PHONE),
                editPersonDescriptor::setPhone, ParserUtil::parsePhone);

        setFieldIfPresent(argMultimap.getValue(PREFIX_EMAIL),
                editPersonDescriptor::setEmail, ParserUtil::parseEmail);

        setFieldIfPresent(argMultimap.getValue(PREFIX_ADDRESS),
                editPersonDescriptor::setAddress, ParserUtil::parseAddress);

        setFieldIfPresent(argMultimap.getValue(PREFIX_TELEGRAM),
                editPersonDescriptor::setTelegram, ParserUtil::parseTelegram);

        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Sets a field in the {@code EditPersonDescriptor} if a value is present.
     * If {@code Optional<String> value} is present, it will be parsed and passed into the updater.
     */
    private <T> void setFieldIfPresent(Optional<String> value, Consumer<T> fieldUpdater,
            StringParser<T> parser) throws ParseException {

        if (value.isPresent()) {
            fieldUpdater.accept(parser.parse(value.get()));
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * Validation of invalid or empty tags is handled in parseTags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(ParserUtil.parseTags(tags));
    }

}
