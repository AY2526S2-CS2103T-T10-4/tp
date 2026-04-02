package seedu.address.ui;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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

    // True when a PersonCard is selected by a mouse click
    private boolean isMouseClick = false;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, Consumer<Person> onPersonSelected) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        logger.fine("Initializing PersonListPanel with " + personList.size() + " contacts in the list.");

        personListView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            isMouseClick = true;
        });

        personListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> onPersonSelected.accept(newValue));
    }

    /**
     * Scrolls the {@code PersonListPanel} to show and select the given {@code Person}.
     * This method should only be triggered by user-commands and not by mouse clicks.
     *
     * @param person the {@code Person} to scroll to and select.
     */
    public void scrollToPerson(Person person) {
        assert person != null : "The person being scrolled to in PersonListPanel must not be null";
        assert !isMouseClick : "scrollToPerson should not be called by a mouse click";

        int index = personListView.getItems().indexOf(person);

        if (index >= 0) {
            personListView.scrollTo(index);
            personListView.getSelectionModel().select(index);
        }
    }

    /**
     * Returns true if the selection was triggered by a mouse click.
     */
    public boolean isMouseClick() {
        return isMouseClick;
    }

    /**
     * Marks the current selection was not due to a mouse click.
     */
    public void resetMouseClick() {
        isMouseClick = false;
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
            } else {
                PersonCard card = new PersonCard(person, getIndex() + 1);
                card.getRoot().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    getListView().getSelectionModel().select(getIndex());
                    event.consume();
                });
                setGraphic(card.getRoot());
            }
        }
    }

}
