package seedu.address.ui;

import java.time.YearMonth;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Attendance;

/**
 * Panel containing the attendance of a person.
 */
public class AttendancePanel extends UiPart<VBox> {

    private static final String FXML = "AttendancePanel.fxml";
    private YearMonth currentDisplayMonth;
    private Attendance attendance;

    @FXML
    private Label monthLabel;
    @FXML
    private Label attendanceContent;
    @FXML
    private Button prevMonthButton;
    @FXML
    private Button nextMonthButton;
    @FXML
    private HBox navigationBox;

    /**
     * Creates a new AttendancePanel.
     */
    public AttendancePanel() {
        super(FXML);
        this.currentDisplayMonth = YearMonth.now();
        initialise();
    }

    /**
     * Initialises the panel components.
     */
    private void initialise() {
        updateMonthLabel();

        prevMonthButton.setOnAction(e -> navigatePreviousMonth());
        nextMonthButton.setOnAction(e -> navigateNextMonth());
    }

    /**
     * Sets the attendance data to display.
     * @param attendance attendance of the person being viewed.
     */
    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
        updateAttendanceDisplay();
    }

    /**
     * Updates the month label with current display month.
     */
    private void updateMonthLabel() {
        monthLabel.setText(currentDisplayMonth.format(java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy")));
    }

    /**
     * Navigate to the previous month.
     */
    private void navigatePreviousMonth() {
        currentDisplayMonth = currentDisplayMonth.minusMonths(1);
        updateMonthLabel();
        updateAttendanceDisplay();
    }

    /**
     * Navigate to the next month.
     */
    private void navigateNextMonth() {
        currentDisplayMonth = currentDisplayMonth.plusMonths(1);
        updateMonthLabel();
        updateAttendanceDisplay();
    }

    /**
     * Updates the attendance display content.
     */
    private void updateAttendanceDisplay() {
        if (attendance != null) {
            attendanceContent.setText(attendance.formatAttendanceRecordsForMonth(currentDisplayMonth));
        } else {
            attendanceContent.setText("No attendance data available");
        }
    }

    /**
     * Resets the display to current month.
     */
    public void resetToCurrentMonth() {
        this.currentDisplayMonth = YearMonth.now();
        updateMonthLabel();
        updateAttendanceDisplay();
    }
}
