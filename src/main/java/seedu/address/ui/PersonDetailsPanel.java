package seedu.address.ui;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutInfo;

/**
 * An UI component that displays full details of a {@code person}.
 */
public class PersonDetailsPanel extends UiPart<Region> {

    public static final String MISSING_FIELD_DISPLAY = "---";

    private static final String FXML = "PersonDetailsPanel.fxml";

    private static final String[] FIELD_NAMES = { "Email", "Telegram", "Phone", "Address" };

    private final Logger logger = LogsCenter.getLogger(PersonDetailsPanel.class);

    private final Person person;

    @FXML
    private ScrollPane nameScrollPane;

    @FXML
    private Label name;

    @FXML
    private VBox fieldNamesColumn;

    @FXML
    private VBox fieldValuesColumn;

    @FXML
    private ScrollPane tagsScrollPane;

    @FXML
    private FlowPane tags;

    @FXML
    private ScrollPane courseTutorialsScrollPane;

    @FXML
    private HBox courseTutorials;

    /**
     * Creates a {@code PersonDetailsPanel} showing the default details.
     * Hides the courseTutorials and tags sections from UI by default.
     *
     * @param defaultMessage The message to display when no contact is selected.
     */
    public PersonDetailsPanel(String defaultMessage) {
        super(FXML);
        this.person = null;

        hideOptionalSections();
        displayDefaultDetails(defaultMessage);
    }

    /**
     * Creates a {@code PersonDetailsPanel} showing the details of {@code person}.
     *
     * @param person The {@code person} whose details will be displayed.
     */
    public PersonDetailsPanel(Person person) {
        super(FXML);

        assert person != null : "Person must not be null";
        this.person = person;

        preventVerticalScroll(nameScrollPane);
        displayPersonDetails();
    }

    /**
     * Displays the default details in the panel when no contact is selected.
     * Shows the default message in the Name field, while other fields are empty.
     *
     * @param message The default message to display.
     */
    private void displayDefaultDetails(String message) {
        logger.fine("Showing default message: " + message);

        name.setText(message);

        courseTutorials.getChildren().clear();

        String[] fieldValues = { "", "", "", "" };
        displayFields(fieldValues);

        tags.getChildren().clear();
    }

    /**
     * Displays the full details of a {@code person} in the panel.
     */
    private void displayPersonDetails() {

        name.setText(formatFieldValue(person.getName().fullName));

        String[] fieldValues = {
                person.getDisplayEmail(),
                person.getDisplayTelegram(),
                person.getDisplayPhone(),
                person.getDisplayAddress()
        };

        displayCourseTutorials();
        displayFields(fieldValues);
        displayTags();
    }

    /**
    * Displays the tags of a {@code person} in the panel.
    * Sorts the tags by tag name and hides the tags section from UI if the person has no tags.
    */
    private void displayTags() {
        assert person.getTags() != null : "Tags of the person must not be null";

        tags.getChildren().clear();

        boolean hasTags = !(person.getTags().isEmpty());
        tagsScrollPane.setVisible(hasTags);
        tagsScrollPane.setManaged(hasTags);

        if (!hasTags) {
            logger.fine("No tags to display for " + person.getName().fullName);
            return;
        }

        logger.fine("Displaying " + person.getTags().size() + " tags for " + person.getName().fullName);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Displays all the field names and their corresponding field values.
     *
     * @param fieldValues Array of field values with same order as FIELD_NAMES.
     */
    private void displayFields(String[] fieldValues) {
        assert FIELD_NAMES.length == fieldValues.length
                : "Length of field names and values arrays must be equal";

        fieldNamesColumn.getChildren().clear();
        fieldValuesColumn.getChildren().clear();

        for (int i = 0; i < fieldValues.length; i++) {
            addFieldToColumns(FIELD_NAMES[i], fieldValues[i]);
        }
    }

    /**
    * Creates and adds two labels for a single field to the VBox columns.
    * The labels each represent the field name and its value, e.g. "Telegram:" and "@alice123".
    *
    * @param fieldName The name of the field.
    * @param fieldValue The value of the field.
    */
    private void addFieldToColumns(String fieldName, String fieldValue) {
        String formattedValue = formatFieldValue(fieldValue);

        Label nameLabel = createFieldLabel(fieldName + ":", fieldValue);
        Label valueLabel = createFieldLabel(formattedValue, fieldValue);

        fieldNamesColumn.getChildren().add(nameLabel);
        fieldValuesColumn.getChildren().add(valueLabel);
    }

    /**
     * Creates a field label and applies styling for missing field.
     *
     * @param text The text to display in the label.
     * @param originalValue The original field value used to check if the field is missing.
     * @return A {@code Label} with the text with styling applied for missing field.
     */
    private Label createFieldLabel(String text, String originalValue) {
        Label label = new Label(text);

        if (isMissingValue(originalValue)) {
            label.getStyleClass().add("missing-field");
        }

        return label;
    }

    /**
     * Formats a field value for display.
     * Returns MISSING_FIELD_DISPLAY if the value is empty, or dash showing an unfilled optional field.
     * Returns the value unchanged otherwise.
     *
     * @param value The value of the field.
     * @return The formatted field value for display.
     */
    private String formatFieldValue(String value) {
        assert value != null : "Field values must not be null";

        if (isMissingValue(value)) {
            return MISSING_FIELD_DISPLAY;
        }

        return value;
    }

    /**
    * Checks whether a field value is missing.
    *
    * @param value The field value to check.
    * @return true if the value is missing, false otherwise.
    */
    private boolean isMissingValue(String value) {
        return (value.isEmpty() || value.equals("-"));
    }

    /**
    * Displays the course and tutorial information of the {@code person} in the courseTutorials HBox.
    * Sorts the tutorial information by course code first, then by tutorial code.
    * Hides the courseTutorials section from UI if the person has no tutorials.
    */
    private void displayCourseTutorials() {
        assert person.getTutInfos() != null : "Tutorial information list of the person must not be null";

        courseTutorials.getChildren().clear();

        boolean hasTutorials = !(person.getTutInfos().isEmpty());

        courseTutorialsScrollPane.setVisible(hasTutorials);
        courseTutorialsScrollPane.setManaged(hasTutorials);

        if (!hasTutorials) {
            logger.fine("No course/tutorials to display for " + person.getName().fullName);
            return;
        }

        logger.fine("Displaying " + person.getTutInfos().size() + " tutorials for " + person.getName().fullName);

        person.getTutInfos().stream()
                .sorted(Comparator.comparing(TutInfo::getCourseCode).thenComparing(TutInfo::getTutorialCode))
                .forEach(this::addCourseTutorialLabel);
    }

    /**
     * Creates a label for a single course and tutorial entry and adds it to courseTutorials HBox.
     *
     * @param tutInfo The TutInfo object representing the course and tutorial entry.
     */
    private void addCourseTutorialLabel(TutInfo tutInfo) {
        Label label = new Label(tutInfo.toDisplayString());
        label.getStyleClass().add("course-tutorial-label");

        courseTutorials.getChildren().add(label);
    }

    /**
     * Prevents vertical scrolling of a ScrollPane by consuming vertical scroll events.
     */
    private void preventVerticalScroll(ScrollPane scrollPane) {
        // Solution below inspired by https://stackoverflow.com/a/53991807
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });
    }

    /**
     * Hides the optional UI sections (courseTutorials and tags sections) in the panel.
     */
    private void hideOptionalSections() {
        courseTutorialsScrollPane.setVisible(false);
        courseTutorialsScrollPane.setManaged(false);

        tagsScrollPane.setVisible(false);
        tagsScrollPane.setManaged(false);
    }

}
