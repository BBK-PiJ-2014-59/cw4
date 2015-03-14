import java.util.Calendar;

/**
 * Some utilities for ContactManager
 */
public interface ContactManagerUtil { 

  /**
    * Returns whether date is in the future.
    *
    * @param date to consider. 
    * @return whether date is in the future.
    */
  boolean isFuture(Calendar date);

  /**
    * Returns whether date is in the past.
    *
    * @param date to consider. 
    * @return whether date is in the past.
    */
  boolean isPast(Calendar date);

  /**
    * Returns whether two Calendar dates are on the same day.
    *
    * @param date1 a date. 
    * @param date2 another date. 
    * @return whether they're on the same day.
    */
  boolean areSameDay(Calendar date1, Calendar date2);
}
