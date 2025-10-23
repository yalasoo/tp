package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FavouriteTest {

    @Test
    public void constructor_initialisesCorrectly() {
        Favourite favourite = new Favourite(true);
        assertTrue(favourite.getIsFavouriteBoolean());

        favourite = new Favourite(false);
        assertFalse(favourite.getIsFavouriteBoolean());
    }

    @Test
    public void differentInstances_initialisesCorrectly() {
        Favourite favouriteOne = new Favourite(true);
        assertTrue(favouriteOne.getIsFavouriteBoolean());

        Favourite favouriteTwo = new Favourite(false);
        assertFalse(favouriteTwo.getIsFavouriteBoolean());
    }

}
