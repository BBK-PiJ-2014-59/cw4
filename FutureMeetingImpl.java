import java.util.Calendar;
import java.util.Set;

/**
 * A class to represent meetings
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts
 */
public class FutureMeetingImpl implements FutureMeeting { 
  private int id;
  private Calendar date;
  private Set<Contact> contacts;

  public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) { 
    this.id = id;
    this.date = date;
    this.contacts = contacts;
  }

  /**
   * Returns the id of the meeting.
   *
   * @return the id of the meeting.
   */
  public int getId() { 
    return id;
  }

  /**
   * Return the date of the meeting.
   *
   * @return the date of the meeting.
   */
  public Calendar getDate() { 
    return date;
  }

  /**
   * Return the details of people that attended the meeting.
   *
   * The list contains a minimum of one contact (if there were
   * just two people: the user and the contact) and may contain an
   * arbitraty number of them.
   *
   * @return the details of people that attended the meeting.
   */
  public Set<Contact> getContacts() { 
    return contacts;
  }
}
