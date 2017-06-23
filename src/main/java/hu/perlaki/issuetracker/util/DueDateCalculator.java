package hu.perlaki.issuetracker.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class DueDateCalculator {

    public static final DayOfWeek WORK_DAYS_START = DayOfWeek.MONDAY;
    public static final DayOfWeek WORK_DAYS_END = DayOfWeek.FRIDAY;
    public static final LocalTime WORK_HOURS_START = LocalTime.of(9, 0);
    public static final LocalTime WORK_HOURS_END = LocalTime.of(17, 0);
    public static final int WORK_HOURS_LENGTH = Math
            .toIntExact(ChronoUnit.HOURS.between(WORK_HOURS_START, WORK_HOURS_END));

    public LocalDateTime CalculateDueDate(LocalDateTime submitDate, int turnaroundTime) {
        validateInputs(submitDate, turnaroundTime);

        LocalDateTime dueDate = LocalDateTime.from(submitDate);

        dueDate = increaseDateWithWholeDays(dueDate, turnaroundTime);
        dueDate = increaseDateWithRemainingTime(dueDate, turnaroundTime);

        return dueDate;
    }

    private void validateInputs(LocalDateTime submitDate, int turnaroundTime) {
        validateSubmitDate(submitDate);
        validateTurnaroundTime(turnaroundTime);
    }

    private void validateSubmitDate(LocalDateTime submitDate) {
        DayOfWeek submitDayOfWeek = submitDate.getDayOfWeek();
        LocalTime submitTime = submitDate.toLocalTime();

        if (!isWorkDay(submitDayOfWeek)) {
            throw new IllegalArgumentException(
                    String.format("Submit date must be on a work day. First work day is: %s, last work day is: %s",
                            WORK_DAYS_START, WORK_DAYS_END));
        } else if (submitTime.isBefore(WORK_HOURS_START) || submitTime.isAfter(WORK_HOURS_END)) {
            throw new IllegalArgumentException(
                    String.format("Submit time must be in a working hour. Work hours start: %s, work hours end: %s",
                            WORK_HOURS_START, WORK_HOURS_END));
        }
    }

    private void validateTurnaroundTime(int turnaroundTime) {
        if (turnaroundTime <= 0) {
            throw new IllegalArgumentException(String.format("Turnaround time must be positive"));
        }
    }

    private LocalDateTime increaseDateWithWholeDays(LocalDateTime date, int turnaroundTime) {
        int turnaroundDays = turnaroundTime / WORK_HOURS_LENGTH,
                turnaroundWeeks = turnaroundDays / 5,
                remainingDays = turnaroundDays % 5;

        date = date.plusWeeks(turnaroundWeeks);
        date = addWorkingDaysToDate(date, remainingDays);

        return date;
    }

    private LocalDateTime addWorkingDaysToDate(LocalDateTime date, int days) {
        for (int i = 0; i < days; i++) {
            date = date.plusDays(1);
            date = adjustToNextOrSameWorkingDay(date);
        }

        return date;
    }

    private LocalDateTime increaseDateWithRemainingTime(LocalDateTime date, int turnaroundTime) {
        int remainingTurnaroundMinutes = turnaroundTime % WORK_HOURS_LENGTH * 60,
                remainingMinutesOfDay = Math.toIntExact(ChronoUnit.MINUTES.between(date.toLocalTime(), WORK_HOURS_END));

        if (remainingTurnaroundMinutes <= remainingMinutesOfDay) {
            date = date.plusMinutes(remainingTurnaroundMinutes);
        } else {
            int rolloverMinutes = remainingTurnaroundMinutes - remainingMinutesOfDay;

            date = LocalDateTime.of(date.toLocalDate(), WORK_HOURS_START)
                        .plusDays(1)
                        .plusMinutes(rolloverMinutes);
            date = adjustToNextOrSameWorkingDay(date);
        }

        return date;
    }

    private LocalDateTime adjustToNextOrSameWorkingDay(LocalDateTime date) {
        if (!isWorkDay(date.getDayOfWeek())) {
            date = date.with(TemporalAdjusters.next(WORK_DAYS_START));
        }

        return date;
    }

    private boolean isWorkDay(DayOfWeek dayOfWeek) {
        return 0 <= dayOfWeek.compareTo(WORK_DAYS_START) && dayOfWeek.compareTo(WORK_DAYS_END) <= 0;
    }

}
