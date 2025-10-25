package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** View information should be shown to user. */
    private final boolean showView;

    /** The application should exit. */
    private final boolean exit;

    /** Person to display for view command. */
    private final Person personToView;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean showView, boolean exit, Person personToView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.showView = showView;
        this.exit = exit;
        this.personToView = personToView;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false, null);
    }

    /**
     * Constructs a {@code CommandResult} with a person to show.
     */
    public CommandResult(String feedbackToUser, Person personToView) {
        this(feedbackToUser, false, true, false, personToView);
    }

    public boolean isShowPerson() {
        return personToView != null;
    }

    public Person getPersonToView() {
        return personToView;
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isShowView() {
        return showView;
    }

    public boolean isExit() {
        return exit;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && showView == otherCommandResult.showView
                && exit == otherCommandResult.exit
                && personToView == otherCommandResult.personToView;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, showView, exit, personToView);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("showView", showView)
                .add("exit", exit)
                .add("personToView", personToView)
                .toString();
    }
}
