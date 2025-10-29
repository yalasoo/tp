package seedu.address.ui;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seedu.address.logic.Messages;
import seedu.address.model.person.Person;

/**
 * A popup window for confirmation after user executes delete contact command.
 */
public class DeletePopup extends UiPart<Stage> {

    private static final String FXML = "DeletePopup.fxml";
    public static final String ERROR_STYLE_CLASS = "error-text";

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
     * Creates a new Delete popup window.
     *
     * @param root Stage to use as the root of the Delete popup window.
     */
    public DeletePopup(Stage root) {
        super(FXML, root);
        setUpKeyboardHandlers();
        inputField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());

        root.setWidth(500);
        root.setHeight(500);
        root.initModality(Modality.APPLICATION_MODAL);
        root.setAlwaysOnTop(true);
    }

    /**
     * Creates a new Delete popup window.
     */
    public DeletePopup() {
        this(new Stage());
    }

    /**
     * Displays a deletion confirmation popup window with the given information.
     *
     * @param headerMessage the message displayed at the top of the popup window.
     * @param matchingResults the list of {@code Person} entries displayed for the user to choose from.
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

    /**
     * Defines how keyboard inputs interact with the deletion popup window,
     * enabling selection, confirmation, and cancellation using keys.
     */
    private void setUpKeyboardHandlers() {
        inputField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case ENTER -> handleEnter();
            case ESCAPE -> handleEscape();
            case UP -> handleUp();
            case DOWN -> handleDown();
            default -> { }
            }
        });
    }

    /**
     * Handles the ENTER key action to confirm the selected person for deletion.
     */
    private void handleEnter() {
        String input = inputField.getText().trim();
        int index;

        try {
            index = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            showError(Messages.MESSAGE_INVALID_INDEX_IN_POPUP);
            return;
        }

        if (index >= 0 && index < matchingResults.size()) {
            setStyleToDefault();
            selectedPerson = matchingResults.get(index);
            isConfirmed = true;
            getRoot().hide();
        } else {
            showError(Messages.MESSAGE_INVALID_INDEX_IN_POPUP);
        }
    }

    /**
     * Handles the ESC key action to cancel the deletion and close the popup window.
     */
    private void handleEscape() {
        isConfirmed = false;
        selectedPerson = null;
        getRoot().hide();
    }

    /**
     * Handles the UP key action to select the previous person listed
     * and scroll upwards in the popup window.
     */
    private void handleUp() {
        int prevIndex = personListView.getSelectionModel().getSelectedIndex() - 1;
        if (prevIndex >= 0) {
            personListView.getSelectionModel().select(prevIndex);
            personListView.scrollTo(prevIndex);
        }
    }

    /**
     * Handles the DOWN key action to select the next person listed
     * and scroll downwards in the popup window.
     */
    private void handleDown() {
        int nextIndex = personListView.getSelectionModel().getSelectedIndex() + 1;
        if (nextIndex < personListView.getItems().size()) {
            personListView.getSelectionModel().select(nextIndex);
            personListView.scrollTo(nextIndex);
        }
    }

    /**
     * Displays an error message in the popup window header.
     *
     * @param message the error message to display.
     */
    private void showError(String message) {
        headerLabel.setText(message);
        setStyleToIndicateError();
    }

    /**
     * Sets the input field style to use the default style.
     */
    private void setStyleToDefault() {
        inputField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the input field style to indicate a invalid index.
     */
    private void setStyleToIndicateError() {
        ObservableList<String> styleClass = inputField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Checks if the user confirms deletion.
     * */
    public boolean isConfirmed() {
        return isConfirmed;
    }

    /**
     * Returns the {@code Person} selected by the user for deletion.
     * */
    public Person getSelectedPerson() {
        return selectedPerson;
    }
}
