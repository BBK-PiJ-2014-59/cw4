import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl implements FutureMeeting, Comparable<Meeting> { 
  private final static int firstId = 100; 
  private static int nextId = firstId;
  private int id;
  private Calendar date;
  private Set<Contact> contacts;

  // IDs are unique within class - GOOD
  public FutureMeetingImpl(Calendar date, Set<Contact> contacts) { 
    this.id = nextId++;
    this.date = date;
    this.contacts = contacts;
  }

  // IDs aren't unique within class - BAD
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

  @Override
  public int compareTo(Meeting m) { 
    return 0;
  }
}
