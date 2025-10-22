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

    // Number of days to look ahead for upcoming birthdays
    private static final int UPCOMING_DAYS = 7; // One week before will start reminding.

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        String reminderMessage = generateReminderMessage(model);
        return new CommandResult(reminderMessage);
    }

    /**
     * Generates the reminder message with people who have birthdays today and upcoming.
     */
    private String generateReminderMessage(Model model) {
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
     * Calculates days until next birthday.
     * Handles birthdays that have already occurred this year (calculates for next year).
     */
    private long calculateDaysUntilBirthday(LocalDate birthday, LocalDate today) {
        LocalDate nextBirthday = birthday.withYear(today.getYear());

        // If birthday already passed this year, use next year's birthday
        if (nextBirthday.isBefore(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        return ChronoUnit.DAYS.between(today, nextBirthday);
    }

    /**
     * Builds the formatted reminder string similar to your previous implementation.
     */
    private String buildReminderString(List<Person> todayBirthdays, List<Person> upcomingBirthdays) {
        StringBuilder message = new StringBuilder();

        // Today's birthdays section
        if (!todayBirthdays.isEmpty()) {
            message.append("Happy Birthday to these people today!\n\n");
            message.append(getFormattedPersonList(todayBirthdays, true));
            message.append("\n");
        } else {
            message.append(MESSAGE_NO_BIRTHDAYS_TODAY).append("\n\n");
        }

        // Upcoming birthdays section
        if (!upcomingBirthdays.isEmpty()) {
            message.append("Upcoming birthdays in the next ").append(UPCOMING_DAYS).append(" days:\n\n");
            message.append(getFormattedPersonList(upcomingBirthdays, false));
        } else if (todayBirthdays.isEmpty()) {
            return MESSAGE_NO_UPCOMING_BIRTHDAYS;
        }

        message.append("\nDon't forget to wish them happy birthday!");
        return message.toString();
    }

    /**
     * Formats a list of persons with numbering and tags, similar to your previous getStringReminders method.
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
                            entry.append(" (in ").append(daysUntil).append(" day").append(daysUntil == 1 ? "" : "s").append(")");
                        }
                    } else {
                        entry.append(" (TODAY!)");
                    }

                    return entry.toString();
                })
                .collect(Collectors.joining("\n"));
    }
}