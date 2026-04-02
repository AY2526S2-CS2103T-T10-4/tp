package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void getDisplayPhone_withOrWithoutPhone_returnsCorrectValue() {
        Person personWithPhone = new PersonBuilder().withPhone("80001212").build();
        assertEquals("80001212", personWithPhone.getDisplayPhone());

        Person personWithoutPhone = new PersonBuilder().withoutPhone().build();
        assertEquals(Person.MISSING_OPTIONAL_FIELD_VALUE, personWithoutPhone.getDisplayPhone());
    }

    @Test
    public void getDisplayEmail_withOrWithoutEmail_returnsCorrectValue() {
        Person personWithEmail = new PersonBuilder().withEmail("xxx@gmail.com").build();
        assertEquals("xxx@gmail.com", personWithEmail.getDisplayEmail());

        Person personWithoutEmail = new PersonBuilder().withoutEmail().build();
        assertEquals(Person.MISSING_OPTIONAL_FIELD_VALUE, personWithoutEmail.getDisplayEmail());
    }

    @Test
    public void getDisplayAddress_withOrWithoutAddress_returnsCorrectValue() {
        Person personWithAddress = new PersonBuilder().withAddress("Blk 30 Geylang Street 29, #06-40").build();
        assertEquals("Blk 30 Geylang Street 29, #06-40", personWithAddress.getDisplayAddress());

        Person personWithoutAddress = new PersonBuilder().withoutAddress().build();
        assertEquals(Person.MISSING_OPTIONAL_FIELD_VALUE, personWithoutAddress.getDisplayAddress());
    }

    @Test
    public void getDisplayTelegram_withOrWithoutTelegram_returnsCorrectValue() {
        Person personWithTelegram = new PersonBuilder().withTelegram("AlexYeoh1230").build();
        assertEquals("@AlexYeoh1230", personWithTelegram.getDisplayTelegram());

        Person personWithoutTelegram = new PersonBuilder().withoutTelegram().build();
        assertEquals(Person.MISSING_OPTIONAL_FIELD_VALUE, personWithoutTelegram.getDisplayTelegram());
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same email, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different email, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // email differs in case, all other attributes same -> returns true
        Person editedBob = new PersonBuilder(BOB).withEmail(VALID_EMAIL_BOB.toUpperCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // email is missing, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withoutEmail().build();
        Person anotherEditedAlice = new PersonBuilder(ALICE).withoutEmail().build();
        assertFalse(editedAlice.isSamePerson(anotherEditedAlice));

        // first person email is missing, second person email is present -> returns false
        assertFalse(editedAlice.isSamePerson(ALICE));

        // first person email is present, second person email is missing -> returns false
        assertFalse(ALICE.isSamePerson(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different telegram -> returns false
        editedAlice = new PersonBuilder(ALICE).withTelegram("@different_tg").build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tutInfos -> returns false
        editedAlice = new PersonBuilder(ALICE).withTutInfos(List.of(new TutInfo("CS2109S", "T19"))).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address="
                + ALICE.getAddress() + ", telegram=" + ALICE.getTelegram() + ", tags=" + ALICE.getTags()
                + ", tutInfos=" + ALICE.getTutInfos() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void hashCodeMethod() {
        // same values -> returns same hashcode
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.hashCode() == aliceCopy.hashCode());
    }

    @Test
    public void testMatchingMethods() {
        TutInfo cs2103Tut = new TutInfo("CS2103T", "G01");
        TutInfo cs2109Tut = new TutInfo("CS2109", "G02");
        Person person = new PersonBuilder(ALICE)
                .withTutInfos(Arrays.asList(cs2109Tut, cs2103Tut))
                .withTelegram("@alice_pauline")
                .build();


        // nameMatches
        assertTrue(person.nameMatches("Alice"));
        assertTrue(person.nameMatches("ALICE"));
        // partial match
        assertTrue(person.nameMatches("Ali"));
        assertFalse(person.nameMatches("Bob"));


        // phoneMatches
        assertTrue(person.phoneMatches("94351253"));
        // partial match
        assertTrue(person.phoneMatches("9435125"));
        assertFalse(person.phoneMatches("9435126"));


        // emailMatches
        assertTrue(person.emailMatches("alice@example.com"));
        assertTrue(person.emailMatches("alice")); // partial match
        assertFalse(person.emailMatches("bob"));


        // addressMatches
        assertTrue(person.addressMatches("Jurong"));
        assertTrue(person.addressMatches("123"));
        assertFalse(person.addressMatches("Clementi"));


        // telegramMatches
        assertTrue(person.telegramMatches("alice"));
        assertTrue(person.telegramMatches("@alice"));
        assertFalse(person.telegramMatches("bob"));


        // tagMatches
        assertTrue(person.tagMatches("friends"));
        assertTrue(person.tagMatches("FRIENDS"));
        assertFalse(person.tagMatches("nonexistent"));


        // courseMatches
        assertTrue(person.courseMatches("CS2103T"));
        assertTrue(person.courseMatches("cs2103t"));
        assertFalse(person.courseMatches("CS210"));
        assertFalse(person.courseMatches("CS2100"));


        // tutorialMatches
        assertTrue(person.tutorialMatches("G01"));
        assertTrue(person.tutorialMatches("g01"));
        assertTrue(person.tutorialMatches("G02"));
        assertFalse(person.tutorialMatches("G0"));
        assertFalse(person.tutorialMatches("G03"));
    }
}
