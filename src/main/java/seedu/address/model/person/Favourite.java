package seedu.address.model.person;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents if a person is marked as favourite.
 */
public class Favourite {

    private BooleanProperty isFavourite;

    /**
     * Constructs a favourite indication.
     * @param value Can be true or false.
     */
    public Favourite(Boolean value) {

        this.isFavourite = new SimpleBooleanProperty(value);
    }

    /**
     * Updates the favourite status.
     * @param value Can be true or false. True means person is marked as favourite.
     */
    public void updateFavourite(Boolean value) {

        this.isFavourite.set(value);
    }

    /**
     * Gets the isFavourite attribute's boolean.
     * @return the isFavourite boolean value.
     */
    public boolean getIsFavouriteBoolean() {

        return this.isFavourite.get();
    }

    /**
     * Gets the favourite boolean property attribute.
     *
     * @return BooleanProperty.
     */
    public BooleanProperty getFavBooleanProperty() {
        return this.isFavourite;
    }
}
