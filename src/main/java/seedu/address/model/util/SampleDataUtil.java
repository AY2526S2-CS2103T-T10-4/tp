package seedu.address.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.person.TutInfo;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            createPerson("Alex Yeoh", "87438807", "alexyeoh@example.com", "Blk 30 Geylang Street 29, #06-40",
                    "@AlexYeoh1230", getTagSet("friends"),
                    createTutInfoList(new String[]{"CS2103T", "T21"}, new String[]{"CS2101", "T02"})),

            createPerson("Bernice Yu", null, null, "Blk 30 Lorong 3 Serangoon Gardens, #07-18",
                    null, getTagSet("colleagues", "friends"),
                    createTutInfoList(new String[]{"CS2103T", "T21"})),

            createPerson("Charlotte Oliveiro", "93210283", "charlotte@example.com", null,
                    "Charlotte_Here", getTagSet("neighbours"), createTutInfoList()),

            createPerson("David Li", "91031282", "lidavid@example.com", null,
                    null, getTagSet("family"),
                    createTutInfoList(new String[]{"CS2103T", "T01"}, new String[]{"CS2101", "T02"})),

            createPerson("Irfan Ibrahim", null, "irfan@example.com", "Blk 47 Tampines Street 20, #17-35",
                    "Irfan_Ibrahim_1", getTagSet(), createTutInfoList()),

            createPerson("Roy Balakrishnan", "92624417", "royb@example.com", "Blk 45 Aljunied Street 85, #11-31",
                    null, getTagSet("colleagues"), createTutInfoList())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns an ArrayList of {@code TutInfo} created from the given arrays of tutorialInfos.
     * Returns an empty arrayList if no tutorial info is given.
     */
    private static ArrayList<TutInfo> createTutInfoList(String[]... tutorialInfos) {
        return Arrays.stream(tutorialInfos)
                .map(tutorialInfo -> new TutInfo(tutorialInfo[0], tutorialInfo[1]))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Creates a {@code Person} with the given fields.
     * Missing optional fields (phone, email, address, telegram) are represented by null.
     */
    private static Person createPerson(String name, String phone, String email, String address,
                                    String telegram, Set<Tag> tags, ArrayList<TutInfo> tutInfos) {
        return new Person(
            new Name(name),
            createOptionalPhone(phone),
            createOptionalEmail(email),
            createOptionalAddress(address),
            createOptionalTelegram(telegram),
            tags,
            tutInfos
        );
    }

    // Creates optional fields from the given values.
    // If the value is missing represented by null, returns an empty Optional.
    private static Optional<Phone> createOptionalPhone(String phone) {
        return (phone == null) ? Optional.empty() : Optional.of(new Phone(phone));
    }

    private static Optional<Email> createOptionalEmail(String email) {
        return (email == null) ? Optional.empty() : Optional.of(new Email(email));
    }

    private static Optional<Address> createOptionalAddress(String address) {
        return (address == null) ? Optional.empty() : Optional.of(new Address(address));
    }

    private static Optional<Telegram> createOptionalTelegram(String telegram) {
        return (telegram == null) ? Optional.empty() : Optional.of(new Telegram(telegram));
    }
}
