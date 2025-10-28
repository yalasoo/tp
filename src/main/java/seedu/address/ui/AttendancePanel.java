package seedu.address.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.model.person.Attendance;

/**
 * Panel containing the attendance of a person.
 */
public class AttendancePanel extends UiPart<VBox> {

    private static final String FXML = "AttendancePanel.fxml";
    private static final String PRESENT_COLOR = "#4caf50";
    private static final String LATE_COLOR = "#ff9800";
    private static final String SICK_COLOR = "#2196f3";
    private static final String ABSENT_COLOR = "#f44336";

    private YearMonth currentDisplayMonth;
    private Attendance attendance;

    @FXML
    private Label monthLabel;
    @FXML
    private Label legendPresent;
    @FXML
    private Label legendLate;
    @FXML
    private Label legendSick;
    @FXML
    private Label legendAbsent;
    @FXML
    private Button prevMonthButton;
    @FXML
    private Button nextMonthButton;
    @FXML
    private HBox navigationBox;
    @FXML
    private GridPane attendanceGrid;

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
        setLegends();
        updateMonthLabel();

        prevMonthButton.setOnAction(e -> navigatePreviousMonth());
        nextMonthButton.setOnAction(e -> navigateNextMonth());
    }

    /**
     * Sets the legends of attendance labels.
     */
    private void setLegends() {
        Label[] labels = {legendPresent, legendLate, legendSick, legendAbsent};

        for (int i = 0; i < labels.length; i++) {
            Label currLegend = labels[i];

            currLegend.setPrefSize(Region.USE_COMPUTED_SIZE, 25);
            currLegend.setMaxWidth(Double.MAX_VALUE);

            if (currLegend == legendPresent) {
                currLegend.setStyle(getDayCellStyle("present") + "-fx-padding: 5px; -fx-font-size: 10px");
                currLegend.setText("PRESENT");
            } else if (currLegend == legendLate) {
                currLegend.setStyle(getDayCellStyle("late") + "-fx-padding: 5px; -fx-font-size: 10px");
                currLegend.setText("LATE");
            } else if (currLegend == legendSick) {
                currLegend.setStyle(getDayCellStyle("sick") + "-fx-padding: 5px; -fx-font-size: 10px");
                currLegend.setText("SICK");
            } else if (currLegend == legendAbsent) {
                currLegend.setStyle(getDayCellStyle("absent") + "-fx-padding: 5px; -fx-font-size: 10px");
                currLegend.setText("ABSENT");
            }
        }
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
    public void navigatePreviousMonth() {
        currentDisplayMonth = currentDisplayMonth.minusMonths(1);
        updateMonthLabel();
        updateAttendanceDisplay();
    }

    /**
     * Navigate to the next month.
     */
    public void navigateNextMonth() {
        currentDisplayMonth = currentDisplayMonth.plusMonths(1);
        updateMonthLabel();
        updateAttendanceDisplay();
    }

    /**
     * Updates the attendance display with colored day cells.
     */
    private void updateAttendanceDisplay() {
        if (attendance != null) {
            displayAttendanceAsGrid();
            attendanceGrid.setVisible(true);
        } else {
            attendanceGrid.setVisible(false);
        }
    }

    /**
     * Displays attendance as a grid of colored day cells.
     */
    private void displayAttendanceAsGrid() {
        // Clear existing day cells (keep header row)
        attendanceGrid.getChildren().removeIf(node ->
                GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        Map<LocalDate, AttendanceStatus> monthlyRecords = attendance.getAttendanceRecordsForMonth(currentDisplayMonth);

        LocalDate firstDay = currentDisplayMonth.atDay(1);
        int daysInMonth = currentDisplayMonth.lengthOfMonth();

        // Calculate the day of week for the first day (0 = Sunday, 1 = Monday, ..., 6 = Saturday)
        int firstDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Convert to Sunday-start week

        // Start from row 1 (after headers)
        int row = 1;
        int col = 0;

        // Add empty cells for days before the first day of the month
        for (int i = 0; i < firstDayOfWeek; i++) {
            Label emptyLabel = createEmptyDayLabel();
            attendanceGrid.add(emptyLabel, col, row);
            col++;
        }

        // Add cells for each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = currentDisplayMonth.atDay(day);
            AttendanceStatus status = monthlyRecords.get(currentDate);
            String statusString = (status != null) ? status.toString().toLowerCase() : "";

            Label dayLabel = createDayLabel(day, statusString);
            attendanceGrid.add(dayLabel, col, row);

            // Move to next column, next row if needed
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        attendanceGrid.setVisible(true);
    }

    /**
     * Creates an empty label for padding before the first day of the month.
     */
    private Label createEmptyDayLabel() {
        Label emptyLabel = new Label("");
        emptyLabel.setMaxWidth(Double.MAX_VALUE);
        emptyLabel.setPrefSize(40, 40);
        emptyLabel.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        GridPane.setHgrow(emptyLabel, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setVgrow(emptyLabel, javafx.scene.layout.Priority.ALWAYS);
        return emptyLabel;
    }

    /**
     * Creates a styled label for a day cell.
     */
    private Label createDayLabel(int day, String status) {
        Label label = new Label(String.valueOf(day));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPrefSize(40, 40);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setStyle(getDayCellStyle(status));

        // Make the label fill the available space in the grid cell
        GridPane.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setVgrow(label, javafx.scene.layout.Priority.ALWAYS);

        return label;
    }

    /**
     * Returns the CSS style for a day cell based on attendance status.
     */
    private String getDayCellStyle(String status) {
        String baseStyle = "-fx-border-color: #ddd; -fx-border-radius: 3; -fx-background-radius: 3; ";

        return switch (status.toLowerCase()) {
        case "present" -> baseStyle + String.format(
                "-fx-background-color: %s; -fx-text-fill: white;", PRESENT_COLOR);
        case "late" -> baseStyle + String.format(
                "-fx-background-color: %s; -fx-text-fill: white;", LATE_COLOR);
        case "sick" -> baseStyle + String.format(
                "-fx-background-color: %s; -fx-text-fill: white;", SICK_COLOR);
        case "absent" -> baseStyle + String.format(
                "-fx-background-color: %s; -fx-text-fill: white;", ABSENT_COLOR);
        default -> baseStyle + "-fx-background-color: #f5f5f5; -fx-text-fill: #666;";
        };
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
