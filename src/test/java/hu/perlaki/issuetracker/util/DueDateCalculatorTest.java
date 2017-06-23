package hu.perlaki.issuetracker.util;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DueDateCalculatorTest
{

	private DueDateCalculator dueDateCalculator;
	
	@Before
	public void setUp() {
		dueDateCalculator = new DueDateCalculator();
	}
	
	@Test
    public void testCalculateDueDate_OnlyWorkDays_WholeDaysTurnaround()
    {
        LocalDateTime submitDateTime = LocalDateTime.of(2017, Month.JUNE, 20, 14, 12), // Tuesday
        		expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 22, 14, 12);
        
        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDateTime, 16);
        
        assertEquals(expectedDateTime, dueDateTime);
    }
	
	@Test
	public void testCalculateDueDate_OnlyWorkDays_PartialDaysTurnaround_WithoutHourRollover()
    {
        LocalDateTime submitDateTime = LocalDateTime.of(2017, Month.JUNE, 20, 10, 7), // Tuesday
        		expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 21, 13, 7);
        
        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDateTime, 11);
        
        assertEquals(expectedDateTime, dueDateTime);
    }
	
	@Test
	public void testCalculateDueDate_OnlyWorkDays_PartialDaysTurnaround_WithHourRollover()
    {
        LocalDateTime submitDateTime = LocalDateTime.of(2017, Month.JUNE, 20, 17, 0), // Tuesday
        		expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 23, 12, 0);
        
        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDateTime, 19);
        
        assertEquals(expectedDateTime, dueDateTime);
    }
	
	@Test
    public void testCalculateDueDate_WithNonWorkingDays_WholeDaysTurnaround()
    {
        LocalDateTime submitDateTime = LocalDateTime.of(2017, Month.DECEMBER, 29, 9, 0), // Friday
        		expectedDateTime = LocalDateTime.of(2018, Month.JANUARY, 2, 9, 0);
        
        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDateTime, 16);
        
        assertEquals(expectedDateTime, dueDateTime);
    }
	
	@Test
	public void testCalculateDueDate_WithNonWorkingDays_PartialDays_WithoutHourRollover()
    {
        LocalDateTime submitDateTime = LocalDateTime.of(2024, Month.FEBRUARY, 28, 10, 35), // Wednesday, in a leap year
        		expectedDateTime = LocalDateTime.of(2024, Month.MARCH, 4, 11, 35);
        
        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDateTime, 25);
        
        assertEquals(expectedDateTime, dueDateTime);
    }
	
	@Test
	public void testCalculateDueDate_WithNonWorkingDays_PartialDays_WithHourRollover()
    {
        LocalDateTime submitDateTime = LocalDateTime.of(2017, Month.JUNE, 23, 16, 52), // Friday
        		expectedDateTime = LocalDateTime.of(2017, Month.JUNE, 26, 15, 52);
        
        LocalDateTime dueDateTime = dueDateCalculator.CalculateDueDate(submitDateTime, 7);
        
        assertEquals(expectedDateTime, dueDateTime);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingDayValidation_OutOfIntervalLowerBoundary()
    {
        LocalDateTime submitDateTime = LocalDateTime.of(2017, Month.JUNE, 24, 10, 0); // Saturday
		dueDateCalculator.CalculateDueDate(submitDateTime, 1);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingDayValidation_OutOfIntervalHigherBoundary()
    {
        LocalDateTime submitDateTime = LocalDateTime.of(2017, Month.JUNE, 25, 10, 0); // Sunday
		dueDateCalculator.CalculateDueDate(submitDateTime, 1);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingHourValidation_OutOfIntervalLowerBoundary()
    {
		dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 17, 1), 1);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingHourValidation_OutOfIntervalHigherBoundary()
    {
		dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 8, 59), 1);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testCalculateDueDate_IsWorkingHourValidation_OutOfInterval()
    {
		dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 23, 59), 1);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testCalculateDueDate_TurnaroundTimeValidation_NonPositiveBoundary()
    {
		dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 13, 0), -1000);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testCalculateDueDate_TurnaroundTimeValidation_NonPositiveHigherBoundary()
    {
		dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 13, 0), -1);
    }
	
	@Test(expected=IllegalArgumentException.class)
    public void testCalculateDueDate_TurnaroundTimeValidation_NonPositiveLowerBoundary()
    {
		dueDateCalculator.CalculateDueDate(LocalDateTime.of(2017, Month.JUNE, 21, 13, 0), Integer.MIN_VALUE);
    }
	
	@Test(expected=NullPointerException.class)
    public void testCalculateDueDate_NullDate()
    {
		dueDateCalculator.CalculateDueDate(null, 2);
    }
	
}
