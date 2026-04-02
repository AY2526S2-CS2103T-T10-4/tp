package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TelegramTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Telegram(null));
    }

    @Test
    public void constructor_invalidTelegram_throwsIllegalArgumentException() {
        String invalidTelegram = "!!!invalid_tg";
        assertThrows(IllegalArgumentException.class, () -> new Telegram(invalidTelegram));

        assertThrows(IllegalArgumentException.class, () -> new Telegram(""));

        assertThrows(IllegalArgumentException.class, () -> new Telegram("@"));
    }

    @Test
    public void isValidTelegramHandle() {
        assertThrows(NullPointerException.class, () -> Telegram.isValidTelegramHandle(null));

        // invalid telegram handles
        assertFalse(Telegram.isValidTelegramHandle(""));
        assertFalse(Telegram.isValidTelegramHandle("@"));
        assertFalse(Telegram.isValidTelegramHandle("john doe"));
        assertFalse(Telegram.isValidTelegramHandle("john"));
        assertFalse(Telegram.isValidTelegramHandle("-"));

        // valid telegram handles
        assertTrue(Telegram.isValidTelegramHandle("johndoe"));
        assertTrue(Telegram.isValidTelegramHandle("john_doe_123"));
        assertTrue(Telegram.isValidTelegramHandle("123456"));
    }

    @Test
    public void equals() {
        Telegram telegram = new Telegram("@johndoe");

        // same values -> returns true
        assertTrue(telegram.equals(new Telegram("@johndoe")));

        // same object -> returns true
        assertTrue(telegram.equals(telegram));

        // null -> returns false
        assertFalse(telegram.equals(null));

        // different types -> returns false
        assertFalse(telegram.equals(5.0f));

        // different values -> returns false
        assertFalse(telegram.equals(new Telegram("@otherhandle")));

        // case insensitive -> returns true
        assertTrue(telegram.equals(new Telegram("@JohnDoe")));
    }

    @Test
    public void hashCodeMethod() {
        Telegram telegram1 = new Telegram("@johndoe");
        Telegram telegram2 = new Telegram("@johndoe");

        assertTrue(telegram1.hashCode() == telegram2.hashCode());
    }

    @Test
    public void constructor_withoutAtSymbol_addsAtSymbol() {
        Telegram telegramWithoutAt = new Telegram("bob_123");

        Telegram telegramWithAt = new Telegram("@bob_123");

        assertTrue(telegramWithoutAt.equals(telegramWithAt));
    }

    @Test
    public void getDisplayValue_returnsCorrectDisplay() {
        Telegram handleWithoutAt = new Telegram("alice123");
        assertEquals("@alice123", handleWithoutAt.toDisplayString());

        Telegram handleWithAt = new Telegram("@alice123");
        assertEquals("@alice123", handleWithAt.toDisplayString());
    }
}
