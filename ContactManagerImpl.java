import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
//import com.google.gson.Gson;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ContactManagerImpl implements ContactManager { 

  //private static final String textfile = "contacts.txt";
  private ContactManagerUtil util = new ContactManagerUtilImpl();
  private String textfile = "contacts.txt";
  private static final int FIRSTCONTACTID = 100;
  private int nextContactId = FIRSTCONTACTID;
  private static final int FIRSTMTGID = 100;
  private int nextMtgId = FIRSTMTGID;
  //private Set<Contact> contacts = new HashSet<Contact>();
  private Set<Contact> contacts;
  private List<Meeting> meetings;

  public ContactManagerImpl() {
    contacts = new HashSet<Contact>();
    meetings = new ArrayList<Meeting>();
  }

/*
  public ContactManagerImpl() {
    //Set<Contact> contacts = new HashSet<Contact>();
    contacts = new HashSet<Contact>();
    Set<Contact> fileContacts = readInTextfile(textfile);
    if (fileContacts != null)
      contacts.addAll(fileContacts);
  }

  public ContactManagerImpl(String filename) {
    textfile = filename;
    contacts = new HashSet<Contact>();
    Set<Contact> fileContacts = readInTextfile(textfile);
    if (fileContacts != null)
      contacts.addAll(fileContacts);
  }
*/

/*
  private Set<Contact> readInTextfile(String filename) { 
    System.out.println("READING IN " + filename);
    Gson gson = new Gson();
    Set<Contact> result = new HashSet<Contact>();;
    if (new File(filename).exists()) { 
      try {
        BufferedReader br = new BufferedReader(
        new FileReader(filename));
        result.addAll(gson.fromJson(br, HashSet.class));
      } catch (IOException e) {
        e.printStackTrace();
      } 
    }
    return result;
  }
*/


  public int addFutureMeeting(Set<Contact> sc, Calendar date) {
    Calendar rightNow = Calendar.getInstance();
    if (date.before(rightNow)) {
      throw new IllegalArgumentException("Date must be in the future.");
    }
    if (!allContactsExist(sc)) {
      throw new IllegalArgumentException("Nonexistent contact.");
    }
    int id = nextMtgId++;
    FutureMeeting fm = new FutureMeetingImpl(id, date, sc);
    meetings.add(fm);
    return id;
  }

  private boolean allContactsExist(Set<Contact> sc) {
    boolean result = true;
    for (Contact c: sc) {
      result = contacts.contains(c);
      if (result == false) {
        break;
      }
    }
    return result;
  }


  public PastMeeting getPastMeeting(int id) {
    //return null;
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
    if (util.isFuture(m.getDate()))
      result = (FutureMeeting) m; 
    else
      throw new IllegalArgumentException("Meeting " + id + " is in the past.");
    return result;
	}


  public Meeting getMeeting(int id) {
    Meeting result = null;
    Iterator<Meeting> i = meetings.iterator();
    while (i.hasNext()) { 
      Meeting m = i.next();
      if (m.getId() == id) {
        result = m;
        break;
      }
    }
    return result;
	}

  public List<Meeting> getFutureMeetingList(Contact contact) {
    //return null;
    if (!contacts.contains(contact))
      throw new IllegalArgumentException("Contact doesn't exist.");
    List<Meeting> result = new ArrayList<Meeting>();
    Iterator<Meeting> i = meetings.iterator();
    while (i.hasNext()) { 
      Meeting m = i.next();
      if (m.getContacts().contains(contact)) 
        result.add(m);
    }
    return result;
	}


  public List<Meeting> getFutureMeetingList(Calendar date) {
    //return null;
    List<Meeting> result = new ArrayList<Meeting>();
    Iterator<Meeting> i = meetings.iterator();
    while (i.hasNext()) { 
      Meeting m = i.next();
      if (util.areSameDay(date,m.getDate())) 
        result.add(m);
    }
    return result;
	}

  public List<PastMeeting> getPastMeetingList(Contact contact) {
    //return null;
    if (!contacts.contains(contact))
      throw new IllegalArgumentException("Contact doesn't exist.");
    List<PastMeeting> result = new ArrayList<PastMeeting>();
    Iterator<Meeting> i = meetings.iterator();
    while (i.hasNext()) { 
      Meeting m = i.next();
      if (util.isPast(m.getDate()) && m.getContacts().contains(contact)) 
        result.add((PastMeeting)m);
    }
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
    int id = nextMtgId++;
    PastMeeting pm = new PastMeetingImpl(id, date, sc, text);
    meetings.add(pm);
	}

  public void addMeetingNotes(int id, String text) {
    if (text == null)
      throw new NullPointerException("Notes added to meeting can't be null.");
    Meeting m = null;
    try {
      m = getPastMeeting(id); 
      if (m == null) { 
        throw new IllegalArgumentException("Can't add notes to a nonexistent meeting.");
      }
    } catch (IllegalArgumentException ex) {
      throw new IllegalStateException("Can't add notes to a future meeting.");
    }
    Meeting newMtg = new PastMeetingImpl(id, m.getDate(), m.getContacts(), text);   
    m = newMtg;
	}

  public void addNewContact(String name, String notes) {
    contacts.add(new ContactImpl(name, notes, nextContactId++));
	}

  public Set<Contact> getContacts(int... ids) {
    //return null;
    Set<Contact> result = new HashSet<Contact>();
    for (int argId : ids) {
      boolean foundId = false;
      for (Contact c : contacts) { // TODO: O(n^2) ... need to redo.. memoize?
        if (c.getId() == argId) {
          result.add(c);
          foundId = true;
        }
      }
      if (foundId == false) {
        throw new IllegalArgumentException("Could not find requested ID " + argId);
      }
    }
    return result;

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
    // save the object to file
    FileOutputStream fos = null;
    ObjectOutputStream out = null;
    try {
      fos = new FileOutputStream(textfile);
      out = new ObjectOutputStream(fos);
      out.writeObject(this);

      out.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

/*
  public void flush() {
    Gson gson = new Gson();
    String jsonContacts = gson.toJson(contacts);
    try {
      //FileWriter writer = new FileWriter("contacts.txt");
      FileWriter writer = new FileWriter(textfile);
      writer.write(jsonContacts);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(jsonContacts);
	}
*/

}
