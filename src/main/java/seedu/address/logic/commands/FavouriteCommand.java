package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
            + ": Favourite chosen contacts based on index.\n"
            + "Parameters: INDEX(es) (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1,2";

    public static final String MESSAGE_FAVOURITE_UPDATE_SUCCESS = "Updated favourites successfully.";

    private static Logger logger = LogsCenter.getLogger(FavouriteCommand.class);

    /** The arraylist storing indexes of all the contacts that have been indicated as favourite */
    private ArrayList<Index> favourites = new ArrayList<>();

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
    }

    /**
     * Checks if the index user passed in is out of bounds.
     *
     * @param vals The List of indexes user passed in.
     * @param contactList The full list of all persons in addressBook.
     * @throws CommandException If index passed in is invalid.
     */
    public void checkOutOfBoundsIndex(List<Index> vals, List<Person> contactList) throws CommandException {
        int validLength = contactList.size();
        if (contactList.isEmpty()) {
            throw new CommandException("No contacts are available to be added to favourites.");
        }
        for (Index i: vals) {
            if (i.getOneBased() > validLength) {
                throw new CommandException("You have passed in out of bound index(es). \n"
                        + "Use only positive indexes within 1 to " + validLength + " inclusive!");
            }
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> fullContactList = model.getFilteredPersonList();
        checkOutOfBoundsIndex(vals, fullContactList);

        if (favourites.isEmpty()) {
            // Populates favourites arraylist based on each person's details
            favourites.addAll(model.retrieveInitialFavList());
        }

        for (Index p : vals) {
            if (favourites.contains(p)) {
                favourites.remove(p);
            } else {
                favourites.add(p);
            }
        }

        String infoOnAddedToFavourites = "";
        String infoOnRemovedFromFavourites = "";

        /* Validity of index has been checked above (meaning all existing indexes
          in favourites are only valid ones) */
        for (Index i: favourites) {
            int zeroBasedIndex = i.getZeroBased();
            Person personToEdit = fullContactList.get(zeroBasedIndex);
            personToEdit.updateFavourite(true);
            logger.info("Person" + i + "isFavourite is set to true");
        }

        /* Case where user tried to favourite an index that was already in favourite:
          unfavourite that contact (the index would have been removed from favourite list,
          so the list should not contain it). Also update the addedToFavourites (case where
          that index is in updated favourites).
         */
        for (Index r: this.vals) {
            int zeroBasedIndex = r.getZeroBased();
            Person person = fullContactList.get(zeroBasedIndex);
            if (!favourites.contains(r)) {
                person.updateFavourite(false);
                logger.info("This person" + r + "was previously in favourites so we make isFavourite to false");
                infoOnRemovedFromFavourites = infoOnRemovedFromFavourites.concat(person.getName() + "\n");
            } else {
                infoOnAddedToFavourites = infoOnAddedToFavourites.concat(person.getName() + "\n");
            }
        }

        return conditionBasedResult(infoOnRemovedFromFavourites, infoOnAddedToFavourites);

    }

    /**
     * Returns the commandResult for execute method based on the favourites conditions.
     *
     * @param infoOnRemoved The string with names of those removed from favourites.
     * @param infoOnAdded The string with names of those added to favourites.
     * @return CommandResult for execute method.
     */
    public CommandResult conditionBasedResult(String infoOnRemoved, String infoOnAdded) {
        if (infoOnRemoved.isEmpty()) {
            // If removed from is empty, then the indexes must have contributed to adding to favourites
            return new CommandResult(MESSAGE_FAVOURITE_UPDATE_SUCCESS
                    + "\nThese people were added to favourites: \n" + infoOnAdded);
        } else if (infoOnAdded.isEmpty()) {
            // Case where removed from is not empty but added to favourites is empty
            return new CommandResult(MESSAGE_FAVOURITE_UPDATE_SUCCESS
                    + "\nThese people were removed from favourites: \n" + infoOnRemoved);
        } else {
            // Case where neither is empty
            return new CommandResult(MESSAGE_FAVOURITE_UPDATE_SUCCESS
                    + "\nThese people were added to favourites: \n" + infoOnAdded
                    + "\nThese people were removed from favourites: \n" + infoOnRemoved);
        }
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
