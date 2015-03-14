import java.util.Calendar;

/**
  * Some utilities for ContactManager.
  */
public class ContactManagerUtilImpl implements ContactManagerUtil { 

  /**
    * Returns true if date is in the future, otherwise false.
    *
    * @param date Date to consider.
    * @returns true if date is in the future, otherwise false.
    */
  public boolean isFuture(Calendar date) { 
    Calendar now = Calendar.getInstance();
    return date.after(now);
  }

  /**
    * Returns true if date is in the past, otherwise false.
    *
    * @param date Date to consider.
    * @returns true if date is in the past, otherwise false.
    */
  public boolean isPast(Calendar date) { 
    Calendar now = Calendar.getInstance();
    return date.before(now);
  }

}
