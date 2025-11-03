package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.ui.InfoPopupHandler;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "LittleLogBook has been cleared!";
    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to clear all contacts?\nThis action is irreversible!";
    public static final String MESSAGE_INSTRUCTION = "Press ENTER to confirm\nor ESC to go back to main window.";
    private final InfoPopupHandler infoPopupHandler;

    public ClearCommand(InfoPopupHandler infoPopupHandler) {
        this.infoPopupHandler = infoPopupHandler;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        infoPopupHandler.showMessage(MESSAGE_CONFIRMATION, MESSAGE_INSTRUCTION);
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
