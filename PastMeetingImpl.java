import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl implements PastMeeting { 
  private int id;
  private Calendar date;
  private Set<Contact> contacts;
  private String notes;

  public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes) {
    this.id = id;
    this.date = date;
    this.contacts = contacts;
    this.notes = notes;
  }

  public String getNotes() { 
    return notes;
  }

  public int getId() {
    return id;
  }

  public Calendar getDate() {
    return date;
  }

  public Set<Contact> getContacts() {
    return contacts;
  }
}
