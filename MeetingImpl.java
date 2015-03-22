import java.util.Calendar;
import java.util.Set;

public class MeetingImpl implements Meeting { 

  private final static int firstId = 100; 
  private static int nextId = firstId;
  private int id;
  private Calendar date;
  private Set<Contact> contacts;
  private String notes;

  public MeetingImpl(Calendar date, Set<Contact> contacts) { 
    this.id = getNextId();
    this.date = date;
    this.contacts = contacts;
  }

  public MeetingImpl(Calendar date, Set<Contact> contacts, String notes) { 
    this.id = getNextId();
    this.date = date;
    this.contacts = contacts;
    this.notes = notes;
  }

  private int getNextId() { 
    return nextId++;
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
