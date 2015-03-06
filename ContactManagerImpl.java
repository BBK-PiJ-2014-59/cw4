import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

public class ContactManagerImpl implements ContactManager { 

  private static final int FIRSTCONTACTID = 100;
  private int nextContactId = FIRSTCONTACTID;

  //private List<Contact> contacts = new ArrayList<Contact>();
  private Set<Contact> contacts = new HashSet<Contact>();

  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    return 0;
	}

  public PastMeeting getPastMeeting(int id) {
    return null;
	}

  public FutureMeeting getFutureMeeting(int id) {
    return null;
	}

  public Meeting getMeeting(int id) {
    return null;
	}

  public List<Meeting> getFutureMeetingList(Contact contact) {
    return null;
	}

  public List<Meeting> getFutureMeetingList(Calendar date) {
    return null;
	}

  public List<PastMeeting> getPastMeetingList(Contact contact) {
    return null;
	}

  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
	}

  public void addMeetingNotes(int id, String text) {
	}

  public void addNewContact(String name, String notes) {
    System.out.println("yoda");
    contacts.add(new ContactImpl(name, notes, nextContactId++));
	}

  public Set<Contact> getContacts(int... ids) {
    return null;
	}

  public Set<Contact> getContacts(String name) {
    //return null;
    //return contacts;
    Set<Contact> result = new HashSet<Contact>();
    Iterator<Contact> i = contacts.iterator();
    Contact c = null;
    while (i.hasNext()) { 
      c = i.next();
      if (c.getName().contains(name)) 
        result.add(c);
    }
    return result;
	}

  public void flush() {
	}

}
