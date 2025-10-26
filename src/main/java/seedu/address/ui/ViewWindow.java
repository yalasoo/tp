package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
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
    private Label birthdayLabel;
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
    private VBox attendanceSection;
    @FXML
    private VBox attendanceContainer;

    private AttendancePanel attendancePanel;

    // Custom text
    @FXML
    private Label contactInfoHeader;
    @FXML
    private Label phoneText;
    @FXML
    private Label emailText;

    /**
     * Creates a new ViewWindow.
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
        clearDisplay();
        fillFields(person);
        getRoot().show();
        getRoot().centerOnScreen();
        getRoot().requestFocus();
        setUpKeyboardNavigation();
    }

    /**
     * Shows the view window.
     */
    public void show() {
        getRoot().show();
        getRoot().centerOnScreen();

        // Ensure the scene has focus to receive key events
        if (getRoot().getScene() != null) {
            getRoot().getScene().getRoot().requestFocus();
        }
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
        birthdayLabel.setText(person.getBirthday().value);

        // Tags
        boolean isStudent = person.getTags().stream()
                .anyMatch(tag -> tag.tagName.equalsIgnoreCase("student"));

        // Update contact information header based on tag
        if (isStudent) {
            contactInfoHeader.setText("Emergency Contact");
            phoneText.setText("Phone:");
            emailText.setText("Email:");
        } else {
            contactInfoHeader.setText("Contact Information");
            phoneText.setText("Personal Phone:");
            emailText.setText("Personal Email:");
        }

        // Tags with different colors based on type
        tags.getChildren().clear(); // Clear any existing tags
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    // Apply different CSS classes based on tag type
                    if (isStudent) {
                        tagLabel.getStyleClass().add("student-tag");
                    } else {
                        tagLabel.getStyleClass().add("colleague-tag");
                    }
                    tags.getChildren().add(tagLabel);
                });

        // Notes
        if (person.getNote() != null && !person.getNote().value.isEmpty()) {
            notesArea.setText(person.getNote().value);
        } else {
            notesArea.setText("No notes available");
        }

        // Attendance - Only show for students

        attendanceSection.setVisible(isStudent);

        if (isStudent) {
            attendancePanel.setAttendance(person.getAttendance());
        } else {
            attendancePanel.setAttendance(null);
        }

        // Set window title
        getRoot().setTitle("View Contact: " + person.getName().fullName);
    }

    /**
     * Sets up listener for left and arrow key to
     * navigate previous and next month respectively.
     */
    public void setUpKeyboardNavigation() {
        getRoot().getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT) {
                attendancePanel.navigatePreviousMonth();
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT) {
                attendancePanel.navigateNextMonth();
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                hide();
                event.consume();
            }
        });
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
        birthdayLabel.setText("");
        tags.getChildren().clear();
        notesArea.setText("");
        attendancePanel.setAttendance(null);
        attendanceSection.setVisible(true);
        attendanceSection.setManaged(true);
        attendancePanel.resetToCurrentMonth();
        getRoot().setTitle("View Contact");
    }
}
