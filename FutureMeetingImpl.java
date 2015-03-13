import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl implements FutureMeeting { 
  private int id;
  private Calendar date;
  private Set<Contact> contacts;

  public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) { 
    this.id = id;
    this.date = date;
    this.contacts = contacts;
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
