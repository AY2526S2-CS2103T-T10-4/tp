package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTelegramHandle(String)}
 */
public class Telegram {

    public static final String MESSAGE_CONSTRAINTS = "Telegram handles must be 5-32 characters long, "
            + "can only contain alphanumeric characters or underscores, "
            + "and can optionally start with '@'.";

    public static final String VALIDATION_REGEX = "^@?[a-zA-Z0-9_]{5,32}$";
    public static final String TELEGRAM_PREFIX = "@";

    public final String value;

    /**
     * Constructs a {@code Telegram}.
     *
     * @param telegramHandle A valid telegram handle.
     */
    public Telegram(String telegramHandle) {
        requireNonNull(telegramHandle);

        String cleanHandle = cleanHandle(telegramHandle);
        checkArgument(isValidTelegramHandle(cleanHandle), MESSAGE_CONSTRAINTS);

        this.value = cleanHandle;
    }

    /**
     * Returns true if a given string is a valid telegramHandle.
     * Dash ("-") and empty strings are considered invalid.
     */
    public static boolean isValidTelegramHandle(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Removes the leading '@' from the telegram handle if present.
     */
    private static String cleanHandle(String telegramHandle) {
        if (telegramHandle.startsWith("@")) {
            return telegramHandle.substring(1);
        }

        return telegramHandle;
    }

    /**
    * Returns telegram handle formatted for display in the UI.
    * Adds '@' in front of telegram value.
    *
    * @return the formatted telegram handle for display.
    */
    public String toDisplayString() {
        return TELEGRAM_PREFIX + value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Telegram)) {
            return false;
        }

        Telegram otherTelegram = (Telegram) other;
        return value.equalsIgnoreCase(otherTelegram.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

}
