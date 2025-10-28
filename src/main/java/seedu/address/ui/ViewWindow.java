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

    // Contact labels for non-students (in personal info section)
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label addressLabel;

    // Contact labels for students (in separate contact section)
    @FXML
    private Label phoneLabelStudent;
    @FXML
    private Label emailLabelStudent;
    @FXML
    private Label addressLabelStudent;

    @FXML
    private TextArea notesArea;
    @FXML
    private VBox attendanceSection;
    @FXML
    private VBox attendanceContainer;

    // Layout containers
    @FXML
    private VBox personalInfoSection;
    @FXML
    private VBox contactInfoSection;
    @FXML
    private VBox contactFieldsForNonStudents;

    // Headers
    @FXML
    private Label personalInfoHeader;
    @FXML
    private Label contactInfoHeader;
    @FXML
    private Label classText;

    private AttendancePanel attendancePanel;

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
        classLabel.setText(person.getStudentClass().value);
        birthdayLabel.setText(person.getBirthday().value);

        // Tags
        boolean isStudent = person.getTags().stream()
                .anyMatch(tag -> tag.tagName.equalsIgnoreCase("student"));

        // Update layout based on whether it's a student or not
        if (isStudent) {
            // Student layout: Separate sections
            setupStudentLayout(person);
            classText.setText("Class:");
        } else {
            // Non-student layout: Merged sections
            setupNonStudentLayout(person);
            classText.setText("Class taught:");
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
        attendanceSection.setManaged(isStudent); // This affects layout

        if (isStudent) {
            attendancePanel.setAttendance(person.getAttendance());
        } else {
            attendancePanel.setAttendance(null);
        }

        // Set window title
        getRoot().setTitle("View Contact: " + person.getName().fullName);
    }

    /**
     * Sets up the layout for student contacts with separate sections.
     */
    private void setupStudentLayout(Person person) {
        // Show separate contact section for students
        contactInfoSection.setVisible(true);
        contactInfoSection.setManaged(true);

        // Hide contact fields in personal info section
        contactFieldsForNonStudents.setVisible(false);
        contactFieldsForNonStudents.setManaged(false);

        // Update headers
        personalInfoHeader.setText("Personal Information");
        contactInfoHeader.setText("Emergency Contact");

        // Set contact data in student section
        phoneLabelStudent.setText(person.getPhone().value);
        emailLabelStudent.setText(person.getEmail().value);
        addressLabelStudent.setText(person.getAddress().value);
    }

    /**
     * Sets up the layout for non-student contacts with merged sections.
     */
    private void setupNonStudentLayout(Person person) {
        // Hide separate contact section
        contactInfoSection.setVisible(false);
        contactInfoSection.setManaged(false);

        // Show contact fields in personal info section
        contactFieldsForNonStudents.setVisible(true);
        contactFieldsForNonStudents.setManaged(true);

        // Update header to reflect merged information
        personalInfoHeader.setText("Contact Information");

        // Set contact data in personal info section
        phoneLabel.setText(person.getPhone().value);
        emailLabel.setText(person.getEmail().value);
        addressLabel.setText(person.getAddress().value);
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
        phoneLabelStudent.setText("");
        emailLabelStudent.setText("");
        addressLabelStudent.setText("");
        classLabel.setText("");
        birthdayLabel.setText("");
        tags.getChildren().clear();
        notesArea.setText("");
        attendancePanel.setAttendance(null);

        // Reset layout to default state
        contactInfoSection.setVisible(false);
        contactInfoSection.setManaged(false);
        contactFieldsForNonStudents.setVisible(false);
        contactFieldsForNonStudents.setManaged(false);
        attendanceSection.setVisible(false);
        attendanceSection.setManaged(false);

        personalInfoHeader.setText("Personal Information");

        attendancePanel.resetToCurrentMonth();
        getRoot().setTitle("View Contact");
    }
}
