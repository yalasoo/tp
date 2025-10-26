package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.Main;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;


/**
 * Indicate contacts as favourites.
 */
public class FavouriteCommand extends Command {

    public static final String COMMAND_WORD = "fav";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Favourite frequently contacted contacts based on index.\n"
            + "Parameters: fav indexes \n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_FAVOURITE_UPDATE_SUCCESS = "Updated favourites successfully";

    /** The arraylist storing indexes of all the contacts that have been indicated as favourite */
    private static ArrayList<Index> favourites = new ArrayList<>();

    private static Logger logger = LogsCenter.getLogger(Main.class);

    /** To refer to the indexes the command is being called on */
    private List<Index> vals;

    /**
     * Updates the arraylist of favourites.
     * If vals is already in favourites then calling fav command on it again
     * will remove it from favourites.
     *
     * @param vals The index values of contact to be added to favourites.
     */
    public FavouriteCommand(List<Index> vals) {
        requireNonNull(vals);
        this.vals = vals;
        for (Index p : vals) {
            if (favourites.contains(p)) {
                favourites.remove(p);
            } else {
                favourites.add(p);
            }
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> fullContactList = model.getFilteredPersonList();
        if (fullContactList.isEmpty()) {
            throw new CommandException("No contacts are available to be added to favourites.");
        }

        /* Validity of index would have been checked in FavouriteCommandParser (meaning all existing indexes
          in favourites are only valid ones) */
        for (Index i: favourites) {
            int zeroBasedIndex = i.getZeroBased();
            Person personToEdit = fullContactList.get(zeroBasedIndex);
            personToEdit.updateFavourite(true);
            logger.info("Person" + i + "isFavourite is set to true");
        }

        /* Case where user tried to favourite an index that was already in favourite:
          unfavourite that contact (the index would have been removed from favourite list,
          so the list should not contain it).
         */
        for (Index r: this.vals) {
            if (!favourites.contains(r)) {
                int zeroBasedIndex = r.getZeroBased();
                Person personToEdit = fullContactList.get(zeroBasedIndex);
                personToEdit.updateFavourite(false);
                logger.info("This person" + r + "was previously in favourites so we make isFavourite to false");
            }
        }

        return new CommandResult(MESSAGE_FAVOURITE_UPDATE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FavouriteCommand)) {
            return false;
        }

        FavouriteCommand e = (FavouriteCommand) other;
        return vals.equals(e.vals);
    }

}
