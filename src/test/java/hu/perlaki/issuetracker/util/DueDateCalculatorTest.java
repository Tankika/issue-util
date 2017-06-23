package hu.perlaki.issuetracker.util;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

public class DueDateCalculatorTest {

    private DueDateCalculator dueDateCalculator;

    @Before
    public void setUp() {
        dueDateCalculator = new DueDateCalculator();
    }

    @Test
    public void testCalculateDueDate_OnlyWorkDays_WholeDaysTurnaround() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.JUNE, 20, 14, 12), // Tuesday
                expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 22, 14, 12);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, 16);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test
    public void testCalculateDueDate_OnlyWorkDays_PartialDaysTurnaround_WithoutHourRollover() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.JUNE, 20, 10, 7), // Tuesday
                expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 21, 13, 7);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, 11);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test
    public void testCalculateDueDate_OnlyWorkDays_PartialDaysTurnaround_WithHourRollover() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.JUNE, 20, 17, 0), // Tuesday
                expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 23, 12, 0);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, 19);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test
    public void testCalculateDueDate_WithNonWorkingDays_WholeDaysTurnaround() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.DECEMBER, 29, 9, 0), // Friday
                expectedDateTime = LocalDateTime.of(2018, Month.JANUARY, 2, 9, 0);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, 16);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test
    public void testCalculateDueDate_WithNonWorkingDays_PartialDays_WithoutHourRollover() {
        LocalDateTime submitDate = LocalDateTime.of(2024, Month.FEBRUARY, 28, 10, 35), // Wednesday, leap year
                expectedDateTime = LocalDateTime.of(2024, Month.MARCH, 4, 11, 35);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, 25);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test
    public void testCalculateDueDate_WithNonWorkingDays_PartialDays_WithHourRollover() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.JUNE, 23, 16, 52), // Friday
                expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 26, 15, 52);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, 7);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test
    public void testCalculateDueDate_MultipleWeeksTurnaround() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.JUNE, 23, 13, 49), // Friday
                expectedDateTime = LocalDateTime.of(2017, Month.JULY, 25, 16, 49);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, 179);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test
    public void testCalculateDueDate_MultipleWeeksTurnaround_LeapYear_YearEnd() {
        LocalDateTime submitDate = LocalDateTime.of(2019, Month.DECEMBER, 30, 12, 11), // Monday
                expectedDateTime = LocalDateTime.of(2020, Month.JUNE, 24, 11, 11);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, 1015);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test
    public void testCalculateDueDate_HighestTurnaroundTime() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.JUNE, 23, 15, 10), // Friday
                expectedDateTime = LocalDateTime.of(1030949, Month.APRIL, 28, 14, 10);

        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDate, Integer.MAX_VALUE);

        assertEquals(expectedDateTime, dueDateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingDayValidation_OutOfIntervalLowerBoundary() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.JUNE, 24, 10, 0); // Saturday
        dueDateCalculator.CalculateDueDate(submitDate, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingDayValidation_OutOfIntervalHigherBoundary() {
        LocalDateTime submitDate = LocalDateTime.of(2017, Month.JUNE, 25, 10, 0); // Sunday
        dueDateCalculator.CalculateDueDate(submitDate, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingHourValidation_OutOfIntervalLowerBoundary() {
        dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 17, 1), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingHourValidation_OutOfIntervalHigherBoundary() {
        dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 8, 59), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingHourValidation_OutOfInterval() {
        dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 23, 59), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDueDate_TurnaroundTimeValidation_NonPositive() {
        dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 13, 0), -1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDueDate_TurnaroundTimeValidation_NonPositiveHigherBoundary() {
        dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 13, 0), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDueDate_TurnaroundTimeValidation_NonPositiveLowerBoundary() {
        dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 13, 0), Integer.MIN_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void testCalculateDueDate_NullDate() {
        dueDateCalculator.CalculateDueDate(null, 2);
    }

    @Test(expected = DateTimeException.class)
    public void testCalculateDueDate_DateTooHigh() {
        LocalDateTime submitDate = LocalDateTime.of(LocalDate.MAX, LocalTime.of(17, 0));
        dueDateCalculator.CalculateDueDate(submitDate, 24);
    }
}
