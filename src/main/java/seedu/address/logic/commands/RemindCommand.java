package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Shows birthday reminders for upcoming birthdays.
 */
public class RemindCommand extends Command {

    public static final String COMMAND_WORD = "remind";

    public static final String MESSAGE_SUCCESS = "Birthday reminders displayed.";
    public static final String MESSAGE_NO_UPCOMING_BIRTHDAYS = "No upcoming birthdays found.";
    public static final String MESSAGE_NO_BIRTHDAYS_TODAY = "No birthdays today!";
    public static final String MESSAGE_NO_PERSONS = "No contacts in LittleLogBook.";

    // Number of days to look ahead for upcoming birthdays
    private static final int UPCOMING_DAYS = 7; // One week before will start reminding.

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        String reminderMessage = generateReminderMessage(model);
        return new CommandResult(reminderMessage);
    }

    /**
     * Generates a reminder message listing people who have birthdays today or within the next {@code UPCOMING_DAYS}.
     *
     * @param model The model containing the filtered list of persons.
     * @return A formatted reminder message string.
     */
    private String generateReminderMessage(Model model) {
        // Check if there's people in the addressbook
        if (model.getFilteredPersonList().isEmpty()) {
            return MESSAGE_NO_PERSONS;
        }

        LocalDate today = LocalDate.now();
        List<Person> todayBirthdays = new ArrayList<>();
        List<Person> upcomingBirthdays = new ArrayList<>();

        // Categorise birthdays
        for (Person person : model.getFilteredPersonList()) {
            LocalDate birthday = person.getBirthday().date;
            long daysUntilBirthday = calculateDaysUntilBirthday(birthday, today);

            if (daysUntilBirthday == 0) {
                todayBirthdays.add(person);
            } else if (daysUntilBirthday > 0 && daysUntilBirthday <= UPCOMING_DAYS) {
                upcomingBirthdays.add(person);
            }
        }

        return buildReminderString(todayBirthdays, upcomingBirthdays);
    }

    /**
     * Calculates the number of days from {@code today} until the next occurrence of {@code birthday}.
     * If the birthday has already occurred this year, the calculation is based on the birthday in the next year.
     *
     * @param birthday The birthday date to check.
     * @param today The current date.
     * @return The number of days until the next birthday.
     */
    protected long calculateDaysUntilBirthday(LocalDate birthday, LocalDate today) {
        // Normalise birthday's month/day to this year
        int year = today.getYear();
        LocalDate nextBirthday;
        try {
            nextBirthday = LocalDate.of(year, birthday.getMonth(), birthday.getDayOfMonth());
        } catch (java.time.DateTimeException e) {
            // Handles Feb 29 on non-leap years: choose Feb 28 (common convention)
            nextBirthday = LocalDate.of(year, 2, 28);
        }

        if (nextBirthday.isBefore(today)) {
            // birthday already passed this year -> next year
            try {
                nextBirthday = LocalDate.of(year + 1, birthday.getMonth(), birthday.getDayOfMonth());
            } catch (java.time.DateTimeException e) {
                // Feb 29 case -> use Feb 28 next year (or adjust as preferred)
                nextBirthday = LocalDate.of(year + 1, 2, 28);
            }
        }

        return ChronoUnit.DAYS.between(today, nextBirthday);
    }

    /**
     * Builds the formatted reminder message string containing today's and upcoming birthdays.
     *
     * @param todayBirthdays List of persons whose birthday is today.
     * @param upcomingBirthdays List of persons whose birthday is within {@code UPCOMING_DAYS}.
     * @return A formatted reminder message string.
     */
    private String buildReminderString(List<Person> todayBirthdays, List<Person> upcomingBirthdays) {
        StringBuilder message = new StringBuilder();

        // Today's birthdays section
        if (!todayBirthdays.isEmpty()) {
            message.append("Happy Birthday to these people today!\n");
            message.append(getFormattedPersonList(todayBirthdays, true));
            message.append("\n");
        } else {
            message.append(MESSAGE_NO_BIRTHDAYS_TODAY).append("\n\n");
        }

        // Upcoming birthdays section
        if (!upcomingBirthdays.isEmpty()) {
            if (!todayBirthdays.isEmpty()) {
                message.append("\n");
            }
            message.append("Upcoming birthdays in the next ").append(UPCOMING_DAYS).append(" days:\n");
            message.append(getFormattedPersonList(upcomingBirthdays, false));
        } else if (todayBirthdays.isEmpty()) {
            return MESSAGE_NO_UPCOMING_BIRTHDAYS;
        }

        message.append("\n\nDon't forget to wish them happy birthday!");
        return message.toString();
    }

    /**
     * Formats a list of persons into a numbered, readable list for display in the reminder message.
     * Each entry includes the person's name, birthday date, and optional tags.
     * If {@code isTodayList} is false, the number of days until the person's birthday is also shown.
     *
     * @param persons The list of persons to format.
     * @param isTodayList Whether the list represents people with birthdays today.
     * @return A formatted string representing the list of persons.
     */
    private String getFormattedPersonList(List<Person> persons, boolean isTodayList) {
        if (persons.isEmpty()) {
            return "";
        }

        return IntStream.range(0, persons.size())
                .mapToObj(i -> {
                    Person person = persons.get(i);
                    StringBuilder entry = new StringBuilder();
                    entry.append(i + 1).append(") ").append(person.getName().toString());

                    // Add birthday date
                    entry.append(" - ").append(person.getBirthday().toString());

                    // Add tags if available
                    if (!person.getTags().isEmpty()) {
                        entry.append(" [");
                        entry.append(person.getTags().stream()
                                .map(tag -> tag.tagName)
                                .collect(Collectors.joining(", ")));
                        entry.append("]");
                    }

                    // Add days until birthday for upcoming birthdays
                    if (!isTodayList) {
                        long daysUntil = calculateDaysUntilBirthday(person.getBirthday().date, LocalDate.now());
                        if (daysUntil > 0) {
                            entry.append(" (in ").append(daysUntil).append(" day")
                                    .append(daysUntil == 1 ? "" : "s").append(")");
                        }
                    } else {
                        entry.append(" (TODAY!)");
                    }

                    return entry.toString();
                })
                .collect(Collectors.joining("\n"));
    }
}
