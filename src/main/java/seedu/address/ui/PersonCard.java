package seedu.address.ui;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.Main;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;


/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static Logger logger = LogsCenter.getLogger(Main.class);

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label studentClass;
    @FXML
    private Label note;
    @FXML
    private FlowPane tags;
    @FXML
    private ImageView icon;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        studentClass.setText(person.getStudentClass().value);
        note.setText(person.getNote().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        Image imageIcon = new Image(this.getClass().getResourceAsStream("/images/star.png"));

        if (this.getClass().getResourceAsStream("/images/star.png") == null) {
            logger.warning("Image path was not found for star icon");
        } else {
            logger.info("Image path for star icon is valid!");
        }

        icon.setImage(imageIcon);
        if (person.getIsFavBoolean()) {
            //if indeed favourite, then display image
            icon.setVisible(true);
            logger.info("Manged to get favourite boolean true and set icon visibility");
        } else {
            icon.setVisible(false);
            logger.info("Favourite boolean was false for this person so icon visibility is false");
        }

    }
}
