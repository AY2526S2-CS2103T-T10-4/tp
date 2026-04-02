package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.person.TutInfo;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private static final String EMPTY_FIELD_PLACEHOLDER = "-";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String telegram;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedTutInfo> tutInfos = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String
                                         address, @JsonProperty("telegram") String telegram,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("tutInfos") List<JsonAdaptedTutInfo> tutInfos) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.telegram = telegram;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (tutInfos != null) {
            this.tutInfos.addAll(tutInfos);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().map(p -> p.value).orElse(EMPTY_FIELD_PLACEHOLDER);
        email = source.getEmail().map(p -> p.value).orElse(EMPTY_FIELD_PLACEHOLDER);
        address = source.getAddress().map(p -> p.value).orElse(EMPTY_FIELD_PLACEHOLDER);
        telegram = source.getTelegram().map(p -> p.value).orElse(EMPTY_FIELD_PLACEHOLDER);

        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        tutInfos.addAll(source.getTutInfos().stream()
                .map(JsonAdaptedTutInfo::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final List<TutInfo> personTutInfos = new ArrayList<>();
        for (JsonAdaptedTutInfo tutInfo : tutInfos) {
            personTutInfos.add(tutInfo.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        final Optional<Phone> modelPhone = parsePhone(phone);
        final Optional<Email> modelEmail = parseEmail(email);
        final Optional<Address> modelAddress = parseAddress(address);
        final Optional<Telegram> modelTelegram = parseTelegram(telegram);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelTelegram, modelTags, personTutInfos);
    }

    private Optional<Phone> parsePhone(String phone) throws IllegalValueException {
        if (phone == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }

        if (phone.equals(EMPTY_FIELD_PLACEHOLDER)) {
            return Optional.empty();
        }

        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }

        return Optional.of(new Phone(phone));
    }

    private Optional<Email> parseEmail(String email) throws IllegalValueException {
        if (email == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }

        if (email.equals(EMPTY_FIELD_PLACEHOLDER)) {
            return Optional.empty();
        }

        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }

        return Optional.of(new Email(email));
    }

    private Optional<Address> parseAddress(String address) throws IllegalValueException {
        if (address == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }

        if (address.equals(EMPTY_FIELD_PLACEHOLDER)) {
            return Optional.empty();
        }

        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }

        return Optional.of(new Address(address));
    }

    private Optional<Telegram> parseTelegram(String telegram) throws IllegalValueException {
        if (telegram == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Telegram.class.getSimpleName()));
        }

        if (telegram.equals(EMPTY_FIELD_PLACEHOLDER)) {
            return Optional.empty();
        }

        if (!Telegram.isValidTelegramHandle(telegram)) {
            throw new IllegalValueException(Telegram.MESSAGE_CONSTRAINTS);
        }

        return Optional.of(new Telegram(telegram));
    }

}
