package seedu.address.ui;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seedu.address.model.person.Person;

/**
 * A popup window for confirmation after a person executes delete contact command.
 */
public class DeletePopup extends UiPart<Stage> {

    private static final String FXML = "DeletePopup.fxml";

    @FXML
    private Label headerLabel;
    @FXML
    private ListView<Person> personListView;
    @FXML
    private TextField inputField;

    private List<Person> matchingResults;
    private boolean isConfirmed = false;
    private Person selectedPerson = null;

    /**
     * Creates a new Delete Pop up.
     *
     * @param root Stage to use as the root of the Delete Pop up.
     */
    public DeletePopup(Stage root) {
        super(FXML, root);
        setUpKeyboardHandlers();
        root.setWidth(500);
        root.setHeight(500);
    }

    /**
     * Creates a new Delete Pop up.
     */
    public DeletePopup() {
        this(new Stage());
    }

    /**
     * Determines the information to be displayed in the Deletion Pop up.
     *
     * @param headerMessage indicates the result of delete command with the input name.
     * @param matchingResults displays the list of possible matching persons, if any.
     */
    public void show(String headerMessage, List<Person> matchingResults) {
        this.matchingResults = matchingResults;
        this.isConfirmed = false;
        this.selectedPerson = null;
        headerLabel.setText(headerMessage);

        personListView.setItems(FXCollections.observableArrayList(matchingResults));
        personListView.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            protected void updateItem(Person person, boolean isEmpty) {
                super.updateItem(person, isEmpty);
                if (isEmpty || person == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    int index = matchingResults.indexOf(person) + 1;
                    setGraphic(new PersonCard(person, index).getRoot());
                }
            }
        });

        inputField.clear();
        inputField.requestFocus();
        getRoot().centerOnScreen();
        getRoot().showAndWait();
    }

    private void setUpKeyboardHandlers() {
        inputField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case ENTER -> handleEnter();
            case ESCAPE -> handleEscape();
            default -> { }
            }
        });
    }

    private void handleEnter() {
        String input = inputField.getText().trim();
        int index;

        try {
            index = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            showError("Please enter a valid index number or press ESC to cancel.");
            return;
        }

        if (index >= 0 && index < matchingResults.size()) {
            selectedPerson = matchingResults.get(index);
            isConfirmed = true;
            getRoot().hide();
        } else {
            showError("Invalid index. Try again or press ESC to cancel.");
        }
    }

    private void handleEscape() {
        isConfirmed = false;
        selectedPerson = null;
        getRoot().hide();
    }

    private void showError(String message) {
        headerLabel.setText(message);
    }

    /**
     * Check if the user confirms deletion.
     * */
    public boolean isConfirmed() {
        return isConfirmed;
    }

    /**
     * Get the person selected for deletion.
     * */
    public Person getSelectedPerson() {
        return selectedPerson;
    }
}
