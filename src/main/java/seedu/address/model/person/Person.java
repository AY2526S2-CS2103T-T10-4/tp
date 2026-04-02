package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    // Display value for optional fields that are missing (Optional.empty())
    public static final String MISSING_OPTIONAL_FIELD_VALUE = "-";

    // Mandatory field
    private final Name name;

    // Optional fields
    private final Optional<Phone> phone;
    private final Optional<Email> email;
    private final Optional<Address> address;
    private final Optional<Telegram> telegram;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final List<TutInfo> tutInfos = new ArrayList<>();

    /**
     * Every field must be present and not null.
     * Optional fields will be stored as Optional.empty() if no value is provided.
     */
    public Person(Name name, Optional<Phone> phone, Optional<Email> email, Optional<Address> address,
                Optional<Telegram> telegram, Set<Tag> tags, List<TutInfo> tutInfos) {

        requireAllNonNull(name, phone, email, address, telegram, tags, tutInfos);

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.telegram = telegram;
        this.tags.addAll(tags);
        this.tutInfos.addAll(tutInfos);
    }

    public Name getName() {
        return name;
    }

    public Optional<Phone> getPhone() {
        return phone;
    }

    public Optional<Email> getEmail() {
        return email;
    }

    public Optional<Address> getAddress() {
        return address;
    }

    public Optional<Telegram> getTelegram() {
        return telegram;
    }

    /**
     * Returns a display-friendly string for the {@code Phone}.
     * Returns MISSING_OPTIONAL_FIELD_VALUE if the {@code Phone} is missing.
     */
    public String getDisplayPhone() {
        return phone.map(Phone::toString).orElse(MISSING_OPTIONAL_FIELD_VALUE);
    }

    /**
     * Returns a display-friendly string for the {@code Email}.
     * Returns MISSING_OPTIONAL_FIELD_VALUE if the {@code Email} is missing.
     */
    public String getDisplayEmail() {
        return email.map(Email::toString).orElse(MISSING_OPTIONAL_FIELD_VALUE);
    }

    /**
     * Returns a display-friendly string for the {@code Address}.
     * Returns MISSING_OPTIONAL_FIELD_VALUE if the {@code Address} is missing.
     */
    public String getDisplayAddress() {
        return address.map(Address::toString).orElse(MISSING_OPTIONAL_FIELD_VALUE);
    }

    /**
     * Returns a display-friendly string for the {@code Telegram}.
     * Adds "@" in front of the telegram handle by Telegram::toDisplayString.
     * Returns MISSING_OPTIONAL_FIELD_VALUE if the {@code Telegram} is missing.
     */
    public String getDisplayTelegram() {
        // Telegram::toDisplayString has added '@' in front of telegram value.
        return telegram.map(Telegram::toDisplayString).orElse(MISSING_OPTIONAL_FIELD_VALUE);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable tutInfo list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<TutInfo> getTutInfos() {
        return Collections.unmodifiableList(tutInfos);
    }

    public ObservableList<TutInfo> getObservableTutInfos() {
        return FXCollections.observableList(this.getTutInfos());
    }

    /**
     * Returns true if both persons have the same email.
     * This defines a weaker notion of equality between two persons.
     * The missing emails are not considered unique.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        if (otherPerson == null) {
            return false;
        }

        if (getEmail().isEmpty() || otherPerson.getEmail().isEmpty()) {
            return false;
        }

        return otherPerson.getEmail().equals(getEmail());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && telegram.equals(otherPerson.telegram)
                && tags.equals(otherPerson.tags)
                && tutInfos.equals(otherPerson.tutInfos);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, telegram, tags, tutInfos);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("telegram", telegram)
                .add("tags", tags)
                .add("tutInfos", tutInfos)
                .toString();
    }

    /**
     * Returns true if the Person's name matches the keyword.
     * Case-insensitive.
     */
    public boolean nameMatches(String keyword) {
        return StringUtil.containsIgnoreCase(name.fullName, keyword);
    }

    /**
     * Returns true if the Person's phone matches the keyword.
     * Case-insensitive.
     */
    public boolean phoneMatches(String keyword) {
        return fieldMatches(phone, keyword);
    }

    /**
     * Returns true if the Person's email matches the keyword.
     * Case-insensitive.
     */
    public boolean emailMatches(String keyword) {
        return fieldMatches(email, keyword);
    }

    /**
     * Returns true if the Person's address matches the keyword.
     * Case-insensitive.
     */
    public boolean addressMatches(String keyword) {
        return fieldMatches(address, keyword);
    }

    /**
     * Returns true if the Person's telegram handle matches the keyword.
     * Case-insensitive.
     */
    public boolean telegramMatches(String keyword) {
        String cleanKeyword = (keyword.startsWith("@")) ? keyword.substring(1) : keyword;

        return fieldMatches(telegram, cleanKeyword);
    }

    /**
     * Checks if the value in the Optional matches the given keyword (case-insensitive).
     *
     * @param field the value to check.
     * @param keyword the keyword to match.
     * @return true if the field is present and contains the keyword, false otherwise.
     */
    private boolean fieldMatches(Optional<?> field, String keyword) {
        return field.map(f -> StringUtil.containsIgnoreCase(f.toString(), keyword)).orElse(false);
    }

    /**
     * Returns true if any of the Person's tags matches the keyword
     * Case-insensitive
     */
    public boolean tagMatches(String keyword) {
        return tags.stream()
                .anyMatch(tag -> tag.tagName.equalsIgnoreCase(keyword));
    }

    /**
     * Returns true if any of the Person's enrolled course codes in TutInfo matches the keyword
     * Case-insensitive
     */
    public boolean courseMatches(String keyword) {
        return tutInfos.stream()
                .anyMatch(tutInfo -> tutInfo.getCourseCode().equalsIgnoreCase(keyword));
    }

    /**
     * Returns true if any of the Person's tutorial codes in TutInfo matches the keyword
     * Case-insensitive
     */
    public boolean tutorialMatches(String keyword) {
        return tutInfos.stream()
                .anyMatch(tutInfo -> tutInfo.getTutorialCode().equalsIgnoreCase(keyword));
    }
}
