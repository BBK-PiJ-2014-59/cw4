import java.util.Calendar;
import java.util.List;

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

  /**
    * Returns whether two meetings duplicate each other. 
    * Two meetings are duplicate if they happen on the same date, at the same 
    * time and involve the same contacts.
    *
    * @param m1 a meeting. 
    * @param m2 another meeting. 
    * @return whether they duplicate each other.
    */
  boolean meetingsAreDuplicate(Meeting m1, Meeting m2);

  /**
    * Deduplicates a list of meetings using the definition of duplicate in 
    * meetingsAreDuplicate(). For a given set of duplicates, only the one with the highest ID
    * is included in the returned list. 
    *
    * @param list a list of meetings. 
    * @return a list minus any duplicates. 
    * @throws NullPointerException if null list is passed.
    * @throws IllegalArgumentException if an empty list is passed.
    */
  <T extends Meeting> List<T> dedupeMeetingList(List<T> list);

  /**
    * Sorts a list of meetings by date.
    *
    * @param list a list of meetings. 
    * @return a sorted list of meetings. 
    * @throws NullPointerException if null list is passed.
    * @throws IllegalArgumentException if an empty list is passed.
    */
  <T extends Meeting> List<T> sortMeetingList(List<T> list);
}
