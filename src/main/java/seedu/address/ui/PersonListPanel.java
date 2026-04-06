package seedu.address.ui;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    // True if the selection triggered manually via mouse click or keyboard press is changing selectedPerson.
    // False if the selection was triggered via a user command.
    private boolean isManualSelection = false;

    // Index of the last person selected to be shown in the ExpandedContactPanel.
    private int lastShownIndex = -1;

    @FXML
    private ListView<Person> personListView;

    @FXML
    private ScrollPane tutInfosScrollPane;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     * Tracks mouse clicks and keyboard presses and responds to any changes in person selected.
     */
    public PersonListPanel(ObservableList<Person> personList, Consumer<Person> onPersonSelected) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        logger.fine("Initializing PersonListPanel with " + personList.size() + " contacts in the list.");

        trackMouseClicks();

        trackKeyboardPresses();

        personListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lastShownIndex = personListView.getSelectionModel().getSelectedIndex();

                logger.fine("Person selected in personListView changes to "
                        + newValue.getName().fullName + " at index: " + lastShownIndex);
            }

            onPersonSelected.accept(newValue);
        });
    }

    /**
     * Tracks mouse clicks on the {@code PersonListPanel}.
     * Updates isManualSelection to true only if a different {@code PersonCard} is mouse-clicked.
     * Updates isManualSelection to false if empty space or same {@code PersonCard} is mouse-clicked.
     */
    private void trackMouseClicks() {
        personListView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Node node = event.getPickResult().getIntersectedNode();

            while (node != null && !(node instanceof ListCell)) {
                node = node.getParent();
            }

            if (!(node instanceof ListCell)) {
                return;
            }

            // Safe to suppress as all cells in the PersonListView are ListCell<Person>
            @SuppressWarnings("unchecked")
            ListCell<Person> cell = (ListCell<Person>) node;

            // Clicking empty space or scrollbar does not change the selected person
            if (cell.getItem() == null) {
                isManualSelection = false;
                return;
            }

            int currentClickedIndex = cell.getIndex();
            int lastClickedIndex = personListView.getSelectionModel().getSelectedIndex();

            // Only clicking different PersonCard is considered as a manual selection
            isManualSelection = (currentClickedIndex != lastClickedIndex);
        });
    }

    /**
     * Tracks keyboard presses on the {@code PersonListPanel}.
     * Only the keys that can change the selected person will update isManualSelection to true.
     */
    private void trackKeyboardPresses() {
        personListView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            int listSize = personListView.getItems().size();
            int currentIndex = personListView.getSelectionModel().getSelectedIndex();

            // Detect pressing of any special keys to mark isManualSelection false
            boolean isSpecialKeyPressed =
                    event.isShiftDown() || event.isControlDown() || event.isAltDown() || event.isMetaDown();

            // No possible changes in selected person or special keys pressed
            if (currentIndex == -1 || listSize <= 1 || isSpecialKeyPressed) {
                isManualSelection = false;
                return;
            }

            int targetIndex = calculateTargetIndex(listSize, currentIndex, event.getCode());

            // Only keyboard press that changes the index of selected person counts as manual selection
            isManualSelection = (targetIndex != currentIndex);
        });
    }

    /**
     * Calculate the index of the person reached after the keyboard press is processed.
     */
    private int calculateTargetIndex(int listSize, int currentIndex, KeyCode key) {
        switch (key) {
        case UP, PAGE_UP:
            return Math.max(currentIndex - 1, 0);

        case DOWN, PAGE_DOWN:
            return Math.min(currentIndex + 1, listSize - 1);

        case HOME:
            return 0;

        case END:
            return listSize - 1;

        default:
            return currentIndex;
        }
    }

    /**
     * Scrolls the {@code PersonListPanel} to show and select the given {@code Person}.
     * This method should only be triggered by user-command and not by manual interaction (mouse or keyboard).
     *
     * @param person the {@code Person} to scroll to and select.
     */
    public void scrollToPerson(Person person) {
        assert person != null : "The person being scrolled to in PersonListPanel must not be null";
        assert !(isManualSelection) : "scrollToPerson should not be called by mouse click or keyboard press";

        int index = personListView.getItems().indexOf(person);

        if (index >= 0) {
            personListView.layout();
            personListView.getSelectionModel().select(index);

            logger.fine("By commands input, scrolling to person: " + person.getName().fullName
                    + " at index: " + index);

            // Scroll to show the last PersonCard fully
            int scrollIndex = (index == personListView.getItems().size() - 1) ? index + 1 : index;
            personListView.scrollTo(scrollIndex);
        }
    }

    /**
     * Clears any currently selected person and scrolls to the index of last shown person.
     */
    public void clearSelection() {
        personListView.getSelectionModel().clearSelection();
        int listSize = personListView.getItems().size();
        int scrollIndex = calculateScrollIndexAfterClearingSelection(lastShownIndex, listSize);

        if (listSize == 0) {
            lastShownIndex = -1;
        } else if (lastShownIndex >= listSize) {
            lastShownIndex = listSize - 1;
        }

        if (scrollIndex >= 0) {
            personListView.scrollTo(scrollIndex);
        }

        assert isValidLastShownIndex(listSize)
                : "lastShownIndex must be between -1 and the number of persons in personListView";

        logger.fine("Cleared person selection successfully and "
                + "scrolled to last shown index of " + lastShownIndex);
    }

    static int calculateScrollIndexAfterClearingSelection(int lastShownIndex, int listSize) {
        if (listSize <= 0) {
            return -1;
        }

        return Math.min(Math.max(lastShownIndex, 0), listSize - 1);
    }

    private boolean isValidLastShownIndex(int listSize) {
        return lastShownIndex == -1 || (lastShownIndex >= 0 && lastShownIndex < listSize);
    }

    /**
     * Returns true if the selection was triggered manually by a mouse click or keyboard press.
     */
    public boolean isManualSelection() {
        return isManualSelection;
    }

    /**
     * Marks the current selection as not triggered manually (i.e. triggered via commands).
     */
    public void resetManualSelection() {
        isManualSelection = false;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            PersonCard card = new PersonCard(person, getIndex() + 1, getListView());
            card.getRoot().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                getListView().getSelectionModel().select(getIndex());
            });

            setGraphic(card.getRoot());
        }
    }

}
