package seedu.address.model.person;

/**
 * Represents if a person is marked as favourite.
 */
public class Favourite {

    private boolean isFavourite;

    /**
     * Constructs a favourite indication.
     * @param value Can be true or false.
     */
    public Favourite(Boolean value) {
        this.isFavourite = value;
    }

    /**
     * Updates the favourite status.
     * @param value Can be true or false. True means person is marked as favourite.
     */
    public void updateFavourite(Boolean value) {
        this.isFavourite = value;
    }

    /**
     * Gets the isFavourite attribute.
     * @return the isFavourite boolean value.
     */
    public boolean getIsFavouriteBoolean() {
        return this.isFavourite;
    }
}
