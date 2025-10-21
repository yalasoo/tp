package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.model.person.Person;

/**
 * A popup window containing the detailed view of a person, following the same pattern as HelpWindow.
 */
public class ViewWindow extends UiPart<Stage> {

    private static final String FXML = "ViewWindow.fxml";
    private static final double MIN_WIDTH = 500;
    private static final double MIN_HEIGHT = 600;

    @FXML
    private FlowPane tags;
    @FXML
    private Label classLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private TextArea notesArea;
    @FXML
    private VBox attendanceContainer; // Add this in your FXML

    private AttendancePanel attendancePanel;

    /**
     * Creates a new ViewWindow.
     *
     * @param root Stage to use as the root of the ViewWindow.
     */
    public ViewWindow(Stage root) {
        super(FXML, root);
        root.setMinWidth(MIN_WIDTH);
        root.setMinHeight(MIN_HEIGHT);
        initializeAttendancePanel();
    }

    /**
     * Creates a new ViewWindow.
     */
    public ViewWindow() {
        this(new Stage());
    }

    /**
     * Initializes the attendance panel and adds it to the container.
     */
    private void initializeAttendancePanel() {
        attendancePanel = new AttendancePanel();
        attendanceContainer.getChildren().add(attendancePanel.getRoot());
    }

    /**
     * Shows the view window with the specified person's details.
     *
     * @param person the person whose details will be displayed, cannot be null.
     */
    public void show(Person person) {
        fillFields(person);
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Shows the view window.
     */
    public void show() {
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the view window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the view window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the view window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Fills all fields with the given person's information.
     *
     * @param person the person whose details will be displayed
     */
    private void fillFields(Person person) {
        nameLabel.setText(person.getName().fullName);
        phoneLabel.setText(person.getPhone().value);
        emailLabel.setText(person.getEmail().value);
        addressLabel.setText(person.getAddress().value);
        classLabel.setText(person.getStudentClass().value);

        // Tags
        if (person.getTags() != null && !person.getTags().isEmpty()) {
            person.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        }

        // Notes
        if (person.getNote() != null && !person.getNote().value.isEmpty()) {
            notesArea.setText(person.getNote().value);
        } else {
            notesArea.setText("No notes available");
        }

        // Attendance
        attendancePanel.setAttendance(person.getAttendance());

        // Set window title
        getRoot().setTitle("View Contact: " + person.getName().fullName);
    }

    /**
     * Clears the display and resets the window title.
     */
    public void clearDisplay() {
        nameLabel.setText("No contact selected");
        phoneLabel.setText("");
        emailLabel.setText("");
        addressLabel.setText("");
        classLabel.setText("");
        tags.getChildren().clear();
        notesArea.setText("");
        attendancePanel.setAttendance(null);
        attendancePanel.resetToCurrentMonth();
        getRoot().setTitle("View Contact");
    }
}
