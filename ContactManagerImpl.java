import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ContactManagerImpl implements ContactManager, Serializable { 

  private ContactManagerUtil util = new ContactManagerUtilImpl();
  private String textfile = "contacts.txt";
  private HashMap<Integer, Contact> contacts = null;
  private HashMap<Integer, Meeting> meetings = null;

  public ContactManagerImpl() {
    if (textfile == null)
      throw new NullPointerException("Missing name of text file.");
    if (new File(textfile).exists()) {
      try { 
        FileInputStream fis = new FileInputStream(textfile);
        ObjectInputStream in = new ObjectInputStream(fis);
        contacts = (HashMap<Integer, Contact>) in.readObject();
        meetings = (HashMap<Integer, Meeting>) in.readObject();
        in.close();
      } catch (Exception ex) { 
        ex.printStackTrace();
      }
    } else {
      contacts = new HashMap<Integer, Contact>();
      meetings = new HashMap<Integer, Meeting>();
    }
  }

  public int addFutureMeeting(Set<Contact> sc, Calendar date) {
    Calendar rightNow = Calendar.getInstance();
    if (date.before(rightNow)) {
      throw new IllegalArgumentException("Date must be in the future.");
    }
    if (!allContactsExist(sc)) {
      throw new IllegalArgumentException("Nonexistent contact.");
    }
    FutureMeeting fm = new FutureMeetingImpl(date, sc);
    int id = fm.getId();
    meetings.put(id, fm);
    return id;
  }

  private boolean allContactsExist(Set<Contact> sc) {
    boolean result = true;
    for (Contact c: sc) {
      result = contacts.containsValue(c);
      if (result == false) {
        break;
      }
    }
    return result;
  }


  public PastMeeting getPastMeeting(int id) {
    PastMeeting result = null;
    Meeting m = getMeeting(id);
    if (m != null) { 
      if (util.isPast(m.getDate()))
        result = (PastMeeting) m; 
      else
        throw new IllegalArgumentException("Meeting " + id + " is in the future.");

    }
    return result;
	}

  public FutureMeeting getFutureMeeting(int id) {
    FutureMeeting result;
    Meeting m = getMeeting(id);
    if (m == null)
      result = null;
    else if (util.isFuture(m.getDate()))
      result = (FutureMeeting) m; 
    else
      throw new IllegalArgumentException("Meeting " + id + " is in the past.");
    return result;
	}


  public Meeting getMeeting(int id) {
    Meeting result = null;
    if (meetings.containsKey(id))
      result = meetings.get(id);
    return result;
	}

  public List<Meeting> getFutureMeetingList(Contact contact) {
    if (!contacts.containsValue(contact))
      throw new IllegalArgumentException("Contact doesn't exist.");
    List<Meeting> result = new ArrayList<Meeting>();
    for (Meeting m : meetings.values())
      if (m.getContacts().contains(contact)) 
        result.add(m);
    if (result.size() != 0)
      result = util.dedupeMeetingList(result);
      util.sortMeetingList(result);
    return result;
	}


  public List<Meeting> getFutureMeetingList(Calendar date) {
    List<Meeting> result = new ArrayList<Meeting>();
    for (Meeting m : meetings.values())
      if (util.areSameDay(date,m.getDate())) 
        result.add(m);
    if (result.size() != 0)
      result = util.dedupeMeetingList(result);
      util.sortMeetingList(result);
    return result;
	}

  public List<PastMeeting> getPastMeetingList(Contact contact) {
    if (!contacts.containsValue(contact))
      throw new IllegalArgumentException("Contact doesn't exist.");
    List<PastMeeting> result = new ArrayList<PastMeeting>();
    for (Meeting m : meetings.values())
      if (util.isPast(m.getDate()) && m.getContacts().contains(contact)) 
        result.add((PastMeeting)m);
    if (result.size() != 0)
      result = util.dedupeMeetingList(result);
      util.sortMeetingList(result);
    return result;
	}

  public void addNewPastMeeting(Set<Contact> sc, Calendar date, String text) {
    Calendar rightNow = Calendar.getInstance();
    if (date.after(rightNow)) {
      throw new IllegalArgumentException("Date must be in the past.");
    }
    if (!allContactsExist(sc) || sc.size() == 0) {
      throw new IllegalArgumentException("Nonexistent contact.");
    }
    if (contacts == null || date == null || text == null) { 
      throw new NullPointerException("null argument passed in.");
    }
    PastMeeting pm = new PastMeetingImpl(date, sc, text);
    meetings.put(pm.getId(), pm);
	}

  public void addMeetingNotes(int id, String text) {
    if (text == null)
      throw new NullPointerException("Notes text is null.");
    Meeting oldMtg = null;
    oldMtg = getMeeting(id); 
    if (oldMtg == null) { 
      throw new IllegalArgumentException("Meeting doesn't exist or hasn't happened yet.");
    }
    if (oldMtg.getDate().after(Calendar.getInstance())) 
      throw new IllegalStateException("Can't add notes to a future meeting.");
    Meeting newMtg = new PastMeetingImpl(oldMtg, text);   
    meetings.remove(oldMtg.getId());
    meetings.put(newMtg.getId(), newMtg);
	}

  public void addNewContact(String name, String notes) {
    Contact c = new ContactImpl(name, notes);
    contacts.put(c.getId(), c);
	}

  public Set<Contact> getContacts(int... ids) {
    Set<Contact> result = new HashSet<Contact>();
    for (int id : ids) 
      if (contacts.containsKey(id))
        result.add(contacts.get(id));
      else
        throw new IllegalArgumentException("Could not find requested ID " + id);
    return result;
  }

  public Set<Contact> getContacts(String name) {
    if (name == null)
      throw new NullPointerException("Contact name was null.");
    Set<Contact> result = new HashSet<Contact>();
    for (Contact c : contacts.values()) { 
      if (c.getName().contains(name)) 
        result.add(c);
    }
    return result;
	}

  public void flush() { 
    FileOutputStream fos = null;
    ObjectOutputStream out = null;
    try {
      fos = new FileOutputStream(textfile);
      out = new ObjectOutputStream(fos);
      out.writeObject(contacts);
      out.writeObject(meetings);
      out.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
